package agh.ics.oop.map;

import agh.ics.oop.Vector2d;
import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.mapelement.animal.Animal;

import java.util.Map;

public class RectangularMap extends AbstractWorldMap {
    final int width;
    final int height;
    private final Vector2d leftBottomCorner;
    private final Vector2d rightTopCorner;


    public RectangularMap(Map<ParameterType, Object> simulationParams) {
        super(simulationParams);
        this.width = (int) simulationParams.get(ParameterType.MAP_WIDTH);
        this.height = (int) simulationParams.get(ParameterType.MAP_HEIGHT);
        leftBottomCorner = new Vector2d(0, 0);
        rightTopCorner = new Vector2d(width-1, height-1);
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(rightTopCorner) && position.follows(leftBottomCorner);
    }

    @Override
    public Vector2d calculateMovePosition(Animal animal, Vector2d potentialMovePosition) {
        if (canMoveTo(potentialMovePosition)) {
            return potentialMovePosition;
        } else {
            return animal.getPosition();
        }
    }


    public Vector2d getLeftBottomCorner() {
        return leftBottomCorner;
    }

    public Vector2d getRightTopCorner() {
        return rightTopCorner;
    }
}
