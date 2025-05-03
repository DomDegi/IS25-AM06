
package it.polimi.ingsw.galaxytruckerproject.model.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Goods implements Serializable {
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
