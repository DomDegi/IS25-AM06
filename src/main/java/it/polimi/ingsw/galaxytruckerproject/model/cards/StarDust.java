package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

public class StarDust extends Card {

    @JsonCreator
    public StarDust(@JsonProperty("level") int level) {
        super(level, 0);
    }

    //This cards doesn't need any input, so it gets instantly executed when initialized
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        broadcastMessage("StarDust: every player will move backward one step for every exposed connector");
        executeCard();
    }

    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {}
    @Override
    public void engineChoice(String playerName, int numDoubleEngine, ArrayList<Coordinates> batteriesToUse) {}
    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {}
    @Override
    public void choice(String playerName, boolean decision) {}

    @Override
    public void planetChoice(String playerName, int planet) {}

    //makes so that the player loses as many days as their exposedConnectors
    public void executeCard() {
        ArrayList<Player> players = game.getListOfInFlightPlayers();
        Player currentPlayer;

        //when moving backward starts from the last
        for (int i = players.size() - 1; i >= 0; i--) {
            currentPlayer = players.get(i);
            int playerExposedConnectors = players.get(i).getShipBoard().countExposedConnectors();

            if (playerExposedConnectors > 0) {
                notifyMovement(currentPlayer);
            }
        }
        game.endCardEvent();
    }

    @Override
    public String toString(){
        return "StarDust";
    }
}

