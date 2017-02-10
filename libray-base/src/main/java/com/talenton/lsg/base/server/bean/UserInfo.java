package com.talenton.lsg.base.server.bean;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.util.HashSet;
import java.util.LinkedList;

public class UserInfo {
	public static UserInfo EMPTY = new UserInfo();
	// 家长-1、教师-2、园长-3、100-其他
	public final static int GROUP_PARENT = 1;
	public final static int GROUP_TEACHER = 2;
	public final static int GROUP_MASTER = 3;
	public final static int GROUP_OTHER = 100;

	public long uid = 0; //用户ID
	public int groupid; //用户组ID
	public int groupkey; //身份标示（家长-1、教师-2、园长-3、100-其他）
	public String realname = "";//昵称
	public String username = "";//用户名
	public String avartar; //头像URL
	//public long birthday = 0; //格式：此域为空表示不修改
	public int gender; //0-保密、1-男、2-女

	public long credits; //用户积分
	public int phone_bind;//0-未绑定手机、1-已绑定手机
	public int isbind; //是否绑定第三方,
	public int wxbind;//0-未绑定微信、1-已绑定微信,
	public int qqbind;//0-未绑定QQ、1-已绑定QQ,
	//
	public String password;
	public String pwd;

	public String level; //成长等级

	public LinkedList<BabyData> baobaodata;
	public LinkedList<SchoolData> schooldata; //学校信息,当老师登录时返回

	public SparseIntArray attentionType = new SparseIntArray();
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean checkUserInfo(){
		if(groupkey == GROUP_TEACHER){
			return true;
		}

		if (TextUtils.isEmpty(realname) || getBaobaodata() == null || getBaobaodata().baobaouid == 0)
			return false;

		return true;
	}

	public BabyData getBaobaodata(){
		if (baobaodata == null){
			baobaodata = new LinkedList<>();
		}
		if(baobaodata.size() == 0){
			return new BabyData();
		}
		return baobaodata.get(0);
	}

	public LinkedList<BabyData> getBaobaodataList(){
		if (baobaodata == null){
			baobaodata = new LinkedList<>();
		}
		return baobaodata;
	}
}
