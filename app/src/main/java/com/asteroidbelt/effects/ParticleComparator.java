package com.asteroidbelt.effects;

import com.jme3.effect.*;
import com.jme3.renderer.Camera;
import java.util.Comparator;

@Deprecated
class ParticleComparator implements Comparator<com.jme3.effect.Particle> {
    private Camera cam;

    ParticleComparator() {
    }

    public void setCamera(Camera cam) {
        this.cam = cam;
    }

    public int compare(com.jme3.effect.Particle p1, com.jme3.effect.Particle p2) {
        return 0;
    }
}
