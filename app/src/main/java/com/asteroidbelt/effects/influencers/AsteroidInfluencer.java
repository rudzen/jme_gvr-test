package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.DefaultParticleInfluencer;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class AsteroidInfluencer extends DefaultParticleInfluencer {
    private static ColorRGBA brightColor = ColorRGBA.LightGray;
    private static ColorRGBA darkColor = ColorRGBA.Black;
    private Vector3f lightDirection = new Vector3f();

    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        super.influenceParticle(particle, emitterShape);
        Vector3f particleDirection = particle.position.normalize();
        particleDirection.setZ(0.0f).normalizeLocal();
        particle.color.interpolateLocal(brightColor, darkColor, FastMath.pow(((-particleDirection.dot(this.lightDirection)) + 1.0f) * 0.5f, 0.5f));
    }

    public void setLight(DirectionalLight light) {
        this.lightDirection.set(light.getDirection());
        this.lightDirection.setZ(0.0f).normalizeLocal();
        brightColor.set(light.getColor());
        brightColor.multLocal(0.7f);
    }
}
