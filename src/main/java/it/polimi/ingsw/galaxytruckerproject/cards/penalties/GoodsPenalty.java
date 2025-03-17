package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

public class GoodsPenalty extends Penalty {

    private int numberOfLostGoods;

    public GoodsPenalty(int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input){
        System.out.printf("You have %d more goods to remove", numberOfLostGoods);
        try {
            int x = Integer.parseInt(input[0]);
            int y = Integer.parseInt(input[1]);
            boolean returnValue = player.getShipBoard().chooseCargoStockToEmpty(new Coordinates(x, y));
            if (returnValue){
                numberOfLostGoods--;
            }
        } catch (NumberFormatException e) {
            System.out.println("you have not entered a number");
            return 0;
        }
        if (numberOfLostGoods == 0){
            return 1;
        }
        printInfo(player);
        return 0;
    }

    @Override
    public String toString() {
        return "GoodsPenalty";
    }

    public void printInfo(Player player) {
        player.printCurrentInfoCargoHolds();
    }
}
