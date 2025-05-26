package it.polimi.ingsw.galaxytruckerproject.model.goods;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class responsible for validating and applying updates to the cargo hold of a {@link Player}'s {@link ShipBoard}.
 * It checks for consistency between the client-provided cargo update and the server-side shipboard.
 */
public class GoodsChecker {
    /** The list of goods that can potentially be gained during the current event. */
    private final ArrayList<Goods> possibleGoodsGain;

    /** The player whose cargo is being checked. */
    private final Player currentPlayer;

    /** The coordinates of all cargo hold tiles in the player's shipboard. */
    private final ArrayList<Coordinates> playersCargo;

    /** The maximum credit value the player could theoretically reach with current + new goods. */
    private int maximumPossibleValue;

    /**
     * Constructs a new {@code GoodsChecker} instance for a given player and list of goods potentially obtained.
     *
     * @param currentPlayer the player whose cargo will be validated
     * @param possibleGoodsGain the list of goods that could be added to the player's cargo
     */
    public GoodsChecker(Player currentPlayer, ArrayList<Goods> possibleGoodsGain) {
        this.currentPlayer=currentPlayer;
        this.possibleGoodsGain=possibleGoodsGain;
        this.playersCargo= currentPlayer.getShipBoard().getCargoHoldCoordinates();
        this.setMaximumValue();
    }

    /**
     * Verifies that the client's updated cargo state is valid and consistent with the server's current cargo state.
     * The check includes:
     * <ul>
     *   <li>The total value must not exceed the theoretical maximum</li>
     *   <li>The coordinates must match known cargo hold positions</li>
     *   <li>Each cargo hold must match in size and hazard status</li>
     *   <li>Non-hazardous cargo must not contain hazardous (RED) goods</li>
     * </ul>
     * If all checks pass, the updated cargo is applied to the server-side shipboard.
     *
     * @param clientValue the total value of goods as calculated on the client
     * @param updatedCargo the cargo holds provided by the client for update
     * @return {@code true} if the update is valid and applied; {@code false} otherwise
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

    /**
     * Replaces the cargo holds on the server-side shipboard with those provided from the client.
     *
     * @param updatedCargo the list of updated cargo hold tiles to apply to the shipboard
     */
    public void swapGoods(ArrayList<CargoHold> updatedCargo) {
        ShipBoard playerShip = currentPlayer.getShipBoard();
        for (CargoHold newCargo : updatedCargo) {
            Coordinates updatedCargoCoordinates = newCargo.getCoordinates();
            playerShip.updateTile(updatedCargoCoordinates, newCargo);
        }
    }


    /**
     * Computes the maximum possible cargo value the player could legally hold,
     * based on their current cargo plus the list of newly available goods.
     */
    private void setMaximumValue() {
        int value= currentPlayer.getShipBoard().convertGoodsToCredit();
        for (Goods goods : possibleGoodsGain) {
            value += goods.getValue();
        }
        this.maximumPossibleValue=value;
    }

    /**
     * Builds a color-coded, numbered string representation of a list of goods,
     * showing their color and value in cosmic credits.
     *
     * @param goodsArray the list of goods to display
     * @return a formatted string representing the goods and their values
     */
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