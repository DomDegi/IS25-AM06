package it.polimi.ingsw.galaxytruckerproject.controller.interfaces;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;

/** this interface is used to define the methods that
 * the player can call to interact with the server
 */

public interface ControllerInterface {

    /** this method verifies that the @param nickname chosen by
     * the player is unique and makes so that the player's view
     * gets linked to the controller. If the name is taken sends a
     * message to notify the player. if there used to be a player in
     * a game with this name, it reconnects the player.
     */
    void login(String nickname) throws Exception;

    /**
     * this method creates a new game on players request with a
     * player count between 2 and 4. The player gets notified if the
     * gameName is note available
     * @param gameName is the name chosen by the player for this game instance
     * @param playerCount max playerCount in game
     * @param mode chosen mode
     */
    void createGame (String gameName, int playerCount, GameMode mode) throws Exception;

    /**
     * this method makes so that the player logged in joins an existing lobby.
     * If the lobby becomes full after the player joins, the game starts.
     * If the lobby doesn't exist, the player is notified.
     * @param gameName is the name of the lobby the player is looking for
     * @throws Exception
     */
    void joinGame (String gameName) throws Exception;

    /**
     * this method makes so that the player leaves the game in lobby phase
     * and updates the player's view back to lobby list.
     * @throws Exception
     */
    void leaveGame () throws Exception;

    /**
     * this method when call makes the player leave the game in whatever state it is
     * @throws Exception
     */
    void leave () throws Exception;

    /**
     * this method is used by the player to choose a color after entering a game
     * Its view is updated with the available colors.
     * if the chosenColor is not available or wrong, player is notified
     * @param chosenColor string of the color player has chosen between RED, YELLOW, GREEN, BLUE
     * @throws Exception
     */
    void chooseColor (String chosenColor) throws Exception;

    /**
     * Used by the player to draw an event card
     * If the cards are over or
     * the player isn't the in the lead notifies player.
     * @throws Exception
     */

    /**
     * this method is used for game phases where the input player input
     * has to be processed internally by the game model and can't be said
     * which type of input is required, so it all gets sent through messages.
     */
    void playerChoiceThroughMessage (Message message) throws Exception;

    void turnHourglass () throws Exception;

    void drawTileFromStack ()  throws Exception;

    void drawTileFromTurned (int index) throws Exception;

    void refuseTile() throws Exception;


    /**
     * this method binds the GameController of the joined game to the player's personal controller
     * @param gameController gameController to set
     */
    void setGameController(GameController gameController);


}
