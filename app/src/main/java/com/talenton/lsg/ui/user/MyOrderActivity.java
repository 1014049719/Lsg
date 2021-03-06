package com.talenton.lsg.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.ui.user.myOrders.AllOrdersActivity;

public class MyOrderActivity extends BaseCompatActivity implements View.OnClickListener {

    //声明对象
    private LinearLayout layout_allOrders;
    private LinearLayout layout_paying;
    private LinearLayout layout_sending;
    private LinearLayout layout_receiving;
    private LinearLayout layout_express;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //实例化
        layout_allOrders= (LinearLayout) findViewById(R.id.layout_allOrders);
        layout_paying= (LinearLayout) findViewById(R.id.layout_paying);
        layout_sending= (LinearLayout) findViewById(R.id.layout_sending);
        layout_receiving= (LinearLayout) findViewById(R.id.layout_receiving);
        layout_express= (LinearLayout) findViewById(R.id.layout_express);

        //绑定点击事件
        layout_allOrders.setOnClickListener(this);
        layout_paying.setOnClickListener(this);
        layout_sending.setOnClickListener(this);
        layout_receiving.setOnClickListener(this);
        layout_express.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View v) {

        int id=v.getId();
        Intent intent;
        if (id==R.id.layout_allOrders){

            AllOrdersActivity.startAllOrdersActivity(this,5);
        }else if (id==R.id.layout_paying){

            AllOrdersActivity.startAllOrdersActivity(this, 1);
        }else if (id==R.id.layout_sending){

            AllOrdersActivity.startAllOrdersActivity(this, 2);
        }else if (id==R.id.layout_receiving){

            AllOrdersActivity.startAllOrdersActivity(this, 3);
        }else if (id==R.id.layout_express){

            AllOrdersActivity.startAllOrdersActivity(this,4);
        }
    }

    //头部导航
    @Override
    protected int getTitleResourceId() {
        return R.string.order_title;
    }
}
