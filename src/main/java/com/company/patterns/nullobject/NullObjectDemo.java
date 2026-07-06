package com.company.patterns.nullobject;

/**
 * NULL OBJECT — a do-nothing implementation of an interface, used instead of
 * `null`, so callers never null-check.
 *
 * The disease: `if (logger != null) logger.log(...)` repeated 40 times.
 * Forget one -> NullPointerException in production.
 *
 * The cure: a NoOpLogger that safely does nothing. Code paths stay identical
 * whether logging is on or off.
 *
 * JDK/real examples: Collections.emptyList() (a null object for lists),
 * Optional.empty(), Mockito default stubs, Spring's NoOpCacheManager.
 */
public class NullObjectDemo {
    public static void main(String[] args) {
        OrderService withLogs = new OrderService(new ConsoleLogger());
        OrderService silent   = new OrderService(Logger.NO_OP);   // never null!

        withLogs.placeOrder("book");
        silent.placeOrder("pen");      // no ifs, no NPE, just silence
    }
}

interface Logger {
    void log(String message);

    /** The NULL OBJECT: shared, stateless, does nothing. */
    Logger NO_OP = message -> { };
}

class ConsoleLogger implements Logger {
    @Override public void log(String message) { System.out.println("[log] " + message); }
}

class OrderService {
    private final Logger logger;      // NEVER null by convention

    OrderService(Logger logger) { this.logger = logger; }

    void placeOrder(String item) {
        logger.log("placing order for " + item);      // no null check. ever.
        System.out.println("ordered: " + item);
        logger.log("order placed");
    }
}
