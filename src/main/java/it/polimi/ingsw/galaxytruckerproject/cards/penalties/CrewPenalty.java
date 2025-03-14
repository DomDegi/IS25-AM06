package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class CrewPenalty extends Penalty {

    private final int numberOfLostCrew;

    public CrewPenalty(int numberOfLostCrew) {
        this.numberOfLostCrew = numberOfLostCrew;
    }

    public int getNumberOfLostCrew() {
        return numberOfLostCrew;
    }

    @Override
    public void applyPenalty(Player player, FlightBoard flightBoard) {
        player.loseCrew(numberOfLostCrew);
    }
}
