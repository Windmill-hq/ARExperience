package com.jacob.windmill.custom;

import android.content.Context;
import android.graphics.Bitmap;

import com.viro.core.ARImageTarget;

public class ImageTarget {
    private ARImageTarget target;

    public ImageTarget(String asset, Context context) {
        Bitmap logo = Utils.getBitmapFromAssets(asset, context.getResources());
        target = new ARImageTarget(logo, ARImageTarget.Orientation.Up, 0.188f);
    }

    public ARImageTarget get() {
        return target;
    }
}
