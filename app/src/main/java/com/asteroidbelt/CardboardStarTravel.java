//package com.asteroidbelt;
//
//import com.jme3.app.SimpleApplication;
//import com.jme3.math.FastMath;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//
//public class CardboardStarTravel extends SimpleApplication {
//    private int count;
//    private float distanceFragment;
//    private long distanceTravelled;
//    private boolean flipView;
//    private Node observer;
//    private float time;
//
//    public static void main(String[] args) {
//        new CardboardStarTravel().start();
//    }
//
//    public void simpleInitApp() {
//        this.observer = new Node("");
//        this.observer.rotate(0.0f, FastMath.PI, 0.0f);
//        this.rootNode.attachChild(this.observer);
//        getRenderManager().removePostView(this.guiViewPort);
//        this.guiNode.detachAllChildren();
//        AsteroidsAppState asteroids = new AsteroidsAppState();
//        this.cam.setFrustumFar(2900.0f);
//        this.stateManager.attach(asteroids);
//        (this.stateManager.getState(CardboardState.class)).setObserver(this.observer);
//    }
//
//    public long getDistanceTravelled() {
//        return this.distanceTravelled;
//    }
//
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
//            System.out.println("FPS: " + (100.0f / this.time));
//            this.time = 0.0f;
//        }
//        this.count++;
//    }
//
//    public Spatial getObserver() {
//        return this.observer;
//    }
//}
