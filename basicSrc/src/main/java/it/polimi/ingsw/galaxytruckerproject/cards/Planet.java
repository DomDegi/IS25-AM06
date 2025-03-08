package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Goods;

import java.util.ArrayList;

public class Planet {
    private final ArrayList<Goods> listOfGoods;
    private boolean occupationStatus = false;

    public Planet(ArrayList<Goods> listOfGoods) {
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
}
