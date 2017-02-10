package com.talenton.lsg.server;

import com.talenton.lsg.server.bean.feed.GiftInfo;

import java.util.ArrayList;

/**
 * Created by ttt on 2016/5/4.
 */
public class GiftService {
    private static ArrayList<GiftInfo> mGiftInfo;

    public static String getGiftUrl(int giftId) {
        if (mGiftInfo == null) {
            return "";
        }
        for (GiftInfo gift : mGiftInfo) {
            if (gift.id == giftId) {
                return gift.imgurl;
            }
        }
        return "";
    }

    public static int getGiftlecount(int giftId) {
        if (mGiftInfo == null) {
            return 0;
        }
        for (GiftInfo gift : mGiftInfo) {
            if (gift.id == giftId) {
                return gift.ldcount;
            }
        }
        return 0;
    }
}
