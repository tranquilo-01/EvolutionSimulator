package agh.ics.oop.map.mapelement.animal.genome;

import agh.ics.oop.map.mapelement.animal.Animal;

public interface IGenome {

    /**
     * method used to generate a random genome when no parents
     *
     * @param length - length of genome to generate
     * @return int[] - randomly generated genome
     */
    int[] generate(int length);

    /**
     * method to be used in constructor when generating new animal from copulation
     *
     * @param parent1 - one of the parents
     * @param parent2 - one of the parents
     * @return int[] - randomly generated genome
     */
    int[] generate(Animal parent1, Animal parent2);

    /**
     * method used to mutate genome of child after copulation, changes random number of genes in given range
     */
    void mutate();

    /**
     * mutates specified gene and returns it
     */
    int mutateGene(int gene);

    /**
     * @return - returns a copy of genome array
     */
    int[] getGenome();

    int[] getGenomePart(boolean left, int length);

    /**
     * changes value of next gene field according to AnimalBehaviorVariant
     */
    void nextGene();

    /**
     * @return - returns gene currently in use
     */
    int getCurrentGene();


}
