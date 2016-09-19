package com.asteroidbelt.effects;

import com.jme3.effect.*;
import com.jme3.effect.Particle;
import com.jme3.math.Matrix3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Mesh;

public abstract class ParticleMesh extends Mesh {

    public enum Type {
        Point,
        Triangle
    }

    public abstract void initParticleData(com.jme3.effect.ParticleEmitter particleEmitter, int i);

    public abstract void setImagesXY(int i, int i2);

    public abstract void updateParticleData(Particle[] particleArr, Camera camera, Matrix3f matrix3f);
}
