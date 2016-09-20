//package com.example.rudz.jme_test.asteroid;
//
//import android.util.Log;
//
//import com.example.rudz.jme_test.asteroid.jmeplanet.FractalDataSource;
//import com.example.rudz.jme_test.asteroid.jmeplanet.Planet;
//import com.example.rudz.jme_test.asteroid.jmeplanet.Utility;
//import com.jme3.app.SimpleApplication;
//import com.jme3.effect.ParticleEmitter;
//import com.jme3.light.DirectionalLight;
//import com.jme3.material.Material;
//import com.jme3.material.RenderState;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.queue.RenderQueue;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.shape.Quad;
//import com.jme3.terrain.noise.fractal.FractalSum;
//import com.jme3.util.SkyFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import jme3tools.optimize.GeometryBatchFactory;
//
///**
// * <p>
// * Project : jme_gvr-test<br>
// * Package : com.example.rudz.jme_test.asteroid<br>
// * Created by : rudz<br>
// * On : sep.20.2016 - 16:13
// * </p>
// */
//public class Asteroids extends SimpleApplication {
//
//    private List<Geometry> asteroidModels;
//    private FractalSum fractalSum;
//    private DirectionalLight light;
//    private Material mat;
//    private float maxDistance = 3000.0f;
//    private Planet moon;
//    private float moonMaxDistance = 300.0f;
//    private float orbitalLocation = 0.0f;
//    private ParticleEmitter particleEmitter;
//    private ParticleEmitter particleEmitter2;
//    private ParticleEmitter particleEmitter3;
//    private float[] rotation = new float[100];
//    private Node stars;
//    private Vector3f velocityDirection = new Vector3f(0.0f, 0.0f, 0.5f);
//    private Vector3f velocityDirectionSlow = new Vector3f(0.0f, 0.0f, 0.5f);
//    private Vector3f viewDirection = new Vector3f();
//
//    // other vars
//    private int count;
//    private float distanceFragment;
//    private long distanceTravelled;
//    private boolean flipView;
//    private Node observer;
//    private float time;
//
//
//    @Override
//    public void simpleInitApp() {
//        // part 1
//        this.asteroidModels = new ArrayList<>();
//        this.asteroidModels.add((Geometry) assetManager.loadModel("Models/asteroid1.j3o"));
//        this.asteroidModels.add((Geometry) assetManager.loadModel("Models/asteroid2.j3o"));
//        this.asteroidModels.add((Geometry) assetManager.loadModel("Models/asteroid3.j3o"));
//        this.asteroidModels.add((Geometry) assetManager.loadModel("Models/asteroid4.j3o"));
//        this.stars = new Node();
//        rootNode.attachChild(this.stars);
//        initPeripheralAsteroids();
//        this.light = new DirectionalLight();
//        this.light.setColor(new ColorRGBA(1.0f, 0.9411765f, 0.78431374f, 1.0f));
//        this.light.setDirection(new Vector3f(0.9f, 0.0f, -0.1f));
//        rootNode.addLight(this.light);
//        createMoon(0);
//        createSkybox();
//        setupEmitters();
//        createLensFlare();
//
//        // part 2
//        this.observer = new Node("");
//        this.observer.rotate(0.0f, FastMath.PI, 0.0f);
//        this.rootNode.attachChild(this.observer);
//        renderManager.removePostView(this.guiViewPort);
//        this.guiNode.detachAllChildren();
//        this.cam.setFrustumFar(2900.0f);
//    }
//
//    @Override
//    public void simpleUpdate(float tpf) {
//        super.simpleUpdate(tpf);
//        this.distanceFragment += tpf;
//        if (this.distanceFragment > 1.0f) {
//            this.distanceTravelled++;
//            this.distanceFragment -= 1.0f;
//        }
//        this.time += tpf;
//        if (this.count == 100) {
//            this.count = 0;
//            Log.d("FPS", "FPS: " + (100.0f / this.time));
//            this.time = 0.0f;
//        }
//        this.count++;
//    }
//
//    private void setupEmitters() {
//        Node particleNode = (Node) assetManager.loadModel("Models/DustEmitter.j3o");
//        this.particleEmitter = (ParticleEmitter) particleNode.getChild(0);
//        this.particleEmitter2 = (ParticleEmitter) particleNode.getChild(1);
//        this.particleEmitter.setLocalTranslation(0.0f, 0.0f, -30.0f);
//        this.particleEmitter2.setLocalTranslation(0.0f, 0.0f, -5.0f);
//        this.particleEmitter.getParticleInfluencer().setInitialVelocity(this.velocityDirection.mult(50.0f));
//        this.particleEmitter.getParticleInfluencer().setVelocityVariation(0.0f);
//        this.particleEmitter2.getParticleInfluencer().setInitialVelocity(this.velocityDirection.mult(10.0f));
//        this.particleEmitter2.getParticleInfluencer().setVelocityVariation(0.0f);
//        this.particleEmitter2.getParticleInfluencer().setInitialVelocity(this.velocityDirection);
//        this.particleEmitter2.getParticleInfluencer().setVelocityVariation(0.0f);
//        this.particleEmitter3 = (ParticleEmitter) particleNode.getChild(2);
//        AsteroidInfluencer asteroidInfluencer = new AsteroidInfluencer();
//        asteroidInfluencer.setLight(this.light);
//        asteroidInfluencer.setVelocityVariation(0.0f);
//        asteroidInfluencer.setInitialVelocity(this.velocityDirection.mult(8.0f));
//        this.particleEmitter3.setParticleInfluencer(asteroidInfluencer);
//        this.particleEmitter3.setLocalTranslation(0.0f, 0.0f, -1000.0f);
//        this.particleEmitter3.setQueueBucket(RenderQueue.Bucket.Sky);
//        this.particleEmitter3.setRandomAngle(true);
//        this.particleEmitter3.setNumParticles(1500);
//        //this.particleEmitter3.setUpdateColor(false);
//        DiscEmitterShape e = new DiscEmitterShape(Vector3f.ZERO, 500.0f);
//        e.setThickness(300.0f);
//        e.setLength(2000.0f);
//        e.setDistributionType(DiscEmitterShape.DistributionType.Cylinder);
//        this.particleEmitter3.setShape(e);
//        this.particleEmitter3.emitAllParticles();
//        e.setLength(100.0f);
//        this.particleEmitter3.setShape(e);
//        this.particleEmitter3.setParticlesPerSec(50.0f);
//        rootNode.attachChild(this.particleEmitter);
//        rootNode.attachChild(this.particleEmitter2);
//        rootNode.attachChild(this.particleEmitter3);
//    }
//
//    public void update(float tpf) {
//        for (Spatial s : this.stars.getChildren()) {
//            s.move(this.velocityDirectionSlow);
//            if (s.getLocalTranslation().z > this.maxDistance) {
//                s.move(0.0f, 0.0f, (-s.getLocalTranslation().z) * 1.99f);
//            }
//        }
//        updateMoon(tpf);
//        this.orbitalLocation = (this.orbitalLocation + tpf) % FastMath.TWO_PI;
//    }
//
//    private void updateMoon(float tpf) {
//        float distFactor = 1.0f - (this.moon.getLocalTranslation().length() / this.moonMaxDistance);
//        this.moon.move(this.velocityDirection.mult(distFactor));
//        this.moon.setLocalScale(distFactor);
//        if (distFactor < 0.01f) {
//            this.moon.setLocalTranslation(this.moon.getLocalTranslation().negate().multLocal(0.98f));
//        } else if (distFactor > 0.2f) {
//            this.moon.rotate(0.05f * tpf, 0.1f * tpf, 0.0f);
//            //this.moon.rotate(DistortionRenderer.VIGNETTE_SIZE_TAN_ANGLE * tpf, 0.1f * tpf, 0.0f);
//            this.moon.setCameraPosition(getCamera().getLocation());
//        }
//    }
//
//    private void initPeripheralAsteroids() {
//        this.mat = assetManager.loadMaterial("Materials/Generated/asteroid1-Icosphere1.j3m");
//        this.fractalSum = new FractalSum();
//        this.fractalSum.setOctaves(1.0f);
//        this.fractalSum.setFrequency(0.65f);
//        this.fractalSum.setRoughness(0.01f);
//        for (int i = -5; i < 5; i++) {
//            generateSlice(i, (int) (this.maxDistance / 5.0f));
//        }
//    }
//
//    private void generateSlice(int zSlice, int sliceSize) {
//        Node cluster = new Node();
//        Vector3f clusterLocation = new Vector3f(0.0f, 0.0f, (float) (zSlice * sliceSize));
//        int x = -30;
//        while (x < 30) {
//            int y = -15;
//            while (y < 15) {
//                if (Math.abs(x) + Math.abs(y) < -5 || Math.abs(y) + Math.abs(x) > 5) {
//                    for (int z = -5; z < 5; z++) {
//                        if (this.fractalSum.value((float) x, (float) y, (float) ((zSlice * sliceSize) + z)) > 0.75f) {
//                            cluster.attachChild(createAsteroid(x, y, z, sliceSize / 5));
//                        }
//                    }
//                }
//                y++;
//            }
//            x++;
//        }
//        Spatial result = GeometryBatchFactory.optimize(cluster);
//        result.setLocalTranslation(clusterLocation);
//        this.stars.attachChild(result);
//    }
//
//    private Geometry createAsteroid(int x, int y, int z, int zMultiplier) {
//        Geometry star = this.asteroidModels.get(FastMath.nextRandomInt(0, 3)).clone();
//        star.setLocalScale(5.0f + (FastMath.nextRandomFloat() * 15.0f));
//        star.setMaterial(this.mat);
//        star.setLocalTranslation((float) (x * 50), (float) (y * 50), (float) (z * zMultiplier));
//        star.rotate(FastMath.nextRandomFloat() * FastMath.TWO_PI, FastMath.nextRandomFloat() * FastMath.TWO_PI, FastMath.nextRandomFloat() * FastMath.TWO_PI);
//        return star;
//    }
//
//    private Spatial createMoon(long seed) {
//        FractalDataSource moonDataSource = new FractalDataSource((int) seed);
//        moonDataSource.setHeightScale(5.0f);
//        moonDataSource.setPersistence(0.8f);
//        this.moon = Utility.createMoonLikePlanet(assetManager, 25.0f, moonDataSource);
//        rootNode.attachChild(this.moon);
//        this.moon.setLocalTranslation(40.0f, 15.0f, (-this.moonMaxDistance) * 0.65f);
//        return this.moon;
//    }
//
//    private void createSkybox() {
//        rootNode.attachChild(SkyFactory.createSky(assetManager,
//                assetManager.loadTexture("Textures/skybox/skybox_left2.png"),
//                assetManager.loadTexture("Textures/skybox/skybox_right1.png"),
//                assetManager.loadTexture("Textures/skybox/skybox_front5.png"),
//                assetManager.loadTexture("Textures/skybox/skybox_back6.png"),
//                assetManager.loadTexture("Textures/skybox/skybox_top3.png"),
//                assetManager.loadTexture("Textures/skybox/skybox_bottom4.png"),
//                new Vector3f(10.0f, 10.0f, 10.0f)));
//    }
//
//    private void createLensFlare() {
//        Geometry flare = new Geometry("Flare", new Quad(150.0f, 75.0f));
//        flare.move(-140.0f, -32.0f, 75.0f);
//        flare.setQueueBucket(RenderQueue.Bucket.Transparent);
//        flare.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/lens_flare.png"));
//        mat.getAdditionalRenderState().setDepthTest(true);
//        mat.getAdditionalRenderState().setDepthWrite(true);
//        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
//        flare.setMaterial(mat);
//        rootNode.attachChild(flare);
//    }
//
//
//    public long getDistanceTravelled() {
//        return this.distanceTravelled;
//    }
//
//
//
//}
