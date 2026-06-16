package LLD.ELEVATOR;

public class HallCall extends Request {
    private final Direction direction;
    public HallCall(int floor, Direction direction) {
        super(floor);
        this.direction = direction;
    }
    public Direction getDirection() { return direction; }
}