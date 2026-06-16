package LLD.ELEVATOR;

import lombok.Getter;
import lombok.Setter;

import java.util.TreeSet;

@Getter
@Setter
public class Elevator {

    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;
    private final TreeSet<Integer> targetFloors;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.state = ElevatorState.IDLE;
        this.direction = Direction.IDLE;
        this.targetFloors = new TreeSet<>();
    }

    public void addRequest(Request request) {
        targetFloors.add(request.getFloor());

        if (state == ElevatorState.IDLE) {
            updateDirection();
            state = (direction == Direction.UP)
                    ? ElevatorState.MOVING_UP
                    : ElevatorState.MOVING_DOWN;
        }
    }

    public  void move() {
        if (targetFloors.isEmpty()) {
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
            return;
        }

        if(direction==Direction.UP) {
            currentFloor++;
        }  else if (direction == Direction.DOWN) {
            currentFloor--;
        }

        if (targetFloors.contains(currentFloor)) {
            targetFloors.remove(currentFloor);
            System.out.println("Elevator " + id + " stopped at floor " + currentFloor);
        }
        updateDirection();
    }

    public void updateDirection() {
        if (targetFloors.isEmpty()) {
            direction = Direction.IDLE;
            state = ElevatorState.IDLE;
            return;
        }

        Integer above = targetFloors.ceiling(currentFloor);
        Integer below = targetFloors.floor(currentFloor);

        if (direction == Direction.UP) {
            direction = (above != null) ? Direction.UP : Direction.DOWN;
        } else {
            direction = (below != null) ? Direction.DOWN : Direction.UP;
        }

        state = (direction == Direction.UP)
                ? ElevatorState.MOVING_UP
                : ElevatorState.MOVING_DOWN;
    }
    }

