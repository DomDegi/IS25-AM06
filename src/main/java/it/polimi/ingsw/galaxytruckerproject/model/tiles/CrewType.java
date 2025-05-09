package it.polimi.ingsw.galaxytruckerproject.model.tiles;

public enum CrewType {
    HUMAN, PURPLE, BROWN, NotAcabin, NotSupportSystem;
    @Override
    public String toString() {
        return switch (this) {
            case HUMAN -> "H";
            case PURPLE -> "P";
            case BROWN -> "B";
            case NotAcabin -> "/";
            default -> "▓";
        };
    }
}

