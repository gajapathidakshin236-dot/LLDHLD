package com.company.patterns.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FLYWEIGHT — share the HEAVY, IMMUTABLE part of many similar objects instead
 * of duplicating it per object.
 *
 * Split every object's state into:
 *   INTRINSIC  — same across many objects, immutable  -> shared flyweight
 *                (here: tree species name, texture, color)
 *   EXTRINSIC  — unique per object                    -> stays outside
 *                (here: x, y position)
 *
 * A forest of 1,000,000 trees with 3 species stores 3 TreeType objects,
 * not 1,000,000 copies of the texture.
 *
 * Real example you already use: Integer.valueOf(-128..127) returns cached
 * instances; String interning; java.awt fonts.
 */
public class FlyweightDemo {
    public static void main(String[] args) {
        Forest forest = new Forest();
        // plant 9 trees of only 2 species
        for (int i = 0; i < 5; i++) forest.plantTree(i * 10, 20, "Oak",  "rough-bark.png");
        for (int i = 0; i < 4; i++) forest.plantTree(i * 10, 50, "Pine", "needles.png");

        forest.drawAll();
        System.out.println("trees planted: 9, TreeType objects created: "
                + TreeTypeFactory.createdCount());
    }
}

/** The FLYWEIGHT: immutable intrinsic state, shared by many trees. */
class TreeType {
    private final String species;
    private final String texture;   // imagine a 5MB image

    TreeType(String species, String texture) {
        this.species = species;
        this.texture = texture;
    }

    /** Extrinsic state (position) is PASSED IN, never stored here. */
    void draw(int x, int y) {
        System.out.println("draw " + species + " [" + texture + "] at (" + x + "," + y + ")");
    }
}

/** The FACTORY that dedupes flyweights. The cache is the whole point. */
class TreeTypeFactory {
    private static final Map<String, TreeType> CACHE = new HashMap<>();

    static TreeType get(String species, String texture) {
        return CACHE.computeIfAbsent(species + "|" + texture,
                k -> new TreeType(species, texture));
    }

    static int createdCount() { return CACHE.size(); }
}

/** The per-object part: tiny — two ints and a shared reference. */
class Tree {
    private final int x, y;              // EXTRINSIC: unique per tree
    private final TreeType type;         // shared flyweight

    Tree(int x, int y, TreeType type) { this.x = x; this.y = y; this.type = type; }

    void draw() { type.draw(x, y); }
}

class Forest {
    private final List<Tree> trees = new ArrayList<>();

    void plantTree(int x, int y, String species, String texture) {
        trees.add(new Tree(x, y, TreeTypeFactory.get(species, texture)));
    }

    void drawAll() { for (Tree t : trees) t.draw(); }
}
