package com.company.patterns.proxy;

import java.util.HashSet;
import java.util.Set;

/**
 * PROXY — an object that stands in front of another object with the SAME
 * interface, controlling access to it.
 *
 * Three classic flavors, two shown runnable here:
 *   1. VIRTUAL proxy  — lazy-load an expensive object (ImageProxy below)
 *   2. PROTECTION proxy — check permissions before delegating (SecureDocument)
 *   3. REMOTE proxy   — local stub for an object on another machine (gRPC stubs)
 *
 * Key rule: proxy IMPLEMENTS THE SAME INTERFACE as the real subject, so the
 * client cannot tell the difference. (Decorator has the same shape — the
 * difference is INTENT: decorator ADDS behavior, proxy CONTROLS ACCESS.)
 */
public class ProxyDemo {
    public static void main(String[] args) {
        System.out.println("--- Virtual proxy (lazy loading) ---");
        Image img = new ImageProxy("holiday.jpg");   // NOTHING loaded yet
        System.out.println("proxy created, disk not touched");
        img.display();                                // first use -> load + display
        img.display();                                // second use -> cached, no load

        System.out.println();
        System.out.println("--- Protection proxy ---");
        Document doc = new SecureDocument(new RealDocument("salary-report"), Set.of("alice"));
        doc.read("alice");                            // allowed
        try {
            doc.read("mallory");                      // denied
        } catch (SecurityException e) {
            System.out.println("denied: " + e.getMessage());
        }
    }
}

/* ===================== Virtual proxy ===================== */

interface Image {
    void display();
}

/** The REAL SUBJECT. Expensive to construct (imagine reading 50MB from disk). */
class RealImage implements Image {
    private final String filename;

    RealImage(String filename) {
        this.filename = filename;
        System.out.println("RealImage: loading " + filename + " from disk (EXPENSIVE)");
    }

    @Override
    public void display() {
        System.out.println("RealImage: displaying " + filename);
    }
}

/** The PROXY. Same interface; creates the real subject only on first use. */
class ImageProxy implements Image {
    private final String filename;
    private RealImage real;          // null until first display()

    ImageProxy(String filename) {
        this.filename = filename;    // CHEAP — no disk I/O here
    }

    @Override
    public void display() {
        if (real == null) {          // lazy init (synchronize if multi-threaded)
            real = new RealImage(filename);
        }
        real.display();
    }
}

/* ===================== Protection proxy ===================== */

interface Document {
    void read(String user);
}

class RealDocument implements Document {
    private final String name;

    RealDocument(String name) { this.name = name; }

    @Override
    public void read(String user) {
        System.out.println(user + " reads document '" + name + "'");
    }
}

/** Checks access BEFORE delegating. The real document stays permission-free. */
class SecureDocument implements Document {
    private final Document target;
    private final Set<String> allowedUsers;

    SecureDocument(Document target, Set<String> allowedUsers) {
        this.target = target;
        this.allowedUsers = new HashSet<>(allowedUsers);
    }

    @Override
    public void read(String user) {
        if (!allowedUsers.contains(user)) {
            throw new SecurityException(user + " may not read this document");
        }
        target.read(user);
    }
}
