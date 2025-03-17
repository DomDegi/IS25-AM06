package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameMode;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.Arrays;

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
}
