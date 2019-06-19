package com.jacob.windmill.custom;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.viro.core.Light;
import com.viro.core.OmniLight;
import com.viro.core.Vector;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static Bitmap getBitmapFromAssets(String assetName, Resources resources) {
        AssetManager mAssetManager = resources.getAssets();

        InputStream imageStream;
        try {
            imageStream = mAssetManager.open(assetName);
        } catch (IOException exception) {
            Log.w("Viro", "Unable to find image. Error: " + exception.getMessage());
            return null;
        }
        return BitmapFactory.decodeStream(imageStream);
    }

    public static Light createLight() {
        OmniLight light = new OmniLight();
        light.setColor(Color.WHITE);
        light.setPosition(new Vector(0, 2, -2));
        light.setIntensity(300);
        return light;
    }

    public static Animation getAnim() {
        Animation anim = new ScaleAnimation(1f, 0.8f, 1f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(100);
        return anim;
    }

    public static boolean hasPermissions(Context context) {
        boolean hasRecordPermissions = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == 0;
        boolean hasExternalStoragePerm = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0;
        return hasRecordPermissions && hasExternalStoragePerm;
    }

}
