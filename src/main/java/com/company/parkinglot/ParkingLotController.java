package com.company.parkinglot;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Coordinates entry and exit. Depends on the three strategies via the
 * constructor (dependency injection) — none of them are hardcoded, so all
 * are swappable and testable.
 *
 * Concurrency note: parkVehicle is synchronized so the "find spot + occupy"
 * sequence is atomic. In a high-throughput system you'd lock per-spot rather
 * than the whole controller, so cars entering different spots don't block
 * each other — but a single lock is correct and simple for this scope.
 */
public class ParkingLotController {
    private final ParkingLot lot;
    private final SpotAllocationStrategy allocationStrategy;
    private final FeeStrategy feeStrategy;

    public ParkingLotController(ParkingLot lot,
                                SpotAllocationStrategy allocationStrategy,
                                FeeStrategy feeStrategy) {
        this.lot = lot;
        this.allocationStrategy = allocationStrategy;
        this.feeStrategy = feeStrategy;
    }

    /** Entry: find a spot, occupy it, issue a ticket. */
    public synchronized Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = allocationStrategy.findSpot(lot.getFloors(), vehicle);
        if (spot == null) {
            System.out.println("LOT FULL — no spot for " + vehicle);
            return null;
        }
        spot.occupy(vehicle);
        Ticket ticket = new Ticket(
                UUID.randomUUID().toString().substring(0, 8),
                vehicle, spot, LocalDateTime.now());
        System.out.println("Parked " + vehicle + " at " + spot
                + " -> ticket " + ticket.getTicketId());
        return ticket;
    }

    /** Exit: compute fee (pure), free the spot, collect payment (separate). */
    public synchronized double exitVehicle(Ticket ticket, PaymentStrategy payment, LocalDateTime exitTime) {
        double fee = feeStrategy.calculateFee(ticket, exitTime);   // step 1: compute
        System.out.println("Exit " + ticket.getVehicle()
                + " | fee = " + fee);
        payment.pay(fee);                                          // step 2: collect
        ticket.getSpot().vacate();                                 // step 3: free spot
        return fee;
    }
}
