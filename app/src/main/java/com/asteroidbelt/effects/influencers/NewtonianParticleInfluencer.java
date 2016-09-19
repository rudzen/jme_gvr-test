package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.*;
import com.jme3.effect.influencers.ParticleInfluencer;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import java.io.IOException;

public class NewtonianParticleInfluencer extends com.jme3.effect.influencers.DefaultParticleInfluencer {
    protected float normalVelocity;
    protected float surfaceTangentFactor;
    protected float surfaceTangentRotation;

    public NewtonianParticleInfluencer() {
        this.velocityVariation = 0.0f;
    }

    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        emitterShape.getRandomPointAndNormal(particle.position, particle.velocity);
        if (this.surfaceTangentFactor == 0.0f) {
            particle.velocity.multLocal(this.normalVelocity);
        } else {
            this.temp.set(particle.velocity.z * this.surfaceTangentFactor, particle.velocity.y * this.surfaceTangentFactor, (-particle.velocity.x) * this.surfaceTangentFactor);
            if (this.surfaceTangentRotation != 0.0f) {
                Matrix3f m = new Matrix3f();
                m.fromAngleNormalAxis(FastMath.PI * this.surfaceTangentRotation, particle.velocity);
                this.temp = m.multLocal(this.temp);
            }
            particle.velocity.multLocal(this.normalVelocity);
            particle.velocity.addLocal(this.temp);
        }
        if (this.velocityVariation != 0.0f) {
            applyVelocityVariation(particle);
        }
    }

    public float getNormalVelocity() {
        return this.normalVelocity;
    }

    public void setNormalVelocity(float normalVelocity) {
        this.normalVelocity = normalVelocity;
    }

    public void setSurfaceTangentFactor(float surfaceTangentFactor) {
        this.surfaceTangentFactor = surfaceTangentFactor;
    }

    public float getSurfaceTangentFactor() {
        return this.surfaceTangentFactor;
    }

    public void setSurfaceTangentRotation(float surfaceTangentRotation) {
        this.surfaceTangentRotation = surfaceTangentRotation;
    }

    public float getSurfaceTangentRotation() {
        return this.surfaceTangentRotation;
    }

    protected void applyVelocityVariation(Particle particle) {
        this.temp.set(FastMath.nextRandomFloat() * this.velocityVariation, FastMath.nextRandomFloat() * this.velocityVariation, FastMath.nextRandomFloat() * this.velocityVariation);
        particle.velocity.addLocal(this.temp);
    }

    public ParticleInfluencer clone() {
        NewtonianParticleInfluencer result = new NewtonianParticleInfluencer();
        result.normalVelocity = this.normalVelocity;
        result.initialVelocity = this.initialVelocity;
        result.velocityVariation = this.velocityVariation;
        result.surfaceTangentFactor = this.surfaceTangentFactor;
        result.surfaceTangentRotation = this.surfaceTangentRotation;
        return result;
    }

    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(this.normalVelocity, "normalVelocity", 0.0f);
        oc.write(this.surfaceTangentFactor, "surfaceTangentFactor", 0.0f);
        oc.write(this.surfaceTangentRotation, "surfaceTangentRotation", 0.0f);
    }

    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        this.normalVelocity = ic.readFloat("normalVelocity", 0.0f);
        this.surfaceTangentFactor = ic.readFloat("surfaceTangentFactor", 0.0f);
        this.surfaceTangentRotation = ic.readFloat("surfaceTangentRotation", 0.0f);
    }
}
