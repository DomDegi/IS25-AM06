
package it.polimi.ingsw.galaxytruckerproject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Goods {
    private final GoodsColor color;

    @JsonCreator
    public Goods(@JsonProperty("color") GoodsColor color) {
        this.color = color;
    }

    public String toString() {
        return color.toString();
    }

    public GoodsColor getColor() {
        return color;
    }
    public int getValue() {
        return color.getValue();
    }
}
