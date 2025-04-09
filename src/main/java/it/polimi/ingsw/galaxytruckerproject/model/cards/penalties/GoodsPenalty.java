package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

public class GoodsPenalty extends Penalty {
    private final int numberOfLostGoods;
    private int numberPlayerOfLostGoods;

    @JsonCreator
    public GoodsPenalty(@JsonProperty("numberOfLostGoods") int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
        this.numberPlayerOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    @Override
    public int applyPenalty(GameInterface game, Player player, ViewInterface playersView, Message message){
        ArrayList<Coordinates> coordinates = player.parseCoordinates(input);
        if (coordinates.isEmpty()) {
            return 0;
        }
        while (coordinates.size() > numberPlayerOfLostGoods) {
            coordinates.removeLast();
        }
        numberPlayerOfLostGoods -= player.removeGoods(coordinates);
        if (numberPlayerOfLostGoods == 0){
            numberPlayerOfLostGoods=numberOfLostGoods;
            return 1;
        }
        System.out.printf("You have %d more goods to remove\n", numberPlayerOfLostGoods);
        printInfo(, player);
        return 0;
    }

    @Override
    public String toString() {
        return "GoodsPenalty " +  numberPlayerOfLostGoods;
    }

    public int getNumber(){
        return numberOfLostGoods;
    }

    public void printInfo(ViewInterface view, Player player) {
        if (player.getShipBoard().isCargoEmpty()) {
            if (player.getShipBoard().getNumBatteries() == 0) {
                numberPlayerOfLostGoods = 0;
            }
            else
                player.printCurrentInfoBatteries();
        } else {
            player.printCurrentInfoCargoHolds();
        }
    }
}
