package agh.ics.oop;

import agh.ics.oop.map.mapelement.animal.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graveyard {
    protected Map<Vector2d, ArrayList<Animal>> animals;
    private int deadAnimalsNumber = 0;
    private int sumOfLifeLengths = 0;

    public Graveyard() {
        this.animals = new HashMap<>();
    }

    public void addAnimal(Animal animal, Vector2d position) {
        if (!animals.containsKey(position)) {
            animals.put(position, new ArrayList<>());
        }
        animals.get(position).add(animal);
        deadAnimalsNumber += 1;
        this.sumOfLifeLengths += animal.getAge();
    }

    public double getAverageLifespan() {
        return (double) sumOfLifeLengths / (double) deadAnimalsNumber;
    }

}
