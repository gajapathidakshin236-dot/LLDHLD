package LLD.ELEVATOR;

/**
 * A button press from inside the cabin. Only carries the desired destination
 * floor — direction is implied by the elevator's current motion.
 *
 * Example: user inside the cabin presses "10" → CarCall(10).
 */
public class CarCall extends Request {

    public CarCall(final int floor) {
        super(floor);
    }
}
