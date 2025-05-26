package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * Enum representing the connector types used on tiles to determine connectivity.
 * Each connector has a different compatibility rule:
 * - SMOOTH: No connector
 * - SINGLE: Can connect to SINGLE or UNIVERSAL
 * - DOUBLE: Can connect to DOUBLE or UNIVERSAL
 * - UNIVERSAL: Can connect to any type
 */
public enum Connectors implements Serializable {
    SMOOTH, SINGLE, DOUBLE, UNIVERSAL;

    /**
     * Returns a string representation of the connector for serialization.
     * "0" = SMOOTH, "1" = SINGLE, "2" = DOUBLE, "3" = UNIVERSAL
     *
     * @return string representation of the connector
     */
    @JsonValue
    @Override
    public String toString() {
        if (this == SMOOTH)
            return "0";
        if (this == SINGLE)
            return "1";
        if (this == DOUBLE)
            return "2";
        if (this == UNIVERSAL)
            return "3";
        return "  ";
    }

    /**
     * Deserializes a string value into the corresponding Connectors enum.
     * This uses the enum name (e.g., "SINGLE", "DOUBLE").
     *
     * @param value the string to convert
     * @return the corresponding Connectors enum
     * @throws IllegalArgumentException if the value is unknown
     */
    @JsonCreator
    public static Connectors fromString(String value) {
        for (Connectors connector : Connectors.values()) {
            if (connector.name().equals(value)) {
                return connector;
            }
        }
        throw new IllegalArgumentException("Unknown connector type: " + value);
    }

    /**
     * Deserializes an integer value (ordinal) into the corresponding Connectors enum.
     *
     * @param value the ordinal value
     * @return the corresponding Connectors enum
     * @throws IllegalArgumentException if the value is not a valid ordinal
     */
    public static Connectors fromValue(int value) {
        for (Connectors connector : Connectors.values()) {
            if (connector.ordinal() == value) {
                return connector;
            }
        }
        throw new IllegalArgumentException("Unknown connector type: " + value);
    }
}
