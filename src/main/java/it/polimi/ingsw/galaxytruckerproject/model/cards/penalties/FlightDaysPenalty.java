package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

public class FlightDaysPenalty extends Penalty{
    private final int numberOfLostDays;

    @JsonCreator
    public FlightDaysPenalty(@JsonProperty("numberOfLostDays") int numberOfLostDays) {
        this.numberOfLostDays = numberOfLostDays;
    }

    @Override
    public String toString() {
        return "FlightDaysPenalty: " +  numberOfLostDays;
    }

    @Override
    public boolean initializePenalty(VirtualView view, Player player) {
        return false;
    }
}
