package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class Epidemic extends Card {

    public Epidemic(int level) {
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
        return "";
    }
}
