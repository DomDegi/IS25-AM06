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
}


