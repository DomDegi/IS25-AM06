package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

public class CrewPenalty extends Penalty {

    private int numberOfLostCrew;

    public CrewPenalty(int numberOfLostCrew) {
        this.numberOfLostCrew = numberOfLostCrew;
    }

    public int getNumberOfLostCrew() {
        return numberOfLostCrew;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input) {
        System.out.printf("you have %d more crew member to remove\n", numberOfLostCrew);
        try {
            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);
            boolean returnValue = player.getShipBoard().chooseCrewToRemove(new Coordinates(x, y));
            if (returnValue){
                numberOfLostCrew--;
            }
        } catch (NumberFormatException e) {
            System.out.println("you have not entered a number");
            return 0;
        }
        if (numberOfLostCrew == 0){
            return 1;
        }
        printInfo(player);
        return 0;
    }

    public void printInfo(Player player) {
        player.printCurrentInfoCabins();
    }

    @Override
    public String toString() {
        return "CrewPenalty";
    }
}
