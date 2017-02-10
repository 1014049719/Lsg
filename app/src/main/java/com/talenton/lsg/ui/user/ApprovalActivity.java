package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.talenton.lsg.BaseListActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.school.BaseRspList;
import com.talenton.lsg.server.bean.user.MyApprovalData;
import com.talenton.lsg.server.bean.user.ReqListApproval;
import com.talenton.lsg.ui.user.adapter.ApprovalAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApprovalActivity extends BaseListActivity {
    public static final String SCHOOL_ID = "school_id";
    private List<MyApprovalData> myApprovalDatas;
    private MineServer.ApprovalListServer approvalListServer;
    private long schoolId;

    public static void startApprovalActivity(Context context,long schoolId){
        Intent intent = new Intent(context,ApprovalActivity.class);
        intent.putExtra(SCHOOL_ID,schoolId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        fillData();

    }

    private void fillData() {
        schoolId = getIntent().getLongExtra(SCHOOL_ID,0);
        myApprovalDatas = new ArrayList<>();
        mAdapter = new ApprovalAdapter(this,myApprovalDatas);
        mPullRefreshListView.getRefreshableView().setAdapter(mAdapter);
        approvalListServer = new MineServer.ApprovalListServer(new ReqListApproval(schoolId));
        startGetData(approvalListServer, new ListResponseCallback() {
            @Override
            public void onSuccess(BaseRspList data) {
                if (data.getList() != null && !data.getList().isEmpty()) {
                    mAdapter.addAll(data.getList());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(XLTError error) {

            }
        });
    }

    @Override
    protected int getPullRefreshListViewResId() {
        return R.id.list;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.mine_text_my_approval;
    }
}
