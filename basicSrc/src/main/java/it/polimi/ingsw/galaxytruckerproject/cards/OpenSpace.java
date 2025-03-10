package it.polimi.ingsw.galaxytruckerproject.cards;

import java.util.Arrays;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;

public class OpenSpace extends Card {


    //the subclass OpenSpace needs the same parameters as the superclass
    public OpenSpace(int level) {
        super(level, 0);
    }

    //makes so that the player gain as many days as their engineStrenght
    @Override
    public void executeCard(FlightBoard flightBoard) {
        Arrays.stream(flightBoard.getRanking()).
                forEach(player -> flightBoard.moveForward(player.getPlayerRanking()-1, player.getEnginePower()));
    }
}
