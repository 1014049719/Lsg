package com.talenton.lsg.server;

/**
 * Created by xiaoxiang on 2016/4/18.
 */

import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.server.bean.shop.RecvGoodsIdData;
import com.talenton.lsg.server.bean.shop.RecvLogisticsQueryData;
import com.talenton.lsg.server.bean.shop.RecvAdvertisementData;
import com.talenton.lsg.server.bean.shop.RecvClassifyDetailGoodsData;
import com.talenton.lsg.server.bean.shop.RecvClassifyGoodsData;
import com.talenton.lsg.server.bean.shop.RecvDiscountGoodsData;
import com.talenton.lsg.server.bean.shop.RecvHotGoodsData;
import com.talenton.lsg.server.bean.shop.RecvListAdressData;
import com.talenton.lsg.server.bean.shop.RecvRecommendGoodsData;
import com.talenton.lsg.server.bean.shop.RecvSearchGoodsData;
import com.talenton.lsg.server.bean.shop.RecvSearchHotGoodsData;
import com.talenton.lsg.server.bean.shop.RecvShopCartData;
import com.talenton.lsg.server.bean.shop.RecvShopHomeGoodsData;
import com.talenton.lsg.server.bean.shop.SendAddAdressData;
import com.talenton.lsg.server.bean.shop.SendAddShopCartData;
import com.talenton.lsg.server.bean.shop.SendAdvertisementData;
import com.talenton.lsg.server.bean.shop.SendBookUpdateOrderData;
import com.talenton.lsg.server.bean.shop.SendClassUpdateOrderData;
import com.talenton.lsg.server.bean.shop.SendClassifyDetailGoodsData;
import com.talenton.lsg.server.bean.shop.SendClassifyGoodsData;
import com.talenton.lsg.server.bean.shop.SendDeleteAdressData;
import com.talenton.lsg.server.bean.shop.SendDeleteShopCartData;
import com.talenton.lsg.server.bean.shop.SendDiscountGoodsData;
import com.talenton.lsg.server.bean.shop.SendEditAdressData;
import com.talenton.lsg.server.bean.shop.SendGoodsIdData;
import com.talenton.lsg.server.bean.shop.SendListAdressData;
import com.talenton.lsg.server.bean.shop.SendLogisticsData;
import com.talenton.lsg.server.bean.shop.SendPayBookOrderData;
import com.talenton.lsg.server.bean.shop.SendPayByOrderData;
import com.talenton.lsg.server.bean.shop.SendPayClassOrderData;
import com.talenton.lsg.server.bean.shop.SendPayGetOrderData;
import com.talenton.lsg.server.bean.shop.SendSearchGoodsData;
import com.talenton.lsg.server.bean.shop.SendSearchHotGoodsData;
import com.talenton.lsg.server.bean.shop.SendShopCartData;
import com.talenton.lsg.server.bean.shop.SendShopHomeGoodsData;
import com.talenton.lsg.server.bean.shop.SendUpdateOrderData;

import org.json.JSONObject;

public class ShopServer {
    /**
     * 首页//获取广告业
     */
    public static void getAdvertisement(SendAdvertisementData mSendAdvertisementData, final XLTResponseCallback<RecvAdvertisementData> listener) {


        OkHttpClientManager.getInstance().addGsonRequest1(SendAdvertisementData.URL, RecvAdvertisementData.class,
                new XLTResponseListener<RecvAdvertisementData>() {

                    @Override
                    public void onResponse(RecvAdvertisementData responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendAdvertisementData));
    }



    /**
     * 首页//获取商品数据
     */
    public static void getShopHomeGoodsData(SendShopHomeGoodsData mSendShopHomeGoodsData, final XLTResponseCallback<RecvShopHomeGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendShopHomeGoodsData.URL, RecvShopHomeGoodsData.class,

                new XLTResponseListener<RecvShopHomeGoodsData>() {

                    @Override
                    public void onResponse(RecvShopHomeGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                            }
                            listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendShopHomeGoodsData));

    }

    /**
     * 首页//获取商品数据
     */
    public static void getDiscountGoodsInfo(SendDiscountGoodsData mSendDiscountGoodsData, final XLTResponseCallback<RecvDiscountGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendDiscountGoodsData.URL, RecvDiscountGoodsData.class,

                new XLTResponseListener<RecvDiscountGoodsData>() {

                    @Override
                    public void onResponse(RecvDiscountGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                            }
                            listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendDiscountGoodsData));

    }

    /**
     * 首页//获取商品数据
     */
    public static void getRecommendGoodsInfo(SendDiscountGoodsData mSendDiscountGoodsData, final XLTResponseCallback<RecvRecommendGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendDiscountGoodsData.URL, RecvRecommendGoodsData.class,

                new XLTResponseListener<RecvRecommendGoodsData>() {

                    @Override
                    public void onResponse(RecvRecommendGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                            }
                            listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendDiscountGoodsData));

    }

    /**
     * 首页//获取商品数据
     */
    public static void getHotGoodsInfo(SendDiscountGoodsData mSendDiscountGoodsData, final XLTResponseCallback<RecvHotGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendDiscountGoodsData.URL, RecvHotGoodsData.class,

                new XLTResponseListener<RecvHotGoodsData>() {

                    @Override
                    public void onResponse(RecvHotGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                            }
                            listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendDiscountGoodsData));

    }

    /**
     * 首页//获取商品数据
     */
    public static void getClassifyGoodsInfo(SendClassifyGoodsData mSendClassifyGoodsData, final XLTResponseCallback<RecvClassifyGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendClassifyGoodsData.URL, RecvClassifyGoodsData.class,

                new XLTResponseListener<RecvClassifyGoodsData>() {

                    @Override
                    public void onResponse(RecvClassifyGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                            }
                            listener.onResponse(responseData, errorData);
                        }

                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendClassifyGoodsData));

    }

    /**
     * 首页//获取支付Charge
     */
    public static void getPayOrdercharge(SendPayGetOrderData mSendPayGetOrderData, final XLTResponseCallback<JSONObject> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(OkHttpClientManager.TYPE_SHOP, SendPayGetOrderData.URL, JSONObject.class,

                new XLTResponseListener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {


                            }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendPayGetOrderData), 30000);

    }
    /**
     * 首页//获取支付Charge
     */
    public static void getPayByOrdercharge(SendPayByOrderData mSendPayByOrderData, final XLTResponseCallback<JSONObject> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(OkHttpClientManager.TYPE_SHOP, SendPayByOrderData.URL, JSONObject.class,

                new XLTResponseListener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseData, XLTError errorData) {
                        if ((errorData == null) && (responseData != null)) {

                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendPayByOrderData), 30000);

    }

    /**
     * 首页//获取支付Charge
     */
    public static void getPayClassOrdercharge(SendPayClassOrderData mSendPayClassOrderData, final XLTResponseCallback<JSONObject> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(SendPayClassOrderData.URL, JSONObject.class,

                new XLTResponseListener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseData, XLTError errorData) {
                        if ((errorData == null) && (responseData != null)) {

                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendPayClassOrderData), 30000);

    }

    /**
     * 首页//获取支付Charge
     */
    public static void getPayBookOrdercharge(SendPayBookOrderData mSendPayBookOrderData, final XLTResponseCallback<JSONObject> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(SendPayBookOrderData.URL, JSONObject.class,

                new XLTResponseListener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendPayBookOrderData), 30000);

    }
    /**
     * 首页//搜索数据
     */

    public static void getShopSearchGoodsData(SendSearchGoodsData mSendSearchGoodsData, final XLTResponseCallback<RecvSearchGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendSearchGoodsData.URL, RecvSearchGoodsData.class,

                new XLTResponseListener<RecvSearchGoodsData>() {

                    @Override
                    public void onResponse(RecvSearchGoodsData responseData, XLTError errorData) {
                        if ((errorData == null) && (responseData != null)) {

                        } else {

                        }
                        listener.onResponse(responseData, errorData);
                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendSearchGoodsData));

    }

    /**
     * 首页//搜索数据
     */

    public static void getShopSearchHotGoodsData(SendSearchHotGoodsData mSendSearchHotGoodsData, final XLTResponseCallback<RecvSearchHotGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendSearchHotGoodsData.URL, RecvSearchHotGoodsData.class,

                new XLTResponseListener<RecvSearchHotGoodsData>() {

                    @Override
                    public void onResponse(RecvSearchHotGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                        }
                        listener.onResponse(responseData, errorData);
                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendSearchHotGoodsData));

    }

    /**
     * 首页//搜索数据
     */

    public static void getClassifyDetailGoodsData(SendClassifyDetailGoodsData mSendClassifyDetailGoodsData, final XLTResponseCallback<RecvClassifyDetailGoodsData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendClassifyDetailGoodsData.URL, RecvClassifyDetailGoodsData.class,

                new XLTResponseListener<RecvClassifyDetailGoodsData>() {

                    @Override
                    public void onResponse(RecvClassifyDetailGoodsData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null) ){

                        } else {

                            }
                            listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendClassifyDetailGoodsData));

    }
    /**
     * 编辑地址
     */
    public static void getEditAdress(SendEditAdressData mSendEditAdressData,
                                          final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendEditAdressData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {

                        if (errorData == null) {


                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                },  OkHttpClientManager.getInstance().getmGson().toJson(mSendEditAdressData));
    }

    /**
     * 编辑地址
     */
    public static void getLogistics(SendLogisticsData mSendLogisticsData,
                                     final XLTResponseCallback<RecvLogisticsQueryData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(OkHttpClientManager.TYPE_NO_PREV, mSendLogisticsData.url, RecvLogisticsQueryData.class,
                new XLTResponseListener<RecvLogisticsQueryData>() {

                    @Override
                    public void onResponse(RecvLogisticsQueryData responseData, XLTError errorData) {

                        if (errorData == null) {


                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, "");
    }
    /**
     * 删除地址
     */
    public static void getDeleteAdress(SendDeleteAdressData mSendDeleteAdressData,
                                         final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendDeleteAdressData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {

                        if (errorData == null) {


                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendDeleteAdressData));
    }
    /**
     * 新增地址
     */
    public static void getAddAdress(SendAddAdressData mSendAddAdressData,
                                     final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendAddAdressData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {

                        if (errorData == null) {


                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                },  OkHttpClientManager.getInstance().getmGson().toJson(mSendAddAdressData));
    }

    /**
     * 获取购物车数据
     */
    public static void getGoodsInfoByIdData(SendGoodsIdData mSendGoodsIdData,
                                         final XLTResponseCallback<RecvGoodsIdData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendGoodsIdData.URL, RecvGoodsIdData.class,
                new XLTResponseListener<RecvGoodsIdData>() {

                    @Override
                    public void onResponse(RecvGoodsIdData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null)) {

                        } else {

                        }
                        listener.onResponse(responseData, errorData);
                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendGoodsIdData));
    }


    /**
     * 获取购物车数据
     */
    public static void getMyShopCartData(SendShopCartData mSendShopCartData,
                                    final XLTResponseCallback<RecvShopCartData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendShopCartData.URL, RecvShopCartData.class,
                new XLTResponseListener<RecvShopCartData>() {

                    @Override
                    public void onResponse(RecvShopCartData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData != null)) {

                        } else {

                            }
                        listener.onResponse(responseData, errorData);
                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendShopCartData));
    }

    /**
     * 删除购物车数据
     */
    public static void deleteMyShopCartData(SendDeleteShopCartData mSendDeleteShopCartData,
                                         final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendDeleteShopCartData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {

                        if (errorData == null) {


                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                },  OkHttpClientManager.getInstance().getmGson().toJson(mSendDeleteShopCartData));
    }

    /**
     * 增加到我的购物车
     */
    public static void addMyShopCartData(SendAddShopCartData mSendAddShopCartData,
                                            final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendAddShopCartData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {

                        if (errorData == null) {


                        } else {


                        }
                        listener.onResponse(responseData, errorData);

                    }
                },  OkHttpClientManager.getInstance().getmGson().toJson(mSendAddShopCartData));
    }

    /**
     * 获得地址列表
     */
    public static void getAdressListData(SendListAdressData mSendListAdressData,
                                    final XLTResponseCallback<RecvListAdressData> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendListAdressData.URL, RecvListAdressData.class,
                new XLTResponseListener<RecvListAdressData>() {

                    @Override
                    public void onResponse(RecvListAdressData responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData!=null)) {

                        }else {

                            }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendListAdressData));
    }

    /**
     * 订单更新
     */
    public static void getClassUpdateOrder(SendClassUpdateOrderData mSendClassUpdateOrderData,
                                         final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(SendClassUpdateOrderData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData!=null)) {

                        }else {

                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendClassUpdateOrderData));
    }

    /**
     * 订单更新
     */
    public static void getBookUpdateOrder(SendBookUpdateOrderData mSendBookUpdateOrderData,
                                           final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest(SendBookUpdateOrderData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData!=null)) {

                        }else {

                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendBookUpdateOrderData));
    }

    /**
     * 订单更新
     */
    public static void getUpdateOrder(SendUpdateOrderData mSendUpdateOrderData,
                                      final XLTResponseCallback<Object> listener) {

        OkHttpClientManager.getInstance().addGsonRequest1(SendUpdateOrderData.URL, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {
                        if ((errorData == null)&&(responseData!=null)) {

                        }else {

                        }
                        listener.onResponse(responseData, errorData);

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(mSendUpdateOrderData));
    }
    /**
     * 商品列表，下拉，刷新用
     */
    public static class ShopDiscountListServer implements IListServer<RecvDiscountGoodsData> {

        @Override
        public void getData(int page, int limit, final XLTResponseCallback<RecvDiscountGoodsData> listener) {

            ShopServer.getDiscountGoodsInfo(new SendDiscountGoodsData(2,page), new XLTResponseCallback<RecvDiscountGoodsData>() {

                @Override
                public void onResponse(RecvDiscountGoodsData data, XLTError error) {
                    // TODO Auto-generated method stub
                    if ((error == null) && (data != null)) {


                    } else {


                    }
                    listener.onResponse(data,error);

                }

            });
        }

        @Override
        public RecvDiscountGoodsData getCacheData() {
            return null;
        }

    }

    /**
     * 商品列表，下拉，刷新用
     */
    public static class ShopRecommendListServer implements IListServer<RecvRecommendGoodsData> {

        @Override
        public void getData(int page, int limit, final XLTResponseCallback<RecvRecommendGoodsData> listener) {

            ShopServer.getRecommendGoodsInfo(new SendDiscountGoodsData(1,page), new XLTResponseCallback<RecvRecommendGoodsData>() {

                @Override
                public void onResponse(RecvRecommendGoodsData data, XLTError error) {
                    // TODO Auto-generated method stub
                    if ((error == null) && (data != null)) {


                    } else {


                    }
                    listener.onResponse(data, error);

                }

            });
        }

        @Override
        public RecvRecommendGoodsData getCacheData() {
            return null;
        }
    }

    /**
     * 商品列表，下拉，刷新用
     */
    public static class ShopHotListServer implements IListServer<RecvHotGoodsData> {

        @Override
        public void getData(int page, int limit, final XLTResponseCallback<RecvHotGoodsData> listener) {

            ShopServer.getHotGoodsInfo(new SendDiscountGoodsData(3,page), new XLTResponseCallback<RecvHotGoodsData>() {

                @Override
                public void onResponse(RecvHotGoodsData data, XLTError error) {
                    // TODO Auto-generated method stub
                    if ((error == null) && (data != null)) {


                    } else {


                    }
                    listener.onResponse(data, error);

                }

            });
        }

        @Override
        public RecvHotGoodsData getCacheData() {
            return null;
        }
    }
    /**
     * 商品列表，下拉，刷新用
     */
    public static class ShopSearchListServer implements IListServer<RecvSearchGoodsData> {

        public String keywords;
        public ShopSearchListServer(String keywords){this.keywords=keywords;}
        @Override
        public void getData(int page, int limit, final XLTResponseCallback<RecvSearchGoodsData> listener) {

            ShopServer.getShopSearchGoodsData(new SendSearchGoodsData(keywords), new XLTResponseCallback<RecvSearchGoodsData>() {

                @Override
                public void onResponse(RecvSearchGoodsData data, XLTError error) {
                    // TODO Auto-generated method stub
                    if ((error == null) && (data != null)) {


                    } else {


                    }
                    listener.onResponse(data, error);


                }

            });
        }

        @Override
        public RecvSearchGoodsData getCacheData() {
            return null;
        }

    }

    /**
     * 商品列表，下拉，刷新用
     */
    public static class ShopClassifyDetailListServer implements IListServer<RecvClassifyDetailGoodsData> {

        public int cat_id;
        public int orderprice;
        public int ordersalesnum;/////1降序，2 升序
        public ShopClassifyDetailListServer(int cat_id,int orderprice,int ordersalesnum){this.cat_id=cat_id;
        this.orderprice=orderprice;this.ordersalesnum=ordersalesnum;}
        @Override
        public void getData(int page, int limit, final XLTResponseCallback<RecvClassifyDetailGoodsData> listener) {
            SendClassifyDetailGoodsData mSendClassifyDetailGoodsData=new SendClassifyDetailGoodsData(cat_id,orderprice,ordersalesnum,page);
            ShopServer.getClassifyDetailGoodsData(mSendClassifyDetailGoodsData, new XLTResponseCallback<RecvClassifyDetailGoodsData>() {

                @Override
                public void onResponse(RecvClassifyDetailGoodsData data, XLTError error) {
                    // TODO Auto-generated method stub
                    if ((error == null) && (data != null)) {


                    } else {


                    }
                    listener.onResponse(data, error);

                }

            });
        }

        @Override
        public RecvClassifyDetailGoodsData getCacheData() {
            return null;
        }

    }
}
