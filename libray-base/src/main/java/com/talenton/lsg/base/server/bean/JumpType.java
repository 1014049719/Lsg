package com.talenton.lsg.base.server.bean;

import com.talenton.lsg.base.XltApplication;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.util.XLTToast;

/**
 * Created by ttt on 2016/5/30.
 */
public class JumpType {

    public static final int JUMP_TYPE_NO_ACTION = 0;
    public static final int JUMP_TYPE_AUTO_LOGIN = 1;
    public static final int JUMP_TYPE_TOAST = 2;

    /**
     * 根据用户UID判断是否要执行动作
     *
     * @return
     */
    public static boolean jump(int type){

        if (UserServer.getCurrentUser().uid <= 0){
            switch (type){
                case JUMP_TYPE_NO_ACTION:
                    break;
                case JUMP_TYPE_AUTO_LOGIN:
                    UserServer.reLogin(true);
                    break;
                case JUMP_TYPE_TOAST:
                    XLTToast.makeText(XltApplication.getAppContext(), "请登录").show();
                    break;
            }
            return true;
        }
        return false;
    }
}
