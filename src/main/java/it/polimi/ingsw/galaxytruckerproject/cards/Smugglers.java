package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Smugglers extends Enemies{
    private int lostGoods;
    private final ArrayList<Goods> rewardGoods;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private int won;
    private int goodsToGet;
    private Coordinates coordinatesToPut;

    @JsonCreator
    public Smugglers(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("lostGoods") int lostGoods,
            @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods
    ) {
        super(level, requiredDays, cannonStrength);  // Chiamata al costruttore della classe base
        this.lostGoods = lostGoods;
        this.rewardGoods = rewardGoods;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = 0;
    }

    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
            System.out.printf("WATCH OUT, SMUGGLERS!! \n If you don't have at least a Cannon Strength of "+cannonStrength+" you will loose "+lostGoods+"\n DESTROY THEM and you will loose"+requiredDays+"to fill your cargo with the following goods:\n");
            rewardGoods.forEach(goods -> System.out.printf("%s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue()));
        }
        currentPlayer= playerToInteract.getFirst();
        System.out.printf(currentPlayer+"they are coming for you! \n Do you want to use your double cannon?(yes/no)\n");
        if(playerToInteract.isEmpty()){
            game.endCardEvent();
        }
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if(playerToInteract.isEmpty()) {
            game.endCardEvent();
            return;
        }
        if(currentPlayer==null)
            currentPlayer=game.getListOfPlayers().getFirst();
        if (!playerName.equals(currentPlayer.getPlayerName()))
            return;
        if(won == 0){
            if (input[0].equalsIgnoreCase("yes")){
                System.out.println("input first the double cannons coordinates and after the batteries coordinates\n");
                won=1;
            }else if (input[0].equalsIgnoreCase("no")){
                if(currentPlayer.useDoubleCannons(new ArrayList<>()) < cannonStrength){
                    System.out.printf("Sorry"+currentPlayer+"you are too weak(looser)\n");
                    if(currentPlayer.getShipBoard().getAllGoods().size()>=lostGoods){
                        System.out.print("Choose the cargo hold that will be pillaged by the Smugglers?");
                    }else {
                        System.out.print("Choose the battery container that will be pillaged by the Smugglers?");
                    }
                    won=-1;
                }else if(currentPlayer.useDoubleCannons(new ArrayList<>()) > cannonStrength){
                    System.out.printf("HURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n"+currentPlayer+"want to pillage dose filthy Smugglers(loose %d days)(yes/no)?",requiredDays);
                    won=2;
                }else if(currentPlayer.useDoubleCannons(new ArrayList<>()) == cannonStrength){
                    System.out.println("YAY!! You are as powerful as the Smugglers, you've defeated them, but they managed to escape jus in time!\n");
                    won=0;
                    playerToInteract.removeFirst();
                    initializeCard(game);
                }
            }else {
                System.out.println("Invalid input: " + input[0]+ " retry");
            }
        } else if(won == 1) {
            ArrayList<Coordinates> coordinates = new ArrayList<>(currentPlayer.parseCoordinates(input));
            if (coordinates.isEmpty()){
                System.out.println("Invalid input\n");
                return;
            }
            float playerStrength = currentPlayer.useDoubleCannons(coordinates);
            if (playerStrength == -2){
                System.out.println("Need the batteries coordinates\n");
                return;
            }else if (playerStrength == -1){
                System.out.println("Invalid input of batteries or cannons: input again cannons coordinates\n");
                return;
            }else if (playerStrength > cannonStrength){
                System.out.printf("HURRAY!! You are more powerful than the Smugglers, you've defeated them!!\n"+currentPlayer+"want to pillage dose filthy Smugglers(loose %d days)(yes/no)?",requiredDays);
                won=2;
            }else if (playerStrength < cannonStrength){
                System.out.printf("Sorry"+currentPlayer+"you are too weak(looser)");
                if(currentPlayer.getShipBoard().getAllGoods().size()>=lostGoods){
                    System.out.print("Choose the cargo hold that will be pillaged by the Smugglers?");
                }else {
                    System.out.print("Choose the battery container that will be pillaged by the Smugglers?");
                }
                won=-1;
            }else if(playerStrength == cannonStrength){
                System.out.println("YAY!! You are as powerful as the Smugglers, you've defeated them, but they managed to escape jus in time!\n");
                won=0;
                playerToInteract.removeFirst();
                initializeCard(game);
            }
        } else if(won == 2){
            if (input[0].equalsIgnoreCase("yes")){
                System.out.println("Good job galaxy truck driver /n here is hour reward: \n");
                int i=1;
                for (Goods goods : rewardGoods) {
                    System.out.printf(i +" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                    i++;
                }
                System.out.println("Enter what good you want to get(1-"+rewardGoods.size()+", 0 for nothing) and where to put it: \n");
                won=3;
                getReward(input);
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            }else if (input[0].equalsIgnoreCase("no")){
                System.out.println("You are too pure of heart for a galaxy truck driver");
                game.endCardEvent();
                return;
            }else {
                System.out.println("Invalid input: " + input[0]);
            }
        } else if(won == 3){
            if(getReward(input)){
                game.endCardEvent();
            }
        } else if(won == 4){
            swapGoods(input);
        } else if(won == -1) {
            ArrayList<Coordinates> coordinates = currentPlayer.parseCoordinates(input);
            if (coordinates.isEmpty()) {
                System.out.println("input valid coordinates\n");
                return;
            }
            lostGoods -= currentPlayer.removeGoods(coordinates);
            if (currentPlayer.getShipBoard().isCargoEmpty()) {
                if (currentPlayer.getShipBoard().getNumBatteries() == 0) {
                    lostGoods = 0;
                } else
                    currentPlayer.printCurrentInfoBatteries();
            } else {
                currentPlayer.printCurrentInfoCargoHolds();
            }
            if (lostGoods == 0){
                System.out.printf("You gave %d good to the Smugglers\n",lostGoods);
                won=0;
                playerToInteract.removeFirst();
                initializeCard(game);
            }else {
                System.out.printf("You sill need to give %d goods to the Smugglers, insert coordinates\n",lostGoods);
            }
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
        if (goodsToGet > 0 && goodsToGet < rewardGoods.size()) {
            int positioned = currentPlayer.getShipBoard().gainGoods(rewardGoods.get(goodsToGet -1),coordinatesToPut);
            if(positioned==0){
                System.out.printf("You've successfully put the %s good in the %d,%d cargo\n", rewardGoods.get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                rewardGoods.remove(goodsToGet-1);
                return rewardGoods.isEmpty();
            }else if(positioned==1){
                System.out.printf("Sorry, you can't put the %s good in the %d,%d cargo, it is full, chose what good to remove (no to select an other good and cargo coordinates)\n", rewardGoods.get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                AtomicInteger i= new AtomicInteger(1);
                currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut).forEach(goods -> {System.out.printf(i+" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                    i.getAndIncrement();});
                won=4;
                return false;
            }else if(positioned==-1){
                return false;
            }
        } else if (goodsToGet == 0) {
            System.out.println("No action performed");
            return true;
        } else {
            System.out.println("Invalid goodsToGet: " + goodsToGet);
            return false;
        }
        return false;
    }

    public void swapGoods(String[] input){
        int chose;
        if (input[0].equalsIgnoreCase("no")) {
            input[0] = "0";
        }
        try {
            chose = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please provide integer values.");
            return;
        }
        ArrayList<Goods> cargo = currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut);
        switch (chose){
            case 0:
                System.out.println("No action performed, select a good to remove from the cargo");
                won=3;
                return;
            case 1:
                currentPlayer.getPlayerShip().removeGood(cargo.getFirst(), coordinatesToPut);
                won=3;
                break;
            case 2:
                if(cargo.size()<2) {
                    System.out.println("Invalid choice: " + chose + " - this cargo container does not exist");
                    return;
                }
                currentPlayer.getPlayerShip().removeGood(cargo.get(1), coordinatesToPut);
                won=3;
                break;
            case 3:
                if(cargo.size()<3) {
                    System.out.println("Invalid choice: " + chose + " - this cargo container does not exist");
                    return;
                }
                currentPlayer.getPlayerShip().removeGood(cargo.get(2), coordinatesToPut);
                won=3;
                break;
        }
        currentPlayer.getPlayerShip().gainGoods(rewardGoods.get(goodsToGet-1), coordinatesToPut);
        rewardGoods.remove(goodsToGet-1);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Smugglers: ").append(super.toString()).append(" lostGoods: ").append(this.lostGoods).append(" ");
        string.append("rewardGoods: ");
        for (Goods goods : rewardGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }
}