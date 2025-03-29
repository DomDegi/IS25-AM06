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
}

