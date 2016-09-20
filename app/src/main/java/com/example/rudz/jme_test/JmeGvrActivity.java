package com.example.rudz.jme_test;

import android.os.Bundle;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

public class JmeGvrActivity extends GvrActivity implements GvrView.StereoRenderer { //} AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onNewFrame(final HeadTransform headTransform) {

    }

    @Override
    public void onDrawEye(final Eye eye) {

    }

    @Override
    public void onFinishFrame(final Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(final int i, final int i1) {

    }

    @Override
    public void onSurfaceCreated(final EGLConfig eglConfig) {

    }

    @Override
    public void onRendererShutdown() {

    }
}
