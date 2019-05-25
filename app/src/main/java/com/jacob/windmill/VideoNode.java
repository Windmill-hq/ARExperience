package com.jacob.windmill;

import android.net.Uri;

import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Quad;
import com.viro.core.VideoTexture;
import com.viro.core.ViroContext;

import java.util.Collections;

class VideoNode extends Node {
    private VideoTexture videoTexture;

    public VideoNode(String videoSourceUri, ViroContext context) {
        Uri videoUri = Uri.parse(videoSourceUri);
        videoTexture = new VideoTexture(context, videoUri);
        Material material = new Material();
        material.setDiffuseTexture(videoTexture);
        Quad quad = new Quad(1f, 1f);
        quad.setMaterials(Collections.singletonList(material));
        setGeometry(quad);
    }

    public void play() {
        if (videoTexture != null) videoTexture.play();
    }
}
