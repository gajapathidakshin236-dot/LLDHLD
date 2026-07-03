package LLD.ELEVATOR;

/**
 * Base type for any request the elevator must serve.
 *
 * Two concrete subtypes exist:
 *   HallCall — pressed from a floor's hallway (has a requested direction)
 *   CarCall  — pressed from inside the cabin (only a target floor)
 */
public abstract class Request {

    protected final int floor;

    protected Request(final int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}
