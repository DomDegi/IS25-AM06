package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

public class GoodsPenalty extends Penalty {
    private final int numberOfLostGoods;
    private int numberOfGoods = 0;
    private int numberOfBatteries = 0;

    @JsonCreator
    public GoodsPenalty(@JsonProperty("numberOfLostGoods") int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    @Override
    public ArrayList<Tile> removeGoods(Player player, VirtualView playersView, ArrayList<Coordinates> toRemove){
        ArrayList<Tile> updatedTiles;
        if (toRemove.size() != numberOfLostGoods && toRemove.size() != numberOfBatteries + numberOfGoods) {
            playersView.showWrongInputMessage();
            return null;
        }
        if (numberOfGoods == numberOfLostGoods) {
            updatedTiles = player.removeGoods(toRemove);
        }
        else {
            ArrayList<Coordinates> goodsToRemove = new ArrayList<>(toRemove.subList(0, numberOfLostGoods));
            ArrayList<Coordinates> batteriesToRemove = new ArrayList<>(toRemove.subList(numberOfGoods, toRemove.size()));
            updatedTiles = player.removeGoods(goodsToRemove);
            updatedTiles.addAll(player.chooseBatteriesUse(batteriesToRemove));
        }
        return updatedTiles;
    }

    @Override
    public ArrayList<Tile> automaticGoodsPenalty(GameInterface game, Player disconnectedPlayer, ViewInterface view) {
        if (numberOfGoods == numberOfLostGoods) {
            return automaticRemoveGoods(disconnectedPlayer, numberOfLostGoods);
        }
        else {
            ArrayList<Tile> updated = new ArrayList<>();
            updated.addAll(automaticRemoveGoods(disconnectedPlayer, numberOfGoods));
            updated.addAll(automaticRemoveBatteries(disconnectedPlayer, numberOfBatteries));
            return updated;
        }
    }

    public ArrayList<Tile> automaticRemoveGoods (Player disconnectedPlayer, int number) {
        int counter = number;
        ShipBoard playerShip = disconnectedPlayer.getShipBoard();

        ArrayList<Coordinates> redGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED));
        ArrayList<Coordinates> yellowGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW));
        ArrayList<Coordinates> greenGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN));
        ArrayList<Coordinates> blueGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE));

        Coordinates currentTile;
        ArrayList<Tile> updatedTiles = new ArrayList<>();
        while (counter > 0) {
            if (!redGoods.isEmpty()) {
                currentTile = redGoods.removeFirst();
                playerShip.removeGood(new Goods(GoodsColor.RED), currentTile);
            } else if (!yellowGoods.isEmpty()) {
                currentTile = yellowGoods.removeFirst();
                playerShip.removeGood(new Goods(GoodsColor.YELLOW), currentTile);
            } else if (!greenGoods.isEmpty()) {
                currentTile = greenGoods.removeFirst();
                playerShip.removeGood(new Goods(GoodsColor.GREEN), currentTile);
            } else if (!blueGoods.isEmpty()) {
                currentTile = blueGoods.removeFirst();
                playerShip.removeGood(new Goods(GoodsColor.BLUE), currentTile);
            }
            else {
                return null;
            }
            if (!updatedTiles.contains(playerShip.getTile(currentTile))) {
                updatedTiles.add(playerShip.getTile(currentTile));
            }
            counter--;
        }
        return updatedTiles;
    }

    public ArrayList<Tile> automaticRemoveBatteries (Player disconnectedPlayer, int number) {
        ArrayList<Tile> updatedTiles = new ArrayList<>();
        Coordinates currentTile;
        ArrayList<Coordinates> batteryComponents = disconnectedPlayer.getShipBoard().getBatteryCoordinates();
        for (int i = 0; i < number; i++) {
            currentTile = batteryComponents.getFirst();
            disconnectedPlayer.getShipBoard().chooseBatteryUse(currentTile);
            if (!updatedTiles.contains(disconnectedPlayer.getShipBoard().getTile(currentTile))) {
                updatedTiles.add(disconnectedPlayer.getShipBoard().getTile(currentTile));
            }
        }
        return updatedTiles;
    }



    @Override
    public String toString() {
        return "GoodsPenalty " +  numberOfLostGoods;
    }

    public int getNumber(){
        return numberOfLostGoods;
    }

    @Override
    public boolean initializePenalty(VirtualView view, Player player) {
        if (player.getShipBoard().isCargoEmpty()) {
            if (player.getShipBoard().getNumBatteries() == 0) {
                return false;
            }
            this.numberOfBatteries = Math.min(player.getShipBoard().getNumBatteries(), numberOfLostGoods);
            view.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
        }
        else {
            if (player.getShipBoard().getAllGoods().size() < numberOfLostGoods) {
                this.numberOfGoods = player.getShipBoard().getAllGoods().size();
                this.numberOfBatteries = numberOfLostGoods - player.getShipBoard().getAllGoods().size();
                if (numberOfBatteries > player.getShipBoard().getNumBatteries()) {
                    numberOfBatteries = player.getShipBoard().getNumBatteries();
                }
            }
            else {
                this.numberOfGoods = numberOfLostGoods;
            }
            view.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
        }
        return true;
    }
}
