package com.example.rudz.jme_test.showcase;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

@SuppressWarnings("unused")
public class Main extends MainAbstract {
    private static final String ICON = "assets/Interface/icon.png";
    private static final String SPLASH = "assets/Interface/Splash-small.png";

    private ActionListener movementActionListener;

    public Main() {
        super();
        this.setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
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
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void update() {
        super.update();
        car.getPlayer().steer(car.getTurn() / 10);
        car.getPlayer().accelerate(-800f);
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
