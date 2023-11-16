package agh.ics.oop.constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ConfigLoader {
    static String configFilePath;
    private static final String defaultConfigFilePath = "./ConfigFiles/config1.txt";
    private static final ParameterType[] ConfigFileStructure = {
            ParameterType.ANIMAL_INITIAL_ENERGY,
            ParameterType.ANIMAL_CONSUMPTION_ENERGY,
            ParameterType.ANIMAL_MIN_COPULATION_ENERGY,
            ParameterType.ANIMAL_ENERGY_USED_ON_COPULATION,
            ParameterType.ANIMAL_ENERGY_USED_ON_MOVE,
            ParameterType.ANIMAL_GENOME_LENGTH,
            ParameterType.ANIMAL_BEHAVIOR_VARIANT,
            ParameterType.GENOME_MIN_MUTATION_NUMBER,
            ParameterType.GENOME_MAX_MUTATION_NUMBER,
            ParameterType.GENOME_MUTATION_VARIANT,
            ParameterType.MAP_HEIGHT,
            ParameterType.MAP_WIDTH,
            ParameterType.MAP_VARIANT,
            ParameterType.MAP_JUNGLE_WIDTH,
            ParameterType.MAP_INITIAL_ANIMAL_NUMBER,
            ParameterType.MAP_INITIAL_GRASS_NUMBER,
            ParameterType.MAP_GRASS_GROWTH_VARIANT,
            ParameterType.MAP_DAILY_GRASS_GROWTH,
            ParameterType.SIMULATION_REFRESH_RATE
    };


    public ConfigLoader(String filePath) {
        configFilePath = filePath;
    }

    public static List<String> getLines() throws FileNotFoundException {
        System.out.println(configFilePath);
        //getting lines from config file
        if (configFilePath == null || configFilePath.isEmpty()) {
            configFilePath = defaultConfigFilePath;
        }
        List<String> lines = new ArrayList<>();
        File configFile = new File(configFilePath);
        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            lines.add(data);
        }
        scanner.close();
        return lines;
    }

    public static Map<ParameterType, Object> getParams() throws FileNotFoundException {
        List<String> lines = getLines();
        int len = lines.size();

        if (len < ConfigFileStructure.length) {
            throw new IllegalArgumentException("Config file too short");
        }
        if (len > ConfigFileStructure.length) {
            throw new IllegalArgumentException("Config file too long");
        }
        Map<ParameterType, Object> result = new HashMap<>();
        for (int i = 0; i < len; i++) {
            String[] row = lines.get(i).split(" ");
            ParameterType type = ConfigFileStructure[i];
            if (!(row.length == 2)) {
                throw new IllegalArgumentException("Invalid input for" + type);
            }
            if (!Objects.equals(row[0], ConfigFileStructure[i].toString())) {
                throw new IllegalArgumentException("Invalid input for" + type);
            }

            int value;
            try {
                value = Integer.parseInt(row[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid input for " + type);
            }
            Object parsedValue = type.parse(value);
            result.put(type, parsedValue);
        }
        return result;
    }
}
