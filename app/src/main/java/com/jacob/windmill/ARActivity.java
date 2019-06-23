package com.jacob.windmill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jacob.windmill.custom.ARSceneListener;
import com.jacob.windmill.custom.LifecycleAwareARView;
import com.jacob.windmill.custom.Utils;
import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.Vector;
import com.viro.core.ViroViewARCore;

public class ARActivity extends AppCompatActivity implements ViroViewARCore.StartupListener {
    public static final String TAG = "EXPERIMENT";
    private static final String PORTAL_MODEL = "file:///android_asset/portal_wood_frame.vrx";
    private static final String CANYON_SOUND = "file:///android_asset/canyon_wind.mp3";
    private static final String PORTAL_BKG_ASSET = "canyon.jpg";
    private LifecycleAwareARView mARView;
    private boolean planesFound = false;
    private ARPortal beachPortal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mARView = new LifecycleAwareARView(this, this);
        beachPortal = new ARPortal(PORTAL_MODEL, CANYON_SOUND, PORTAL_BKG_ASSET, mARView.getViroContext(), getResources());
        setContentView(mARView);
        getLifecycle().addObserver(mARView);
        getLifecycle().addObserver(beachPortal);
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
        View.inflate(this, R.layout.viro_view_hud, mARView);
        ARScene scene = createScene();
        mARView.setScene(scene);
        mARView.getScene().getRootNode().addLight(Utils.createLight());

        beachPortal.show(mARView.getScene(), new Vector(0, 0, -2));
    }

    private ARScene createScene() {
        ARScene scene = new ARScene();
        scene.displayPointCloud(true);
        scene.setListener((ARSceneListener) this::handleAnchor);
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
            View hintView = findViewById(R.id.hint_view);
            hintView.animate().alpha(0.0f).setDuration(500);
        }
    }

    @Override
    protected void onDestroy() {
        getLifecycle().removeObserver(mARView);
        getLifecycle().removeObserver(beachPortal);
        super.onDestroy();
    }
}


