package com.talenton.lsg.ui.shop;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.talenton.lsg.BaseListActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.ui.user.JumpType;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.GoodsInfo;
import com.talenton.lsg.server.bean.shop.RecvRecommendGoodsData;
import com.talenton.lsg.ui.shop.adapter.GoodsAdapter;
import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/4/7.
 */
public class RecommendAreaActivity extends BaseListActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private ArrayList<GoodsInfo> mGoodsDiscountInfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_discount);

        initView();
        fillListData();

    }
    /**
     * 初始化view
     * @param
     */
    private void initView() {

        mGoodsDiscountInfoList=new ArrayList<>();
        findViewById(R.id.iv_shop_cart).setOnClickListener(this);

    }

    @Override
    protected int getTitleResourceId() {
        return R.string.shop_text_recommend_area;
    }

    @Override
    protected int getPullRefreshListViewResId() {
        return R.id.pull_refresh_list;
    }

    private void fillListData() {


        mGoodsDiscountInfoList=new ArrayList<GoodsInfo>();


        startGetData(new ShopServer.ShopRecommendListServer(), new ListResponseCallback<RecvRecommendGoodsData>() {
            @Override
            public void onSuccess(RecvRecommendGoodsData data) {
                mGoodsDiscountInfoList.addAll(data.getList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(XLTError error) {

            }
        });
        mAdapter = new GoodsAdapter(this,mGoodsDiscountInfoList,1);
        mPullRefreshListView.getRefreshableView().setAdapter(mAdapter);
        mPullRefreshListView.setOnItemClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){

            case R.id.iv_shop_cart:
                //购物车
                if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, this)){
                    return;
                }
                intent.setClass(this,ShoppingCartActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - mPullRefreshListView.getRefreshableView().getHeaderViewsCount();
        GoodsInfo mGoodsInfo = (GoodsInfo) mAdapter.getItem(position);
        GoodsDetailActivity.startGoodsDetailActivity(this, mGoodsInfo);

    }
}
