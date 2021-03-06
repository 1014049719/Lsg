package com.talenton.lsg.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.AppConfig;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.ui.user.JumpType;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.GoodsCartInfo;
import com.talenton.lsg.server.bean.shop.GoodsInfo;
import com.talenton.lsg.server.bean.shop.RecvGoodsIdData;
import com.talenton.lsg.server.bean.shop.SendAddShopCartData;
import com.talenton.lsg.server.bean.shop.SendGoodsIdData;
import com.talenton.lsg.widget.MsgTipsView;

import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/4/22.
 */
/*
增加收货地址
 */
public class GoodsDetailActivity extends BaseCompatActivity implements View.OnClickListener {


    private Button add_to_cart;
    private Button buy_right_now;

    private int goods_id;
    private GoodsInfo mGoodsInfo;
    private GoodsCartInfo mSelected;
    private ArrayList<GoodsCartInfo> mSelectedList;

    private ImageView shop_detail_cart;
    private MsgTipsView iv_msg_tips;
    private ImageView shop_detail_back;

    private int _xDelta;
    private Resources _resources;
    private ImageView imgTabLine;
    private ViewPager pgeView;
    private LinearLayout layTab;
    private TextView tvwTabNewApply,tvwTabPass,tvwTabRefuse;
    private ArrayList<Fragment> _fragmentList;
    private int _currIndex = 0;
    private static int mGoodsIdOrInfo=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_shop_goods_detail);
        super.onCreate(savedInstanceState);
        mGoodsInfo = new GoodsInfo();
        mSelectedList = new ArrayList<GoodsCartInfo>();
        mSelected = new GoodsCartInfo();
        if(mGoodsIdOrInfo==1){
            goods_id= (int)getIntent().getExtras().getLong("mGoodsId");
        }else {

            mGoodsInfo = (GoodsInfo) getIntent().getExtras().getSerializable("mGoodsInfo");

            //////将goodsinfo的东西转为goodscartinfo;
            mSelected.goods_id = mGoodsInfo.goods_id;
            mSelected.goods_price = mGoodsInfo.shop_price;
            mSelected.name = mGoodsInfo.name;
            mSelected.goods_number = 1;
            mSelected.goods_img = mGoodsInfo.goods_img;
            mSelectedList.add(mSelected);
            goods_id = mSelected.goods_id;


        }
        _resources = getResources();
        initWidth();
        initView();
        initViewPager(0);



    }

    public static void startGoodsDetailActivity(Context context, GoodsInfo mGoodsInfo){
        mGoodsIdOrInfo=0;
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mGoodsInfo", mGoodsInfo);
        intent.putExtras(bundle);
    //    intent.setClass(context, GoodsDetailActivity.class);
        context.startActivity(intent);
    }

    public static void startGoodsDetailActivity(Context context, long  mGoodsId){
        mGoodsIdOrInfo=1;
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("mGoodsId", mGoodsId);
        intent.putExtras(bundle);
        //    intent.setClass(context, GoodsDetailActivity.class);
        context.startActivity(intent);
    }
    /**
     * 初始化view
     *
     * @param
     */
    private void initView() {


        add_to_cart = (Button) findViewById(R.id.add_to_cart);
        buy_right_now = (Button) findViewById(R.id.buy_right_now);

        add_to_cart.setOnClickListener(this);
        buy_right_now.setOnClickListener(this);

        tvwTabNewApply = (TextView) findViewById(R.id.tvwNewApply);
        tvwTabPass = (TextView) findViewById(R.id.tvwPass);
        tvwTabRefuse = (TextView) findViewById(R.id.tvwRefuse);

        tvwTabNewApply.setOnClickListener(new MyOnClickListener(0));
        tvwTabPass.setOnClickListener(new MyOnClickListener(1));
        tvwTabRefuse.setOnClickListener(new MyOnClickListener(2));

        pgeView = (ViewPager) findViewById(R.id.pgeRequestManageView);
        layTab= (LinearLayout)findViewById(R.id.layRequestManageTab);
        shop_detail_cart=(ImageView)findViewById(R.id.shop_detail_cart);
        shop_detail_back=(ImageView)findViewById(R.id.shop_detail_back);

        shop_detail_cart.setOnClickListener(this);
        shop_detail_back.setOnClickListener(this);

        iv_msg_tips=(MsgTipsView)findViewById(R.id.iv_msg_tips);
        iv_msg_tips.setImgSrc(R.mipmap.icon_shop_detail_message);


    }
    private void getGoodsInfoById() {

        ShopServer.getGoodsInfoByIdData(new SendGoodsIdData(goods_id), new XLTResponseCallback<RecvGoodsIdData>() {

            @Override
            public void onResponse(RecvGoodsIdData data, XLTError error) {
                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {
                    //////将goodsinfo的东西转为goodscartinfo;
                    mSelected.goods_id = data.goods_id;
                    mSelected.goods_price = data.shop_price;
                    mSelected.name = data.name;
                    mSelected.goods_number = 1;
                    mSelected.goods_img = data.goods_img;
                    mSelectedList.add(mSelected);
                } else if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getMesssage(), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(), "根据ID获取商品失败，请检查网络", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    private void addToShoppingCart() {

        ShopServer.addMyShopCartData(new SendAddShopCartData(goods_id, 1), new XLTResponseCallback<Object>() {

            @Override
            public void onResponse(Object data, XLTError error) {
                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {
                    Toast.makeText(getApplicationContext(), "加入购物车成功", Toast.LENGTH_SHORT).show();
                }else if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getMesssage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "加入购物车失败,请检查网络", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    /**
     * ViewPager初始化
     */
    private void initViewPager(int currentItem) {


        _fragmentList = new ArrayList<Fragment>();


        //	new RequestManageFragment();
        String urlShop = AppConfig.HOME_URL_SHOP+"mobile/goods.php?id="+String.valueOf(goods_id)+"&cmdcode=12";
        String urlShopDetail = AppConfig.HOME_URL_SHOP+"mobile/goods.php?id="+String.valueOf(goods_id)+"&act=1&cmdcode=12";
        String urlShopComment = AppConfig.HOME_URL_SHOP+"mobile/goods.php?id="+String.valueOf(goods_id)+"&act=2&cmdcode=12";

        _fragmentList.add(GoodsDetailFragment.newInstance(urlShop));
        _fragmentList.add(GoodsDetailFragment.newInstance(urlShopDetail));
        _fragmentList.add(GoodsDetailFragment.newInstance(urlShopComment));

        pgeView.setAdapter(new ManagePagerAdapter(
                getSupportFragmentManager(), _fragmentList));
        pgeView.setOnPageChangeListener(new MyOnPageChangeListener());
        pgeView.setCurrentItem(currentItem);
        pgeView.setOffscreenPageLimit(3);

        getTabView(_currIndex).setTextColor(
                _resources.getColor(R.color.red));

    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            // 创建动画
            Animation animation = null;
            int fromXDelta = _currIndex * _xDelta;
            int toXDelta = arg0 * _xDelta;
            animation = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
            // 设置颜色
            /*
            getTabView(_currIndex).setTextColor(
                    _resources.getColor(R.color.black_bg));


            layTab.setBackgroundColor(getResources().getColor(R.color.white));
            */
            for(int i=0;i<3;i++){
            getTabView(i).setTextColor(
                    _resources.getColor(R.color.white));}
            _currIndex = arg0;
            getTabView(_currIndex).setTextColor(
                    _resources.getColor(R.color.red));

            // 显示动画
            animation.setFillAfter(true);
            animation.setDuration(300);
            imgTabLine.startAnimation(animation);
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            pgeView.setCurrentItem(index);


        }
    };

    /**
     * 获取分栏目的TextView
     *
     * @param position
     *            位置索引 从0开始
     * @return
     */
    private TextView getTabView(int position) {
        switch (position) {
            case 0:
                return tvwTabNewApply;
            case 1:
                return tvwTabPass;
            case 2:
                return tvwTabRefuse;
            default:
                return null;
        }
    }
    /**
     * 宽度初始化
     */
    private void initWidth() {

        imgTabLine = (ImageView) findViewById(R.id.imgTabRequestManageLine);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;

        _xDelta = (int) (screenW / 5.0);


        float scale = _resources.getDisplayMetrics().density;
        int width = _xDelta;
        int height = (int) (2 * scale + 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                height);
        imgTabLine.setLayoutParams(params);
    }

    public class ManagePagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> _fragmentList;

        public ManagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ManagePagerAdapter(FragmentManager fm,
                                  ArrayList<Fragment> fragments) {
            super(fm);
            _fragmentList = fragments;
        }

        @Override
        public int getCount() {
            return _fragmentList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return _fragmentList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }



    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.add_to_cart:
                if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, this)){
                    return;
                }
                addToShoppingCart();

                break;
            case R.id.buy_right_now:
                if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, this)){
                    return;
                }
                ConfirmOrderNowActivity.startConfirmOrderNowActivity(this, mSelectedList);

                break;
            case R.id.shop_detail_cart:
                if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, this)){
                    return;
                }
                intent.setClass(this, ShoppingCartActivity.class);
                startActivity(intent);
            case R.id.shop_detail_back:
                finish();



        }
    }
}