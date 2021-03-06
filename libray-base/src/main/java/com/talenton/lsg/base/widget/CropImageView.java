package com.talenton.lsg.base.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;


/**
 * Created with IntelliJ IDEA.
 * User: kanedong
 * Date: 13-2-28
 */
public class CropImageView extends TouchImageView {

    private Paint mPaint;
    private Paint mCropBoundPaint;
    private RectF mCropRect;
    private RectF mViewRect = new RectF();
    private int mCropWidth = 0, mCropHeight = 0;
    private int mSizeChangeCount;

    public CropImageView(Context context) {
        super(context);
        init();
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mViewRect.set(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewRect.set(0, 0, w, h);

        if (mCropWidth > 0 && mCropHeight > 0) {
            float halfWidth = (w - mCropWidth) / 2;
            float halfHeight = (h - mCropHeight) / 2;
            mCropRect.set(halfWidth, halfHeight, halfWidth + mCropWidth, halfHeight + mCropHeight);
        }

        mSizeChangeCount++;
        if (mInitScaleMode == INIT_SCALE_MODE_FULL_SCREEN && mSizeChangeCount == 2)  {
            measure((int)mCropRect.width(), (int)mCropRect.height());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCropWidth > 0 && mCropHeight > 0) {
            // 关闭硬件加速后绘制裁剪区域遮罩的代码：
/*            canvas.save();
            canvas.clipRect(mViewRect);
            canvas.clipRect(mCropRect, Region.Op.DIFFERENCE);
            canvas.drawRect(mViewRect, mPaint);
            canvas.restore();*/

            /*
             * kane: Android 3.0 开始全面支持通过硬件加速来提高 View 的绘制效率。
             * 但开启硬件加速后 Canvas 原来的一些绘制操作比如 clipPath/drawPicture/drawVertices 等不被支持
             * （参考：http://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported）
             * 为了尽量发挥硬件性能，下面使用一个比较笨的方式来实现开启加速时的裁剪区域遮罩：
             */
            canvas.drawRect(mViewRect.left, mViewRect.top, mViewRect.right, mCropRect.top, mPaint); // Top
            canvas.drawRect(mViewRect.left, mCropRect.bottom, mViewRect.right, mViewRect.bottom, mPaint); // Bottom
            canvas.drawRect(mViewRect.left, mCropRect.top, mCropRect.left, mCropRect.bottom, mPaint); // Left
            canvas.drawRect(mCropRect.right, mCropRect.top, mViewRect.right, mCropRect.bottom, mPaint); // Right

            // Draw crop bound line
            canvas.drawRect(mCropRect.left, mCropRect.top,
                    mCropRect.right, mCropRect.bottom, mCropBoundPaint);
        }
    }

    protected float getXoffset() {
        return (mInitScaleMode == INIT_SCALE_MODE_FULL_SCREEN) ? mCropRect.left : 0;
    }

    protected float getYoffset() {
        return (mInitScaleMode == INIT_SCALE_MODE_FULL_SCREEN) ? mCropRect.top : 0;
    }

    @Override
    protected void onTouchMove(PointF curr, float x, float y) {
        float deltaX = curr.x - last.x;
        float deltaY = curr.y - last.y;

        float relativeScale = saveScale / mInitScale;

        float scaleWidth = Math.round(origWidth * relativeScale);
        float scaleHeight = Math.round(origHeight * relativeScale);

        if (scaleWidth >= mCropWidth) { // 图片宽度不小于裁剪区域
            // 水平方向强制与裁剪边框对齐
            if (x + deltaX > mCropRect.left) {
                deltaX = mCropRect.left - x;
            } else if (x + scaleWidth + deltaX < mCropRect.right) {
                deltaX = mCropRect.right - x - scaleWidth;
            }
        } else { // 图片宽度小于裁剪区域
            // 使图片垂直居中
            deltaX = (mCropRect.left + (mCropWidth - scaleWidth) / 2) - x;
        }

        if (scaleHeight >= mCropHeight) {
            // 竖直方向强制与裁剪边框对齐
            if (y + deltaY > mCropRect.top) {
                deltaY = mCropRect.top - y;
            } else if (y + scaleHeight + deltaY < mCropRect.bottom) {
                deltaY = mCropRect.bottom - y - scaleHeight;
            }
        } else {
            // 使图片水平居中
            deltaY = (mCropRect.top + (mCropHeight - scaleHeight) / 2) - y;
        }
        matrix.postTranslate(deltaX, deltaY);
        last.set(curr.x, curr.y);
    }

    // Set a crop area in center of view with specified size.
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setCropSize(int w, int h) {
        if (w < 0 || h < 0)  return ;

        mCropWidth = w;
        mCropHeight = h;

        if (null == mPaint) {
            mPaint = new Paint();
            mPaint.setARGB(0x90, 0, 0, 0);
        }
        if (null == mCropBoundPaint) {
            mCropBoundPaint = new Paint();
            mCropBoundPaint.setARGB(200, 255, 255, 255);
            mCropBoundPaint.setStyle(Paint.Style.STROKE);
        }

        if (null == mCropRect) {
            mCropRect = new RectF();
        }
        // kane: Canvas 在开启硬件加速时不能支持多 clip 区域交叉运算。
        // 所以当设置了剪切域的时候将硬件加速特性关闭，以便支持 Region.Op.DIFFERENCE。
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }*/
    }

    public final RectF getCropRect() {
        return mCropRect;
    }

    public Bitmap getCropRectBitmap() {
        //BitmapCache.prepareMemoryBeforeLoadBitmap((int)mCropRect.width(),
        //        (int)mCropRect.height());
        Bitmap bm = Bitmap.createBitmap((int)mCropRect.width(),
                (int)mCropRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Matrix matrix = getImageMatrix();
        matrix.postTranslate(-mCropRect.left, -mCropRect.top);
        canvas.setMatrix(matrix);
        getDrawable().draw(canvas);
        return bm;
    }
}
