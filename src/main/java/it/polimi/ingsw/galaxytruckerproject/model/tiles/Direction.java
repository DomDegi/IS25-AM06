package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import java.io.Serializable;

public enum Direction implements Serializable {
        NORTH,EAST,SOUTH,WEST;

        @Override
        public String toString() {
            return switch (this) {
                case NORTH -> "N";
                case EAST -> "E";
                case SOUTH -> "S";
                case WEST -> "W";
            };
        }

    // Metodo per deserializzare la stringa in un enum
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


