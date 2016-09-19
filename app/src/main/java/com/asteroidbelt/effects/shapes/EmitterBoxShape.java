//package com.asteroidbelt.effects.shapes;
//
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.util.clone.Cloner;
//
//import java.io.IOException;
//
//public class EmitterBoxShape implements com.jme3.effect.shapes.EmitterShape {
//    private Vector3f len;
//    private Vector3f min;
//
//    public EmitterBoxShape(Vector3f min, Vector3f max) {
//        if (min == null || max == null) {
//            throw new IllegalArgumentException("min or max cannot be null");
//        }
//        this.min = min;
//        this.len = new Vector3f();
//        this.len.set(max).subtractLocal(min);
//    }
//
//    public void getRandomPoint(Vector3f store) {
//        store.x = this.min.x + (this.len.x * FastMath.nextRandomFloat());
//        store.y = this.min.y + (this.len.y * FastMath.nextRandomFloat());
//        store.z = this.min.z + (this.len.z * FastMath.nextRandomFloat());
//    }
//
//    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
//        getRandomPoint(store);
//    }
//
//    public com.jme3.effect.shapes.EmitterShape deepClone() {
//        try {
//            EmitterBoxShape clone = (EmitterBoxShape) super.clone();
//            clone.min = this.min.clone();
//            clone.len = this.len.clone();
//            return clone;
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError();
//        }
//    }
//
//    public Vector3f getMin() {
//        return this.min;
//    }
//
//    public void setMin(Vector3f min) {
//        this.min = min;
//    }
//
//    public Vector3f getLen() {
//        return this.len;
//    }
//
//    public void setLen(Vector3f len) {
//        this.len = len;
//    }
//
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(this.min, "min", null);
//        oc.write(this.len, "length", null);
//    }
//
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        this.min = (Vector3f) ic.readSavable("min", null);
//        this.len = (Vector3f) ic.readSavable("length", null);
//    }
//
//    @Override
//    public Object jmeClone() {
//        return null;
//    }
//
//    @Override
//    public void cloneFields(final Cloner cloner, final Object o) {
//
//    }
//}
