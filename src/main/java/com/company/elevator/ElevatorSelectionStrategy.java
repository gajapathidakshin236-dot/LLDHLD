package com.company.elevator;

import java.util.List;

/**
 * Strategy contract for choosing WHICH elevator should serve a request.
 *
 * Why an interface (not abstract class)?
 *   - Strategy is pure behavior with no shared state
 *   - Supports Dependency Inversion: controller depends on this contract,
 *     not on a concrete implementation
 *   - To add a new dispatch algorithm (e.g., destination dispatch,
 *     least-busy, energy-optimal), implement this interface — no existing
 *     code changes. Open/Closed Principle in action.
 */
public interface ElevatorSelectionStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
