package com.example.rudz.jme_test.asteroid.jmeplanet;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.TextureCubeMap;

public class Utility {
    public static Node createGridAxis(AssetManager assetManager, int lines, int spacing) {
        Node grid = new Node("Grid Axis");
        float half_size = (((float) (lines * spacing)) / 2.0f) - ((float) (spacing / 2));
        Geometry xGrid = new Geometry();
        Material xMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        xMat.setColor("Color", ColorRGBA.Blue);
        xGrid.setMesh(new Grid(lines, lines, (float) spacing));
        xGrid.setMaterial(xMat);
        grid.attachChild(xGrid);
        xGrid.setLocalTranslation(-half_size, 0.0f, -half_size);
        Geometry yGrid = new Geometry();
        Material yMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        yMat.setColor("Color", ColorRGBA.Green);
        yGrid.setMesh(new Grid(lines, lines, (float) spacing));
        yGrid.setMaterial(yMat);
        grid.attachChild(yGrid);
        yGrid.rotate(FastMath.HALF_PI, 0.0f, 0.0f);
        yGrid.setLocalTranslation(-half_size, half_size, 0.0f);
        Geometry zGrid = new Geometry();
        Material zMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        zMat.setColor("Color", ColorRGBA.Red);
        zGrid.setMesh(new Grid(lines, lines, (float) spacing));
        zGrid.setMaterial(zMat);
        grid.attachChild(zGrid);
        zGrid.rotate(0.0f, 0.0f, FastMath.HALF_PI);
        zGrid.setLocalTranslation(0.0f, -half_size, -half_size);
        return grid;
    }

    public static Spatial createSkyBox(AssetManager assetManager, String textureName) {
        Mesh sphere = new Sphere(10, 10, 10000.0f);
        sphere.setStatic();
        Geometry sky = new Geometry("SkyBox", sphere);
        sky.setQueueBucket(Bucket.Sky);
        sky.setCullHint(CullHint.Never);
        sky.setShadowMode(ShadowMode.Off);
        sky.setModelBound(new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO));
        TextureCubeMap cubemap = new TextureCubeMap(assetManager.loadTexture("Textures/blue-glow-1024.dds").getImage());
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Sky.j3md");
        mat.setBoolean("SphereMap", false);
        mat.setTexture("Texture", cubemap);
        mat.setVector3("NormalScale", Vector3f.UNIT_XYZ);
        sky.setMaterial(mat);
        return sky;
    }

    public static Planet createEarthLikePlanet(AssetManager assetManager, float radius, Material oceanMaterial, HeightDataSource dataSource) {
        float heightScale = dataSource.getHeightScale();
        Material material = new Material(assetManager, "JmePlanet/MatDefs/Terrain.j3md");
        material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);
        Texture dirt = assetManager.loadTexture("Textures/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        material.setTexture("Region1ColorMap", dirt);
        material.setVector3("Region1", new Vector3f(0.0f, 0.2f * heightScale, 0.0f));
        Texture grass = assetManager.loadTexture("Textures/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        material.setTexture("Region2ColorMap", grass);
        material.setVector3("Region2", new Vector3f(0.16f * heightScale, 1.05f * heightScale, 0.0f));
        Texture gravel = assetManager.loadTexture("Textures/gravel_mud.jpg");
        gravel.setWrap(WrapMode.Repeat);
        material.setTexture("Region3ColorMap", gravel);
        material.setVector3("Region3", new Vector3f(0.84f * heightScale, 1.1f * heightScale, 0.0f));
        Texture snow = assetManager.loadTexture("Textures/snow.jpg");
        snow.setWrap(WrapMode.Repeat);
        material.setTexture("Region4ColorMap", snow);
        material.setVector3("Region4", new Vector3f(0.94f * heightScale, 1.5f * heightScale, 0.0f));
        Texture rock = assetManager.loadTexture("Textures/rock.jpg");
        rock.setWrap(WrapMode.Repeat);
        material.setTexture("SlopeColorMap", rock);
        Planet planet = new Planet("Planet", radius, material, dataSource);
        if (oceanMaterial == null) {
            oceanMaterial = assetManager.loadMaterial("Materials/Ocean.j3m");
        }
        planet.createOcean(oceanMaterial);
        Material atmosphereMaterial = new Material(assetManager, "JmePlanet/MatDefs/Atmosphere.j3md");
        float atmosphereRadius = radius + (0.025f * radius);
        atmosphereMaterial.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 1.0f, 1.0f));
        atmosphereMaterial.setColor("Diffuse", new ColorRGBA(0.1886778f, 0.4978443f, 0.66160655f, 1.0f));
        atmosphereMaterial.setColor("Specular", new ColorRGBA(0.7f, 0.7f, 1.0f, 1.0f));
        atmosphereMaterial.setFloat("Shininess", 3.0f);
        Material cloudMat = assetManager.loadMaterial("Materials/Clouds.j3m");
        Sphere cloudSphere = new Sphere(50, 50, 1.0f + atmosphereRadius, true, false);
        cloudSphere.setTextureMode(TextureMode.Projected);
        Geometry cloudGeom = new Geometry("Clouds", cloudSphere);
        cloudGeom.rotate(FastMath.HALF_PI, 0.0f, 0.0f);
        cloudGeom.setMaterial(cloudMat);
        cloudGeom.setQueueBucket(Bucket.Transparent);
        cloudMat.setBoolean("SelectFromTexture", true);
        cloudGeom.setShadowMode(ShadowMode.CastAndReceive);
        planet.attachChild(cloudGeom);
        return planet;
    }

    public static Planet createMoonLikePlanet(AssetManager assetManager, float radius, HeightDataSource dataSource) {
        float heightScale = dataSource.getHeightScale();
        Material planetMaterial = new Material(assetManager, "JmePlanet/MatDefs/Terrain.j3md");
        Texture region1 = assetManager.loadTexture("Textures/moon_sea.jpg");
        region1.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region1ColorMap", region1);
        planetMaterial.setVector3("Region1", new Vector3f(heightScale * 0.0f, heightScale * 0.75f, 0.0f));
        Texture region2 = assetManager.loadTexture("Textures/moon.jpg");
        region2.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region2ColorMap", region2);
        planetMaterial.setVector3("Region2", new Vector3f(heightScale * 0.0f, heightScale * 0.75f, 0.0f));
        Texture region3 = assetManager.loadTexture("Textures/moon.jpg");
        region3.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region3ColorMap", region3);
        planetMaterial.setVector3("Region3", new Vector3f(heightScale * 0.0f, heightScale * 0.75f, 0.0f));
        Texture region4 = assetManager.loadTexture("Textures/moon_rough.jpg");
        region4.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region4ColorMap", region4);
        planetMaterial.setVector3("Region4", new Vector3f(0.5f * heightScale, 1.0f * heightScale, 0.0f));
        planetMaterial.setTexture("SlopeColorMap", region3);
        return new Planet("Moon", radius, planetMaterial, dataSource);
    }

    public static Planet createMiniPlanet(AssetManager assetManager, float radius, HeightDataSource dataSource) {
        float heightScale = dataSource.getHeightScale();
        Material planetMaterial = new Material(assetManager, "JmePlanet/MatDefs/Terrain.j3md");
        Texture region1 = assetManager.loadTexture("Textures/moon_sea.jpg");
        region1.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region1ColorMap", region1);
        planetMaterial.setVector3("Region1", new Vector3f(heightScale * 0.0f, heightScale * 0.75f, 0.0f));
        Texture region2 = assetManager.loadTexture("Textures/moon.jpg");
        region2.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region2ColorMap", region2);
        planetMaterial.setVector3("Region2", new Vector3f(heightScale * 0.0f, heightScale * 0.75f, 0.0f));
        Texture region3 = assetManager.loadTexture("Textures/moon.jpg");
        region3.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region3ColorMap", region3);
        planetMaterial.setVector3("Region3", new Vector3f(heightScale * 0.0f, heightScale * 0.75f, 0.0f));
        Texture region4 = assetManager.loadTexture("Textures/moon_rough.jpg");
        region4.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region4ColorMap", region4);
        planetMaterial.setVector3("Region4", new Vector3f(0.5f * heightScale, 1.0f * heightScale, 0.0f));
        planetMaterial.setTexture("SlopeColorMap", region3);
        return new Planet("Moon", radius, planetMaterial, dataSource, 16, 1, 4);
    }
}
