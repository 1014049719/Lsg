package com.talenton.lsg.base.server.bean;

import com.talenton.lsg.base.util.DESUtils;

import android.text.TextUtils;

/**
 * @author bliang 登录需要用到的账号密码信息
 */
public class Authority {
	public static Authority EMPTY = new Authority();
	public final static int TYPE_LSG = 0, TYPE_WEIXIN = 1, TYPE_QQ = 2;
	public int authtype = TYPE_LSG;
	public String username = "";
	public String password = "";// 加密后的
	public String passwordEn = "";// 加密后的
	// 第三方登录信息
	public String openid;
	public String nickname;
	public String avartarurl;
	public String access_token;
	public String mobile;
	public String yzcode;

	public Authority(Authority a) {
		if (a != null) {
			this.authtype = a.authtype;
			this.username = a.username;
			this.password = a.password;
			this.passwordEn = a.passwordEn;
			this.openid = a.openid;
			this.nickname = a.nickname;
			this.avartarurl = a.avartarurl;
			this.access_token = a.access_token;
			this.mobile = a.mobile;
			this.yzcode = a.yzcode;
		} else {
			this.authtype = TYPE_LSG;
			this.username = "";
			this.password = "";
			this.passwordEn = "";
		}
	}

	public Authority(String username, String password) {
		this.username = username;
		this.password = password;
		this.passwordEn = DESUtils.encode(password);
	}

	public Authority(int authtype, String openid, String nickname, String avartarurl, String access_token) {
		this.authtype = authtype;
		this.openid = openid;
		this.nickname = nickname;
		this.avartarurl = avartarurl;
		this.access_token = access_token;
	}

	public Authority(int authtype, String openid, String nickname, String avartarurl, String access_token,
			String mobile, String yzcode) {
		this.authtype = authtype;
		this.openid = openid;
		this.nickname = nickname;
		this.avartarurl = avartarurl;
		this.access_token = access_token;
		this.mobile = mobile;
		this.yzcode = yzcode;
	}

	public Authority() {
		// TODO Auto-generated constructor stub
	}

	public void setPlaint() {
		this.password = DESUtils.decode(passwordEn);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLegal() {
		return (authtype == Authority.TYPE_LSG && (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)))
				|| (authtype != Authority.TYPE_LSG && !TextUtils.isEmpty(openid) && !TextUtils.isEmpty(access_token));
	}

	@Override
	public String toString() {
		return "Authority [authtype=" + authtype + ", username=" + username + ", password=" + password + ", passwordEn="
				+ passwordEn + ", openid=" + openid + ", nickname=" + nickname + ", avartarurl=" + avartarurl
				+ ", access_token=" + access_token + "]";
	}

}
