package agh.ics.oop;

import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.IWorldMap;
import agh.ics.oop.map.mapelement.animal.Animal;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DataGatherer {
    private final IWorldMap map;
    private final Graveyard graveyard;
    private Animal currentlyWatchedAnimal;

    private int aliveAnimalNumber;
    private int grassNumber;
    private int mostPopularGenome;
    private int sumOfAnimalEnergy;
    private int freeFieldsNumber;
    private double averageLifespan;
    private double averageAnimalEnergy;
    private final int mapWidth;
    private final int mapHeight;
    private String csvStatsFilePath;


    public DataGatherer(IWorldMap map, Graveyard graveyard, String csvPath) {
        this.map = map;
        this.graveyard = graveyard;
        this.mapHeight = (int) SimulationEngine.simulationParams.get(ParameterType.MAP_HEIGHT);
        this.mapWidth = (int) SimulationEngine.simulationParams.get(ParameterType.MAP_WIDTH);
        setCsvStatsFilePath(csvPath);
    }

    public void gatherData() {
        aliveAnimalNumber = map.animalNumber();
        grassNumber = map.getGrassNumber();
        freeFieldsNumber = mapHeight * mapWidth - map.getNumberOfTakenFields();
        sumOfAnimalEnergy = map.getTotalEnergy();
        averageLifespan = graveyard.getAverageLifespan();
        averageAnimalEnergy = (double) sumOfAnimalEnergy / (double) aliveAnimalNumber;
        saveToCSV();
    }

    public void saveToCSV() {
        File currentCSVFile = new File(csvStatsFilePath);
        try {
            FileWriter writer = new FileWriter(currentCSVFile, true);

            String toWrite = aliveAnimalNumber + "," + grassNumber + "," + freeFieldsNumber + "," + sumOfAnimalEnergy + "," + averageLifespan +
                    "," + averageAnimalEnergy;

            writer.write(toWrite + "\n");
            writer.close();

        } catch (IOException e) {
            System.out.println("IOException in DataGatherer: " + e.getMessage());
        }
    }


    public Animal getCurrentlyWatchedAnimal() {
        return currentlyWatchedAnimal;
    }

    public int getAliveAnimalNumber() {
        return aliveAnimalNumber;
    }

    public int getGrassNumber() {
        return grassNumber;
    }

    public int getFreeFieldsNumber() {
        return freeFieldsNumber;
    }

    public int getMostPopularGenome() {
        return mostPopularGenome;
    }

    public double getAverageLifespan() {
        return averageLifespan;
    }

    public double getAverageAnimalEnergy() {
        return averageAnimalEnergy;
    }

    public void setCsvStatsFilePath(String path) {
        String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        if (Objects.equals(path, "") || path == null) {
            this.csvStatsFilePath = ".\\CSVFiles\\statistics_" + dateString + ".csv";
        } else {
            this.csvStatsFilePath = path + "\\statistics_" + dateString + ".csv";
        }

        File csvFile = new File(csvStatsFilePath);

        try {
            csvFile.createNewFile();
        } catch (IOException e) {
            System.out.println("IOException in DataGatherer: " + e.getMessage());
        }
    }
}
