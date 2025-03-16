package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Epidemic extends Card {

    public Epidemic(int level) {
        super(level, 0);
    }

    // calls for every player epidemic and at the end checks if there is any player with no more human crew member
    //in that case forces them to early land.
    @Override
    public void executeCard(Game game, String[] input) {
        ArrayList<Player> players = game.getListOfPlayers();

        for (Player player : players) {
            player.getShipBoard().epidemic();
        }

        for (Player player : players) {
            if (player.getShipBoard().checkEarlyLanding()){
                game.getFlightBoard().earlyLanding(player.getPlayerRanking());
            }
        }
    }
}
