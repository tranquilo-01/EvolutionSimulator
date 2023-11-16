package agh.ics.oop.map;

import agh.ics.oop.IPositionChangeObserver;
import agh.ics.oop.Vector2d;
import agh.ics.oop.constants.GrassGrowthVariant;
import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.mapelement.IMapElement;
import agh.ics.oop.map.mapelement.animal.Animal;
import agh.ics.oop.map.mapelement.plant.Grass;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Vector2d, ArrayList<Animal>> animals;
    protected Map<Vector2d, Grass> grasses;
    protected final int mapWidth;
    protected final int mapHeight;
    protected final GrassGrowthVariant grassGrowthVariant;
    protected final int jungleWidth;
    protected final int animalMinCopulationEnergy;
    private final Map<ParameterType, Object> simulationParams;
    private final Random random = new Random();



    public AbstractWorldMap(Map<ParameterType, Object> simulationParams) {
        this.simulationParams = simulationParams;
        animals = new HashMap<>();
        grasses = new HashMap<>();
        this.mapWidth = (int) simulationParams.get(ParameterType.MAP_WIDTH);
        this.mapHeight = (int) simulationParams.get(ParameterType.MAP_HEIGHT);
        this.grassGrowthVariant = (GrassGrowthVariant) simulationParams.get(ParameterType.MAP_GRASS_GROWTH_VARIANT);
        this.jungleWidth = (int) simulationParams.get(ParameterType.MAP_JUNGLE_WIDTH);
        this.animalMinCopulationEnergy = (int) simulationParams.get(ParameterType.ANIMAL_MIN_COPULATION_ENERGY);
    }

    @Override
    public boolean place(Animal animal) {
        if (animal.getPosition() != null) {
            addAnimal(animal);
            return true;
        } else {
            throw new IllegalArgumentException("Position of animal to place must not be null");
        }
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public IMapElement objectAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return firstAnimal(animals.get(position));
        }
        return null;
    }

    @Override
    public void addAnimal(Animal animal) {
        if (!animals.containsKey(animal.getPosition())) {
            animals.put(animal.getPosition(), new ArrayList<>() {
            });
        }
        animals.get(animal.getPosition()).add(animal);

    }

    @Override
    public void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        (animals.get(position)).remove(animal);
        if ((animals.get(position)).isEmpty()) {
            animals.remove(position);
        }
    }

    @Override
    public int animalNumber() {
        int size = 0;
        for (Vector2d key : animals.keySet()) {
            size += animals.get(key).size();
        }
        return size;
    }

    @Override
    public void changePosition(Animal animal) {
        removeAnimal(animal);
        animal.applyDailyChanges();
        addAnimal(animal);
    }

    public void consumption() {
        for (Vector2d position : animals.keySet()) {
            Grass grass = grasses.get(position);
            if (grass != null) {
                Animal animal = firstAnimal(animals.get(position));
                animal.eat();
                grasses.remove(position);
            }
        }
    }

    public void growGrass(int number) {
        switch (grassGrowthVariant) {
            case FORESTED_EQUATOR -> {
                int x1 = (mapHeight - jungleWidth) / 2;
                int x2 = (mapHeight + jungleWidth) / 2;
                int grassesPlaced = 0;
                while (grassesPlaced < number) {
                    int area = random.nextInt(10);
                    if (area < 2) {
                        int x = random.nextInt(0, mapWidth);
                        int y = random.nextInt(0, x1);
                        Vector2d potentialPosition = new Vector2d(x, y);
                        if (!((objectAt(potentialPosition) instanceof Grass))) {
                            Grass toAdd = new Grass(potentialPosition);
                            grasses.put(potentialPosition, toAdd);
                            grassesPlaced++;
                        }
                    } else if (area < 8) {
                        int x = random.nextInt(0, mapWidth);
                        int y = random.nextInt(x1, x2);
                        Vector2d potentialPosition = new Vector2d(x, y);
                        if (!((objectAt(potentialPosition) instanceof Grass))) {
                            Grass toAdd = new Grass(potentialPosition);
                            grasses.put(potentialPosition, toAdd);
                            grassesPlaced++;
                        }
                    } else {
                        int x = random.nextInt(0, mapWidth);
                        int y = random.nextInt(x2, mapHeight);
                        Vector2d potentialPosition = new Vector2d(x, y);
                        if (!((objectAt(potentialPosition) instanceof Grass))) {
                            Grass toAdd = new Grass(potentialPosition);
                            grasses.put(potentialPosition, toAdd);
                            grassesPlaced++;
                        }
                    }
                }
            }
            case TOXIC_CORPSES -> throw new UnsupportedOperationException("Not implemented yet"); //TODO
        }
    }

    public LinkedList<Animal> copulation() {
        LinkedList<Animal> result = new LinkedList<>();
        for (Vector2d key : animals.keySet()) {
            if (animals.get(key).size() > 1) {
                Animal firstAnimal = firstAnimal(animals.get(key));
                Animal secondAnimal = secondAnimal(animals.get(key));
                if (secondAnimal.getEnergy() >= animalMinCopulationEnergy) {
                    Animal child = Animal.copulate(firstAnimal, secondAnimal);
                    child.addPositionObserver(this);
                    result.add(child);
                    this.place(child);
                }
            }
        }
        return result;
    }

    public Animal firstAnimal(ArrayList<Animal> animalList) {
        Animal max = animalList.get(0);
        for (Animal animal : animalList) {
            if (animal.compareTo(max) > 0) {
                max = animal;
            }
        }
        return max;
    }

    public Animal secondAnimal(ArrayList<Animal> animalList) {
        Animal max = animalList.get(0);
        Animal second = animalList.get(1);
        if (second.compareTo(max) > 0) {
            Animal tmp = max;
            max = second;
            second = tmp;
        }
        for (Animal animal : animalList) {
            if (animal.compareTo(max) > 0) {
                max = animal;
                second = max;
            }
        }
        return second;
    }

    public int getGrassNumber() {
        return grasses.size();
    }

    public int getTotalEnergy() {
        int totalEnergy = 0;
        for (Vector2d key : animals.keySet()) {
            for (Animal animal : animals.get(key)) {
                totalEnergy += animal.getEnergy();
            }
        }
        return totalEnergy;
    }

    public int getNumberOfTakenFields() {
        return animals.size() + grasses.size(); // todo - wynik prawidłowy tylko pomiędzy jedzeniem a rośnięciem trawy
    }


}
