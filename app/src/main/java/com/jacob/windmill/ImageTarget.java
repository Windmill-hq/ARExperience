package com.jacob.windmill;

import android.content.Context;
import android.graphics.Bitmap;

import com.viro.core.ARImageTarget;

public class ImageTarget {
    private ARImageTarget target;

    public ImageTarget(String asset, Context context) {
        Bitmap logo = Utils.getBitmapFromAssets(asset, context.getResources());
        target = new ARImageTarget(logo, ARImageTarget.Orientation.Down, 0.1f);
    }

    public ARImageTarget get() {
        return target;
    }
}
