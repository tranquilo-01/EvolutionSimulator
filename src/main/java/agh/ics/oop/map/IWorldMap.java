package agh.ics.oop.map;

import agh.ics.oop.Vector2d;
import agh.ics.oop.map.mapelement.animal.Animal;

import java.util.LinkedList;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 */
public interface IWorldMap {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);

    /**
     * Return number of animals on map
     *
     * @return - number of animals on map
     */
    int animalNumber();

    /**
     * Calculates position animal should have after moving taking into consideration
     * map constraints.
     *
     * @param animal                - Animal to move
     * @param potentialMovePosition - potential position after moving given ni constraints
     * @return - return correct map position after moving
     */
    Vector2d calculateMovePosition(Animal animal, Vector2d potentialMovePosition);

    void removeAnimal(Animal animal);

    void addAnimal(Animal animal);

    void consumption();

    void growGrass(int number);

    LinkedList<Animal> copulation();

    int getGrassNumber();

    int getTotalEnergy();

    int getNumberOfTakenFields();


}