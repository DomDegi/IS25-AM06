package it.polimi.ingsw.galaxytruckerproject.tiles;

public enum Coverage {
    NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST;

    @Override
    public String toString() {
        switch(this){
            case NORTH_EAST:{
                return "NORTH_EAST";
            }
            case SOUTH_EAST:{
            return "SOUTH_EAST";}
            case SOUTH_WEST:{
            return "SOUTH_WEST";}
            case NORTH_WEST:{
                return "NORTH_WEST";}
            default:
                return "";
        }
        }
}

