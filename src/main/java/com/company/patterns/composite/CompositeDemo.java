package com.company.patterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE — treat a single object and a GROUP of objects uniformly through
 * one interface, forming a tree.
 *
 * Classic example: a file system. A File is a leaf; a Directory contains
 * FileSystemNodes — which may themselves be files OR directories. The client
 * calls node.size() and never cares which kind it's holding: recursion is
 * hidden inside the composite.
 */
public class CompositeDemo {
    public static void main(String[] args) {
        Directory root = new Directory("/");
        Directory home = new Directory("home");
        Directory pics = new Directory("pics");

        root.add(new File("boot.log", 120));
        root.add(home);
        home.add(new File("notes.md", 300));
        home.add(pics);
        pics.add(new File("cat.jpg", 2048));
        pics.add(new File("dog.jpg", 4096));

        root.print("");
        System.out.println("total size = " + root.size() + " bytes");
        // root.size() -> recurses the whole tree; client wrote ZERO recursion.
    }
}

/** The COMPONENT — the uniform interface for leaf and composite alike. */
interface FileSystemNode {
    String name();
    long size();
    void print(String indent);
}

/** LEAF — has no children; size is its own. */
class File implements FileSystemNode {
    private final String name;
    private final long bytes;

    File(String name, long bytes) { this.name = name; this.bytes = bytes; }

    @Override public String name() { return name; }
    @Override public long size()   { return bytes; }
    @Override public void print(String indent) {
        System.out.println(indent + "- " + name + " (" + bytes + "B)");
    }
}

/** COMPOSITE — holds children of the COMPONENT type (files OR directories). */
class Directory implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    Directory(String name) { this.name = name; }

    void add(FileSystemNode child) { children.add(child); }

    @Override public String name() { return name; }

    @Override public long size() {                 // the recursion lives HERE
        long total = 0;
        for (FileSystemNode child : children) total += child.size();
        return total;
    }

    @Override public void print(String indent) {
        System.out.println(indent + "+ " + name + "/");
        for (FileSystemNode child : children) child.print(indent + "  ");
    }
}
