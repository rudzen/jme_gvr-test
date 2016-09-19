//package com.asteroidbelt.effects.shapes;
//
//import com.jme3.effect.shapes.*;
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import java.io.IOException;
//
//public class EmitterSphereShape implements com.jme3.effect.shapes.EmitterShape {
//    private Vector3f center;
//    private float radius;
//
//    public EmitterSphereShape(Vector3f center, float radius) {
//        if (center == null) {
//            throw new IllegalArgumentException("center cannot be null");
//        } else if (radius <= 0.0f) {
//            throw new IllegalArgumentException("Radius must be greater than 0");
//        } else {
//            this.center = center;
//            this.radius = radius;
//        }
//    }
//
//    public com.jme3.effect.shapes.EmitterShape deepClone() {
//        try {
//            EmitterSphereShape clone = (EmitterSphereShape) super.clone();
//            clone.center = this.center.clone();
//            return clone;
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError();
//        }
//    }
//
//    public void getRandomPoint(Vector3f store) {
//        do {
//            store.x = ((FastMath.nextRandomFloat() * 2.0f) - 1.0f) * this.radius;
//            store.y = ((FastMath.nextRandomFloat() * 2.0f) - 1.0f) * this.radius;
//            store.z = ((FastMath.nextRandomFloat() * 2.0f) - 1.0f) * this.radius;
//        } while (store.distance(this.center) > this.radius);
//    }
//
//    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
//        getRandomPoint(store);
//    }
//
//    public Vector3f getCenter() {
//        return this.center;
//    }
//
//    public void setCenter(Vector3f center) {
//        this.center = center;
//    }
//
//    public float getRadius() {
//        return this.radius;
//    }
//
//    public void setRadius(float radius) {
//        this.radius = radius;
//    }
//
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(this.center, "center", null);
//        oc.write(this.radius, "radius", 0.0f);
//    }
//
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        this.center = (Vector3f) ic.readSavable("center", null);
//        this.radius = ic.readFloat("radius", 0.0f);
//    }
//}
