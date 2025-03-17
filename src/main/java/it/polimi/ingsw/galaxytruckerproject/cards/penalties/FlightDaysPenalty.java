package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class FlightDaysPenalty extends Penalty{
    private final int numberOfLostDays;

    public FlightDaysPenalty(int numberOfLostDays) {
        this.numberOfLostDays = numberOfLostDays;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input) {
        game.getFlightBoard().moveBackward(player, numberOfLostDays);
        game.getFlightBoard().concludeMovement();
        return 1;
    }

    @Override
    public String toString() {
        return "FlightDaysPenalty";
    }
}
