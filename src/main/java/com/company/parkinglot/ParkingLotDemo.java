package com.company.parkinglot;

import java.time.LocalDateTime;

/**
 * Demo of the parking lot.
 *
 * Run: javac *.java && java ParkingLotDemo
 */
public class ParkingLotDemo {
    public static void main(String[] args) {
        // Build a lot: 2 floors, each with 2 bike + 2 compact + 1 large spot.
        ParkingLot lot = ParkingLot.build(2, 2, 2, 1);

        // Wire strategies via DI — all swappable.
        ParkingLotController controller = new ParkingLotController(
                lot,
                new NearestFirstAllocationStrategy(),
                new HourlyFeeStrategy());

        System.out.println("=== Parking vehicles ===");
        Ticket t1 = controller.parkVehicle(new Vehicle("TN01-AB-1234", VehicleType.CAR));
        Ticket t2 = controller.parkVehicle(new Vehicle("TN01-XY-9999", VehicleType.BIKE));
        Ticket t3 = controller.parkVehicle(new Vehicle("TN01-TR-0001", VehicleType.TRUCK));

        System.out.println("\n=== Exiting vehicles ===");
        // Simulate the car staying 2.5 hours -> billed 3 hours * 30 = 90
        LocalDateTime exit1 = t1.getEntryTime().plusMinutes(150);
        controller.exitVehicle(t1, new UPIPayment(), exit1);

        // Bike staying 30 min -> billed 1 hour * 10 = 10
        LocalDateTime exit2 = t2.getEntryTime().plusMinutes(30);
        controller.exitVehicle(t2, new CashPayment(), exit2);

        // Truck staying 4 hours -> billed 4 * 50 = 200
        LocalDateTime exit3 = t3.getEntryTime().plusMinutes(240);
        controller.exitVehicle(t3, new CardPayment(), exit3);

        System.out.println("\n=== Filling the lot to show LOT FULL ===");
        // Only 1 large spot existed; the truck freed it, but let's fill all
        // large spots with trucks to trigger a rejection.
        controller.parkVehicle(new Vehicle("T1", VehicleType.TRUCK));  // floor0 large
        controller.parkVehicle(new Vehicle("T2", VehicleType.TRUCK));  // floor1 large
        controller.parkVehicle(new Vehicle("T3", VehicleType.TRUCK));  // none left -> LOT FULL
    }
}
