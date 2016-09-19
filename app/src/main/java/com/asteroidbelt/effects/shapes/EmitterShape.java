package com.asteroidbelt.effects.shapes;

import com.jme3.export.Savable;
import com.jme3.math.Vector3f;

public interface EmitterShape extends Savable, Cloneable {
    EmitterShape deepClone();

    void getRandomPoint(Vector3f vector3f);

    void getRandomPointAndNormal(Vector3f vector3f, Vector3f vector3f2);
}
