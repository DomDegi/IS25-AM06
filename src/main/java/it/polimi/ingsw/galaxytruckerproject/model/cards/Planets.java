package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private boolean won;
    private GoodsManager goodsManager;

    @JsonCreator
    public Planets(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("listOfPlanets") ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = false;
    }
    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
        }
        AtomicInteger i = new AtomicInteger();
        listOfPlanets.forEach(planet -> {
            if (planet.getOccupationStatus())
                i.getAndIncrement();
        });
        if (playerToInteract.isEmpty()||i.get() ==listOfPlanets.size()) {
            System.out.print("Card Finished\n");
            game.endCardPhase();
            return;
        }
        currentPlayer= playerToInteract.getFirst();
        printListOfPlanets();
        System.out.printf(currentPlayer.getPlayerName()+" input from 1 to %d to pick which to land on, input 'no' to ignore\n\n", listOfPlanets.size());
    }
    //for each player asks if they want to spend the required days to occupy the planet they choose
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(currentPlayer==null)
            currentPlayer=game.getListOfPlayers().getFirst();
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        if(playerToInteract.isEmpty()||listOfPlanets.isEmpty()){
            System.out.print("Card Finished\n");
            game.endCardEvent();
            return;
        }
        if (input.length == 0) {
            System.out.println("Invalid input format. Please provide integer values.\n");
            return;
        }
        if (!won) {
            int choice;
            if (input[0].equalsIgnoreCase("no")) {
                input[0] = "0";
            }
            try {
                choice = Integer.parseInt(input[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please provide integer values.\n");
                return;
            }
            if (choice > 0 && choice <= listOfPlanets.size() && !listOfPlanets.get(choice - 1).getOccupationStatus()) {
                listOfPlanets.get(choice - 1).setOccupationStatus();
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                goodsManager=new GoodsManager(currentPlayer,listOfPlanets.get(choice - 1).getListOfGoods());
                won=true;
                goodsManager.goodsPrinter(listOfPlanets.get(choice - 1).getListOfGoods());
                System.out.print("Chose for each good where to put it, input 'done' to stop,'pick x y' to pick one good from your cargo\n");
            } else if (choice > 0 && choice <= listOfPlanets.size() && listOfPlanets.get(choice - 1).getOccupationStatus()) {
                System.out.printf("Error, planet %d is already taken\n\n", choice);
                printListOfPlanets();
                System.out.printf(currentPlayer.getPlayerName()+" input from 1 to %d to pick which to land on, input 'no' to ignore\n\n", listOfPlanets.size());
            } else if (choice == 0) {
                System.out.println("No action performed\n");
                playerToInteract.removeFirst();
                won=false;
                initializeCard(game);
            } else {
                System.out.println("Invalid choice: " + choice);
            }
        }else{
            if(goodsManager.getReward(input)){
                playerToInteract.removeFirst();
                won=false;
                initializeCard(game);
            }
        }
    }

    public void printListOfPlanets() {
        int index = 1;
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED= "\u001B[31m";
        for (Planet planet : listOfPlanets) {
            if (!planet.getOccupationStatus()) {
                System.out.printf("Planet %d: ", index);
                for (Goods goods : planet.getListOfGoods()) {
                    if(goods.getColor()== GoodsColor.BLUE){
                        System.out.printf(ANSI_BLUE+"%s(%d) "+ANSI_RESET, goods.getColor(), goods.getValue());
                    }else if(goods.getColor()==GoodsColor.GREEN){
                        System.out.printf(ANSI_GREEN+"%s(%d) "+ANSI_RESET, goods.getColor(), goods.getValue());
                    }else if(goods.getColor()==GoodsColor.YELLOW){
                        System.out.printf(ANSI_YELLOW+"%s(%d) "+ANSI_RESET, goods.getColor(), goods.getValue());
                    }else if(goods.getColor()==GoodsColor.RED){
                        System.out.printf(ANSI_RED+"%s(%d) "+ANSI_RESET, goods.getColor(), goods.getValue());
                    }
                }
                System.out.print("\n");
            }
            index++;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Planets: "). append(super.toString()).append(" ");
        int i = 0;
        for (Planet planet : listOfPlanets) {
            i++;
            string.append("planet ").append(i).append(" - ").append(planet.toString());
        }
        return string.toString();
    }
}