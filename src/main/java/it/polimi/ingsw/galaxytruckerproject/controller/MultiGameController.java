package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.persistence.GameLoader;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.PingPong;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages multiple simultaneous game sessions in Galaxy Trucker.
 * Handles player login, game creation and joining, and routes views and controllers to the appropriate game instance.
 * It's also responsible for maintaining the state of all lobbies and notifying clients of new games.
 */
public class MultiGameController implements Serializable {

    /**
     * Map of active games, identified by their unique name.
     */
    private final ConcurrentHashMap<String, GameController> gamesMap = new ConcurrentHashMap<>();

    /**
     * Map of players currently in the lobby phase, awaiting to join or create a game.
     */
    private final Map<String, VirtualView> viewsMap = new HashMap<>();

    /**
     * Constructs a new MultiGameController with empty game and view maps.
     */
    public MultiGameController() {}

    /**
     * Attempts to log a player into the system.
     * Checks if the nickname is already taken or if the player should reconnect to an existing game.
     * Otherwise, adds the player to the view map and displays the list of joinable games.
     *
     * @param nickname the chosen nickname of the player
     * @param view the remote view of the player
     * @param controller the personal controller associated with the player
     * @return true if login was successful; false otherwise
     */
    public boolean login(String nickname, VirtualView view, Controller controller){
        boolean wasSuccessful;
        if (view != null && controller != null) {
            //check if the nickname is unique
            if (allConnectedPlayers().containsKey(nickname)) {
                wasSuccessful = false;
                try {
                    view.showLoginResponse(false);
                    view.showWrongInputMessage();
                } catch (Exception ignored) {
                }
            }
            else {
                wasSuccessful = true;
                try{
                view.showLoginResponse(true);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if (isAlreadyInAGame(nickname)) {
                    GameController previouslyJoinedGame = gameFromNickname(nickname);
                    controller.setGameController(previouslyJoinedGame);
                    previouslyJoinedGame.addToPlayersViewMap(nickname, view, true);
                }
                else {
                    joinableGamesList(nickname, view);
                    try {
                        view.setClientState(ClientState.LOBBY);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        else  {
            wasSuccessful = false;
        }
        return wasSuccessful;
    }

    /**
     * Creates a new game or resumes a previously saved one, if available.
     * Handles player limit, name uniqueness, and adds the creator to the game.
     *
     * @param creator the nickname of the player creating the game
     * @param gameName the name of the new or saved game
     * @param playerCount the number of players required to start the game
     * @param controller the player's controller
     * @param chosenMode the selected {@link GameMode}
     */
    public void createGame (String creator, String gameName, int playerCount, Controller controller, GameMode chosenMode) {
        VirtualView creatorView = viewsMap.get(creator);

        if (creator != null && creatorView != null && controller != null && !isAlreadyInAGame(creator)) {
            if (gamesMap.get(gameName) != null) {
                try {
                    creatorView.showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (playerCount < 2 || playerCount > 4) {
                try {
                    creatorView.showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Optional<GameController> oldGame = GameLoader.findSavedGame(gameName);
                if (oldGame.isEmpty()) {
                    Game game = new Game(chosenMode, playerCount);
                    GameController newGameController = new GameController(game, gameName);
                    controller.setGameController(newGameController);
                    gamesMap.put(gameName, newGameController); //adds game to open games
                    //remove player from map of the views of player joining a game
                    viewsMap.remove(creator);
                    newGameController.addToPlayersViewMap(creator, creatorView, false);
                }
                else {
                    controller.setGameController(oldGame.get());
                    gamesMap.put(gameName, oldGame.get());
                    viewsMap.remove(creator);
                    oldGame.get().addToPlayersViewMap(creator, creatorView, isAlreadyInAGame(creator));
                }
                notifyNewGame(creator, creatorView);
            }
        }
    }

    /**
     * Allows a player to join an existing game by name.
     *
     * @param joiner the nickname of the player joining
     * @param gameName the name of the game to join
     * @param controller the player's controller
     */
    public void joinGame (String joiner, String gameName, Controller controller) {
        GameController gameToJoin = gamesMap.get(gameName);
        VirtualView joinerView = viewsMap.get(joiner);

        if (joinerView != null && controller != null) {
            if (gameToJoin == null) {
                try {
                    joinerView.showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                viewsMap.remove(joiner);
                controller.setGameController(gameToJoin);
                gameToJoin.addToPlayersViewMap(joiner, joinerView, false);
            }
        }
    }

    /**
     * Removes the player from their current game.
     * If the game becomes empty, it is removed.
     * The player is then shown the lobby and the list of joinable games.
     *
     * @param leaver the nickname of the player who is leaving the game
     */
    public void leaveGame (String leaver) {
        GameController gameToLeave = this.gameFromNickname(leaver);

        if (gameToLeave != null) {
             VirtualView leaverView = gameToLeave.getPlayersViewMap().get(leaver);
             gameToLeave.playerLeaves(leaver);

            if (gameToLeave.isGameEmpty()) {
                gamesMap.remove(gameToLeave.getGameName());
            }
            try {
                leaverView.setClientState(ClientState.LOGIN);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            joinableGamesList(leaver, leaverView);
        }
    }

    /**
     * Returns a map of all players currently inside a game, mapped to their views.
     *
     * @return a map from nickname to {@link ViewInterface} for all connected players
     */
    public Map<String, ViewInterface> allConnectedPlayers() {
        Map<String, ViewInterface> allConnectedPlayers = new HashMap<>();
        gamesMap.forEach( (gameName, gameController) -> allConnectedPlayers.putAll(gameController.getPlayersViewMap()));
        return allConnectedPlayers;
    }

    /**
     * Checks whether the player is already inside an ongoing or saved game.
     *
     * @param nickname the nickname of the player
     * @return true if the player is in a game; false otherwise
     */
    public boolean isAlreadyInAGame(String nickname) {
        return (gameFromNickname(nickname) != null);
    }

    /**
     * Checks whether a player is currently browsing the game lobby.
     *
     * @param nickname the nickname of the player
     * @return true if the player is in the lobby view; false otherwise
     */
    public boolean isLookingToJoinAGame (String nickname) {
        return (viewsMap.containsKey(nickname));
    }

    /**
     * Finds and returns the game that the given player is part of.
     *
     * @param nickname the nickname of the player
     * @return the {@link GameController} containing the player, or null if not found
     */
    public GameController gameFromNickname(String nickname) {
        return gamesMap.values().stream()
                .filter(gameController -> gameController.getAllPlayers().stream()
                        .anyMatch(player -> player.getPlayerName().equals(nickname)))
                .findFirst()
                .orElse(null);
    }

    /**
     * Shows the player a list of games still in the lobby phase.
     * Adds the player to the views map if not already in a game.
     *
     * @param nickname the player's nickname
     * @param view the player's view to send the list to
     */
    public void joinableGamesList(String nickname, VirtualView view) {
        ArrayList<GameInfo> joinableGames = new ArrayList<>();
        for (GameController gameController : gamesMap.values()) {
            if (gameController.getGameState().equals(GameState.LOBBY_PHASE) || gameController.isRestarted()
            || isAlreadyInAGame(nickname)) {
                GameInfo currentGame = new GameInfo(gameController);
                if (gameController.isRestarted()) {
                    currentGame.setRestarted();
                }
                joinableGames.add(currentGame);
            }
        }
        try {
            view.showJoinableGamesList(joinableGames);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        viewsMap.put(nickname, view);
    }

    /**
     * Notifies all players still in the game selection phase that a new game has been created.
     * Updates their view with the refreshed list of joinable games.
     *
     * @param gameCreator the player who created the game
     * @param creatorView the creator's view
     */
    public void notifyNewGame (String gameCreator, ViewInterface creatorView)  {
        for (Map.Entry<String, VirtualView> entry : viewsMap.entrySet()) {
            String key = entry.getKey();
            VirtualView value = entry.getValue();
            joinableGamesList(key, value);
        }
    }

    /**
     * Starts the PingPong connection check service for all active games.
     * Used to detect and manage dropped client connections.
     */
    public void playPingPong(){
       PingPong pingPong = new PingPong(this.gamesMap);
       pingPong.run();
    }
}
