package com.company.patterns.visitor;

import java.util.List;

/**
 * VISITOR — add NEW OPERATIONS to a stable class hierarchy without touching
 * the classes. The operation lives in one visitor class instead of being
 * smeared as one method across every element class.
 *
 * The trick is DOUBLE DISPATCH:
 *   element.accept(visitor) -> visitor.visit(THIS-concrete-type)
 * The first call picks the element's class (virtual dispatch); the second
 * picks the right overload because `this` inside Circle IS a Circle.
 *
 * Trade-off to say out loud:
 *   Easy to ADD OPERATIONS (new visitor class),
 *   Painful to ADD ELEMENT TYPES (every visitor gains a method).
 * So: stable hierarchy + growing operations = Visitor territory.
 * Growing hierarchy = avoid Visitor.
 */
public class VisitorDemo {
    public static void main(String[] args) {
        List<Shape> drawing = List.of(
                new Circle(5),
                new Rectangle(3, 4),
                new Circle(1));

        AreaVisitor area = new AreaVisitor();
        XmlExportVisitor xml = new XmlExportVisitor();

        for (Shape s : drawing) {
            s.accept(area);      // double dispatch happens here
            s.accept(xml);
        }
        System.out.printf("total area: %.2f%n", area.total());
        System.out.println(xml.output());
    }
}

interface Shape {
    void accept(ShapeVisitor v);
}

interface ShapeVisitor {
    void visit(Circle c);
    void visit(Rectangle r);
}

class Circle implements Shape {
    final double radius;

    Circle(double radius) { this.radius = radius; }

    @Override public void accept(ShapeVisitor v) { v.visit(this); }  // `this` is Circle
}

class Rectangle implements Shape {
    final double w, h;

    Rectangle(double w, double h) { this.w = w; this.h = h; }

    @Override public void accept(ShapeVisitor v) { v.visit(this); }
}

/** Operation #1 — lives in ONE class, not smeared across shapes. */
class AreaVisitor implements ShapeVisitor {
    private double total = 0;

    @Override public void visit(Circle c)    { total += Math.PI * c.radius * c.radius; }
    @Override public void visit(Rectangle r) { total += r.w * r.h; }

    double total() { return total; }
}

/** Operation #2 — added WITHOUT touching Circle/Rectangle. That's the win. */
class XmlExportVisitor implements ShapeVisitor {
    private final StringBuilder sb = new StringBuilder();

    @Override public void visit(Circle c)    { sb.append("<circle r='").append(c.radius).append("'/>\n"); }
    @Override public void visit(Rectangle r) { sb.append("<rect w='").append(r.w).append("' h='").append(r.h).append("'/>\n"); }

    String output() { return sb.toString(); }
}
