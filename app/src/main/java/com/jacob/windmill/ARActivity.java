package com.jacob.windmill;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.OmniLight;
import com.viro.core.Vector;
import com.viro.core.ViroView;
import com.viro.core.ViroViewARCore;

public class ARActivity extends Activity implements ARScene.Listener {
    public static final String TAG = "EXPERIMENT";
    private ViroView mViroView;
    private boolean planesFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViroView = new ViroViewARCore(this, new ViroViewARCore.StartupListener() {
            @Override
            public void onSuccess() {
                buildScene();
            }

            @Override
            public void onFailure(ViroViewARCore.StartupError error, String errorMessage) {
                Log.e(TAG, "Error initializing AR [" + errorMessage + "]");
            }
        });
        setContentView(mViroView);
    }

    private void buildScene() {
        View.inflate(this, R.layout.viro_view_hud, mViroView);
        mViroView.setScene(createScene());
        addLight();
    }

    private ARScene createScene() {
        ARScene scene = new ARScene();
        scene.displayPointCloud(true);
        scene.setListener(this);
        return scene;
    }

    private void addLight() {
        OmniLight light = new OmniLight();
        light.setColor(Color.WHITE);
        light.setPosition(new Vector(10, 10, 1));
        light.setAttenuationStartDistance(20);
        light.setAttenuationEndDistance(30);
        light.setIntensity(300);
        mViroView.getScene().getRootNode().addLight(light);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViroView.onActivityStarted(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViroView.onActivityResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViroView.onActivityPaused(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViroView.onActivityStopped(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViroView.onActivityDestroyed(this);
    }

    @Override
    public void onAnchorFound(ARAnchor arAnchor, ARNode arNode) {
        if (arAnchor.getType() == ARAnchor.Type.PLANE) {
            Log.w(ARActivity.TAG, "PLANE detected");
            hideIsTrackingLayoutUI();
        } else if (arAnchor.getType() == ARAnchor.Type.IMAGE) {
            Log.w(ARActivity.TAG, "IMAGE detected");
        }
    }

    @Override
    public void onTrackingInitialized() {
        Log.e(TAG, "onTrackingInitialized");
    }

    @Override
    public void onTrackingUpdated(ARScene.TrackingState trackingState, ARScene.TrackingStateReason trackingStateReason) {
        Log.e(TAG, "onTrackingUpdated");
    }

    @Override
    public void onAmbientLightUpdate(float v, Vector vector) {
        Log.e(TAG, "onAnchorUpdated");
    }

    @Override
    public void onAnchorUpdated(ARAnchor arAnchor, ARNode arNode) {
    }

    @Override
    public void onAnchorRemoved(ARAnchor arAnchor, ARNode arNode) {
        Log.e(TAG, "onAnchorRemoved");
    }

    private void hideIsTrackingLayoutUI() {
        if (!planesFound) {
            planesFound = true;
            View hintView = findViewById(R.id.hint_view);
            hintView.animate().alpha(0.0f).setDuration(1000);
        }
    }
}


