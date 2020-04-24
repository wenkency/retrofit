package com.lven.retrofitdemo.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.lven.retrofitdemo.R;

public class ScaleImageView extends View {
    private Bitmap mBitmap;
    private int mWidth = Utils.dp2px(200);
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mBitmapWidth;
    private int mBitmapHeight;

    // 放大系数
    private static final float OVER_SCALE_FRACTION = 1.5f;
    private boolean isBig;
    private float bigScale;
    private float smallScale;
    private float currentScale;
    // 缩放的动画
    private ObjectAnimator animator;
    private float originalOffsetX, originalOffsetY;
    // 手势偏移
    private float offsetX, offsetY;
    // 手势操作类
    private GestureDetectorCompat detector;
    private ViewOnGestureListener onGestureListener;
    private OverScroller overScroller;
    // 快速划动执行任务：动画
    private FlingTask flingTask;

    ScaleGestureDetector scaleGestureDetector;
    private ScaleGestureListener scaleGestureListener;

    public ScaleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmap = Utils.getAvatarBitmap(context, R.drawable.ic_icon, mWidth);
        // 手势点击、双击、快速划动处理
        onGestureListener = new ViewOnGestureListener();
        detector = new GestureDetectorCompat(context, onGestureListener);
        flingTask = new FlingTask();
        overScroller = new OverScroller(context);

        // 手势缩放
        scaleGestureListener = new ScaleGestureListener();
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()) {
            result = detector.onTouchEvent(event);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        if (mBitmapWidth * 1.0f / mBitmapHeight > w * 1.0f / h) {
            smallScale = w * 1.0f / mBitmapWidth;
            bigScale = h * 1.0f / mBitmapHeight * OVER_SCALE_FRACTION;
        } else {
            smallScale = h * 1.0f / mBitmapHeight;
            bigScale = w * 1.0f / mBitmapWidth * OVER_SCALE_FRACTION;
        }
        originalOffsetX = (w - mBitmapWidth) * 1.0f / 2f;
        originalOffsetY = (h - mBitmapHeight) * 1.0f / 2f;

        currentScale = smallScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // 0 - 1
        float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);
        // 缩放，后面两个参数是缩放的中心点位置
        canvas.scale(currentScale, currentScale, getWidth() * 1.0f / 2f, getHeight() * 1.0f / 2f);
        canvas.drawBitmap(mBitmap, originalOffsetX, originalOffsetY, mPaint);
        canvas.restore();
    }

    class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        float initScale;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // 放大系数
            currentScale = initScale * detector.getScaleFactor();
            currentScale = Math.min(currentScale, bigScale);
            currentScale = Math.max(currentScale, smallScale);
            if (currentScale / bigScale > 0.5f) {
                isBig = true;
            } else {
                isBig = false;
            }
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initScale = currentScale;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }

    class ViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent down, MotionEvent event, float distanceX, float distanceY) {
            // 滚动
            // distanceX:旧位置减新位置
            if (isBig) {
                offsetX -= distanceX;
                offsetY -= distanceY;
                // （图片宽度*缩放 - 控件宽度）/2
                float moveMaxX = (mBitmapWidth * bigScale - getWidth()) / 2;
                float moveMaxY = (mBitmapHeight * bigScale - getHeight()) / 2;
                offsetX = Math.min(offsetX, moveMaxX);
                offsetX = Math.max(offsetX, -moveMaxX);
                offsetY = Math.min(offsetY, moveMaxY);
                offsetY = Math.max(offsetY, -moveMaxY);
                invalidate();
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 快速划动
            if (isBig) {
                int moveMaxX = (int) ((mBitmapWidth * bigScale - getWidth()) / 2);
                int moveMaxY = (int) (mBitmapHeight * bigScale - getHeight()) / 2;
                // 这里只是记录，未执行动画的
                overScroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                        -moveMaxX, moveMaxX, -moveMaxY, moveMaxY, 100, 100);
                // post执行动画
                postOnAnimation(flingTask);
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // 双击（300毫秒内）
            isBig = !isBig;
            animator = getScaleObjectAnimator();
            if (isBig) {
                offsetX = (e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2f) * bigScale / smallScale;
                offsetY = (e.getY() - getHeight() / 2f) - (e.getY() - getHeight() / 2f) * bigScale / smallScale;
                // 从小到大
                animator.start();
            } else {
                // 从大到小
                animator.start();
            }
            return false;
        }
    }

    /**
     * 快速划动动画
     */
    class FlingTask implements Runnable {
        @Override
        public void run() {
            // 计算结果没有结束，继续执行
            if (overScroller.computeScrollOffset()) {
                offsetX = overScroller.getCurrX();
                offsetY = overScroller.getCurrY();
                invalidate();
                postOnAnimation(this);
            }
        }
    }


    private ObjectAnimator getScaleObjectAnimator() {
        if (animator == null) {
            // 由最小缩放到最大缩放
            animator = ObjectAnimator.ofFloat(this,
                    "currentScale",
                    smallScale, bigScale);
        }
        if (isBig) {
            animator.setFloatValues(currentScale, bigScale);
        } else {
            animator.setFloatValues(currentScale, smallScale);
        }

        animator.setDuration(300);
        return animator;
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }
}
