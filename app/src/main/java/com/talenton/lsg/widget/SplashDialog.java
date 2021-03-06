package com.talenton.lsg.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.talenton.lsg.R;

import java.util.ArrayList;

/**
 * Created by ttt on 2016/4/5.
 */
public class SplashDialog extends DialogFragment implements View.OnClickListener{

    private ImageAdapter mAdapter;
    private RadioGroup mDots;
    private View.OnClickListener mOnClickListener;
    private View mJoin;

    public static SplashDialog newInstance() {
        SplashDialog fragment = new SplashDialog();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (getActivity() instanceof View.OnClickListener){
            mOnClickListener = (View.OnClickListener) getActivity();
        }else {
            mOnClickListener = null;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Dialog dialog = new Dialog(getActivity(), R.style.common_dialog);
        dialog.setContentView(R.layout.dialog_splash);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (mDots.getCheckedRadioButtonId() != R.id.dot_5 && getActivity() != null) {
                        dismiss();
                        getActivity().finish();
                    } else {
                        mJoin.performClick();
                    }
                    return true;
                }
                return false;
            }
        });
        ViewPager pager = (ViewPager) dialog.findViewById(R.id.pager);
        mDots = (RadioGroup)dialog.findViewById(R.id.dots);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // This space for rent
                if (mDots != null) {
                    ((RadioButton) mDots.getChildAt(position)).setChecked(true);
                }
            }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ArrayList<View> views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.item_splash_01, null));
        views.add(inflater.inflate(R.layout.item_splash_02, null));
        views.add(inflater.inflate(R.layout.item_splash_03, null));
        views.add(inflater.inflate(R.layout.item_splash_04, null));
        View plash5 = inflater.inflate(R.layout.item_splash_05, null);
        views.add(plash5);
        mJoin = plash5.findViewById(R.id.splash_in);
        mJoin.setOnClickListener(this);

        mAdapter = new ImageAdapter(views);
        pager.setAdapter(mAdapter);
        pager.setCurrentItem(0);
        ((RadioButton) mDots.getChildAt(0)).setChecked(true);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.splash_in) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            dismiss();
        }
    }

    public class ImageAdapter extends PagerAdapter{

        private ArrayList<View> mViews;// 存放View的ArrayList

        public ImageAdapter(ArrayList<View> Views) {
            this.mViews = Views;
        }

        @Override
        public int getCount() {
            if (mViews != null) {
                return mViews.size();
            }
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mViews.get(position);
            ((ViewPager) container).addView(v, 0);
            return v;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
