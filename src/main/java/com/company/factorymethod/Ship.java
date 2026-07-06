package com.company.factorymethod;

/** A CONCRETE PRODUCT. Only SeaLogistics knows this class exists. */
public class Ship implements Transport {
    @Override
    public String deliver() {
        System.out.println("Ship: delivering by SEA in a container.");
        return "SHP-" + System.nanoTime();
    }
}
