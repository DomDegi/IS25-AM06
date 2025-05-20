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

public class MultiGameController implements Serializable {

    private final ConcurrentHashMap<String, GameController> gamesMap = new ConcurrentHashMap<>();

    private final Map<String, VirtualView> viewsMap = new HashMap<>();

    public MultiGameController() {}

    /**
     * checks if the nickname is taken already from the active players, if not checks if there
     * used to be a player connected to a started game that disconnected,
     * if none of the above puts player in the viewsMap and shows them all the joinable games
     * @param nickname player's chosen nickname
     * @param view player's view
     * @param controller player's personal controller to interact with the model
     * @return boolean value to tell if the login was successful
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
     * removes player from game and if game is empty removes game from gamesMap
     * returns leaver to joinableGamesList so that their view visualizes all the open lobbies
     * @param leaver player to leave
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
     * returns a map with every player nickname associated with their ViewInterface
     * of the players that are inside a game.
     * @return a map that associates every playerName with their interface
     */
    public Map<String, ViewInterface> allConnectedPlayers() {
        Map<String, ViewInterface> allConnectedPlayers = new HashMap<>();
        gamesMap.forEach( (gameName, gameController) -> allConnectedPlayers.putAll(gameController.getPlayersViewMap()));
        return allConnectedPlayers;
    }

    /**
     * returns true if the player's nickname was present in a game
     * that reached after start_game phase
     * @param nickname playersName as a string
     * @return true if player was already connected to an ongoing game
     */
    public boolean isAlreadyInAGame(String nickname) {
        return (gameFromNickname(nickname) != null);
    }

    public boolean isLookingToJoinAGame (String nickname) {
        return (viewsMap.containsKey(nickname));
    }

    /**
     * returns the gameController of the game that contains the player with
     * the nickname passed as parameter
     * @param nickname player whose game we are looking for
     * @return GameController object the player is a part of
     */
    public GameController gameFromNickname(String nickname) {
        return gamesMap.values().stream()
                .filter(gameController -> gameController.getAllPlayers().stream()
                        .anyMatch(player -> player.getPlayerName().equals(nickname)))
                .findFirst()
                .orElse(null);
    }

    /**
     * shows the player the games still in lobby phase and adds player to the viewsMap
     * in the multiGameController.
     * @param nickname nickname of the player to add the viewsMap
     * @param view view of the player to call the method that shows the joinable games on
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
     * notifies every player still in game selection that a new game was created
     * @param gameCreator game creator
     * @param creatorView view of the game creator
     */
    public void notifyNewGame (String gameCreator, ViewInterface creatorView)  {
        for (Map.Entry<String, VirtualView> entry : viewsMap.entrySet()) {
            String key = entry.getKey();
            VirtualView value = entry.getValue();
            joinableGamesList(key, value);
        }
    }

    public void playPingPong(){
       PingPong pingPong = new PingPong(this.gamesMap);
       pingPong.run();
    }
}
