package com.asteroidbelt.effects.shapes;

import com.jme3.effect.shapes.EmitterMeshVertexShape;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.List;

public class EmitterMeshFaceShape extends EmitterMeshVertexShape {
    public EmitterMeshFaceShape(List<Mesh> meshes) {
        super(meshes);
    }

    public void setMeshes(List<Mesh> meshes) {
        this.vertices = new ArrayList(meshes.size());
        this.normals = new ArrayList(meshes.size());
        for (Mesh mesh : meshes) {
            Vector3f[] vertexTable = BufferUtils.getVector3Array(mesh.getFloatBuffer(Type.Position));
            int[] indices = new int[3];
            List<Vector3f> vertices = new ArrayList(mesh.getTriangleCount() * 3);
            List<Vector3f> normals = new ArrayList(mesh.getTriangleCount());
            for (int i = 0; i < mesh.getTriangleCount(); i++) {
                mesh.getTriangle(i, indices);
                vertices.add(vertexTable[indices[0]]);
                vertices.add(vertexTable[indices[1]]);
                vertices.add(vertexTable[indices[2]]);
                normals.add(FastMath.computeNormal(vertexTable[indices[0]], vertexTable[indices[1]], vertexTable[indices[2]]));
            }
            this.vertices.add(vertices);
            this.normals.add(normals);
        }
    }

    public void getRandomPoint(Vector3f store) {
        int meshIndex = FastMath.nextRandomInt(0, this.vertices.size() - 1);
        int vertIndex = FastMath.nextRandomInt(0, (((List) this.vertices.get(meshIndex)).size() / 3) - 1) * 3;
        float moveFactor = FastMath.nextRandomFloat();
        store.set(Vector3f.ZERO);
        store.addLocal((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex));
        store.addLocal((((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 1)).x - ((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex)).x) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 1)).y - ((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex)).y) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 1)).z - ((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex)).z) * moveFactor);
        moveFactor = FastMath.nextRandomFloat();
        store.addLocal((((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 2)).x - store.x) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 2)).y - store.y) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 2)).z - store.z) * moveFactor);
    }

    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        int meshIndex = FastMath.nextRandomInt(0, this.vertices.size() - 1);
        int faceIndex = FastMath.nextRandomInt(0, (((List) this.vertices.get(meshIndex)).size() / 3) - 1);
        int vertIndex = faceIndex * 3;
        float moveFactor = FastMath.nextRandomFloat();
        store.set(Vector3f.ZERO);
        store.addLocal((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex));
        store.addLocal((((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 1)).x - ((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex)).x) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 1)).y - ((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex)).y) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 1)).z - ((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex)).z) * moveFactor);
        moveFactor = FastMath.nextRandomFloat();
        store.addLocal((((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 2)).x - store.x) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 2)).y - store.y) * moveFactor, (((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex + 2)).z - store.z) * moveFactor);
        normal.set((Vector3f) ((List) this.normals.get(meshIndex)).get(faceIndex));
    }
}
