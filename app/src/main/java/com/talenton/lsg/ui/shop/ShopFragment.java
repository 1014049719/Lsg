package com.talenton.lsg.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.ui.user.JumpType;
import com.talenton.lsg.server.bean.school.AdvertiseBean;
import com.talenton.lsg.server.bean.school.ReqAdvertisement;
import com.talenton.lsg.widget.AdvertisementView;
import com.talenton.lsg.base.widget.ImageTextButton;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.AdvertisementInfo;
import com.talenton.lsg.server.bean.shop.GoodsInfo;
import com.talenton.lsg.server.bean.shop.RecvShopHomeGoodsData;
import com.talenton.lsg.server.bean.shop.SendShopHomeGoodsData;
import com.talenton.lsg.ui.shop.adapter.GoodsExpandableListViewAdapter;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends BaseCompatFragment implements View.OnClickListener,AdapterView.OnItemClickListener{


    private AdvertisementView ad_view; //广告
    private ImageTextButton btn_shop_discount; //优惠
    private ImageTextButton btn_shop_recommend; //推荐
    private ImageTextButton btn_shop_hot_sale; //热卖
    private ImageTextButton btn_shop_classify; //分类
    private ArrayList<GoodsInfo> mGoodsRecommendInfoList;
    private ArrayList<GoodsInfo> mGoodsDiscountInfoList;
    private ArrayList<GoodsInfo> mGoodsHotSaleInfoList;
    private ArrayList<ArrayList<GoodsInfo>> mGoodsAllInfoList;
    private ArrayList<GroupString> mGoodsGroupList;
    private PullToRefreshExpandableListView mListView;
    private GoodsExpandableListViewAdapter mGoodsExpandableListViewAdapter;
    private String keywords;
    private EditText et_search;
    List<AdvertiseBean> datas;
    List<AdvertisementInfo> getDatas;
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        if (context instanceof View.OnClickListener) {

        } else {

        }
    }

    static public ShopFragment newInstance() {
        ShopFragment f = new ShopFragment();
        f.isLazyMode = true;
        return f;
    }

    public ShopFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (ad_view != null){
            //不调会导致内存泄漏
            ad_view.onDestroy();
        }
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        if (ad_view != null){
            ad_view.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (ad_view != null){
            ad_view.onPause();
        }
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // image_detail_fragment.xml contains just an ImageView
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        initActionBar(v);

        initView(v);
        fillAdViewData();
        fillListData();

        return v;
    }

    /**
     * 填充广告view数据
     */
    private void fillAdViewData() {

        /*
        ShopServer.getAdvertisement(new SendAdvertisementData(21), new XLTResponseCallback<RecvAdvertisementData>() {

            @Override
            public void onResponse(RecvAdvertisementData data, XLTError error) {
                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {

                    getDatas.addAll(data.list);
                    for (int i = 0; i < getDatas.size(); i++) {

                        datas.add(new AdvertiseBean(getDatas.get(i).ad_code));
                    }
                    ad_view.setData(datas);
                } else {


                }

            }

        });
        */

        ad_view.setData(new ReqAdvertisement(ReqAdvertisement.AD_SHOP_CLASS_LIST));


    }


    public class GroupString{
        public String tex;
        public String texEnglish;
        public GroupString(String tex,String texEnglish){
            this.tex=tex;
            this.texEnglish=texEnglish;
        }
    }
    private void fillListData() {

        mGoodsRecommendInfoList=new ArrayList<GoodsInfo>();
        mGoodsDiscountInfoList=new ArrayList<GoodsInfo>();
        mGoodsHotSaleInfoList=new ArrayList<GoodsInfo>();
        mGoodsAllInfoList=new ArrayList<ArrayList<GoodsInfo>>() ;
        mGoodsGroupList=new ArrayList<GroupString>();
        GroupString DiscountList = new GroupString("优惠区","SALE");;
        GroupString RecommendList = new GroupString("推荐区","RECOMMEND");;
        GroupString HotList = new GroupString("热卖区","HOT");
        mGoodsGroupList.add(DiscountList);
        mGoodsGroupList.add(RecommendList);
        mGoodsGroupList.add(HotList);

        ShopServer.getShopHomeGoodsData(new SendShopHomeGoodsData(), new XLTResponseCallback<RecvShopHomeGoodsData>() {

            @Override
            public void onResponse(RecvShopHomeGoodsData data, XLTError error) {
                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {

                    mGoodsRecommendInfoList.addAll(data.bestlist);
                    mGoodsDiscountInfoList.addAll(data.promotelist);
                    mGoodsHotSaleInfoList.addAll(data.hotlist);
                    mGoodsAllInfoList.add(mGoodsDiscountInfoList);
                    mGoodsAllInfoList.add(mGoodsRecommendInfoList);
                    mGoodsAllInfoList.add(mGoodsHotSaleInfoList);
                    mGoodsExpandableListViewAdapter.setData(mGoodsGroupList, mGoodsAllInfoList);
                    for (int i = 0; i < mGoodsExpandableListViewAdapter.getGroupCount(); i++) {

                        mListView.getRefreshableView().expandGroup(i);

                    }
                    //	UserService.reLogin(false);
                } else {
                    //	Toast.makeText(getApplicationContext(), error.getMesssage(), Toast.LENGTH_LONG).show();

                }
                mListView.onRefreshComplete();

            }

        });
    }

    /**
     * 初始化view
     * @param v
     */
    private void initView(View v) {

        datas = new ArrayList<AdvertiseBean>();
        getDatas=new ArrayList<AdvertisementInfo>();

        v.findViewById(R.id.iv_shop_cart).setOnClickListener(this);

        View listHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.item_shop_header_shop,null);
        btn_shop_discount = (ImageTextButton) listHeaderView.findViewById(R.id.btn_shop_discount);
        btn_shop_recommend = (ImageTextButton) listHeaderView.findViewById(R.id.btn_shop_recommend);
        btn_shop_hot_sale = (ImageTextButton) listHeaderView.findViewById(R.id.btn_shop_hot_sale);
        btn_shop_classify = (ImageTextButton) listHeaderView.findViewById(R.id.btn_shop_classify);
        ad_view = (AdvertisementView) listHeaderView.findViewById(R.id.shop_widget_advertisement);

        btn_shop_discount.setOnClickListener(this);
        btn_shop_recommend.setOnClickListener(this);
        btn_shop_hot_sale.setOnClickListener(this);
        btn_shop_classify.setOnClickListener(this);
        mListView=(PullToRefreshExpandableListView)v.findViewById(R.id.shop_home_list_view);

        mListView.getRefreshableView().addHeaderView(listHeaderView);
        mListView.setOnItemClickListener(this);
        mGoodsExpandableListViewAdapter = new GoodsExpandableListViewAdapter(getActivity());
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                ad_view.setIsRefresh(true);
                fillAdViewData();
                fillListData();
            }
        });
        mListView.getRefreshableView().setAdapter(mGoodsExpandableListViewAdapter);
        mListView.getRefreshableView().setGroupIndicator(null);

        mListView.getRefreshableView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            Intent intent = new Intent();

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                if (groupPosition == 0) {
                    intent.setClass(getActivity(), DiscountAreaActivity.class);
                    startActivity(intent);
                }
                if (groupPosition == 1) {
                    intent.setClass(getActivity(), RecommendAreaActivity.class);
                    startActivity(intent);
                }
                if (groupPosition == 2) {
                    intent.setClass(getActivity(), HotAreaActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        mListView.getRefreshableView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                GoodsInfo mGoodsInfo=mGoodsExpandableListViewAdapter.getChild(groupPosition,childPosition);
                /*
                Log.d("wuxiaoxiang",String.valueOf(mGoodsInfo.goods_id));
                Intent intent=new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("mGoodsInfo", mGoodsInfo);
                intent.putExtras(bundle);

                        intent.setClass(getActivity(),GoodsDetailActivity .class);
                        startActivity(intent);
                        */
                GoodsDetailActivity.startGoodsDetailActivity(getActivity(), mGoodsInfo);

                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.main_menu_shop;
    }

    @Override
    protected int getLeftImageResourceId() {
        return R.mipmap.nav_search;
    }

    /*
    @Override
    protected void onRightClick(MenuItem item) {
        if (JumpType.jump(JumpType.JUMP_TYPE_AUTO_LOGIN)){
            return;
        }
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.menu_item_shop_cart:
                intent.setClass(getActivity(), ShoppingCartActivity.class);
                startActivity(intent);
                break;
        }
    }
    */
    @Override
    protected void onLeftClick() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SearchGoodsActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){

            //优惠
            case R.id.btn_shop_discount:
                intent.setClass(getActivity(), DiscountAreaActivity.class);
                startActivity(intent);
                break;
            //推荐
            case R.id.btn_shop_recommend:
                intent.setClass(getActivity(), RecommendAreaActivity.class);
                startActivity(intent);
                break;
            //热卖
            case R.id.btn_shop_hot_sale:
                intent.setClass(getActivity(), HotAreaActivity.class);
                startActivity(intent);
                break;
            //分类
            case R.id.btn_shop_classify:

                intent.setClass(getActivity(), ClassifyAreaActivity.class);
                startActivity(intent);


                break;
            case R.id.iv_shop_cart:
                //购物车
                if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, getContext())){
                    return;
                }
                intent.setClass(getContext(),ShoppingCartActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO onItem

    }

}




