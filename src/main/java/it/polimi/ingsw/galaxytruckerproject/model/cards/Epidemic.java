package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

public class Epidemic extends Card {

    @JsonCreator
    public Epidemic(@JsonProperty("level") int level) {
        super(level, 0);
    }

    @Override
    public void initializeCard(Game game) {
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        for(Player player: game.getFlightBoard().getInGamePlayers()){
            player.getShipBoard().epidemic();
        }
    }

    @Override
    public String toString() {
        return "Epidemic";
    }
}
