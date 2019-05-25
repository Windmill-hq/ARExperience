package com.jacob.windmill;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

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
        light.setPosition(new Vector(10, 10, 1));
        light.setAttenuationStartDistance(20);
        light.setAttenuationEndDistance(30);
        light.setIntensity(300);
        return light;
    }
}
