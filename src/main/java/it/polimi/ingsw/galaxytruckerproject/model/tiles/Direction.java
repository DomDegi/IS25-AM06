package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public enum Direction {
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
}


