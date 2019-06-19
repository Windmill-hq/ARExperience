package com.jacob.windmill.custom;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.net.Uri;

import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Quad;
import com.viro.core.VideoTexture;
import com.viro.core.ViroContext;

import java.util.Collections;

class VideoNode extends Node implements LifecycleObserver {
    private VideoTexture videoTexture;
    private boolean isStarted = false;

    public VideoNode(String videoSourceUri, ViroContext context) {
        Uri videoUri = Uri.parse(videoSourceUri);
        videoTexture = new VideoTexture(context, videoUri);
        videoTexture.setLoop(true);
        Material material = new Material();
        material.setDiffuseTexture(videoTexture);
        material.setChromaKeyFilteringColor(Color.GREEN);
        material.setChromaKeyFilteringEnabled(true);
        Quad quad = new Quad(2f, 1.5f);
        quad.setMaterials(Collections.singletonList(material));
        setGeometry(quad);
    }

    public void play() {
        if (videoTexture != null && !isStarted) {
            videoTexture.play();
            isStarted = true;
        }
    }

    public void setSize(float width, float height) {
        Quad quad = (Quad) getGeometry();
        quad.setWidth(width);
        quad.setHeight(height);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (videoTexture != null && isStarted) {
            videoTexture.play();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (videoTexture != null && isStarted) {
            videoTexture.pause();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (videoTexture != null) {
            videoTexture.pause();
            videoTexture.dispose();
        }
    }
}
