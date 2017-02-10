package com.talenton.lsg;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ttt on 2016/3/29.
 */
@Deprecated
public class BaseFragment extends Fragment {

    // 是否不预加载 true:不预加载 false:预加载（默认）
    public boolean isLazyMode;
    protected TextView mActionBarTitle = null, mActionBarRightText = null;
    protected ImageView mActionBarRightImage = null;
    protected View mActionBar;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume()  ;
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
                mActionBarTitle = (TextView) actionBar.findViewById(R.id.tv_actionbar_title);
                mActionBarTitle.setText(resourceId);
            }
            resourceId = getRightTextResourceId();
            mActionBarRightText = (TextView) actionBar.findViewById(R.id.tv_actionbar_right);
            mActionBarRightImage = (ImageView) actionBar.findViewById(R.id.iv_actionbar_right);
            if (resourceId > 0) {
                mActionBarRightImage.setVisibility(View.INVISIBLE);
                mActionBarRightText.setVisibility(View.VISIBLE);
                mActionBarRightText.setText(resourceId);
            } else {
                resourceId = getRightImageResourceId();
                if (resourceId > 0) {
                    mActionBarRightImage.setVisibility(View.VISIBLE);
                    mActionBarRightText.setVisibility(View.INVISIBLE);
                    mActionBarRightImage.setImageResource(resourceId);
                }
            }

            if (resourceId > 0) {
                View actionBarRight = actionBar.findViewById(R.id.fl_actionbar_right);
                actionBarRight.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onRightClick(v);
                    }

                });
            }

            resourceId = getLeftImageResourceId();

            if (resourceId > 0) {
                ImageView actionBarLeft = (ImageView) actionBar.findViewById(R.id.iv_actionbar_back);
                actionBarLeft.setImageResource(resourceId);
                actionBarLeft.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onLeftClick();
                    }

                });
            } else {
                actionBar.findViewById(R.id.iv_actionbar_back).setVisibility(View.INVISIBLE);
            }

        }
    }

    protected int getActionBarResourceId() {
        return R.layout.global_action_bar;
    }

    protected int getTitleResourceId() {
        return R.string.app_name;
    }

    protected int getRightTextResourceId() {
        return 0;
    }

    protected int getRightImageResourceId() {
        return 0;
    }

    protected void onRightClick(View v) {
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

}
