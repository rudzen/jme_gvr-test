package com.example.rudz.jme_test.showcase;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

public class Terrain {

    private static final float dirtScale = 16.0f;
    private static final float darkRockScale = 32.0f;
    private static final float pinkRockScale = 32.0f;
    private static final float riverRockScale = 80.0f;
    private static final float grassScale = 32.0f;
    private static final float brickScale = 128.0f;
    private static final float roadScale = 200.0f;

    public static void createTerrain(AssetManager assetManager, Node attachTo, Camera camera, BulletAppState bulletAppState) {
        // TERRAIN TEXTURE material
        Material matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setFloat("Shininess", 0.0f);

        // ALPHA map (for splat textures)
        matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
        matTerrain.setTexture("AlphaMap_1", assetManager.loadTexture("Textures/Terrain/splat/alpha2.png"));
        // this material also supports 'AlphaMap_2', so you can get up to 12 diffuse textures

        // HEIGHTMAP image (for the terrain heightmap)
        TextureKey hmKey = new TextureKey("Textures/Terrain/splat/mountains512.png", false);
        Texture heightMapImage = assetManager.loadTexture(hmKey);

        // DIRT texture, Diffuse textures 0 to 3 use the first AlphaMap
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", dirt);
        matTerrain.setFloat("DiffuseMap_0_scale", dirtScale);

        // DARK ROCK texture
        Texture darkRock = assetManager.loadTexture("Textures/Terrain/Rock2/rock.jpg");
        darkRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_1", darkRock);
        matTerrain.setFloat("DiffuseMap_1_scale", darkRockScale);

        // PINK ROCK texture
        Texture pinkRock = assetManager.loadTexture("Textures/Terrain/Rock/Rock.PNG");
        pinkRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_2", pinkRock);
        matTerrain.setFloat("DiffuseMap_2_scale", pinkRockScale);

        // RIVER ROCK texture, this texture will use the next alphaMap: AlphaMap_1
        Texture riverRock = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        riverRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_3", riverRock);
        matTerrain.setFloat("DiffuseMap_3_scale", riverRockScale);

        // GRASS texture
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_4", grass);
        matTerrain.setFloat("DiffuseMap_4_scale", grassScale);

        // BRICK texture
        Texture brick = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        brick.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_5", brick);
        matTerrain.setFloat("DiffuseMap_5_scale", brickScale);

        // ROAD texture
        Texture road = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        road.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_6", road);
        matTerrain.setFloat("DiffuseMap_6_scale", roadScale);

        // diffuse textures 0 to 3 use AlphaMap
        // diffuse textures 4 to 7 use AlphaMap_1
        // diffuse textures 8 to 11 use AlphaMap_2
        // NORMAL MAPS
        Texture normalMapDirt = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
        normalMapDirt.setWrap(Texture.WrapMode.Repeat);
        Texture normalMapPinkRock = assetManager.loadTexture("Textures/Terrain/Rock/Rock_normal.png");
        normalMapPinkRock.setWrap(Texture.WrapMode.Repeat);
        Texture normalMapGrass = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
        normalMapGrass.setWrap(Texture.WrapMode.Repeat);
        Texture normalMapRoad = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
        normalMapRoad.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("NormalMap", normalMapDirt);
        matTerrain.setTexture("NormalMap_1", normalMapPinkRock);
        matTerrain.setTexture("NormalMap_2", normalMapPinkRock);
        matTerrain.setTexture("NormalMap_4", normalMapGrass);
        matTerrain.setTexture("NormalMap_6", normalMapRoad);

        /*
        // WIREFRAME material (used to debug the terrain, only useful for this test case)
        matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
         */

        //createSky();
        // CREATE HEIGHTMAP
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.3f);
            heightmap.load();
            heightmap.smooth(0.9f, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Here we create the actual terrain. The tiles will be 65x65, and the total size of the
         * terrain will be 513x513. It uses the heightmap we created to generate the height values.
         */
        /**
         * Optimal terrain patch size is 65 (64x64). The total size is up to
         * you. At 1025 it ran fine for me (200+FPS), however at size=2049 it
         * got really slow. But that is a jump from 2 million to 8 million
         * triangles...
         */
        TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
        TerrainLodControl control = new TerrainLodControl(terrain, camera);
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f)); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        //terrain.setModelBound(new BoundingBox());
        terrain.updateModelBound();
        terrain.setLocalTranslation(50f, -180f, 0f);
        terrain.setLocalScale(2f, 1f, 2f);
        terrain.addControl(new RigidBodyControl(0f));
        bulletAppState.getPhysicsSpace().add(terrain);
        attachTo.attachChild(terrain);

        //Material debugMat = assetManager.loadMaterial("Common/Materials/VertexColor.j3m");
        //terrain.generateDebugTangents(debugMat);

        /*
        DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(-0.1f, -0.1f, -0.1f)).normalize());
        rootNode.addLight(light);
        */
    }

}