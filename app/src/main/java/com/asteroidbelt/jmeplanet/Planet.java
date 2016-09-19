package com.asteroidbelt.jmeplanet;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;

public class Planet extends Node {
    protected ColorRGBA atmosphereFogColor;
    protected float atmosphereFogDensity;
    protected float atmosphereFogDistance;
    protected Material atmosphereMaterial;
    protected Node atmosphereNode;
    protected float atmosphereRadius;
    protected Quad[] atmosphereSide;
    protected float baseRadius;
    protected boolean currentlyInAtmosphere;
    protected boolean currentlyInOcean;
    protected HeightDataSource dataSource;
    protected float distanceToCamera;
    protected int maxDepth;
    protected int minDepth;
    protected boolean oceanFloorCulling;
    protected Material oceanMaterial;
    protected Node oceanNode;
    protected Quad[] oceanSide;
    protected Node planetNode;
    protected Vector3f planetToCamera;
    protected boolean previouslyInAtmosphere;
    protected boolean previouslyInOcean;
    protected int quads;
    protected Material terrainMaterial;
    protected Node terrainNode;
    protected Quad[] terrainSide;
    private float texMaxPow;
    protected ColorRGBA underwaterFogColor;
    protected float underwaterFogDensity;
    protected float underwaterFogDistance;
    protected boolean wireframeMode;

    public Planet(String name, float baseRadius, Material material, HeightDataSource dataSource, int quads, int minDepth, int maxDepth) {
        super(name);
        this.quads = 4;
        this.minDepth = 1;
        this.maxDepth = 3;
        this.terrainSide = new Quad[6];
        this.oceanSide = new Quad[6];
        this.atmosphereSide = new Quad[6];
        this.atmosphereFogColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f);
        this.atmosphereFogDistance = 100.0f;
        this.atmosphereFogDensity = 1.0f;
        this.underwaterFogColor = new ColorRGBA(0.2f, 0.3f, 0.9f, 1.0f);
        this.underwaterFogDistance = 50.0f;
        this.underwaterFogDensity = 3.0f;
        this.texMaxPow = 12.0f;
        this.terrainMaterial = material;
        this.baseRadius = baseRadius;
        this.dataSource = dataSource;
        this.quads = quads;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
        this.planetNode = new Node("PlanetNode");
        attachChild(this.planetNode);
        prepareTerrain();
    }

    public Planet(String name, float baseRadius, Material material, HeightDataSource dataSource) {
        super(name);
        this.quads = 4;
        this.minDepth = 1;
        this.maxDepth = 3;
        this.terrainSide = new Quad[6];
        this.oceanSide = new Quad[6];
        this.atmosphereSide = new Quad[6];
        this.atmosphereFogColor = new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f);
        this.atmosphereFogDistance = 100.0f;
        this.atmosphereFogDensity = 1.0f;
        this.underwaterFogColor = new ColorRGBA(0.2f, 0.3f, 0.9f, 1.0f);
        this.underwaterFogDistance = 50.0f;
        this.underwaterFogDensity = 3.0f;
        this.texMaxPow = 12.0f;
        this.terrainMaterial = material;
        this.baseRadius = baseRadius;
        this.dataSource = dataSource;
        this.planetNode = new Node("PlanetNode");
        attachChild(this.planetNode);
        prepareTerrain();
    }

    public void createOcean(Material material) {
        this.oceanMaterial = material;
        if (this.oceanNode == null) {
            prepareOcean();
        }
    }

    public void createAtmosphere(Material material, float atmosphereRadius) {
        this.atmosphereMaterial = material;
        this.atmosphereRadius = atmosphereRadius;
        if (this.atmosphereNode == null) {
            prepareAtmosphere();
        }
    }

    public void setCameraPosition(Vector3f position) {
        int i;
        boolean skirting;
        this.planetToCamera = position.subtract(getLocalTranslation());
        this.distanceToCamera = this.planetToCamera.length() - this.baseRadius;
        if (this.atmosphereNode != null) {
            if (this.distanceToCamera < this.atmosphereRadius - this.baseRadius) {
                this.previouslyInAtmosphere = this.currentlyInAtmosphere;
                this.currentlyInAtmosphere = true;
            } else {
                this.previouslyInAtmosphere = this.currentlyInAtmosphere;
                this.currentlyInAtmosphere = false;
            }
        }
        if (this.oceanNode != null) {
            if (this.distanceToCamera <= 1.0f) {
                this.previouslyInOcean = this.currentlyInOcean;
                this.currentlyInOcean = true;
            } else {
                this.previouslyInOcean = this.currentlyInOcean;
                this.currentlyInOcean = false;
            }
        }
        int currentTerrainMaxDepth = 0;
        for (i = 0; i < 6; i++) {
            if (this.terrainSide[i] != null) {
                this.terrainSide[i].setCameraPosition(position);
                currentTerrainMaxDepth = Math.max(currentTerrainMaxDepth, this.terrainSide[i].getCurrentMaxDepth());
            }
            if (this.oceanSide[i] != null) {
                this.oceanSide[i].setCameraPosition(position);
            }
            if (this.atmosphereSide[i] != null) {
                this.atmosphereSide[i].setCameraPosition(position);
            }
        }
        if (currentTerrainMaxDepth == this.minDepth) {
            skirting = false;
        } else {
            skirting = true;
        }
        for (i = 0; i < 6; i++) {
            if (this.terrainSide[i] != null) {
                this.terrainSide[i].setSkirting(skirting);
            }
        }
    }

    public Node getPlanetNode() {
        return this.planetNode;
    }

    public Node getTerrainNode() {
        return this.terrainNode;
    }

    public Node getOceanNode() {
        return this.oceanNode;
    }

    public Node getAtmosphereNode() {
        return this.atmosphereNode;
    }

    public float getRadius() {
        return this.baseRadius;
    }

    public float getAtmosphereRadius() {
        return this.atmosphereRadius;
    }

    public float getHeightScale() {
        return this.dataSource.getHeightScale();
    }

    public Vector3f getPlanetToCamera() {
        return this.planetToCamera;
    }

    public float getDistanceToCamera() {
        return this.distanceToCamera;
    }

    public boolean getIsInAtmosphere() {
        return this.currentlyInAtmosphere;
    }

    public boolean getIsTransitioningAtmosphere() {
        return this.currentlyInAtmosphere != this.previouslyInAtmosphere;
    }

    public boolean getIsInOcean() {
        return this.currentlyInOcean;
    }

    public boolean getIsTransitioningOcean() {
        return this.currentlyInOcean != this.previouslyInOcean;
    }

    public ColorRGBA getAtmosphereFogColor() {
        return this.atmosphereFogColor;
    }

    public void setAtmosphereFogColor(ColorRGBA atmosphereFogColor) {
        this.atmosphereFogColor = atmosphereFogColor;
    }

    public float getAtmosphereFogDistance() {
        return this.atmosphereFogDistance;
    }

    public void setAtmosphereFogDistance(float atmosphereFogDistance) {
        this.atmosphereFogDistance = atmosphereFogDistance;
    }

    public float getAtmosphereFogDensity() {
        return this.atmosphereFogDensity;
    }

    public void setAtmosphereFogDensity(float atmosphereFogDensity) {
        this.atmosphereFogDensity = atmosphereFogDensity;
    }

    public ColorRGBA getUnderwaterFogColor() {
        return this.underwaterFogColor;
    }

    public void setUnderwaterFogColor(ColorRGBA underwaterFogColor) {
        this.underwaterFogColor = underwaterFogColor;
    }

    public float getUnderwaterFogDistance() {
        return this.underwaterFogDistance;
    }

    public void setUnderwaterFogDistance(float underwaterFogDistance) {
        this.underwaterFogDistance = underwaterFogDistance;
    }

    public float getUnderwaterFogDensity() {
        return this.underwaterFogDensity;
    }

    public void setUnderwaterFogDensity(float atmosphereFogDensity) {
        this.underwaterFogDensity = this.underwaterFogDensity;
    }

    public void toogleWireframe() {
        if (this.wireframeMode) {
            this.wireframeMode = false;
        } else {
            this.wireframeMode = true;
        }
        setWireframe(this.wireframeMode);
    }

    public void setWireframe(boolean value) {
        for (int i = 0; i < 6; i++) {
            boolean z;
            if (this.terrainSide[i] != null) {
                this.terrainSide[i].setWireframe(value);
            }
            Quad quad;
            if (this.oceanSide[i] != null) {
                quad = this.oceanSide[i];
                z = !value;
                quad.setVisiblity(z);
            }
            if (this.atmosphereSide[i] != null) {
                quad = this.atmosphereSide[i];
                z = !value;
                quad.setVisiblity(z);
            }
        }
    }

    public void setVisiblity(boolean value) {
        for (int i = 0; i < 6; i++) {
            if (this.terrainSide[i] != null) {
                this.terrainSide[i].setVisiblity(value);
            }
            if (this.oceanSide[i] != null) {
                this.oceanSide[i].setVisiblity(value);
            }
            if (this.atmosphereSide[i] != null) {
                this.atmosphereSide[i].setVisiblity(value);
            }
        }
    }

    private void prepareTerrain() {
        this.terrainNode = new Node("TerrainNode");
        Node node = this.terrainNode;
        ShadowMode shadowMode = this.shadowMode;
        node.setShadowMode(ShadowMode.Receive);
        this.planetNode.attachChild(this.terrainNode);
        this.terrainSide[0] = new Quad("TerrainRight", this.terrainMaterial, this.terrainNode, new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, this.texMaxPow), 0.0f, FastMath.pow(2.0f, this.texMaxPow), this.baseRadius, this.dataSource, this.quads, 0, this.minDepth, this.maxDepth, null, 0);
        this.terrainSide[1] = new Quad("TerrainLeft", this.terrainMaterial, this.terrainNode, new Vector3f(-1.0f, 1.0f, -1.0f), new Vector3f(-1.0f, -1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, this.texMaxPow), 0.0f, FastMath.pow(2.0f, this.texMaxPow), this.baseRadius, this.dataSource, this.quads, 0, this.minDepth, this.maxDepth, null, 0);
        this.terrainSide[2] = new Quad("TerrainTop", this.terrainMaterial, this.terrainNode, new Vector3f(-1.0f, 1.0f, -1.0f), new Vector3f(1.0f, 1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, this.texMaxPow), 0.0f, FastMath.pow(2.0f, this.texMaxPow), this.baseRadius, this.dataSource, this.quads, 0, this.minDepth, this.maxDepth, null, 0);
        this.terrainSide[3] = new Quad("TerrainBottom", this.terrainMaterial, this.terrainNode, new Vector3f(-1.0f, -1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, this.texMaxPow), 0.0f, FastMath.pow(2.0f, this.texMaxPow), this.baseRadius, this.dataSource, this.quads, 0, this.minDepth, this.maxDepth, null, 0);
        this.terrainSide[5] = new Quad("TerrainBack", this.terrainMaterial, this.terrainNode, new Vector3f(1.0f, 1.0f, -1.0f), new Vector3f(-1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, this.texMaxPow), 0.0f, FastMath.pow(2.0f, this.texMaxPow), this.baseRadius, this.dataSource, this.quads, 0, this.minDepth, this.maxDepth, null, 0);
        this.terrainSide[4] = new Quad("TerrainFront", this.terrainMaterial, this.terrainNode, new Vector3f(-1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, this.texMaxPow), 0.0f, FastMath.pow(2.0f, this.texMaxPow), this.baseRadius, this.dataSource, this.quads, 0, this.minDepth, this.maxDepth, null, 0);
    }

    private void prepareOcean() {
        this.oceanNode = new Node("OceanNode");
        Node node = this.oceanNode;
        ShadowMode shadowMode = this.shadowMode;
        node.setShadowMode(ShadowMode.Off);
        this.planetNode.attachChild(this.oceanNode);
        int quads = this.quads;
        int maxDepth = this.maxDepth;
        SimpleHeightDataSource dataSource = new SimpleHeightDataSource();
        this.oceanSide[0] = new Quad("OceanRight", this.oceanMaterial, this.oceanNode, new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.baseRadius, dataSource, quads, 0, 0, maxDepth, null, 0);
        this.oceanSide[1] = new Quad("OceanLeft", this.oceanMaterial, this.oceanNode, new Vector3f(-1.0f, 1.0f, -1.0f), new Vector3f(-1.0f, -1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.baseRadius, dataSource, quads, 0, 0, maxDepth, null, 0);
        this.oceanSide[2] = new Quad("OceanTop", this.oceanMaterial, this.oceanNode, new Vector3f(-1.0f, 1.0f, -1.0f), new Vector3f(1.0f, 1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.baseRadius, dataSource, quads, 0, 0, maxDepth, null, 0);
        this.oceanSide[3] = new Quad("OceanBottom", this.oceanMaterial, this.oceanNode, new Vector3f(-1.0f, -1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.baseRadius, dataSource, quads, 0, 0, maxDepth, null, 0);
        this.oceanSide[5] = new Quad("OceanBack", this.oceanMaterial, this.oceanNode, new Vector3f(1.0f, 1.0f, -1.0f), new Vector3f(-1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.baseRadius, dataSource, quads, 0, 0, maxDepth, null, 0);
        this.oceanSide[4] = new Quad("OceanFront", this.oceanMaterial, this.oceanNode, new Vector3f(-1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.baseRadius, dataSource, quads, 0, 0, maxDepth, null, 0);
    }

    private void prepareAtmosphere() {
        this.atmosphereNode = new Node("AtmosphereNode");
        Node node = this.atmosphereNode;
        ShadowMode shadowMode = this.shadowMode;
        node.setShadowMode(ShadowMode.Off);
        this.atmosphereNode.setQueueBucket(Bucket.Transparent);
        int quads = this.quads;
        SimpleHeightDataSource dataSource = new SimpleHeightDataSource();
        this.atmosphereSide[0] = new Quad("AtmosphereRight", this.atmosphereMaterial, this.atmosphereNode, new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.atmosphereRadius, dataSource, quads, 0, 2, 4, null, 0);
        this.atmosphereSide[1] = new Quad("AtmosphereLeft", this.atmosphereMaterial, this.atmosphereNode, new Vector3f(-1.0f, 1.0f, -1.0f), new Vector3f(-1.0f, -1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.atmosphereRadius, dataSource, quads, 0, 2, 4, null, 0);
        this.atmosphereSide[2] = new Quad("AtmosphereTop", this.atmosphereMaterial, this.atmosphereNode, new Vector3f(-1.0f, 1.0f, -1.0f), new Vector3f(1.0f, 1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.atmosphereRadius, dataSource, quads, 0, 2, 4, null, 0);
        this.atmosphereSide[3] = new Quad("AtmosphereBottom", this.atmosphereMaterial, this.atmosphereNode, new Vector3f(-1.0f, -1.0f, 1.0f), new Vector3f(1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.atmosphereRadius, dataSource, quads, 0, 2, 4, null, 0);
        this.atmosphereSide[5] = new Quad("AtmosphereBack", this.atmosphereMaterial, this.atmosphereNode, new Vector3f(1.0f, 1.0f, -1.0f), new Vector3f(-1.0f, -1.0f, -1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.atmosphereRadius, dataSource, quads, 0, 2, 4, null, 0);
        this.atmosphereSide[4] = new Quad("AtmosphereFront", this.atmosphereMaterial, this.atmosphereNode, new Vector3f(-1.0f, 1.0f, 1.0f), new Vector3f(1.0f, -1.0f, 1.0f), 0.0f, FastMath.pow(2.0f, 20.0f), 0.0f, FastMath.pow(2.0f, 20.0f), this.atmosphereRadius, dataSource, quads, 0, 2, 4, null, 0);
    }
}
