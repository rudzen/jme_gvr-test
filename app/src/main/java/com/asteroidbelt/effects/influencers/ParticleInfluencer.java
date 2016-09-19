package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;

public interface ParticleInfluencer extends Savable, Cloneable {
    ParticleInfluencer clone();

    Vector3f getInitialVelocity();

    float getVelocityVariation();

    void influenceParticle(Particle particle, EmitterShape emitterShape);

    void influenceRealtime(Particle particle, float f);

    void setInitialVelocity(Vector3f vector3f);

    void setVelocityVariation(float f);

    void update(float f);
}
