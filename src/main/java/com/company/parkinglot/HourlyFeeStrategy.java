package com.company.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Hourly pricing with per-vehicle-type rates. Rounds partial hours UP
 * (a 61-minute stay is billed as 2 hours), which is how real lots charge.
 */
public class HourlyFeeStrategy implements FeeStrategy {

    @Override
    public double calculateFee(Ticket ticket, LocalDateTime exitTime) {
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        long hours = (long) Math.ceil(minutes / 60.0);
        if (hours == 0) hours = 1;   // minimum 1 hour

        double rate = ratePerHour(ticket.getVehicle().getType());
        return hours * rate;
    }

    private double ratePerHour(VehicleType type) {
        switch (type) {
            case BIKE:  return 10;
            case CAR:   return 30;
            case TRUCK: return 50;
            default:    return 30;
        }
    }
}
