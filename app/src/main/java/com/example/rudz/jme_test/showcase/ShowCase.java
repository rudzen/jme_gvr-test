package com.example.rudz.jme_test.showcase;


import com.example.rudz.jme_test.showcase.data.cars.Car;
import com.example.rudz.jme_test.showcase.data.cars.Ferrari;
import com.example.rudz.jme_test.stardust.StarDust;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;

import java.util.List;

/**
 * @author rudz
 */
public class ShowCase extends SimpleApplication {

    private static final String TAG = "ShowCase";

    // constants for some stuff.
    private static final String ICON = "assets/Interface/icon.png";
    private static final String SPLASH = "assets/Interface/Splash-small.png";

    private static final String LIGHTNING = "Common/MatDefs/Light/Lighting.j3md";


    private ActionListener movementActionListener;

    private BulletAppState bulletAppState;
    // constants
    private final String LEFTS = "Lefts";
    private final String RIGHTS = "Rights";
    private final String UPS = "Ups";
    private final String DOWNS = "Downs";
    private final String SPACE = "Space";
    private final String RESET = "Reset";
    // player stuff
    private float steeringValue = 0f;
    private float accelerationValue = 0f;

    private ChaseCamera chaseCam;

    private Audio audio;

    private Car car;

    private Node stars;
    private SpotLight light;
    private Material mat;
    private Node observer;
    private float maxDistance = 75f;

    private StarDust sd;

    private BitmapText points_base;
    private BitmapText points;
    private long score;

    public ShowCase() {
        super();
        //this.setShowSettings(false);
    }

    private void preInit() {
        //cam = getCamera();
        //audio = new Audio();
    }


    @Override
    public void simpleInitApp() {
        preInit();

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // CAR

        //buildPlayer();
        //movementActionListener = new MovementListener();
        //setupKeys(movementActionListener);

        //configureCamera();

        /*
        audio.setDefaults(assetManager, audioRenderer);
        audio.nodes.get(audio.MUSIC).play();
        */

        sd = new StarDust("StarDust", 200, 700f, getCamera(), assetManager);
        sd.addControl(sd);


        // add startravel to prove a point!

        cam = getCamera();

        sd = new StarDust("StarDust", 200, 700f, cam, assetManager);
        sd.addControl(sd);

        // add startravel to prove a point!
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        mat = new Material(assetManager, LIGHTNING);
        mat.setColor("Diffuse", ColorRGBA.Magenta);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);

        observer = new Node("");
        rootNode.attachChild(observer);

        /* TODO: Remove these, but for now I just want something to show
        getRenderManager().removePostView(guiViewPort);
        guiNode.detachAllChildren();
         */

        stars = new Node();
        sd.attachChild(stars);

        rootNode.attachChild(sd);

        initStars(b);

        light = new SpotLight();
        light.setSpotOuterAngle(FastMath.QUARTER_PI);
        stars.addLight(light);

        initGUI();
        score = 0;

    }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public void simpleUpdate(final float tpf) {
        super.simpleUpdate(tpf);
        sd.update(tpf);

        // update cubes
        List<Spatial> starList = stars.getChildren();
        for (Spatial s : starList) {
            s.move(-cam.getDirection().x, -cam.getDirection().y, -.4f);
            if (s.getWorldTranslation().z < -maxDistance) {
                s.setLocalTranslation(FastMath.nextRandomFloat() * 100 - 50, FastMath.nextRandomFloat() * 100 - 50, maxDistance);
                if (s.getLocalTranslation().x < 3f && s.getLocalTranslation().x > -3f && s.getLocalTranslation().y < 3f && s.getLocalTranslation().y > -3f) {
                    points.setText(Long.toString(++score));
                }
            }
        }
        light.setDirection(getCamera().getDirection());
    }

    private void initGUI() {
        points_base = initText("Points: ");
        points = initText("0");
        final Vector3f pos = new Vector3f(guiViewPort.getCamera().getWidth() / 2 - (points_base.getLineWidth() / 2), guiViewPort.getCamera().getHeight() - (points_base.getHeight() * 8), 0);
        points_base.setLocalTranslation(pos);
        points.setLocalTranslation(pos.addLocal(points_base.getLineWidth(), 0, 0));
        guiNode.attachChild(points_base);
        guiNode.attachChild(points);
    }

    private BitmapText initText(final String text) {
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(text);
        return helloText;
    }


    private Geometry createLensFlare(Node baseNode) {
        Geometry flare = new Geometry("Flare", new Quad(150.0f, 75.0f));
        flare.move(-140.0f, -32.0f, 75.0f);
        flare.setQueueBucket(RenderQueue.Bucket.Transparent);
        flare.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/lens_flare.png"));
        mat.getAdditionalRenderState().setDepthTest(true);
        mat.getAdditionalRenderState().setDepthWrite(true);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
        flare.setMaterial(mat);
        //baseNode.attachChild(flare);
        return flare;
    }

    protected void configureCamera() {
        //cam.setLocation(player.getPhysicsLocation().mult(20f));
        cam.setFrustumFar(1000);
        chaseCam = new ChaseCamera(cam, car.getCarNode());
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
                assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg"), new Vector3f(0f, 0f, 0f), 1f);
    }

    protected void buildPlayer() {
        float stiffness = 120.0f;//200=f1 car
        float compValue = 0.2f; //(lower than damp!)
        float dampValue = 0.3f;
        final float mass = 400f;

        car = new Ferrari(800f, 50f, .5f, mass, stiffness, compValue, dampValue);
        car.loadCar(assetManager);
        bulletAppState.getPhysicsSpace().add(car.getPlayer());
        car.updateCollision();
        rootNode.attachChild(car.getCarNode());
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

    private void initStars(final Box baseBox) {
        Geometry star;
        for (int i = 0; i < 300; i++) {
            star = new Geometry("Star" + Integer.toString(i), baseBox.clone());
            star.setMaterial(mat);
            star.rotate(FastMath.nextRandomFloat() * FastMath.TWO_PI, FastMath.nextRandomFloat() * FastMath.TWO_PI, FastMath.nextRandomFloat() * FastMath.TWO_PI);
            star.setLocalTranslation(FastMath.nextRandomFloat() * 100 - 50, FastMath.nextRandomFloat() * 100 - 50, FastMath.nextRandomFloat() * maxDistance * 2 - maxDistance);
            stars.attachChild(star);
        }
    }
}