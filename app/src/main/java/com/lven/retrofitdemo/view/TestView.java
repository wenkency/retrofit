package com.lven.retrofitdemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class TestView extends View {
    private int mWidth, mHeight;
    private Paint mPaint;
    //
    private float mRadius;
    private RectF mOval;
    // 预留出来的角度
    float mAngle = 120;
    // 开始角度
    float mStartAngle = 90 + mAngle / 2;
    float mSweepAngle = 360 - mAngle;
    // 刻度
    PathEffect mDashPathEffect;
    private Path mArcPath;
    private int mMark = 20;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(Utils.dp2px(2));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        // 半径
        mRadius = Math.min(mWidth, mHeight) * 0.70f / 2;
        // 扇形绘制的矩形区域
        mOval = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        // 扇形路径
        mArcPath = new Path();
        mArcPath.addArc(mOval, mStartAngle, mSweepAngle);

        // 计算扇形的路径长度
        PathMeasure pathMeasure = new PathMeasure(mArcPath, false);
        float length = pathMeasure.getLength() - Utils.dp2px(2);

        // 刻度
        Path dash = new Path();
        dash.addRect(new RectF(0, 0, Utils.dp2px(2), Utils.dp2px(10)), Path.Direction.CW);

        float advance = length / mMark; // 在路径里面绘制多少个刻度
        mDashPathEffect = new PathDashPathEffect(dash, advance, 0, PathDashPathEffect.Style.ROTATE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.translate(mWidth / 2, mHeight / 2);
        // 画弧
        canvas.drawPath(mArcPath, mPaint);
        // 画刻度
        mPaint.setPathEffect(mDashPathEffect);
        canvas.drawPath(mArcPath, mPaint);
        mPaint.setPathEffect(null);
        // 画指针
        int mark = 5;
        canvas.drawLine(0, 0,
                (float) Math.cos(Math.toRadians(getMarkAngle(mark))) * mRadius*0.8f,
                (float) Math.sin(Math.toRadians(getMarkAngle(mark))) * mRadius*0.8f,
                mPaint);

        canvas.restore();
    }

    private double getMarkAngle(int mark) {
        if (mark <= 0) {
            return mStartAngle;
        }
        return mStartAngle + mSweepAngle / mMark * mark;
    }


}
