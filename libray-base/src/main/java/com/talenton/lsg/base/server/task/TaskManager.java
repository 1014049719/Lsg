package com.talenton.lsg.base.server.task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.talenton.lsg.base.dao.TaskBeanDao.Properties;
import com.talenton.lsg.base.dao.model.TaskBean;
import com.talenton.lsg.base.server.DBHelper;
import com.talenton.lsg.base.util.SystemUtil;

import android.text.TextUtils;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 需要重发的写请求任务管理.
 */
public class TaskManager {
	final static long taskCacheTime = 7 *24* 3600;// 上传记录保留7天
	public static boolean mNetworkConnected, mWifiConnected;
	private static final int ID_FIXED_VALUE;

	/**
	 * the queue
	 */
	private volatile static TaskQueue mRequestQueue;

	static {
		ID_FIXED_VALUE = new Random().nextInt(1000);
	}

	/**
	 * 本地产生feedId
	 */
	public static long generateLocalFeedsId() {
		long id = System.currentTimeMillis();
		id -= id % 1000;
		id += ID_FIXED_VALUE;
		return -id;
	}

	public static void clear() {
		synchronized (TaskManager.class) {
			if (mRequestQueue != null) {
				mRequestQueue.stop();
			}
			mRequestQueue = null;
		}
	}

	public static void init(long uid) {
		if (uid <= 0) {
			clear();
			return;
		}
		synchronized (TaskManager.class) {
			if (uid > 0 && (mRequestQueue == null || mRequestQueue.mUid != uid)) {
				mRequestQueue = new TaskQueue(uid);
				mRequestQueue.start();
			}
		}
		updateNetworkStatus();
	}

	public static void doAllTask() {
		// 获取历史所有需要重试的任务，全部跑一遍
		List<TaskBean> tasks = DBHelper.getInstance().list("", DBHelper.DAO_TASK);
		for (TaskBean bean : tasks) {
			TaskBase task = TaskBase.getInstance(bean);
			if (task.status != TaskBase.STATUS_SUCCESS) {
				task.status = TaskBase.STATUS_READY;
				task.retrys = 0;// 重新开始任务
				addTask(task);
			}
		}
	}

	/**
	 * @param taskType 任务类别。空的话标识返回全部任务
	 * @return
	 */
	public static List<TaskBase> getAllTaskFromDB(String taskType) {
		List<TaskBean> tasks = DBHelper.getInstance().list("", DBHelper.DAO_TASK);
		List<TaskBase> res = new LinkedList<TaskBase>();
		for (TaskBean bean : tasks) {
			TaskBase task = TaskBase.getInstance(bean);

			if(TextUtils.isEmpty(taskType)||taskType.equals(task.type)){
				res.add(task);
			}
		}
		return res;
	}

	public static TaskBase getTaskFromDB(long taskId) {
		return TaskBase.getInstance((TaskBean) DBHelper.getInstance().load(DBHelper.DAO_TASK, taskId));
	}

	public static void deleteTaskFromDB(long taskId) {
		DBHelper.getInstance().delete(DBHelper.DAO_TASK, new TaskBase(taskId).generateTask(), true);
	}

	public static void deleteOldTask() {
		long deletePoint = System.currentTimeMillis() / 1000 - taskCacheTime;

		QueryBuilder<TaskBean> qb = DBHelper.getInstance().getQueryBuilder(DBHelper.DAO_TASK);
		qb.where(Properties.Status.eq(TaskBase.STATUS_SUCCESS), Properties.UpdateTime.lt(deletePoint));
		qb.buildDelete().executeDeleteWithoutDetachingEntities();
	}
	
	public static void updateNetworkStatus() {
		mWifiConnected = SystemUtil.isWifiConnected();
		mNetworkConnected = SystemUtil.isNetworkConnected();
		if (mRequestQueue != null && (mWifiConnected || mNetworkConnected)) {
			doAllTask();
		}
	}

	/**
	 * @param request
	 *            继续执行的任务
	 */
	public static void continueTask(TaskBase request) {
		if (mRequestQueue == null) {
			return;
		}
		mRequestQueue.continueTask(request);
	}

	public static void addTask(TaskBase request) {
		if (mRequestQueue == null) {
			return;
		}
		mRequestQueue.add(request);
	}

	public static void retryTask(TaskBase request) {
		if (mRequestQueue == null) {
			return;
		}
		mRequestQueue.retry(request);
	}

	public static void finishTask(TaskBase request) {
		if (mRequestQueue == null) {
			return;
		}
		mRequestQueue.finish(request);
	}

}
