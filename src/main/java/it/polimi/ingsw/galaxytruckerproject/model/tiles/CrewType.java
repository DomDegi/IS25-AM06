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

    public static CrewType fromString(String value) {
        return switch (value) {
            case "H" -> HUMAN;
            case "P" -> PURPLE;
            case "B" -> BROWN;
            case "/" -> NotAcabin;
            case "▓" -> NotSupportSystem;
            default -> throw new IllegalArgumentException("Unknown crew type: " + value);
        };
    }
}

