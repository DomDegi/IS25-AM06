package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;

import java.util.Arrays;

public class Epidemic extends Card {

    public Epidemic(int level) {
        super(level, 0);
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {
        Arrays.stream(flightBoard.getRanking()).
                forEach(player -> player.adiacentCabins()); //could be called epidemic()
    }
}
