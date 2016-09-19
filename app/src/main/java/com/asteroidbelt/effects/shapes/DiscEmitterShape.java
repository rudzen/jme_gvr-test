package com.asteroidbelt.effects.shapes;

import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.plugins.TGALoader;
import java.util.ArrayList;
import java.util.List;

public class DiscEmitterShape extends EmitterSphereShape {
    private DistributionType distributionType = DistributionType.Ring;
    private List<Vector2f> forbiddenPoints = new ArrayList();
    private float forbiddenRadius = 0.2f;
    private float length;
    private boolean standing;
    private Vector2f tempVec = new Vector2f();
    private float tempZ;
    private float thickness;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$jme3$effect$shapes$DiscEmitterShape$DistributionType = new int[DistributionType.values().length];

        static {
            try {
                $SwitchMap$com$jme3$effect$shapes$DiscEmitterShape$DistributionType[DistributionType.Ring.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$jme3$effect$shapes$DiscEmitterShape$DistributionType[DistributionType.Cylinder.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$jme3$effect$shapes$DiscEmitterShape$DistributionType[DistributionType.Disc.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$jme3$effect$shapes$DiscEmitterShape$DistributionType[DistributionType.Disc2.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum DistributionType {
        Ring,
        Cylinder,
        Disc,
        Disc2
    }

    public DiscEmitterShape(Vector3f center, float radius) {
        super(center, radius);
    }

    public void getRandomPoint(Vector3f store) {
        boolean allowEmit = true;
        for (int tries = 0; tries < 50; tries++) {
            float radius = getRadius();
            float rand = FastMath.nextRandomFloat();
            allowEmit = true;
            switch (AnonymousClass1.$SwitchMap$com$jme3$effect$shapes$DiscEmitterShape$DistributionType[this.distributionType.ordinal()]) {
                case TGALoader.TYPE_COLORMAPPED /*1*/:
                    store.x = FastMath.sin(rand * FastMath.TWO_PI) * radius;
                    store.y = ((FastMath.nextRandomFloat() * radius) * 0.2f) - (radius * 0.1f);
                    store.z = FastMath.cos(rand * FastMath.TWO_PI) * radius;
                    break;
                case TGALoader.TYPE_TRUECOLOR /*2*/:
                    store.x = ((FastMath.sin(rand * FastMath.TWO_PI) * radius) + (FastMath.nextRandomFloat() * this.thickness)) - (this.thickness * 0.5f);
                    store.y = ((FastMath.cos(rand * FastMath.TWO_PI) * radius) + (FastMath.nextRandomFloat() * this.thickness)) - (this.thickness * 0.5f);
                    store.z = FastMath.nextRandomFloat() * this.length;
                    break;
                case TGALoader.TYPE_BLACKANDWHITE /*3*/:
                    radius *= FastMath.rand.nextFloat();
                    store.x = FastMath.sin(rand * FastMath.TWO_PI) * radius;
                    store.y = ((FastMath.nextRandomFloat() * radius) * 0.2f) - (radius * 0.1f);
                    store.z = FastMath.cos(rand * FastMath.TWO_PI) * radius;
                    break;
                case 4: //ImageCodec.FLAG_GRAY /*4*/:
                    radius *= FastMath.sqr(FastMath.rand.nextFloat());
                    store.x = FastMath.sin(rand * FastMath.TWO_PI) * radius;
                    store.y = ((FastMath.nextRandomFloat() * (1.0f - radius)) * 0.5f) - ((1.0f - radius) * 0.25f);
                    store.z = FastMath.cos(rand * FastMath.TWO_PI) * radius;
                    break;
            }
            if (!this.forbiddenPoints.isEmpty()) {
                for (Vector2f point : this.forbiddenPoints) {
                    this.tempVec.set(store.x, store.z);
                    if (point.distance(this.tempVec) < this.forbiddenRadius) {
                        allowEmit = false;
                    }
                }
            }
            if (allowEmit) {
                if (this.standing) {
                    this.tempZ = store.z;
                    store.z = store.y;
                    store.y = this.tempZ;
                }
                if (!allowEmit) {
                    store.z = Float.POSITIVE_INFINITY;
                    store.y = Float.POSITIVE_INFINITY;
                    store.x = Float.POSITIVE_INFINITY;
                }
            }
        }
        if (!allowEmit) {
            store.z = Float.POSITIVE_INFINITY;
            store.y = Float.POSITIVE_INFINITY;
            store.x = Float.POSITIVE_INFINITY;
        }
    }

    public void setDistributionType(DistributionType type) {
        this.distributionType = type;
    }

    public void addForbiddenPoint(Vector3f point) {
        this.forbiddenPoints.add(new Vector2f(point.x, point.z));
    }

    public void clearForbiddenPoints() {
        this.forbiddenPoints.clear();
    }

    public float getForbiddenRadius() {
        return this.forbiddenRadius;
    }

    public void setForbiddenRadius(float forbiddenRadius) {
        this.forbiddenRadius = forbiddenRadius;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
