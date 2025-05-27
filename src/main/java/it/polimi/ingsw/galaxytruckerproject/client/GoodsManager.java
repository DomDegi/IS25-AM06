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

/**
 * The GoodsManager class is responsible for managing the operations related to goods in the game.
 * It handles the allocation, swapping, and placement of goods in the player's cargo.
 * This class interacts with the player's ship board, manages game state transitions,
 * and communicates relevant updates or errors to the display view.
 */
public class GoodsManager {
    /**
     * Represents a collection of goods that can potentially be acquired by the player.
     * This list is dynamically updated based on the game state and the player's actions.
     * Goods in this list are instances of the {@link Goods} class, which define their
     * attributes such as color and value.
     *
     * This variable is used to manage available goods during gameplay, allowing the
     * player to choose and interact with these goods as part of game mechanics.
     */
    private final ArrayList<Goods> possibleGoodsGain;
    /**
     * Stores the coordinates where the player intends to place a good on the shipboard.
     * This variable is used during the shipboard management phase to track the target
     * location for placing goods.
     */
    private Coordinates coordinatesToPut;
    /**
     * Represents the current player interacting with the goods management system.
     * This is a reference to the `LightPlayer` instance that is actively making
     * decisions or performing actions related to goods acquisition or management
     * within the `GoodsManager` class.
     */
    private final LightPlayer currentPlayer;
    /**
     * Represents the current state of the game or player's progress in the GoodsManager.
     * The state can indicate transitions between different actions or phases during
     * gameplay. This variable may be influenced or updated based on methods that
     * manage the player's interactions with goods, cargo, and rewards.
     */
    private int state;
    /**
     * The number of goods that the current player is required to obtain during the
     * relevant game phase or action.
     *
     * This variable represents a numeric value tracked within the game logic for managing
     * the player's progress or choices, specifically related to acquiring goods. It is
     * dynamically updated during gameplay depending on the game's state and player actions.
     */
    private int goodsToGet;
    /**
     * Holds the set of CargoHold objects that have been modified.
     * It represents the ongoing changes related to the cargo holds
     * during the current game phase or operation in the GoodsManager.
     * This set is typically updated when cargo holds are altered or interacted with.
     */
    private final HashSet<CargoHold> changes;
    /**
     * Represents the view associated with the GoodsManager, allowing interaction with
     * the display of game-related elements for the current player. The DisplayableView
     * interface provides methods to handle updates to the game interface, such as showing
     * tiles, cards, flightboards, and other elements, as well as managing player interactions.
     * This variable is final and initialized during the GoodsManager construction.
     */
    private final DisplayableView view;
    /**
     * Represents the collection of cargo currently held in a player's cargo hold.
     *
     * This variable stores an {@code ArrayList} of {@link Goods} objects that
     * the player has collected during the game. The goods in the cargo can be
     * managed, swapped, or used as part of the game's mechanics.
     *
     * The cargo is manipulated through specific methods within the
     * {@code GoodsManager} class, allowing for adding, swapping,
     * and retrieving goods as needed during gameplay.
     */
    private ArrayList<Goods> cargo;

    /**
     * Initializes a new instance of the {@code GoodsManager} class.
     *
     * This class is responsible for handling the operations associated with managing
     * goods in the game, including selecting, picking, swapping, and storing goods.
     *
     * @param currentPlayer the current player who is interacting with the goods
     * @param possibleGoodsGain a list of goods that the player might be able to acquire
     * @param view the displayable view associated with the goods management UI
     */
    public GoodsManager(LightPlayer currentPlayer, ArrayList<Goods> possibleGoodsGain, DisplayableView view) {
        this.view=view;
        this.currentPlayer=currentPlayer;
        this.changes=new HashSet<>();
        this.possibleGoodsGain=possibleGoodsGain;
        this.state=0;
        this.goodsToGet=0;
        this.coordinatesToPut=new Coordinates(0,0);
    }
    /**
     * Stops the positioning of cargo and finalizes any necessary changes
     * to the tracked cargo holds. If there are no changes to process,
     * a default empty cargo hold is added as a fallback.
     *
     * @return the set of finalized or modified cargo holds
     */
    public HashSet<CargoHold> doneGoods(){
        view.showGenericMessage("\nYou stopped positioning your cargo\n");
        if (changes.isEmpty()) {
            changes.add(new CargoRed(-1,null,null,null,null).send());
        }
        return changes;
    }

    /**
     * Allows the player to choose a good from the available options or to swap goods
     * if the current state permits swapping.
     *
     * @param chose an integer representing the index or option for the good the player
     *              wants to select or swap. The behavior depends on the current state:
     *              - If the state is 0, the selected good is assigned to be acquired.
     *              - Otherwise, the swapGoods method is invoked to handle the selection.
     */
    public void chooseGoods(int chose) {
        if (state == 0) {
            this.goodsToGet = chose;
        }else {
            swapGoods(chose);
        }
    }

    /**
     * Allows the current player to select and pick goods from a specific cargo hold
     * on their shipboard at a predefined location specified by the coordinatesToPut field.
     * The method performs the following steps:
     *
     * 1. Checks if the cargo hold at the given coordinates is empty. If it is,
     *    a message stating "Cargo Hold is empty" is displayed, and the method terminates.
     * 2. Displays the list of goods available in the cargo hold using the goodsPrinter
     *    method of the view.
     * 3. Prompts the player to input which specific good to pick from the cargo hold.
     * 4. Creates a clone of the cargo hold using its send method and adds it to the
     *    changes list.
     * 5. Updates the state of the game to -1.
     *
     * The method interacts with various player and shipboard components, as well as the
     * view, to facilitate this process.
     */
    public void pickGoods() {
        if (currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut).isEmpty()) {
            view.showGenericMessage("\nCargo Hold is empty");
        }
        view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        view.showGenericMessage("Input witch good to pick:");
        changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut).send());
        state = -1;
    }

    /**
     * Allows a player to select a cargo at the specified coordinates on their shipboard.
     * If no tile exists at the specified coordinates, an error message is displayed.
     * If goods are present at the specified cargo hold and no goods have been selected,
     * initiates the process to pick goods. Otherwise, moves to the reward phase.
     *
     * @param coordinates the coordinates of the cargo tile the player wants to select
     */
    public void chooseCargo(Coordinates coordinates) {
        if (currentPlayer.getShipBoard().getTile(coordinates)==null) {
            view.wrongLocalInput();
            return;
        }
        view.cargoSelected(coordinates);
        coordinatesToPut=coordinates;
        if(goodsToGet==0&&currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut)!=null&&!currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut).isEmpty())
            pickGoods();
        else
            getReward();
    }

    /**
     * Handles the reward process by allowing the current player to place goods in the cargo hold.
     * It validates the state of the game, the goods to be placed, and their placement in the shipboard.
     * Provides feedback to the player through messages displayed on the associated view.
     *
     * Behavior:
     * - Checks if the game state allows for placing goods.
     * - If the goods can be placed in the specified cargo hold:
     *   - Adds the goods to the cargo hold.
     *   - Displays a success message indicating the placement.
     *   - Removes the goods from the list of possible goods to gain.
     *   - Updates the goods display and prompts for further actions if the list is not empty.
     * - If the cargo hold is full:
     *   - Displays an error message prompting the user to decide how to handle the situation.
     *   - Displays the goods currently in the cargo hold.
     *   - Updates the game state to allow for further actions related to swapping goods.
     * - If an invalid action or input occurs:
     *   - Displays an error message indicating the invalid input.
     *   - Resets the goods selection process.
     *
     * This method ensures that the goods placement follows the game rules and maintains the state
     * of the cargo hold while providing the player with relevant information and options.
     */
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
                    changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut).send());
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
                    changes.add((CargoHold)currentPlayer.getShipBoard().getTile(coordinatesToPut).send());
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

    /**
     * Allows the player to swap goods from their ship's cargo hold with goods available on a planet.
     * The action performed is determined by the input parameter, and the state is updated accordingly.
     *
     * @param chose the action choice indicating which good to swap:
     *              0 - No action, prompts the player to select a good to remove.
     *              1 - Swap the first good in the cargo.
     *              2 - Swap the second good in the cargo (if available).
     *              3 - Swap the third good in the cargo (if available).
     *              Other values trigger an invalid input message.
     */
    public void swapGoods(int chose) {
        cargo =new ArrayList<>(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        if(cargo.isEmpty())
        {
            state =0;
            getReward();
            return;
        }
        view.showGenericMessage("you're trying to swap the goods\n");

        Goods goodToSwap;
        switch (chose){
            case 0:
                view.showGenericMessage("\nNo action performed, select a good to remove from the cargo\n");
                state =0;
                return;
            case 1:
                if(cargo.isEmpty()) {
                    view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
                    view.showGenericMessage("Non dovrei essere qui\n");
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
            currentPlayer.getShipBoard().gainGoods(possibleGoodsGain.get(goodsToGet), coordinatesToPut);
            possibleGoodsGain.remove(goodsToGet);
        }
        possibleGoodsGain.add(goodToSwap);
        view.showGenericMessage("\nCargoHold"+coordinatesToPut+":\n");
        view.goodsPrinter(currentPlayer.getShipBoard().getSingleCargoGoods(coordinatesToPut));
        view.showGenericMessage("Planet:\n");
        view.goodsPrinter(possibleGoodsGain);
        cargo=null;
        view.showGenericMessage("Chose for each good where to put it[first the good, then x y], input 'done' to stop,'x y' to pick one good from your cargo:\n\n");
        state =0;
    }

    /**
     * Retrieves the list of goods currently stored in the cargo hold.
     *
     * @return an ArrayList containing the {@code Goods} objects representing the current cargo
     */
    public ArrayList<Goods> getCargo() {
        return cargo;
    }
}