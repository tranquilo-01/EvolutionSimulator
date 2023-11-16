package agh.ics.oop;

import agh.ics.oop.map.mapelement.animal.Animal;

public interface IPositionChangeObserver {
    void changePosition(Animal animal);
}
