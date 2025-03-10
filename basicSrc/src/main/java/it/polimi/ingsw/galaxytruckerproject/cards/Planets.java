package it.polimi.ingsw.galaxytruckerproject.cards;

import java.util.ArrayList;
import java.util.Scanner;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;
import it.polimi.ingsw.galaxytruckerproject.Goods;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private final int numberOfPlanets;

    public Planets(int level, int requiredDays, ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.numberOfPlanets = listOfPlanets.size();
    }

    //prints on stdout the planets with their goods:
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

    //for each player asks if they want to spend the required days to occupy the planet they choose
    @Override
    public void executeCard(FlightBoard flightBoard){

        for (Player player: flightBoard.getRanking()){
            System.out.printf("input from 1 to %d to pick which to land on, input 0 to ignore", numberOfPlanets);
            printListOfPlanets();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if (choice > 0 && !listOfPlanets.get(choice - 1).getOccupationStatus()){
                player.gainGoods(listOfPlanets.get(choice - 1).getListOfGoods());
                listOfPlanets.get(choice - 1).setOccupationStatus();
                flightBoard.moveBackward(player.getPlayerRanking()-1, requiredDays);
            }
            else if (listOfPlanets.get(choice - 1).getOccupationStatus()){
                System.out.printf("Error, planet %d is already taken", choice);
            }
        }
    }
}
