package com.talenton.lsg.ui.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.ShopServer;
import com.talenton.lsg.server.bean.shop.AdressInfo;
import com.talenton.lsg.server.bean.shop.RecvListAdressData;
import com.talenton.lsg.server.bean.shop.SendListAdressData;
import com.talenton.lsg.ui.message.MsgActivity;

import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/5/26.
 */

public class SelectAddressActivity extends BaseCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{


    private ListView mListView;
    private AddressInAdapter mAdapter;
    private LoadingViewHolder mLoading;
    private static final int REQUEST_CODE_MANAGE_ADDRESS=4;
    private AdressInfo mDefautAdress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_shop_select_address);
        super.onCreate(savedInstanceState);
        mDefautAdress=new AdressInfo();
        mDefautAdress = (AdressInfo) getIntent().getExtras().getSerializable("mDefautAdress");
        initView();
        getAdressList();

    }

    public static void startSelectAddressActivity(Context context){
        Intent intent = new Intent(context, SelectAddressActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        //再次进入时自动刷新
        getAdressList();
    }
    /**
     * 初始化view
     * @param
     */
    private void initView() {



        mListView=(ListView)findViewById(R.id.select_adress_list_view);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new AddressInAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        View loading = findViewById(R.id.shop_select_adress_loading_container);
        mLoading = new LoadingViewHolder(mListView, loading, this, this);


    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_shop_manage_address;
    }

    protected void onLeftClick() {

        Intent intent=new Intent();

        setResult(RESULT_CANCELED,intent);
        finish();
    }

    @Override
    protected void onRightClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_shop_manage_address){
            Intent intent=new Intent();
            intent.setClass(SelectAddressActivity.this, ManageAdressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_ADDRESS);
        }
    }
    private  void getAdressList(){
        SendListAdressData mSendListAdressData= new SendListAdressData();
        ShopServer.getAdressListData(mSendListAdressData,
                new XLTResponseCallback<RecvListAdressData>() {

                    @Override
                    public void onResponse(RecvListAdressData data, XLTError error) {
                        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
                        // TODO Auto-generated method stub

                        if ((error == null) && (data != null)) {
                            mLoading.showView(LoadingViewHolder.VIEW_TYPE_DATA);
                            //      mAdressInfoList = data.list;
                            mAdapter.setData(data.list);

                        } else if (error != null) {
                            mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                        } else {
                            mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA);
                        }

                    }


                });
    }
    @Override
    protected int getTitleResourceId() {
        return R.string.shop_text_select_address;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_reload:
            case R.id.empty_action:
                getAdressList();
                break;


        }
    }

    public class AddressInAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<AdressInfo> mAdressInfo;


        public AddressInAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mAdressInfo = new ArrayList<AdressInfo>();


        }

        public int getCount() {
            return mAdressInfo.size();
        }

        public void setData(ArrayList<AdressInfo> paths) {
            mAdressInfo.clear();
            mAdressInfo.addAll(paths);

            notifyDataSetChanged();
        }



        public AdressInfo getItem(int position) {
            return mAdressInfo.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_shop_select_address, parent, false);
                holder.imageButton = (ImageButton)convertView.findViewById(R.id.select_address);
                holder.address_select=(TextView)convertView.findViewById(R.id.select_address_default);
                holder.address_manage_name=(TextView)convertView.findViewById(R.id.select_address_manage_name);
                holder.address_manage_number=(TextView)convertView.findViewById(R.id.select_address_manage_number);
                holder.address_manage_area=(TextView)convertView.findViewById(R.id.select_address_manage_area);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.position = position;
            final AdressInfo item = getItem(position);
            holder.address_manage_name.setText(item.consignee);
            holder.address_manage_number.setText(item.mobile);
            holder.address_manage_area.setText(item.area + item.address);
            if(item.is_default==1){
                holder.address_select.setVisibility(View.VISIBLE);
            }
            else{
                holder.address_select.setVisibility(View.GONE);
            }
            if((item.address_id==mDefautAdress.address_id)||((item.consignee.equals(mDefautAdress.consignee))&&
                    (item.mobile.equals(mDefautAdress.mobile))&&
                    (item.address.equals(mDefautAdress.address))&&
                    (item.area.equals(mDefautAdress.area)))){
                holder.imageButton.setImageResource(R.mipmap.icon_shop_defalut_address);
            }else{
                holder.imageButton.setImageResource(R.mipmap.icon_shop_not_defalut_address);
            }


            holder.imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent=new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mSelected", item);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);

                    finish();
                }

            });


            return convertView;
        }

        public class ViewHolder {
            ImageButton imageButton;
            TextView address_manage_name;
            TextView address_manage_number;
            TextView address_manage_area;
            TextView address_select;
            public int position;
        }



    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_MANAGE_ADDRESS) {

            if (resultCode == Activity.RESULT_OK) {

                setResult(RESULT_OK, data);

                    finish();

                //   AdressInfo  mDefautAdress = (AdressInfo) data.getExtras().getSerializable("mSelected");

            }
        }

        }

}

