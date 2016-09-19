package com.asteroidbelt.effects.shapes;

import com.jme3.effect.shapes.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.List;

public class EmitterMeshConvexHullShape extends com.jme3.effect.shapes.EmitterMeshFaceShape {
    public EmitterMeshConvexHullShape(List<Mesh> meshes) {
        super(meshes);
    }

    public void getRandomPoint(Vector3f store) {
        super.getRandomPoint(store);
        store.multLocal(FastMath.nextRandomFloat());
    }

    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        super.getRandomPointAndNormal(store, normal);
        store.multLocal(FastMath.nextRandomFloat());
    }
}
