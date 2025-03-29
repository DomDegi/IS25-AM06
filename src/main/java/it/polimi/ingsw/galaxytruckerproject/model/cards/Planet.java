package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Goods;

import java.util.ArrayList;

public class Planet {
    private final ArrayList<Goods> listOfGoods;
    private boolean occupationStatus = false;

    @JsonCreator
    public Planet(@JsonProperty("listOfGoods") ArrayList<Goods> listOfGoods) {
        this.listOfGoods = listOfGoods;
    }

    //returns the list of goods that can be gained by occupying the planet
    public ArrayList<Goods> getListOfGoods() {
        return listOfGoods;
    }

    //return the planet occupation state
    public boolean getOccupationStatus(){
        return occupationStatus;
    }

    //sets the planet status to occupied
    public void setOccupationStatus(){
        this.occupationStatus = true;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Goods goods : listOfGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }
}
