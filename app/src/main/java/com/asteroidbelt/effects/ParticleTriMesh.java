//package com.asteroidbelt.effects;
//
//import com.jme3.effect.*;
//import com.jme3.effect.Particle;
//import com.jme3.effect.ParticleEmitter;
//import com.jme3.math.FastMath;
//import com.jme3.math.Matrix3f;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.Camera;
//import com.jme3.scene.Mesh.Mode;
//import com.jme3.scene.VertexBuffer;
//import com.jme3.scene.VertexBuffer.Format;
//import com.jme3.scene.VertexBuffer.Type;
//import com.jme3.scene.VertexBuffer.Usage;
//import com.jme3.util.BufferUtils;
//import com.jme3.util.TempVars;
//import java.nio.Buffer;
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//import java.nio.ShortBuffer;
//
//public class ParticleTriMesh extends com.jme3.effect.ParticleMesh {
//    private com.jme3.effect.ParticleEmitter emitter;
//    private int imagesX = 1;
//    private int imagesY = 1;
//    private boolean uniqueTexCoords = false;
//
//    public void initParticleData(ParticleEmitter emitter, int numParticles) {
//        int i;
//        setMode(Mode.Triangles);
//        this.emitter = emitter;
//        FloatBuffer pb = BufferUtils.createVector3Buffer(numParticles * 4);
//        VertexBuffer buf = getBuffer(Type.Position);
//        if (buf != null) {
//            buf.updateData(pb);
//        } else {
//            VertexBuffer pvb = new VertexBuffer(Type.Position);
//            pvb.setupData(Usage.Stream, 3, Format.Float, pb);
//            setBuffer(pvb);
//        }
//        ByteBuffer cb = BufferUtils.createByteBuffer((numParticles * 4) * 4);
//        buf = getBuffer(Type.Color);
//        if (buf != null) {
//            buf.updateData(cb);
//        } else {
//            VertexBuffer cvb = new VertexBuffer(Type.Color);
//            cvb.setupData(Usage.Stream, 4, Format.UnsignedByte, cb);
//            cvb.setNormalized(true);
//            setBuffer(cvb);
//        }
//        FloatBuffer tb = BufferUtils.createVector2Buffer(numParticles * 4);
//        this.uniqueTexCoords = false;
//        for (i = 0; i < numParticles; i++) {
//            tb.put(0.0f).put(1.0f);
//            tb.put(1.0f).put(1.0f);
//            tb.put(0.0f).put(0.0f);
//            tb.put(1.0f).put(0.0f);
//        }
//        tb.flip();
//        buf = getBuffer(Type.TexCoord);
//        if (buf != null) {
//            buf.updateData(tb);
//        } else {
//            VertexBuffer tvb = new VertexBuffer(Type.TexCoord);
//            tvb.setupData(Usage.Static, 2, Format.Float, tb);
//            setBuffer(tvb);
//        }
//        ShortBuffer ib = BufferUtils.createShortBuffer(numParticles * 6);
//        for (i = 0; i < numParticles; i++) {
//            int startIdx = i * 4;
//            ib.put((short) (startIdx + 1)).put((short) (startIdx + 0)).put((short) (startIdx + 2));
//            ib.put((short) (startIdx + 1)).put((short) (startIdx + 2)).put((short) (startIdx + 3));
//        }
//        ib.flip();
//        buf = getBuffer(Type.Index);
//        if (buf != null) {
//            buf.updateData(ib);
//        } else {
//            VertexBuffer ivb = new VertexBuffer(Type.Index);
//            ivb.setupData(Usage.Static, 3, Format.UnsignedShort, ib);
//            setBuffer(ivb);
//        }
//        updateCounts();
//    }
//
//    public void setImagesXY(int imagesX, int imagesY) {
//        this.imagesX = imagesX;
//        this.imagesY = imagesY;
//        if (imagesX != 1 || imagesY != 1) {
//            this.uniqueTexCoords = true;
//            getBuffer(Type.TexCoord).setUsage(Usage.Stream);
//        }
//    }
//
//    public void updateParticleData(com.jme3.effect.Particle[] particles, Camera cam, Matrix3f inverseRotation) {
//        VertexBuffer pvb = getBuffer(Type.Position);
//        Buffer positions = (FloatBuffer) pvb.getData();
//        VertexBuffer cvb = getBuffer(Type.Color);
//        ByteBuffer colors = (ByteBuffer) cvb.getData();
//        VertexBuffer tvb = getBuffer(Type.TexCoord);
//        Buffer texcoords = (FloatBuffer) tvb.getData();
//        Vector3f camUp = cam.getUp();
//        Vector3f camLeft = cam.getLeft();
//        Vector3f camDir = cam.getDirection();
//        inverseRotation.multLocal(camUp);
//        inverseRotation.multLocal(camLeft);
//        inverseRotation.multLocal(camDir);
//        boolean facingVelocity = this.emitter.isFacingVelocity();
//        Vector3f up = new Vector3f();
//        Vector3f left = new Vector3f();
//        if (!facingVelocity) {
//            up.set(camUp);
//            left.set(camLeft);
//        }
//        positions.clear();
//        colors.clear();
//        texcoords.clear();
//        Vector3f faceNormal = this.emitter.getFaceNormal();
//        for (Particle p : particles) {
//            if (p.life == 0.0f) {
//                positions.put(0.0f).put(0.0f).put(0.0f);
//                positions.put(0.0f).put(0.0f).put(0.0f);
//                positions.put(0.0f).put(0.0f).put(0.0f);
//                positions.put(0.0f).put(0.0f).put(0.0f);
//            } else {
//                if (facingVelocity) {
//                    left.set(p.velocity).normalizeLocal();
//                    camDir.cross(left, up);
//                    up.multLocal(p.size);
//                    left.multLocal(p.size);
//                } else if (faceNormal != null) {
//                    up.set(faceNormal).crossLocal(Vector3f.UNIT_X);
//                    faceNormal.cross(up, left);
//                    up.multLocal(p.size);
//                    left.multLocal(p.size);
//                    if (p.angle != 0.0f) {
//                        TempVars vars = TempVars.get();
//                        vars.vect1.set(faceNormal).normalizeLocal();
//                        vars.quat1.fromAngleNormalAxis(p.angle, vars.vect1);
//                        vars.quat1.multLocal(left);
//                        vars.quat1.multLocal(up);
//                        vars.release();
//                    }
//                } else if (p.angle != 0.0f) {
//                    float cos = FastMath.cos(p.angle) * p.size;
//                    float sin = FastMath.sin(p.angle) * p.size;
//                    left.x = (camLeft.x * cos) + (camUp.x * sin);
//                    left.y = (camLeft.y * cos) + (camUp.y * sin);
//                    left.z = (camLeft.z * cos) + (camUp.z * sin);
//                    up.x = (camLeft.x * (-sin)) + (camUp.x * cos);
//                    up.y = (camLeft.y * (-sin)) + (camUp.y * cos);
//                    up.z = (camLeft.z * (-sin)) + (camUp.z * cos);
//                } else {
//                    up.set(camUp);
//                    left.set(camLeft);
//                    up.multLocal(p.size);
//                    left.multLocal(p.size);
//                }
//                positions.put((p.position.x + left.x) + up.x).put((p.position.y + left.y) + up.y).put((p.position.z + left.z) + up.z);
//                positions.put((p.position.x - left.x) + up.x).put((p.position.y - left.y) + up.y).put((p.position.z - left.z) + up.z);
//                positions.put((p.position.x + left.x) - up.x).put((p.position.y + left.y) - up.y).put((p.position.z + left.z) - up.z);
//                positions.put((p.position.x - left.x) - up.x).put((p.position.y - left.y) - up.y).put((p.position.z - left.z) - up.z);
//                if (this.uniqueTexCoords) {
//                    int imgX = p.imageIndex % this.imagesX;
//                    float startX = ((float) imgX) / ((float) this.imagesX);
//                    float startY = ((float) ((p.imageIndex - imgX) / this.imagesY)) / ((float) this.imagesY);
//                    float endX = startX + (1.0f / ((float) this.imagesX));
//                    float endY = startY + (1.0f / ((float) this.imagesY));
//                    texcoords.put(startX).put(endY);
//                    texcoords.put(endX).put(endY);
//                    texcoords.put(startX).put(startY);
//                    texcoords.put(endX).put(startY);
//                }
//                int abgr = p.color.asIntABGR();
//                colors.putInt(abgr);
//                colors.putInt(abgr);
//                colors.putInt(abgr);
//                colors.putInt(abgr);
//            }
//        }
//        positions.clear();
//        colors.clear();
//        if (this.uniqueTexCoords) {
//            texcoords.clear();
//            tvb.updateData(texcoords);
//        } else {
//            texcoords.clear();
//        }
//        pvb.updateData(positions);
//        cvb.updateData(colors);
//    }
//}
