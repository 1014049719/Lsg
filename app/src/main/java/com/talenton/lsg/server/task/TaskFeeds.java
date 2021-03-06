package com.talenton.lsg.server.task;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.dao.model.PhotoPathBean;
import com.talenton.lsg.base.dao.model.TaskBean;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.DBHelper;
import com.talenton.lsg.base.server.UploadCompletionHandler;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.task.TaskBase;
import com.talenton.lsg.base.server.task.TaskManager;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.BitmapUtil;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.event.FeedsTaskEvent;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.server.bean.feed.QNPicInfo;
import com.talenton.lsg.server.bean.feed.RspPostFeeds;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class TaskFeeds extends TaskBase {
	public Feeds feeds;
	public static String taskTpe;

	static {
		taskTpe = new Object() {
			public String getClassName() {
				String clazzName = this.getClass().getName();
				return clazzName.substring(0, clazzName.lastIndexOf('$'));
			}
		}.getClassName();
		AppLogger.i("TASK_TYPES.add(taskTpe) " + taskTpe);
	}

	public TaskFeeds() {
		super();
		type = taskTpe;
	}

	public TaskFeeds(Feeds feeds) {
		super(feeds.appLocalid);

		this.feeds = feeds;
		this.feeds.appLocalid = id;
		this.feeds.localid = String.valueOf(id);
		this.feeds.from = Feeds.FROM_TASK;
		type = taskTpe;
		onCache();// 把自己缓存起来
	}

	/**
	 * 把数据库的任务对象转换为本地对象
	 */
	public TaskFeeds(TaskBean taskBean) {
		super(taskBean);
	}

	public Priority getPriority() {
		return Priority.NORMAL;
	}

	public void setContent(String content) {
		feeds = gson.fromJson(content, Feeds.class);
	}

	public String getContent() {
		return gson.toJson(feeds, Feeds.class);
	}

	@Override
	public void onFinish(int status, String message) {
		// TODO Auto-generated method stub
		super.onFinish(status, message);
		EventBus.getDefault().post(new FeedsTaskEvent(TaskFeeds.this));
	}

	private void commitCollectFeeds() {
		/*
		FeedServer.commitCollectFeeds(feeds, new XLTResponseCallback<Feeds>() {

			@Override
			public void onResponse(Feeds data, XLTError error) {
				if (error != null) {
					// 本次操作失败，尝试重试
					onRetry(error);
				} else {
					// 替换成功后的数据
					feeds.from = Feeds.FROM_DB;
					feeds.guid = data.guid;
					// 发送成功的动态，直接插入本地数据库缓存起来
					DBHelper.getInstance().insertOrReplace(DBHelper.DAO_FEEDS, feeds.genFeedsBean(), false);// 插一条本地动态到数据库
					onFinish(STATUS_SUCCESS, "");
				}

			}

		});
		*/
	}

	// 执行本任务
	public void onExcute() {
		super.onExcute();
		EventBus.getDefault().post(new FeedsTaskEvent(this));
		if (feeds.fb_flag == Feeds.POST_COLLECT) {
			// 转发任务
			commitCollectFeeds();
			return;
		}

		if (feeds.isVideo()) {

			if (!uploadVideo())
				return;
		} else {

			if (!uploadImage())
				return;
		}

		// 上传动态
		FeedServer.commitFeeds(feeds, new XLTResponseCallback<RspPostFeeds>() {

			@Override
			public void onResponse(RspPostFeeds data, XLTError error) {
				if (error != null) {
					// 本次操作失败，尝试重试
					onRetry(error);
				} else {
					// 替换成功后的数据
					ArrayList<MediaBean> pics = feeds.attachinfo;
					if (pics != null && !pics.isEmpty()) {

						for (int i = 0; i < pics.size(); i++) {
							PhotoPathBean photoPathBean = new PhotoPathBean();
							photoPathBean.setPhotoPath(pics.get(i).orgpath);
							DBHelper.getInstance().insertOrReplace(DBHelper.DAO_PHOTO, photoPathBean, true);
						}
					}
					if (TextUtils.isEmpty(feeds.guid) && !TextUtils.isEmpty(data.guid)) {
						feeds.guid = data.guid;
					}
					if(feeds.tid == 0){
						feeds.tid = data.tid;
					}
					feeds.from = Feeds.FROM_DB;
					// 发送成功的动态，直接插入本地数据库缓存起来
					DBHelper.getInstance().insertOrReplace(DBHelper.DAO_FEEDS, feeds.genFeedsBean(), false);// 插一条本地动态到数据库
					onFinish(STATUS_SUCCESS, "");
				}

			}

		});

	}

	private boolean uploadImage() {

		// 上传图片
		ArrayList<MediaBean> pics = feeds.attachinfo;
		if (pics != null && pics.size() > 0) {
			boolean compressed = false;
			for (MediaBean pic : pics) {
				// 还需要压缩
				if (pic.remote == MediaBean.ADDR_LOCAL_ORG) {
					AppLogger.i("compressImage " + pic.orgpath);
					File dir = FileUtil.getExternalPath(LsgApplication.getAppContext(), FileUtil.PICTURE_DOC);
					File destFile = new File(dir, FileUtil.genUploadFileName(FileUtil.PICTURE_SUFFIX));
					Boolean res = BitmapUtil.compressImage(pic.orgpath, destFile);

					if (res) {// 压缩成功
						pic.filepath = destFile.getAbsolutePath();
					} else {
						pic.filepath = pic.orgpath;
					}
					pic.remote = MediaBean.ADDR_LOCAL;
					onCache();
					compressed = true;
				}
			}
			if (compressed) {
				EventBus.getDefault().post(new FeedsTaskEvent(TaskFeeds.this));
			}

			for (final MediaBean pic : pics) {
				if (pic.remote == MediaBean.ADDR_LOCAL) {
					// 上传图片
					final String path;
					if (feeds.isUseOriginal) {// 指定传原图
						path = pic.orgpath;
					} else {
						path = pic.filepath;
					}
					if ((!TaskManager.mWifiConnected && Preference.getInstance().isOnlyWifi())
							|| !TaskManager.mNetworkConnected) {
						onFinish(STATUS_FAILED, "网络环境异常，暂停上传");
						new Handler(LsgApplication.getAppContext().getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								XLTToast.makeText(LsgApplication.getAppContext(), R.string.main_post_failed_network_hint,
										Toast.LENGTH_LONG).show();
							}

						});
						return false;
					}
					FeedServer.upload(path, new UploadCompletionHandler(
						new UploadCompletionHandler.OnUpCompletionListener() {
							
							@Override
							public void error(int statusCode, ResponseInfo info) {
								// TODO Auto-generated method stub
								// 本次上传失败，但允许重试
								onRetry(new XLTError(info.statusCode, info.error));
								if (isFinish()) {
									XLTToast.makeText(LsgApplication.getAppContext(), info.error).show();
									CrashReport.postCatchedException(
											new Throwable(String.format("user=%s,upload qiniu,error=%s",
													UserServer.getCurrentUser().username, info.error)));
								}
							}
							
							@Override
							public void complete(String key, ResponseInfo info, JSONObject response) {
								// TODO Auto-generated method stub
								// 本图片上传成功
								QNPicInfo qnPic = OkHttpClientManager.getInstance().getmGson().fromJson(response.toString(),
										QNPicInfo.class);
								pic.remote = MediaBean.ADDR_QINIU;
								pic.filepath = MediaBean.genQNFullUrl(qnPic.key);
								// pic.orgpath = "";
								pic.width = qnPic.width;
								pic.height = qnPic.height;
								pic.size = qnPic.size;
								onCache();
								onContinue();
								File org = new File(path);
								if (org.exists() && !feeds.isUseOriginal) {
									// 删除临时的压缩图片文件
									org.delete();
								}
							}
						}));
					return false;
				}
			}
		}
		return true;
	}

	private boolean uploadVideo() {
		// 上传视频
		ArrayList<MediaBean> mbs = feeds.attachinfo;
		if (mbs != null && mbs.size() > 0) {
			for (final MediaBean mb : mbs) {

				final MediaBean pic = (mb.remote == MediaBean.ADDR_LOCAL) ? mb : mb.getCover();

				if (pic != null && pic.remote == MediaBean.ADDR_LOCAL) {
					// 上传视频
					final String path = pic.orgpath;

					if ((!TaskManager.mWifiConnected && Preference.getInstance().isOnlyWifi())
							|| !TaskManager.mNetworkConnected) {
						onFinish(STATUS_FAILED, "网络环境异常，暂停上传");
						new Handler(LsgApplication.getAppContext().getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								XLTToast.makeText(LsgApplication.getAppContext(), R.string.main_post_failed_network_hint,
										Toast.LENGTH_LONG).show();
							}

						});
						return false;
					}
					FeedServer.upload(path, new UploadCompletionHandler(
							new UploadCompletionHandler.OnUpCompletionListener() {
						
						@Override
						public void error(int statusCode, ResponseInfo info) {
							// TODO Auto-generated method stub
							// 本次上传失败，但允许重试
							onRetry(new XLTError(info.statusCode, info.error));
							if (isFinish()) {
								XLTToast.makeText(LsgApplication.getAppContext(), info.error).show();
								CrashReport.postCatchedException(
										new Throwable(String.format("user=%s,upload qiniu,error=%s",
												UserServer.getCurrentUser().username, info.error)));
							}
						}
						
						@Override
						public void complete(String key, ResponseInfo info, JSONObject response) {
							// TODO Auto-generated method stub
							// 视频上传成功
							QNPicInfo qnPic = OkHttpClientManager.getInstance().getmGson().fromJson(response.toString(),
									QNPicInfo.class);
							pic.remote = MediaBean.ADDR_QINIU;
							pic.filepath = MediaBean.genQNFullUrl(qnPic.key);
							if (pic.itype == MediaBean.TYPE_PIC)
								mb.setCover(pic.filepath, qnPic.width, qnPic.height);
							// pic.orgpath = "";
							pic.width = qnPic.width;
							pic.height = qnPic.height;
							pic.size = qnPic.size;
							onCache();
							onContinue();
						}
					}));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TaskFeeds [feeds=").append(feeds).append(", id=").append(id).append(", type=").append(type)
				.append(", status=").append(status).append(", retrys=").append(retrys).append(", message=")
				.append(message);
		ArrayList<MediaBean> pics = feeds.attachinfo;
		if (pics != null && pics.size() > 0) {
			builder.append(", images=[");
			for (final MediaBean pic : pics) {
				builder.append(pic.toString());
				builder.append(",");
			}
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}

}