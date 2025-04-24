package it.polimi.ingsw.galaxytruckerproject.model.goods;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GoodsChecker {
    private final ArrayList<Goods> possibleGoodsGain;
    private final Player currentPlayer;
    private final ArrayList<Coordinates> playersCargo;
    private int maximumPossibleValue;

    public GoodsChecker(Player currentPlayer, ArrayList<Goods> possibleGoodsGain) {
        this.currentPlayer=currentPlayer;
        this.possibleGoodsGain=possibleGoodsGain;
        this.playersCargo= currentPlayer.getShipBoard().getCargoHoldCoordinates();
        this.setMaximumValue();
    }

    /**
     * checks if the cargoHolds that have been updated in the client are coherent with the ones in the server:
     * - the total value of goods in the client shipboard has to be less of the hypothetical max value obtainable gaining
     *      all the goods in the current event and having space for all of them + the already possessed goods value
     * - same type of cargo
     * - same number of spaces
     * - don't contain hazardous goods (RED) if they don't have the hazard flag
     * @param clientValue credit value of the goods owned by the client's shipboard
     * @param updatedCargo the cargoHolds to swap with the ones on the server to update the server shipboard
     * @return true if the check didn't find anything wrong
     * At the end calls for swap goods to substitute the tile at those coordinates with the new Cargo
     */
    public boolean check(int clientValue, ArrayList<CargoHold> updatedCargo) {
        if (clientValue > maximumPossibleValue) {
            return false;
        }
        for (CargoHold newCargo : updatedCargo) {
            Coordinates updatedCargoCoordinates = newCargo.getCoordinates();
            if (!playersCargo.contains(updatedCargoCoordinates)) {
                return false;
            }
            CargoHold oldCargo = (CargoHold) currentPlayer.getShipBoard().getTile(updatedCargoCoordinates.getX(), updatedCargoCoordinates.getY());
            if (newCargo.getHazard() != oldCargo.getHazard()) {
                return false;
            }
            if (newCargo.getTotSpaces() != oldCargo.getTotSpaces()) {
                return false;
            }
            if (!newCargo.getHazard()) {
                for (Goods goods: newCargo.getCargo()) {
                    if (goods.getColor().equals(GoodsColor.RED)) {
                        return false;
                    }
                }
            }
        }
        this.swapGoods(updatedCargo);
        return true;
    }

    public void swapGoods(ArrayList<CargoHold> updatedCargo) {
        ShipBoard playerShip = currentPlayer.getShipBoard();
        for (CargoHold newCargo : updatedCargo) {
            Coordinates updatedCargoCoordinates = newCargo.getCoordinates();
            playerShip.updateTile(updatedCargoCoordinates, newCargo);
        }
    }


    /**
     * this method finds out what is the hypothetical max obtainable value by the server shipboard
     */
    private void setMaximumValue() {
        int value= currentPlayer.getShipBoard().convertGoodsToCredit();
        for (Goods goods : possibleGoodsGain) {
            value += goods.getValue();
        }
        this.maximumPossibleValue=value;
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