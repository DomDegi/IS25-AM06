package it.polimi.ingsw.galaxytruckerproject.model.player;

import java.io.Serializable;

public enum PlayersColor implements Serializable {
    RED, YELLOW,GREEN,BLUE;

    @Override
    public String toString() {
        return switch (this) {
            case RED -> "R";
            case YELLOW -> "Y";
            case GREEN -> "G";
            case BLUE -> "B";
        };
    }

    public static PlayersColor fromString(String value) {
        return switch (value.toUpperCase()) {
            case "R" -> RED;
            case "Y" -> YELLOW;
            case "G" -> GREEN;
            case "B" -> BLUE;
            default -> throw new IllegalArgumentException("Unknown player color: " + value);
        };
    }

    public int toInt() {
        return switch (this) {
            case RED -> 1;
            case YELLOW -> 2;
            case GREEN -> 3;
            case BLUE -> 4;
        };
    }
}
