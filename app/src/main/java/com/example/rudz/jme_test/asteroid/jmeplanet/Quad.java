package com.example.rudz.jme_test.asteroid.jmeplanet;

import com.jme3.bounding.BoundingBox;
import com.jme3.input.JoystickButton;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.shader.VarType;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.texture.plugins.TGALoader;

public class Quad {
    protected BoundingBox aabb;
    protected float baseRadius;
    protected HeightDataSource dataSource;
    protected int depth;
    protected AbstractHeightMap heightMap;
    protected Material material;
    protected Vector3f max;
    protected int maxDepth;
    protected Vector3f min;
    protected int minDepth;
    protected String name;
    protected Quad[] neighborQuad = new Quad[4];
    protected Node parentNode;
    protected Quad parentQuad;
    protected Patch patch;
    protected int position;
    protected Vector3f quadCenter;
    protected Geometry quadGeometry;
    protected Node quadNode;
    protected int quads;
    protected Quad[] subQuad = new Quad[4];
    protected float texXMax;
    protected float texXMin;
    protected float texYMax;
    protected float texYMin;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$jme3$shader$VarType = new int[VarType.values().length];

        static {
            try {
                $SwitchMap$com$jme3$shader$VarType[VarType.Boolean.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    enum Neighbor {
        Top,
        Right,
        Bottom,
        Left
    }

    public Quad(String name, Material material, Node parentNode, Vector3f min, Vector3f max, float texXMin, float texXMax, float texYMin, float texYMax, float baseRadius, HeightDataSource dataSource, int quads, int depth, int minDepth, int maxDepth, Quad parentQuad, int position) {
        this.name = name;
        this.material = material.clone();
        this.min = min;
        this.max = max;
        this.texXMin = texXMin;
        this.texXMax = texXMax;
        this.texYMin = texYMin;
        this.texYMax = texYMax;
        this.baseRadius = baseRadius;
        this.dataSource = dataSource;
        this.quads = quads;
        this.depth = depth;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
        this.parentQuad = parentQuad;
        this.position = position;
        this.parentNode = parentNode;
        this.aabb = new BoundingBox();
        this.quadCenter = new Vector3f();
    }

    public void setCameraPosition(Vector3f position) {
        int i;
        for (i = 0; i < 4; i++) {
            if (this.subQuad[i] != null) {
                this.subQuad[i].setCameraPosition(position);
            }
        }
        float distanceToEdge = this.aabb.distanceToEdge(position);
        float aabbLength = this.aabb.getExtent(null).length();
        if (!(this.quadGeometry == null && (this.subQuad[0] == null || this.subQuad[1] == null || this.subQuad[2] == null || this.subQuad[3] == null)) && (this.depth < this.minDepth || (this.depth < this.maxDepth && distanceToEdge < aabbLength))) {
            if (this.subQuad[0] == null || this.subQuad[1] == null || this.subQuad[2] == null || this.subQuad[3] == null) {
                prepareSubQuads();
            } else {
                hide();
            }
        } else if (this.subQuad[0] != null && !this.subQuad[0].isLeaf()) {
        } else {
            if (this.subQuad[1] != null && !this.subQuad[1].isLeaf()) {
                return;
            }
            if (this.subQuad[2] != null && !this.subQuad[2].isLeaf()) {
                return;
            }
            if (this.subQuad[3] == null || this.subQuad[3].isLeaf()) {
                if (!isPrepared()) {
                    preparePatch();
                }
                if (this.quadGeometry == null) {
                    show();
                }
                for (i = 0; i < 4; i++) {
                    if (this.subQuad[i] != null) {
                        this.subQuad[i].hide();
                        this.subQuad[i] = null;
                    }
                }
            }
        }
    }

    public void show() {
        if (this.quadGeometry == null) {
            this.quadGeometry = new Geometry(this.name + "Geometry", this.patch.getMesh());
            if (this.material.getMaterialDef().getMaterialParam("PatchCenter") != null) {
                this.material.setVector3("PatchCenter", this.quadCenter);
            }
            if (this.material.getMaterialDef().getMaterialParam("PlanetRadius") != null) {
                this.material.setFloat("PlanetRadius", this.baseRadius);
            }
            this.quadGeometry.setMaterial(this.material);
        }
        if (this.quadNode == null) {
            this.quadNode = new Node(this.name);
            this.parentNode.attachChild(this.quadNode);
            this.quadNode.setLocalTranslation(this.quadCenter);
        }
        if (this.quadGeometry.getParent() == null) {
            this.quadNode.attachChild(this.quadGeometry);
            this.aabb = (BoundingBox) this.quadNode.getWorldBound();
        }
    }

    public void hide() {
        if (this.patch != null) {
            this.patch = null;
        }
        if (this.quadGeometry != null) {
            this.quadGeometry.removeFromParent();
            this.quadGeometry = null;
        }
        if (this.quadNode != null) {
            this.quadNode.removeFromParent();
            this.quadNode = null;
        }
    }

    public boolean isPrepared() {
        if (this.patch == null) {
            return false;
        }
        return this.patch.isPrepared();
    }

    public boolean isLeaf() {
        return this.subQuad[0] == null && this.subQuad[1] == null && this.subQuad[2] == null && this.subQuad[3] == null;
    }

    public void setWireframe(boolean value) {
        this.material.getAdditionalRenderState().setWireframe(value);
        for (int i = 0; i < 4; i++) {
            if (this.subQuad[i] != null) {
                this.subQuad[i].setWireframe(value);
            }
        }
    }

    public void setVisiblity(boolean value) {
        if (value) {
            this.material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);
        } else {
            this.material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.FrontAndBack);
        }
        for (int i = 0; i < 4; i++) {
            if (this.subQuad[i] != null) {
                this.subQuad[i].setVisiblity(value);
            }
        }
    }

    public void setMaterialParam(String name, VarType type, String value) {
        switch (AnonymousClass1.$SwitchMap$com$jme3$shader$VarType[type.ordinal()]) {
            case TGALoader.TYPE_COLORMAPPED /*1*/:
                this.material.setBoolean(name, Boolean.parseBoolean(value));
                break;
        }
        for (int i = 0; i < 4; i++) {
            if (this.subQuad[i] != null) {
                this.subQuad[i].setMaterialParam(name, type, value);
            }
        }
    }

    public void setSkirting(boolean skirting) {
        if (this.patch != null) {
            this.patch.setSkirting(skirting);
        }
        for (int i = 0; i < 4; i++) {
            if (this.subQuad[i] != null) {
                this.subQuad[i].setSkirting(skirting);
            }
        }
    }

    public int getDepth() {
        return this.depth;
    }

    public int getCurrentMaxDepth() {
        int cDepth = this.depth;
        for (int i = 0; i < 4; i++) {
            if (this.subQuad[i] != null) {
                cDepth = Math.max(cDepth, this.subQuad[i].getCurrentMaxDepth());
            }
        }
        return cDepth;
    }

    protected void preparePatch() {
        this.patch = new Patch(this.quads, this.min, this.max, this.texXMin, this.texXMax, this.texYMin, this.texYMax, this.baseRadius, this.dataSource, this.position, false);
        this.patch.prepare();
        this.quadCenter = this.patch.getCenter();
        this.aabb = this.patch.getAABB();
    }

    protected void prepareSubQuads() {
        float pow;
        Vector3f center = new Vector3f(this.min.x + ((this.max.x - this.min.x) / 2.0f), this.min.y + ((this.max.y - this.min.y) / 2.0f), this.min.z + ((this.max.z - this.min.z) / 2.0f));
        Vector3f topCenter = new Vector3f();
        Vector3f bottomCenter = new Vector3f();
        Vector3f leftCenter = new Vector3f();
        Vector3f rightCenter = new Vector3f();
        Vector3f vector3f;
        if (this.min.x == this.max.x) {
            vector3f = new Vector3f(this.min.x, this.min.y, center.z);
            vector3f = new Vector3f(this.max.x, this.max.y, center.z);
            vector3f = new Vector3f(this.min.x, center.y, this.min.z);
            vector3f = new Vector3f(this.max.x, center.y, this.max.z);
        } else if (this.min.y == this.max.y) {
            vector3f = new Vector3f(center.x, this.min.y, this.min.z);
            vector3f = new Vector3f(center.x, this.max.y, this.max.z);
            vector3f = new Vector3f(this.min.x, this.min.y, center.z);
            vector3f = new Vector3f(this.max.x, this.max.y, center.z);
        } else if (this.min.z == this.max.z) {
            vector3f = new Vector3f(center.x, this.min.y, this.min.z);
            vector3f = new Vector3f(center.x, this.max.y, this.max.z);
            vector3f = new Vector3f(this.min.x, center.y, this.min.z);
            vector3f = new Vector3f(this.max.x, center.y, this.max.z);
        }
        if (this.subQuad[0] == null) {
            float pow2;
            Quad[] quadArr = this.subQuad;
            String str = this.name + JoystickButton.BUTTON_0;
            Material material = this.material;
            Node node = this.parentNode;
            Vector3f vector3f2 = this.min;
            float f = this.depth < this.maxDepth + -9 ? 0.0f : this.texXMin;
            float pow3 = this.depth < this.maxDepth + -9 ? FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f) : this.texXMin + ((this.texXMax - this.texXMin) / 2.0f);
            float f2 = this.depth < this.maxDepth + -9 ? 0.0f : this.texYMin;
            if (this.depth < this.maxDepth - 9) {
                pow2 = FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f);
            } else {
                pow2 = this.texYMin + ((this.texYMax - this.texYMin) / 2.0f);
            }
            quadArr[0] = new Quad(str, material, node, vector3f2, center, f, pow3, f2, pow2, this.baseRadius, this.dataSource, this.quads, this.depth + 1, this.minDepth, this.maxDepth, this, 0);
        }
        Quad[] quadArr2;
        String str2;
        Material material2;
        Node node2;
        float f3, pow4, f4;
        if (this.subQuad[1] == null) {
            quadArr2 = this.subQuad;
            str2 = this.name + JoystickButton.BUTTON_1;
            material2 = this.material;
            node2 = this.parentNode;
            f3 = this.depth < this.maxDepth + -9 ? 0.0f : this.texXMin + ((this.texXMax - this.texXMin) / 2.0f);
            pow4 = this.depth < this.maxDepth + -9 ? FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f) : this.texXMax;
            f4 = this.depth < this.maxDepth + -9 ? 0.0f : this.texYMin;
            if (this.depth < this.maxDepth - 9) {
                pow = FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f);
            } else {
                pow = this.texYMin + ((this.texYMax - this.texYMin) / 2.0f);
            }
            quadArr2[1] = new Quad(str2, material2, node2, topCenter, rightCenter, f3, pow4, f4, pow, this.baseRadius, this.dataSource, this.quads, this.depth + 1, this.minDepth, this.maxDepth, this, 1);
        }
        if (this.subQuad[2] == null) {
            quadArr2 = this.subQuad;
            str2 = this.name + JoystickButton.BUTTON_2;
            material2 = this.material;
            node2 = this.parentNode;
            f3 = this.depth < this.maxDepth + -9 ? 0.0f : this.texXMin;
            pow4 = this.depth < this.maxDepth + -9 ? FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f) : this.texXMin + ((this.texXMax - this.texXMin) / 2.0f);
            f4 = this.depth < this.maxDepth + -9 ? 0.0f : this.texYMin + ((this.texYMax - this.texYMin) / 2.0f);
            if (this.depth < this.maxDepth - 9) {
                pow = FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f);
            } else {
                pow = this.texYMax;
            }
            quadArr2[2] = new Quad(str2, material2, node2, leftCenter, bottomCenter, f3, pow4, f4, pow, this.baseRadius, this.dataSource, this.quads, this.depth + 1, this.minDepth, this.maxDepth, this, 2);
        }
        if (this.subQuad[3] == null) {
            this.subQuad[3] = new Quad(this.name + JoystickButton.BUTTON_3, this.material, this.parentNode, center, this.max, this.depth < this.maxDepth + -9 ? 0.0f : this.texXMin + ((this.texXMax - this.texXMin) / 2.0f), this.depth < this.maxDepth + -9 ? FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f) : this.texXMax, this.depth < this.maxDepth + -9 ? 0.0f : this.texYMin + ((this.texYMax - this.texYMin) / 2.0f), this.depth < this.maxDepth + -9 ? FastMath.pow(2.0f, ((float) (this.maxDepth - this.depth)) - 1.0f) : this.texYMax, this.baseRadius, this.dataSource, this.quads, this.depth + 1, this.minDepth, this.maxDepth, this, 3);
        }
    }
}
