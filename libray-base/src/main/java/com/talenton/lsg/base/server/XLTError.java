package com.talenton.lsg.base.server;

import com.google.gson.annotations.SerializedName;

/**
 * 错误类
 * 
 * @author yellow
 * @version 2015年4月5日 下午3:17:18
 */
public class XLTError {

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	public XLTError(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMesssage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean shouldRetry() {
		// 网络错误才应该重试
		return code == 501 || code == 502 || code == 503;

	}

	@Override
	public String toString() {
		return "XLTError [code=" + code + ", message=" + message + "]";
	}

}
