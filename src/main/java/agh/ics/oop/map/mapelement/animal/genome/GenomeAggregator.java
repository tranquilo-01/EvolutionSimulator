package agh.ics.oop.map.mapelement.animal.genome;

import javafx.collections.transformation.SortedList;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

public class GenomeAggregator { // todo - nieużywane
    private Map<int[], Integer> genomeMap;

    public GenomeAggregator(){
        this.genomeMap = new HashMap<>();
    }

    public void addGenome(IGenome iGenome){
        int[] genome = iGenome.getGenome();

        if(!genomeMap.containsKey(genome)){
            genomeMap.put(genome, 0);
        }else {
            int n = genomeMap.get(genome);
            genomeMap.remove(genome);
            genomeMap.put(genome, n+1);
        }
    }

    public void removeGenome(IGenome genome){
        int n = genomeMap.get(genome.getGenome());
        genomeMap.remove(genome.getGenome());
        if (n != 1) {
            genomeMap.put(genome.getGenome(), n + 1);
        }
    }
}


