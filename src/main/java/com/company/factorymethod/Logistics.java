package com.company.factorymethod;

/**
 * The CREATOR (GoF role).
 *
 * Declares the FACTORY METHOD {@link #createTransport()} and owns the business
 * logic that uses the product. Note the method is `protected abstract`:
 * it is an INTERNAL extension point, not public API. Clients call
 * {@link #planDelivery()}; they never call createTransport() directly.
 *
 * Adding a new transport type = ONE new ConcreteCreator subclass.
 * This class is never edited again (Open/Closed Principle).
 */
public abstract class Logistics {

    /** The factory method. Subclasses decide which concrete Transport to build. */
    protected abstract Transport createTransport();

    /** The business logic. Works purely against the Transport abstraction. */
    public String planDelivery() {
        Transport t = createTransport();   // subclass decides the concrete type
        return t.deliver();
    }
}
