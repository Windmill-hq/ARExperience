package com.jacob.windmill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.Vector;
import com.viro.core.ViroViewARCore;

public class ARActivity extends AppCompatActivity implements ViroViewARCore.StartupListener {
    public static final String TAG = "EXPERIMENT";
    private ViroViewARCoreLifecycleAware mViroView;
    private boolean planesFound = false;
    private ImageTarget targetLogo;
    public static final String VIDEO_SOURCE = "file:///android_asset/video.mp4";
    public static final String IMAGE_SOURCE = "logo2.jpg";
    private VideoNode videoNode;

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
        videoNode = new VideoNode(VIDEO_SOURCE, mViroView.getViroContext());
        targetLogo = new ImageTarget(IMAGE_SOURCE, this);
        scene.addARImageTarget(targetLogo.get());
        mViroView.setScene(scene);
        mViroView.getScene().getRootNode().addLight(Utils.createLight());
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
            hideIsTrackingLayoutUI();
        }
        if (arAnchor.getAnchorId().equalsIgnoreCase(targetLogo.get().getId())) {

            Vector pos = arAnchor.getPosition();
            videoNode.setPosition(new Vector(pos.x, pos.y, pos.z));
            videoNode.setRotation(new Vector(0, arAnchor.getRotation().y, 0));
            videoNode.play();
            arNode.addChildNode(videoNode);

            Toast.makeText(this, "IMAGE detected", Toast.LENGTH_SHORT).show();
//            ARScene scene = (ARScene) mViroView.getScene();
//            scene.removeARImageTarget(targetLogo.get());
        }
    }

    private void hideIsTrackingLayoutUI() {
        if (!planesFound) {
            planesFound = true;
            View hintView = findViewById(R.id.hint_view);
            hintView.animate().alpha(0.0f).setDuration(1000);
        }
    }
}


