package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;

public class CrewPenalty extends Penalty {

    private int numberOfLostCrew;

    @JsonCreator
    public CrewPenalty(@JsonProperty("numberOfLostCrew") int numberOfLostCrew) {
        this.numberOfLostCrew = numberOfLostCrew;
    }

    public int getNumberOfLostCrew() {
        return numberOfLostCrew;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input) {
        System.out.printf("you have %d more crew member to remove\n", numberOfLostCrew);
        ArrayList<Coordinates> coordinates = player.parseCoordinates(input);
        if (coordinates.isEmpty()) {
            return 0;
        }
        while (coordinates.size() > numberOfLostCrew) {
            coordinates.removeLast();
        }
        numberOfLostCrew -= player.removeCrew(coordinates);

        if (numberOfLostCrew == 0) {
            return 1;
        }
        printInfo(player);
        return 0;
    }


    public void printInfo(Player player) {
        if (player.getTotalCrew() == 0)
            numberOfLostCrew = 0;
        player.printCurrentInfoCabins();
    }

    @Override
    public String toString() {
        return "CrewPenalty";
    }
}
