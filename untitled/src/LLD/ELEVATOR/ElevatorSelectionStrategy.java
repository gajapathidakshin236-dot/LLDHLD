package LLD.ELEVATOR;

import java.util.List;

/**
 * STRATEGY pattern.
 *
 * Given a fleet of elevators + a Request, choose which elevator should serve it.
 * Swappable at runtime so different buildings can use different policies
 * (nearest-idle, directional-scan, load-balanced, energy-optimized, etc.).
 */
public interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
