package com.talenton.lsg.server.bean.feed;

import java.util.ArrayList;

public class ReqSendGift {
	public final static String URL = "mod=member&ac=zengsong_virtualgifts&cmdcode=18";
	public String guid;
	public int dtype;
	public ArrayList<GiftSendDetail> giftdata;
	// 发送者身份标示
	public String schoolname;
	public String classname;
	public int groupkey;
	public int gxid;
	public String gxname;
	public String baobaoname;

	public ReqSendGift(String guid, int dtype, ArrayList<GiftSendDetail> giftdata) {
		super();
		this.dtype = dtype;
		this.guid = guid;
		this.giftdata = giftdata;
	}

}