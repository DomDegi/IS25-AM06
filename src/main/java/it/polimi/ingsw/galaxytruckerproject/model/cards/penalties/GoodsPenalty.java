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

/**
 * Represents a penalty where the player must discard a number of goods, potentially replacing
 * some of them with batteries if not enough goods are available.
 */
public class GoodsPenalty extends Penalty {
    private final int numberOfLostGoods;
    private int numberOfGoods = 0;
    private int numberOfBatteries = 0;

    /**
     * Constructs a GoodsPenalty with the specified number of goods to be lost.
     *
     * @param numberOfLostGoods the total number of goods to be removed
     */
    @JsonCreator
    public GoodsPenalty(@JsonProperty("numberOfLostGoods") int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
    }

    /**
     * Returns the number of goods the player is required to lose.
     *
     * @return number of lost goods
     */
    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    /**
     * Removes the specified goods and optionally batteries from the player's ship.
     *
     * @param player     the affected player
     * @param playersView the view to notify
     * @param toRemove   list of coordinates to remove
     * @return list of updated tiles after removal
     */
    @Override
    public ArrayList<Tile> removeGoods(Player player, VirtualView playersView, ArrayList<Coordinates> toRemove){
        ArrayList<Tile> updatedTiles;
        if (toRemove.size() != numberOfLostGoods && toRemove.size() != numberOfBatteries + numberOfGoods) {
            try {
                playersView.showWrongInputMessage();
            }catch(Exception ignored) {}
            return null;
        }
        if (numberOfGoods == numberOfLostGoods) {
            updatedTiles = player.removeGoods(toRemove);
        }
        else {
            ArrayList<Coordinates> goodsToRemove = new ArrayList<>(toRemove.subList(0, Math.min(numberOfLostGoods, numberOfGoods)));
            ArrayList<Coordinates> batteriesToRemove = new ArrayList<>(toRemove.subList(numberOfGoods, toRemove.size()));
            updatedTiles = player.removeGoods(goodsToRemove);
            updatedTiles.addAll(player.chooseBatteriesUse(batteriesToRemove));
        }
        return updatedTiles;
    }

    /**
     * Automatically removes goods and/or batteries for disconnected players.
     *
     * @param disconnectedPlayer the disconnected player
     * @param view               the interface to notify
     * @return list of updated tiles
     */
    @Override
    public ArrayList<Tile> automaticGoodsPenalty(Player disconnectedPlayer, ViewInterface view) {
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

    /**
     * Automatically removes goods from the ship of a disconnected player.
     *
     * @param disconnectedPlayer the player whose goods to remove
     * @param number             number of goods to remove
     * @return list of updated tiles after removal
     */
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

    /**
     * Automatically removes batteries from the ship of a disconnected player.
     *
     * @param disconnectedPlayer the player whose batteries to remove
     * @param number             number of batteries to remove
     * @return list of updated tiles after removal
     */
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

    /**
     * Returns a string representation of this penalty.
     *
     * @return the description string
     */
    @Override
    public String toString() {
        return "GoodsPenalty " +  numberOfLostGoods;
    }

    /**
     * Returns the number of goods to remove.
     *
     * @return the number of goods
     */
    public int getNumber(){
        return numberOfLostGoods;
    }

    /**
     * Initializes the penalty, determining how many goods and/or batteries will be removed,
     * and prompts the player to select them if connected.
     *
     * @param game   the game interface
     * @param view   the view interface
     * @param player the player subject to the penalty
     * @return true if player input is expected, false otherwise
     */
    @Override
    public boolean initializePenalty(GameInterface game,VirtualView view, Player player) {
        if (player.getShipBoard().isCargoEmpty()) {
            if (player.getShipBoard().getNumBatteries() == 0) {
                return false;
            }
            this.numberOfBatteries = Math.min(player.getShipBoard().getNumBatteries(), numberOfLostGoods);
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
            if (player.IsDisconnected()) {
                game.getDrawnCard().notifyModifiedTiles(player.getPlayerName(), automaticGoodsPenalty(player, view));
                return false;
            }
        }
        if (player.IsDisconnected()) {
            game.getDrawnCard().notifyModifiedTiles(player.getPlayerName(), automaticGoodsPenalty(player, view));
            return false;
        }
        else {
            try {
                view.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
            }catch(Exception ignored) {}
        }
        return true;
    }
}
