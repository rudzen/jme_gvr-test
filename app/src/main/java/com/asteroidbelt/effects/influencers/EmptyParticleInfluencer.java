package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;

import java.io.IOException;

public class EmptyParticleInfluencer implements com.jme3.effect.influencers.ParticleInfluencer {
    public void write(JmeExporter ex) throws IOException {
    }

    public void read(JmeImporter im) throws IOException {
    }

    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
    }

    public void setInitialVelocity(Vector3f initialVelocity) {
    }

    public Vector3f getInitialVelocity() {
        return null;
    }

    public void setVelocityVariation(float variation) {
    }

    public float getVelocityVariation() {
        return 0.0f;
    }

    public com.jme3.effect.influencers.ParticleInfluencer clone() {
        try {
            return (com.jme3.effect.influencers.ParticleInfluencer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void influenceRealtime(Particle particle, float tpf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(float tpf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object jmeClone() {
        return null;
    }

    @Override
    public void cloneFields(final Cloner cloner, final Object o) {

    }
}
