package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.Map;

public class Epidemic extends Card {

    @JsonCreator
    public Epidemic(@JsonProperty("level") int level) {
        super(level, 0);
    }

    @Override
    public void initializeCard(Game game, Map<String, ViewInterface> viewsMap) {
        String[] input = {"any", "input"};
        executeCard(game, , game.getListOfInFlightPlayers().getFirst().getPlayerName());
    }

    @Override
    public void executeCard(Message message) {
        for(Player player: game.getFlightBoard().getInGamePlayers()){
            player.getShipBoard().epidemic();
        }
        game.endCardEvent();
    }

    @Override
    public String toString() {
        return "Epidemic";
    }
}
