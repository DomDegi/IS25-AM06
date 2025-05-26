package it.polimi.ingsw.galaxytruckerproject.model.player;

import java.io.Serializable;

/**
 * Enum representing the possible player colors in the game.
 * Each color is associated with a short string code and a numerical identifier.
 *
 * <p>Used for assigning and recognizing players visually and in serialized form.</p>
 */
public enum PlayersColor implements Serializable {
    RED, YELLOW,GREEN,BLUE;

    /**
     * Returns a one-letter string representing the color:
     * <ul>
     *   <li>"R" for RED</li>
     *   <li>"Y" for YELLOW</li>
     *   <li>"G" for GREEN</li>
     *   <li>"B" for BLUE</li>
     * </ul>
     *
     * @return the short string representation of the color
     */
    @Override
    public String toString() {
        return switch (this) {
            case RED -> "R";
            case YELLOW -> "Y";
            case GREEN -> "G";
            case BLUE -> "B";
        };
    }

    /**
     * Converts a one-letter string to the corresponding {@code PlayersColor}.
     *
     * @param value the string representation of the color ("R", "Y", "G", "B")
     * @return the matching {@code PlayersColor} enum
     * @throws IllegalArgumentException if the input string is invalid
     */
    public static PlayersColor fromString(String value) {
        return switch (value.toUpperCase()) {
            case "R" -> RED;
            case "Y" -> YELLOW;
            case "G" -> GREEN;
            case "B" -> BLUE;
            default -> throw new IllegalArgumentException("Unknown player color: " + value);
        };
    }

    /**
     * Converts the {@code PlayersColor} to a numeric identifier.
     * <ul>
     *   <li>RED = 1</li>
     *   <li>YELLOW = 2</li>
     *   <li>GREEN = 3</li>
     *   <li>BLUE = 4</li>
     * </ul>
     *
     * @return an integer representing the color
     */
    public int toInt() {
        return switch (this) {
            case RED -> 1;
            case YELLOW -> 2;
            case GREEN -> 3;
            case BLUE -> 4;
        };
    }
}
