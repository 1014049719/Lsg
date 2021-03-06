package com.talenton.lsg.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.shop.AdressInfo;
import com.talenton.lsg.ui.user.myOrders.AllOrdersActivity;

/**
 * Created by xiaoxiang on 2016/5/25.
 */
public class PayClassOrderOkActivity extends BaseCompatActivity implements View.OnClickListener{


    private Button pay_ok_to_order;
    private Button pay_ok_to_first;
    private Button pay_error_to_order;
    private Button pay_error_to_first;

    private LinearLayout LinearLayout_pay_ok_to_order;
    private LinearLayout LinearLayout_pay_error_to_order;
    private ImageView icon_shop_pay_order_ok;
    private TextView pay_result_text;
    private static boolean payok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_shop_class_pay_order_ok);
        super.onCreate(savedInstanceState);

        initView();


    }

    public static void startPayClassOrderOkActivity(Context context,boolean payoktemp){
        Intent intent=new Intent();
        intent.setClass(context, PayClassOrderOkActivity.class);
        context.startActivity(intent);
        payok=payoktemp;
    }
    /**
     * 初始化view
     * @param
     */
    private void initView() {


        LinearLayout_pay_ok_to_order=(LinearLayout)findViewById(R.id.LinearLayout_pay_ok_to_order);
        LinearLayout_pay_error_to_order=(LinearLayout)findViewById(R.id.LinearLayout_pay_error_to_order);
        pay_ok_to_order=(Button)findViewById(R.id.pay_ok_to_order);
        pay_ok_to_order.setOnClickListener(this);
        pay_ok_to_first=(Button)findViewById(R.id.pay_ok_to_first);
        pay_ok_to_first.setOnClickListener(this);
        pay_error_to_order=(Button)findViewById(R.id.pay_error_to_order);
        pay_error_to_order.setOnClickListener(this);
        pay_error_to_first=(Button)findViewById(R.id.pay_error_to_first);
        pay_error_to_first.setOnClickListener(this);

        icon_shop_pay_order_ok=(ImageView)findViewById(R.id.icon_shop_pay_order_ok);
        pay_result_text= (TextView)findViewById(R.id.pay_result_text);

        if(payok){

        }else {
            LinearLayout_pay_ok_to_order.setVisibility(View.GONE);
            LinearLayout_pay_error_to_order.setVisibility(View.VISIBLE);
            icon_shop_pay_order_ok.setImageResource(R.mipmap.icon_shop_pay_order_error);
            pay_result_text.setText("支付未成功");

        }

    }
    @Override
    protected int getTitleResourceId() {
        return R.string.shop_text_confirm_order;
    }




    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.pay_ok_to_order:
          //      AllOrdersActivity.startAllOrdersActivity(this,5);
                finish();
                break;
            case R.id.pay_ok_to_first:

                finish();
                break;
            case R.id.pay_error_to_order:
                //      AllOrdersActivity.startAllOrdersActivity(this,5);
                finish();
                break;
            case R.id.pay_error_to_first:

                finish();
                break;
        }
    }

}

