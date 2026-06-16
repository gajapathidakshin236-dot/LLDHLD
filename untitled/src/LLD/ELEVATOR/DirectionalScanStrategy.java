package LLD.ELEVATOR;

import java.util.List;

public class DirectionalScanStrategy  implements ElevatorSelectionStrategy {

    @Override
    public Elevator selectElevator(List<Elevator> elevators, Request request) {

        Elevator best = null;

        int bestDistance = Integer.MAX_VALUE;

        int requestFloor = request.getFloor();

        Direction requestDirection = null;

        if (request instanceof HallCall) {
            requestDirection = ((HallCall) request).getDirection();
        }

        for(Elevator e: elevators) {
            if(e.getState()==ElevatorState.MAINTENANCE) continue;
            int distance = Math.abs(e.getCurrentFloor() - requestFloor);

            boolean isIdle = e.getState()==ElevatorState.IDLE;

            boolean sameDirectionAndOnTheWay = requestDirection!=null && requestDirection==e.getDirection() &&
                    isOnTheWay(e,requestFloor,requestDirection);

            if(isIdle || sameDirectionAndOnTheWay) {
                if(distance < bestDistance) {
                    bestDistance=distance;
                    best=e;
                }
            }
        }

        if (best == null) {
            for (Elevator e : elevators) {
                if (e.getState() == ElevatorState.MAINTENANCE) continue;
                int distance = Math.abs(e.getCurrentFloor() - requestFloor);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = e;
                }
            }
        }
        return best;
    }

    private boolean isOnTheWay(Elevator e, int requestFloor, Direction requestDir) {
        if (requestDir == Direction.UP) {
            return e.getCurrentFloor() <= requestFloor;
        } else {
            return e.getCurrentFloor() >= requestFloor;
        }
    }
}
