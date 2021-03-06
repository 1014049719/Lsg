package com.talenton.lsg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.talenton.lsg.R;

import java.util.ArrayList;

/**
 * @author zjh
 * @date 2016/5/13
 */
public class SwitchView extends RelativeLayout implements View.OnClickListener {
    public static final int OPEN = 1;
    public static final int CLOSE = 2;

    private ImageView iv_open;
    private ImageView iv_close;
    private int status = OPEN;

    private OnSwitchListener onSwitchListener;

    public SwitchView(Context context) {
        super(context);
        init(context);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        iv_open = new ImageView(context);
        iv_open.setImageResource(R.mipmap.y_wifi_button);

        iv_close = new ImageView(context);
        iv_close.setImageResource(R.mipmap.n_wifi_button);

        addView(iv_open);
        addView(iv_close);

        setOnClickListener(this);
        chageStatus();
    }

    public void chageStatus(){
        if (status == 1){
            iv_open.setVisibility(VISIBLE);
            iv_close.setVisibility(GONE);
            if (onSwitchListener != null){
                onSwitchListener.open();
            }
        }else {
            iv_open.setVisibility(GONE);
            iv_close.setVisibility(VISIBLE);
            if (onSwitchListener != null){
                onSwitchListener.close();
            }
        }
    }


    public OnSwitchListener getOnSwitchListener() {
        return onSwitchListener;
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        chageStatus();
    }

    @Override
    public void onClick(View v) {
        if (status == OPEN){
            status = CLOSE;
            chageStatus();
        }else {
            status = OPEN;
            chageStatus();
        }
    }

    public interface OnSwitchListener {
        void open();
        void close();
    }


}
