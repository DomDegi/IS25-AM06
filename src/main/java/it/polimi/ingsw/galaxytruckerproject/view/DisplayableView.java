package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface representing a view that can be displayed to the user in the Galaxy Trucker game.
 * This view allows for the interaction between the game's client and the UI to show various
 * game-related elements such as flightboards, shipboards, tiles, cards, projectiles, and penalties.
 * <p>
 * Methods in this interface are meant to update the view with game information and handle player interactions.
 * </p>
 */
public interface DisplayableView extends ViewInterface {

    /**
     * Signals the start of sending coordinates for a player's move.
     */
    void sendingCoordinates();

    /**
     * Displays the tiles that were turned down by the player.
     * @param turnedTiles the map of turned tiles, where the key is the tile ID and the value is the tile object
     */
    void showTurnedTiles(Map<Integer, Tile> turnedTiles);

    /**
     * Signals that a cargo item has been selected by the player.
     * @param coordinates the coordinates of the selected cargo item
     */
    void cargoSelected(Coordinates coordinates);

    /**
     * Notifies the player of incorrect input from the local client.
     */
    void wrongLocalInput();

    /**
     * Signals that the player has selected a coordinate for their action.
     */
    void coordinateSelected();

    /**
     * Displays the list of cards that are available for the player.
     * @param cards the list of cards to be shown
     */
    void showDeck(ArrayList<Card> cards);

    /**
     * Displays the player's flightboard.
     * @param lightFlightboard the flightboard representation to be shown
     */
    void printFlightboard(LightFlightboard lightFlightboard);

    /**
     * Verifies the status of the shipboard.
     * @param lightShipBoard the shipboard status to be checked
     */
    void checkShipboard(LightShipBoard lightShipBoard);

    /**
     * Displays the shipboard information.
     * @param lightShipBoard the shipboard details to be shown
     */
    void printShipboard(LightShipBoard lightShipBoard);

    /**
     * Displays the projectile information for the player.
     * @param projectile the projectile data to be shown
     */
    void printProjectile(Projectile projectile);

    /**
     * Displays the cabin tiles.
     * @param cabins the tile data representing the cabins to be shown
     */
    void printCabins(Tile cabins);

    /**
     * Displays the tiles that have been drawn by the player.
     * @param drawnTiles the map of drawn tiles, where the key is the tile ID and the value is the tile object
     */
    void printDrawnTiles(Map<Integer, Tile> drawnTiles);

    /**
     * Prints the list of goods available in the player's cargo.
     * @param goodsArray the list of goods to be shown
     */
    void goodsPrinter(ArrayList<Goods> goodsArray);

    /**
     * Displays the shipboard with the booked status.
     * @param shipBoard the shipboard data showing which elements are booked
     */
    void printBooked(LightShipBoard shipBoard);

    /**
     * Sets the client controller for handling interactions with the game.
     * @param clientController the client controller to be set
     */
    void setClientController(ClientController clientController);

    /**
     * Signals that the crew has been positioned by the player.
     */
    void crewPositioned();

    /**
     * Displays a generic message to the player.
     * @param genericMessage the message to be shown
     */
    void showGenericMessage(String genericMessage);

    /**
     * Notifies the player that they are the victim of a penalty.
     * @param playerName the name of the player receiving the penalty
     * @param penalty the penalty object being applied to the player
     */
    void victimOfThePenalty(String playerName, Penalty penalty);

    /**
     * Notifies the player that another player has joined the game.
     * @param expectedPlayer the expected number of players in the game
     * @param currentPlayer the current number of players in the game
     * @param reconnected true if the player has reconnected, false if it's a new player
     */
    void notifyPlayerJoined(int expectedPlayer, int currentPlayer, boolean reconnected);

    /**
     * Displays the list of available decks to the player.
     */
    void showAvailableDecks();
}
