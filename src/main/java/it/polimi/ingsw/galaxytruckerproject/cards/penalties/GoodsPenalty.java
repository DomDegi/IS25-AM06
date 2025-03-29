package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;

public class GoodsPenalty extends Penalty {

    private int numberOfLostGoods;

    @JsonCreator
    public GoodsPenalty(@JsonProperty("numberOfLostGoods") int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input){
        System.out.printf("You have %d more goods to remove", numberOfLostGoods);
        ArrayList<Coordinates> coordinates = player.parseCoordinates(input);
        if (coordinates.isEmpty()) {
            return 0;
        }
        while (coordinates.size() > numberOfLostGoods) {
            coordinates.removeLast();
        }
        numberOfLostGoods -= player.removeGoods(coordinates);
        if (numberOfLostGoods == 0){
            return 1;
        }
        printInfo(player);
        return 0;
    }

    @Override
    public String toString() {

        return "GoodsPenalty " +  numberOfLostGoods;
    }

    public void printInfo(Player player) {
        if (player.getShipBoard().isCargoEmpty()) {
            if (player.getShipBoard().getNumBatteries() == 0) {
                numberOfLostGoods = 0;
            }
            else
                player.printCurrentInfoBatteries();
        }
        player.printCurrentInfoCargoHolds();
    }
}
