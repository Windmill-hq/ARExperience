package com.jacob.windmill.custom;

import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.Vector;

public interface ARSceneListener extends ARScene.Listener {
    @Override
    default void onTrackingInitialized() {
    }

    @Override
    default void onTrackingUpdated(ARScene.TrackingState trackingState, ARScene.TrackingStateReason trackingStateReason) {
    }

    @Override
    default void onAmbientLightUpdate(float v, Vector vector) {

    }

    @Override
    default void onAnchorUpdated(ARAnchor arAnchor, ARNode arNode) {
    }

    @Override
    default void onAnchorRemoved(ARAnchor arAnchor, ARNode arNode) {
    }
}