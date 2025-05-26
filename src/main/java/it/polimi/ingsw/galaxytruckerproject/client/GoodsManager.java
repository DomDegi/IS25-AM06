package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoRed;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class responsible for managing the goods and cargo on the player's ship. It handles
 * the selection, placement, and swapping of goods in cargo holds. The class also tracks
 * the state of the goods management process and communicates with the player through
 * the {@link DisplayableView}.
 * <p>
 * This class manages the goods gain and positioning, including interactions such as
 * selecting a good, swapping goods, and receiving rewards.
 * </p>
 */
public class GoodsManager {
    /**
     * A list of possible goods the player can gain. This list contains the goods
     * that are available for the player to place in their cargo hold.
     */
    private final ArrayList<Goods> possibleGoodsGain;

    /**
     * The coordinates of the cargo hold where the player wants to place or remove goods.
     */
    private Coordinates coordinatesToPut;

    /**
     * The light player object representing the current player. This object is used
     * to access the player's shipboard and interact with their current state in the game.
     */
    private final LightPlayer currentPlayer;

    /**
     * The current state of the goods management process. This state controls the actions
     * the player can perform, such as selecting goods, swapping goods, or placing goods.
     */
    private int state;

    /**
     * The number of goods that the player needs to get. This value is updated based on the
     * current action, such as removing goods or placing new goods into cargo holds.
     */
    private int goodsToGet;

    /**
     * A set of cargo holds that have been modified during the goods management process.
     * This is used to track changes to the player's cargo.
     */
    private final HashSet<CargoHold> changes;

    /**
     * The view used to display messages and interact with the player. It provides methods
     * to show the current state of the game and prompt the player for input.
     */
    private final DisplayableView view;

    /**
     * Constructs a new {@link GoodsManager} for the specified player and view.
     *
     * @param currentPlayer the player whose goods are being managed
     * @param possibleGoodsGain the list of goods available to gain
     * @param view the view to display messages to the user
     */

    public GoodsManager(LightPlayer currentPlayer, ArrayList<Goods> possibleGoodsGain, DisplayableView view) {
        this.view = view;
        this.currentPlayer = currentPlayer;
        this.changes = new HashSet<>();
        this.possibleGoodsGain = possibleGoodsGain;
        this.state = 0;
        this.goodsToGet = 0;
        this.coordinatesToPut = new Coordinates(0, 0);
    }

    /**
     * Ends the goods management process and returns the changes made to the cargo holds.
     *
     * @return a set of changes made to the cargo holds
     */
    public HashSet<CargoHold> doneGoods() {
        view.showGenericMessage("\nYou stopped positioning your cargo\n");
        if (changes.isEmpty()) {
            changes.add(new CargoRed(-1, null, null, null, null).send());
        }
        return changes;
    }

    /**
     * Chooses the good to be placed in the cargo hold.
     *
     * @param chose the index of the chosen good
     */
    public void chooseGoods(int chose) {
        if (state == 0) {
            this.goodsToGet = chose;
        } else {
            swapGoods(chose);
        }
    }

    /**
     * Picks a good from the cargo hold for the player.
     */
    public void pickGoods() {
        if (currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut).isEmpty()) {
            view.showGenericMessage("\nCargo Hold is empty");
        }
        view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        view.showGenericMessage("Input which good to pick:");
        changes.add((CargoHold) currentPlayer.getShipBoard().getTile(coordinatesToPut).send());
        state = -1;
    }

    /**
     * Chooses the coordinates where the good will be placed.
     *
     * @param coordinates the coordinates to place the good
     */
    public void chooseCargo(Coordinates coordinates) {
        if (currentPlayer.getShipBoard().getTile(coordinates) == null) {
            view.wrongLocalInput();
            return;
        }
        view.cargoSelected(coordinates);
        coordinatesToPut = coordinates;
        if (goodsToGet == 0)
            pickGoods();
        else
            getReward();
    }

    /**
     * Places the chosen good in the cargo hold and updates the available goods.
     */
    public void getReward() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED = "\u001B[31m";
        if (state == 0) {
            if (goodsToGet > 0 && goodsToGet <= possibleGoodsGain.size()) {
                int positioned = currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet - 1), coordinatesToPut);
                if (positioned == 0) {
                    if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.BLUE) {
                        view.showGenericMessage("\nYou've successfully put the " + ANSI_BLUE + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo\n\n");
                    } else if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.GREEN) {
                        view.showGenericMessage("\nYou've successfully put the " + ANSI_GREEN + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo\n\n");
                    } else if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.YELLOW) {
                        view.showGenericMessage("\nYou've successfully put the " + ANSI_YELLOW + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo\n\n");
                    } else if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.RED) {
                        view.showGenericMessage("\nYou've successfully put the " + ANSI_RED + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo\n\n");
                    }
                    changes.add((CargoHold) currentPlayer.getShipBoard().getTile(coordinatesToPut).send());
                    possibleGoodsGain.remove(goodsToGet - 1);
                    if (possibleGoodsGain.isEmpty()) {
                        view.showGenericMessage("\nGoods stock is empty, input 'done' to stop,'x y' to pick one good from your cargo: ");
                        goodsToGet = 0;
                        return;
                    }
                    view.goodsPrinter(possibleGoodsGain);
                    view.showGenericMessage("Choose for each good where to put it, input 'no' to stop:\n");
                    goodsToGet = 0;
                    return;
                } else if (positioned == 1) {
                    if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.BLUE) {
                        view.showGenericMessage("\nSorry, you can't put the " + ANSI_BLUE + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo, it is full, choose what good to remove (input 'no' to select another good and cargo coordinates)\n");
                    } else if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.GREEN) {
                        view.showGenericMessage("\nSorry, you can't put the " + ANSI_GREEN + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo, it is full, choose what good to remove (input 'no' to select another good and cargo coordinates)\n");
                    } else if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.YELLOW) {
                        view.showGenericMessage("\nSorry, you can't put the " + ANSI_YELLOW + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo, it is full, choose what good to remove (input 'no' to select another good and cargo coordinates)\n");
                    } else if (possibleGoodsGain.get(goodsToGet - 1).getColor() == GoodsColor.RED) {
                        view.showGenericMessage("\nSorry, you can't put the " + ANSI_RED + possibleGoodsGain.get(goodsToGet - 1).getColor() + ANSI_RESET + " good in the " + coordinatesToPut.getX() + "," + coordinatesToPut.getY() + " cargo, it is full, choose what good to remove (input 'no' to select another good and cargo coordinates)\n");
                    }
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    changes.add((CargoHold) currentPlayer.getShipBoard().getTile(coordinatesToPut).send());
                    state = 1;
                    goodsToGet = 0;
                    return;
                } else if (positioned == -1) {
                    view.wrongLocalInput();
                    goodsToGet = 0;
                    return;
                }
            } else {
                view.wrongLocalInput();
                goodsToGet = 0;
                return;
            }
        } else {
            view.wrongLocalInput();
            goodsToGet = 0;
            return;
        }
        goodsToGet = 0;
    }

    /**
     * Swaps the chosen good with one already in the cargo hold.
     *
     * @param chose the index of the chosen good to swap
     */
    public void swapGoods(int chose) {
        ArrayList<Goods> cargo = currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut);
        if (cargo.isEmpty()) {
            state = 0;
            getReward();
            return;
        }
        view.showGenericMessage("You're trying to swap the goods\n");

        Goods goodToSwap;
        switch (chose) {
            case 0:
                view.showGenericMessage("\nNo action performed, select a good to remove from the cargo\n");
                state = 0;
                return;
            case 1:
                if (cargo.isEmpty()) {
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("I shouldn't be here\n");
                    return;
                }
                goodToSwap = cargo.get(0);
                currentPlayer.getShipBoard().removeGood(cargo.get(0), coordinatesToPut);
                break;
            case 2:
                if (cargo.size() < 2) {
                    view.wrongLocalInput();
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("Choose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap = cargo.get(1);
                currentPlayer.getShipBoard().removeGood(cargo.get(1), coordinatesToPut);
                break;
            case 3:
                if (cargo.size() < 3) {
                    view.wrongLocalInput();
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("Choose what good to swap, input 'no' to stop\n");
                    return;
                }
                goodToSwap = cargo.get(2);
                currentPlayer.getShipBoard().removeGood(cargo.get(2), coordinatesToPut);
                break;
            default:
                view.wrongLocalInput();
                view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                view.showGenericMessage("Choose what good to swap, input 'no' to stop\n");
                return;
        }
        if (state == 1) {
            currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet), coordinatesToPut);
            possibleGoodsGain.remove(goodsToGet);
        }
        possibleGoodsGain.add(goodToSwap);
        view.showGenericMessage("\nCargoHold" + coordinatesToPut + ":\n");
        view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        view.showGenericMessage("Planet:\n");
        view.goodsPrinter(possibleGoodsGain);
        view.showGenericMessage("Choose for each good where to put it [first the good, then x y], input 'done' to stop, 'x y' to pick one good from your cargo:\n\n");
        state = 0;
    }
}
