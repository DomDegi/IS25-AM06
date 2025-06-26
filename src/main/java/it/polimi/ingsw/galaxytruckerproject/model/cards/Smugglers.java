package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.GoodsPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;


/**
 * Represents the "Smugglers" enemy card.
 * If a player defeats the smugglers, they receive a set of goods as a reward.
 * If they fail, they must lose a certain number of goods.
 */
public class Smugglers extends Enemies{
    /** Penalty applied when the player loses: number of goods to discard. */
    private final GoodsPenalty lostGoods;
    /** List of goods awarded if the player defeats the smugglers. */
    private final ArrayList<Goods> rewardGoods;
    /** The current player taking their turn against the smugglers. */
    private Player currentPlayer;
    /** View associated with the current player. */
    private VirtualView playersView;
    /** Index of the current player in the in-flight players list. */
    private int playerIndex = -1;
    /** Outcome of the encounter: 1 = win, -1 = loss, 0 = undecided. */
    private int won;
    /** Utility to check the validity of goods configuration after reward assignment. */
    private GoodsChecker goodsChecker;

    /**
     * Creates a new Smugglers card using JSON properties.
     *
     * @param level threat level
     * @param requiredDays number of days lost when taking the reward
     * @param cannonStrength the strength to beat in cannon power
     * @param lostGoods number of goods to lose if failed
     * @param rewardGoods goods to gain if succeeded
     * @param filePath path to the image of the card
     */
    @JsonCreator
    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods,  @JsonProperty("imagePath") String filePath) {
        super(level, requiredDays, cannonStrength, filePath);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.currentPlayer = null;
        this.won = 0;
    }

    /**
     * Alternative constructor without an image path.
     *
     * @param level threat level
     * @param requiredDays number of days lost when taking the reward
     * @param cannonStrength the strength to beat in cannon power
     * @param lostGoods number of goods to lose if failed
     * @param rewardGoods goods to gain if succeeded
     */
    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods) {
        super(level, requiredDays, cannonStrength, null);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.currentPlayer = null;
        this.won = 0;
    }


    /**
     * Initializes the card with game and player views.
     *
     * @param game the game interface
     * @param viewsMap a map of player names to their views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    /**
     * Moves to the next player in the in-flight list to resolve the encounter.
     */
    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = currentPlayer.getPlayerName();
        this.playersView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
        if (singleCannonPower > 0) {
            singleCannonPower = singleCannonPower + 2*currentPlayer.getShipBoard().getNumPurpleAliens();
        }
        won = 0;
        if (currentPlayer.IsDisconnected()) {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            }
            else if (singleCannonPower == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                lostGoods.initializePenalty(game, playersView, currentPlayer);
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                try {
                    playersView.setClientState(ClientState.ACTION);
                }catch(Exception ignored) {}
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    if (!lostGoods.initializePenalty(game,playersView, currentPlayer)) {
                        nextPlayer();
                    }
                    else {
                        try {
                            playersView.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            else {
                try {
                    playersView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                }catch(Exception ignored) {}
            }
        }
    }

    /**
     * Processes the player's cannon attack.
     *
     * @param playerName the name of the player
     * @param doubleCannonPower additional cannon power used
     * @param batteriesToUse batteries activated for the attack
     */
    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        Map<Float,ArrayList<Tile>> returned = player.useCannons(doubleCannonPower, batteriesToUse);
        if (returned == null) {
            if (player.getShipBoard().getSingleCannonPower() > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            }
            else if (player.getShipBoard().getSingleCannonPower()  == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                lostGoods.initializePenalty(game, playersView, currentPlayer);
                nextPlayer();
            }
            return;
        }
        float cannonPower = returned.keySet().iterator().next();
        notifyModifiedTiles(playerName, returned.get(cannonPower));
        if (cannonPower > cannonStrength) {
            won = 1;
            if (currentPlayer.IsDisconnected()) {
                choice(playerName, false);
            }
            else{
                try {
                    playersView.setClientState(ClientState.ACTION);
                }catch(Exception ignored) {}
            }
        }
        else if (cannonPower == cannonStrength) {
            nextPlayer();
        }
        else {
            won = -1;
            if (!lostGoods.initializePenalty(game,playersView, currentPlayer)) {
                nextPlayer();
                return;
            }
            try {
                playersView.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
            }catch(Exception ignored) {}
        }
    }

    /**
     * Handles the player's final decision after winning the encounter.
     *
     * @param playerName name of the player
     * @param decision true to accept the reward, false to skip
     */
    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won != 1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        if (decision) {
            goodsChecker =new GoodsChecker(currentPlayer,rewardGoods);
            try {
                playersView.setClientState(ClientState.MANAGE_GOODS);
            }catch(Exception ignored) {}
        }
        else {
            game.endCardEvent();
        }
    }

    /**
     * Validates and applies the chosen cargo configuration after taking the reward.
     *
     * @param playerName the name of the player
     * @param clientCredits the number of credits after taking the reward
     * @param updatedCargos updated cargo tiles
     */
    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        if (goodsChecker.check(clientCredits, updatedCargos)) {
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            game.endCardEvent();
        }
        else {
            try {
                playersView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }

    /**
     * Removes the selected goods from the ship after a failed encounter.
     *
     * @param playerName the name of the player
     * @param goodsToRemove coordinates of goods to remove
     */
    @Override
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || won != -1) {
            System.out.println(currentPlayer.getPlayerName());
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updatedTiles = lostGoods.removeGoods(currentPlayer,playersView, goodsToRemove);
        if (updatedTiles == null) {
            try {
                playersView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
        else {
            notifyModifiedTiles(playerName, updatedTiles);
            nextPlayer();
        }
    }

    /**
     * Returns the list of goods rewarded to the player.
     *
     * @param c an unused parameter
     * @return the reward goods
     */
    @Override
    public ArrayList<Goods> getChosenPlanets(int c) {
        return rewardGoods;
    }

    /**
     * Returns the number of goods lost in the penalty.
     *
     * @return number of goods to be removed
     */
    @Override
    public int getGoodsPenalty() {
        return lostGoods.getNumberOfLostGoods();
    }

    @Override
    public ArrayList<Goods> getGoodsList(String playerName) {
        return rewardGoods;
    }

    /**
     * Provides a string representation of the card.
     *
     * @return string with level, lost goods, and reward goods
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Smugglers: ").append(super.toString()).append(" lostGoods: ").append(this.lostGoods).append(" ");
        string.append("rewardGoods: ");
        for (Goods goods : rewardGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }

    /**
     * Handles player disconnection and resumes the sequence.
     *
     * @param playerName the name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            if (won == 1 && goodsChecker != null) {
                ArrayList<Tile> updatedTiles = currentPlayer.automaticGoodsPositioner(rewardGoods);
                notifyModifiedTiles(playerName,updatedTiles);
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                notifyMovement(currentPlayer);
                game.endCardEvent();
            }
            else if (won == 1) {
                choice(playerName, false);
            }
            else if (won == -1) {
                if (lostGoods.initializePenalty(game,playersView,currentPlayer)){
                    System.out.println("Automatic goods penalty didn't work");
                }
                nextPlayer();
            }
            else {
                playerIndex--;
                nextPlayer();
            }
        }
    }

    /**
     * Handles the scenario when a player lands on the 'Smugglers' card and resolves
     * the corresponding sequence of events based on game conditions.
     *
     * @param playerName the name of the player who landed on the card
     */
    @Override
    public void playerLanded(String playerName) {
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            if (won == 1 && goodsChecker != null) {
                ArrayList<Tile> updatedTiles = currentPlayer.automaticGoodsPositioner(rewardGoods);
                notifyModifiedTiles(playerName,updatedTiles);
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                notifyMovement(currentPlayer);
                game.endCardEvent();
            }
            else if (won == 1) {
                choice(playerName, false);
            }
            else if (won == -1) {
                if (lostGoods.initializePenalty(game,playersView,currentPlayer)){
                    System.out.println("Automatic goods penalty didn't work");
                }
                nextPlayer();
            }
            else {
                nextPlayer();
            }
        }
    }


}