package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "Abandoned Station" encounter card.
 * <p>
 * Each eligible in-flight player in turn may choose to send a fixed number
 * of crew members and spend days in order to load up specified goods.
 * Upon supplying crew and correctly managing their cargo holds, the player
 * delays their flight and the event ends.
 * </p>
 */
public class AbandonedStation extends Card {
    /** How many crew members the player must send to the station. */
    private final int crewNumberRequired;
    /** The list of goods the player can gain if they succeed. */
    private final ArrayList<Goods> possibleGoodsGain;
    /** Index of the current player being asked to resolve this card. */
    private int playerIndex = -1;
    /** The player currently resolving this card. */
    private Player currentPlayer;
    /** The view interface for the current player. */
    private ViewInterface currentPlayersView = null;
    /** Whether the current player has elected to attempt the station. */
    private boolean won;
    /** Helper for validating that the player's cargo management is correct. */
    private GoodsChecker goodsChecker = null;
    /** Whether the current player has already removed their crew to accept the reward */
    private boolean removedCrew = false;

    /**
     * Constructs an AbandonedStation card with an image path.
     *
     * @param level               the difficulty level of the card
     * @param requiredDays        the number of days to delay if accepted
     * @param crewNumberRequired  how many crew members must be sent
     * @param possibleGoodsGain   the goods to award upon success
     * @param filePath            path to the card’s image resource
     */
    @JsonCreator
    public AbandonedStation(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain,
            @JsonProperty("imagePath") String filePath
    ) {
        super(level, requiredDays, filePath);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.currentPlayer = null;
        this.won = false;
    }

    /**
     * Constructs an AbandonedStation card without specifying an image path.
     *
     * @param level               the difficulty level of the card
     * @param requiredDays        the number of days to delay if accepted
     * @param crewNumberRequired  how many crew members must be sent
     * @param possibleGoodsGain   the goods to award upon success
     */
    public AbandonedStation(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain
    ) {
        super(level, requiredDays, null);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.currentPlayer = null;
        this.won = false;
    }

    /**
     * Initializes the card event by storing references to the game and views,
     * then advances to the first eligible player.
     *
     * @param game      the game engine interface
     * @param viewsMap  mapping from player names to their virtual views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    /**
     * Processes the player's yes/no decision to attempt the station.
     * If accepted, prompts for crew coordinates; otherwise advances to the next player.
     *
     * @param playerName  name of the player making the decision
     * @param decision    true to attempt (send crew), false to skip
     */
    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won) {
            try { viewsMap.get(playerName).showWrongInputMessage(); } catch (Exception ignored) {}
            return;
        }
        if (!decision) {
            nextPlayer();
        } else {
            try {
                currentPlayersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            } catch (Exception ignored) {}
            won = true;
            goodsChecker = new GoodsChecker(currentPlayer, possibleGoodsGain);
        }
    }

    /**
     * Handles removal of crew tiles after the player has opted in.
     * If the correct number of crew is removed, transitions to goods management.
     *
     * @param playerName     name of the player removing crew
     * @param toRemoveFrom   list of coordinates of crew tiles to remove
     */
    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> toRemoveFrom) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || !won) {
            try { viewsMap.get(playerName).showWrongInputMessage(); } catch (Exception ignored) {}
            return;
        }
        if (toRemoveFrom.size() != this.crewNumberRequired) {
            try { currentPlayersView.showWrongInputMessage(); } catch (Exception ignored) {}
            return;
        }

        ArrayList<Tile> updatedTiles = player.removeCrew(toRemoveFrom);
        if (updatedTiles != null) {

            notifyModifiedTiles(playerName, updatedTiles);
            try {
                removedCrew = true;
                currentPlayersView.setClientState(ClientState.MANAGE_GOODS);
            } catch (Exception ignored) {}
        } else {
            try { currentPlayersView.showWrongInputMessage(); } catch (Exception ignored) {}
        }
    }

    /**
     * Validates the player's cargo hold update and, if correct,
     * moves the flight, updates tiles, and ends the card event.
     *
     * @param playerName     name of the player managing goods
     * @param clientCredits  player's reported credit total
     * @param updatedCargos  updated list of CargoHold tiles
     */
    @Override
    public void manageGoods(String playerName,
                            int clientCredits,
                            ArrayList<CargoHold> updatedCargos) {
        Player player = game.identifyPlayerByName(playerName);
        if (!won || !player.equals(currentPlayer)) {
            try { viewsMap.get(playerName).showWrongInputMessage(); } catch (Exception ignored) {}
            return;
        }
        if (!goodsChecker.check(clientCredits, updatedCargos)) {
            try { currentPlayersView.showWrongInputMessage(); } catch (Exception ignored) {}
        } else {
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
            game.endCardEvent();
        }
    }

    /**
     * Advances to the next in-flight player who meets the crew requirement.
     * Skips disconnected players and those with insufficient crew.
     * Ends the event if all players have been considered.
     */
    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentPlayersView = viewsMap.get(currentPlayer.getPlayerName());

        if (currentPlayer.IsDisconnected() ||
                currentPlayer.getTotalCrew() < crewNumberRequired) {
            nextPlayer();
        } else {
            try {
                currentPlayersView.setClientState(ClientState.ACTION);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Returns a detailed string representation including level, delay days,
     * required crew, and possible goods gain.
     *
     * @return a string describing this card’s parameters
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbandonedStation: ").append(super.toString())
                .append(" crewNumberRequired: ").append(crewNumberRequired)
                .append(" possibleGoodsGain: ");
        for (Goods good : possibleGoodsGain) {
            sb.append(good).append(" ");
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param playerName  name of the player querying goods
     * @return the list of goods available from this card
     */
    @Override
    public ArrayList<Goods> getGoodsList(String playerName) {
        return possibleGoodsGain;
    }

    /**
     * {@inheritDoc}
     *
     * @return the number of crew required by this card
     */
    @Override
    public int getCrewNumber() {
        return crewNumberRequired;
    }

    /**
     * Handles a player disconnecting mid-event by rewinding the index
     * so that the nextPlayer logic will correctly advance.
     * If the player already removed the crew to accept the goods gain
     * and only needs to place them, the goods will be automatically placed
     * randomly in the free spaces.
     * @param playerName  name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && playerName.equals(currentPlayer.getPlayerName())) {
            if (removedCrew && won) {
                ArrayList<Tile> updatedTile = currentPlayer.automaticGoodsPositioner(possibleGoodsGain);
                notifyModifiedTiles(playerName, updatedTile);
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                notifyMovement(currentPlayer);
                game.endCardEvent();
                return;
            }
            playerIndex--;
            nextPlayer();
            won = false;
        }
    }
}
