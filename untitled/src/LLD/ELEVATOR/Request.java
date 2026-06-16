package LLD.ELEVATOR;

public abstract class Request {
    protected final int floor;
    public Request(int floor) { this.floor = floor; }
    public int getFloor() { return floor; }
}
