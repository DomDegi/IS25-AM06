package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
<<<<<<< Updated upstream:src/main/java/it/polimi/ingsw/galaxytruckerproject/cards/Planet.java
<<<<<<< Updated upstream:src/main/java/it/polimi/ingsw/galaxytruckerproject/cards/Planet.java
import it.polimi.ingsw.galaxytruckerproject.Goods;
=======
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
>>>>>>> Stashed changes:src/main/java/it/polimi/ingsw/galaxytruckerproject/model/cards/Planet.java
=======
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
>>>>>>> Stashed changes:src/main/java/it/polimi/ingsw/galaxytruckerproject/model/cards/Planet.java

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
