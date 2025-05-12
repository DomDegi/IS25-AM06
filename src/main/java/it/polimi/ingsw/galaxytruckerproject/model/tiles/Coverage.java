package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public enum Coverage {
    NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NONE;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH_EAST -> "NORTH_EAST";
            case SOUTH_EAST -> "SOUTH_EAST";
            case SOUTH_WEST -> "SOUTH_WEST";
            case NORTH_WEST -> "NORTH_WEST";
            case NONE -> "NONE";
        };
    }

    public String toStringData() {
        return switch (this) {
            case NORTH_EAST -> "NE";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
            case NORTH_WEST -> "NW";
            case NONE -> "N";
        };
    }

    public static Coverage fromStringData(String value) {
        return switch (value.toUpperCase()) {
            case "NE" -> NORTH_EAST;
            case "SE" -> SOUTH_EAST;
            case "SW" -> SOUTH_WEST;
            case "NW" -> NORTH_WEST;
            case "N" -> NONE;
            default -> throw new IllegalArgumentException("Unknown coverage value: " + value);
        };
    }
}

