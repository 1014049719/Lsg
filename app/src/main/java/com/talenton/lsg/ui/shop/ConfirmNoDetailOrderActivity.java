package com.talenton.lsg.ui.shop;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PaymentActivity;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.event.PayCommentEvent;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.AdressInfo;
import com.talenton.lsg.server.bean.shop.BookOrderInfo;
import com.talenton.lsg.server.bean.shop.ClassOrderInfo;
import com.talenton.lsg.server.bean.shop.GoodsCartInfo;
import com.talenton.lsg.server.bean.shop.GoodsOrderInfo;
import com.talenton.lsg.server.bean.shop.PayOkShowData;
import com.talenton.lsg.server.bean.shop.RecvListAdressData;
import com.talenton.lsg.server.bean.shop.SendBookUpdateOrderData;
import com.talenton.lsg.server.bean.shop.SendClassUpdateOrderData;
import com.talenton.lsg.server.bean.shop.SendListAdressData;
import com.talenton.lsg.server.bean.shop.SendPayBookOrderData;
import com.talenton.lsg.server.bean.shop.SendPayByOrderData;
import com.talenton.lsg.server.bean.shop.SendPayClassOrderData;
import com.talenton.lsg.server.bean.shop.SendPayGetOrderData;
import com.talenton.lsg.server.bean.shop.SendUpdateOrderData;
import com.talenton.lsg.ui.shop.adapter.GoodsCartAdapter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/5/25.
 */
public class ConfirmNoDetailOrderActivity extends BaseCompatActivity implements View.OnClickListener{




    private Button mButton;
    private LinearLayout address_LinearLayout;
    private LinearLayout address_picture_LinearLayout;
    private ArrayList<AdressInfo> mAdressInfoList;
    private AdressInfo mDefautAdress;


    private ClassOrderInfo mClassOrderInfo;
    private BookOrderInfo mBookOrderInfo;
    private TextView addressName;
    private TextView addressNumber;
    private ImageView shop_goods_picture;
    private TextView addressDetail;
    private TextView shop_goods_name;
    private TextView shop_goods_price;
    private int payType;
    private ImageButton shop_select_weixin;
    private ImageButton shop_select_alipay;
    private LinearLayout shop_pay_ali;
    private LinearLayout shop_pay_weixin;
    private static final int REQUEST_CODE_PAYMENT_PAY = 1;
    private static final int REQUEST_CODE_SELECT_ADDRESS=2;
    private static final int REQUEST_CODE_ADD_ADDRESS=3;
    String orderid;
    private TextView shop_cart_money;
    private double price;
    private static boolean bOrderSn;//用订单号付款
    private PayOkShowData mPayOkShowData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_shop_confirm_nodetail_order);
        super.onCreate(savedInstanceState);
        if(bOrderSn) {////课程
            mClassOrderInfo = new ClassOrderInfo();
            mClassOrderInfo = (ClassOrderInfo) getIntent().getExtras().getSerializable("mSelectedOrder");
            price =mClassOrderInfo.price;
        }else{
            mBookOrderInfo = new BookOrderInfo();
            mBookOrderInfo = (BookOrderInfo) getIntent().getExtras().getSerializable("mSelectedOrder");
            price =mBookOrderInfo.price;
        }
        initView();
        if(!bOrderSn) {///课程不需要地址
            getAdressList();
        }

    }


    public static void startConfirmNoDetailOrderActivity(Context context, ClassOrderInfo mClassOrderInfo){

        bOrderSn = true;
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mSelectedOrder", mClassOrderInfo);
        intent.putExtras(bundle);
        intent.setClass(context, ConfirmNoDetailOrderActivity.class);
        context.startActivity(intent);
    }

    public static void startConfirmNoDetailOrderActivity(Context context, BookOrderInfo mBookOrderInfo){
        bOrderSn = false;
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mSelectedOrder", mBookOrderInfo);
        intent.putExtras(bundle);
        intent.setClass(context, ConfirmNoDetailOrderActivity.class);
        context.startActivity(intent);
    }

    /**
     * 初始化view
     * @param
     */
    private void initView() {


        mButton = (Button) findViewById(R.id.shopping_buy);
        mButton.setOnClickListener(this);
        shop_goods_picture=(ImageView)findViewById(R.id.shop_goods_picture);
        shop_goods_name=(TextView)findViewById(R.id.shop_goods_name);
        shop_goods_price=(TextView)findViewById(R.id.shop_goods_price);
        mPayOkShowData=new PayOkShowData();
        mPayOkShowData.price=price;

        if (!bOrderSn){
            address_LinearLayout = (LinearLayout) findViewById(R.id.address_LinearLayout);
        address_LinearLayout.setOnClickListener(this);
        mAdressInfoList = new ArrayList<AdressInfo>();
        mDefautAdress = new AdressInfo();

        addressName = (TextView) findViewById(R.id.address_name);
        addressNumber = (TextView) findViewById(R.id.address_number);
        addressDetail = (TextView) findViewById(R.id.address_detail);
            shop_goods_name.setText(mBookOrderInfo.name+"成长书");

        }
        else{
            address_LinearLayout = (LinearLayout) findViewById(R.id.address_LinearLayout);
            address_LinearLayout.setVisibility(View.GONE);
            address_picture_LinearLayout=(LinearLayout)findViewById(R.id.address_picture_LinearLayout);
            address_picture_LinearLayout.setVisibility(View.GONE);
            shop_goods_name.setText(mClassOrderInfo.title);
            ImageLoaderManager.getInstance().displayImage(mClassOrderInfo.imgUrl, shop_goods_picture, ImageLoaderManager.DEFAULT_IMAGE_GRAY_LOADING_DISPLAYER, null, null);
        }
        payType=2;//默认微信
        shop_select_weixin=(ImageButton)findViewById(R.id.shop_select_weixin);
        shop_select_alipay=(ImageButton)findViewById(R.id.shop_select_zhifubao);

        shop_pay_ali=(LinearLayout)findViewById(R.id.shop_pay_ali);
        shop_pay_weixin=(LinearLayout)findViewById(R.id.shop_pay_wx);
        shop_pay_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_select_weixin.setBackgroundResource(R.mipmap.icon_shop_cart_select);
                shop_select_alipay.setBackgroundResource(R.mipmap.icon_shop_cart_not_select);
                payType = 2;
            }
        });
        shop_pay_ali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_select_alipay.setBackgroundResource(R.mipmap.icon_shop_cart_select);
                shop_select_weixin.setBackgroundResource(R.mipmap.icon_shop_cart_not_select);
                payType = 1;
            }
        });

        shop_cart_money=(TextView)findViewById(R.id.shop_cart_money);

        shop_goods_price.setText("￥" + price);
        shop_cart_money.setText("￥" + price);


    }
    @Override
    protected int getTitleResourceId() {
        return R.string.shop_text_confirm_order;
    }

    private  void getAdressList(){
        showProgress("正在获取地址数据，请等待");
        SendListAdressData mSendListAdressData= new SendListAdressData();
        ShopServer.getAdressListData(mSendListAdressData,
                new XLTResponseCallback<RecvListAdressData>() {

                    @Override
                    public void onResponse(RecvListAdressData data, XLTError error) {

                        // TODO Auto-generated method stub

                        if ((error == null) && (data != null)) {

                            mAdressInfoList = data.list;
                            if (findDefaultAddress(mAdressInfoList)) {

                            } else {
                                Toast.makeText(getApplicationContext(), "你还没有填写收货地址", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent();
                                intent.setClass(ConfirmNoDetailOrderActivity.this, AddNewAdressActivity.class);
                                startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);

                            }
                        } else if (error != null) {

                            Toast.makeText(getApplicationContext(), error.getMesssage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "地址返回空值", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        hideProgress();

                    }


                });
    }
    private  void getShowAdressList(){
        showProgress("正在更新地址数据，请等待");
        SendListAdressData mSendListAdressData= new SendListAdressData();
        ShopServer.getAdressListData(mSendListAdressData,
                new XLTResponseCallback<RecvListAdressData>() {

                    @Override
                    public void onResponse(RecvListAdressData data, XLTError error) {

                        // TODO Auto-generated method stub

                        if ((error == null) && (data != null)) {

                            mAdressInfoList.clear();
                            mAdressInfoList = data.list;
                            if (comPareDefaultAddress(mAdressInfoList)) {

                            } else {
                                addressName.setText(null);
                                addressNumber.setText(null);
                                addressDetail.setText(null);

                            }
                        } else if (error != null) {

                            Toast.makeText(getApplicationContext(), error.getMesssage(), Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(), "地址返回空值", Toast.LENGTH_SHORT).show();

                        }
                        hideProgress();

                    }


                });
    }
    private boolean comPareDefaultAddress(ArrayList<AdressInfo> mAdressInfoList){

        for(int i=0;i<mAdressInfoList.size();i++){
            if((mAdressInfoList.get(i).consignee.equals(mDefautAdress.consignee))&&
                    (mAdressInfoList.get(i).mobile.equals(mDefautAdress.mobile))&&
                    (mAdressInfoList.get(i).address.equals(mDefautAdress.address))&&
                    (mAdressInfoList.get(i).area.equals(mDefautAdress.area))){
                return true;
            }
        }

        return false;
    }
    private boolean findDefaultAddress(ArrayList<AdressInfo> mAdressInfoList){

        for(int i=0;i<mAdressInfoList.size();i++){
            if(mAdressInfoList.get(i).is_default==1){
                mDefautAdress=mAdressInfoList.get(i);
                addressName.setText(mDefautAdress.consignee);
                addressNumber.setText(mDefautAdress.mobile);
                addressDetail.setText(mDefautAdress.area+mDefautAdress.address);
                mPayOkShowData.consignee=mDefautAdress.consignee;
                mPayOkShowData.mobile=mDefautAdress.mobile;
                mPayOkShowData.area=mDefautAdress.area;
                mPayOkShowData.address=mDefautAdress.address;
                return true;
            }
        }
        if(mAdressInfoList.size()>0){
            mDefautAdress=mAdressInfoList.get(0);
            addressName.setText(mDefautAdress.consignee);
            addressNumber.setText(mDefautAdress.mobile);
            addressDetail.setText(mDefautAdress.area+mDefautAdress.address);
            mPayOkShowData.consignee=mDefautAdress.consignee;
            mPayOkShowData.mobile=mDefautAdress.mobile;
            mPayOkShowData.area=mDefautAdress.area;
            mPayOkShowData.address=mDefautAdress.address;
            return true;
        }
        return false;
    }
public boolean bHaveAddress() {
    if (addressName.getText().toString().trim().length() == 0) {
        Toast.makeText(getApplicationContext(), "你还没有填写收货地址", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.setClass(ConfirmNoDetailOrderActivity.this, AddNewAdressActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
        return false;
    }
    return true;
}

    public void PaymentTaskBook () {

         if(!bHaveAddress()){
            return;
          }
        Toast.makeText(this, "正在获取后端charge数据，请稍后", Toast.LENGTH_SHORT).show();
        showProgress("正在获取后端charge数据，请等待");
        //按键点击之后的禁用，防止重复点击
        mButton.setOnClickListener(null);
        /*
        mDefautAdress.is_default=1;
        mDefautAdress.address="ggyuggfdd";
        mDefautAdress.consignee="fghyff";
        mDefautAdress.area="甘肃省酒泉市肃北蒙古族自治县";
        mDefautAdress.mobile="18620519838";
        */
        SendPayBookOrderData mSendPaybookOrderData= new SendPayBookOrderData(mBookOrderInfo.package_label,mBookOrderInfo.baobao_id,mBookOrderInfo.num,payType);
        ShopServer.getPayBookOrdercharge(mSendPaybookOrderData,
                new XLTResponseCallback<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject data, XLTError error) {

                        // TODO Auto-generated method stub
                        hideProgress();
                        if (data != null) {

                            try {
                                orderid = data.getString("orderid");

                                pay(data.getString("charge"));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block

                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "charge返回失败", Toast.LENGTH_SHORT).show();

                            finish();
                        }



                    }


                });

    }

    public void PaymentTaskClass () {

        Toast.makeText(this, "正在获取后端charge数据，请稍后", Toast.LENGTH_SHORT).show();
        showProgress("正在获取后端charge数据，请等待");
        //按键点击之后的禁用，防止重复点击
        mButton.setOnClickListener(null);
        /*
        mDefautAdress.is_default=1;
        mDefautAdress.address="ggyuggfdd";
        mDefautAdress.consignee="fghyff";
        mDefautAdress.area="甘肃省酒泉市肃北蒙古族自治县";
        mDefautAdress.mobile="18620519838";
        */
        SendPayClassOrderData mSendPayClassOrderData= new SendPayClassOrderData(mClassOrderInfo.aid,payType);
        ShopServer.getPayClassOrdercharge(mSendPayClassOrderData,
                new XLTResponseCallback<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject data, XLTError error) {

                        // TODO Auto-generated method stub
                        hideProgress();
                        if (data != null) {

                            try {
                                orderid = data.getString("orderid");

                                pay(data.getString("charge"));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block

                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "charge返回失败", Toast.LENGTH_SHORT).show();

                            finish();
                        }



                    }


                });

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.shopping_buy:

                if(bOrderSn) {
                    PaymentTaskClass();
                }else{
                    PaymentTaskBook();
                }

                break;
            case R.id.address_LinearLayout:
                intent.setClass(ConfirmNoDetailOrderActivity.this, SelectAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mDefautAdress", mDefautAdress);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_SELECT_ADDRESS);
                break;

        }
    }

    private void pay(String dataStringcharge){
        Toast.makeText(this, "正在调用支付接口，请等待", Toast.LENGTH_SHORT).show();

        String packageName = getPackageName();
        Intent intent=new Intent();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, dataStringcharge);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT_PAY);
    }
    /////// 发送订单
    public void SendClassOrderInfo(String sOrder, int iResult, String sResult) {
        Toast.makeText(this, "正在发送订单更新", Toast.LENGTH_SHORT).show();
        ShopServer.getClassUpdateOrder(new SendClassUpdateOrderData(sOrder, iResult, sResult),
                new XLTResponseCallback<Object>() {

                    @Override
                    public void onResponse(Object data, XLTError error) {

                        if ((error == null) && (data != null)) {

                            Toast.makeText(getApplicationContext(), "支付接口更新已经返回", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    /////// 发送订单
    public void SendBookOrderInfo(String sOrder, int iResult, String sResult) {
        Toast.makeText(this, "正在发送订单更新", Toast.LENGTH_SHORT).show();
        ShopServer.getBookUpdateOrder(new SendBookUpdateOrderData(sOrder, iResult, sResult),
                new XLTResponseCallback<Object>() {

                    @Override
                    public void onResponse(Object data, XLTError error) {

                        if ((error == null) && (data != null)) {

                            Toast.makeText(getApplicationContext(), "支付接口更新已经返回", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "支付接口已经返回", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_CODE_PAYMENT_PAY) {

            if (resultCode == Activity.RESULT_OK) {

                setResult(RESULT_OK, data);
                String result = data.getExtras().getString("pay_result");
                Log.d("wuxiaoxiang", result);
                mPayOkShowData.bPayOk = false;
                if (bOrderSn) {

				/*
				 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
                    if (result.equals("success")) {
                        SendClassOrderInfo(orderid, 1, "success");
                        PayClassOrderOkActivity.startPayClassOrderOkActivity(this, true);
                        EventBus.getDefault().post(new PayCommentEvent(true));

                    } else if (result.equals("fail")) {
                        SendClassOrderInfo(orderid, 4, "fail");
                        PayClassOrderOkActivity.startPayClassOrderOkActivity(this, false);
                        Toast.makeText(getApplicationContext(), "支付失败，请进入订单再次支付或返回首页", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PayCommentEvent(false));
                    } else if (result.equals("cancel")) {
                        SendClassOrderInfo(orderid, 5, "cancel");
                        PayClassOrderOkActivity.startPayClassOrderOkActivity(this, false);
                        Toast.makeText(getApplicationContext(), "您已经取消支付，请进入订单再次支付或返回首页", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PayCommentEvent(false));
                    } else if (result.equals("invalid")) {
                        SendClassOrderInfo(orderid, 5, "cancel");
                        PayClassOrderOkActivity.startPayClassOrderOkActivity(this, false);
                        Toast.makeText(getApplicationContext(), "您没有安装微信或者支付宝", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PayCommentEvent(false));
                    }

                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                } else {
                    				/*
				 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
                    if (result.equals("success")) {
                        SendBookOrderInfo(orderid, 1, "success");
                        mPayOkShowData.bPayOk = true;
                        PayBookOrderOkActivity.startPayBookOrderOkActivity(this, mPayOkShowData);
                        EventBus.getDefault().post(new PayCommentEvent(true));
                    } else if (result.equals("fail")) {
                        SendBookOrderInfo(orderid, 4, "fail");
                        PayBookOrderOkActivity.startPayBookOrderOkActivity(this, mPayOkShowData);
                        Toast.makeText(getApplicationContext(), "支付失败，请进入订单再次支付或返回首页", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PayCommentEvent(false));
                    } else if (result.equals("cancel")) {
                        SendBookOrderInfo(orderid, 5, "cancel");
                        PayBookOrderOkActivity.startPayBookOrderOkActivity(this, mPayOkShowData);
                        Toast.makeText(getApplicationContext(), "您已经取消支付，请进入订单再次支付或返回首页", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PayCommentEvent(false));
                    } else if (result.equals("invalid")) {
                        SendBookOrderInfo(orderid, 5, "cancel");
                        PayBookOrderOkActivity.startPayBookOrderOkActivity(this, mPayOkShowData);
                        Toast.makeText(getApplicationContext(), "您没有安装微信或者支付宝", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new PayCommentEvent(false));
                    }

                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息

                }
                finish();

            }

        }
                if (requestCode == REQUEST_CODE_SELECT_ADDRESS) {

                    if (resultCode == Activity.RESULT_OK) {

                     //   setResult(RESULT_OK, data);
                        mDefautAdress = (AdressInfo) data.getExtras().getSerializable("mSelected");
                        addressName.setText(mDefautAdress.consignee);
                        addressNumber.setText(mDefautAdress.mobile);
                        addressDetail.setText(mDefautAdress.area + mDefautAdress.address);
                        mPayOkShowData.consignee=mDefautAdress.consignee;
                        mPayOkShowData.mobile=mDefautAdress.mobile;
                        mPayOkShowData.area=mDefautAdress.area;
                        mPayOkShowData.address=mDefautAdress.address;
                    }
                    if(resultCode == Activity.RESULT_CANCELED){
                        getShowAdressList();
                    }
                }
                if (requestCode == REQUEST_CODE_ADD_ADDRESS) {

                    if (resultCode == Activity.RESULT_OK) {

                    //    setResult(RESULT_OK, data);
                        mDefautAdress = (AdressInfo) data.getExtras().getSerializable("mSelected");
                        addressName.setText(mDefautAdress.consignee);
                        addressNumber.setText(mDefautAdress.mobile);
                        addressDetail.setText(mDefautAdress.area+mDefautAdress.address);
                        mPayOkShowData.consignee=mDefautAdress.consignee;
                        mPayOkShowData.mobile=mDefautAdress.mobile;
                        mPayOkShowData.area=mDefautAdress.area;
                        mPayOkShowData.address=mDefautAdress.address;
                    }
                }
                //     finish();

        // Log.d("wuxiaoxiang", "onActivityResult payledouactivity end");
    }

}

