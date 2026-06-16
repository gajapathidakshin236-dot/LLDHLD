package com.company.parkinglot;

/**
 * A vehicle wanting to park. Immutable — a vehicle's plate and type
 * don't change. We use a VehicleType enum rather than subclasses because
 * the types differ only by label/size, not behavior.
 */
public class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() { return licensePlate; }
    public VehicleType getType()    { return type; }

    @Override
    public String toString() {
        return type + "(" + licensePlate + ")";
    }
}
