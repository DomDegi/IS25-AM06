package it.polimi.ingsw.galaxytruckerproject.tiles;

public enum Connectors {
    SMOOTH, SINGLE, DOUBLE, UNIVERSAL;

    @Override
    public String toString() {
        if(this == SMOOTH)
            return "SMOOTH";
        if(this == SINGLE)
            return "SINGLE";
        if(this == DOUBLE)
            return "DOUBLE";
        if(this == UNIVERSAL)
            return "UNIVERSAL";
        return "";
    }
}
