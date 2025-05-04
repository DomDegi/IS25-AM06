package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum Connectors implements Serializable {
    SMOOTH, SINGLE, DOUBLE, UNIVERSAL;

    @JsonValue
    @Override
    public String toString() {
        if (this == SMOOTH)
            return "SMOOTH";
        if (this == SINGLE)
            return "SINGLE";
        if (this == DOUBLE)
            return "DOUBLE";
        if (this == UNIVERSAL)
            return "UNIVERSAL";
        return "";
    }

    // Metodo per deserializzare la stringa in un enum
    @JsonCreator
    public static Connectors fromString(String value) {
        for (Connectors connector : Connectors.values()) {
            if (connector.name().equalsIgnoreCase(value)) {
                return connector;
            }
        }
        throw new IllegalArgumentException("Unknown connector type: " + value);
    }
}
