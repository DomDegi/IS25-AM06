package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a planet card that offers a list of goods to the player who lands on it.
 * The planet can only be occupied by one player.
 */
public class Planet implements Serializable {

    /** List of goods available on this planet. */
    private final ArrayList<Goods> listOfGoods;

    /** Whether the planet has already been occupied by a player. */
    private boolean occupationStatus = false;

    /**
     * Constructor used for JSON deserialization.
     *
     * @param listOfGoods list of goods available on the planet
     */
    @JsonCreator
    public Planet(@JsonProperty("listOfGoods") ArrayList<Goods> listOfGoods) {
        this.listOfGoods = listOfGoods;
    }

    /**
     * Returns the list of goods available on the planet.
     *
     * @return list of goods
     */
    public ArrayList<Goods> getListOfGoods() {
        return listOfGoods;
    }

    /**
     * Returns the occupation status of the planet.
     *
     * @return true if the planet is already occupied, false otherwise
     */
    public boolean getOccupationStatus() {
        return occupationStatus;
    }

    /**
     * Marks the planet as occupied.
     * Once set, it cannot be reset.
     */
    public void setOccupationStatus() {
        this.occupationStatus = true;
    }

    /**
     * Returns a string representation of the goods on the planet.
     *
     * @return formatted string listing the goods
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Goods goods : listOfGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }
}
