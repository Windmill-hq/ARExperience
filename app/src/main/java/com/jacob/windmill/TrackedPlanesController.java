package com.jacob.windmill;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.jacob.windmill.custom.Utils;
import com.viro.core.ARAnchor;
import com.viro.core.ARNode;
import com.viro.core.ARPlaneAnchor;
import com.viro.core.ARScene;
import com.viro.core.ClickListener;
import com.viro.core.ClickState;
import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Surface;
import com.viro.core.Texture;
import com.viro.core.Vector;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TrackedPlanesController implements ARScene.Listener {
    private WeakReference<Activity> mCurrentActivityWeak;
    private boolean searchingForPlanesLayoutIsVisible = false;
    private HashMap<String, Node> surfaces = new HashMap<String, Node>();
    private Set<ClickListener> mPlaneClickListeners = new HashSet<ClickListener>();

    @Override
    public void onTrackingUpdated(ARScene.TrackingState trackingState, ARScene.TrackingStateReason trackingStateReason) {
        //no-op
    }

    public TrackedPlanesController(Activity activity, View rootView) {
        mCurrentActivityWeak = new WeakReference<Activity>(activity);
        // Inflate viro_view_hud.xml layout to display a "Searching for surfaces" text view.
//            View.inflate(activity, R.layout.viro_view_hud, ((ViewGroup) rootView));
    }

    public void addOnPlaneClickListener(ClickListener listener) {
        mPlaneClickListeners.add(listener);
    }

    public void removeOnPlaneClickListener(ClickListener listener) {
        if (mPlaneClickListeners.contains(listener)) {
            mPlaneClickListeners.remove(listener);
        }
    }

    @Override
    public void onAnchorFound(ARAnchor arAnchor, ARNode arNode) {
        // Spawn a visual plane if a PlaneAnchor was found
        if (arAnchor.getType() == ARAnchor.Type.PLANE) {
            if (surfaces.containsKey(arAnchor.getAnchorId())) {
                return;
            }

            ARPlaneAnchor planeAnchor = (ARPlaneAnchor) arAnchor;

            // Create the visual geometry representing this plane
            Vector dimensions = planeAnchor.getExtent();
            Surface plane = new Surface(1, 1);
            plane.setWidth(dimensions.x / 4);
            plane.setHeight(dimensions.x / 4);

            // Set a default material for this plane.
            Material material = new Material();
            Bitmap bkg = Utils.getBitmapFromAssets("grid4.png", mCurrentActivityWeak.get().getResources());
            Texture texture = new Texture(bkg, Texture.Format.RGBA8, true, false);

            material.setDiffuseTexture(texture);
//                material.setDiffuseColor(Color.parseColor("#BF000000"));
            plane.setMaterials(Arrays.asList(material));

            // Attach it to the node
            Node planeNode = new Node();
            planeNode.setGeometry(plane);
            planeNode.setRotation(new Vector(-Math.toRadians(90.0), 0, 0));
            planeNode.setPosition(planeAnchor.getCenter());

            // Attach this planeNode to the anchor's arNode
            arNode.addChildNode(planeNode);
            surfaces.put(arAnchor.getAnchorId(), planeNode);

            // Attach click listeners to be notified upon a plane onClick.
            planeNode.setClickListener(new ClickListener() {
                @Override
                public void onClick(int i, Node node, Vector vector) {
                    for (ClickListener listener : mPlaneClickListeners) {
                        listener.onClick(i, node, vector);
                    }
                }

                @Override
                public void onClickState(int i, Node node, ClickState clickState, Vector vector) {
                    //No-op
                }
            });

            // Finally, hide isTracking UI if we haven't done so already.
        }
    }

    @Override
    public void onAnchorUpdated(ARAnchor arAnchor, ARNode arNode) {
        if (arAnchor.getType() == ARAnchor.Type.PLANE) {
            ARPlaneAnchor planeAnchor = (ARPlaneAnchor) arAnchor;

            // Update the mesh surface geometry
            Node node = surfaces.get(arAnchor.getAnchorId());
            Surface plane = (Surface) node.getGeometry();
            Vector dimensions = planeAnchor.getExtent();
            plane.setWidth(dimensions.x);
            plane.setHeight(dimensions.z);
        }
    }

    @Override
    public void onAnchorRemoved(ARAnchor arAnchor, ARNode arNode) {
        surfaces.remove(arAnchor.getAnchorId());
    }

    @Override
    public void onTrackingInitialized() {
        //No-op
    }

    @Override
    public void onAmbientLightUpdate(float lightIntensity, Vector lightColor) {
        //No-op
    }
}
