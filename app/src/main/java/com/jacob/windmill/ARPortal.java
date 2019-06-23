package com.jacob.windmill;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;

import com.jacob.windmill.custom.Utils;
import com.viro.core.Object3D;
import com.viro.core.Portal;
import com.viro.core.PortalScene;
import com.viro.core.Scene;
import com.viro.core.Sound;
import com.viro.core.Texture;
import com.viro.core.Vector;
import com.viro.core.ViroContext;

public class ARPortal implements LifecycleObserver {
    private final String portalAsset;
    private final String portalBkgSound;
    private final String portalBkgAsset;
    private final ViroContext viroContext;
    private final Resources resources;
    private boolean soundWasRan = false;
    private Sound mSound;

    public ARPortal(String portalAsset, String portalBkgSound, String portalBkgAsset, ViroContext viroContext, Resources resources) {
        this.portalAsset = portalAsset;
        this.portalBkgSound = portalBkgSound;
        this.portalBkgAsset = portalBkgAsset;
        this.viroContext = viroContext;
        this.resources = resources;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (mSound != null && soundWasRan) mSound.play();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mSound != null) mSound.pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mSound != null) mSound.dispose();
    }

    void show(Scene scene, Vector vector) {
        mSound = new Sound(viroContext, Uri.parse(portalBkgSound), null);
        mSound.setLoop(true);
        mSound.setVolume(1f);

        PortalScene mainPortal = buildPortalScene(portalBkgAsset);
        mainPortal.setPosition(vector);
        scene.getRootNode().addChildNode(mainPortal);
    }

    private PortalScene buildPortalScene(String portalBkgAsset) {
        Object3D model = new Object3D();
        model.loadModel(viroContext, Uri.parse(portalAsset), Object3D.Type.FBX, null);
        Portal portal = new Portal();
        portal.addChildNode(model);


        PortalScene portalScene = new PortalScene();
        portalScene.setPortalEntrance(portal);
        portalScene.setPassable(true);
        portalScene.setScale(new Vector(0.8, 0.8, 0.8));

        portalScene.setEntryListener(new PortalScene.EntryListener() {
            @Override
            public void onPortalEnter(PortalScene portalScene) {
                soundWasRan = true;
                mSound.play();
            }

            @Override
            public void onPortalExit(PortalScene portalScene) {
                mSound.pause();
            }
        });

        Bitmap bkg = Utils.getBitmapFromAssets(portalBkgAsset, resources);
        Texture texture = new Texture(bkg, Texture.Format.RGBA8, true, false);
        portalScene.setBackgroundTexture(texture);
        return portalScene;
    }
}
