package com.talenton.lsg.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.widget.ImageTextButton;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.AdressInfo;
import com.talenton.lsg.server.bean.shop.SendAddAdressData;
import com.talenton.lsg.util.UIHelper;
import com.talenton.lsg.widget.RegionSelectFragment;

/**
 * Created by xiaoxiang on 2016/4/22.
 */
/*
增加收货地址
 */
public class AddNewAdressActivity extends BaseCompatActivity implements View.OnClickListener {


    private Button mButton;
    private LinearLayout LinearLayout_address_area;
    private LinearLayout LinearLayout_is_default;
    private EditText address_name;
    private EditText address_phone;
    private TextView address_area;
    private EditText address_detail_area;
    private ImageButton ImageButton_is_default;

    String maddress_name;
    String maddress_phone;
    String maddress_area;
    String maddress_detail_area;
    private int maddress_id;
    int is_default=0;
    AdressInfo mAdressInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_shop_add_new_adress);
        super.onCreate(savedInstanceState);
        initView();

    }

    /**
     * 初始化view
     *
     * @param
     */
    private void initView() {

        LinearLayout_address_area=(LinearLayout)findViewById(R.id.LinearLayout_address_area);
        LinearLayout_address_area.setOnClickListener(this);
        LinearLayout_is_default=(LinearLayout)findViewById(R.id.LinearLayout_is_default);
        LinearLayout_is_default.setOnClickListener(this);

        mButton = (Button) findViewById(R.id.add_new_address);
        mButton.setOnClickListener(this);

        ImageButton_is_default=(ImageButton)findViewById(R.id.is_default);
        address_name=(EditText)findViewById(R.id.address_name);
        address_name.setOnClickListener(this);
        address_phone=(EditText)findViewById(R.id.address_phone);
        address_phone.setOnClickListener(this);
        address_area=(TextView)findViewById(R.id.address_area);

        address_detail_area=(EditText)findViewById(R.id.address_detail_area);
        address_detail_area.setOnClickListener(this);
        mAdressInfo=new AdressInfo();

    }

    private boolean isTextData(){
        maddress_name=address_name.getText().toString();
        maddress_phone=address_phone.getText().toString();
        maddress_area=address_area.getText().toString();
        maddress_detail_area=address_detail_area.getText().toString();

        if(address_name.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address_phone.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address_area.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(), "地区不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address_detail_area.getText().toString().trim().length()==0){
            Toast.makeText(getApplicationContext(), "详细地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void addNewAddress() {
        ShopServer.getAddAdress(new SendAddAdressData(maddress_name,maddress_phone,maddress_area,maddress_detail_area,is_default), new XLTResponseCallback<Object>() {

            @Override
            public void onResponse(Object data, XLTError error) {
                // TODO Auto-generated method stub
                if ((error == null) && (data != null)) {

                    Toast.makeText(getApplicationContext(), "添加新地址成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    Bundle bundle = new Bundle();
                    mAdressInfo.consignee=maddress_name;
                    mAdressInfo.mobile=maddress_phone;
                    mAdressInfo.area=maddress_area;
                    mAdressInfo.address=maddress_detail_area;
                    bundle.putSerializable("mSelected", mAdressInfo);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }  else if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getMesssage(), Toast.LENGTH_SHORT).show();
                } else{

                    Toast.makeText(getApplicationContext(), "添加新地址失败，请检查网络", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.shop_text_add_address;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.add_new_address:
               if(isTextData()) {
                   addNewAddress();
               }
                break;
            case R.id.LinearLayout_address_area:
                chooseArea();
                break;
            case R.id.LinearLayout_is_default:
               if(is_default==0){
                   ImageButton_is_default.setBackgroundResource(R.mipmap.icon_shop_set_default_address);
                   is_default=1;
               }else{
                   ImageButton_is_default.setBackgroundResource(R.mipmap.icon_shop_not_set_defalut_address);
                   is_default=0;
               }
                break;



        }
    }
private void  chooseArea() {
    RegionSelectFragment fragment = RegionSelectFragment.newInstance(
            new RegionSelectFragment.OnClickReginSelectListener() {

                @Override
                public void onData(String province, String city, String area) {

                    address_area.setText(province+city+area);

                }
            });
    UIHelper.showDialog(this, fragment, "RegionSelectFragment");
}
}
