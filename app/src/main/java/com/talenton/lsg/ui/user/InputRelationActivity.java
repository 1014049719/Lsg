package com.talenton.lsg.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;

public class InputRelationActivity extends BaseCompatActivity implements View.OnClickListener{

    private EditText mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_relation);

        String name = getIntent().getStringExtra("gxname");
        mName = (EditText)findViewById(R.id.name);
        if (!TextUtils.isEmpty(name)){
            mName.setText(name);
        }
        findViewById(R.id.save_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_ok:
                String name = mName.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    showShortToast("自定义关系不能为空");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("relationName", name);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.fill_relation_new;
    }
}