package com.company.factorymethod;

/** A CONCRETE PRODUCT. Only RoadLogistics knows this class exists. */
public class Truck implements Transport {
    @Override
    public String deliver() {
        System.out.println("Truck: delivering by ROAD in a box.");
        return "TRK-" + System.nanoTime();
    }
}
