package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiGameController {

    private final Map<String, GameController> gamesMap = new HashMap<>();

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
                    view.askNickname();
                } catch (Exception ignored) {
                }
            }
            else {
                wasSuccessful = true;
                try{
                view.showLoginResponse(true);
                } catch(Exception ignored) {}
                if (isAlreadyInAGame(nickname)) {
                    GameController previouslyJoinedGame = gameFromNickname(nickname);
                    controller.setGameController(previouslyJoinedGame);
                    previouslyJoinedGame.addToPlayersViewMap(nickname, view, true);
                }
                else {
                    joinableGamesList(nickname, view);
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

        if (creator != null && creatorView != null && controller != null && !isAlreadyInAGame(gameName)) {
            if (gamesMap.get(gameName) != null) {
                try {
                    creatorView.showErrorMessage("Game with this name already exists");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (playerCount < 2 || playerCount > 4) {
                try {
                    creatorView.showErrorMessage("Invalid number of players");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Game game = new Game(chosenMode, playerCount);
                GameController gameController = new GameController(game, gameName);
                controller.setGameController(gameController);
                gamesMap.put(gameName, gameController); //adds game to open games
                viewsMap.remove(creator); //remove player from map of the views of player joining a game
                notifyNewGame(creator, creatorView);
                gameController.addToPlayersViewMap(creator, creatorView, false);
            }
        }
    }

    public void joinGame (String joiner, String gameName, Controller controller) {
        GameController gameToJoin = gamesMap.get(gameName);
        VirtualView joinerView = viewsMap.get(joiner);

        if (joinerView != null && controller != null) {
            if (gameToJoin == null) {
                try {
                    joinerView.showErrorMessage("a game with this name doesn't exists");
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
     * removes player from game adn if game is empty removes game from gamesMap
     * returns leaver to joinableGamesList so that their view visualizes all the open lobbies
     * @param leaver player to leave
     */
    public void leaveGame (String leaver) {
        GameController gameToLeave = this.gameFromNickname(leaver);

        if (gameToLeave != null) {
             VirtualView leaverView = gameToLeave.removePlayer(leaver);

            if (gameToLeave.isGameEmpty()) {
                gamesMap.remove(gameToLeave.getGameName());
            }
            joinableGamesList(leaver, leaverView);
        }
    }

    /**
     * completely leaves the game also the game selection
     * @param leaver nickname of the leaver
     */
    public void leave (String leaver) {
        if (allConnectedPlayers().containsKey(leaver)) {
            if (this.isAlreadyInAGame(leaver)) {
                GameController gameToLeave = gameFromNickname(leaver);
                gameToLeave.removePlayer(leaver);
            }
            else if (isLookingToJoinAGame(leaver)) {
                viewsMap.remove(leaver);
            }
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
                .filter(gameController -> gameController.getGame().identifyPlayerByName(nickname) != null)
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
        Map<String, GameController> joinableGames;
        joinableGames = gamesMap.entrySet().stream().filter(entry -> entry.getValue().getGameState().equals(GameState.START_GAME))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        try {
            creatorView.showGenericMessage("created game");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<String, VirtualView> entry : viewsMap.entrySet()) {
            String key = entry.getKey();
            VirtualView value = entry.getValue();
            joinableGamesList(key, value);
        }
    }

    public void playPingPong(){
        while(true){
            if(gamesMap!=null)
                for(GameController game : gamesMap.values()){
                    if(game.getActivePlayers()!=null)
                        for(String playerName : game.getActivePlayers().keySet()){
                            game.pingPong(playerName,game.getViewFromNickname(playerName));
                        }
                }
        }
    }
}
