package com.talenton.lsg.server.bean.feed;

import java.io.Serializable;

public final class GiftInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -582457752806705419L;
	public final static String URL_LIST = "mod=gift&ac=giftinfo&cmdcode=23";
	public final static String KEY_GIFT_TYPE = "gifttype";
	public final static int GIFT_TYPE_FAMILY = 1;
	public final static int GIFT_TYPE_TEACHER = 2;

	public int id;
	public int ldcount;
	public int gifttype;

	public String giftname;

	public String imgurl;

}