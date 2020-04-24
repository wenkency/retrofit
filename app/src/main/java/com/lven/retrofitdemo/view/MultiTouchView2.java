package com.lven.retrofitdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MultiTouchView2 extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Path> paths = new SparseArray<>();

    public MultiTouchView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStrokeWidth(Utils.dp2px(2));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        print(event.getActionMasked());

        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);
        int pointerCount = event.getPointerCount();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // 根据ID添加点
                Path path = new Path();
                // event.getX(actionIndex)
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex));
                paths.append(pointerId, path);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < pointerCount; i++) {
                    path = paths.get(event.getPointerId(i));
                    if (path!=null){
                        path.lineTo(event.getX(i), event.getY(i));
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                paths.remove(pointerId);
                invalidate();
                break;
        }
        return true;
    }
    private void print(int action){
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("action","ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("action","ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("action","ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("action","ACTION_UP");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e("action","ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("action","ACTION_CANCEL");
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.valueAt(i);
            if (path != null) {
                canvas.drawPath(path, mPaint);
            }
        }
    }
}
