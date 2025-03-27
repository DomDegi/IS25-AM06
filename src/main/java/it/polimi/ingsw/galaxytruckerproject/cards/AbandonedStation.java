package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AbandonedStation extends Card {
    private final int crewNumberRequired;
    private final ArrayList<Goods> possibleGoodsGain;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private boolean initialized;
    private int won;
    private int goodsToGet;
    private Coordinates coordinatesToPut;
    private final ArrayList<Goods> goodsRemovedFromCargo;

    @JsonCreator
    public AbandonedStation(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain
    ) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.initialized=false;
        this.currentPlayer =null;
        this.playerToInteract = new ArrayList<>();
        this.won =0;
        this.goodsRemovedFromCargo = new ArrayList<>();
    }
    //asks every player in order of ranking that meets the requirements if they want to spend days to gain the goods
    @Override
    public void initializeCard(Game game) {
        if (!initialized) {
            playerToInteract = new ArrayList<>(game.getListOfPlayers());
            initialized=true;
        }
        if(playerToInteract.isEmpty()){
            game.endCardPhase();
            return;
        }
        System.out.printf("\nAbandoned station: you will loose %d flight days to gain the following goods:\n", requiredDays);
        possibleGoodsGain.forEach(goods -> System.out.printf("%s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue()));
        currentPlayer= playerToInteract.getFirst();
        if (currentPlayer.getTotalCrew() < crewNumberRequired){
            System.out.printf("\nSorry "+currentPlayer.getPlayerName()+" you can't land on the station, you need at least %d crew members and you have %d\n", crewNumberRequired,currentPlayer.getTotalCrew() );
            playerToInteract.removeFirst();
            initializeCard(game);
            return;
        }
        System.out.printf("\n"+currentPlayer.getPlayerName()+" congratulation, you have "+currentPlayer.getTotalCrew() +" witch is more than %d input 'yes' to land on the station, input 'no' to ignore, you will loose %d flight days\n\n", crewNumberRequired,requiredDays);
    }

    @Override
    public void executeCard(Game game, String playerName, String[] input) {
        if (won == 0) {
            if (playerToInteract.isEmpty()) {
                game.endCardPhase();
                return;
            }
            if (currentPlayer == null) {
                currentPlayer = game.getListOfPlayers().getFirst();
            }
            if (!playerName.equals(currentPlayer.getPlayerName()))
                return;
            int choice;
            if (input[0].equalsIgnoreCase("no")) {
                input[0] = "0";
            }
            if (input[0].equalsIgnoreCase("yes")) {
                input[0] = "1";
            }
            try {
                choice = Integer.parseInt(input[0]);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return;
            }
            if (currentPlayer.getTotalCrew() >= crewNumberRequired) {
                if (choice == 1) {
                    game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                    won=1;
                    AtomicInteger i= new AtomicInteger(1);
                    possibleGoodsGain.forEach(goods -> {
                        System.out.printf(i + " - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                        i.getAndIncrement();
                    });
                    System.out.print("\nChose for each good where to put it, input 'done' to stop\n");
                    //exit for loop: the station has been claimed
                } else if (choice == 0) {
                    System.out.println("\nNo action performed");
                    playerToInteract.removeFirst();
                    initializeCard(game);
                } else {
                    System.out.println("\nInvalid choice: " + choice);
                }
            } else {
                System.out.printf("\nSorry" + currentPlayer + "you can't land on the station, you need at least %d crew members\n", crewNumberRequired);
            }
        }else if(won == 1){
            if(getReward(input)){
                game.endCardEvent();
            }
        }else if(won == 2){
            goodsRemovedFromCargo.add(swapGoods(input));
        }else if(won == 3){
            System.out.println("\nGood removed from cargo:\n ");
            AtomicInteger i = new AtomicInteger(1);
            goodsRemovedFromCargo.forEach(goods -> {
                System.out.printf(i + " - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                i.getAndIncrement();
            });
            System.out.println("\nEnter what good of these you want to put in the cargo again(1-"+goodsRemovedFromCargo.size()+", done to stop) and where to put it: \n");
            won = 4;
        }else if(won == 4){
            if(getReward(input)){
                game.endCardEvent();
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
            System.out.println("\nInvalid input format. Please provide integer values.");
            return false;
        }
        try {
            CoordinatesX = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return false;
        }
        try {
            CoordinatesY = Integer.parseInt(input[2]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return false;
        }
        coordinatesToPut = new Coordinates(CoordinatesX, CoordinatesY);
        if (won==1){
            if (goodsToGet > 0 && goodsToGet < possibleGoodsGain.size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet-1),coordinatesToPut);
                if(positioned==0){
                    System.out.printf("\nYou've successfully put the %s good in the %d,%d cargo\n", possibleGoodsGain.get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    possibleGoodsGain.remove(goodsToGet-1);
                    return possibleGoodsGain.isEmpty();
                }else if(positioned==1){
                    System.out.printf("\nSorry, you can't put the %s good in the %d,%d cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n",possibleGoodsGain.get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    AtomicInteger i= new AtomicInteger(1);
                    currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut).forEach(goods -> {System.out.printf(i+" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                        i.getAndIncrement();});
                    won=2;
                    return false;
                }else if(positioned==-1){
                    return false;
                }
            } else if (goodsToGet == 0) {
                System.out.println("\nYou stopped positioning your cargo");
                return true;
            } else {
                System.out.println("\nInvalid goodsToGet: " + goodsToGet);
                return false;
            }
            return false;
        }else if(won == 4){
            if (goodsToGet > 0 && goodsToGet < possibleGoodsGain.size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(goodsRemovedFromCargo.get(goodsToGet-1),coordinatesToPut);
                if(positioned==0){
                    System.out.printf("\nYou've successfully put the %s good in the %d,%d cargo\n", possibleGoodsGain.get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    goodsRemovedFromCargo.remove(goodsToGet-1);
                    return goodsRemovedFromCargo.isEmpty();
                }else if(positioned==1){
                    System.out.printf("\nSorry, you can't put the %s good in the %d,%d cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n",possibleGoodsGain.get(goodsToGet -1).getColor(), coordinatesToPut.getX(),coordinatesToPut.getY() );
                    AtomicInteger i= new AtomicInteger(1);
                    currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut).forEach(goods -> {System.out.printf(i+" - %s good: it equals to %d cosmic credits\n", goods.getColor(), goods.getValue());
                        i.getAndIncrement();});
                    won=5;
                    return false;
                }else if(positioned==-1){
                    return false;
                }
            } else if (goodsToGet == 0) {
                System.out.println("\nYou stopped positioning your cargo");
                return true;
            } else {
                System.out.println("\nInvalid goodsToGet: " + goodsToGet);
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
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        ArrayList<Goods> cargo = currentPlayer.getPlayerShip().getSingleCargoGoods(coordinatesToPut);
        Goods goodToSwap=null;
        switch (chose){
            case 0:
                System.out.println("\nNo action performed, select a good to remove from the cargo");
                return null;
            case 1:
                goodToSwap= cargo.getFirst();
                currentPlayer.getPlayerShip().removeGood(cargo.getFirst(), coordinatesToPut);
                break;
            case 2:
                if(cargo.size()<2) {
                    System.out.println("\nInvalid choice: " + chose + " - this cargo container does not exist");
                    return null;
                }
                goodToSwap= cargo.get(1);
                currentPlayer.getPlayerShip().removeGood(cargo.get(1), coordinatesToPut);
                break;
            case 3:
                if(cargo.size()<3) {
                    System.out.println("\nInvalid choice: " + chose + " - this cargo container does not exist");
                    return null;
                }
                goodToSwap= cargo.get(2);
                currentPlayer.getPlayerShip().removeGood(cargo.get(2), coordinatesToPut);

                break;
        }
        currentPlayer.getPlayerShip().gainGoods(possibleGoodsGain.get(goodsToGet-1), coordinatesToPut);
        if(won==2){
            possibleGoodsGain.remove(goodsToGet-1);
            won=3;
        } else if(won==5){
            goodsRemovedFromCargo.remove(goodsToGet-1);
            won=3;
        }
        return goodToSwap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbandonedStation: ").append(super.toString()).append(" ").
                append("crewNumberRequired: ").append(crewNumberRequired).
                append(" possibleGoodsGain: ");
        for (Goods good: possibleGoodsGain)
            sb.append(good.toString()).append(" ");
        return sb.toString();
    }
}
