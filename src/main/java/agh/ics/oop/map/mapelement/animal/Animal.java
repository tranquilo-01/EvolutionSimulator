package agh.ics.oop.map.mapelement.animal;

import agh.ics.oop.IPositionChangeObserver;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Vector2d;
import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.IWorldMap;
import agh.ics.oop.map.mapelement.IMapElement;
import agh.ics.oop.map.mapelement.animal.genome.Genome;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement {

    private Vector2d position;
    private int energy;
    private Genome genome;
    private int age;
    private int childrenNumber;
    private final ArrayList<IPositionChangeObserver> positionObservers;
    private final IWorldMap map;
    private MapDirection orientation;
    private final String name;


    public Animal(IWorldMap map, Vector2d initialPosition) {
        Random rand = new Random();
        this.map = map;
        this.positionObservers = new ArrayList<>();
        this.position = initialPosition;
        this.genome = new Genome();
        this.energy = (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_INITIAL_ENERGY);
        this.age = 0;
        this.orientation = MapDirection.values()[rand.nextInt(8)];
        this.name = generateName();
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Genome genome) {
        this(map, initialPosition);
        this.genome = genome;
        this.energy = 2 * (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_ENERGY_USED_ON_COPULATION);
    }


    public Vector2d getPosition() {
        return position;
    }

    public boolean isAt(Vector2d input) {
        return input.equals(this.position);
    }


    public void move() {
        changePosition();
    }

    public void applyDailyChanges() {
        this.genome.nextGene();
        int g = this.genome.getCurrentGene();
        this.orientation = this.orientation.turn(g);
        Vector2d potentialPosition = this.position.add(this.orientation.toVector2d());
        Vector2d calculatedPosition = this.map.calculateMovePosition(this, potentialPosition);
        this.setPosition(calculatedPosition);
        this.age++;
        this.energy -= (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_ENERGY_USED_ON_MOVE);
    }


    public void eat() {
        this.energy += (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_CONSUMPTION_ENERGY);
    }

    public boolean isDead() {
        return this.energy <= 0;
    }

    public void addPositionObserver(IPositionChangeObserver observer) {
        positionObservers.add(observer);
    }

    public void removePositionObserver(IPositionChangeObserver observer) {
        positionObservers.remove(observer);
    }

    private void changePosition() {
        for (IPositionChangeObserver observer : positionObservers) {
            observer.changePosition(this);
        }

    }

    public int compareTo(Animal otherAnimal) {
        int[] other = {otherAnimal.getEnergy(), otherAnimal.getAge(), otherAnimal.getChildrenNumber()};
        int[] thisAnimal = {this.energy, this.age, this.childrenNumber};
        for (int i = 0; i < 3; i++) {
            int result = Integer.compare(thisAnimal[i], other[i]);
            if (result != 0) {
                return result;
            }
        }
        if (this.hashCode() == otherAnimal.hashCode()) {
            return 0;
        }
        return this.name.compareTo(otherAnimal.name);
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public int getEnergy() {
        return energy;
    }

    public void turnAround() {
        this.orientation = this.orientation.turn(4);
    }

    public int getAge() {
        return age;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public Genome getGenome() {
        return genome;
    }

    public static Animal copulate(Animal animal1, Animal animal2){
        Genome genome = new Genome(animal1, animal2);
        genome.mutate();

        animal1.energy -= (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_ENERGY_USED_ON_COPULATION);
        animal2.energy -= (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_ENERGY_USED_ON_COPULATION);

        animal1.childrenNumber++;
        animal2.childrenNumber++;

        return new Animal(animal1.map, animal1.position, genome);
    }

    public String generateName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }


}
