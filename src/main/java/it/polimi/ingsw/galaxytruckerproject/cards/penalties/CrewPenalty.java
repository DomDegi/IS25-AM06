package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.Game;
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
    public int applyPenalty(Game game, Player player, String[] input) {
        
        player.getShipBoard().chooseCrewtoRemove();
    }

    @Override
    public String toString() {
        return "CrewPenalty";
    }
}
