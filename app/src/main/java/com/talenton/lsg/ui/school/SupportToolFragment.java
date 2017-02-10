package com.talenton.lsg.ui.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.server.SchoolServer;
import com.talenton.lsg.server.bean.school.BaseRspList;
import com.talenton.lsg.server.bean.school.SupportToolData;
import com.talenton.lsg.BaseListFragment;
import com.talenton.lsg.server.bean.shop.GoodsInfo;
import com.talenton.lsg.ui.school.adapter.SupportToolAdapter;
import com.talenton.lsg.ui.shop.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @date 2016/4/8
 */
public class SupportToolFragment extends BaseCompatFragment implements AdapterView.OnItemClickListener {
    private ArrayList<SupportToolData> supportToolDatas;
    private SupportToolAdapter mAdapter;
    private ListView list;
    private static String DATA = "data";

    public static SupportToolFragment newInstance(ArrayList<SupportToolData> supportToolDatas) {
        SupportToolFragment fragment = new SupportToolFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATA,supportToolDatas);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportToolDatas = getArguments().getParcelableArrayList(DATA);
        mAdapter = new SupportToolAdapter(getActivity(),supportToolDatas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_support_tool, container, false);
        list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SupportToolData supportToolData = mAdapter.getItem(position);
        GoodsDetailActivity.startGoodsDetailActivity(getContext(),parseSupportTool(supportToolData));
    }

    private GoodsInfo parseSupportTool(SupportToolData supportToolData){
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.goods_id = (int) supportToolData.getShopid();
        goodsInfo.shop_price = supportToolData.getShopprice();
        goodsInfo.name = supportToolData.getShopname();
        return goodsInfo;
    }
}
