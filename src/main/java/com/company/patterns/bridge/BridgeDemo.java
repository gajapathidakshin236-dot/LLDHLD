package com.company.patterns.bridge;

/**
 * BRIDGE — split ONE class hierarchy that varies in TWO independent dimensions
 * into TWO hierarchies connected by composition.
 *
 * Without bridge: Circle/Square x Vector/Raster = 4 classes
 * (VectorCircle, RasterCircle, VectorSquare, RasterSquare). Add a Triangle and
 * an SVG renderer -> 3x3 = 9 classes. MULTIPLICATIVE explosion.
 *
 * With bridge: shapes (abstraction) HOLD a Renderer (implementation).
 * 3 shapes + 3 renderers = 6 classes, combined at runtime. ADDITIVE.
 *
 * Recognition rule: "class names that multiply two words" (VectorCircle,
 * WindowsButton...) -> consider Bridge.
 */
public class BridgeDemo {
    public static void main(String[] args) {
        Renderer vector = new VectorRenderer();
        Renderer raster = new RasterRenderer();

        // Any shape x any renderer, chosen at RUNTIME:
        new Circle(vector, 5).draw();
        new Circle(raster, 5).draw();
        new Square(vector, 3).draw();
        new Square(raster, 3).draw();
    }
}

/* --- Implementation hierarchy (the "how to draw" dimension) --- */

interface Renderer {
    void renderCircle(double radius);
    void renderSquare(double side);
}

class VectorRenderer implements Renderer {
    @Override public void renderCircle(double r) {
        System.out.println("vector: circle of radius " + r + " as path commands");
    }
    @Override public void renderSquare(double s) {
        System.out.println("vector: square of side " + s + " as path commands");
    }
}

class RasterRenderer implements Renderer {
    @Override public void renderCircle(double r) {
        System.out.println("raster: circle of radius " + r + " as pixels");
    }
    @Override public void renderSquare(double s) {
        System.out.println("raster: square of side " + s + " as pixels");
    }
}

/* --- Abstraction hierarchy (the "what shape" dimension) --- */

abstract class Shape {
    protected final Renderer renderer;   // THE BRIDGE (composition, injected)

    protected Shape(Renderer renderer) { this.renderer = renderer; }

    abstract void draw();
}

class Circle extends Shape {
    private final double radius;

    Circle(Renderer renderer, double radius) {
        super(renderer);
        this.radius = radius;
    }

    @Override void draw() { renderer.renderCircle(radius); }
}

class Square extends Shape {
    private final double side;

    Square(Renderer renderer, double side) {
        super(renderer);
        this.side = side;
    }

    @Override void draw() { renderer.renderSquare(side); }
}
