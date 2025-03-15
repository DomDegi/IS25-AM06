package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;

import java.util.Arrays;

public class StarDust extends Card {

    public StarDust(int level) {
        super(level, 0);
    }


    //makes so that the player loses as many days as their exposedConnectors
    @Override
    public void executeCard(FlightBoard flightBoard) {
        Arrays.stream(flightBoard.getRanking()).
                forEach(player -> flightBoard.moveBackward(player.getPlayerRanking(), player.getShipBoard().countExposedConnectors()));
    }
}
