package com.talenton.lsg.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.talenton.lsg.base.R;

public class CornerView extends View {

	private Paint mPaint;

	private RectF mRectF;

    private float mRadius;

	public CornerView(Context context, AttributeSet attrs) {
		super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 
        TypedArray t = getContext().obtainStyledAttributes(attrs,R.styleable.bbg);
        int color = t.getColor(R.styleable.bbg_corner_color, 0xffffffff); 
        mRadius = t.getDimension(R.styleable.bbg_corner_radius, 10);
        mPaint.setColor(color);  
        t.recycle();
	}
	
	public void setColor(int color){
		mPaint.setColor(color);
		invalidate();
	}

	@Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if(mRectF==null){
        	mRectF = new RectF(0, 0, getWidth(), getHeight());
        }
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }  

}