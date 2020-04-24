package com.lven.retrofitdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.lven.retrofitdemo.R;

public class MultiTouchView extends View {
    private Bitmap mBitmap;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float originalOffsetX, originalOffsetY;
    private float offsetX, offsetY;
    private float downX, downY;

    // 响应触摸点的ID
    private int tracePointerId;

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmap = Utils.getAvatarBitmap(context, R.drawable.ic_icon, Utils.dp2px(200));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                tracePointerId = event.getPointerId(0);
                initEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                try {
                    int pointerIndex = event.findPointerIndex(tracePointerId);
                    offsetX = originalOffsetX + event.getX(pointerIndex) - downX;
                    offsetY = originalOffsetY + event.getY(pointerIndex) - downY;
                    invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // 多点
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                tracePointerId = event.findPointerIndex(actionIndex);
                initEvent(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionIndex = event.getActionIndex();
                int actionId = event.findPointerIndex(actionIndex);
                // 如果抬起来的手指是当前响应的手指
                if (actionId == tracePointerId) {
                    int newIndex = event.getPointerCount() - 1;
                    if (actionIndex == event.getPointerCount() - 1) {
                        newIndex--;
                    }
                    if (newIndex >= 0) {
                        tracePointerId = event.findPointerIndex(newIndex);
                        initEvent(event);
                    }
                }
                break;
        }
        return true;
    }

    private void initEvent(MotionEvent event) {
        try {
            int pointerIndex = event.findPointerIndex(tracePointerId);
            downX = event.getX(pointerIndex);
            downY = event.getY(pointerIndex);
            originalOffsetX = offsetX;
            originalOffsetY = offsetY;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, offsetX, offsetY, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap == null) {
            return;
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mBitmap.getHeight());
    }
}
