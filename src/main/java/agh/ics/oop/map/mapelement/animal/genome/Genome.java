package agh.ics.oop.map.mapelement.animal.genome;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.constants.AnimalBehaviorVariant;
import agh.ics.oop.constants.AnimalMutationVariant;
import agh.ics.oop.constants.ParameterType;
import agh.ics.oop.map.mapelement.animal.Animal;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Genome implements IGenome {
    private final int[] genome;
    private final AnimalBehaviorVariant behaviorVariant = (AnimalBehaviorVariant) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_BEHAVIOR_VARIANT);
    private final AnimalMutationVariant mutationVariant = (AnimalMutationVariant) SimulationEngine.simulationParams.get(ParameterType.GENOME_MUTATION_VARIANT);
    private int currentGeneIndex;
    private final int animalMaxMutationNumber = (int) SimulationEngine.simulationParams.get(ParameterType.GENOME_MAX_MUTATION_NUMBER);
    private final int animalMinMutationNumber = (int) SimulationEngine.simulationParams.get(ParameterType.GENOME_MIN_MUTATION_NUMBER);
    private final int length;
    private final Random rand = new Random();


    public Genome() {
        this.length = (int) SimulationEngine.simulationParams.get(ParameterType.ANIMAL_GENOME_LENGTH);
        this.genome = generate(length);
        this.currentGeneIndex = rand.nextInt(length);
    }

    public Genome(Animal parent1, Animal parent2) {
        this.genome = generate(parent1, parent2);
        this.length = parent1.getGenome().getGenome().length;
        this.currentGeneIndex = rand.nextInt(length);
    }

    @Override
    public int[] generate(int length) {
        int[] result = new int[length];
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            result[i] = rand.nextInt(8);
        }
        return result;
    }

    @Override
    public int[] generate(Animal parent1, Animal parent2) {
        int len = parent1.getGenome().getGenome().length;
        int idx = (int) (((double) parent1.getEnergy() / ((double) parent1.getEnergy() + (double) parent2.getEnergy())) * (double) len);
        boolean left = rand.nextBoolean();
        int[] result = new int[len];
        int[] part1 = parent1.getGenome().getGenomePart(left, idx);
        int[] part2 = parent2.getGenome().getGenomePart(!left, idx);

        if (left) {
            System.arraycopy(part1, 0, result, 0, idx);
            System.arraycopy(part2, 0, result, idx, len - idx);
        } else {
            System.arraycopy(part2, 0, result, 0, idx);
            System.arraycopy(part1, 0, result, idx, len - idx);
        }
        return result;

    }

    @Override
    public void mutate() {
        Random random = new Random();
        int mutationsNumber = random.nextInt(animalMaxMutationNumber - animalMinMutationNumber + 1) + animalMinMutationNumber;
        for (int i = 0; i < mutationsNumber; i++) {
            int idx = random.nextInt(genome.length);
            genome[idx] = mutateGene(genome[idx]);
        }

    }


    public int mutateGene(int gene) {
        Random random = new Random();
        switch (mutationVariant) {
            case SLIGHT_CORRECTION -> {
                boolean up = random.nextBoolean();
                if (up) {
                    return Math.abs(gene + 1) % 8;
                } else {
                    return Math.abs(gene - 1) % 8;
                }
            }
            case FULL_RANDOMNESS -> {
                return random.nextInt(8);
            }
        }
        //dziwne, bo powinno nie byc problemu z niezwracaniem
        return gene;
    }

    @Override
    public int[] getGenome() {
        return Arrays.copyOfRange(this.genome, 0, genome.length);
    }

    @Override
    public int[] getGenomePart(boolean left, int index) {
        if (left) {
            return Arrays.copyOfRange(this.genome, 0, index);
        } else {
            return Arrays.copyOfRange(this.genome, index, this.genome.length);
        }
    }

    @Override
    public void nextGene() {
        int len = this.genome.length;
        switch (behaviorVariant) {
            case FULL_PREDESTINATION -> this.currentGeneIndex = (this.currentGeneIndex + 1) % len;
            case A_LITTLE_MADNESS -> {
                if (rand.nextInt(10) < 8) {
                    this.currentGeneIndex = (this.currentGeneIndex + 1) % len;
                } else {
                    this.currentGeneIndex = rand.nextInt(len);
                }
            }
        }
    }

    @Override
    public int getCurrentGene() {
        return this.genome[currentGeneIndex];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome1 = (Genome) o;
        return currentGeneIndex == genome1.currentGeneIndex && Arrays.equals(genome, genome1.genome) && behaviorVariant == genome1.behaviorVariant && mutationVariant == genome1.mutationVariant;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(behaviorVariant, mutationVariant, currentGeneIndex);
        result = 31 * result + Arrays.hashCode(genome);
        return result;
    }
}
