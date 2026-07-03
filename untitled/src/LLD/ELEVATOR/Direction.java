package LLD.ELEVATOR;

/**
 * Direction an elevator (or a hall call) is currently oriented towards.
 * IDLE means "no direction yet" — typically when the elevator has no pending targets.
 */
public enum Direction {
    UP,
    DOWN,
    IDLE
}
