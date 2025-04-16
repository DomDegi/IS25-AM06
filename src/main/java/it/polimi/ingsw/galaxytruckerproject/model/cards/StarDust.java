package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.GenericMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
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
        executeCard(new GenericMessage("w/e"));
    }

    //makes so that the player loses as many days as their exposedConnectors
    @Override
    public void executeCard(Message message) {
        ArrayList<Player> players = game.getListOfInFlightPlayers();

        //when moving backward starts from the last
        for (int i = players.size() - 1; i >= 0; i--) {
            int playerExposedConnectors = players.get(i).getShipBoard().countExposedConnectors();

            if (playerExposedConnectors > 0) {
                game.getFlightBoard().moveBackward(players.get(i), playerExposedConnectors);
                broadcastMessage(players.get(i).getPlayerName() + " moved backward as many steps as their exposed connectors: " + playerExposedConnectors + "\n");
            }
            else {
                broadcastMessage(players.get(i).getPlayerName() + " has no exposed connectors. StarDust doesn't affect them\n");
            }
        }
        game.endCardEvent();
    }

    @Override
    public String toString(){
        return "StarDust";
    }
}

