package com.talenton.lsg.base.widget;

import java.lang.reflect.Field;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * Implement the following functions: </br>
 * 1. auto fit the height of the widget;</br>
 * 2. auto fit the columns in different density devices.</br>
 *
 */
public class OptimizeGridView extends GridView {
    private OnTouchInvalidPositionListener mTouchInvalidPosListener;
    private OnTouchItemListener mOnTouchItemListener;
    public static OnTouchInvalidPositionListener defaultOnTouchInvalidPositionListener = new OptimizeGridView.OnTouchInvalidPositionListener() {
        @Override
        public boolean onTouchInvalidPosition(int motionEvent) {
            return false; // 不终止路由事件让父级控件处理事件
        }
    };

    public OptimizeGridView(Context context) {
        super(context);
    }

    public OptimizeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptimizeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint({ "DrawAllocation", "NewApi" })
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);

        int numColumns = AUTO_FIT;
        final boolean isApi11 = false; //getResources().getBoolean(R.bool.api11);
        if (isApi11) {
            numColumns = getNumColumns();
        } else {
            try {
                Field numColumnsField = GridView.class.getDeclaredField("mNumColumns");
                numColumnsField.setAccessible(true);
                numColumns = numColumnsField.getInt(this);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
        if (numColumns != AUTO_FIT) {
        }
    }

    public interface OptimizeGridAdapter<T> {
        List<T> getItems();

        void setItems(List<T> items);
    }

    public interface OnTouchInvalidPositionListener {
        /**
         * motionEvent 可使用 MotionEvent.ACTION_DOWN 或者
         * MotionEvent.ACTION_UP等来按需要进行判断
         *
         * @return 是否要终止事件的路由
         */
        boolean onTouchInvalidPosition(int motionEvent);
    }

    /**
     * 点击元素时的回调函数
     */
    public interface OnTouchItemListener {
        /**
         * @param motionPosition
         *            触发事件的item位置 motionEvent 可使用 MotionEvent.ACTION_DOWN 或者
         *            MotionEvent.ACTION_UP等来按需要进行判断
         * @return 是否要终止事件的路由
         */
        boolean onTouchItem(View v, int motionPosition, int motionEvent);
    }

    /**
     * 点击空白区域时的响应和处理接口
     */
    public void setOnTouchInvalidPositionListener(OnTouchInvalidPositionListener listener) {
        mTouchInvalidPosListener = listener;
    }

    /**
     * 点击元素的响应和处理接口
     */
    public void setOnTouchItemListener(OnTouchItemListener listener) {
        mOnTouchItemListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            return true; // 禁止GridView滑动

        }

        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mTouchInvalidPosListener == null && mOnTouchItemListener == null) {
            return super.onTouchEvent(event);
        }

        if (!isEnabled()) {
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return isClickable() || isLongClickable();
        }

        final int motionPosition = pointToPosition((int) event.getX(), (int) event.getY());

        if (mTouchInvalidPosListener != null) {
            if (motionPosition == INVALID_POSITION) {
                super.onTouchEvent(event);
                return mTouchInvalidPosListener.onTouchInvalidPosition(event.getActionMasked());
            }
        }

        if (mOnTouchItemListener != null) {
            boolean handleEvent = mOnTouchItemListener.onTouchItem(OptimizeGridView.this,motionPosition, event.getActionMasked());
            if (handleEvent == false) {
                return false;
            }
        }

        return super.onTouchEvent(event);
    }
}
