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
            return "0";
        if (this == SINGLE)
            return "1";
        if (this == DOUBLE)
            return "2";
        if (this == UNIVERSAL)
            return "3";
        return "  ";
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
