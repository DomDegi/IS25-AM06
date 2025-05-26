package it.polimi.ingsw.galaxytruckerproject.model.tiles;

/**
 * Enum representing the coverage direction of a shield on a tile.
 * It defines which corners of the tile are protected by a shield.
 */
public enum Coverage {

    /**
     * Shield covers the north-east corner.
     */
    NORTH_EAST,

    /**
     * Shield covers the south-east corner.
     */
    SOUTH_EAST,

    /**
     * Shield covers the south-west corner.
     */
    SOUTH_WEST,

    /**
     * Shield covers the north-west corner.
     */
    NORTH_WEST,

    /**
     * Tile is not covered by any shield.
     */
    NONE;

    /**
     * Returns the full string representation of the coverage.
     *
     * @return the string name of the coverage (e.g., "NORTH_EAST")
     */
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

    /**
     * Returns a compact string representation for serialization.
     *
     * @return the short code of the coverage (e.g., "NE", "SW", "N")
     */
    public String toStringData() {
        return switch (this) {
            case NORTH_EAST -> "NE";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
            case NORTH_WEST -> "NW";
            case NONE -> "N";
        };
    }

    /**
     * Parses a compact string representation and returns the corresponding Coverage enum.
     *
     * @param value the compact string value (e.g., "NE", "SE", "N")
     * @return the corresponding {@code Coverage} enum value
     * @throws IllegalArgumentException if the value is not recognized
     */
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
