package com.talenton.lsg.base.server;

public interface XLTResponseCallback<T> {
	void onResponse(T data, XLTError error);
}