package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class GoodsManager {
    private final ArrayList<Goods> possibleGoodsGain;
    private Coordinates coordinatesToPut;
    private final LightPlayer currentPlayer;
    private int state;
    private int goodsToGet;
    private final HashSet<CargoHold> changes;

    public GoodsManager(LightPlayer currentPlayer, ArrayList<Goods> possibleGoodsGain) {
        this.currentPlayer=currentPlayer;
        this.changes=new HashSet<>();
        this.possibleGoodsGain=possibleGoodsGain;
        this.state=0;
        this.goodsToGet=0;
        this.coordinatesToPut=new Coordinates(0,0);
    }

    public HashSet<CargoHold> getReward(String[] input){
        if(state==0) {
            int CoordinatesX;
            int CoordinatesY;
            if (input.length == 0) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return null;
            }
            if (input[0].equalsIgnoreCase("done") || input[0].equalsIgnoreCase("0")) {
                System.out.println("\nYou stopped positioning your cargo\n");
                return changes;
            }
            if (input.length < 3) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return null;
            }
            try {
                CoordinatesX = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return null;
            }
            try {
                CoordinatesY = Integer.parseInt(input[2]);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return null;
            }
            coordinatesToPut = new Coordinates(CoordinatesX, CoordinatesY);
            if (input[0].equalsIgnoreCase("pick")) {
                if (currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut).isEmpty()) {
                    System.out.println("\nCargo Hold is empty");
                    return null;
                }
                goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                System.out.println("Input witch good to pick:");
                changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
                state = -1;
                return null;
            }
            try {
                goodsToGet = Integer.parseInt(input[0]);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return null;
            }

            if (goodsToGet > 0 && goodsToGet <=possibleGoodsGain.size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet - 1), coordinatesToPut);
                if (positioned == 0) {
                    changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
                    possibleGoodsGain.remove(goodsToGet - 1);
                    if(possibleGoodsGain.isEmpty()) {
                        System.out.println("\nGoods stock is empty, input 'done' to stop,'pick  x y' to pick one good from your cargo: ");
                        return null;
                    }
                    System.out.printf("\nYou've successfully put the %s good in the %d,%d cargo\n\n", possibleGoodsGain.get(goodsToGet - 1).getColor(), coordinatesToPut.getX(), coordinatesToPut.getY());
                    goodsPrinter(possibleGoodsGain);
                    System.out.print("Chose for each good where to put it, input 'no' to stop:\n");
                    return null;
                } else if (positioned == 1) {
                    System.out.printf("\nSorry, you can't put the %s good in the %d,%d cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n", possibleGoodsGain.get(goodsToGet - 1).getColor(), coordinatesToPut.getX(), coordinatesToPut.getY());
                    goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
                    state = 1;
                    return null;
                } else if (positioned == -1) {
                    return null;
                }
            } else {
                System.out.println("\nInvalid goodsToGet: " + goodsToGet);
                return null;
            }
        }else {
            swapGoods(input);
        }
        return null;
    }

    public void swapGoods(String[] input){
        int chose;
        if (input.length==0) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return ;
        }
        if (input[0].equalsIgnoreCase("no")) {
            input[0] = "0";
        }
        try {
            chose = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return ;
        }
        ArrayList<Goods> cargo = currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut);
        Goods goodToSwap;
        switch (chose){
            case 0:
                System.out.println("\nNo action performed, select a good to remove from the cargo\n");
                state =0;
                return;
            case 1:
                if(cargo.isEmpty()) {
                    System.out.println("\nInvalid choice: " + chose + " - this cargo container does not exist");
                    goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    System.out.print("Chose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap= cargo.getFirst();
                currentPlayer.getShipBoard().removeGood(cargo.getFirst(), coordinatesToPut);
                break;
            case 2:
                if(cargo.size()<2) {
                    System.out.println("\nInvalid choice: " + chose + " - this cargo container does not exist");
                    goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    System.out.print("Chose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap= cargo.get(1);
                currentPlayer.getShipBoard().removeGood(cargo.get(1), coordinatesToPut);
                break;
            case 3:
                if(cargo.size()<3) {
                    System.out.println("\nInvalid choice: " + chose + " - this cargo container does not exist");
                    goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    System.out.print("Chose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap= cargo.get(2);
                currentPlayer.getShipBoard().removeGood(cargo.get(2), coordinatesToPut);
                break;
            default:
                System.out.println("\nInvalid choice: " + chose + " - this cargo container does not exist");
                goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                System.out.print("Chose what good to swap, input 'no' to stop\n");
                return;
        }
        if(state ==1) {
            currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet - 1), coordinatesToPut);
            possibleGoodsGain.remove(goodsToGet - 1);
        }
        possibleGoodsGain.add(goodToSwap);
        System.out.print("\nCargoHold"+coordinatesToPut+":\n");
        goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        System.out.print("Planet:\n");
        goodsPrinter(possibleGoodsGain);
        System.out.print("Chose for each good where to put it, input 'done' to stop,'pick  x y' to pick one good from your cargo:\n\n");
        state =0;
    }

    public String goodsPrinter(ArrayList<Goods> goodsArray) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED= "\u001B[31m";

        StringBuilder sb= new StringBuilder();

        AtomicInteger i = new AtomicInteger(1);
        goodsArray.forEach(goods -> {
            if(goods.getColor()==GoodsColor.BLUE){
                sb.append(i).append(" - ").append(ANSI_BLUE).append(goods.getColor()).append(" ").append(ANSI_RESET).append("good: it equals to ").append(goods.getValue()).append(" cosmic credits\n");
            }else if(goods.getColor()==GoodsColor.GREEN){
                sb.append(i).append(" - ").append(ANSI_GREEN).append(goods.getColor()).append(" ").append(ANSI_RESET).append("good: it equals to ").append(goods.getValue()).append(" cosmic credits\n");
            }else if(goods.getColor()==GoodsColor.YELLOW){
                sb.append(i).append(" - ").append(ANSI_YELLOW).append(goods.getColor()).append(" ").append(ANSI_RESET).append("good: it equals to ").append(goods.getValue()).append(" cosmic credits\n");
            }else if(goods.getColor()==GoodsColor.RED){
                sb.append(i).append(" - ").append(ANSI_RED).append(goods.getColor()).append(" ").append(ANSI_RESET).append("good: it equals to ").append(goods.getValue()).append(" cosmic credits\n");
            }
            i.getAndIncrement();
        });
        return sb.toString();
    }
}