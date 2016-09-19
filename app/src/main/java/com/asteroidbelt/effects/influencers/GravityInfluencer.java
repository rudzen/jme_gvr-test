package com.asteroidbelt.effects.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.DefaultParticleInfluencer;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class GravityInfluencer extends DefaultParticleInfluencer {
    private static ColorRGBA coolColor = ColorRGBA.Red.clone();
    private static float gravityConstant = 10.1f;
    private static ColorRGBA hotColor = ColorRGBA.White.clone();
    private static ColorRGBA mediumColor = ColorRGBA.Yellow.clone();
    private static ColorRGBA normalColor = ColorRGBA.DarkGray.clone();
    private static ColorRGBA outColor = ColorRGBA.Black.clone();
    private static float velocityMultiplier = 0.25f;
    private float gravityPull;
    private Vector3f normalVelocity = new Vector3f();

    static {
        ColorRGBA colorRGBA = normalColor;
        ColorRGBA colorRGBA2 = hotColor;
        ColorRGBA colorRGBA3 = mediumColor;
        ColorRGBA colorRGBA4 = coolColor;
        outColor.a = 0.5f;
        colorRGBA4.a = 0.5f;
        colorRGBA3.a = 0.5f;
        colorRGBA2.a = 0.5f;
        colorRGBA.a = 0.5f;
        normalColor.a = 0.1f;
    }

    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        emitterShape.getRandomPoint(particle.position);
        particle.velocity.set(particle.position.z, 0.0f, -particle.position.x);
        particle.velocity.normalizeLocal();
        particle.velocity.multLocal(velocityMultiplier);
        particle.color.set(normalColor);
    }

    public void influenceRealtime(Particle particle, float tpf) {
        //super.
        //super.influenceRealtime(particle, tpf);
        float distance = particle.position.length();
        this.gravityPull = (1.0f / FastMath.pow(distance, 2.0f)) * gravityConstant;
        this.normalVelocity.set(particle.position).normalizeLocal();
        particle.velocity.subtractLocal(this.normalVelocity.mult(this.gravityPull * tpf));
        float combine = distance;
        if (combine < 5.0f) {
            particle.color.interpolateLocal(hotColor, mediumColor, combine / 5.0f);
        } else if (combine < 10.0f) {
            particle.color.interpolateLocal(mediumColor, coolColor, (combine - 5.0f) / 5.0f);
        } else if (combine < 20.0f) {
            particle.color.interpolateLocal(coolColor, normalColor, (combine - 10.0f) / 10.0f);
        } else {
            particle.color.interpolateLocal(outColor, normalColor, 1.0f - ((Math.min(combine, 25.0f) - 20.0f) / 5.0f));
        }
        if (particle.life < 5.0f) {
            particle.color.a = FastMath.interpolateLinear((5.0f - particle.life) / 5.0f, 0.5f, 0.0f);
        }
    }
}
