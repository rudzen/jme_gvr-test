package com.example.rudz.jme_test.showcase;


import com.example.rudz.jme_test.showcase.data.cars.Car;
import com.example.rudz.jme_test.showcase.data.cars.Ferrari;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Matrix3f;
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
public abstract class ShowCase extends SimpleApplication {

    // constants for some stuff.
    private static final String ICON = "assets/Interface/icon.png";
    private static final String SPLASH = "assets/Interface/Splash-small.png";
    private ActionListener movementActionListener;

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


    public ShowCase() {
        super();
        this.setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        //audio = new Audio();
        geomMap = new HashMap<>();
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        /*
        movementActionListener = new MovementListener();
        setupKeys(movementActionListener);
        */
        buildPlayer();

        //createTerrain();
        Terrain.createTerrain(assetManager, rootNode, cam, bulletAppState);

        configureCamera();

        rootNode.attachChild(loadSky());

        /*
        audio.setDefaults(assetManager, audioRenderer);
        audio.nodes.get(audio.AMBIENCE).play();
        audio.nodes.get(audio.WAVES).play();
        */

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.5f, -1f, -0.3f).normalizeLocal());
        rootNode.addLight(dl);

        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0.5f, -0.1f, 0.3f).normalizeLocal());
        rootNode.addLight(dl);
    }

    @Override
    public void update() {
        super.update();
        car.getPlayer().steer(car.getTurn() / 10);
        car.getPlayer().accelerate(-800f);
    }

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

    private class MovementListener implements ActionListener {

        public MovementListener() {
        }

        @Override
        public void onAction(String binding, boolean isPressed, float tpf) {
            switch (binding) {
                case LEFTS:
                    if (isPressed) {
                        steeringValue += car.getTurn();
                    } else {
                        steeringValue -= car.getTurn();
                    }
                    car.getPlayer().steer(steeringValue);
                    break;
                case RIGHTS:
                    if (isPressed) {
                        steeringValue -= car.getTurn();
                    } else {
                        steeringValue += car.getTurn();
                    }
                    car.getPlayer().steer(steeringValue);
                    break;
                case UPS:
                    if (isPressed) {
                        accelerationValue -= car.getAcceleration();
                    } else {
                        accelerationValue += car.getAcceleration();
                    }
                    car.getPlayer().accelerate(accelerationValue);
                    car.getPlayer().setCollisionShape(CollisionShapeFactory.createDynamicMeshShape(car.getCarGeometry()));
                    break;
                case DOWNS:
                    if (isPressed) {
                        accelerationValue += car.getAcceleration();
                    } else {
                        accelerationValue -= car.getAcceleration();
                    }
                    car.getPlayer().accelerate(accelerationValue);
                    car.getPlayer().setCollisionShape(CollisionShapeFactory.createDynamicMeshShape(car.getCarGeometry()));
                    break;
                case RESET:
                    if (isPressed) {
                        car.getPlayer().setPhysicsLocation(new Vector3f(-50f, -25f, 10f));
                        car.getPlayer().setPhysicsRotation(new Matrix3f());
                        car.getPlayer().setLinearVelocity(Vector3f.ZERO);
                        car.getPlayer().setAngularVelocity(Vector3f.ZERO);
                        car.getPlayer().resetSuspension();
                    }
                    break;
                case SPACE:
                    car.getPlayer().brake(isPressed ? car.getBrakeForce() : 0f);
                    break;
            }
        }
    }
}