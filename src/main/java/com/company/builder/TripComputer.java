package com.company.builder;

public final class TripComputer {
    private Car car;

    public void setCar(Car car) {
        this.car = car;
    }

    public String showFuelLevel() {
        if (car == null) return "TripComputer not calibrated";
        return "Fuel: " + car.getFuel() + "L";
    }
}

