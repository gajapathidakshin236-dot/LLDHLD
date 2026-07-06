package com.company.factorymethod;

/**
 * Demo. Trace (exercise B.12.1 in notes/02-patterns-creational.md):
 * Main -> Logistics.planDelivery() -> [subclass].createTransport() -> Transport.deliver()
 *
 * Exercise B.12.2: add Drone + AirLogistics WITHOUT editing any existing file.
 */
public class Main {
    public static void main(String[] args) {
        Logistics road = new RoadLogistics();
        Logistics sea  = new SeaLogistics();

        System.out.println("tracking: " + road.planDelivery());
        System.out.println("tracking: " + sea.planDelivery());

        // The client never mentions Truck or Ship. That's the decoupling win.
    }
}
