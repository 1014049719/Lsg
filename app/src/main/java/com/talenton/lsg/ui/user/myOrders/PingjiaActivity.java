package com.talenton.lsg.ui.user.myOrders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.ObjectCode;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.event.OrderUpdateEvent;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.MyOrders;
import com.talenton.lsg.server.bean.user.PingjiaParam;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class PingjiaActivity extends BaseCompatActivity {

    //用intent传过来的对象
    MyOrders.GoodslistBean goodslistBean;
    MyOrders myOrders;
    //星级控件
    private RatingBar ratingBar;
    //评论内容
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjia);

        goodslistBean = getIntent().getParcelableExtra("goods_thumb");
        myOrders = getIntent().getParcelableExtra("myOrders");
        AppLogger.d("图片地址" + goodslistBean.getGoods_thumb());

        //加载图片
        ImageView goods_thumb_imageView = (ImageView) findViewById(R.id.goods_thumb_ImageView);
        ImageLoader.getInstance().displayImage(goodslistBean.getGoods_thumb(), goods_thumb_imageView);

        //评级
        final TextView textView = (TextView) findViewById(R.id.pingji_textView);
        textView.setVisibility(View.INVISIBLE);

        //星级
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                textView.setVisibility(View.VISIBLE);
                if (rating<2){
                    textView.setText("差评");
                }else if (rating>4){
                    textView.setText("好评");
                }else {
                    textView.setText("中评");
                }
            }
        });
        editText = (EditText) findViewById(R.id.content_editText);
        //发表评价
        Button button = (Button) findViewById(R.id.pingjia_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //发表评价
                sendPingjia();
            }
        });
    }


    //评价
    protected void sendPingjia() {

        PingjiaParam pingjiaParam = new PingjiaParam();
        pingjiaParam.order_id = Integer.parseInt(myOrders.getOrder_id());
        pingjiaParam.goods_id = Integer.parseInt(goodslistBean.getGoods_id());
        pingjiaParam.comment_rank = (int) ratingBar.getRating();
        pingjiaParam.content = editText.getText().toString();
        pingjiaParam.username = UserServer.getCurrentUser().realname;

        MineServer.pingJiaData(pingjiaParam, new XLTResponseCallback<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode data, XLTError error) {

                if (data != null) {

                    if (data.res == 1) {

                        showShortToast(data.msg);
                        //评价成功 发布通知
                        EventBus.getDefault().post(new OrderUpdateEvent());
                        finish();
                    } else {

                        showShortToast(data.msg);
                    }
                } else {

                    showShortToast(error.getMesssage());
                }
            }
        });
    }

    //头部导航
    @Override
    protected int getTitleResourceId() {
        return R.string.pingjia_title;
    }
}
