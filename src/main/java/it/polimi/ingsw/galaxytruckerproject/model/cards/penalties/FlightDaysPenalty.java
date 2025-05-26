package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

/**
 * Represents a penalty that causes the player to lose a number of flight days,
 * effectively moving them backward on the flight board.
 */
public class FlightDaysPenalty extends Penalty {

    /**
     * The number of flight days to be lost by the player.
     */
    private final int numberOfLostDays;

    /**
     * Constructs a FlightDaysPenalty with the specified number of lost days.
     *
     * @param numberOfLostDays the number of days to move backward on the flight board
     */
    @JsonCreator
    public FlightDaysPenalty(@JsonProperty("numberOfLostDays") int numberOfLostDays) {
        this.numberOfLostDays = numberOfLostDays;
    }

    /**
     * Returns a string representation of this penalty.
     *
     * @return a string describing the penalty
     */
    @Override
    public String toString() {
        return "FlightDaysPenalty: " + numberOfLostDays;
    }

    /**
     * Applies the penalty by moving the player backward on the flight board and notifying the game.
     *
     * @param game   the game context
     * @param view   the virtual view used for communication
     * @param player the player receiving the penalty
     * @return false as no further interaction is required
     */
    @Override
    public boolean initializePenalty(GameInterface game, VirtualView view, Player player) {
        game.getFlightBoard().moveBackward(player, numberOfLostDays);
        game.getDrawnCard().notifyMovement(player);
        return false;
    }
}
