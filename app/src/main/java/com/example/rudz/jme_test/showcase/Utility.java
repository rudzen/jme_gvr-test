package com.example.rudz.jme_test.showcase;


import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author rudz
 */
public final class Utility {

    private Utility() {

    }

    public static Geometry findGeom(Spatial spatial, String name) {
        if (spatial instanceof Node) {
            Node node = (Node) spatial;
            Spatial child;
            Geometry result;
            for (int i = 0; i < node.getQuantity(); i++) {
                child = node.getChild(i);
                result = findGeom(child, name);
                if (result != null) {
                    return result;
                }
            }
        } else if (spatial instanceof Geometry && spatial.getName().startsWith(name)) {
            return (Geometry) spatial;
        }
        return null;
    }
}
