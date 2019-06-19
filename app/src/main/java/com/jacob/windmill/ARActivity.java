package com.jacob.windmill;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jacob.windmill.custom.ARSceneListener;
import com.jacob.windmill.custom.Utils;
import com.jacob.windmill.custom.ViroViewARCoreLifecycleAware;
import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARPlaneAnchor;
import com.viro.core.ARScene;
import com.viro.core.ClickListener;
import com.viro.core.ClickState;
import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Object3D;
import com.viro.core.Portal;
import com.viro.core.PortalScene;
import com.viro.core.Sound;
import com.viro.core.Surface;
import com.viro.core.Texture;
import com.viro.core.Vector;
import com.viro.core.ViroViewARCore;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ARActivity extends AppCompatActivity implements ViroViewARCore.StartupListener {
    public static final String TAG = "EXPERIMENT";
    private static final String PORTAL_MODEL = "file:///android_asset/portal_wood_frame.vrx";
    private static final String BEACH_NOISE = "file:///android_asset/beach_noise.mp3";
    private static final String PORTAL_BKG_ASSET = "360_waikiki.jpg";
    private ViroViewARCoreLifecycleAware mViroView;
    private boolean planesFound = false;
    private Sound mSound;
    private boolean soundWasRan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViroView = new ViroViewARCoreLifecycleAware(this, this);
        setContentView(mViroView);
        getLifecycle().addObserver(mViroView);
    }

    @Override
    public void onSuccess() {
        buildScene();
    }

    @Override
    public void onFailure(ViroViewARCore.StartupError error, String errorMessage) {
        Log.e(TAG, "Error initializing AR [" + errorMessage + "]");
    }

    private void buildScene() {
        View.inflate(this, R.layout.viro_view_hud, mViroView);
        ARScene scene = createScene();

        mViroView.setScene(scene);
        mViroView.getScene().getRootNode().addLight(Utils.createLight());

//        TrackedPlanesController controller = new TrackedPlanesController(this, mViroView);
//
//        controller.addOnPlaneClickListener(new ClickListener() {
//            @Override
//            public void onClick(int i, Node node, Vector vector) {
//                Toast.makeText(ARActivity.this, "click on plane ", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onClickState(int i, Node node, ClickState clickState, Vector vector) {
//            }
//        });
//        scene.setListener(controller);
        mViroView.getScene().getRootNode().addChildNode(initPortal());
    }

    private PortalScene initPortal() {
        Object3D portalModel = new Object3D();
        portalModel.loadModel(mViroView.getViroContext(), Uri.parse(PORTAL_MODEL), Object3D.Type.FBX, null);
        Portal portal = new Portal();
        portal.addChildNode(portalModel);

        PortalScene portalScene = new PortalScene();
        portalScene.setPosition(new Vector(0, 0, -1));
        portalScene.setPortalEntrance(portal);
        portalScene.setPassable(true);

        mSound = new Sound(mViroView.getViroContext(), Uri.parse(BEACH_NOISE), null);
        mSound.setLoop(true);
        mSound.setVolume(1f);

        portalScene.setEntryListener(new PortalScene.EntryListener() {
            @Override
            public void onPortalEnter(PortalScene portalScene) {
                soundWasRan = true;
                if (!mSound.isPlaying()) mSound.play();
                Toast.makeText(ARActivity.this, "ENTERED", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPortalExit(PortalScene portalScene) {
                mSound.pause();
                Toast.makeText(ARActivity.this, "EXITED", Toast.LENGTH_LONG).show();
            }
        });

        Bitmap bkg = Utils.getBitmapFromAssets(PORTAL_BKG_ASSET, getResources());
        Texture texture = new Texture(bkg, Texture.Format.RGBA8, true, false);
        portalScene.setBackgroundTexture(texture);
        return portalScene;
    }

    private ARScene createScene() {
        ARScene scene = new ARScene();
        scene.displayPointCloud(true);
        scene.setListener(new ARSceneListener() {
            @Override
            public void onAnchorFound(ARAnchor arAnchor, ARNode arNode) {
                handleAnchor(arAnchor, arNode);
            }
        });
        return scene;
    }

    private void handleAnchor(ARAnchor arAnchor, ARNode arNode) {
        if (arAnchor.getType() == ARAnchor.Type.PLANE) {
            Log.w(ARActivity.TAG, "PLANE detected");
            hideUIHint();
        }
    }

    private void hideUIHint() {
        if (!planesFound) {
            planesFound = true;
//            View hintView = findViewById(R.id.hint_view);
//            hintView.animate().alpha(0.0f).setDuration(500);
        }
    }

    @Override
    protected void onPause() {
        if (mSound != null) mSound.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mSound != null && soundWasRan) mSound.play();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mSound != null) mSound.dispose();
        super.onDestroy();
    }


}


