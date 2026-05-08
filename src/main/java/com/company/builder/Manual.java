package com.company.builder;

public final class Manual {
    private final CarType type;
    private final int seats;
    private final Engine engine;
    private final Transmission transmission;
    private final TripComputer tripComputer;
    private final GPSNavigator gpsNavigator;

    public Manual(
            CarType type,
            int seats,
            Engine engine,
            Transmission transmission,
            TripComputer tripComputer,
            GPSNavigator gpsNavigator
    ) {
        this.type = type;
        this.seats = seats;
        this.engine = engine;
        this.transmission = transmission;
        this.tripComputer = tripComputer;
        this.gpsNavigator = gpsNavigator;
    }

    public String print() {
        return """
                Car manual
                - Type: %s
                - Seats: %d
                - Engine: %.1fL (mileage: %d)
                - Transmission: %s
                - Trip computer: %s
                - GPS: %s
                """.formatted(
                type,
                seats,
                engine == null ? 0.0 : engine.volume(),
                engine == null ? 0 : engine.mileage(),
                transmission,
                tripComputer == null ? "no" : "yes",
                gpsNavigator == null ? "no" : gpsNavigator.route()
        );
    }
}

