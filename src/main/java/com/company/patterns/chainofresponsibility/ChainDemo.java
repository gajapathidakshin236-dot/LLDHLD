package com.company.patterns.chainofresponsibility;

/**
 * CHAIN OF RESPONSIBILITY PATTERN
 *
 * Intent: Pass a request along a chain of handlers. Each handler either
 *         processes it and/or forwards it to the next. Decouples the sender
 *         from the receiver. Like support escalation: L1 -> L2 -> L3.
 *
 * Two common flavors:
 *   - Stop on first handler that can deal with it (e.g., ATM dispenser)
 *   - Let every applicable handler run (the logger below — console+file+email
 *     can all handle the same message based on level)
 *
 * Real-world: Servlet filter chains, Spring Security filters, Express/Node
 * middleware, log4j logger hierarchy.
 *
 * Run: javac ChainDemo.java && java ChainDemo
 */
public class ChainDemo {
    public static void main(String[] args) {
        // Build the chain: console handles INFO+, file handles DEBUG+, email handles ERROR+
        Logger chain = new ConsoleLogger(Logger.INFO);
        chain.setNext(new FileLogger(Logger.DEBUG))
             .setNext(new EmailLogger(Logger.ERROR));

        System.out.println("-- log INFO --");
        chain.log(Logger.INFO, "User signed in");

        System.out.println("\n-- log DEBUG --");
        chain.log(Logger.DEBUG, "Cache miss for key=42");

        System.out.println("\n-- log ERROR --");
        chain.log(Logger.ERROR, "Database connection lost!");
    }
}

abstract class Logger {
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int ERROR = 3;

    protected int level;       // minimum level this handler cares about
    protected Logger next;     // next handler in the chain

    // setNext returns the argument so you can chain .setNext().setNext()...
    public Logger setNext(Logger next) {
        this.next = next;
        return next;
    }

    public void log(int messageLevel, String message) {
        if (messageLevel >= this.level) {
            write(message);
        }
        if (next != null) {
            next.log(messageLevel, message);   // forward down the chain
        }
    }

    protected abstract void write(String message);
}

class ConsoleLogger extends Logger {
    public ConsoleLogger(int level) { this.level = level; }
    protected void write(String m) { System.out.println("CONSOLE: " + m); }
}

class FileLogger extends Logger {
    public FileLogger(int level) { this.level = level; }
    protected void write(String m) { System.out.println("FILE:    " + m); }
}

class EmailLogger extends Logger {
    public EmailLogger(int level) { this.level = level; }
    protected void write(String m) { System.out.println("EMAIL:   " + m); }
}

