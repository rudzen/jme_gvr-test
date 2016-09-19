package com.asteroidbelt.jmeplanet;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.opengl.GL;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.plugins.TGALoader;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Patch {
    protected BoundingBox aabb;
    protected float baseRadius;
    protected Vector3f center;
    protected HeightDataSource dataSource;
    protected int[] edgeVertexIndex;
    protected Vector3f max;
    protected Mesh mesh;
    protected Vector3f min;
    protected int padding = 2;
    protected int position;
    protected int quadTriangles;
    protected int quadVertexCount;
    protected int quadVertexCountPadded;
    protected int quads;
    protected int skirtTriangles;
    protected int skirtVertexCount;
    protected boolean skirting;
    protected float texXMax;
    protected float texXMin;
    protected float texYMax;
    protected float texYMin;
    protected int totalTriangles;
    protected int totalVertexCount;
    protected int verticesPerSide;

    public Patch(int quads, Vector3f min, Vector3f max, float texXMin, float texXMax, float texYMin, float texYMax, float baseRadius, HeightDataSource dataSource, int position, boolean skirting) {
        this.quads = quads;
        this.min = min;
        this.max = max;
        this.texXMin = texXMin;
        this.texXMax = texXMax;
        this.texYMin = texYMin;
        this.texYMax = texYMax;
        this.baseRadius = baseRadius;
        this.dataSource = dataSource;
        this.position = position;
        this.skirting = skirting;
    }

    public Mesh prepare() {
        int indexEdgeVertexIndex;
        this.quadVertexCount = (this.quads + 1) * (this.quads + 1);
        this.quadVertexCountPadded = ((this.quads + (this.padding * 2)) + 1) * ((this.quads + (this.padding * 2)) + 1);
        this.skirtVertexCount = this.quads * 4;
        this.totalVertexCount = this.quadVertexCount + this.skirtVertexCount;
        this.verticesPerSide = this.quads + 1;
        this.quadTriangles = (this.quads * 2) * this.quads;
        this.skirtTriangles = this.skirtVertexCount * 2;
        this.totalTriangles = this.quadTriangles + this.skirtTriangles;
        Vector3f[] vertexPosition = new Vector3f[this.quadVertexCountPadded];
        float[] vertexColor = new float[(this.quadVertexCountPadded * 4)];
        Vector3f[] vertexNormal = new Vector3f[this.quadVertexCount];
        Vector2f[] textureCoordinate = new Vector2f[this.quadVertexCount];
        generateVertexPositions(vertexPosition, vertexColor);
        generateVertexNormals(vertexNormal, textureCoordinate, vertexPosition);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(this.totalVertexCount * 3);
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(this.totalVertexCount * 4);
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(this.totalVertexCount * 3);
        FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(this.quadVertexCount * 4);
        int y = 0;
        while (y < this.quads + 1) {
            int x = 0;
            while (x < this.quads + 1) {
                float f;
                int vi = (((this.quads + (this.padding * 2)) + 1) * (this.padding + y)) + (this.padding + x);
                int ni = ((this.quads + 1) * y) + x;
                vertexBuffer.put(vertexPosition[vi].x);
                vertexBuffer.put(vertexPosition[vi].y);
                vertexBuffer.put(vertexPosition[vi].z);
                colorBuffer.put(vertexColor[vi * 4]);
                colorBuffer.put(vertexColor[(vi * 4) + 1]);
                colorBuffer.put(vertexColor[(vi * 4) + 2]);
                colorBuffer.put(vertexColor[(vi * 4) + 3]);
                normalBuffer.put(vertexNormal[ni].x);
                normalBuffer.put(vertexNormal[ni].y);
                normalBuffer.put(vertexNormal[ni].z);
                textureBuffer.put(textureCoordinate[ni].x);
                textureBuffer.put(textureCoordinate[ni].y);
                if (x == 0) {
                    f = 0.0f;
                } else {
                    f = x == this.quads ? 1.0f : ((float) x) / ((float) this.quads);
                }
                textureBuffer.put(f);
                if (y == 0) {
                    f = 0.0f;
                } else {
                    f = y == this.quads ? 1.0f : ((float) y) / ((float) this.quads);
                }
                textureBuffer.put(f);
                x++;
            }
            y++;
        }
        int indexEdgeVertexIndex2 = 0;
        this.edgeVertexIndex = new int[this.skirtVertexCount];
        int i = 0;
        while (i < this.verticesPerSide) {
            indexEdgeVertexIndex = indexEdgeVertexIndex2 + 1;
            this.edgeVertexIndex[indexEdgeVertexIndex2] = i;
            i++;
            indexEdgeVertexIndex2 = indexEdgeVertexIndex;
        }
        i = this.verticesPerSide + this.quads;
        while (i < this.quadVertexCount + 1) {
            indexEdgeVertexIndex = indexEdgeVertexIndex2 + 1;
            this.edgeVertexIndex[indexEdgeVertexIndex2] = i;
            i += this.verticesPerSide;
            indexEdgeVertexIndex2 = indexEdgeVertexIndex;
        }
        i = this.quadVertexCount - 2;
        while (i >= this.verticesPerSide * this.quads) {
            indexEdgeVertexIndex = indexEdgeVertexIndex2 + 1;
            this.edgeVertexIndex[indexEdgeVertexIndex2] = i;
            i--;
            indexEdgeVertexIndex2 = indexEdgeVertexIndex;
        }
        i = (this.verticesPerSide * this.quads) - this.verticesPerSide;
        indexEdgeVertexIndex = indexEdgeVertexIndex2;
        while (i > 0) {
            indexEdgeVertexIndex2 = indexEdgeVertexIndex + 1;
            this.edgeVertexIndex[indexEdgeVertexIndex] = i;
            i -= this.verticesPerSide;
            indexEdgeVertexIndex = indexEdgeVertexIndex2;
        }
        for (i = 0; i < this.skirtVertexCount; i++) {
            Vector3f v = new Vector3f(vertexBuffer.get(this.edgeVertexIndex[i] * 3), vertexBuffer.get((this.edgeVertexIndex[i] * 3) + 1), vertexBuffer.get((this.edgeVertexIndex[i] * 3) + 2));
            v.subtractLocal(this.center.normalize().mult((this.dataSource.getHeightScale() / 10.0f) + 0.01f));
            vertexBuffer.put(v.x);
            vertexBuffer.put(v.y);
            vertexBuffer.put(v.z);
            normalBuffer.put(normalBuffer.get(this.edgeVertexIndex[i] * 3));
            normalBuffer.put(normalBuffer.get((this.edgeVertexIndex[i] * 3) + 1));
            normalBuffer.put(normalBuffer.get((this.edgeVertexIndex[i] * 3) + 2));
            colorBuffer.put(colorBuffer.get(this.edgeVertexIndex[i] * 4));
            colorBuffer.put(colorBuffer.get((this.edgeVertexIndex[i] * 4) + 1));
            colorBuffer.put(colorBuffer.get((this.edgeVertexIndex[i] * 4) + 2));
            colorBuffer.put(colorBuffer.get((this.edgeVertexIndex[i] * 4) + 3));
        }
        IntBuffer indexBuffer = generateIndices();
        this.mesh = new Mesh();
        this.mesh.setBuffer(Type.Position, 3, vertexBuffer);
        this.mesh.setBuffer(Type.Normal, 3, normalBuffer);
        this.mesh.setBuffer(Type.TexCoord, 4, textureBuffer);
        this.mesh.setBuffer(Type.Color, 4, colorBuffer);
        this.mesh.setBuffer(Type.Index, 3, indexBuffer);
        this.mesh.updateBound();
        return this.mesh;
    }

    public boolean isPrepared() {
        return this.mesh != null;
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public Vector3f getCenter() {
        return this.center;
    }

    public BoundingBox getAABB() {
        return this.aabb;
    }

    public void setSkirting(boolean skirting) {
        if (this.skirting != skirting) {
            this.skirting = skirting;
            IntBuffer indexBuffer = generateIndices();
            this.mesh.clearBuffer(Type.Index);
            this.mesh.setBuffer(Type.Index, 3, indexBuffer);
        }
    }

    protected void generateVertexPositions(Vector3f[] vertexPosition, float[] vertexColor) {
        int y;
        int x;
        Vector2f startPos = new Vector2f();
        Vector2f endPos = new Vector2f();
        int c = 0;
        int side = 0;
        if (((double) this.min.x) == 1.0d && ((double) this.max.x) == 1.0d) {
            side = 0;
            startPos.x = this.min.z;
            startPos.y = this.min.y;
            endPos.x = this.max.z;
            endPos.y = this.max.y;
            c = 1;
        }
        if (((double) this.min.x) == -1.0d && ((double) this.max.x) == -1.0d) {
            side = 1;
            startPos.x = this.min.z;
            startPos.y = this.min.y;
            endPos.x = this.max.z;
            endPos.y = this.max.y;
            c = -1;
        } else if (((double) this.min.y) == 1.0d && ((double) this.max.y) == 1.0d) {
            side = 2;
            startPos.x = this.min.x;
            startPos.y = this.min.z;
            endPos.x = this.max.x;
            endPos.y = this.max.z;
            c = 1;
        } else if (((double) this.min.y) == -1.0d && ((double) this.max.y) == -1.0d) {
            side = 3;
            startPos.x = this.min.x;
            startPos.y = this.min.z;
            endPos.x = this.max.x;
            endPos.y = this.max.z;
            c = -1;
        } else if (((double) this.min.z) == 1.0d && ((double) this.max.z) == 1.0d) {
            side = 4;
            startPos.x = this.min.x;
            startPos.y = this.min.y;
            endPos.x = this.max.x;
            endPos.y = this.max.y;
            c = 1;
        } else if (((double) this.min.z) == -1.0d && ((double) this.max.z) == -1.0d) {
            side = 5;
            startPos.x = this.min.x;
            startPos.y = this.min.y;
            endPos.x = this.max.x;
            endPos.y = this.max.y;
            c = -1;
        }
        Vector3f[] unitSpherePos = new Vector3f[(((this.quads + (this.padding * 2)) + 1) * ((this.quads + (this.padding * 2)) + 1))];
        float[] heightData = new float[(((this.quads + (this.padding * 2)) + 1) * ((this.quads + (this.padding * 2)) + 1))];
        Vector3f pos = new Vector3f();
        int index;
        for (y = 0 - this.padding; y <= this.quads + this.padding; y++) {
            for (x = 0 - this.padding; x <= this.quads + this.padding; x++) {
                index = (((this.quads + (this.padding * 2)) + 1) * (this.padding + y)) + (this.padding + x);
                float xPos = startPos.x + ((endPos.x - startPos.x) * (((float) x) / ((float) this.quads)));
                float yPos = startPos.y + ((endPos.y - startPos.y) * (((float) y) / ((float) this.quads)));
                float zPos = (float) c;
                switch (side) {
                    case TGALoader.TYPE_NO_IMAGE /*0*/:
                        pos.x = zPos;
                        pos.y = yPos;
                        pos.z = xPos;
                        break;
                    case TGALoader.TYPE_COLORMAPPED /*1*/:
                        pos.x = zPos;
                        pos.y = yPos;
                        pos.z = xPos;
                        break;
                    case TGALoader.TYPE_TRUECOLOR /*2*/:
                        pos.x = xPos;
                        pos.y = zPos;
                        pos.z = yPos;
                        break;
                    case TGALoader.TYPE_BLACKANDWHITE /*3*/:
                        pos.x = xPos;
                        pos.y = zPos;
                        pos.z = yPos;
                        break;
                    case 4: //ImageCodec.FLAG_GRAY /*4*/:
                        pos.x = xPos;
                        pos.y = yPos;
                        pos.z = zPos;
                        break;
                    case GL.GL_TRIANGLE_STRIP /*5*/:
                        pos.x = xPos;
                        pos.y = yPos;
                        pos.z = zPos;
                        break;
                    default:
                        break;
                }
                unitSpherePos[index] = pos.normalize();
                heightData[index] = this.dataSource.getValue(unitSpherePos[index]);
            }
        }
        Vector3f minBounds = new Vector3f(2.1474836E9f, 2.1474836E9f, 2.1474836E9f);
        Vector3f maxBounds = new Vector3f(-2.1474836E9f, -2.1474836E9f, -2.1474836E9f);
        for (y = 0; y < (this.quads + (this.padding * 2)) + 1; y++) {
            for (x = 0; x < (this.quads + (this.padding * 2)) + 1; x++) {
                index = (((this.quads + (this.padding * 2)) + 1) * y) + x;
                vertexPosition[index] = unitSpherePos[index].mult(this.baseRadius + heightData[index]);
                ColorRGBA color = getHeightColor(heightData[index], this.dataSource.getHeightScale());
                vertexColor[index * 4] = color.r;
                vertexColor[(index * 4) + 1] = color.g;
                vertexColor[(index * 4) + 2] = color.b;
                vertexColor[(index * 4) + 3] = color.a;
                minBounds.x = Math.min(minBounds.x, vertexPosition[index].x);
                minBounds.y = Math.min(minBounds.y, vertexPosition[index].y);
                minBounds.z = Math.min(minBounds.z, vertexPosition[index].z);
                maxBounds.x = Math.max(maxBounds.x, vertexPosition[index].x);
                maxBounds.y = Math.max(maxBounds.y, vertexPosition[index].y);
                maxBounds.z = Math.max(maxBounds.z, vertexPosition[index].z);
            }
        }
        this.aabb = new BoundingBox(minBounds, maxBounds);
        this.center = this.aabb.getCenter();
        minBounds = minBounds.subtract(this.center);
        maxBounds = maxBounds.subtract(this.center);
        for (y = 0; y < (this.quads + (this.padding * 2)) + 1; y++) {
            for (x = 0; x < (this.quads + (this.padding * 2)) + 1; x++) {
                index = (((this.quads + (this.padding * 2)) + 1) * y) + x;
                vertexPosition[index] = vertexPosition[index].subtract(this.center);
            }
        }
    }

    protected void generateVertexNormals(Vector3f[] vertexNormal, Vector2f[] textureCoordinate, Vector3f[] vertexPosition) {
        for (int y = 0; y < this.quads + 1; y++) {
            for (int x = 0; x < this.quads + 1; x++) {
                int index = ((this.quads + 1) * y) + x;
                int pNextXIndex = (((this.quads + (this.padding * 2)) + 1) * (this.padding + y)) + ((this.padding + x) + 1);
                int pNextYIndex = (((this.quads + (this.padding * 2)) + 1) * ((this.padding + y) + 1)) + (this.padding + x);
                int pPrevXIndex = (((this.quads + (this.padding * 2)) + 1) * (this.padding + y)) + ((this.padding + x) - 1);
                int pPrevYIndex = (((this.quads + (this.padding * 2)) + 1) * ((this.padding + y) - 1)) + (this.padding + x);
                int pNextXPrevYIndex = (((this.quads + (this.padding * 2)) + 1) * ((this.padding + y) - 1)) + ((this.padding + x) + 1);
                int pPrevXNextYIndex = (((this.quads + (this.padding * 2)) + 1) * ((this.padding + y) + 1)) + ((this.padding + x) - 1);
                Vector3f thisVertex = vertexPosition[(((this.quads + (this.padding * 2)) + 1) * (this.padding + y)) + (this.padding + x)];
                Vector3f nextXVertex = vertexPosition[pNextXIndex];
                Vector3f nextYVertex = vertexPosition[pNextYIndex];
                Vector3f prevXVertex = vertexPosition[pPrevXIndex];
                Vector3f prevYVertex = vertexPosition[pPrevYIndex];
                Vector3f nextXPrevYVertex = vertexPosition[pNextXPrevYIndex];
                Vector3f prevXNextYVertex = vertexPosition[pPrevXNextYIndex];
                Vector3f n1 = nextXVertex.subtract(thisVertex).cross(nextXPrevYVertex.subtract(thisVertex));
                Vector3f n2 = nextXPrevYVertex.subtract(thisVertex).cross(prevYVertex.subtract(thisVertex));
                Vector3f n3 = prevYVertex.subtract(thisVertex).cross(prevXVertex.subtract(thisVertex));
                Vector3f n4 = prevXVertex.subtract(thisVertex).cross(prevXNextYVertex.subtract(thisVertex));
                Vector3f n5 = prevXNextYVertex.subtract(thisVertex).cross(nextYVertex.subtract(thisVertex));
                vertexNormal[index] = n1.add(n2).add(n3).add(n4).add(n5).add(nextYVertex.subtract(thisVertex).cross(nextXVertex.subtract(thisVertex))).normalize();
                float jx = ((float) x) / ((float) this.quads);
                float jy = ((float) y) / ((float) this.quads);
                textureCoordinate[index] = new Vector2f(((1.0f - jx) * this.texXMin) + (this.texXMax * jx), ((1.0f - jy) * this.texYMin) + (this.texYMax * jy));
            }
        }
    }

    protected IntBuffer generateIndices() {
        IntBuffer indexBuffer;
        int y;
        int x;
        if (this.skirting) {
            indexBuffer = BufferUtils.createIntBuffer(this.totalTriangles * 3);
        } else {
            indexBuffer = BufferUtils.createIntBuffer(this.quadTriangles * 3);
        }
        for (y = 0; y < this.quads; y++) {
            for (x = 0; x < this.quads; x++) {
                indexBuffer.put(((this.quads + 1) * y) + x);
                indexBuffer.put(((y + 1) * (this.quads + 1)) + x);
                indexBuffer.put((((this.quads + 1) * y) + x) + 1);
                indexBuffer.put(((y + 1) * (this.quads + 1)) + x);
                indexBuffer.put((((y + 1) * (this.quads + 1)) + x) + 1);
                indexBuffer.put((((this.quads + 1) * y) + x) + 1);
            }
        }
        if (this.skirting) {
            int skirtOffset = (this.quads + 1) * (this.quads + 1);
            for (y = 0; y < 1; y++) {
                for (x = 0; x < this.edgeVertexIndex.length; x++) {
                    if (x != this.edgeVertexIndex.length - 1) {
                        indexBuffer.put(this.edgeVertexIndex[x]);
                        indexBuffer.put(this.edgeVertexIndex[x + 1]);
                        indexBuffer.put(x + skirtOffset);
                        indexBuffer.put(x + skirtOffset);
                        indexBuffer.put(this.edgeVertexIndex[x + 1]);
                        indexBuffer.put((x + 1) + skirtOffset);
                    } else {
                        indexBuffer.put(this.edgeVertexIndex[x]);
                        indexBuffer.put(this.edgeVertexIndex[0]);
                        indexBuffer.put(x + skirtOffset);
                        indexBuffer.put(x + skirtOffset);
                        indexBuffer.put(this.edgeVertexIndex[0]);
                        indexBuffer.put(skirtOffset);
                    }
                }
            }
        }
        return indexBuffer;
    }

    protected ColorRGBA getHeightColor(float height, float heightScale) {
        ColorRGBA color = new ColorRGBA();
        if (height <= 0.0f) {
            color.r = 0.0f;
            color.g = 0.4f;
            color.b = 0.8f;
            color.a = 1.0f;
        } else if (height <= heightScale * 0.1f) {
            color.r = 0.83f;
            color.g = 0.72f;
            color.b = 0.34f;
            color.a = 1.0f;
        } else if (height <= heightScale * 0.83f) {
            color.r = 0.2f;
            color.g = 0.6f;
            color.b = 0.1f;
            color.a = 1.0f;
        } else {
            color.r = 0.5f;
            color.g = 0.5f;
            color.b = 0.5f;
            color.a = 1.0f;
        }
        return color;
    }
}
