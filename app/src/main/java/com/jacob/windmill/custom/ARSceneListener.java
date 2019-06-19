package com.jacob.windmill.custom;

import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.Vector;

public abstract class ARSceneListener implements ARScene.Listener {
    @Override
    public void onTrackingInitialized() {
    }

    @Override
    public void onTrackingUpdated(ARScene.TrackingState trackingState, ARScene.TrackingStateReason trackingStateReason) {
    }

    @Override
    public void onAmbientLightUpdate(float v, Vector vector) {

    }

    @Override
    public void onAnchorUpdated(ARAnchor arAnchor, ARNode arNode) {
    }

    @Override
    public void onAnchorRemoved(ARAnchor arAnchor, ARNode arNode) {
    }
}
