package agh.ics.oop;

import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.gui.IRenderer;
import agh.ics.oop.map.IWorldMap;
import agh.ics.oop.map.mapelement.animal.Animal;

import java.util.LinkedList;
import java.util.Map;


public class SimulationEngine implements IEngine, Runnable {
    private final IWorldMap map;
    IRenderer renderer;
    private final LinkedList<Animal> animals = new LinkedList<>();
    private final Graveyard graveyard;
    public DataGatherer dataGatherer = null;
    public static Map<ParameterType, Object> simulationParams;
    volatile boolean running = true;


    public SimulationEngine(Map<ParameterType, Object> simulationParams, IWorldMap map, IRenderer renderer, String csvPath) {
        SimulationEngine.simulationParams = simulationParams;
        this.map = map;
        this.renderer = renderer;
        this.graveyard = new Graveyard();
        dataGatherer = new DataGatherer(this.map, graveyard, csvPath);

        int animalsPlaced = 0;
        while (animalsPlaced < (int) simulationParams.get(ParameterType.MAP_INITIAL_ANIMAL_NUMBER)) {
            int x = randInt(0, (int) simulationParams.get(ParameterType.MAP_WIDTH));
            int y = randInt(0, (int) simulationParams.get(ParameterType.MAP_HEIGHT));
            Vector2d potentialPosition = new Vector2d(x, y);
            Animal animal = new Animal(map, potentialPosition);
            animal.addPositionObserver((IPositionChangeObserver) map);
            animals.add(animal);
            map.place(animal);
            animalsPlaced++;
        }
    }


    public void run() {
        LinkedList<Animal> deadAnimals = new LinkedList<>();
        while (running) {
//            remove all dead animals
            for (Animal animal : animals) {
                if (animal.isDead()) {
                    deadAnimals.add(animal);
                }
            }
            moveAllToGraveyard(deadAnimals);

//            move all animals
            for (Animal animal : animals) {
                animal.move();
            }

//            copulation
            LinkedList<Animal> newAnimals = map.copulation();
            animals.addAll(newAnimals);

//            get all animals to consume
            map.consumption();

            map.growGrass((int) simulationParams.get(ParameterType.MAP_DAILY_GRASS_GROWTH));
            dataGatherer.gatherData();
            renderer.requestMapRendering();
            try {
                Thread.sleep((int) simulationParams.get(ParameterType.SIMULATION_REFRESH_RATE));
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }


    private int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void moveAllToGraveyard(LinkedList<Animal> deadAnimals) {
        animals.removeAll(deadAnimals);
        if (deadAnimals.size() > 0) {
            for (Animal animal : deadAnimals) {
                map.removeAnimal(animal);
                graveyard.addAnimal(animal, animal.getPosition());
            }
            deadAnimals.clear();
        }
    }


    public void stop(){
        this.running=false;
    }

}


