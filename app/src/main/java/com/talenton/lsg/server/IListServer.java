package com.talenton.lsg.server;

import com.talenton.lsg.base.server.XLTResponseCallback;


/**
 * @author zjh
 * @date 2016/4/1
 */
public interface IListServer<T> {
    /**
     * 加载分页数据
     * @param page 加载第几页
     * @param limit 1页加载多少条
     * @param listener 回调
     */
    void getData(int page,int limit,XLTResponseCallback<T> listener);

    /**
     * 获取缓存数据
     * @return
     */
    T getCacheData();
}
