package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the "Open Space" card event where players use engines to advance.
 * If a player has no available engines or batteries to use DoubleEngines, they are forced to land early.
 */
public class OpenSpace extends Card {

    /** The player currently resolving the card */
    private Player currentPlayer = null;

    /** The virtual view associated with the current player */
    private ViewInterface currentPlayerView = null;

    /** Index to track which player's turn it is */
    private int playerIndex = -1;

    /** List of players who have to land early due to no engine action */
    ArrayList<Player> playersToEarlyLand = new ArrayList<>();

    /**
     * Constructor with level and file path.
     * @param level the level of the card
     * @param filePath path to image or data file
     */
    @JsonCreator
    public OpenSpace(@JsonProperty("level") int level, @JsonProperty("imagePath") String filePath) {
        super(level, 0, filePath);
    }

    /**
     * Constructor with only level.
     * @param level the level of the card
     */
    public OpenSpace(@JsonProperty("level") int level) {
        super(level, 0);
    }

    /**
     * Initializes the card for gameplay and begins player turn processing.
     * @param game the game interface
     * @param viewsMap map of player names to their virtual views
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    /**
     * Handles the choice of engine usage for the current player.
     * If valid, moves the player forward or marks them for early landing.
     * @param playerName the name of the player
     * @param numDoubleEngines number of double engines to use
     * @param batteriesToUse battery coordinates used to power engines
     */
    @Override
    public void engineChoice(String playerName, int numDoubleEngines, ArrayList<Coordinates> batteriesToUse) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            engineChoice(currentPlayer.getPlayerName(), 0, new ArrayList<>());
            return;
        }
        Map<Integer, ArrayList<Tile>> returned = currentPlayer.useEngines(numDoubleEngines, batteriesToUse);
        if (returned == null) {
            try {
                currentPlayerView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        int engineStrength = returned.keySet().iterator().next();
        ArrayList<Tile> tiles = returned.get(engineStrength);
        if (!tiles.isEmpty()) {
            notifyModifiedTiles(playerName, tiles);
        }
        this.moveOrEarlyLand(engineStrength);
    }

    /**
     * Moves the player forward if they have engine strength, or initiates early landing otherwise.
     * @param engineStrength the engine power value from the player's ship
     */
    private void moveOrEarlyLand(int engineStrength) {
        if (engineStrength == 0) {
            System.out.println("sono qui");
            playersToEarlyLand.add(currentPlayer);
            try {
                currentPlayerView.notifyEarlyLanding();
            } catch (RemoteException e) {
            }
        } else {
            game.getFlightBoard().moveForward(currentPlayer, engineStrength);
            notifyMovement(currentPlayer);
        }
        nextPlayer();
    }

    /**
     * Processes the turn for the next player. If all players are done, ends the card event.
     */
    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            for (Player player : playersToEarlyLand) {
                game.getFlightBoard().earlyLanding(player);
            }
            game.endCardEvent();
            return;
        }
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentPlayerView = viewsMap.get(currentPlayer.getPlayerName());
        if (currentPlayer.IsDisconnected()
                || currentPlayer.getShipBoard().getDoubleEngine().isEmpty()
                || currentPlayer.getShipBoard().getNumBatteries() == 0) {
            engineChoice(currentPlayer.getPlayerName(), 0, new ArrayList<>());
        } else {
            try {
                currentPlayerView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
            } catch (Exception ignored) {}
        }
    }

    /**
     * String representation of the card for debugging or display.
     * @return a simple string with type and ID
     */
    @Override
    public String toString() {
        return "OpenSpace" + " id: " + id;
    }

    /**
     * Handles the case where a player disconnects during their turn.
     * @param playerName the name of the player that disconnected
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && playerName.equals(currentPlayer.getPlayerName())) {
            playerIndex--;
            nextPlayer();
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
        if (currentPlayer != null && playerName.equals(currentPlayer.getPlayerName())) {
            nextPlayer();
        }
    }
}
