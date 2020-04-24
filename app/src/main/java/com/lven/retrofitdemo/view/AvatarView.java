package com.lven.retrofitdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lven.retrofitdemo.R;

public class AvatarView extends View {
    private Bitmap mAvatarBitmap;
    private int mWidth;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int store = Utils.dp2px(3);

    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mAvatarBitmap = getAvatarBitmap(R.drawable.ic_icon);
        mPaint.setColor(Color.WHITE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAvatarBitmap == null) {
            return;
        }
        // 先绘制一个圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);

        // 2. 离屏缓冲
        int saveLayer = canvas.saveLayer(new RectF(0, 0, getWidth(), getHeight()), mPaint, Canvas.ALL_SAVE_FLAG);


        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - store, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(mAvatarBitmap, 0, 0, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(saveLayer);
    }

    public void setImageUrl(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mAvatarBitmap = fitBitmap(resource,mWidth);
                        invalidate();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public Bitmap getAvatarBitmap(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, opts);
        opts.inJustDecodeBounds = false;
        opts.inDensity = opts.outWidth;
        opts.inTargetDensity = mWidth;
        return BitmapFactory.decodeResource(getResources(), resId, opts);
    }

    public static Bitmap fitBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        matrix.postScale(scaleWidth, scaleWidth);

        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        if (target != null && !target.equals(bmp) && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        return bmp;
    }
}
