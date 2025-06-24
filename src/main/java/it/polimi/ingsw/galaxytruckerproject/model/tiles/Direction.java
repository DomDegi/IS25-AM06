package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.io.Serializable;

/**
 * Enum representing the four cardinal directions used in tile orientation.
 * This enum is used primarily for determining the facing of components such as cannons.
 */
public enum Direction implements Serializable {

    /**
     * Represents the north direction (upward).
     */
    NORTH,

    /**
     * Represents the east direction (right).
     */
    EAST,

    /**
     * Represents the south direction (downward).
     */
    SOUTH,

    /**
     * Represents the west direction (left).
     */
    WEST;

    /**
     * Returns the short string representation of the direction.
     *
     * @return "N" for NORTH, "E" for EAST, "S" for SOUTH, "W" for WEST
     */
    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }

    /**
     * Parses a single-character string into a corresponding {@code Direction} enum.
     *
     * @param value the string representation of the direction ("N", "E", "S", or "W")
     * @return the corresponding Direction
     * @throws IllegalArgumentException if the input is not one of the expected values
     */
    public static Direction fromString(String value) {
        switch (value.toUpperCase()) {
            case "N":
                return Direction.NORTH;
            case "E":
                return Direction.EAST;
            case "S":
                return Direction.SOUTH;
            case "W":
                return Direction.WEST;
            default:
                throw new IllegalArgumentException("Unknown direction: " + value);
        }
    }
}
