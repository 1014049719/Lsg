package com.talenton.lsg.base.server;

/**
 * Callback interface for delivering parsed responses.
 * Created by ttt on 2016/3/28.
 */
public interface XLTResponseListener<T> {
    /** Called when a response is received. */
    public void onResponse(T responseData, XLTError errorData);
}
