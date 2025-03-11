package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;

public class FlightDaysPenalty extends Penalty{
    private final int numberOfLostDays;

    public FlightDaysPenalty(int numberOfLostDays) {
        this.numberOfLostDays = numberOfLostDays;
    }

    @Override
    public void applyPenalty(Player player, FlightBoard flightBoard) {
        flightBoard.moveBackward(player.getPlayerRanking(), numberOfLostDays);
    }
}
