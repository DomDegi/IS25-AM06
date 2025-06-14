package it.polimi.ingsw.galaxytruckerproject.model.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Enumeration representing the possible colors of goods in the game, each associated with a credit value.
 * The colors are also used to encode and decode goods in compact or serialized form.
 *
 * <p>Each color has a defined value:
 * <ul>
 *   <li>RED = 4 credits (hazardous)</li>
 *   <li>YELLOW = 3 credits</li>
 *   <li>GREEN = 2 credits</li>
 *   <li>BLUE = 1 credit</li>
 * </ul>
 * </p>
 */
public enum GoodsColor implements Serializable {

    RED(4), YELLOW(3), GREEN(2), BLUE(1);

    /** The numeric value in credits associated with this color of goods. */
    private final int value;

    /**
     * Constructs a {@code GoodsColor} with the specified value in cosmic credits.
     *
     * @param value the numeric credit value for the color
     */
    GoodsColor(int value) {
        this.value = value;
    }

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
        if(this == RED)
            return "R";
        if(this == YELLOW)
            return "Y";
        if (this == GREEN)
            return "G";
        return "B";
    }

    /**
     * Gets the value of the goods color in cosmic credits.
     *
     * @return the credit value of this goods color
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the enum name of the color (e.g. "RED", "YELLOW").
     * Used by Jackson for JSON serialization.
     *
     * @return the full name of this color
     */
    @JsonValue
    public String getColorName() {
        return this.name(); // Restituisce il nome dell'enum (es. "RED")
    }

    /**
     * Parses a string to a {@code GoodsColor}, ignoring case.
     * Used by Jackson for JSON deserialization.
     *
     * @param value the name of the color (e.g. "red", "RED")
     * @return the corresponding {@code GoodsColor}
     * @throws IllegalArgumentException if the value is invalid
     */
    @JsonCreator
    public static GoodsColor fromValue(String value) {
        return GoodsColor.valueOf(value.toUpperCase());
    }

    /**
     * Parses a short one-letter code to a {@code GoodsColor}.
     *
     * @param s the short code: "R", "Y", "G", or "B" (case insensitive)
     * @return the corresponding {@code GoodsColor}
     * @throws IllegalArgumentException if the code is unknown
     */
    public static GoodsColor fromString(String s) {
        return switch (s.toUpperCase()) {
            case "R" -> RED;
            case "Y" -> YELLOW;
            case "G" -> GREEN;
            case "B" -> BLUE;
            default -> throw new IllegalArgumentException("Unknown color code: " + s);
        };
    }
}


