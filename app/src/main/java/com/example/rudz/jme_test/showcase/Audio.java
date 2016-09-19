package com.example.rudz.jme_test.showcase;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Environment;
import com.jme3.math.Vector3f;
import java.util.HashMap;

/**
 *
 * @author rudz
 */
public class Audio {

    public byte AMBIENCE = 0;
    public byte WAVES = 1;
    public byte ENGINE = 2;
    public byte MUSIC = 3;
    public Environment environment;
    private final float[] def_eax = new float[]{
            15, 38.0f, 0.300f, -1000, -3300, 0,
            1.49f, 0.54f, 1.00f, -2560, 0.162f, 0.00f, 0.00f,
            0.00f, -229, 0.088f, 0.00f, 0.00f, 0.00f, 0.125f, 1.000f,
            0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f};
    public final HashMap<String, float[]> eax;
    public final HashMap<Byte, AudioNode> nodes;

    public Audio() {
        eax = new HashMap<>();
        nodes = new HashMap<>();
        environment = new Environment(def_eax);
    }

    public void setDefaults(AssetManager assetManager, AudioRenderer audioRenderer) {

        audioRenderer.setEnvironment(environment);

        // default AudioNodes
        nodes.put(AMBIENCE, new AudioNode(assetManager, "Sounds/Environment/Nature.ogg", true));
        nodes.put(WAVES, new AudioNode(assetManager, "Sounds/Environment/Ocean Waves.ogg", true));
        nodes.put(ENGINE, new AudioNode(assetManager, "Sounds/Effects/Gun.wav", false));
        nodes.put(MUSIC, new AudioNode(assetManager, "Sounds/Loops/Space Fighter Loop.ogg", true));

        nodes.get(WAVES).setPositional(true);
        nodes.get(WAVES).setLocalTranslation(new Vector3f(0, 0, 0));
        nodes.get(WAVES).setMaxDistance(100);
        nodes.get(WAVES).setRefDistance(5);
        nodes.get(WAVES).setVolume(3);

        nodes.get(AMBIENCE).setPositional(false);
        nodes.get(AMBIENCE).setVolume(5);

        nodes.get(ENGINE).setPositional(true);
        nodes.get(ENGINE).setPitch(2f);
        nodes.get(ENGINE).setVolume(7);
        nodes.get(ENGINE).setLooping(true);

        nodes.get(MUSIC).setPositional(true);
        nodes.get(MUSIC).setLocalTranslation(new Vector3f(0, 0, 0));
        nodes.get(MUSIC).setMaxDistance(100);
        nodes.get(MUSIC).setRefDistance(5);
        nodes.get(MUSIC).setVolume(3);

        //engine.setVelocity(new Vector3f(144.0f, 144.0f, 144.0f));
        //engine.play();
    }

    public boolean addEax(final String key, final float[] eax) {
        if (key.isEmpty() || eax.length != 28) {
            return false;
        }
        this.eax.put(key, eax);
        return true;
    }

    public boolean applyEax(final String eaxKey) {
        if (eax.containsKey(eaxKey)) {
            environment = new Environment(eax.get(eaxKey));
            return true;
        }
        return false;
    }
}
