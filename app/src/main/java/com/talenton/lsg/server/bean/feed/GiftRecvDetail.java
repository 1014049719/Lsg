package com.talenton.lsg.server.bean.feed;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.UserInfo;
import com.talenton.lsg.server.GiftService;

public final class GiftRecvDetail implements Parcelable {
	public final static String URL_LIST_RECV_DETAIL = "mod=gift&ac=teachergift&cmdcode=53";
	public final static String KEY_GIFT_ID = "giftid";
	public String guid;
	public Long uid;
	public String nickname;
	public Long baobaouid;
	public Integer giftcount;
	public Long dateline;
	public String fbztx;
	public Integer gifteid;
	public String giftname;
	public String imgurl;
	// 发送者身份标示
	public String schoolname;
	public String classname;
	public int groupkey;
	public int gxid;
	public String gxname;
	public String baobaoname;
	public String fullgxname;

	public GiftRecvDetail(ReqSendGift gift) {
		UserInfo me = UserServer.getCurrentUser();
		if (gift.giftdata == null || gift.giftdata.isEmpty() || me == null) {
			guid = null;
			return;
		}
		guid = gift.guid;
		GiftSendDetail g = gift.giftdata.get(0);
		giftcount = g.giftcount;
		giftname = g.giftname;
		imgurl = GiftService.getGiftUrl(g.giftid);
		fbztx = me.avartar;
		nickname = me.realname;
		uid = me.uid;
		dateline = System.currentTimeMillis() / 1000;
	}

	public GiftRecvDetail() {
		// TODO Auto-generated constructor stub
	}

	public String getFullGxName() {
		if (TextUtils.isEmpty(fullgxname)) {
			fullgxname = nickname;
			if (groupkey == UserInfo.GROUP_PARENT) {
				String gx = TextUtils.isEmpty(gxname) ? "" : gxname;
				if (!TextUtils.isEmpty(gx) && !TextUtils.isEmpty(baobaoname)) {
					fullgxname = String.format("%s%s", baobaoname, gx);
				}
			}
		}
		return fullgxname;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Constructs a Question from a Parcel
	 * 
	 * @param parcel
	 *            Source Parcel
	 */
	public GiftRecvDetail(Parcel parcel) {
		this.uid = parcel.readLong();
		this.giftcount = parcel.readInt();
		this.dateline = parcel.readLong();
		this.imgurl = parcel.readString();
		this.fbztx = parcel.readString();
		this.giftname = parcel.readString();
		this.fullgxname = parcel.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		getFullGxName();
		dest.writeLong(uid);
		dest.writeInt(giftcount);
		dest.writeLong(dateline);
		dest.writeString(imgurl);
		dest.writeString(fbztx);
		dest.writeString(giftname);
		dest.writeString(fullgxname);
	}

	public static Creator<GiftRecvDetail> CREATOR = new Creator<GiftRecvDetail>() {

		@Override
		public GiftRecvDetail createFromParcel(Parcel source) {
			return new GiftRecvDetail(source);
		}

		@Override
		public GiftRecvDetail[] newArray(int size) {
			return new GiftRecvDetail[size];
		}

	};
}