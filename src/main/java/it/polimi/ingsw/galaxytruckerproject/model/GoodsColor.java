package it.polimi.ingsw.galaxytruckerproject.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GoodsColor {
    RED(4), YELLOW(3), GREEN(2), BLUE(1);

    private final int value;

    GoodsColor(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if(this == RED)
            return "Red";
        if(this == YELLOW)
            return "Yellow";
        if (this == GREEN)
            return "Green";
        return "Blue";
    }

    public int getValue() {
        return value;
    }

    @JsonValue
    public String getColorName() {
        return this.name(); // Restituisce il nome dell'enum (es. "RED")
    }

    @JsonCreator
    public static GoodsColor forValue(String value) {
        return GoodsColor.valueOf(value.toUpperCase());
    }
}


