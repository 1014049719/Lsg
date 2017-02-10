package com.talenton.lsg.ui.feed.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.GiftRecvDetail;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.talenton.lsg.R;

public class GiftTotalContainerLayout extends LinearLayout {

	private Context mContext;
	private LayoutInflater inflater;
	private LinearLayout giftList;
	private TextView giftInfo;
	private ArrayList<View> giftViews = new ArrayList<View>(1);
	private static int MAX_SIZE = 0;

	public GiftTotalContainerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.item_feeds_gift_total, this);
		giftList = (LinearLayout) findViewById(R.id.gift_list);
		giftInfo = (TextView) findViewById(R.id.gift_total_intro);
	}

	public void setGiftInfo(Feeds feedsInfo) {

		if (MAX_SIZE == 0) {
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			int width = mContext.getResources().getDimensionPixelSize(R.dimen.height_50);
			width += mContext.getResources().getDimensionPixelSize(R.dimen.space_5_0) * 2;
			int margin = mContext.getResources().getDimensionPixelSize(R.dimen.space_47_5);
			margin += mContext.getResources().getDimensionPixelSize(R.dimen.space_15_0) * 2;
			margin += mContext.getResources().getDimensionPixelSize(R.dimen.space_7_5) * 2;
			MAX_SIZE = (dm.widthPixels - margin) / width;
		}

		if (feedsInfo == null || feedsInfo.giftdata == null || feedsInfo.giftdata.isEmpty()) {
			setVisibility(View.GONE);
			return;
		} else {
			setVisibility(View.VISIBLE);
			ArrayList<GiftRecvDetail> gifts = feedsInfo.giftdata;
			int giftSum = 0;
			HashSet<Long> senders = new HashSet<Long>();
			for (GiftRecvDetail giftInfo : gifts) {
				giftSum += giftInfo.giftcount;
				senders.add(giftInfo.uid);
			}
			giftInfo.setText(mContext.getResources().getString(R.string.circle_feeds_list_gift_total, giftSum, senders.size()));

			if (feedsInfo.circle_type != CircleListData.CIRCLE_TYPE_BABY) {
				// 显示统计信息
				ArrayList<GiftRecvDetail> giftsCount = feedsInfo.countByGift;
				if (giftsCount == null || giftsCount.size() == 0) {
					giftsCount = new ArrayList<GiftRecvDetail>();
					HashMap<String, Integer> counts = new HashMap<String, Integer>();
					for (GiftRecvDetail giftInfo : gifts) {
						Integer o = counts.get(giftInfo.imgurl);
						counts.put(giftInfo.imgurl, giftInfo.giftcount + (o == null ? 0 : o));
					}
					for (HashMap.Entry<String, Integer> entry : counts.entrySet()) {
						GiftRecvDetail gift = new GiftRecvDetail();
						gift.imgurl = entry.getKey();
						gift.giftcount = entry.getValue();
						giftsCount.add(gift);
					}
					feedsInfo.countByGift = giftsCount;
					addGiftViews(giftsCount, feedsInfo.circle_type);
				}
			} else {
				addGiftViews(gifts, feedsInfo.circle_type);
			}
		}
	}
	//
	// public void setGiftInfo(ArrayList<GiftRecvDetail> infos, int feedsType) {
	// if (infos == null || infos.isEmpty()) {
	// setVisibility(View.GONE);
	// return;
	// } else {
	// setVisibility(View.VISIBLE);
	// int giftSum = 0, sender = infos.size();
	// for (GiftRecvDetail giftInfo : infos) {
	// giftSum += giftInfo.giftcount;
	// }
	// giftInfo.setText(mContext.getResources().getString(R.string.feeds_list_gift_total,
	// giftSum, sender));
	// addGiftViews(infos, feedsType);
	// }
	// }

	private void addGiftViews(ArrayList<GiftRecvDetail> sendInfos, int feedsType) {
		int showingSize = Math.min(MAX_SIZE, sendInfos.size());
		int resId = R.layout.item_feeds_gift_baby;
		if (giftViews.size() < showingSize) {
			int gap = showingSize - giftViews.size();
			for (int i = 0; i < gap; i++) {
				View commentv = inflater.inflate(resId, null);
				LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				giftList.addView(commentv, params);
				giftViews.add(commentv);
			}
		}
		//
		for (int i = 0; i < showingSize; i++) {
			GiftRecvDetail gift = sendInfos.get(i);
			View gv = giftViews.get(i);
			gv.setVisibility(View.VISIBLE);
			ImageView logo = (ImageView) gv.findViewById(R.id.gift_logo);
			ImageView userLogo = (ImageView) gv.findViewById(R.id.user_logo);
			TextView sum = (TextView) gv.findViewById(R.id.gift_sum);
			if (userLogo != null) {
				//ImageLoader.getInstance().displayImage(gift.fbztx, userLogo, ImageLoaderManager.DEFAULT_USER_DISPLAYER);
			}
			//ImageLoader.getInstance().displayImage(gift.imgurl, logo, ImageLoaderManager.DEFAULT_GIFT_DISPLAYER);
			sum.setText(String.format("+%d", gift.giftcount));
		}

		if (giftViews.size() > showingSize) {
			for (int i = showingSize; i < giftViews.size(); i++) {
				View tv = giftViews.get(i);
				if (tv != null) {
					tv.setVisibility(View.GONE);
				}
			}
		}
	}

}