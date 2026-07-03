package LLD.ELEVATOR;

/**
 * A hallway button press. Carries both the floor AND the direction the rider
 * wants to travel — this lets the dispatcher pick an elevator moving that way.
 *
 * Example: user on floor 5 presses "UP" → HallCall(5, Direction.UP).
 */
public class HallCall extends Request {

    private final Direction direction;

    public HallCall(final int floor, final Direction direction) {
        super(floor);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
