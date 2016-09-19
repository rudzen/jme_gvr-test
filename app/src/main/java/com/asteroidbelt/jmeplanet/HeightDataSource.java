package com.asteroidbelt.jmeplanet;

import com.jme3.math.Vector3f;

public interface HeightDataSource {
    float getHeightScale();

    float getValue(Vector3f vector3f);

    void setHeightScale(float f);
}
