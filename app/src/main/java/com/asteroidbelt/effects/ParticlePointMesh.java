//package com.asteroidbelt.effects;
//
//import com.jme3.effect.*;
//import com.jme3.effect.Particle;
//import com.jme3.effect.ParticleEmitter;
//import com.jme3.math.Matrix3f;
//import com.jme3.renderer.Camera;
//import com.jme3.scene.Mesh.Mode;
//import com.jme3.scene.VertexBuffer;
//import com.jme3.scene.VertexBuffer.Format;
//import com.jme3.scene.VertexBuffer.Type;
//import com.jme3.scene.VertexBuffer.Usage;
//import com.jme3.util.BufferUtils;
//import java.nio.Buffer;
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//
//public class ParticlePointMesh extends com.jme3.effect.ParticleMesh {
//    private com.jme3.effect.ParticleEmitter emitter;
//    private int imagesX = 1;
//    private int imagesY = 1;
//
//    public void setImagesXY(int imagesX, int imagesY) {
//        this.imagesX = imagesX;
//        this.imagesY = imagesY;
//    }
//
//    public void initParticleData(ParticleEmitter emitter, int numParticles) {
//        setMode(Mode.Points);
//        this.emitter = emitter;
//        FloatBuffer pb = BufferUtils.createVector3Buffer(numParticles);
//        VertexBuffer buf = getBuffer(Type.Position);
//        if (buf != null) {
//            buf.updateData(pb);
//        } else {
//            VertexBuffer pvb = new VertexBuffer(Type.Position);
//            pvb.setupData(Usage.Stream, 3, Format.Float, pb);
//            setBuffer(pvb);
//        }
//        ByteBuffer cb = BufferUtils.createByteBuffer(numParticles * 4);
//        buf = getBuffer(Type.Color);
//        if (buf != null) {
//            buf.updateData(cb);
//        } else {
//            VertexBuffer cvb = new VertexBuffer(Type.Color);
//            cvb.setupData(Usage.Stream, 4, Format.UnsignedByte, cb);
//            cvb.setNormalized(true);
//            setBuffer(cvb);
//        }
//        FloatBuffer sb = BufferUtils.createFloatBuffer(numParticles);
//        buf = getBuffer(Type.Size);
//        if (buf != null) {
//            buf.updateData(sb);
//        } else {
//            VertexBuffer svb = new VertexBuffer(Type.Size);
//            svb.setupData(Usage.Stream, 1, Format.Float, sb);
//            setBuffer(svb);
//        }
//        FloatBuffer tb = BufferUtils.createFloatBuffer(numParticles * 4);
//        buf = getBuffer(Type.TexCoord);
//        if (buf != null) {
//            buf.updateData(tb);
//        } else {
//            VertexBuffer tvb = new VertexBuffer(Type.TexCoord);
//            tvb.setupData(Usage.Stream, 4, Format.Float, tb);
//            setBuffer(tvb);
//        }
//        updateCounts();
//    }
//
//    public void updateParticleData(com.jme3.effect.Particle[] particles, Camera cam, Matrix3f inverseRotation) {
//        VertexBuffer pvb = getBuffer(Type.Position);
//        FloatBuffer positions = (FloatBuffer) pvb.getData();
//        VertexBuffer cvb = getBuffer(Type.Color);
//        ByteBuffer colors = (ByteBuffer) cvb.getData();
//        VertexBuffer svb = getBuffer(Type.Size);
//        FloatBuffer sizes = (FloatBuffer) svb.getData();
//        VertexBuffer tvb = getBuffer(Type.TexCoord);
//        Buffer texcoords = (FloatBuffer) tvb.getData();
//        float sizeScale = this.emitter.getWorldScale().x;
//        positions.rewind();
//        colors.rewind();
//        sizes.rewind();
//        texcoords.rewind();
//        for (Particle p : particles) {
//            positions.put(p.position.x).put(p.position.y).put(p.position.z);
//            sizes.put(p.size * sizeScale);
//            colors.putInt(p.color.asIntABGR());
//            int imgX = p.imageIndex % this.imagesX;
//            float startX = ((float) imgX) / ((float) this.imagesX);
//            float startY = ((float) ((p.imageIndex - imgX) / this.imagesY)) / ((float) this.imagesY);
//            texcoords.put(startX).put(startY).put(startX + (1.0f / ((float) this.imagesX))).put(startY + (1.0f / ((float) this.imagesY)));
//        }
//        positions.flip();
//        colors.flip();
//        sizes.flip();
//        texcoords.flip();
//        pvb.updateData(positions);
//        cvb.updateData(colors);
//        svb.updateData(sizes);
//        tvb.updateData(texcoords);
//    }
//}
