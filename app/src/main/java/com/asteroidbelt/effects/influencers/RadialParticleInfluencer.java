package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.DefaultParticleInfluencer;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.io.IOException;

public class RadialParticleInfluencer extends DefaultParticleInfluencer {
    private boolean horizontal = false;
    private Vector3f origin = new Vector3f(0.0f, 0.0f, 0.0f);
    private float radialVelocity = 0.0f;

    protected void applyVelocityVariation(Particle particle) {
        particle.velocity.set(this.initialVelocity);
        this.temp.set(particle.position).subtractLocal(this.origin).normalizeLocal().multLocal(this.radialVelocity);
        if (this.horizontal) {
            this.temp.y = 0.0f;
        }
        particle.velocity.addLocal(this.temp);
        this.temp.set(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
        this.temp.multLocal(2.0f);
        this.temp.subtractLocal(1.0f, 1.0f, 1.0f);
        this.temp.multLocal(this.initialVelocity.length());
        particle.velocity.interpolateLocal(this.temp, this.velocityVariation);
    }

    public Vector3f getOrigin() {
        return this.origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public float getRadialVelocity() {
        return this.radialVelocity;
    }

    public void setRadialVelocity(float radialVelocity) {
        this.radialVelocity = radialVelocity;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(this.radialVelocity, "radialVelocity", 0.0f);
        oc.write(this.origin, "origin", new Vector3f());
        oc.write(this.horizontal, "horizontal", false);
    }

    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        this.radialVelocity = ic.readFloat("radialVelocity", 0.0f);
        this.origin = (Vector3f) ic.readSavable("origin", new Vector3f());
        this.horizontal = ic.readBoolean("horizontal", false);
    }
}
