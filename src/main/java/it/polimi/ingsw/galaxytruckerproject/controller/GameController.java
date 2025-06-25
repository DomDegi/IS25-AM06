package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.Observer;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.persistence.ClientUpdater;
import it.polimi.ingsw.galaxytruckerproject.persistence.GameSaver;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

import static it.polimi.ingsw.galaxytruckerproject.client.ClientState.*;
import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.LEVEL2;
import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.TRIAL;
import static it.polimi.ingsw.galaxytruckerproject.model.GameState.CARD_EVENT;

/**
 * The GameController class manages all aspects of a single game session,
 * including player management, game phases, tile and card handling, disconnections,
 * state transitions, and synchronization with client views.
 * Implements the Observer interface to react to state changes in the Game model.
 */
public class GameController implements Observer, Serializable {
    /** The game name identifier. */
    private final String gameName;

    /** The game logic interface. */
    private GameInterface game;

    /** List of players who need to correct their ships. */
    private final ArrayList<String> playersWithErrors;

    /** Map storing each player's current client state. */
    private final Map<String, ClientState> clientsStatesMap = new HashMap<>();

    /** Map linking player nicknames to their virtual view. */
    private final Map<String, VirtualView> playersViewMap;

    /** Map of active players currently in the game. */
    private final Map<String, Player> activePlayers;

    /** Map of players who disconnected from the game. */
    private final Map<String, Player> disconnectedPlayers;

    /** Map used for ping-pong connectivity checks. */
    private final Map<String, CompletableFuture<Void>> pendingPongs = new ConcurrentHashMap<>();

    /** List of players who should land early due to no crew. */
    private final ArrayList<Player> playersToEarlyLand = new ArrayList<>();

    /** Tracks which small decks are locked by which players. */
    private final ConcurrentHashMap<String, Integer> lockedSmallDecks = new ConcurrentHashMap<>();

    /** The number of times the hourglass has been turned. */
    private int hourglassTurns = 0;

    /** Task to manage hourglass countdown. */
    private TimerTask hourglassTask;

    /** Timer for the hourglass logic. */
    private Timer hourglassTimer;

    /** Whether the hourglass is currently active. */
    private boolean hourglassON = false;

    /** Indicates whether the game has been loaded from a saved state. */
    private boolean restarted;

    /** Cached string of the current game status sent on reconnections. */
    private String currentGameStatus;

    /** Executor that periodically autosaves the game. */
    private ScheduledExecutorService autoSaveExecutor;

    /** Flag to enable or disable autosaving. */
    private volatile boolean autoSaveEnabled = false;


    public String toString(){
        return gameName+"\ngame state:"+game.getGameState().toString()+"\nplayer needed: "+game.getPlayerCount()+"\nplatyer in game: "+game.getNumberOfPlayers();
    }

    /**
     * Constructs a GameController linked to a new game instance.
     * @param game the GameInterface instance
     * @param gameName the name of the game session
     */
    public GameController(GameInterface game, String gameName) {
        this.gameName = gameName;
        this.game = game;
        game.addObserver(this);
        this.playersWithErrors = new ArrayList<>();
        this.playersViewMap = new HashMap<>();
        this.activePlayers = new HashMap<>();
        this.disconnectedPlayers = new HashMap<>();
        this.restarted = false;
    }
    /**
     * Constructs a GameController for a reloaded (persisted) game.
     * @param gameName name of the game
     */
    public GameController(String gameName) {
        this.gameName = gameName;
        this.playersWithErrors = new ArrayList<>();
        this.playersViewMap = new HashMap<>();
        this.activePlayers = new HashMap<>();
        this.disconnectedPlayers = new HashMap<>();
        this.restarted = true;
    }

    /**
     * Sets the GameInterface for this controller (used during loading).
     * @param game the game instance to link
     */
    public void setGameInterface(GameInterface game) {
        this.game = game;
        game.addObserver(this);
    }

    /**
     * Handles state transitions based on GameState changes.
     * @param newState the new state of the game
     */
    @Override
    public void update(GameState newState) {
        switch (newState) {
            case START_GAME -> startGame();
            case VERIFY_SHIP_CORRECTNESS ->  verifyShipCorrectness();
            case DRAW_CARD -> askFirstPlayerToDraw();
            case CARD_EVENT -> initializeDrawnCard();
            case CONCLUDE_GAME -> concludeGame();
        }
    }

    /**
     * Adds a player view to the session or handles reconnection.
     * @param playerName name of the player
     * @param view the VirtualView associated to the player
     * @param reconnecting true if player is reconnecting
     */
    public void addToPlayersViewMap(String playerName, VirtualView view, boolean reconnecting) {
        if (restarted) {
            if (activePlayers.containsKey(playerName)) {
                playersViewMap.put(playerName, view);
                notifyPlayerJoined(false);
                updateReconnectedPlayer(playerName,view);
                checkIfAllJoinedAgain();
                return;
            }
            else if (checkIfReconnectionIsPossible(playerName,view)) {
                playersViewMap.put(playerName, view);
                notifyPlayerJoined(true);
                checkIfAllJoinedAgain();
                return;
            }
            else {
                try {
                    System.out.println("Player "+playerName+" can't reconnect because they were never inGame");
                    view.showWrongInputMessage();
                }catch (RemoteException e){
                    throw new RuntimeException(e);
                }
            }
        }
        if (reconnecting && checkIfReconnectionIsPossible(playerName,view)) {
            prepareForReconnection(playerName,view);
        }
        else {
            try {
                view.setGameMode(this.game.getMode());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (this.getGameState().equals(GameState.LOBBY_PHASE)){
                playersViewMap.put(playerName, view);
                notifyPlayerJoined(false);
                ArrayList<PlayersColor> notAvailableColors=new ArrayList<>();
                for(PlayersColor color:PlayersColor.values()){
                    if(!checkColorAvailable(color)){
                        notAvailableColors.add(color);
                    }
                }
                try {
                    playersViewMap.get(playerName).notifyNotAvailableColor(notAvailableColors);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                if(playersViewMap.size()!=1){
                try {
                    view.setClientState(ClientState.COLOR_CHOICE);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                }
            }
            else{
                try {
                    view.showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    /**
     * Checks if all previously disconnected players have rejoined.
     * If all players are present, resumes the game from the appropriate state,
     * restarting timers and autosave if needed.
     */
    public void checkIfAllJoinedAgain() {
        if (playersViewMap.size() == activePlayers.size()) {
            currentGameStatus = null;
            restarted = false;
            if (game.getGameState() == GameState.SHIPS_CREATION || game.getGameState() == GameState.VERIFY_SHIP_CORRECTNESS) {
                if (autoSaveEnabled) {
                    startAutoSave();
                }
            }
            switch (game.getGameState()) {
                case START_GAME -> {
                    for (VirtualView view : playersViewMap.values()) {
                        try {
                            view.setGameMode(game.getMode());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    startGame();
                }
                case SHIPS_CREATION -> {
                    updateEveryView(S_END_DRAW_TILE_CARD);
                    for (Player player : this.getAllPlayers()) {
                        if (player.getDrawnTile() != null) {
                            refuseTile(player.getPlayerName());
                        }
                    }
                    flightBoardReposition();
                    if (hourglassTurns == 3) {
                        hourglassTurns--;
                        startTimer();
                    }
                }
                case VERIFY_SHIP_CORRECTNESS -> {
                    verifyShipCorrectness();
                }
                case DRAW_CARD -> askFirstPlayerToDraw();
                case CARD_EVENT,CONCLUDE_GAME -> {
                    System.out.println("The game shouldn't be able to be restarted from here");
                    throw new IllegalArgumentException();
                }
            }
        }
    }
    /**
     * Notifies all connected clients that a new player has joined or rejoined the game.
     *
     * @param reconnected true if the player is reconnecting, false if joining for the first time
     */
    public void notifyPlayerJoined(boolean reconnected) {
        for (VirtualView view : playersViewMap.values()) {
            try{
                view.notifyPlayerJoined(game.getPlayerCount(),playersViewMap.size(),reconnected);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Repositions players on the flight board based on their saved position and ranking.
     * Handles repositioning differently based on the game mode (TRIAL or LEVEL2).
     */
    public void flightBoardReposition() {
        ArrayList<Player> players = this.getAllPlayers();
        if (game.getMode().equals(GameMode.LEVEL2)) {
            for (Player player: players) {
                int position = player.getPlayerPosition();
                int ranking = player.getPlayerRanking();
                if (position != 0 || ranking != 0) {
                    switch(position) {
                        case 6 -> this.setPosition(player.getPlayerName(),getViewFromNickname(player.getPlayerName()), 1);
                        case 3 -> this.setPosition(player.getPlayerName(),getViewFromNickname(player.getPlayerName()), 2);
                        case 1 -> this.setPosition(player.getPlayerName(),getViewFromNickname(player.getPlayerName()), 3);
                        case 0 -> this.setPosition(player.getPlayerName(),getViewFromNickname(player.getPlayerName()), 4);
                    }
                }
            }
        }
        else {
            ArrayList<Player> orderedPlayers = new ArrayList<>();
            for (Player player: players) {
                if (player.getPlayerRanking() != 0) {
                    orderedPlayers.add(player);
                }
            }
            orderedPlayers.sort(Comparator.comparingInt(Player::getPlayerRanking));
            orderedPlayers.forEach(game.getFlightBoard()::addToTrialFlightBoard);
        }
    }

    /**
     * only reconnects player if either it's still verify_ship phase or they have already
     * corrected their ship and chosen a starting position. If they disconnected after their
     * phase just puts the player in LAST position FOR NOW!!!!!!
     * @param playerName reconnecting player
     * @param view their view
     */
    //Reconnects disconnected player
    public boolean checkIfReconnectionIsPossible(String playerName, VirtualView view)  {
        if (disconnectedPlayers.containsKey(playerName)) {
            switch(this.getGameState()) {
                case START_GAME, SHIPS_CREATION, VERIFY_SHIP_CORRECTNESS -> {
                    playersViewMap.put(playerName, view);
                    activePlayers.put(playerName, disconnectedPlayers.remove(playerName));
                    activePlayers.get(playerName).playerReconnects();
                    return true;
                }
                case DRAW_CARD,CARD_EVENT -> {
                    Player player = game.identifyPlayerByName(playerName);
                    if ((player.getPlayerRanking() == 0 &&  player.getPlayerPosition() == 0) ||
                            (playersWithErrors.contains(playerName)) || !player.getShipBoard().isCompleted()) {
                        return false;
                    }
                    else {
                        playersViewMap.put(playerName, view);
                        activePlayers.put(playerName, disconnectedPlayers.remove(playerName));
                        activePlayers.get(playerName).playerReconnects();
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Prepares a player who has reconnected by restoring their state
     * and sending the appropriate client state depending on the current game phase.
     *
     * @param playerName the name of the reconnecting player
     * @param view the VirtualView associated with the reconnecting player
     */
    public void prepareForReconnection (String playerName,  VirtualView view)  {
        updateReconnectedPlayer(playerName,view);
        switch(this.getGameState()) {
            case START_GAME -> {
                updatePlayerView(ClientState.START_SHIP_CREATION, playerName);
            }
            case SHIPS_CREATION -> {
                Player player = activePlayers.get(playerName);
                if (player.getPlayerRanking() == 0 &&  player.getPlayerPosition() == 0) {
                    updatePlayerView(S_END_DRAW_TILE_CARD, playerName);
                }
                if (!lockedSmallDecks.isEmpty()) {
                    notifyNotAvailableCardDeck();
                }
            }
            case VERIFY_SHIP_CORRECTNESS -> {
                Player player = game.identifyPlayerByName(playerName);
                if (playersWithErrors.contains(playerName)) {
                    try {
                        view.asksToInputCoordinates(CoordReqType.CHOOSE_TO_BREAK);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (!player.getShipBoard().isCompleted()) {
                    updatePlayerView(ClientState.MANAGE_CABINS,playerName);
                }
                else {
                    checkIfPlayersPickedCrew();
                }
            }
            case DRAW_CARD,CARD_EVENT -> {
                updatePlayerView(ClientState.WAIT,playerName);
            }
        }
    }
    /**
     * Sends a summary of game events that occurred during the player's disconnection.
     * This helps synchronize the client state with the current server state.
     *
     * @param view the VirtualView of the reconnected player
     */
    public void updateReconnectedPlayer(String playerName, VirtualView view) {
        if (currentGameStatus == null) {
            currentGameStatus = ClientUpdater.currentGameStatus(this);
        }
        try {
            view.notifyChangesWhileGone(playerName, currentGameStatus);
        } catch(Exception ignored) {}
    }

    /**
     * adds player to the gameModel after they enter a valid color for their starting cabin
     * both nickname and color.
     * If players number reaches the initial setted count, starts game.
     */
    public void playerAddition(String playerName, PlayersColor playersColor)  {
        if (this.getGameState() == GameState.LOBBY_PHASE && playersViewMap.containsKey(playerName)) {
            //if playerCount is still to be reached, add player to the game model
            if (game.getNumberOfPlayers() < game.getPlayerCount()) {
                game.addPlayer(playerName, playersColor);
                activePlayers.put(playerName, game.identifyPlayerByName(playerName));
                for (Player player : activePlayers.values()) {
                    try {
                        ArrayList<PlayersColor> color=new ArrayList<>();
                        color.add(playersColor);
                        playersViewMap.get(player.getPlayerName()).notifyNotAvailableColor(color);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else { //player count already reached
                try {
                    playersViewMap.get(playerName).showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            //if game is already out of lobby phase
        } else if (playersViewMap.containsKey(playerName)) {
            try {
                playersViewMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {
            }
        } // if the count has been reached starts the game
    }
    /**
     * Handles the disconnection of a player by removing them from the active players list,
     * saving their state as disconnected, and cleaning up their view and state tracking.
     * Also removes the player from the FlightBoard if present.
     *
     * @param playerName the name of the player to remove
     * @return the VirtualView associated with the removed player, or null if none
     */
    public VirtualView removePlayer (String playerName) {
        if (disconnectedPlayers.containsKey(playerName)) {
            Player removedPlayer = disconnectedPlayers.get(playerName);
            activePlayers.remove(playerName);
            if (removedPlayer.getDrawnTile() != null) {
                refuseTile(playerName);
            }
            removedPlayer.playerDisconnects();
            if (clientsStatesMap.get(playerName).equals(ClientState.S_MANAGE_CARDS)) {
                stopLookingAtCards(getViewFromNickname(playerName), playerName);
            }
            clientsStatesMap.remove(playerName);
            if (game.getListOfAllPlayer().contains(removedPlayer)) {
                game.getFlightBoard().removePlayer(removedPlayer);
            }
            disconnectedPlayers.remove(playerName);
        }
        return playersViewMap.remove(playerName);
    }

    /**
     * checks if the color chosen by the player is già taken or not
     * @param playerName player that asked the check
     * @param view player's view
     * @param playersColor chosen color
     * @return true if color is available, false otherwise
     */
    public boolean checkColorAvailable (String playerName, ViewInterface view, PlayersColor playersColor)  {
        if (!playersViewMap.containsKey(playerName)) {
            try {
                view.showWrongInputMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        for (Player player : activePlayers.values()) {
            if (player.getPlayerColor().equals(playersColor)) {
                try {
                view.showWrongInputMessage();
                } catch(RemoteException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if a given player color is available (i.e., not already taken by another active player).
     *
     * @param playersColor the color to check for availability
     * @return true if the color is available, false if already taken
     */
    public boolean checkColorAvailable (PlayersColor playersColor)  {
        for (Player player : activePlayers.values()) {
            if (player.getPlayerColor().equals(playersColor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Starts the game and notifies clients.
     */
    public void startGame () {
        startAutoSave();
        for (Player player : game.getListOfAllPlayer()) {
            notifyStartPosition(player.getPlayerName(), player.getPlayerColor());
        }
        updateEveryView(START_SHIP_CREATION);
        if (game.getMode() == LEVEL2) {
            notifyFlightBoardCards();
        }
    }
    /**
     * Sends the current state of the flight board's cards to all connected clients.
     * Typically used at the start of the ship creation phase in LEVEL2 mode.
     */
    public void notifyFlightBoardCards() {
        Map<Integer,ArrayList<Card>> flightBoardCards = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            flightBoardCards.put(i,game.getANumberOfInGameCards(i));
        }
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyFlightBoardCards(flightBoardCards);
            } catch (Exception ignored) {

            }
        }
    }
    /**
     * Draws a tile for the player from stack or turned tiles.
     * @param playersView the view of the player
     * @param playerName the player's name
     * @param index index of the turned tile (0 for stack)
     * @param turned true if drawing from turned pile
     */
    public  void drawTile(VirtualView playersView, String playerName, int index, boolean turned) {

        // Can't draw if the shipboard is completed or there is already a tile to place/book/refuse
        if (playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)
                || playerStateIs(playerName, ClientState.S_FINISHED)) {
            try {
                playersView.showWrongInputMessage();
            } catch (Exception ignored) {
            }
            return;
        }
        Tile drawnTile;
        if (!turned) {
            drawnTile = game.drawTile(playerName);
            if (drawnTile == null) {
                try {
                    playersView.showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            try {
                playersView.showDrawnTile(drawnTile.send());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            drawnTile = game.drawTurnedTile(playerName, index);
            if (drawnTile == null) {
                try {
                    playersView.showWrongInputMessage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            notifyRemoveTurnedTile(drawnTile);
            try {
                playersView.showDrawnTile(drawnTile.send());
            } catch (Exception ignored) {
            }
        }
        updatePlayerView(ClientState.S_MANAGE_DRAWN_TILE, playerName);
    }
    /**
     * Notifies all connected views that a turned tile has been removed from the common pool.
     *
     * @param tile the turned tile that has been removed
     */
    public void notifyRemoveTurnedTile(Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyRemoveTurnedTile(tile.send());
            } catch (Exception ignored) { //unhandled exception
            }
        });
    }
    /**
     * Notifies all players that a booked tile was removed from a specific player's inventory.
     *
     * @param playerName the player who removed the booked tile
     * @param tile the tile that was removed
     */
    public void notifyBookedTile (String playerName, Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            if(virtualView!=playersViewMap.get(playerName))
                try {
                    virtualView.notifyBookedTile(playerName, tile.send());
                } catch (Exception ignored) {//unhandled exception
                }
        });
    }
    /**
     * Notifies all connected views that a new turned tile has been added to the common pool.
     *
     * @param tile the newly turned tile to be broadcasted to all clients
     */
    public void notifyNewTurnedTile(Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyNewTurnedTile(tile.send());
            } catch (Exception ignored) { //unhandled exception
            }
        });
    }

    /**
     * Verifies all ships for correctness after construction.
     */
    public void verifyShipCorrectness() {
        for (Player player :new ArrayList<>(game.getListOfInFlightPlayers()) ) {
            boolean correctness = player.getShipBoard().verifyCorrectness();
            ViewInterface playersView = this.getViewFromNickname(player.getPlayerName());
            if (correctness){
                if (player.IsDisconnected()) {
                    this.setCrewForDisconnectedPlayer(player);
                }
                else {
                    if (game.getMode().equals(LEVEL2)) {
                        try {
                            playersView.setClientState(ClientState.MANAGE_CABINS);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        player.setAllCrewToHuman();
                        checkIfPlayersPickedCrew();
                    }
                }
            } else {
                try{
                if (playersView != null) {
                    playersView.asksToInputCoordinates(CoordReqType.CHOOSE_TO_BREAK);
                }
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
                playersWithErrors.add(player.getPlayerName());
            }

        }
    }
    /**
     * Handles correction of invalid tiles.
     * @param playerName the player name
     * @param playersView the view of the player
     * @param toRemove the list of coordinates to remove
     */
    public void shipErrorManagement(String playerName, VirtualView playersView, ArrayList<Coordinates> toRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (player == null)
            return;
        if (!playersWithErrors.contains(playerName)) {
            try {
                playersView.showWrongInputMessage();
            } catch (Exception ignored) {
            }
            return;
        }
        for (Coordinates coord : toRemove) {
            player.getShipBoard().destroyForCorrection(coord);
        }
        notifyBrokenTile(playerName, toRemove);
        boolean correctness = player.getShipBoard().verifyCorrectness();
        if (correctness) {
            if (game.getMode() == TRIAL) {
                game.getFlightBoard().setPlayerToLast(player);
                notifyPlayerMovement(playerName, player.getPlayerColor(), player.getPlayerPosition(), player.getPlayerRanking());
            }
            playersWithErrors.remove(playerName);
            if (player.IsDisconnected()) {
                this.setCrewForDisconnectedPlayer(player);
            } else {
                try {
                    updatePlayerView(ClientState.MANAGE_CABINS,playerName);
                } catch (Exception ignored) {
                }
            }
        } else {
            try {
                playersView.asksToInputCoordinates(CoordReqType.CHOOSE_TO_BREAK);
            } catch (Exception ignored) {
            }
        }
    }
    /**
     * Notifies all connected views that a player has removed invalid tiles during ship correction.
     *
     * @param playerName the name of the player who removed the tiles
     * @param removed a list of coordinates representing the removed tiles
     */
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> removed) {
        for (VirtualView view: playersViewMap.values()){
            try {
                view.notifyBrokenTile(playerName,removed);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Automatically sets human crew members on all cabins for a disconnected player.
     * Notifies views of tile changes and updates the player's client state to WAIT.
     *
     * @param player the disconnected player
     */
    public void setCrewForDisconnectedPlayer(Player player) {
        notifyModifiedTiles(player.getPlayerName(), player.setAllCrewToHuman());
    }
    /**
     * Allows a player to assign crew members to their cabins.
     * If successful, marks the ship as completed, updates views, and proceeds to crew check.
     *
     * @param playerName the name of the player assigning crew
     * @param playersView the view associated with the player
     * @param cabins a list of tiles representing cabin modules
     */
    public void playerPicksCrewMembers(String playerName, VirtualView playersView ,ArrayList<Tile> cabins) {
        Player player = game.identifyPlayerByName(playerName);
        if (player == null) {
            return;
        }
        if (player.verifyAndSetupCrew(cabins)) {
            player.getShipBoard().setCompleted(true);
            notifyModifiedTiles(playerName, cabins);
            updatePlayerView(ClientState.WAIT, player.getPlayerName());
        }
        else {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
        checkIfPlayersPickedCrew();
    }
    /**
     * Checks if all players (including reconnected ones) have completed crew assignment.
     * If all players are ready and no errors remain, proceeds to end ship verification.
     */
    public void checkIfPlayersPickedCrew() {
        ShipBoard shipBoard;
        ArrayList<Coordinates> cabinsToCheck;
        for (Player p1: activePlayers.values()) {
            shipBoard = p1.getShipBoard();
            cabinsToCheck = shipBoard.getCabinsCoordinates();
            if(!shipBoard.isCompleted())
                return;
            for (Coordinates coord: cabinsToCheck) {
                if (shipBoard.getTile(coord).getCrew() == 0)
                    return;
            }
        }
        for (Player p2: disconnectedPlayers.values()) {
            if (p2.getShipBoard().isCompleted()) {
                continue;
            }
            shipBoard = p2.getShipBoard();
            cabinsToCheck = shipBoard.getCabinsCoordinates();
            if (cabinsToCheck.isEmpty() || playersWithErrors.contains(p2.getPlayerName())) {
                removePlayer(p2.getPlayerName());
                playersWithErrors.remove(p2.getPlayerName());
            }
            else{
                p2.setAllCrewToHuman();
            }
        }
        if(playersWithErrors.isEmpty()) {
            this.endShipVerification();
        }
    }
    /**
     * Ends the ship verification phase and transitions to the next game phase.
     */
    public void endShipVerification() {
        game.endShipVerification();
    }

    /**
     * Handles the refusal of a drawn tile by a player during ship construction.
     * Moves the tile to the turned pile and updates the player view accordingly.
     *
     * @param playerName the player refusing the tile
     */
    public void refuseTile(String playerName) {
        if (!game.getGameState().equals(GameState.SHIPS_CREATION)) {
            return;
        }
        if (!playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)) {
            try {
                playersViewMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        Tile refused = game.refuseTile(playerName);
        notifyNewTurnedTile(refused);
        updatePlayerView(S_END_DRAW_TILE_CARD, playerName);
    }
    /**
     * Handles a player looking at a card deck.
     * @param playerName player's name
     * @param playersView view of the player
     * @param cardsToLookAt index of deck
     */
    public synchronized void lookGameCards(String playerName, ViewInterface playersView, int cardsToLookAt)  {
        if (playerStateIs(playerName, S_END_DRAW_TILE_CARD)) {
            for (Integer integer: lockedSmallDecks.values()) {
                if (integer == cardsToLookAt) {
                    try{
                    playersView.showWrongInputMessage();
                    } catch(Exception ignored) {
                    }
                    return;
                }
            }
            lockedSmallDecks.put(playerName, cardsToLookAt);
            try {
                playersView.notifyYouCanDrawThisCardDeck();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            updatePlayerView(ClientState.S_MANAGE_CARDS, playerName);
            notifyNotAvailableCardDeck();
        }
        else {
            try {
                playersView.showWrongInputMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Notifies all players about which card decks are not available.
     */
    private void notifyNotAvailableCardDeck() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyNotAvailableCardDeck(notAvailableCardDecks());
            }
            catch (Exception ignored) {
            }
        }
    }
    /**
     * Gets the list of indexes of unavailable small decks.
     * @return list of locked deck indexes
     */
    private ArrayList<Integer> notAvailableCardDecks() {
        return new ArrayList<>(lockedSmallDecks.values());
    }
    /**
     * Handles cancellation of card inspection.
     * @param playersView view of the player
     * @param playerName player's name
     */
    public void stopLookingAtCards(ViewInterface playersView, String playerName) {
        if (clientsStatesMap.get(playerName) != ClientState.S_MANAGE_CARDS) {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        lockedSmallDecks.remove(playerName);
        updatePlayerView(S_END_DRAW_TILE_CARD, playerName);
        notifyNotAvailableCardDeck();
    }

    /**
     * Sets the tile on the player's ship board.
     * @param playersView the player view
     * @param playerName the player's name
     * @param tile the tile to set
     */
    public void setTile (ViewInterface playersView, String playerName, Tile tile) {
        Tile settedTile;
        if (!tile.isBooked()) {
            if (!(clientsStatesMap.get(playerName) == ClientState.S_MANAGE_DRAWN_TILE || clientsStatesMap.get(playerName) == S_END_DRAW_TILE_CARD || clientsStatesMap.get(playerName) == WAIT) ) {
                try {
                    playersView.showWrongInputMessage();
                } catch (Exception ignored) {
                }
                return;
            }
        } else {
            if (playerStateIs(playerName, ClientState.S_MANAGE_CARDS) || playerStateIs(playerName, ClientState.S_FINISHED)) {
                try {
                    playersView.showWrongInputMessage();
                } catch (Exception ignored) {
                }
                return;
            }
            settedTile = game.drawAndPositionBookedTile(playerName, tile);
            if (settedTile == null) {
                try {
                    playersView.showWrongInputMessage();
                } catch (Exception ignored) {
                }
                return;
            }
            notifyRemovedBookedTile(playerName, settedTile);
        }
        settedTile = game.playerSetTile(playerName, tile);
        if (settedTile != null) {
            updatePlayerView(S_END_DRAW_TILE_CARD, playerName);
            notifyPositionedTile(playerName, settedTile.send());

        } else {
            try {
                playersView.showWrongInputMessage();
            } catch (Exception ignored) {
            }
        }
    }
    /**
     * Notifies all players that a tile has been positioned by a player on their shipboard.
     *
     * @param playerName the player who placed the tile
     * @param tile the tile that was positioned
     */
    private void notifyPositionedTile(String playerName, Tile tile) {
        for (VirtualView view: playersViewMap.values()) {
            try {

                view.notifyPositionedTile(playerName, tile.send());
            } catch (Exception ignored) {}
        }
    }
    /**
     * Notifies players that a booked tile has been removed from a player.
     * @param playerName the player who lost the tile
     * @param tile the tile that was removed
     */
    private void notifyRemovedBookedTile (String playerName, Tile tile) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyRemovedBookedTile(playerName, tile.send());
            } catch (Exception ignored) {}
        }
    }

    /**
     * Books the currently drawn tile for the player.
     * @param playersView the player view
     * @param playerName name of the player
     */
    public void bookTile(ViewInterface playersView, String playerName) {
        if (playerStateIs(playerName,  ClientState.S_MANAGE_DRAWN_TILE)) {
            Tile toBook = game.playerBookTile(playerName);
            if (toBook != null) {
                updatePlayerView(S_END_DRAW_TILE_CARD, playerName);
                notifyBookedTile(playerName, toBook);
            }
            else {
                try{
                playersView.showWrongInputMessage();
                } catch(Exception ignored) {}
            }
        }
        else  {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
    }

    /**
     * Handles player completion of their ship.
     * @param playerName name of the player
     * @param playersView player's view
     */
    public void completed (String playerName, ViewInterface playersView)  {
        if (!playerStateIs(playerName, S_END_DRAW_TILE_CARD)) {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        updatePlayerView(ClientState.S_FINISHED, playerName);
        if(game.getMode() == TRIAL) {
            for (Player player : game.getFlightBoard().getAllPlayers()) {
                if (player.getPlayerName().equals(playerName)) {
                    game.getFlightBoard().addToTrialFlightBoard(player);
                    notifyPlayerMovement(playerName, player.getPlayerColor(), player.getPlayerPosition(), player.getPlayerRanking());
                    //It's not important for trial flight, so 0 is a placeholder value
                    break;
                }
            }
        }
        checkIfAllPlayersReady();
    }
    /**
     * Assigns the player to a position on the FlightBoard.
     * @param playerName player's name
     * @param playersView view of the player
     * @param position the selected position
     */
    public void setPosition (String playerName, VirtualView playersView, int position) {
        if (!playerStateIs(playerName, ClientState.S_FINISHED)) {
            return;
        }
        FlightBoard flightBoard = game.getFlightBoard();
        Player player = game.identifyPlayerByName(playerName);

        if (flightBoard.addToFlightBoard(player, position)) {
            notifyPlayerMovement(playerName, player.getPlayerColor(), player.getPlayerPosition(), player.getPlayerRanking());
            checkIfAllPlayersReady();

        } else {
            try {
                playersView.showWrongInputMessage();
                updatePlayerView(S_FINISHED, playerName);

            } catch (Exception ignored) {
            }
        }

    }
    /**
     * Notifies all players of the updated flight board position and ranking of a player.
     *
     * @param playerName the name of the player whose position was updated
     * @param playersColor the color of the player
     * @param position the new position of the player
     * @param ranking the new ranking of the player
     */
    private void notifyPlayerMovement(String playerName,PlayersColor playersColor, int position, int ranking) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyPlayerMovement(playerName, playersColor, position, ranking);
            } catch (Exception ignored) {}
        }
    }
    /**
     * Sends the initial position and setup information for a player to all connected views.
     * Also initializes ship boards on the client side.
     *
     * @param playerName the name of the player to initialize
     * @param playersColor the color of the player
     */
    private void notifyStartPosition(String playerName,PlayersColor playersColor) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyPlayerMovement(playerName, playersColor, 0, 0);
                view.initializeShipBoards(this.game.getMode());
            } catch (Exception ignored) {}
        }
    }
    /**
     * Notifies all connected views about tiles that were modified on a player's shipboard.
     *
     * @param playerName the name of the player whose tiles were modified
     * @param modifiedTiles the list of modified tiles
     */
    private void notifyModifiedTiles(String playerName, ArrayList<Tile> modifiedTiles) {
        ArrayList<Tile> sendableTiles = new ArrayList<>();
        for(Tile tile: modifiedTiles) {
            sendableTiles.add(tile.send());
        }
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyModifiedTiles(playerName, sendableTiles);
            } catch (Exception ignored) {}
        }
    }
    /**
     * Checks if all players have completed their ship and are ready to continue.
     */
    private void checkIfAllPlayersReady() {
        if (game.getListOfAllPlayer().size() == game.getListOfInFlightPlayers().size()) {
            cancelTimer();
            this.endShipCreation();
        } else if (hourglassTurns == 3 && !hourglassON) {
            if (disconnectedPlayers.size() == (game.getPlayerCount()) - game.getListOfInFlightPlayers().size()) {
                for (Player player: disconnectedPlayers.values()) {
                    game.getFlightBoard().autoSetInFreeLastPosition(player,game.getPlayerCount());
                }
                this.endShipCreation();
            }
        }
    }
    /**
     * Ends the ship creation phase and transitions to the next phase.
     */
    private void endShipCreation () {
        game.endShipCreation();
    }


    /**
     * Handles the player's action of turning the hourglass during the ship construction phase.
     * <p>
     * - If in TRIAL mode, the hourglass doesn't actually get turned, the game just begins.<br>
     * - In LEVEL2 mode, allows up to 3 hourglass turns.<br>
     * - The first two turns can be done freely.<br>
     * - The third turn can only be initiated by a player who has completed their ship.<br>
     * - Starts a countdown timer upon valid hourglass turn.
     *
     * @param playerName the name of the player turning the hourglass
     */
    public synchronized void turnHourglass(String playerName)  {
        ViewInterface playersView = this.getViewFromNickname(playerName);
        if (game.getMode() == TRIAL) {
            game.startShipCreation();
            updateEveryView(S_END_DRAW_TILE_CARD);
            return;
        }

        if (hourglassON) {
            try {
                playersView.showErrorMessage("hourglass is already trickling");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        switch (hourglassTurns) {
            case 0->{
                game.startShipCreation();
                updateEveryView(S_END_DRAW_TILE_CARD);
                startTimer();
            }
            case 1->{
                startTimer();
            }
            case 2-> {
                if (playerStateIs(playerName, ClientState.S_FINISHED)) {
                    startTimer();
                }
                else {
                    try{
                        playersView.showWrongInputMessage();
                    } catch(Exception ignored) {}
                }
            }
            default->
                throw new IllegalStateException("Unexpected value: " + hourglassTurns + "\n");
        }
    }
    /**
     * Starts a 95-second countdown timer triggered by the hourglass.
     * <p>
     * - Increments the hourglass turn counter.<br>
     * - When the timer ends, notifies all players and, if it's the third turn,
     *   forces all remaining players to end ship construction.
     */
    public void startTimer() {
        hourglassTimer = new Timer();
        this.hourglassTurns++;
        hourglassON = true;
        notifyTurnedHourglass();

        hourglassTask = new TimerTask() {
            @Override
            public void run() {
            hourglassON = false;
            notifyEndOfTime();
            if (hourglassTurns == 3) {
                updateEveryView(ClientState.S_FINISHED);
                checkIfAllPlayersReady();
            }
            }
        };
        hourglassTimer.schedule(hourglassTask, 95000); //95 seconds
    }
    /**
     * Cancels the hourglass timer and task.
     */
    public void cancelTimer() {
        if (hourglassTask != null) {
            hourglassTask.cancel();
        }
        if (hourglassTimer != null) {
            hourglassTimer.cancel();
            hourglassTimer.purge();
            hourglassTimer =  null;
        }
        hourglassON = false;
    }
    /**
     * Notifies all players that the hourglass has been turned.
     */
    public void notifyTurnedHourglass() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyTurnedHourglass(hourglassTurns);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Notifies all players that the hourglass timer has expired.
     */
    public void notifyEndOfTime() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyEndOfTime();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Determines the next player eligible to draw a card in the flight phase.
     * <p>
     * - Stops the autosave mechanism.<br>
     * - Automatically forces early landing for players with no human crew.<br>
     * - If all players have landed or none are left in flight, the game is concluded.<br>
     * - Otherwise, sets the next available (connected) player to DRAW_CARD state.
     */
    public void askFirstPlayerToDraw() {
        stopAutoSave();
        GameSaver.save(this);
        //updatePlayerView(DRAW_CARD,game.getListOfInFlightPlayers().getFirst().getPlayerName())
        // ;
        for(Player player : game.getListOfInFlightPlayers()) {
            if(player.getShipBoard().getNumHumanCrew()<=0)
                playersToEarlyLand.add(player);
        }
        for (Player player: playersToEarlyLand) {
            game.getFlightBoard().earlyLanding(player);
            try {
                playersViewMap.get(player.getPlayerName()).notifyEarlyLanding();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        playersToEarlyLand.clear();
        if (game.getListOfInFlightPlayers().isEmpty()) {
            System.out.println("Game ends because everyone earlyLanded");
            concludeGame();
            return;
        }
        if(game.getFlightBoard().concludeMovement() &&game.getListOfInFlightPlayers()!=null&& !game.getListOfInFlightPlayers().isEmpty() ) {
            int i = 0;
            Player currentPlayer =  game.getListOfInFlightPlayers().getFirst();
            while (currentPlayer.IsDisconnected()) {
                i++;
                if (i > game.getListOfInFlightPlayers().size() - 1) {
                    System.out.println("Game ends because everyone is disconnected");
                    concludeGame();
                    return;
                }
                currentPlayer = game.getListOfInFlightPlayers().get(i);
            }
            for(Player player : game.getListOfInFlightPlayers()){
                if(player!=currentPlayer) {
                   updatePlayerView(WAIT_TO_DRAW,player.getPlayerName());
                }
            }
            updatePlayerView(DRAW_CARD, currentPlayer.getPlayerName());
        }
        else{
            System.out.println("Game ends because flightboard empty");
            concludeGame();
        }
    }
    /**
     * Initializes the currently drawn card by applying its effects.
     * This method delegates the initialization logic to the drawn card,
     * which will perform any setup needed for the card event.
     */
    public void initializeDrawnCard () {
        game.getDrawnCard().initializeCard(game, playersViewMap);
    }

    /**
     * Draws the next card in the event deck if the player is the first in flight order.
     * If no cards are left or all players have landed, the game is concluded.
     * Otherwise, the drawn card is saved, announced, and the game state advances to CARD_EVENT.
     *
     * @param playerName the name of the player attempting to draw the card
     * @param playersView the view of the player drawing the card
     */
    public void drawCard (String playerName, VirtualView playersView) {
        if (game.getCardsLeft() == 0) {
            game.endCardPhase();
            concludeGame();
            return;
        }

        if (game.getListOfInFlightPlayers().isEmpty()) {
            concludeGame();
            return;
        }
        if (!game.identifyPlayerByName(playerName).equals(game.getListOfInFlightPlayers().getFirst())){
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
        else {
            game.drawCard();
            notifyDrawnCard(game.getDrawnCard());
            game.setGameState(CARD_EVENT);
        }
    }

    /**
     * Notifies all players about a drawn card.
     * @param card the drawn card
     */
    public void notifyDrawnCard (Card card) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyDrawnCard(card);
            } catch (Exception ignored) {}
        }

    }

    /**
     * Handles a player's decision to perform an early landing.
     * If the current state is DRAW_CARD, the player is immediately removed from the flight.
     * If the state is CARD_EVENT, the early landing is deferred until the event is resolved.
     *
     * @param playerName the name of the player requesting early landing
     * @param playersView the player's associated view
     */
    public void earlyLanding (String playerName, VirtualView playersView) {
        Player player = game.identifyPlayerByName(playerName);
        if (game.getGameState() == GameState.DRAW_CARD) {
            game.getFlightBoard().earlyLanding(player);
            if (game.getListOfInFlightPlayers().isEmpty()) {
                game.endCardPhase();
            }
        }
        else if (game.getGameState() == GameState.CARD_EVENT) {
            playersToEarlyLand.add(player);
        //    game.getDrawnCard().playerLanded(playerName);
        }
        // updatePlayerView(WAIT, playerName);
    }


    /**
     * Called when a player chooses to activate engines.
     *
     * @param playerName the player activating engines
     * @param numDoubleEngines the number of double engines used
     * @param coordinates the coordinates of engines used
     */
    public void playerUsesEngines (String playerName, int numDoubleEngines, ArrayList<Coordinates>  coordinates) {
        game.getDrawnCard().engineChoice(playerName, numDoubleEngines, coordinates);
    }
    /**
     * Called when a player uses cannons during a card event.
     *
     * @param playerName the player using cannons
     * @param doubleCannonPower the power of the double cannons used
     * @param coordinates coordinates of the cannons used
     */
    public void playerUsesCannons (String playerName, float doubleCannonPower, ArrayList<Coordinates>  coordinates) {
        game.getDrawnCard().cannonChoice(playerName, doubleCannonPower, coordinates);
    }
    /**
     * Called when a player manages goods in cargo holds.
     *
     * @param playerName the player managing goods
     * @param clientCredits number of credits held by the client
     * @param updatedCargos updated list of cargo hold tiles
     */
    public void playerManagesGoods (String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        game.getDrawnCard().manageGoods(playerName, clientCredits, updatedCargos);
    }
    /**
     * Called when a player is required to make a binary choice (yes/no).
     *
     * @param playerName the player making the choice
     * @param choice the boolean value representing the player's decision
     */
    public void playerMakesAChoice (String playerName, boolean choice) {
        game.getDrawnCard().choice(playerName, choice);
    }
    /**
     * Called when a player selects a planet.
     *
     * @param playerName the name of the player
     * @param planet the index of the selected planet
     */
    public void playerChoosesPlanet (String playerName, int planet) {
        game.getDrawnCard().planetChoice(playerName, planet);
    }
    /**
     * Called when a player chooses to remove crew members from specific tiles.
     *
     * @param playerName the player making the choice
     * @param toRemoveFrom list of coordinates from which to remove crew
     */
    public void playerRemovesCrew (String playerName, ArrayList<Coordinates> toRemoveFrom) {
        game.getDrawnCard().removeCrew(playerName, toRemoveFrom);
    }
    /**
     * Called when a player removes goods from cargo tiles.
     *
     * @param playerName the player performing the action
     * @param toRemoveFrom list of coordinates from which goods are removed
     */
    public void playerRemovesGoods(String playerName, ArrayList<Coordinates> toRemoveFrom) {
        game.getDrawnCard().removeGoods(playerName, toRemoveFrom);
    }
    /**
     * Called when a player uses batteries located on the ship.
     *
     * @param playerName the name of the player
     * @param batteries list of battery tile coordinates to use
     */
    public void playerUsesBatteries(String playerName, ArrayList<Coordinates> batteries) {
        game.getDrawnCard().useBatteries(playerName, batteries);
    }
    /**
     * Called when a player rolls dice during an event.
     *
     * @param playerName the player rolling the dice
     */
    public void playerRollsTheDices(String playerName) {
        game.getDrawnCard().rollTheDices(playerName);
    }
    /**
     * Called when a player selects a path or branch in an event.
     *
     * @param playerName the name of the player
     * @param branch the chosen branch coordinates
     */
    public void playerChoosesBranch(String playerName, ArrayList<Coordinates> branch) {
        game.getDrawnCard().branchChoice(playerName, branch);
    }


    /**
     * Triggers the end-game logic, scoring and podium.
     */
    public synchronized void concludeGame() {
        for (Player player : game.getFlightBoard().getAllPlayers()) {
            if (player != null) {
                game.getFlightBoard().earlyLanding(player);
                switch (player.getPlayerRanking()){
                    case 1:
                        switch (game.getMode()) {
                            case TRIAL:
                                player.addCredit(4);
                                break;
                            case LEVEL2:
                                player.addCredit(8);
                                break;
                        }
                        break;
                    case 2:
                        switch (game.getMode()) {
                            case TRIAL:
                                player.addCredit(3);
                                break;
                            case LEVEL2:
                                player.addCredit(6);
                                break;
                        }
                        break;
                    case 3:
                        switch (game.getMode()) {
                            case TRIAL:
                                player.addCredit(2);
                                break;
                            case LEVEL2:
                                player.addCredit(4);
                                break;
                        }
                        break;
                    case 4:
                        switch (game.getMode()) {
                            case TRIAL:
                                player.addCredit(1);
                                break;
                            case LEVEL2:
                                player.addCredit(2);
                                break;
                        }
                        break;
                }
            }
        }
        ArrayList<Player> coolestPlayers = new ArrayList<>();
        coolestPlayers.add(game.getFlightBoard().getAllPlayers().getFirst());
        for(Player player : game.getFlightBoard().getAllPlayers()){
            if (player.getShipBoard().countExposedConnectors() < coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
                coolestPlayers.clear();
                coolestPlayers.add(player);
            }
            if (player.getShipBoard().countExposedConnectors() == coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
                coolestPlayers.add(player);
            }
        }
        for (Player player : coolestPlayers) {
            switch (game.getMode()) {
                case TRIAL:
                    player.addCredit(2);
                case LEVEL2:
                    player.addCredit(4);
            }
        }
        for (Player player : game.getFlightBoard().getAllPlayers()) {
            player.addCredit(player.getShipBoard().convertGoodsToCredit());
        }
        for (Player player : game.getFlightBoard().getAllPlayers()) {
            player.removeCredit(player.getShipBoard().getPenalty());
        }
        for (String playerName: playersViewMap.keySet()) {
            if(activePlayers!=null && !activePlayers.isEmpty())
                notifyPlayerCredits(playerName, activePlayers.get(playerName).getCredit());
        }
        // Sort players based on their credits in descending order
        game.setPodium();
        showScores();
    }
    /**
     * Displays the final scores to all players.
     */
    public void showScores() {
        Map<String,Integer> scores = new HashMap<>();
        for (Player player: game.getListOfAllPlayer())
            scores.put(player.getPlayerName(), player.getCredit());
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.showScores(scores);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Notifies all players about a change in a player's credit attribute
     * @param playerName player's name
     * @param credits new credits value
     */
    public void notifyPlayerCredits (String playerName, int credits) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyGainedCredits(playerName, credits);
            } catch (Exception ignored) {}
        }
    }
    /**
     * Returns the current game mode and model.
     * @return the GameInterface instance
     */
    public GameInterface getGame() {
        return game;
    }
    /**
     * Returns the current game state.
     * @return the current GameState
     */
    public GameState getGameState() {
        return game.getGameState();
    }
    /**
     * Returns a map of player names to their views.
     * @return map of player views
     */
    public Map<String, VirtualView> getPlayersViewMap() {
        return playersViewMap;
    }
    /**
     * Returns a map of active players.
     * @return map of players
     */
    public Map<String, Player> getActivePlayers() {
        return activePlayers;
    }
    /**
     * Returns the name of this game session.
     * @return the game name
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns true if there are no more players in the game.
     * @return true if empty
     */
    public boolean isGameEmpty() {
        return (activePlayers.isEmpty());
    }

    /**
     * Updates every connected player's view with a new client state.
     * @param newState the new client state
     */
    public void updateEveryView(ClientState newState) {
        for (String playerName:  playersViewMap.keySet()) {
            try{
            playersViewMap.get(playerName).setClientState(newState);
            } catch(Exception ignored) {}
            clientsStatesMap.put(playerName, newState);
        }
    }
    /**
     * Updates a single player's view with a new state.
     * @param newState the new client state
     * @param playerName player to update
     */
    public void updatePlayerView (ClientState newState, String playerName) {
        try {
            playersViewMap.get(playerName).setClientState(newState);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        clientsStatesMap.put(playerName, newState);
    }
    /**
     * Verifies the current state of the player.
     * @param playerName player's name
     * @param stateToVerify expected state
     * @return true if matches, false otherwise
     */
    public boolean playerStateIs(String playerName,ClientState stateToVerify) {
        return clientsStatesMap.get(playerName) == stateToVerify;
    }

    /**
     * Returns the view associated with a player name.
     * @param playerName player identifier
     * @return VirtualView of the player
     */
    public VirtualView getViewFromNickname(String playerName) {
        return playersViewMap.get(playerName);
    }

    public void pingPong(String playerName, VirtualView view){
        Player player=activePlayers.get(playerName);
        CompletableFuture<Void> future = new CompletableFuture<>();
        pendingPongs.put(playerName, future);
        try{
            view.ping();
            future.get(1500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | RemoteException | ExecutionException | TimeoutException e) {
            player.playerDisconnects();
            prepareForDisconnection(playerName);
            activePlayers.remove(playerName);
            playersViewMap.remove(playerName);
            disconnectedPlayers.put(playerName,player);
            System.out.println(playerName+" disconnected");
            notifyPlayerDisconnected(playerName);
        } finally {
            pendingPongs.remove(playerName);
        }
    }
    public void pong(String nickname) {
        CompletableFuture<Void> future = pendingPongs.get(nickname);
        if (future != null) {
            future.complete(null);
        }
    }

    public ConcurrentHashMap<String, Integer> getLockedSmallDecks() {
        return lockedSmallDecks;
    }

    public Map<String, Player> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }

    /**
     * Converts the internal state of the controller to a string (for save purposes).
     * @return a serialized state string
     */
    public String toStringData() {
        //First gameName and hourglassTurns, second clientStates, third playersWithErrors
        return gameName + " " + hourglassTurns + " ";
    }
    /**
     * Loads specific data (like hourglass state) from a saved state.
     * @param attributes string array representing saved state
     */
    public void dataLoader(String[] attributes) {
        // GameName should be taken from game file name or should be used to create the gameController
        this.hourglassTurns = Integer.parseInt(attributes[1]);
    }
    /**
     * Retrieves the full list of active + disconnected players.
     * @return a list of all players in the session
     */
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        if (!activePlayers.isEmpty()) {
            players.addAll(activePlayers.values());
        }
        if (!disconnectedPlayers.isEmpty()) {
            players.addAll(disconnectedPlayers.values());
        }
        return players;
    }
    /**
     * Returns whether this game was restored from disk.
     * @return true if it was restarted
     */
    public boolean isRestarted() {
        return restarted;
    }

    /**
     * Starts periodic autosave task.
     */
    public void startAutoSave() {
        if (autoSaveExecutor != null && !autoSaveExecutor.isShutdown()) {
            autoSaveExecutor.shutdown();
        }
        autoSaveExecutor = Executors.newSingleThreadScheduledExecutor();
        autoSaveExecutor.scheduleAtFixedRate(() -> {
            if (autoSaveEnabled) {
                System.out.println("Auto-saving game...");
                GameSaver.save(this);  // Saves game state
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
    /**
     * Stops the autosave thread.
     */
    public void stopAutoSave() {
        autoSaveEnabled = false;
        if (autoSaveExecutor != null && !autoSaveExecutor.isShutdown()) {
            autoSaveExecutor.shutdown();
        }
    }
    /**
     * Gets the current number of hourglass turns.
     * @return number of turns
     */
    public int getHourglassTurns() {
        return hourglassTurns;
    }

    /**
     * Removes a player from the game entirely (disconnect and forget).
     * This happens only by player's choice.
     * @param playerName player name
     */
    public void playerLeaves(String playerName) {
        if (activePlayers.containsKey(playerName)) {
            Player removedPlayer = activePlayers.remove(playerName);
            removedPlayer.playerDisconnects();
            prepareForDisconnection(playerName);
            if (clientsStatesMap.get(playerName).equals(ClientState.S_MANAGE_CARDS)) {
                stopLookingAtCards(getViewFromNickname(playerName), playerName);
            }
            clientsStatesMap.remove(playerName);
            if (game.getListOfAllPlayer().contains(removedPlayer)) {
                game.getFlightBoard().removePlayer(removedPlayer);
                game.setPlayerCount(game.getPlayerCount() - 1);
            }
        }
        playersViewMap.remove(playerName);
        notifyPlayerLeftTheGame(playerName);
    }
    /**
     * Prepares the controller and game model for a player disconnection.
     * @param playerName the player who disconnected
     */
    public void prepareForDisconnection(String playerName) {
        switch(this.getGameState()) {
            case SHIPS_CREATION -> checkIfAllPlayersReady();
            case VERIFY_SHIP_CORRECTNESS -> verifyShipCorrectness();
            case DRAW_CARD -> {
                if (!game.getListOfInFlightPlayers().isEmpty()&&playerName.equals(game.getListOfInFlightPlayers().getFirst().getPlayerName())) {
                    if(activePlayers!=null && !activePlayers.isEmpty())
                        this.askFirstPlayerToDraw();
                }
            }
            case CARD_EVENT -> skipPlayersTurn(playerName);
        }
    }
    /**
     * Skips the disconnected player's turn during a card event.
     * @param playerName name of the player to skip
     */
    public void  skipPlayersTurn(String playerName) {
        game.getDrawnCard().playerDisconnected(playerName);
    }


    public void notifyPlayerDisconnected(String playerName) {
        for (VirtualView view: playersViewMap.values()) {
            try{
                view.notifyPlayerDisconnected(playerName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyPlayerLeftTheGame(String playerName) {
        for (VirtualView view: playersViewMap.values()) {
            try{
                view.notifyPlayerLeft(playerName);
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // For test purposes
    public void setHourglassTurns(int hourglassTurns) {
        this.hourglassTurns = hourglassTurns;
    }

    public void setHourglassStatus(boolean hourglassStatus) {
        hourglassON =  hourglassStatus;
    }

    public void emptyHourglass() {
        hourglassTask.run();
    }
}