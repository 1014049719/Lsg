package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;

import com.talenton.lsg.ui.user.JumpType;
import com.talenton.lsg.base.server.task.TaskBase;
import com.talenton.lsg.base.server.task.TaskManager;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.base.widget.CommentTextView;
import com.talenton.lsg.base.widget.CommonAlertDialog;
import com.talenton.lsg.event.SelectedDateEvent;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.server.bean.feed.CommentInfo;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.server.task.TaskFeeds;
import com.talenton.lsg.ui.feed.adapter.CircleDetailFeedsItemLayout;
import com.talenton.lsg.ui.feed.adapter.CircleFeedsItemLayout;
import com.talenton.lsg.ui.feed.adapter.FeedsItemLayout;
import com.talenton.lsg.R;
import com.talenton.lsg.ui.feed.adapter.TopicFeedsItemLayout;
import com.talenton.lsg.ui.user.BrowserActivity;
import com.talenton.lsg.ui.user.ShowPersonalInfoActivity;

import org.greenrobot.eventbus.EventBus;

public class OnFeedsClickListener extends CommentTextView.HighlightClickableSpan implements View.OnClickListener {
	private Context mContext;
	private PopupWindow mPopupWindow;
	private View mPopupView;
	private int mH, mDtype, mFeedsType;
	private long mCreuid, mClassId, mSchoolId;
	public String mGuid;
	private CommonAlertDialog mAlertDlg;
	public PostToParam mPostToParam;

	public OnFeedsClickListener(Context context, PostToParam postToParam) {
		mContext = context;
		mPostToParam = postToParam;
		/*
		mPopupView = LayoutInflater.from(context).inflate(R.layout.popup_feeds_op_menu, null);
		mPopupWindow = new PopupWindow(mPopupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
		mPopupView.findViewById(R.id.delete).setOnClickListener(this);
		mPopupView.findViewById(R.id.comment).setOnClickListener(this);
		mPopupView.findViewById(R.id.edit).setOnClickListener(this);
		mPopupView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		*/
	}

	public void onTopicFeedsItemLayout(TopicFeedsItemLayout container, View view){
		Feeds feeds = container.feeds;
		switch (view.getId()) {
			case R.id.tv_title:
			case R.id.layout_info:
			case R.id.feeds_container:
				if (feeds.ext_circle == null) {
					return;
				}
				if (feeds.from == Feeds.FROM_TASK) {
					if(feeds.ext_circle.circle_type == CircleListData.CIRCLE_TYPE_BABY) {
						FeedsDetailActivity.startFeedsTaskActivity(mContext, feeds.appLocalid);
					}else {
						CircleFeedsDetailActivity.startCircleFeedsDetailActivity(mContext, feeds.appLocalid);
					}
				} else {
					mPostToParam.tid = feeds.tid;
					mPostToParam.guid = feeds.guid;
					mPostToParam.count = feeds.browsecount;
					mPostToParam.name = feeds.circle_name;
					mPostToParam.circleType = feeds.ext_circle.circle_type;

					if(feeds.ext_circle.circle_type == CircleListData.CIRCLE_TYPE_BABY) {
						FeedsDetailActivity.startFeedsDetailActivity(mContext, mPostToParam);
					}else {
						CircleFeedsDetailActivity.startCircleFeedsDetailActivity(mContext, mPostToParam);
					}
				}
				break;
			case R.id.user_logo:
				ShowPersonalInfoActivity.startShowPersonalInfoActivity(mContext, feeds.creuid, feeds.crerealname, feeds.getCircleMember().avatar);
				break;
			default:
				onFeedsClick(container.feeds, view, false);
				break;
		}
	}

	public void onCircleFeedsClick(CircleFeedsItemLayout container, View view){
		Feeds feeds = container.feeds;
		switch (view.getId()){
			case R.id.tv_title:
			case R.id.layout_info:
			case R.id.feeds_container:
				if (feeds.from == Feeds.FROM_TASK) {
					CircleFeedsDetailActivity.startCircleFeedsDetailActivity(mContext, feeds.appLocalid);
				} else {
					mPostToParam.tid = feeds.tid;
					mPostToParam.guid = feeds.guid;
					mPostToParam.count = feeds.browsecount;
					mPostToParam.name = feeds.circle_name;
					CircleFeedsDetailActivity.startCircleFeedsDetailActivity(mContext, mPostToParam);
				}
				break;
			case R.id.user_logo:
				ShowPersonalInfoActivity.startShowPersonalInfoActivity(mContext, feeds.creuid, feeds.crerealname, feeds.getCircleMember().avatar);
				break;
			default:
				onFeedsClick(container.feeds, view, false);
				break;
		}
	}

	public void onCircleFeedsDetailClick(CircleDetailFeedsItemLayout container, View view){
		Feeds feeds = container.feeds;
		switch (view.getId()){
			case R.id.feeds_like:
				if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
					return;
				}
				int status = 0;
				if (feeds.ext_is_like == Feeds.FLAG_UN_LIKE){
					status = Feeds.FLAG_LIKE;
					feeds.ext_is_like = Feeds.FLAG_LIKE;
					feeds.likecount++;
				}else {
					feeds.ext_is_like = Feeds.FLAG_UN_LIKE;
					if (feeds.likecount > 0){
						feeds.likecount--;
					}
				}
				FeedServer.setLikeAndCollect(feeds.tid, status, null);
				if (mContext instanceof View.OnClickListener){
					((View.OnClickListener)mContext).onClick(view);
				}
				break;
			case R.id.feeds_collect:
				if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
					return;
				}
				int stat = Feeds.FLAG_UN_COLLECT;
				if (feeds.ext_is_collect == Feeds.FLAG_UN_LIKE){
					stat = Feeds.FLAG_COLLECT;
					feeds.ext_is_collect = Feeds.FLAG_LIKE;

				}else {
					feeds.ext_is_collect = Feeds.FLAG_UN_LIKE;
				}
				FeedServer.setLikeAndCollect(feeds.tid, stat, null);
				if (mContext instanceof View.OnClickListener){
					((View.OnClickListener)mContext).onClick(view);
				}
				break;
			case R.id.feeds_delete:
				if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
					return;
				}
				if (mContext instanceof View.OnClickListener){
					((View.OnClickListener)mContext).onClick(view);
				}
				break;
			case R.id.feeds_share:
				SocialActivity.startSocialActivity(mContext, feeds, SocialActivity.SOCIAL_ACTION_TOPIC);
				break;
			case R.id.user_logo:
				ShowPersonalInfoActivity.startShowPersonalInfoActivity(mContext, feeds.creuid, feeds.crerealname, feeds.getCircleMember().avatar);
				break;
			default:
				onFeedsClick(container.feeds, view, true);
				break;
		}
	}

	public void onFeedsClick(FeedsItemLayout container, View view) {
		Feeds feeds = container.feeds;
		switch (view.getId()){
			case R.id.feeds_operator:
				if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
					return;
				}
				if (feeds.from == Feeds.FROM_TASK) {
					XLTToast.makeText(mContext, R.string.feeds_ban_uploading).show();
					return;
				}
				//if (mPopupWindow == null) {
				//	return;
				//}
				int[] lm = new int[2];
				view.getLocationOnScreen(lm);
				int[] lp = new int[2];
				container.holder.mRoot.getLocationOnScreen(lp);
				mH = lm[1] - lp[1];
				//mDtype = feeds.circle_type;
				//mGuid = feeds.guid;
				//mCreuid = feeds.creuid;
				PostToParam param = new PostToParam();
				param.circleId = feeds.circle_id;
				param.tid = feeds.tid;
				param.guid = feeds.guid;
				param.name = null;
				param.height = mH;
				FeedsCommentActivity.startCommentActivity(mContext, param);
				//mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, lm[0] - mPopupView.getMeasuredWidth(), lm[1]);
				break;
			case R.id.feeds_delete:
				if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
					return;
				}
				if (mContext instanceof View.OnClickListener){
					((View.OnClickListener)mContext).onClick(view);
				}
				break;
			default:
				onFeedsClick(container.feeds, view, container.isDetailPage);
				break;
		}
	}

	public void onFeedsClick(Feeds feed, View view, boolean isDetailPage) {
		int id = view.getId();
		Feeds feeds = feed;

		if (id == R.id.feeds_container || id == R.id.layout_info) {
			if (isDetailPage) {
				return;
			}
			if (feeds.from == Feeds.FROM_TASK) {
				FeedsDetailActivity.startFeedsTaskActivity(mContext, feeds.appLocalid);
			} else {
				mPostToParam.tid = feeds.tid;
				mPostToParam.guid = feeds.guid;
				mPostToParam.count = feeds.browsecount;
				FeedsDetailActivity.startFeedsDetailActivity(mContext, mPostToParam);
			}
		} else if(id == R.id.feeds_delete){
			if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
				return;
			}
			if (FeedServer.checkDeleteFeedsAuthority(feeds, Feeds.ACTION_DELETE)) {
				int resId = R.string.alter_post_delete;
				final long localid = feeds.appLocalid;
				final int feedFrom = feeds.from;
				final String guid = feeds.guid;
				final long tid = feeds.tid;
				final long cirlceId = feeds.circle_id;

				alterDialog(resId, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if(feedFrom == Feeds.FROM_TASK){

							TaskFeeds task = (TaskFeeds) TaskManager.getTaskFromDB(localid);
							if(task != null){
								task.onFinish(TaskBase.STATUS_CANCELED, "");
							}

						}else{
							FeedServer.deleteFeeds(cirlceId, guid, tid, null);
						}
						mAlertDlg.dismiss();
					}
				});
			} else {
				XLTToast.makeText(mContext, R.string.feeds_ban_delete).show();
			}
		}
		else if(id == R.id.feeds_edit){
			if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
				return;
			}
			if(feeds.from == Feeds.FROM_TASK){
				/*
				TaskFeeds task = (TaskFeeds) TaskManager.getTaskFromDB(feeds.appLocalid);
				if(task != null){
					task.onFinish(TaskBase.STATUS_READY, "");
				}
				*/
				XLTToast.makeText(mContext, R.string.feeds_ban_uploading).show();
				return;
			}

			if (FeedServer.checkDeleteFeedsAuthority(feeds, Feeds.ACTION_MODIFY)) {
				mPostToParam.tid = feeds.tid;
				mPostToParam.guid = feeds.guid;
				FeedsPostActivity.startPostFeedsActivity(mContext, mPostToParam);
			} else {
				XLTToast.makeText(mContext, R.string.feeds_ban_delete).show();
			}

		}
		else if (id == R.id.feeds_operator) {

		} else if (id == R.id.feeds_send_failed_view) {
			if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, mContext)){
				return;
			}
			FeedServer.rePostFeeds(feeds);

		} else if (id == R.id.feeds_gift) {
			if (feeds.from == Feeds.FROM_TASK) {
				return;
			}
			//GiftShopActivity.startGiftShopActivity(mContext, feeds.guid, feeds.dtype);

		} else if (id == R.id.item_gift_list) {
			//GiftListInFeedsActivity.startGiftListActivity(mContext, feeds);

		} else if (id == R.id.feeds_share) {
			int actionId = isDetailPage ? SocialActivity.SOCIAL_ACTION_BABY_DETAIL : SocialActivity.SOCIAL_ACTION_BABY;
			SocialActivity.startSocialActivity(mContext, feeds, actionId);

		} else if (id == R.id.user_logo) {
			String name = feeds.crerealname;
			String gx = TextUtils.isEmpty(feeds.gxname) ? "" : feeds.gxname;
			if (!TextUtils.isEmpty(gx) && !TextUtils.isEmpty(feeds.baobaorealname)){
				name = String.format("%s%s", feeds.baobaorealname, gx);
			}
			ShowPersonalInfoActivity.startShowPersonalInfoActivity(mContext, feeds.creuid, name, feeds.getCircleMember().avatar);
		} else if(id == R.id.feeds_browser){
			if (feeds.tid > 0) {
				if (!isDetailPage) {
					FeedServer.incrementBrowswerNum(feeds.tid);
				}
				BrowserActivity.startBrowserActivity(mContext, feeds.tid);
			}
		}
		else if(id == R.id.photo_time_container){
			EventBus.getDefault().post(new SelectedDateEvent());
		}

	}

	public void onDeleteComment(final CommentInfo comment) {
		if (comment.comment_id <= 0) {
			XLTToast.makeText(mContext, R.string.comment_uploading).show();
			return;
		}

		int resId = R.string.alter_comment_delete;
		alterDialog(resId, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedServer.deleteComment(comment, null);
				mAlertDlg.dismiss();
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
		int id = v.getId();
		/*
		if (id == R.id.comment) {
			//FeedsCommentActivity.startCommentActivity(mContext, mDtype, mGuid, 0, null, null, mH);
		} else if (id == R.id.delete) {

			if (FeedsServer.checkDeleteFeedsAuthority(mDtype, mCreuid, mClassId, mSchoolId)) {
				int resId = R.string.alter_post_delete;
				alterDialog(resId, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						FeedsServer.deleteFeeds(mGuid,mDtype, null);
						mAlertDlg.dismiss();
					}
				});
			} else {
				XLTToast.makeText(mContext, R.string.feeds_ban_delete).show();
			}

		} else if (id == R.id.edit) {

			if (FeedsServer.checkDeleteFeedsAuthority(mDtype, mCreuid, mClassId, mSchoolId)
					&& mFeedsType == Feeds.TAG_NORMAL) {
				//FeedsPostActivity.startEditFeedsActivity(mContext, mGuid, mDtype);
			} else {
				XLTToast.makeText(mContext, R.string.feeds_ban_delete).show();
			}

		} else if (id == R.id.collect_from) {
			//ShowPersonalInfoActivity.startShowPersonalInfoActivity(mContext, (Long) v.getTag());
		}
		*/
	}

	private void alterDialog(int msgResId, View.OnClickListener comfirm) {
		if (mAlertDlg == null) {

			mAlertDlg = new CommonAlertDialog(mContext);
			mAlertDlg.setTitle(R.string.main_prompt);
			mAlertDlg.setCancelable(false);
			mAlertDlg.setNegativeButton(mContext.getString(android.R.string.no), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mAlertDlg.dismiss();
				}
			});
		}
		mAlertDlg.setMessage(msgResId);
		mAlertDlg.setPositiveButton(mContext.getString(android.R.string.yes), comfirm);
		mAlertDlg.show();
	}
}