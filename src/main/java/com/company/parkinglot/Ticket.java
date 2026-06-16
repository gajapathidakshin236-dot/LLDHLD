package com.company.parkinglot;

import java.time.LocalDateTime;

/**
 * Issued at entry, surrendered at exit. Records which spot, which vehicle,
 * and when the vehicle entered (used to compute the fee).
 */
public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
    }

    public String getTicketId()        { return ticketId; }
    public Vehicle getVehicle()        { return vehicle; }
    public ParkingSpot getSpot()       { return spot; }
    public LocalDateTime getEntryTime() { return entryTime; }

    @Override
    public String toString() {
        return "Ticket[" + ticketId + ", " + vehicle + ", " + spot
                + ", in=" + entryTime + "]";
    }
}
