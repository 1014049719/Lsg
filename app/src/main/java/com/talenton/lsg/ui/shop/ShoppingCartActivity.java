package com.talenton.lsg.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.widget.CommonAlertDialog;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.event.PayCommentEvent;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.GoodsCartInfo;
import com.talenton.lsg.server.bean.shop.GoodsInfo;
import com.talenton.lsg.server.bean.shop.RecvShopCartData;
import com.talenton.lsg.server.bean.shop.SendDeleteShopCartData;
import com.talenton.lsg.server.bean.shop.SendShopCartData;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/4/7.
 */
public class ShoppingCartActivity extends BaseCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private ListView mListView;
    private GoodsCartInAdapter mAdapter;
    private GoodsCartInfo mSelected;
    private ArrayList<GoodsCartInfo> mSelectedList;
    private Button mButton;
    private pick_class pickclass[];
    private pick_class_num pickclassnum[];
    public double price;//总价格选择后
    public double priceAll;//所有的总价格
    public int  num;//总数量
    public ImageButton shop_cart_goods_all_pik;
    public LinearLayout LinearLayout_shop_cart_goods_all_pik;
    private CommonAlertDialog mDeleteOkDlg;
    private boolean bSelectAll;
    public TextView shop_cart_money;
    private LoadingViewHolder mLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_shop_shoppingcart);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        fillListData();

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    /**
     * 初始化view
     * @param
     */
    private void initView() {
        bSelectAll=false;
        mListView=(ListView)findViewById(R.id.shop_cart_list_view);
        mButton=(Button)findViewById(R.id.btn_shopping_account);
        mButton.setOnClickListener(this);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new GoodsCartInAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mSelectedList=new ArrayList<GoodsCartInfo>();
        shop_cart_goods_all_pik = (ImageButton)findViewById(R.id.shop_cart_goods_all_pik);
        LinearLayout_shop_cart_goods_all_pik=(LinearLayout)findViewById(R.id.LinearLayout_shop_cart_goods_all_pik);
        LinearLayout_shop_cart_goods_all_pik.setOnClickListener(this);
        shop_cart_money=(TextView)findViewById(R.id.shop_cart_money);
        View loading = findViewById(R.id.shop_cart_loading_container);
        mLoading = new LoadingViewHolder(mListView, loading, this, this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PayCommentEvent payCommentEvent) {

        ///////不管成功和失败都要更新
        fillListData();
    }
    private void fillListData() {
        ShopServer.getMyShopCartData(new SendShopCartData(), new XLTResponseCallback<RecvShopCartData>() {

            @Override
            public void onResponse(RecvShopCartData data, XLTError error) {

                mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_DATA);
                    //获得数据，初始化哪些可能被选中
                    mSelectedList = data.list;
                    mAdapter.setData(data.list);
                    pickclass = new pick_class[data.list.size()];
                    pickclassnum=new pick_class_num[data.list.size()];

                    for (int i = 0; i < data.list.size(); i++) {
                        pickclass[i] = new pick_class();
                        pickclassnum[i]=new pick_class_num();
                        pickclassnum[i].num=1;//////////刚开始默认为1
        //                priceAll+=mSelectedList.get(i).goods_price;

                    }
                    if(data.list.size()==0){
                        mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA_CLICK);
                    }
                } else if(error != null) {
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                }
                else{
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA_CLICK);
                }

            }

        });
    }
    private void deleteGoods(int goodid) {
        ShopServer.deleteMyShopCartData(new SendDeleteShopCartData(goodid), new XLTResponseCallback<Object>() {
            @Override
            public void onResponse(Object data, XLTError error) {

                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    fillListData();
                } else if (error != null) {

                } else {

                }

            }

        });
    }
    @Override
    protected int getTitleResourceId() {
        return R.string.shop_text_shop_cart;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.btn_shopping_account:
                /*
                Bundle bundle = new Bundle();
                bundle.putSerializable("mSelected", mAdapter.getPikGoodsCartInfo());
                intent.putExtras(bundle);
                intent.setClass(ShoppingCartActivity.this, ConfirmOrderActivity.class);
                startActivity(intent);
                */
                if(mAdapter.getPikGoodsCartInfo().size()==0){
                    Toast.makeText(getApplicationContext(), "请选择商品", Toast.LENGTH_SHORT).show();
                }else {
                    ConfirmOrderActivity.startConfirmOrderActivity(this, mAdapter.getPikGoodsCartInfo());
                }
                break;

            case R.id.LinearLayout_shop_cart_goods_all_pik:

                if(bSelectAll){
                    mAdapter.changeSelected(false);
                    bSelectAll=false;
                    shop_cart_money.setText("￥"+"0.0");
                    shop_cart_goods_all_pik.setImageResource((R.mipmap.icon_shop_cart_not_select));}
                else{
                    mAdapter.changeSelected(true);
                    bSelectAll=true;
                    shop_cart_money.setText("￥"+price);
                    shop_cart_goods_all_pik.setImageResource((R.mipmap.icon_shop_cart_select));
                }

                break;

            case R.id.btn_reload_no_data:
            case R.id.empty_action_no_data:

                finish();
                break;

        }
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        GoodsCartInfo mGoodsCartInfo=(GoodsCartInfo)mAdapter.getItem(arg2);
        GoodsInfo mGoodsInfo=new GoodsInfo();
        mGoodsInfo.goods_id=mGoodsCartInfo.goods_id;
     //   mGoodsInfo.url=mGoodsCartInfo.;
        /*
        Intent intent=new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mGoodsInfo", mGoodsInfo);
        intent.putExtras(bundle);
        intent.setClass(this, GoodsDetailActivity.class);
        startActivity(intent);
        */
        GoodsDetailActivity.startGoodsDetailActivity(this,mGoodsInfo);

    }
    @Override

        public boolean onItemLongClick (AdapterView < ? > parent, View view,
        int position, long id){

        GoodsCartInfo mGoodsCartInfo=(GoodsCartInfo)mAdapter.getItem(position);
       final GoodsInfo mGoodsInfo=new GoodsInfo();
        mGoodsInfo.goods_id=mGoodsCartInfo.rec_id;
        //   mGoodsInfo.url=mGoodsCartInfo.;
        if (mDeleteOkDlg == null) {
            mDeleteOkDlg = new CommonAlertDialog(this);
            mDeleteOkDlg.setTitle("确定要将该商品从购物车中删除么");
        }

        String stringText = "删除请点击确定,不删除点击取消";
        mDeleteOkDlg.setMessage(stringText);
        mDeleteOkDlg.setPositiveButton(
                getString(android.R.string.yes),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDeleteOkDlg.dismiss();
                        deleteGoods(mGoodsInfo.goods_id);
                        //////如果删除宝宝的是当前宝宝，要设置当前宝宝

                    }
                });
        mDeleteOkDlg.setNegativeButton(
                getString(android.R.string.no),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDeleteOkDlg.dismiss();
                    }
                });
        mDeleteOkDlg.show();
        return true;

    }
        public class GoodsCartInAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<GoodsCartInfo> mGoodsCart;
        private ArrayList<GoodsCartInfo> mGoodsCartPick;

        public boolean bGetData=false;

        public GoodsCartInAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mGoodsCart = new ArrayList<GoodsCartInfo>();
            mGoodsCartPick=new ArrayList<GoodsCartInfo>();

        }

        public int getCount() {
            return mGoodsCart.size();
        }

        public ArrayList<GoodsCartInfo> getPikGoodsCartInfo() {
            return mGoodsCartPick;
        }
        public void setData(ArrayList<GoodsCartInfo> paths) {
            mGoodsCart.clear();
            mGoodsCart.addAll(paths);

            notifyDataSetChanged();
        }


        public void changeSelected(int position){ //刷新方法点击图片选择的时候

            {

                if(pickclass[position].mbSelect){
                    pickclass[position].mbSelect=false;
                    mGoodsCartPick.remove(mGoodsCart.get(position));
                    double tempmultiply =multiply(mGoodsCart.get(position).goods_price,pickclassnum[position].num);
                    price = subtract(price,tempmultiply);
                  //  price-=mGoodsCart.get(position).goods_price*pickclassnum[position].num;
                    shop_cart_goods_all_pik.setImageResource((R.mipmap.icon_shop_cart_not_select));
                }
                else{

                    pickclass[position].mbSelect=true;
                    double tempmultiply =multiply(mGoodsCart.get(position).goods_price,pickclassnum[position].num);
                    price = add(price, tempmultiply);
            //        price+=mGoodsCart.get(position).goods_price*pickclassnum[position].num;
                    mGoodsCart.get(position).goods_number=pickclassnum[position].num;
                    mGoodsCartPick.add(mGoodsCart.get(position));

                }
                shop_cart_money.setText("￥"+String.valueOf(price));
            }
            notifyDataSetChanged();

        }

            public void changeSelected(int position,boolean b){ //刷新方法点击加减数量的时候

                {

                    if(!b) {
                        if (pickclass[position].mbSelect) {


                            price = subtract(price,mGoodsCart.get(position).goods_price);
                     //       price -= mGoodsCart.get(position).goods_price;

                        }
                    } else{
                        if (pickclass[position].mbSelect) {
                            price = add(price, mGoodsCart.get(position).goods_price);
                    //        price += mGoodsCart.get(position).goods_price ;


                        }

                    }
                    shop_cart_money.setText("￥" + String.valueOf(price));
                    if(pickclass[position].mbSelect){
                        mGoodsCartPick.remove(mGoodsCart.get(position));
                        mGoodsCart.get(position).goods_number=pickclassnum[position].num;
                        mGoodsCartPick.add(mGoodsCart.get(position));
                    }
                }
                notifyDataSetChanged();

            }

        public void changeSelected(boolean bSelect){ //刷新方法点击全部选择的时候

            {

                if(bSelect) {
                    price=0;
                    for (int i = 0; i < mSelectedList.size(); i++) {
                        pickclass[i].mbSelect = true;

                        double tempmultiply =multiply(mSelectedList.get(i).goods_price,pickclassnum[i].num);
                        price = add(price, tempmultiply);
                //        price+=mSelectedList.get(i).goods_price*pickclassnum[i].num;
                    }
                    mGoodsCartPick.clear();
                    mGoodsCartPick.addAll(mSelectedList);
                    shop_cart_money.setText("￥" + String.valueOf(price));

                }else{
                    for (int i = 0; i < mSelectedList.size(); i++) {
                        pickclass[i].mbSelect = false;
                    }
                    price=0;
                    shop_cart_money.setText("￥"+"0.0");
                    mGoodsCartPick.clear();
                }

            }
            notifyDataSetChanged();


        }

        public GoodsCartInfo getItem(int position) {
            return mGoodsCart.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
           ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_shop_cart, parent, false);

                holder.name = (TextView) convertView.findViewById(R.id.shop_cart_goods_name);
                holder.imageButton = (ImageButton)convertView.findViewById(R.id.shop_cart_goods_pik);
                holder.linearLayout_imageButton=(LinearLayout)convertView.findViewById(R.id.LinearLayout_shop_cart_goods_pik);
                holder.price = (TextView)convertView.findViewById(R.id.shop_cart_price);
                holder.goodsnum=(TextView)convertView.findViewById(R.id.shop_cart_sale_number);
                holder.img=(ImageView)convertView.findViewById(R.id.shop_cart_goods_picture);
                holder.imgmin=(ImageView)convertView.findViewById(R.id.icon_shop_min);
                holder.imgpuls=(ImageView)convertView.findViewById(R.id.icon_shop_plus);
                holder.goodsaddnum=(TextView)convertView.findViewById(R.id.shop_cart_number);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.position = position;
            holder.imgmin.setVisibility(View.VISIBLE);
            holder.imgpuls.setVisibility(View.VISIBLE);
            holder.goodsaddnum.setVisibility(View.VISIBLE);
            final GoodsCartInfo item = getItem(position);
            final TextView tvNumber = holder.goodsaddnum;
            final TextView tvNum = holder.goodsnum;
          //  final int positiontemp=holder.position;
            holder.imgmin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    int num = Integer.valueOf(tvNumber.getText().toString()).intValue();
                    if(num>1){
                        num--;
                        tvNumber.setText(String.valueOf(num));
                        tvNum.setText(String.valueOf(num));
                        pickclassnum[position].num=num;
                        changeSelected(position,false);
                    }

                }

            });
            holder.imgpuls.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    int num = Integer.valueOf(tvNumber.getText().toString()).intValue();

                        num++;

                    tvNumber.setText(String.valueOf(num));
                    tvNum.setText(String.valueOf(num));
                    pickclassnum[position].num=num;
                    changeSelected(position,true);
                }

            });
            holder.linearLayout_imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    changeSelected(position);
                }

            });
            holder.name.setText(item.name);
            holder.price.setText("￥"+item.goods_price);
//            holder.goodsnum.setText(item.goods_number);

            if (pickclass[position].mbSelect) {


                holder.imageButton.setImageResource((R.mipmap.icon_shop_cart_select));

            } else {

                holder.imageButton.setImageResource((R.mipmap.icon_shop_cart_not_select));
            }
            ImageLoaderManager.getInstance().displayImage(mGoodsCart.get(position).goods_img, holder.img, ImageLoaderManager.DEFAULT_IMAGE_GRAY_LOADING_DISPLAYER, null, null);

            return convertView;
        }

        public class ViewHolder {
            ImageButton imageButton;
            LinearLayout linearLayout_imageButton;
            TextView name;
            TextView price;
            TextView goodsnum;
            TextView goodsaddnum;
            ImageView img;
            public int position;
            ImageView imgmin;
            ImageView imgpuls;
        }



    }

    public class pick_class{
        public boolean mbSelect;//是否选中
    }

    public class pick_class_num{
        public int num;
    }

    public static double multiply(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    public static double add(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    public static double subtract(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
}
