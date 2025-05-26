package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a pair of coordinates (x, y) used to identify positions on a ShipBoard.
 * Implements {@link Serializable} to allow saving and loading of game state.
 */
public class Coordinates implements Serializable {

    /**
     * The row index of the coordinate.
     */
    int x;

    /**
     * The column index of the coordinate.
     */
    int y;

    /**
     * Constructs a new Coordinates object with specified row and column.
     *
     * @param x the row index
     * @param y the column index
     */
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation in the format (x;y).
     *
     * @return the coordinate as a string
     */
    public String toString(){
        return "(" + x + ";" + y + ")";
    }

    /**
     * Returns the row index.
     *
     * @return the x value (row)
     */
    public int getX(){
        return x;
    }

    /**
     * Returns the column index.
     *
     * @return the y value (column)
     */
    public int getY(){
        return y;
    }

    /**
     * Updates the current coordinates with new x and y values.
     *
     * @param x the new row index
     * @param y the new column index
     */
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Prints the coordinate to the standard output in format {x;y}.
     * For debugging purposes.
     */
    public void print(){
        System.out.print(" {" + x + ";" + y + "} ");
    }

    /**
     * Checks if two Coordinates objects are equal based on their x and y values.
     *
     * @param obj the object to compare with
     * @return true if the coordinates are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates that = (Coordinates) obj;
        return x == that.x && y == that.y;
    }

    /**
     * Computes the hash code based on x and y.
     *
     * @return the hash code for the coordinates
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
