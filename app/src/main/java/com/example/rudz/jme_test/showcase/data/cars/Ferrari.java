package com.example.rudz.jme_test.showcase.data.cars;

public class Ferrari extends Car {
    private static final String MODEL_PATH = "Models/Ferrari/Car.scene";

    public Ferrari() {
        super(800f, 50f, .5f, 300, MODEL_PATH, CarType.FERRARI, 150f, .3f, .4f);
    }

    public Ferrari(final float acceleration, final float brakeForce, final float turn, final float mass, final float stiffness, final float compressionValue, final float dampnessValue) {
        super(acceleration, brakeForce, turn, mass, MODEL_PATH, CarType.FERRARI, stiffness, compressionValue, dampnessValue);
    }

}