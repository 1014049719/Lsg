package com.talenton.lsg.util;

import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.server.bean.operation.HomeAdvertisement;

import java.util.LinkedList;

/**
 * Created by ttt on 2016/4/5.
 */
public class CacheManager {
    private static CacheManager mInstance = null;
    private volatile LinkedList<HomeAdvertisement> mHomeAdvertisement = null;
    private volatile int mQnUpProgress = 0; //七牛上传进度

    private CacheManager()
    {
        mQnUpProgress = 0;
    }

    public static CacheManager getInstance(){
        if(mInstance == null){
            mInstance = new CacheManager();
        }
        return mInstance;
    }

    public LinkedList<HomeAdvertisement> getHomeAdvertisement(){
        return mHomeAdvertisement;
    }
    public void setHomeAdvertisement(LinkedList<HomeAdvertisement> ha) {
        mHomeAdvertisement = ha;
    }

    public void removeAllData(){

    }

    /* 登录成功，或者登录失败，但存在缓存的登录信息时，切换数据库环境 */
    public void reset() {
        long uid = UserServer.getCurrentUser().uid;
        if (uid > 0) {
            //FeedsServer.syncFeedsDeleted(bb);
            removeAllData();
        }
    }

    public void  setQnUpProgress(int progress){
        mQnUpProgress = progress;
    }

    public int getQnUpProgress(){
        return mQnUpProgress;
    }
}