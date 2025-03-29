package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Planets extends Card{
    private final ArrayList<Planet> listOfPlanets;
    private final int numberOfPlanets;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private int won;
    private int choice;
    private int goodsToGet;
    private Coordinates coordinatesToPut;
    private final ArrayList<Goods> goodsRemovedFromCargo;

    @JsonCreator
    public Planets(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("listOfPlanets") ArrayList<Planet> listOfPlanets
    ) {
        super(level, requiredDays);
        this.listOfPlanets = listOfPlanets;
        this.numberOfPlanets = listOfPlanets.size();
        this.initialized=false;
        this.currentPlayer =null;
        this.playerToInteract = new ArrayList<>();
        this.won =0;
        this.goodsRemovedFromCargo = new ArrayList<>();
    }
    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
        }
        if (playerToInteract.isEmpty()) {
            game.endCardPhase();
            return;
        }
        currentPlayer= playerToInteract.getFirst();
        printListOfPlanets();
        System.out.printf(currentPlayer+"input from 1 to %d to pick which to land on, input 'no' to ignore", numberOfPlanets);

    }
    //for each player asks if they want to spend the required days to occupy the planet they choose
    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (won == 0) {
            if(playerToInteract.isEmpty()){
                game.endCardEvent();
                return;
            }
            if(currentPlayer==null){
                currentPlayer=game.getListOfPlayers().getFirst();
            }
            if (!playerName.equals(currentPlayer.getPlayerName()))
                return;
            if (input[0].equalsIgnoreCase("no")) {
                input[0] = "0";
            }
            try {
                choice = Integer.parseInt(input[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please provide integer values.");
                return;
            }
            if (choice > 0 && choice < listOfPlanets.size() && !listOfPlanets.get(choice - 1).getOccupationStatus()) {
                listOfPlanets.get(choice - 1).setOccupationStatus();
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                won=1;
                AtomicInteger i = new AtomicInteger(1);
                goodsRemovedFromCargo.forEach(goods -> {
                    System.out.printf(i + " - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                    i.getAndIncrement();
                });
                System.out.print("Chose for each good where to put it, input 'done' to stop");
            } else if (choice > 0 && choice <= listOfPlanets.size() && listOfPlanets.get(choice - 1).getOccupationStatus()) {
                System.out.printf("Error, planet %d is already taken\n", choice);
            } else if (choice == 0) {
                System.out.println("No action performed");
                playerToInteract.removeFirst();
                won=0;
                initializeCard(game);
            } else {
                System.out.println("Invalid choice: " + choice);
            }
        }else if(won == 1){
            if(getReward(input)){
                playerToInteract.removeFirst();
                won=0;
                initializeCard(game);
            }
        }else if(won == 2){
            goodsRemovedFromCargo.add(swapGoods(input));
        }else if(won == 3){
            System.out.println("Goods removed from cargo: ");
            AtomicInteger i= new AtomicInteger(1);
            goodsRemovedFromCargo.forEach(goods -> {System.out.printf(i+" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                i.getAndIncrement();});
            System.out.println("Enter what good of these you want to put in the cargo again(1-"+goodsRemovedFromCargo.size()+", done to stop) and where to put it: \n");
            won = 4;
        }else if(won == 4){
            if(getReward(input)){
                playerToInteract.removeFirst();
                won=0;
                initializeCard(game);
            }
        }else if(won == 5){
            goodsRemovedFromCargo.add(swapGoods(input));
        }
    }

    private boolean getReward(String[] input){
        int CoordinatesX;
        int CoordinatesY;
        if (input[0].equalsIgnoreCase("done")) {
            input[0] = "0";
        }
        try {
            goodsToGet = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return false;
        }
        try {
            CoordinatesX = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return false;
        }
        try {
            CoordinatesY = Integer.parseInt(input[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return false;
        }
        coordinatesToPut = new Coordinates(CoordinatesX, CoordinatesY);
        if (won==1){
            if (goodsToGet > 0 && goodsToGet < listOfPlanets.get(choice - 1).getListOfGoods().size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(listOfPlanets.get(choice - 1).getListOfGoods().get(goodsToGet-1),coordinatesToPut);
                if(positioned==0){
                    System.out.printf("You've successfully put the %s good in the %d,%d cargo\n", listOfPlanets.get(choice - 1).getListOfGoods().get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    listOfPlanets.get(choice - 1).getListOfGoods().remove(goodsToGet-1);
                    if(goodsRemovedFromCargo.isEmpty())
                        return listOfPlanets.get(choice - 1).getListOfGoods().isEmpty();
                }else if(positioned==1){
                    System.out.printf("Sorry, you can't put the %s good in the %d,%d cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n",listOfPlanets.get(choice - 1).getListOfGoods().get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    AtomicInteger i= new AtomicInteger(1);
                    currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut).forEach(goods -> {System.out.printf(i+" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                        i.getAndIncrement();});
                    won=2;
                    return false;
                }else if(positioned==-1){
                    return false;
                }
            } else if (goodsToGet == 0) {
                System.out.println("You stopped positioning your cargo");
                return true;
            } else {
                System.out.println("Invalid goodsToGet: " + goodsToGet);
                return false;
            }
            return false;
        }else if(won == 4){
            if (goodsToGet > 0 && goodsToGet < listOfPlanets.get(choice - 1).getListOfGoods().size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(goodsRemovedFromCargo.get(goodsToGet-1),coordinatesToPut);
                if(positioned==0){
                    System.out.printf("You've successfully put the %s good in the %d,%d cargo\n", listOfPlanets.get(choice - 1).getListOfGoods().get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    goodsRemovedFromCargo.remove(goodsToGet-1);
                    return goodsRemovedFromCargo.isEmpty();
                }else if(positioned==1){
                    System.out.printf("Sorry, you can't put the %s good in the %d,%d cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n",listOfPlanets.get(choice - 1).getListOfGoods().get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    AtomicInteger i= new AtomicInteger(1);
                    currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut).forEach(goods -> {System.out.printf(i+" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                        i.getAndIncrement();});
                    won=5;
                    return false;
                }else if(positioned==-1){
                    return false;
                }
            } else if (goodsToGet == 0) {
                System.out.println("You stopped positioning your cargo");
                return true;
            } else {
                System.out.println("Invalid goodsToGet: " + goodsToGet);
                return false;
            }
            return false;
        }
        return false;
    }

    public Goods swapGoods(String[] input){
        int chose;
        if (input[0].equalsIgnoreCase("no")) {
            input[0] = "0";
        }
        try {
            chose = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return null;
        }
        ArrayList<Goods> cargo = currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut);
        Goods goodToSwap=null;
        switch (chose){
            case 0:
                System.out.println("No action performed, select a good to remove from the cargo");
                return null;
            case 1:
                goodToSwap= cargo.getFirst();
                currentPlayer.getPlayerShip().removeGood(cargo.getFirst(), coordinatesToPut);
                break;
            case 2:
                if(cargo.size()<2) {
                    System.out.println("Invalid choice: " + chose + " - this cargo container does not exist");
                    return null;
                }
                goodToSwap= cargo.get(1);
                currentPlayer.getPlayerShip().removeGood(cargo.get(1), coordinatesToPut);
                break;
            case 3:
                if(cargo.size()<3) {
                    System.out.println("Invalid choice: " + chose + " - this cargo container does not exist");
                    return null;
                }
                goodToSwap= cargo.get(2);
                currentPlayer.getPlayerShip().removeGood(cargo.get(2), coordinatesToPut);
                break;
        }
        currentPlayer.getPlayerShip().gainGoods(listOfPlanets.get(choice - 1).getListOfGoods().get(goodsToGet-1), coordinatesToPut);
        if(won==2){
            listOfPlanets.get(choice - 1).getListOfGoods().remove(goodsToGet-1);
            won=3;
        } else if(won==5){
            goodsRemovedFromCargo.remove(goodsToGet-1);
            won=3;
        }
        return goodToSwap;
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