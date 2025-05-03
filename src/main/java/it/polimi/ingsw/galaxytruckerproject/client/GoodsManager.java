package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoRed;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;

public class GoodsManager {
    private final ArrayList<Goods> possibleGoodsGain;
    private Coordinates coordinatesToPut;
    private final LightPlayer currentPlayer;
    private int state;
    private int goodsToGet;
    private final HashSet<CargoHold> changes;
    private final ViewInterface view;

    public GoodsManager(LightPlayer currentPlayer, ArrayList<Goods> possibleGoodsGain, ViewInterface view) {
        this.view=view;
        this.currentPlayer=currentPlayer;
        this.changes=new HashSet<>();
        this.possibleGoodsGain=possibleGoodsGain;
        this.state=0;
        this.goodsToGet=0;
        this.coordinatesToPut=new Coordinates(0,0);
    }

    public HashSet<CargoHold> getReward(String[] input) throws RemoteException {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED= "\u001B[31m";
        if(state==0) {
            int CoordinatesX;
            int CoordinatesY;
            if (input.length == 0) {
                view.wrongLocalInput();
                return new HashSet<>();
            }
            if (input[0].equalsIgnoreCase("done") || input[0].equalsIgnoreCase("0")) {
               view.showGenericMessage("\nYou stopped positioning your cargo\n");
                if (changes.isEmpty()) {
                    changes.add(new CargoRed(-1,null,null,null,null));
                }
                return changes;
            }
            if (input.length < 3) {
                view.wrongLocalInput();
                return null;
            }
            try {
                CoordinatesX = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                view.wrongLocalInput();
                return null;
            }
            try {
                CoordinatesY = Integer.parseInt(input[2]);
            } catch (NumberFormatException e) {
                view.wrongLocalInput();
                return null;
            }
            coordinatesToPut = new Coordinates(CoordinatesX, CoordinatesY);
            if (input[0].equalsIgnoreCase("pick")) {
                if (currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut).isEmpty()) {
                    view.showGenericMessage("\nCargo Hold is empty");
                    return null;
                }
                view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                view.showGenericMessage("Input witch good to pick:");
                changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
                state = -1;
                return null;
            }
            try {
                goodsToGet = Integer.parseInt(input[0]);
            } catch (NumberFormatException e) {
                view.showGenericMessage("\nInvalid input format. Please provide integer values.");
                return null;
            }

            if (goodsToGet > 0 && goodsToGet <=possibleGoodsGain.size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet - 1), coordinatesToPut);
                if (positioned == 0) {
                    if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.BLUE){
                        view.showGenericMessage("\nYou've successfully put the "+ ANSI_BLUE +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo\n\n");
                    }else if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.GREEN){
                        view.showGenericMessage("\nYou've successfully put the "+ ANSI_GREEN +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo\n\n");
                    }else if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.YELLOW){
                        view.showGenericMessage("\nYou've successfully put the "+ ANSI_YELLOW +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo\n\n");
                    }else if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.RED){
                        view.showGenericMessage("\nYou've successfully put the "+ ANSI_RED +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo\n\n");
                    }
                    changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
                    possibleGoodsGain.remove(goodsToGet - 1);
                    if(possibleGoodsGain.isEmpty()) {
                        view.showGenericMessage("\nGoods stock is empty, input 'done' to stop,'pick  x y' to pick one good from your cargo: ");
                        return null;
                    }
                    view.goodsPrinter(possibleGoodsGain);
                    view.showGenericMessage("Chose for each good where to put it, input 'no' to stop:\n");
                    return null;
                } else if (positioned == 1) {
                    if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.BLUE){
                        view.showGenericMessage("\nSorry, you can't put the "+ ANSI_BLUE +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n");
                    }else if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.GREEN){
                        view.showGenericMessage("\nSorry, you can't put the "+ ANSI_GREEN +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n");
                    }else if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.YELLOW){
                        view.showGenericMessage("\nSorry, you can't put the "+ ANSI_YELLOW +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n");
                    }else if(possibleGoodsGain.get(goodsToGet - 1).getColor()==GoodsColor.RED){
                        view.showGenericMessage("\nSorry, you can't put the "+ ANSI_RED +possibleGoodsGain.get(goodsToGet - 1).getColor()+ ANSI_RESET+" good in the "+coordinatesToPut.getX()+","+coordinatesToPut.getY()+" cargo, it is full, chose what good to remove (input 'no' to select an other good and cargo coordinates)\n");
                    }
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
                    state = 1;
                    return null;
                } else if (positioned == -1) {
                    return null;
                }
            } else {
                view.wrongLocalInput();
                return null;
            }
        }else {
            swapGoods(input);
        }
        return null;
    }

    public void swapGoods(String[] input) throws RemoteException {
        int chose;
        if (input.length==0) {
            view.wrongLocalInput();
            return ;
        }
        if (input[0].equalsIgnoreCase("no")) {
            input[0] = "0";
        }
        try {
            chose = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            view.wrongLocalInput();
            return ;
        }
        ArrayList<Goods> cargo = currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut);
        Goods goodToSwap;
        switch (chose){
            case 0:
                view.showGenericMessage("\nNo action performed, select a good to remove from the cargo\n");
                state =0;
                return;
            case 1:
                if(cargo.isEmpty()) {
                    view.wrongLocalInput();
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("Chose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap= cargo.getFirst();
                currentPlayer.getShipBoard().removeGood(cargo.getFirst(), coordinatesToPut);
                break;
            case 2:
                if(cargo.size()<2) {
                    view.wrongLocalInput();
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("Chose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap= cargo.get(1);
                currentPlayer.getShipBoard().removeGood(cargo.get(1), coordinatesToPut);
                break;
            case 3:
                if(cargo.size()<3) {
                    view.wrongLocalInput();
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("Chose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap= cargo.get(2);
                currentPlayer.getShipBoard().removeGood(cargo.get(2), coordinatesToPut);
                break;
            default:
                view.wrongLocalInput();
                view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                view.showGenericMessage("Chose what good to swap, input 'no' to stop\n");
                return;
        }
        if(state ==1) {
            currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet - 1), coordinatesToPut);
            possibleGoodsGain.remove(goodsToGet - 1);
        }
        possibleGoodsGain.add(goodToSwap);
        view.showGenericMessage("\nCargoHold"+coordinatesToPut+":\n");
        view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        view.showGenericMessage("Planet:\n");
        view.goodsPrinter(possibleGoodsGain);
        view.showGenericMessage("Chose for each good where to put it, input 'done' to stop,'pick  x y' to pick one good from your cargo:\n\n");
        state =0;
    }

}