package com.lven.retrofitdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

public class TwoPager extends ViewGroup {

    public static final String TAG = "TwoPager";
    private ViewConfiguration viewConfiguration;
    private float downX;
    private VelocityTracker tracker;
    private int minVelocity;
    private float maxVelocity;
    private OverScroller overScroller;
    private int downScrollX;
    private boolean scrolling = false;
    // 当前位置
    private int mCurrentPosition;

    public TwoPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        viewConfiguration = ViewConfiguration.get(getContext());
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        tracker = VelocityTracker.obtain();
        overScroller = new OverScroller(getContext());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                scrolling = false;
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动的时候计算一次
                if (!scrolling) {
                    float dx = downX - event.getX();
                    if (Math.abs(dx) > viewConfiguration.getScaledTouchSlop()) {
                        scrolling = true;
                        // 请求父类不要拦截我的事件，我要响应
                        getParent().requestDisallowInterceptTouchEvent(true);
                        result = true;
                    }
                }
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            tracker.clear();
        }
        tracker.addMovement(event);

        float dx = downX - event.getX();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = dx + downScrollX;
                if (distance < 0) {
                    distance = 0;
                } else if (distance > getWidth() * (getChildCount() - 1)) {
                    distance = getWidth() * (getChildCount() - 1);
                }
                scrollTo((int) distance, 0);
                break;
            case MotionEvent.ACTION_UP:
                // 1. 计算是不是快速划动
                tracker.computeCurrentVelocity(1000, maxVelocity);
                float xv = tracker.getXVelocity();
                Log.e(TAG, xv + "");
                int scrollX = getScrollX();
                // 计算返回的位置
                if (Math.abs(xv) > minVelocity) {
                    // 快速划动：xv:左划是负数
                    if (xv < 0) {
                        mCurrentPosition++;
                    } else {
                        mCurrentPosition--;
                    }
                } else {
                    // 正常移动
                    if (dx > 0 && dx > getWidth() / 2) {
                        mCurrentPosition++;
                    } else if (dx < 0 && Math.abs(dx) > getWidth() / 2) {
                        mCurrentPosition--;
                    }
                }
                if (mCurrentPosition <= 0) {
                    mCurrentPosition = 0;
                }
                if (mCurrentPosition >= getChildCount() - 1) {
                    mCurrentPosition = getChildCount() - 1;
                }
                // 自己计算移动的距离
                int scrollDistance = mCurrentPosition * getWidth() - scrollX;
                overScroller.startScroll(scrollX, 0, scrollDistance, 0);
                postInvalidateOnAnimation();
                break;
        }
        return true;
    }

    /**
     * 系统下一帧UI刷新的回调
     */
    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量所有的子类
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = getWidth();
        int bottom = getHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(left, top, right, bottom);
            left = right;
            right += getWidth();
        }
    }
}
