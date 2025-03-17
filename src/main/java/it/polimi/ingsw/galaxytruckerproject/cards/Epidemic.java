package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.Arrays;

public class Epidemic extends Card {

    public Epidemic(int level) {
        super(level, 0);
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {
        //could be called epidemic()
        for (Player player : flightBoard.getRanking()) {
            player.getPlayerShip().epidemic();
        }
    }
}
