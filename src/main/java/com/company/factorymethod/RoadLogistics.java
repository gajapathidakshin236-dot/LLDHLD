package com.company.factorymethod;

/** CONCRETE CREATOR: commits to Truck. The only place `new Truck()` appears. */
public class RoadLogistics extends Logistics {
    @Override
    protected Transport createTransport() {
        return new Truck();
    }
}
