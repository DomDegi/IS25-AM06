package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

public class FlightDaysPenalty extends Penalty{
    private final int numberOfLostDays;

    @JsonCreator
    public FlightDaysPenalty(@JsonProperty("numberOfLostDays") int numberOfLostDays) {
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

        return "FlightDaysPenalty: " +  numberOfLostDays;
    }

    public void printInfo(Player player) {}
}
