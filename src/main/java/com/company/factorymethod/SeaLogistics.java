package com.company.factorymethod;

/** CONCRETE CREATOR: commits to Ship. The only place `new Ship()` appears. */
public class SeaLogistics extends Logistics {
    @Override
    protected Transport createTransport() {
        return new Ship();
    }
}
