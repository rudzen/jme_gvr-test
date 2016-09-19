//package com.asteroidbelt.effects;
//
//import com.jme3.bounding.BoundingBox;
//import com.jme3.effect.*;
//import com.jme3.effect.ParticleMesh.Type;
//import com.jme3.effect.influencers.DefaultParticleInfluencer;
//import com.jme3.effect.influencers.ParticleInfluencer;
//import com.jme3.effect.shapes.EmitterPointShape;
//import com.jme3.effect.shapes.EmitterShape;
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.FastMath;
//import com.jme3.math.Matrix3f;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.Camera;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.renderer.queue.RenderQueue.Bucket;
//import com.jme3.renderer.queue.RenderQueue.ShadowMode;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.Spatial.BatchHint;
//import com.jme3.scene.control.Control;
//import com.jme3.texture.plugins.TGALoader;
//import com.jme3.util.TempVars;
//import java.io.IOException;
//
//public class ParticleEmitter extends Geometry {
//    private static final ParticleInfluencer DEFAULT_INFLUENCER = new DefaultParticleInfluencer();
//    private static final EmitterShape DEFAULT_SHAPE = new EmitterPointShape(Vector3f.ZERO);
//    private ParticleEmitterControl control;
//    private boolean enabled = true;
//    private ColorRGBA endColor = new ColorRGBA(0.1f, 0.1f, 0.1f, 0.0f);
//    private float endSize = 2.0f;
//    private Vector3f faceNormal = new Vector3f(Vector3f.NAN);
//    private boolean facingVelocity;
//    private boolean fadeAlphaSquare = false;
//    private int firstUnUsed;
//    private Vector3f gravity = new Vector3f(0.0f, 0.1f, 0.0f);
//    private float highLife = 7.0f;
//    private int imagesX = 1;
//    private int imagesY = 1;
//    private int lastUsed;
//    private float lowLife = 3.0f;
//    private Type meshType;
//    private ParticleInfluencer particleInfluencer = DEFAULT_INFLUENCER;
//    private ParticleMesh particleMesh;
//    private com.jme3.effect.Particle[] particles;
//    private float particlesPerSec = 20.0f;
//    private boolean randomAngle;
//    private float rotateSpeed;
//    private boolean selectRandomImage;
//    private EmitterShape shape = DEFAULT_SHAPE;
//    private ColorRGBA startColor = new ColorRGBA(0.4f, 0.4f, 0.4f, 0.5f);
//    private float startSize = 0.2f;
//    private transient Vector3f temp = new Vector3f();
//    private float timeDifference = 0.0f;
//    private boolean updateColor = true;
//    private boolean worldSpace = true;
//
//    static /* synthetic */ class AnonymousClass1 {
//        static final /* synthetic */ int[] $SwitchMap$com$jme3$effect$ParticleMesh$Type = new int[Type.values().length];
//
//        static {
//            try {
//                $SwitchMap$com$jme3$effect$ParticleMesh$Type[Type.Point.ordinal()] = 1;
//            } catch (NoSuchFieldError e) {
//            }
//            try {
//                $SwitchMap$com$jme3$effect$ParticleMesh$Type[Type.Triangle.ordinal()] = 2;
//            } catch (NoSuchFieldError e2) {
//            }
//        }
//    }
//
//    public static class ParticleEmitterControl implements Control {
//        ParticleEmitter parentEmitter;
//
//        public ParticleEmitterControl(ParticleEmitter parentEmitter) {
//            this.parentEmitter = parentEmitter;
//        }
//
//        public Control cloneForSpatial(Spatial spatial) {
//            return this;
//        }
//
//        public void setSpatial(Spatial spatial) {
//        }
//
//        public void setEnabled(boolean enabled) {
//            this.parentEmitter.setEnabled(enabled);
//        }
//
//        public boolean isEnabled() {
//            return this.parentEmitter.isEnabled();
//        }
//
//        public void update(float tpf) {
//            this.parentEmitter.updateFromControl(tpf);
//        }
//
//        public void render(RenderManager rm, ViewPort vp) {
//            this.parentEmitter.renderFromControl(rm, vp);
//        }
//
//        public void write(JmeExporter ex) throws IOException {
//        }
//
//        public void read(JmeImporter im) throws IOException {
//        }
//    }
//
//    public ParticleEmitter clone() {
//        return clone(true);
//    }
//
//    public ParticleEmitter clone(boolean cloneMaterial) {
//        ParticleEmitter clone = (ParticleEmitter) super.clone(cloneMaterial);
//        clone.shape = this.shape.deepClone();
//        clone.setNumParticles(this.particles.length);
//        clone.faceNormal = this.faceNormal.clone();
//        clone.startColor = this.startColor.clone();
//        clone.endColor = this.endColor.clone();
//        clone.particleInfluencer = this.particleInfluencer.clone();
//        clone.controls.remove(this.control);
//        clone.control = new ParticleEmitterControl(clone);
//        clone.controls.add(clone.control);
//        switch (AnonymousClass1.$SwitchMap$com$jme3$effect$ParticleMesh$Type[this.meshType.ordinal()]) {
//            case TGALoader.TYPE_COLORMAPPED /*1*/:
//                //clone.particleMesh = com.jme3.effect.ParticleMesh();
//                clone.setMesh(clone.particleMesh);
//                break;
//            case TGALoader.TYPE_TRUECOLOR /*2*/:
//                //clone.particleMesh = new ParticleTriMesh();
//                clone.setMesh(clone.particleMesh);
//                break;
//            default:
//                throw new IllegalStateException("Unrecognized particle type: " + this.meshType);
//        }
//        clone.particleMesh.initParticleData(clone, clone.particles.length);
//        clone.particleMesh.setImagesXY(clone.imagesX, clone.imagesY);
//        return clone;
//    }
//
//    public ParticleEmitter(String name, Type type, int numParticles) {
//        super(name);
//        setBatchHint(BatchHint.Never);
//        setIgnoreTransform(true);
//        setShadowMode(ShadowMode.Off);
//        setQueueBucket(Bucket.Transparent);
//        this.meshType = type;
//        this.shape = this.shape.deepClone();
//        this.particleInfluencer = this.particleInfluencer.clone();
//        this.control = new ParticleEmitterControl(this);
//        this.controls.add(this.control);
//        switch (AnonymousClass1.$SwitchMap$com$jme3$effect$ParticleMesh$Type[this.meshType.ordinal()]) {
//            case TGALoader.TYPE_COLORMAPPED /*1*/:
//                this.particleMesh = new ParticlePointMesh();
//                setMesh(this.particleMesh);
//                break;
//            case TGALoader.TYPE_TRUECOLOR /*2*/:
//                this.particleMesh = new ParticleTriMesh();
//                setMesh(this.particleMesh);
//                break;
//            default:
//                throw new IllegalStateException("Unrecognized particle type: " + this.meshType);
//        }
//        setNumParticles(numParticles);
//    }
//
//    public ParticleEmitter() {
//        setBatchHint(BatchHint.Never);
//    }
//
//    public void setShape(EmitterShape shape) {
//        this.shape = shape;
//    }
//
//    public EmitterShape getShape() {
//        return this.shape;
//    }
//
//    public void setParticleInfluencer(ParticleInfluencer particleInfluencer) {
//        this.particleInfluencer = particleInfluencer;
//    }
//
//    public ParticleInfluencer getParticleInfluencer() {
//        return this.particleInfluencer;
//    }
//
//    public Type getMeshType() {
//        return this.meshType;
//    }
//
//    public void setMeshType(Type meshType) {
//        this.meshType = meshType;
//        switch (AnonymousClass1.$SwitchMap$com$jme3$effect$ParticleMesh$Type[meshType.ordinal()]) {
//            case TGALoader.TYPE_COLORMAPPED /*1*/:
//                this.particleMesh = new ParticlePointMesh();
//                setMesh(this.particleMesh);
//                break;
//            case TGALoader.TYPE_TRUECOLOR /*2*/:
//                this.particleMesh = new ParticleTriMesh();
//                setMesh(this.particleMesh);
//                break;
//            default:
//                throw new IllegalStateException("Unrecognized particle type: " + meshType);
//        }
//        setNumParticles(this.particles.length);
//    }
//
//    public boolean isInWorldSpace() {
//        return this.worldSpace;
//    }
//
//    public void setInWorldSpace(boolean worldSpace) {
//        setIgnoreTransform(worldSpace);
//        this.worldSpace = worldSpace;
//    }
//
//    public int getNumVisibleParticles() {
//        return this.lastUsed + 1;
//    }
//
//    public final void setNumParticles(int numParticles) {
//        this.particles = new com.jme3.effect.Particle[numParticles];
//        for (int i = 0; i < numParticles; i++) {
//            this.particles[i] = new com.jme3.effect.Particle();
//        }
//        this.particleMesh.initParticleData(this, this.particles.length);
//        this.particleMesh.setImagesXY(this.imagesX, this.imagesY);
//        this.firstUnUsed = 0;
//        this.lastUsed = -1;
//    }
//
//    public int getMaxNumParticles() {
//        return this.particles.length;
//    }
//
//    public com.jme3.effect.Particle[] getParticles() {
//        return this.particles;
//    }
//
//    public Vector3f getFaceNormal() {
//        if (Vector3f.isValidVector(this.faceNormal)) {
//            return this.faceNormal;
//        }
//        return null;
//    }
//
//    public void setFaceNormal(Vector3f faceNormal) {
//        if (faceNormal == null || !Vector3f.isValidVector(faceNormal)) {
//            this.faceNormal.set(Vector3f.NAN);
//        } else {
//            this.faceNormal = faceNormal;
//        }
//    }
//
//    public float getRotateSpeed() {
//        return this.rotateSpeed;
//    }
//
//    public void setRotateSpeed(float rotateSpeed) {
//        this.rotateSpeed = rotateSpeed;
//    }
//
//    public boolean isRandomAngle() {
//        return this.randomAngle;
//    }
//
//    public void setRandomAngle(boolean randomAngle) {
//        this.randomAngle = randomAngle;
//    }
//
//    public boolean isSelectRandomImage() {
//        return this.selectRandomImage;
//    }
//
//    public void setSelectRandomImage(boolean selectRandomImage) {
//        this.selectRandomImage = selectRandomImage;
//    }
//
//    public boolean isFacingVelocity() {
//        return this.facingVelocity;
//    }
//
//    public void setFacingVelocity(boolean followVelocity) {
//        this.facingVelocity = followVelocity;
//    }
//
//    public ColorRGBA getEndColor() {
//        return this.endColor;
//    }
//
//    public void setEndColor(ColorRGBA endColor) {
//        this.endColor.set(endColor);
//    }
//
//    public float getEndSize() {
//        return this.endSize;
//    }
//
//    public void setEndSize(float endSize) {
//        this.endSize = endSize;
//    }
//
//    public Vector3f getGravity() {
//        return this.gravity;
//    }
//
//    public void setGravity(Vector3f gravity) {
//        this.gravity.set(gravity);
//    }
//
//    public void setGravity(float x, float y, float z) {
//        this.gravity.x = x;
//        this.gravity.y = y;
//        this.gravity.z = z;
//    }
//
//    public float getHighLife() {
//        return this.highLife;
//    }
//
//    public void setHighLife(float highLife) {
//        this.highLife = highLife;
//    }
//
//    public int getImagesX() {
//        return this.imagesX;
//    }
//
//    public void setImagesX(int imagesX) {
//        this.imagesX = imagesX;
//        this.particleMesh.setImagesXY(this.imagesX, this.imagesY);
//    }
//
//    public int getImagesY() {
//        return this.imagesY;
//    }
//
//    public void setImagesY(int imagesY) {
//        this.imagesY = imagesY;
//        this.particleMesh.setImagesXY(this.imagesX, this.imagesY);
//    }
//
//    public float getLowLife() {
//        return this.lowLife;
//    }
//
//    public void setLowLife(float lowLife) {
//        this.lowLife = lowLife;
//    }
//
//    public float getParticlesPerSec() {
//        return this.particlesPerSec;
//    }
//
//    public void setParticlesPerSec(float particlesPerSec) {
//        this.particlesPerSec = particlesPerSec;
//        this.timeDifference = 0.0f;
//    }
//
//    public ColorRGBA getStartColor() {
//        return this.startColor;
//    }
//
//    public void setStartColor(ColorRGBA startColor) {
//        this.startColor.set(startColor);
//    }
//
//    public float getStartSize() {
//        return this.startSize;
//    }
//
//    public void setStartSize(float startSize) {
//        this.startSize = startSize;
//    }
//
//    @Deprecated
//    public Vector3f getInitialVelocity() {
//        return this.particleInfluencer.getInitialVelocity();
//    }
//
//    @Deprecated
//    public void setInitialVelocity(Vector3f initialVelocity) {
//        this.particleInfluencer.setInitialVelocity(initialVelocity);
//    }
//
//    @Deprecated
//    public float getVelocityVariation() {
//        return this.particleInfluencer.getVelocityVariation();
//    }
//
//    @Deprecated
//    public void setVelocityVariation(float variation) {
//        this.particleInfluencer.setVelocityVariation(variation);
//    }
//
//    private com.jme3.effect.Particle emitParticle(Vector3f min, Vector3f max) {
//        int idx = this.lastUsed + 1;
//        if (idx >= this.particles.length) {
//            return null;
//        }
//        com.jme3.effect.Particle p = this.particles[idx];
//        if (this.selectRandomImage) {
//            p.imageIndex = (FastMath.nextRandomInt(0, this.imagesY - 1) * this.imagesX) + FastMath.nextRandomInt(0, this.imagesX - 1);
//        }
//        p.startlife = this.lowLife + (FastMath.nextRandomFloat() * (this.highLife - this.lowLife));
//        p.life = p.startlife;
//        p.color.set(this.startColor);
//        p.size = this.startSize;
//        this.particleInfluencer.influenceParticle(p, this.shape);
//        if (this.worldSpace) {
//            this.worldTransform.transformVector(p.position, p.position);
//            this.worldTransform.getRotation().mult(p.velocity, p.velocity);
//        }
//        if (this.randomAngle) {
//            p.angle = FastMath.nextRandomFloat() * FastMath.TWO_PI;
//        }
//        if (this.rotateSpeed != 0.0f) {
//            p.rotateSpeed = this.rotateSpeed * (0.2f + (((FastMath.nextRandomFloat() * 2.0f) - 1.0f) * 0.8f));
//        }
//        this.temp.set(p.position).addLocal(p.size, p.size, p.size);
//        max.maxLocal(this.temp);
//        this.temp.set(p.position).subtractLocal(p.size, p.size, p.size);
//        min.minLocal(this.temp);
//        this.lastUsed++;
//        this.firstUnUsed = idx + 1;
//        return p;
//    }
//
//    public void emitAllParticles() {
//        emitParticles(this.particles.length);
//    }
//
//    public void emitParticles(int num) {
//        getWorldTransform();
//        TempVars vars = TempVars.get();
//        BoundingBox bbox = (BoundingBox) getMesh().getBound();
//        Vector3f min = vars.vect1;
//        Vector3f max = vars.vect2;
//        bbox.getMin(min);
//        bbox.getMax(max);
//        if (!Vector3f.isValidVector(min)) {
//            min.set(Vector3f.POSITIVE_INFINITY);
//        }
//        if (!Vector3f.isValidVector(max)) {
//            max.set(Vector3f.NEGATIVE_INFINITY);
//        }
//        for (int i = 0; i < num && emitParticle(min, max) != null; i++) {
//        }
//        bbox.setMinMax(min, max);
//        setBoundRefresh();
//        vars.release();
//    }
//
//    public void killAllParticles() {
//        for (int i = 0; i < this.particles.length; i++) {
//            if (this.particles[i].life > 0.0f) {
//                freeParticle(i);
//            }
//        }
//    }
//
//    public void killParticle(int index) {
//        freeParticle(index);
//    }
//
//    private void freeParticle(int idx) {
//        com.jme3.effect.Particle p = this.particles[idx];
//        p.life = 0.0f;
//        p.size = 0.0f;
//        p.color.set(0.0f, 0.0f, 0.0f, 0.0f);
//        p.imageIndex = 0;
//        p.angle = 0.0f;
//        p.rotateSpeed = 0.0f;
//        if (idx == this.lastUsed) {
//            while (this.lastUsed >= 0 && this.particles[this.lastUsed].life == 0.0f) {
//                this.lastUsed--;
//            }
//        }
//        if (idx < this.firstUnUsed) {
//            this.firstUnUsed = idx;
//        }
//    }
//
//    private void swap(int idx1, int idx2) {
//        com.jme3.effect.Particle p1 = this.particles[idx1];
//        this.particles[idx1] = this.particles[idx2];
//        this.particles[idx2] = p1;
//    }
//
//    private void updateParticle(com.jme3.effect.Particle p, float tpf, Vector3f min, Vector3f max) {
//        Vector3f vector3f = p.velocity;
//        vector3f.x -= this.gravity.x * tpf;
//        vector3f = p.velocity;
//        vector3f.y -= this.gravity.y * tpf;
//        vector3f = p.velocity;
//        vector3f.z -= this.gravity.z * tpf;
//        this.temp.set(p.velocity).multLocal(tpf);
//        p.position.addLocal(this.temp);
//        float b = (p.startlife - p.life) / p.startlife;
//        if (this.updateColor) {
//            p.color.interpolateLocal(this.startColor, this.endColor, b);
//            if (this.fadeAlphaSquare) {
//                p.color.a = (float) (1.0d - Math.sqrt((double) b));
//            }
//        }
//        p.size = FastMath.interpolateLinear(b, this.startSize, this.endSize);
//        p.angle += p.rotateSpeed * tpf;
//        this.temp.set(p.position).addLocal(p.size, p.size, p.size);
//        max.maxLocal(this.temp);
//        this.temp.set(p.position).subtractLocal(p.size, p.size, p.size);
//        min.minLocal(this.temp);
//        if (!this.selectRandomImage) {
//            p.imageIndex = (int) ((((float) this.imagesX) * b) * ((float) this.imagesY));
//        }
//        this.particleInfluencer.influenceRealtime(p, tpf);
//    }
//
//    private void updateParticleState(float tpf) {
//        getWorldTransform();
//        TempVars vars = TempVars.get();
//        Vector3f min = vars.vect1.set(Vector3f.POSITIVE_INFINITY);
//        Vector3f max = vars.vect2.set(Vector3f.NEGATIVE_INFINITY);
//        for (int i = 0; i < this.particles.length; i++) {
//            com.jme3.effect.Particle p = this.particles[i];
//            if (p.life != 0.0f) {
//                p.life -= tpf;
//                if (p.life <= 0.0f) {
//                    freeParticle(i);
//                } else {
//                    updateParticle(p, tpf, min, max);
//                    if (this.firstUnUsed < i) {
//                        swap(this.firstUnUsed, i);
//                        if (i == this.lastUsed) {
//                            this.lastUsed = this.firstUnUsed;
//                        }
//                        this.firstUnUsed++;
//                    }
//                }
//            }
//        }
//        float interval = 1.0f / this.particlesPerSec;
//        tpf += this.timeDifference;
//        while (tpf > interval) {
//            tpf -= interval;
//            p = emitParticle(min, max);
//            if (p != null) {
//                p.life -= tpf;
//                if (p.life <= 0.0f) {
//                    freeParticle(this.lastUsed);
//                } else {
//                    updateParticle(p, tpf, min, max);
//                }
//            }
//        }
//        this.timeDifference = tpf;
//        ((BoundingBox) getMesh().getBound()).setMinMax(min, max);
//        setBoundRefresh();
//        vars.release();
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public boolean isEnabled() {
//        return this.enabled;
//    }
//
//    public void updateFromControl(float tpf) {
//        if (this.enabled) {
//            updateParticleState(tpf);
//        }
//    }
//
//    private void renderFromControl(RenderManager rm, ViewPort vp) {
//        Camera cam = vp.getCamera();
//        if (this.meshType == Type.Point) {
//            getMaterial().setFloat("Quadratic", cam.getProjectionMatrix().m00 * (((float) cam.getWidth()) * 0.5f));
//        }
//        Matrix3f inverseRotation = Matrix3f.IDENTITY;
//        TempVars vars = null;
//        if (!this.worldSpace) {
//            vars = TempVars.get();
//            inverseRotation = getWorldRotation().toRotationMatrix(vars.tempMat3).invertLocal();
//        }
//        this.particleMesh.updateParticleData(this.particles, cam, inverseRotation);
//        if (!this.worldSpace) {
//            vars.release();
//        }
//    }
//
//    public void preload(RenderManager rm, ViewPort vp) {
//        updateParticleState(0.0f);
//        this.particleMesh.updateParticleData(this.particles, vp.getCamera(), Matrix3f.IDENTITY);
//    }
//
//    public void write(JmeExporter ex) throws IOException {
//        super.write(ex);
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(this.shape, "shape", DEFAULT_SHAPE);
//        oc.write(this.meshType, "meshType", Type.Triangle);
//        oc.write(this.enabled, "enabled", true);
//        oc.write(this.particles.length, "numParticles", 0);
//        oc.write(this.particlesPerSec, "particlesPerSec", 0.0f);
//        oc.write(this.lowLife, "lowLife", 0.0f);
//        oc.write(this.highLife, "highLife", 0.0f);
//        oc.write(this.gravity, "gravity", null);
//        oc.write(this.imagesX, "imagesX", 1);
//        oc.write(this.imagesY, "imagesY", 1);
//        oc.write(this.startColor, "startColor", null);
//        oc.write(this.endColor, "endColor", null);
//        oc.write(this.startSize, "startSize", 0.0f);
//        oc.write(this.endSize, "endSize", 0.0f);
//        oc.write(this.worldSpace, "worldSpace", false);
//        oc.write(this.facingVelocity, "facingVelocity", false);
//        oc.write(this.faceNormal, "faceNormal", new Vector3f(Vector3f.NAN));
//        oc.write(this.selectRandomImage, "selectRandomImage", false);
//        oc.write(this.randomAngle, "randomAngle", false);
//        oc.write(this.rotateSpeed, "rotateSpeed", 0.0f);
//        oc.write(this.particleInfluencer, "influencer", DEFAULT_INFLUENCER);
//    }
//
//    public void read(JmeImporter im) throws IOException {
//        super.read(im);
//        InputCapsule ic = im.getCapsule(this);
//        this.shape = (EmitterShape) ic.readSavable("shape", DEFAULT_SHAPE);
//        if (this.shape == DEFAULT_SHAPE) {
//            this.shape = this.shape.deepClone();
//        }
//        this.meshType = (Type) ic.readEnum("meshType", Type.class, Type.Triangle);
//        int numParticles = ic.readInt("numParticles", 0);
//        this.enabled = ic.readBoolean("enabled", true);
//        this.particlesPerSec = ic.readFloat("particlesPerSec", 0.0f);
//        this.lowLife = ic.readFloat("lowLife", 0.0f);
//        this.highLife = ic.readFloat("highLife", 0.0f);
//        this.gravity = (Vector3f) ic.readSavable("gravity", null);
//        this.imagesX = ic.readInt("imagesX", 1);
//        this.imagesY = ic.readInt("imagesY", 1);
//        this.startColor = (ColorRGBA) ic.readSavable("startColor", null);
//        this.endColor = (ColorRGBA) ic.readSavable("endColor", null);
//        this.startSize = ic.readFloat("startSize", 0.0f);
//        this.endSize = ic.readFloat("endSize", 0.0f);
//        this.worldSpace = ic.readBoolean("worldSpace", false);
//        setIgnoreTransform(this.worldSpace);
//        this.facingVelocity = ic.readBoolean("facingVelocity", false);
//        this.faceNormal = (Vector3f) ic.readSavable("faceNormal", new Vector3f(Vector3f.NAN));
//        this.selectRandomImage = ic.readBoolean("selectRandomImage", false);
//        this.randomAngle = ic.readBoolean("randomAngle", false);
//        this.rotateSpeed = ic.readFloat("rotateSpeed", 0.0f);
//        switch (AnonymousClass1.$SwitchMap$com$jme3$effect$ParticleMesh$Type[this.meshType.ordinal()]) {
//            case TGALoader.TYPE_COLORMAPPED /*1*/:
//                this.particleMesh = new ParticlePointMesh();
//                setMesh(this.particleMesh);
//                break;
//            case TGALoader.TYPE_TRUECOLOR /*2*/:
//                this.particleMesh = new ParticleTriMesh();
//                setMesh(this.particleMesh);
//                break;
//            default:
//                throw new IllegalStateException("Unrecognized particle type: " + this.meshType);
//        }
//        setNumParticles(numParticles);
//        this.particleInfluencer = (ParticleInfluencer) ic.readSavable("influencer", DEFAULT_INFLUENCER);
//        if (this.particleInfluencer == DEFAULT_INFLUENCER) {
//            this.particleInfluencer = this.particleInfluencer.clone();
//        }
//        if (im.getFormatVersion() == 0) {
//            for (int i = 0; i < this.controls.size(); i++) {
//                if (this.controls.get(i) instanceof ParticleEmitter) {
//                    this.controls.remove(i);
//                    this.controls.add(new ParticleEmitterControl(this));
//                    if (this.gravity == null) {
//                        this.gravity = new Vector3f();
//                        this.gravity.y = ic.readFloat("gravity", 0.0f);
//                        return;
//                    }
//                    return;
//                }
//            }
//            if (this.gravity == null) {
//                this.gravity = new Vector3f();
//                this.gravity.y = ic.readFloat("gravity", 0.0f);
//                return;
//            }
//            return;
//        }
//        this.control = (ParticleEmitterControl) getControl(ParticleEmitterControl.class);
//        this.control.parentEmitter = this;
//    }
//
//    public void setUpdateColor(boolean updateColor) {
//        this.updateColor = updateColor;
//    }
//
//    public boolean isFadeAlphaSquare() {
//        return this.fadeAlphaSquare;
//    }
//
//    public void setFadeAlphaSquare(boolean fadeAlphaSquare) {
//        this.fadeAlphaSquare = fadeAlphaSquare;
//    }
//}
