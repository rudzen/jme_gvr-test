//package com.asteroidbelt.effects.shapes;
//
//import com.jme3.effect.shapes.*;
//import com.jme3.export.InputCapsule;
//import com.jme3.export.JmeExporter;
//import com.jme3.export.JmeImporter;
//import com.jme3.export.OutputCapsule;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Mesh;
//import com.jme3.scene.VertexBuffer.Type;
//import com.jme3.util.BufferUtils;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//public class EmitterMeshVertexShape implements com.jme3.effect.shapes.EmitterShape {
//    protected List<List<Vector3f>> normals;
//    protected List<List<Vector3f>> vertices;
//
//    public EmitterMeshVertexShape(List<Mesh> meshes) {
//        setMeshes(meshes);
//    }
//
//    public void setMeshes(List<Mesh> meshes) {
//        Map<Vector3f, Vector3f> vertToNormalMap = new HashMap();
//        this.vertices = new ArrayList(meshes.size());
//        this.normals = new ArrayList(meshes.size());
//        for (Mesh mesh : meshes) {
//            float[] vertexTable = BufferUtils.getFloatArray(mesh.getFloatBuffer(Type.Position));
//            float[] normalTable = BufferUtils.getFloatArray(mesh.getFloatBuffer(Type.Normal));
//            for (int i = 0; i < vertexTable.length; i += 3) {
//                Vector3f vert = new Vector3f(vertexTable[i], vertexTable[i + 1], vertexTable[i + 2]);
//                Vector3f norm = (Vector3f) vertToNormalMap.get(vert);
//                if (norm == null) {
//                    vertToNormalMap.put(vert, new Vector3f(normalTable[i], normalTable[i + 1], normalTable[i + 2]));
//                } else {
//                    norm.addLocal(normalTable[i], normalTable[i + 1], normalTable[i + 2]);
//                }
//            }
//            List<Vector3f> vertices = new ArrayList(vertToNormalMap.size());
//            List<Vector3f> normals = new ArrayList(vertToNormalMap.size());
//            for (Entry<Vector3f, Vector3f> entry : vertToNormalMap.entrySet()) {
//                vertices.add(entry.getKey());
//                normals.add(((Vector3f) entry.getValue()).normalizeLocal());
//            }
//            this.vertices.add(vertices);
//            this.normals.add(normals);
//        }
//    }
//
//    public void getRandomPoint(Vector3f store) {
//        int meshIndex = FastMath.nextRandomInt(0, this.vertices.size() - 1);
//        store.set((Vector3f) ((List) this.vertices.get(meshIndex)).get(FastMath.nextRandomInt(0, ((List) this.vertices.get(meshIndex)).size() - 1)));
//    }
//
//    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
//        int meshIndex = FastMath.nextRandomInt(0, this.vertices.size() - 1);
//        int vertIndex = FastMath.nextRandomInt(0, ((List) this.vertices.get(meshIndex)).size() - 1);
//        store.set((Vector3f) ((List) this.vertices.get(meshIndex)).get(vertIndex));
//        normal.set((Vector3f) ((List) this.normals.get(meshIndex)).get(vertIndex));
//    }
//
//    public com.jme3.effect.shapes.EmitterShape deepClone() {
//        try {
//            List<Vector3f> vectorList;
//            EmitterMeshVertexShape clone = (EmitterMeshVertexShape) super.clone();
//            if (this.vertices != null) {
//                clone.vertices = new ArrayList(this.vertices.size());
//                for (List<Vector3f> list : this.vertices) {
//                    vectorList = new ArrayList(list.size());
//                    for (Vector3f vector : list) {
//                        vectorList.add(vector.clone());
//                    }
//                    clone.vertices.add(vectorList);
//                }
//            }
//            if (this.normals != null) {
//                clone.normals = new ArrayList(this.normals.size());
//                for (List<Vector3f> list2 : this.normals) {
//                    vectorList = new ArrayList(list2.size());
//                    for (Vector3f vector2 : list2) {
//                        vectorList.add(vector2.clone());
//                    }
//                    clone.normals.add(vectorList);
//                }
//            }
//            return clone;
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError();
//        }
//    }
//
//    public void write(JmeExporter ex) throws IOException {
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.writeSavableArrayList((ArrayList) this.vertices, "vertices", null);
//        oc.writeSavableArrayList((ArrayList) this.normals, "normals", null);
//    }
//
//    public void read(JmeImporter im) throws IOException {
//        InputCapsule ic = im.getCapsule(this);
//        this.vertices = ic.readSavableArrayList("vertices", null);
//        List<List<Vector3f>> tmpNormals = ic.readSavableArrayList("normals", null);
//        if (tmpNormals != null) {
//            this.normals = tmpNormals;
//        }
//    }
//}
