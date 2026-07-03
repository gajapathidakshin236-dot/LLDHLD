package LLD.ELEVATOR;

/**
 * Runtime state of an elevator. Independent from Direction because an elevator
 * may be MOVING_UP with direction UP, but also OUT_OF_SERVICE / MAINTENANCE.
 */
public enum ElevatorState {
    IDLE,
    MOVING_UP,
    MOVING_DOWN,
    MAINTENANCE
}
