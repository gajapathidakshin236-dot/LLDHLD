package com.company.parkinglot;

/**
 * A single parking spot.
 *
 * Holds its id, its size (SpotType), and the vehicle currently in it
 * (null when empty). The occupy/vacate methods guard against misuse
 * (occupying a taken spot or vacating an empty one).
 *
 * Note: we deliberately did NOT use the State pattern here. A spot has only
 * two states (free / occupied) with no per-state behavior differences, so a
 * simple null-check is enough. State pattern would be over-engineering.
 */
public class ParkingSpot {
    private final int spotId;
    private final SpotType type;
    private Vehicle vehicle;   // null = available

    public ParkingSpot(int spotId, SpotType type) {
        this.spotId = spotId;
        this.type = type;
    }

    public boolean isAvailable() {
        return vehicle == null;
    }

    /** Can a vehicle of this type physically fit in this spot? */
    public boolean canFit(VehicleType vt) {
        switch (vt) {
            case BIKE:  return true;                              // bike fits anywhere
            case CAR:   return type == SpotType.COMPACT || type == SpotType.LARGE;
            case TRUCK: return type == SpotType.LARGE;            // truck needs large
            default:    return false;
        }
    }

    public void occupy(Vehicle v) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot " + spotId + " already occupied");
        }
        this.vehicle = v;
    }

    public void vacate() {
        if (isAvailable()) {
            throw new IllegalStateException("Spot " + spotId + " is already empty");
        }
        this.vehicle = null;
    }

    public int getSpotId()    { return spotId; }
    public SpotType getType() { return type; }
    public Vehicle getVehicle() { return vehicle; }

    @Override
    public String toString() {
        return "Spot#" + spotId + "(" + type + ", "
                + (isAvailable() ? "free" : "taken by " + vehicle) + ")";
    }
}
