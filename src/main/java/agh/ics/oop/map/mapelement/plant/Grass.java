package agh.ics.oop.map.mapelement.plant;

import agh.ics.oop.Vector2d;
import agh.ics.oop.map.mapelement.IMapElement;

public class Grass implements IMapElement {
    private final Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

}
