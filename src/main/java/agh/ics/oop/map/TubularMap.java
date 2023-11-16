package agh.ics.oop.map;

import agh.ics.oop.Vector2d;
import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.mapelement.IMapElement;
import agh.ics.oop.map.mapelement.animal.Animal;

import java.util.Map;


public class TubularMap extends AbstractWorldMap {


    public TubularMap(Map<ParameterType, Object> simulationParams) {
        super(simulationParams);
        growGrass((int) simulationParams.get(ParameterType.MAP_INITIAL_GRASS_NUMBER));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(new Vector2d(mapWidth - 1, mapHeight - 1)) && position.follows(new Vector2d(0, 0));
    }

    @Override
    public IMapElement objectAt(Vector2d position) {
        IMapElement to_ret = super.objectAt(position);
        if (to_ret != null) {
            return to_ret;
        }
        return grasses.get(position);
    }

    @Override
    public Vector2d calculateMovePosition(Animal animal, Vector2d potentialMovePosition) {
        if (canMoveTo(potentialMovePosition)) {
            return potentialMovePosition;
        } else {
            if (potentialMovePosition.x >= mapWidth) {
                return this.calculateMovePosition(animal, new Vector2d(0, potentialMovePosition.y));
            } else if (potentialMovePosition.x < 0) {
                return this.calculateMovePosition(animal, new Vector2d(mapWidth - 1, potentialMovePosition.y));
            } else if (potentialMovePosition.y >= mapHeight || potentialMovePosition.y < 0) {
                animal.turnAround();
                return animal.getPosition();
            }
        }
        System.out.println("calculating position error");
        return new Vector2d(0, 0);
    }

    private int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public Vector2d leftBottomCorner() {
        return new Vector2d(0, 0);
    }

    public Vector2d rightTopCorner() {
        return new Vector2d(mapWidth, mapHeight);
    }

}
