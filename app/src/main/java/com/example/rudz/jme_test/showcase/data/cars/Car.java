package com.example.rudz.jme_test.showcase.data.cars;


import com.example.rudz.jme_test.showcase.Utility;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author rudz
 */
public abstract class Car {

    public enum CarType {
        FERRARI
    }

    protected float acceleration;
    protected float brakeForce;
    protected float turn;
    protected float mass;
    private float stiffness; //200=f1 car
    private float compValue; //(lower than damp!)
    private float dampValue; // 0.4f;

    private static final Vector3f wheelDirection = new Vector3f(0, -1, 0);
    private static final Vector3f wheelAxle = new Vector3f(-1, 0, 0);
    private final String WHEEL_FRONT_RIGHT = "WheelFrontRight";
    private final String WHEEL_FRONT_LEFT = "WheelFrontLeft";
    private final String WHEEL_BACK_RIGHT = "WheelBackRight";
    private final String WHEEL_BACK_LEFT = "WheelBackLeft";

    protected ArrayList<Geometry> wheels;

    protected VehicleControl player;
    protected Node carNode;
    protected BoundingBox carBox;
    protected AtomicBoolean isLoaded;

    protected final String modelPath;
    protected CarType type;

    private Geometry geomObject;
    private CollisionShape collisionShape;

    protected Car(float acceleration, float brakeForce, float turn, float mass, final String modelPath, final CarType type, final float stifness, final float compressionValue, final float dampnessValue) {
        isLoaded = new AtomicBoolean(false);
        wheels = new ArrayList<>();
        this.acceleration = acceleration;
        this.brakeForce = brakeForce;
        this.turn = turn;
        this.mass = mass;
        this.modelPath = modelPath;
        this.type = type;
        this.stiffness = stifness;
        this.compValue = (compressionValue >= dampnessValue ? dampnessValue - 0.01f : compressionValue);
        this.dampValue = dampnessValue;
    }

    public void loadCar(AssetManager assetManager) {
        if (isLoaded.get()) {
            return;
        }
        carNode = (Node) assetManager.loadModel(modelPath);
        carNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        geomObject = Utility.findGeom(carNode, "Car");
        BoundingBox box = (BoundingBox) geomObject.getModelBound();
        collisionShape = CollisionShapeFactory.createDynamicMeshShape(geomObject);
        configurePlayer();
        loadWheels();
        isLoaded.set(true);
    }

    public void updateCollision() {
        geomObject = Utility.findGeom(carNode, "Car");
        collisionShape = CollisionShapeFactory.createDynamicMeshShape(geomObject);
        player.setCollisionShape(collisionShape);
    }

    private void configurePlayer() {
        player = new VehicleControl(collisionShape, mass);
        player.setSuspensionCompression(compValue * 4.0f * FastMath.sqrt(stiffness));
        player.setSuspensionDamping(dampValue * 5.0f * FastMath.sqrt(stiffness));
        player.setSuspensionStiffness(stiffness);
        player.setMaxSuspensionForce(10000);
        carNode.addControl(player);

    }

    private void loadWheels() {
        wheels.add(Utility.findGeom(carNode, WHEEL_FRONT_RIGHT));
        wheels.get(0).center();
        BoundingBox box = (BoundingBox) wheels.get(0).getModelBound();
        float wheelRadius = box.getYExtent();
        float back_wheel_h = (wheelRadius * 1.7f) - 1f;
        float front_wheel_h = (wheelRadius * 1.9f) - 1f;
        player.addWheel(wheels.get(0).getParent(), box.getCenter().add(0, -front_wheel_h, 0), wheelDirection, wheelAxle, .2f, wheelRadius, true);

        wheels.add(Utility.findGeom(carNode, WHEEL_FRONT_LEFT));
        wheels.get(1).center();
        box = (BoundingBox) wheels.get(1).getModelBound();
        player.addWheel(wheels.get(1).getParent(), box.getCenter().add(0, -front_wheel_h, 0), wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        wheels.add(Utility.findGeom(carNode, WHEEL_BACK_RIGHT));
        wheels.get(2).center();
        box = (BoundingBox) wheels.get(2).getModelBound();
        player.addWheel(wheels.get(2).getParent(), box.getCenter().add(0, -back_wheel_h, 0), wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        wheels.add(Utility.findGeom(carNode, WHEEL_BACK_LEFT));
        wheels.get(3).center();
        box = (BoundingBox) wheels.get(3).getModelBound();
        player.addWheel(wheels.get(3).getParent(), box.getCenter().add(0, -back_wheel_h, 0), wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        player.getWheel(2).setFrictionSlip(6);
        player.getWheel(3).setFrictionSlip(6);
    }

    public Node getCarNode() {
        return carNode;
    }

    public VehicleControl getPlayer() {
        return player;
    }

    public Geometry getWheel(final int index) {
        return wheels.get(index);
    }

    public Geometry getCarGeometry() {
        return geomObject;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getBrakeForce() {
        return brakeForce;
    }

    public void setBrakeForce(float brakeForce) {
        this.brakeForce = brakeForce;
    }

    public float getTurn() {
        return turn;
    }

    public void setTurn(float turn) {
        this.turn = turn;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getStiffness() {
        return stiffness;
    }

    public void setStiffness(float stiffness) {
        this.stiffness = stiffness;
    }

    public float getCompValue() {
        return compValue;
    }

    public void setCompValue(float compValue) {
        this.compValue = compValue;
    }

    public float getDampValue() {
        return dampValue;
    }

    public void setDampValue(float dampValue) {
        this.dampValue = dampValue;
    }

}
