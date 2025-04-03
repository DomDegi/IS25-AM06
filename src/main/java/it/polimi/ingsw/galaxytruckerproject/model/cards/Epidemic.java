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
        String[] input = {"any", "input"};
        executeCard(game, game.getListOfPlayers().getFirst().getPlayerName(), input);
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
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
