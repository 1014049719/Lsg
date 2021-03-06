package com.talenton.lsg.server.bean.school;

import com.talenton.lsg.base.util.JsonObjUtil;

/**
 * @author zjh
 * @date 2016/4/29
 */
public class ReqFavorite implements IBaseReq{
    private static final String FAVORITE_URL = "classroom.php?mod=collect&cmdcode=106";
    private static final String FAVORITE_CANCEL_URL = "classroom.php?mod=uncollect&cmdcode=112";
    private long aid;
    private boolean isFavorite; //是否收藏

    public ReqFavorite(long aid, boolean isFavorite) {
        this.aid = aid;
        this.isFavorite = isFavorite;
    }

    @Override
    public String getReqParams() {
        return JsonObjUtil.getInstance().addParam("aid",aid).toJsonString();
    }

    @Override
    public String getReqUrl() {
        if (isFavorite){
            return FAVORITE_URL;
        }else {
            return FAVORITE_CANCEL_URL;
        }
    }
}
