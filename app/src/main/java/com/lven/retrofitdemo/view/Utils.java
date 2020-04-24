package com.lven.retrofitdemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

public class Utils {
    public static int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                Resources.getSystem().getDisplayMetrics());
    }


    public static Bitmap getAvatarBitmap(Context context, int resId, int width) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, opts);
        opts.inJustDecodeBounds = false;
        opts.inDensity = opts.outWidth;
        opts.inTargetDensity = width;
        return BitmapFactory.decodeResource(context.getResources(), resId, opts);
    }
}
