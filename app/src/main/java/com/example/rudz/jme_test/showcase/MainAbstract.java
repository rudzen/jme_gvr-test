package com.example.rudz.jme_test.showcase;


import com.example.rudz.jme_test.showcase.data.cars.Car;
import com.example.rudz.jme_test.showcase.data.cars.Ferrari;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.util.SkyFactory;

import java.util.HashMap;

/**
 *
 * @author rudz
 */
public abstract class MainAbstract extends SimpleApplication {

    protected BulletAppState bulletAppState;
    // constants
    protected final String LEFTS = "Lefts";
    protected final String RIGHTS = "Rights";
    protected final String UPS = "Ups";
    protected final String DOWNS = "Downs";
    protected final String SPACE = "Space";
    protected final String RESET = "Reset";
    // terrain stuff
    protected TerrainQuad terrain;
    // geometry map
    protected HashMap<String, Geometry> geomMap;
    // player stuff
    protected float steeringValue = 0f;
    protected float accelerationValue = 0f;
    Node carNode;
    private ChaseCamera chaseCam;

    protected Audio audio;

    protected Car car;

    protected MainAbstract() {
        audio = new Audio();
        geomMap = new HashMap<>();
    }

    @Override
    public abstract void simpleInitApp();


    protected void configureCamera() {
        //cam.setLocation(player.getPhysicsLocation().mult(20f));
        cam.setFrustumFar(1000);
        chaseCam = new ChaseCamera(cam, car != null ? car.getCarNode() : carNode);
        chaseCam.setSmoothMotion(true);
        chaseCam.setChasingSensitivity(1f);
        chaseCam.setLookAtOffset(new Vector3f(0.0f, 0.0f, 10.0f));
        chaseCam.setMaxVerticalRotation(1f);
        chaseCam.setDefaultDistance(100f);
        chaseCam.setDownRotateOnCloseViewOnly(true);
        chaseCam.setTrailingEnabled(true);
    }

    protected void setupKeys(final ActionListener actionListener) {
        inputManager.addMapping(LEFTS, new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping(LEFTS, new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(RIGHTS, new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping(RIGHTS, new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(UPS, new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping(UPS, new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(DOWNS, new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping(DOWNS, new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addMapping(SPACE, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(RESET, new KeyTrigger(KeyInput.KEY_RETURN));

        inputManager.addListener(actionListener, LEFTS);
        inputManager.addListener(actionListener, RIGHTS);
        inputManager.addListener(actionListener, UPS);
        inputManager.addListener(actionListener, DOWNS);
        inputManager.addListener(actionListener, SPACE);
        inputManager.addListener(actionListener, RESET);
    }

    protected Spatial loadSky() {
        return SkyFactory.createSky(assetManager,
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg"),
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg"),
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg"),
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg"),
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg"),
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg"));
    }

    protected void buildPlayer() {
        //car = new Ferrari();
        float stiffness = 120.0f;//200=f1 car
        float compValue = 0.2f; //(lower than damp!)
        float dampValue = 0.3f;
        final float mass = 400f;

        car = new Ferrari(800f, 50f, .5f, mass, stiffness, compValue, dampValue);
        car.loadCar(assetManager);
        rootNode.attachChild(car.getCarNode());
        bulletAppState.getPhysicsSpace().add(car.getPlayer());
        car.updateCollision();
    }

}