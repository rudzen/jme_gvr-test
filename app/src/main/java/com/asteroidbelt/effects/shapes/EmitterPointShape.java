//package com.asteroidbelt.effects.shapes;
//
//import com.jme3.effect.shapes.*;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.math.Vector3f;
//import java.io.IOException;
//
//public class EmitterPointShape implements com.jme3.effect.shapes.EmitterShape {
//    private Vector3f point;
//
//    public EmitterPointShape(Vector3f point) {
//        this.point = point;
//    }
//
//    public com.jme3.effect.shapes.EmitterShape deepClone() {
//        try {
//            EmitterPointShape clone = (EmitterPointShape) super.clone();
//            clone.point = this.point.clone();
//            return clone;
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError();
//        }
//    }
//
//    public void getRandomPoint(Vector3f store) {
//        store.set(this.point);
//    }
//
//    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
//        store.set(this.point);
//    }
//
//    public Vector3f getPoint() {
//        return this.point;
//    }
//
//    public void setPoint(Vector3f point) {
//        this.point = point;
//    }
//
//    public void write(JmeExporter ex) throws IOException {
//        ex.getCapsule(this).write(this.point, "point", null);
//    }
//
//    public void read(JmeImporter im) throws IOException {
//        this.point = (Vector3f) im.getCapsule(this).readSavable("point", null);
//    }
//}
