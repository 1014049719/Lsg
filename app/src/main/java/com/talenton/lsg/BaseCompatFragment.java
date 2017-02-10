package com.talenton.lsg;

import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.UserInfo;
import com.talenton.lsg.base.util.XLTToast;

/**
 * Created by ttt on 2016/3/31.
 */
public class BaseCompatFragment extends Fragment {

    // 是否不预加载 true:不预加载 false:预加载（默认）
    public boolean isLazyMode;
    protected TextView mActionBarTitle = null;
    protected View mActionBar;

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void initActionBar(View v) {
        ViewStub stub = (ViewStub) v.findViewById(R.id.vs_action_bar);
        if (stub == null) {
            return;
        }
        stub.setLayoutResource(getActionBarResourceId());
        mActionBar = stub.inflate();
        initActionBarContent(mActionBar);
    }

    protected void initActionBarContent(View actionBar) {
        if (actionBar != null) {
            int resourceId = getTitleResourceId();
            if (resourceId > 0) {
                mActionBarTitle = (TextView) actionBar.findViewById(R.id.tv_toolbar_title);
                mActionBarTitle.setText(resourceId);
            }
            Toolbar toolbar = (Toolbar)actionBar.findViewById(R.id.toolbar);
            if(toolbar == null) return;
            /*
            resourceId = getRightTextResourceId();
            if (resourceId > 0) {
                toolbar.setTitle(resourceId);
            }
            */
            resourceId = getLeftImageResourceId();
            if (resourceId > 0) {
                toolbar.setNavigationIcon(resourceId);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLeftClick();
                    }

                });
            }

            resourceId = getMenuResourceId();

            if (resourceId > 0) {
                toolbar.inflateMenu(resourceId);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onRightClick(item);
                        return true;
                    }
                });
            }

        }
    }

    protected int getActionBarResourceId() {
        return R.layout.global_app_bar;
    }

    protected int getTitleResourceId() {
        return R.string.app_name;
    }

    protected int getMenuResourceId() {
        return 0;
    }

    protected void onRightClick(MenuItem item) {
    }

    protected void onLeftClick() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    protected int getLeftImageResourceId() {
        return R.drawable.actionbar_back_btn;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isLazyMode) {
            if (getUserVisibleHint()) {
                onVisible();
            } else {
                onInvisible();
            }
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
    }

    protected void onInvisible() {
    }

    /**
     * toast提示
     * @param msg
     */
    public void showLongToast(String msg){
        XLTToast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();
    }

    /**
     * toast提示
     * @param msg
     */
    public void showShortToast(String msg){
        XLTToast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断用户是否登录
     * @return
     */
    protected boolean isLogin(){
        if (UserServer.getCurrentUser().getUid() != 0){
            return true;
        }
        return false;
    }

    /**
     * 是否为老师登录
     * @return
     */
    public boolean isTeacher(){
        return UserServer.getCurrentUser().groupkey == UserInfo.GROUP_TEACHER ? true : false;
    }

}
