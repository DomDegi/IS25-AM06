
package it.polimi.ingsw.galaxytruckerproject.model.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Represents a unit of cargo in the game, defined by its {@link GoodsColor}.
 * Each good has a color that determines its value.
 *
 * <p>This class is serializable and used to represent goods stored in cargo holds.</p>
 */
public class Goods implements Serializable {

    private final GoodsColor color;

    /**
     * Creates a new {@code Goods} instance with the specified color.
     *
     * @param color the color of the goods, which determines its value
     */
    @JsonCreator
    public Goods(@JsonProperty("color") GoodsColor color) {
        this.color = color;
    }

    /**
     * Returns a string representation of the goods, which is the string representation of its color.
     *
     * @return the name of the color
     */
    public String toString() {
        return color.toString();
    }

    /**
     * Returns the color of the goods.
     *
     * @return the {@link GoodsColor} of this goods item
     */
    public GoodsColor getColor() {
        return color;
    }

    /**
     * Returns the value of the goods based on its color.
     *
     * @return the point value associated with the color of this goods
     */
    public int getValue() {
        return color.getValue();
    }
}
