package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private final int numberOfPlanets;
    private ArrayList<Player> playerToLand;
    private Player currentPlayer;
    private boolean initialized;

    public Planets(int level, int requiredDays, ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.numberOfPlanets = listOfPlanets.size();
        this.initialized=false;
        this.currentPlayer =null;
        this.playerToLand = new ArrayList<>();
    }
    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToLand = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
        }
        currentPlayer=playerToLand.removeFirst();
        printListOfPlanets();
        System.out.printf(currentPlayer+"input from 1 to %d to pick which to land on, input 0 to ignore", numberOfPlanets);
    }
    //for each player asks if they want to spend the required days to occupy the planet they choose
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(currentPlayer==null){
            currentPlayer=game.getListOfPlayers().getFirst();
        }
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        int choice;
        try {
            choice = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return;
        }
        if (choice > 0 && choice <= listOfPlanets.size() && !listOfPlanets.get(choice - 1).getOccupationStatus()) {
            currentPlayer.gainGoods(listOfPlanets.get(choice - 1).getListOfGoods());
            listOfPlanets.get(choice - 1).setOccupationStatus();
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
        } else if (choice > 0 && choice <= listOfPlanets.size() && listOfPlanets.get(choice - 1).getOccupationStatus()) {
            System.out.printf("Error, planet %d is already taken\n", choice);
        } else if (choice == 0) {
            System.out.println("No action performed");
        } else {
            System.out.println("Invalid choice: " + choice);
            executeCard(game, playerName, input);
            return;
        }
        initializeCard(game);
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

    @Override
    public String toString() {
        return "";
    }
}