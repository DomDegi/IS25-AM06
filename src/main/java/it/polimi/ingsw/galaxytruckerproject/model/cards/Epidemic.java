package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.GenericMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.Map;

public class Epidemic extends Card {

    @JsonCreator
    public Epidemic(@JsonProperty("level") int level) {
        super(level, 0);
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        broadcastMessage("Epidemic:\n All the players will lose one crew member from every populated cabin that's " +
                "connected to others populated cabins");
        executeCard(new GenericMessage("w/e"));
    }

    @Override
    public void executeCard(Message message) {
        for(Player player: game.getListOfInFlightPlayers()){
            if (!player.IsDisconnected()) {
                player.getShipBoard().epidemic();
            }
        }
        game.endCardEvent();
    }

    @Override
    public String toString() {
        return "Epidemic";
    }
}
