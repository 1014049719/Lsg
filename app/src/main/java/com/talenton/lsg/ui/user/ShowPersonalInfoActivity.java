package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.ui.feed.FeedFragment;

public class ShowPersonalInfoActivity extends BaseCompatActivity implements View.OnClickListener{

    long mCreUID;
    String mName, mUrl;

    public static void startShowPersonalInfoActivity(Context context, long creuid, String name, String url){
        Intent intent = new Intent(context, ShowPersonalInfoActivity.class);
        intent.putExtra("creuid", creuid);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_personal_info);

        findViewById(R.id.left).setOnClickListener(this);

        initData();
    }

    private void initData(){
        mCreUID = this.getIntent().getLongExtra("creuid", 0);
        mName = this.getIntent().getStringExtra("name");
        mUrl = this.getIntent().getStringExtra("url");

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(mName);
        ImageView userLogo = (ImageView)findViewById(R.id.user_logo);
        ImageLoader.getInstance().displayImage(mUrl, userLogo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_content,
                FeedFragment.newInstance(CircleListData.CIRCLE_TYPE_MINE_PUBLIC, mCreUID)).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
        }
    }
}
