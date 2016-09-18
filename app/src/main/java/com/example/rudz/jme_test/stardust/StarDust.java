package com.example.rudz.jme_test.stardust;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.Control;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <p>
 * Project : jme-test<br>
 * Package : com.example.rudz.jme_test.stardust<br>
 * Created by : rudz<br>
 * On : sep.18.2016 - 17:12
 * </p>
 */
public class StarDust extends Node implements Control {

    private boolean alphaBlending; // make this false if you dont want alpha blending
    private AtomicBoolean enabled;

    private final ArrayList<Vector3f> dustLocations3f; // this houses the locations of all bits of star dust
    private final float[] dustRGBAs; // this houses the specific colour information for each bit of dust
    private final Mesh dustMesh; // the mesh of vertex data
    private final Geometry dustGeometry; // Geometry (the mesh, material, colouring, options)

    private final int numDusts;
    private final Camera cam;
    private Vector3f currentCamLocation;
    private Vector3f previousCamLocation;
    private final float cubedSize;
    private final float cubeSizeDoubled; // to shave off some calculations in update..
    private final Random random;

/** StarDust()
* Name - of the Node (this)
* numDusts - that will appear in the 'cubedSize' surrounding the camera
* cubedSize- the length of the virtual cube (in any direction) that the dust will be located in.
* cam - the camera on which the cube (and so dust) will always "follow".
*/
    public StarDust(String name, int numDusts, float cubedSize, Camera cam, AssetManager assetManager) {
        // Insert the name of the Node
        super(name);

        // The basics
        alphaBlending = true;
        enabled = new AtomicBoolean(true);

        // Init the pseudo random number generator
        random = new Random();

        // Since we cant seem to attach this node to the Camera, lets do it another way
        // Grab the current camera location
        currentCamLocation = cam.getLocation();
        previousCamLocation = currentCamLocation.clone();

        // Now set the StarDust node to the Cam location
        this.setLocalTranslation(currentCamLocation);

        // Init the dust locations and colour arrays
        dustLocations3f = new ArrayList<>(numDusts);
        dustRGBAs = new float[numDusts * 4]; // * 4 because we need to store 4 values for each star dust

        // reference some info!
        this.numDusts = numDusts;
        this.cam = cam;
        cubeSizeDoubled = cubedSize; // this is the factual cubesize, but to save a lot of calculations, it's kept.
        this.cubedSize = cubedSize / 2; // we divide this figure here to avoid division in the update loop

        // Init an initial random distribution of star dust through the virtual cube based on inputs
        for (int dustCount = 0; dustCount < numDusts; dustCount++) {
            // Set the inital locations of each bit of dust
            dustLocations3f.add(dustCount, new Vector3f((random.nextFloat() * (cubeSizeDoubled)) + (currentCamLocation.x - this.cubedSize),
                    (random.nextFloat() * (cubeSizeDoubled)) + (currentCamLocation.y - this.cubedSize),
                    (random.nextFloat() * (cubeSizeDoubled)) + (currentCamLocation.z - this.cubedSize)));

            // Set the initial Colour values (*4 and + to offset 'dustCount' at the correct location in the array)
            dustRGBAs[dustCount * 4] = 1f; // Red
            dustRGBAs[dustCount * 4 + 1] = 1f; // Green
            dustRGBAs[dustCount * 4 + 2] = 1f; // Blue
            dustRGBAs[dustCount * 4 + 3] = 1f; // Alpha
        }

        // create the mesh
        dustMesh = new Mesh();

        // Ladies and gentlemen, Positions please!
        FloatBuffer vertices = BufferUtils.createFloatBuffer(dustLocations3f.toArray(new Vector3f[numDusts]));
        dustMesh.setBuffer(VertexBuffer.Type.Position, 3, vertices);

        // Now colours...
        dustMesh.setBuffer(VertexBuffer.Type.Color, 4, dustRGBAs);

        // Set some options
        dustMesh.setMode(Mesh.Mode.Points);
        //noinspection deprecation
        dustMesh.setPointSize(3f);

        // Dont forget this! ... this solves everything... computer doesnt work, just restart it!
        dustMesh.updateBound();

        // Use the unshaded material type, and we need to define vertex colouring here.
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setBoolean("VertexColor", true);

        // Set alpha blending to use transparent dust when its far away
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        dustGeometry = new Geometry("StarDust");
        dustGeometry.setMesh(dustMesh);
        dustGeometry.setMaterial(material);
        dustGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);

        // attach the spatial to the node
        this.attachChild(dustGeometry);

        // Add the control to the node
        //this.addControl(this); // dont know if "Leaking 'this' in constructor" matters.
    }

    public void update(float tpf) {
        if (!enabled.get()) {
            return;
        }

        // House keeping!
        previousCamLocation = currentCamLocation;
        currentCamLocation = cam.getLocation().clone();
        Vector3f differenceVector = currentCamLocation.subtract(previousCamLocation);

        // Move all dust particles based on their current location, and the difference
        // in movement of the camera position.
        for (int dustCount = 0; dustCount < numDusts; dustCount++) {
            Vector3f d = dustLocations3f.get(dustCount);

            // calculate where the dust should now be along the X axis
            d.x -= differenceVector.x;

            // Make minor modifications if d.X is now out of bounds
            if (d.x < currentCamLocation.x - cubedSize) {
                d.x += cubeSizeDoubled;
            } else if (d.x > currentCamLocation.x + cubedSize) {
                d.x -= cubeSizeDoubled;
            }

            // calculate where the dust should now be along the Y axis
            d.y -= differenceVector.y;

            // Make minor modifications if d.Y is now out of bounds
            if (d.y < currentCamLocation.y - cubedSize) {
                d.y += cubeSizeDoubled;
            } else if (d.y > currentCamLocation.y + cubedSize) {
                d.y -= cubeSizeDoubled;
            }

            // calculate where the dust should now be along the Z axis
            d.z -= differenceVector.z;

            // Make minor modifications if d.Z is now out of bounds
            if (d.z < currentCamLocation.z - cubedSize) {
                d.z += cubeSizeDoubled;
            } else if (d.z > currentCamLocation.z + cubedSize) {
                d.z -= cubeSizeDoubled;
            }
        }

        dustMesh.clearBuffer(VertexBuffer.Type.Position);
        dustMesh.clearBuffer(VertexBuffer.Type.Color);
        dustMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(dustLocations3f.toArray(new Vector3f[dustLocations3f.size()])));
        dustMesh.setBuffer(VertexBuffer.Type.Color, 4, dustRGBAs);
        dustMesh.updateBound();

        dustGeometry.updateModelBound();
        if (alphaBlending) {
            updateAlpha(); // update the alpha on all bits of dust..
        }
    }

    private void updateAlpha() {
        // Get the current location of the camera
        Vector3f viewLocation = cam.getLocation();

        // loop through all the points
        for (int i = 0; i < dustLocations3f.size(); i++) {
            // once we have the distance to the viewer, we can calculate the new alpha
            // anything thats a tiny bit less than the distance between the camera and the edge of the star field (or further)
            // ... and so now we cant see this one
            dustRGBAs[i * 4 + 3] = (viewLocation.distance(dustLocations3f.get(i)) > (cubeSizeDoubled) - 0.001f) ? 0f : 1f; // Alpha (i*4 value offset, then add another 3 value offset to get to the correct alpha)
        }

        // re-enter the colours for the mesh so they can be displayed.
        dustMesh.clearBuffer(VertexBuffer.Type.Color);
        dustMesh.setBuffer(VertexBuffer.Type.Color, 4, dustRGBAs);
    }

    public Control cloneForSpatial(Spatial spatial) {
        return (Control) spatial;
    }

    public void setSpatial(Spatial spatial) {
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public boolean isAlphaBlending() {
        return alphaBlending;
    }

    public void setAlphaBlending(final boolean alphaBlending) {
        this.alphaBlending = alphaBlending;
    }

    public void render(RenderManager rm, ViewPort vp) {
    }
}





/*
public void simpleInitApp() {





// Start the StarDust

// 1500, 300

// 1000 700

        StarDust starDust = new StarDust("StarDust", 1000, 700f, cam, assetManager);

        rootNode.attachChild(starDust);


}
*/