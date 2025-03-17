package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private final int numberOfPlanets;

    public Planets(int level, int requiredDays, ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.numberOfPlanets = listOfPlanets.size();
    }
    @Override
    public void initializeCard(Game game) {

    }
    //for each player asks if they want to spend the required days to occupy the planet they choose
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        FlightBoard flightBoard=game.getFlightBoard();
        for (Player player: game.getListOfAllPlayer()){
            System.out.printf("input from 1 to %d to pick which to land on, input 0 to ignore", numberOfPlanets);
            printListOfPlanets();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if (choice > 0 && !listOfPlanets.get(choice - 1).getOccupationStatus()){
                player.gainGoods(listOfPlanets.get(choice - 1).getListOfGoods());
                listOfPlanets.get(choice - 1).setOccupationStatus();
                flightBoard.moveBackward(player.getPlayerRanking(), requiredDays);
            }
            else if (listOfPlanets.get(choice - 1).getOccupationStatus()){
                System.out.printf("Error, planet %d is already taken", choice);
            }

            //prints on stdout the planets with their goods:

     }
    }
    public void printListOfPlanets() {
        int index = 1;
        for (Planet planet : listOfPlanets) {
            if (!planet.getOccupationStatus()) {
                System.out.printf("Planet %d: ", index);
                for (Goods goods : planet.getListOfGoods()) {
                    System.out.printf("%s(%d) ", goods.getColor(), goods.getValue());
                }
                System.out.print("\n");
            }
            index++;
        }
    }

}