package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;

import java.io.IOException;

public class DefaultParticleInfluencer implements com.jme3.effect.influencers.ParticleInfluencer {
    public static final int SAVABLE_VERSION = 1;
    protected Vector3f initialVelocity = new Vector3f();
    protected transient Vector3f temp = new Vector3f();
    protected float velocityVariation = 0.2f;

    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        emitterShape.getRandomPoint(particle.position);
        applyVelocityVariation(particle);
    }

    protected void applyVelocityVariation(Particle particle) {
        particle.velocity.set(this.initialVelocity);
        this.temp.set(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
        this.temp.multLocal(2.0f);
        this.temp.subtractLocal(1.0f, 1.0f, 1.0f);
        this.temp.multLocal(this.initialVelocity.length());
        particle.velocity.interpolateLocal(this.temp, this.velocityVariation);
    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(this.initialVelocity, "initialVelocity", Vector3f.ZERO);
        oc.write(this.velocityVariation, "variation", 0.2f);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        if (ic.getSavableVersion(DefaultParticleInfluencer.class) == 0) {
            this.initialVelocity = (Vector3f) ic.readSavable("startVelocity", Vector3f.ZERO.clone());
        } else {
            this.initialVelocity = (Vector3f) ic.readSavable("initialVelocity", Vector3f.ZERO.clone());
        }
        this.velocityVariation = ic.readFloat("variation", 0.2f);
    }

    public com.jme3.effect.influencers.ParticleInfluencer clone() {
        try {
            DefaultParticleInfluencer clone = (DefaultParticleInfluencer) super.clone();
            clone.initialVelocity = this.initialVelocity.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void setInitialVelocity(Vector3f initialVelocity) {
        this.initialVelocity.set(initialVelocity);
    }

    public Vector3f getInitialVelocity() {
        return this.initialVelocity;
    }

    public void setVelocityVariation(float variation) {
        this.velocityVariation = variation;
    }

    public float getVelocityVariation() {
        return this.velocityVariation;
    }

    public void influenceRealtime(Particle particle, float tpf) {
    }

    public void update(float tpf) {
    }

    @Override
    public Object jmeClone() {
        return null;
    }

    @Override
    public void cloneFields(final Cloner cloner, final Object o) {

    }
}
