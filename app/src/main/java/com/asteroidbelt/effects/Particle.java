package com.asteroidbelt.effects;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Particle {
    public float angle;
    public final ColorRGBA color = new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f);
    public int imageIndex = 0;
    public float life;
    public final Vector3f position = new Vector3f();
    public float rotateSpeed;
    public float size;
    public float startlife;
    public final Vector3f velocity = new Vector3f();
}
