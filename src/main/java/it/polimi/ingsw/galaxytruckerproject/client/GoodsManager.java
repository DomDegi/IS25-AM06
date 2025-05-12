package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoRed;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

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
    private final DisplayableView view;

    public GoodsManager(LightPlayer currentPlayer, ArrayList<Goods> possibleGoodsGain, DisplayableView view) {
        this.view=view;
        this.currentPlayer=currentPlayer;
        this.changes=new HashSet<>();
        this.possibleGoodsGain=possibleGoodsGain;
        this.state=0;
        this.goodsToGet=0;
        this.coordinatesToPut=new Coordinates(0,0);
    }
    public HashSet<CargoHold> doneGoods(){
        view.showGenericMessage("\nYou stopped positioning your cargo\n");
        if (changes.isEmpty()) {
            changes.add(new CargoRed(-1,null,null,null,null));
        }
        return changes;
    }

    public void chooseGoods(int chose) {
        if (state == 0) {
            this.goodsToGet = chose;
        }else {
            swapGoods(chose);
        }
    }

    public void pickGoods() {
        if (currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut).isEmpty()) {
            view.showGenericMessage("\nCargo Hold is empty");
        }
        view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        view.showGenericMessage("Input witch good to pick:");
        changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut));
        state = -1;
    }

    public void chooseCargo(Coordinates coordinates) {
        coordinatesToPut=coordinates;
        if(goodsToGet==0)
            pickGoods();
        else
            getReward();
    }

    public void getReward() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED= "\u001B[31m";
        if(state==0) {
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
                        view.showGenericMessage("\nGoods stock is empty, input 'done' to stop,'x y' to pick one good from your cargo: ");
                        goodsToGet=0;
                        return;
                    }
                    view.goodsPrinter(possibleGoodsGain);
                        view.showGenericMessage("Chose for each good where to put it, input 'no' to stop:\n");
                        goodsToGet=0;
                    return;
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
                    goodsToGet=0;
                    return;
                } else if (positioned == -1) {
                    view.wrongLocalInput();
                    goodsToGet=0;
                    return;
                }
            } else {
                view.wrongLocalInput();
                goodsToGet=0;
                return;
            }
        } else {
            view.wrongLocalInput();
            goodsToGet=0;
            return;
        }
        goodsToGet=0;
    }

    public void swapGoods(int chose) {
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
        view.showGenericMessage("Chose for each good where to put it[first the good, then x y], input 'done' to stop,'x y' to pick one good from your cargo:\n\n");
        state =0;
    }
}