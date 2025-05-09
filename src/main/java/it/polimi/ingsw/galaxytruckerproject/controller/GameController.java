package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.Observer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
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

import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.TRIAL;


public class GameController implements Observer, Serializable {
    private final String gameName;
    private final GameInterface game;
    private final ArrayList<String> playersWithErrors;
    private final Map<String, ClientState> clientsStatesMap = new HashMap<>();
    private final Map<String, VirtualView> playersViewMap;
    private final Map<String, Player> activePlayers;
    private final Map<String, Player> disconnectedPlayers;
    private final Map<String, CompletableFuture<Void>> pendingPongs = new ConcurrentHashMap<>();
    private final ArrayList<Player> playersToEarlyLand = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> lockedSmallDecks = new ConcurrentHashMap<>();
    private int hourglassTurns = 0;
    private boolean hourglassON = false;


    public String toString(){
        return gameName+"\ngame state:"+game.getGameState().toString()+"\nplayer needed: "+game.getPlayerCount()+"\nplatyer in game: "+game.getNumberOfPlayers();
    }

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

    public GameController(GameInterface game, String gameName) {
        this.gameName = gameName;
        this.game = game;
        game.addObserver(this);
        this.playersWithErrors = new ArrayList<>();
        this.playersViewMap = new HashMap<>();
        this.activePlayers = new HashMap<>();
        this.disconnectedPlayers = new HashMap<>();
    }

    /**
     * Adds player to a game that's still in lobby phase or if player is reconnecting
     * it calls for reconnectPlayer method
     * @param playerName player that just joined the game but still isn't istanced
     * @param view player's view
     * @param reconnecting true if player used to be in the lobby
     */

    //ASSOCIA IL PLAYER ALLA VIEW
    public void addToPlayersViewMap(String playerName, VirtualView view, boolean reconnecting) {
        try {
            view.setGameMode(this.game.getMode());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (reconnecting) {
            reconnectPlayer(playerName, view);
        }
        else {
            if (this.getGameState().equals(GameState.LOBBY_PHASE)){
                playersViewMap.put(playerName, view);
                try {
                    view.setClientState(ClientState.COLOR_CHOICE);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
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
     * only reconnects player if either it's still verify_ship phase or they have already
     * corrected their ship and chosen a starting position. If they disconnected after their
     * phase just puts the player in LAST position FOR NOW!!!!!!
     * @param playerName reconnecting player
     * @param view their view
     */

    //RICONNETTE IL PLAYER SE DISCONNESSO
    public void reconnectPlayer(String playerName, VirtualView view)  {
        if (disconnectedPlayers.containsKey(playerName)) {
            //If player disconnected during ships verification without fixing the ship
            if (playersWithErrors.contains(playerName)) {
                if (!this.getGameState().equals(GameState.VERIFY_SHIP_CORRECTNESS)) {
                    try {
                        view.showWrongInputMessage();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                else{
                    try {
                        view.asksToInputCoordinates(CoordReqType.CHOOSE_TO_BREAK);
                    } catch(Exception ignored) {}
                }
            }
            // if player disconnected during ships verification without choosing a starting position
            if (disconnectedPlayers.get(playerName).getPlayerPosition() == 0 && disconnectedPlayers.get(playerName).getPlayerRanking() == 0 &&
                    !this.getGameState().equals(GameState.START_GAME) ||
                    !this.getGameState().equals(GameState.SHIPS_CREATION) ||
                    !this.getGameState().equals(GameState.LOBBY_PHASE)) {

                if (this.getGameState() != GameState.VERIFY_SHIP_CORRECTNESS) {
                    try {
                        view.showWrongInputMessage();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                else {
                    try {
                        view.setClientState(ClientState.S_FINISHED);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Player reconnectingPlayer = disconnectedPlayers.get(playerName);
            playersViewMap.put(playerName, view);
            activePlayers.put (playerName, reconnectingPlayer);
            reconnectingPlayer.playerReconnects();
            if (GameState.SHIPS_CREATION.equals(this.getGameState())) {
                updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
            }
            updateReconnectedPlayer(view);
        }
        else {
            try {
                view.showWrongInputMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateReconnectedPlayer(VirtualView view) {
        Map<String, LightShipBoard> updatedShipBoards = new HashMap<>();
        for (Player player: game.getListOfAllPlayer()) {
            updatedShipBoards.put(player.getPlayerName(), new LightShipBoard(player.getShipBoard()));
        }
        try {
            view.notifyChangesWhileGone(updatedShipBoards, new LightFlightboard(game.getFlightBoard()),
                    game.getDrawnCard(), hourglassTurns, game.getTurnedTiles(), notAvailableCardDecks());
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
                        playersViewMap.get(player.getPlayerName()).notifyNotAvailableColor(playersColor);
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

    public VirtualView removePlayer (String playerName) {
        if (activePlayers.containsKey(playerName)) {
            Player removedPlayer = activePlayers.get(playerName);
            if (removedPlayer.getDrawnTile() != null) {
                removedPlayer.removeDrawnTile();
            }
            disconnectedPlayers.put(playerName, removedPlayer);
            activePlayers.remove(playerName);
            removedPlayer.playerDisconnects();
            if (clientsStatesMap.get(playerName).equals(ClientState.S_MANAGE_CARDS)) {
                stopLookingAtCards(getViewFromNickname(playerName), playerName);
            }
            clientsStatesMap.remove(playerName);
            if (game.getListOfAllPlayer().contains(removedPlayer)) {
                game.getFlightBoard().removePlayer(removedPlayer);
            }
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
        if (! playersViewMap.containsKey(playerName)) {
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
     * Starts the game as soon as one of the players turns the hourglass if LEVEL2 game,
     * if TRIAL starts the game without hourglass
     */
    public void startGame() {
        game.startShipCreation();
        if (game.getMode() == TRIAL) {
            for (Player player : game.getListOfAllPlayer()) {
                notifyStartPosition(player.getPlayerName(),player.getPlayerColor());
            }
            updateEveryView(ClientState.S_END_DRAW_TILE_CARD);
        }
        else {
            for (Player player : game.getListOfAllPlayer()) {
                notifyStartPosition(player.getPlayerName(),player.getPlayerColor());
            }
            updateEveryView(ClientState.START_SHIP_CREATION);
            notifyFlightBoardCards();
        }
    }

    public void notifyFlightBoardCards() {
        Map<Integer,ArrayList<Card>> flightBoardCards = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            flightBoardCards.put(i,game.getInGameCards(i));
        }
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyFlightBoardCards(flightBoardCards);
            } catch (Exception ignored) {

            }
        }
    }

    public  void drawTile(VirtualView playersView, String playerName, int index, boolean turned)  {

        // Can't draw if the shipboard is completed or there is already a tile to place/book/refuse
        if (playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)
                || playerStateIs(playerName, ClientState.S_FINISHED)) {
            try {
                playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
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
            try{
            playersView.showDrawnTile(drawnTile.send());
            } catch(RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            drawnTile = game.drawTurnedTile(playerName, index);
                if (drawnTile == null) {
                    try {
                        playersView.showWrongInputMessage();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                try{
                playersView.showDrawnTile(drawnTile.send());
                } catch(Exception ignored) {}
                notifyRemoveTurnedTile(drawnTile);
        }
        updatePlayerView(ClientState.S_MANAGE_DRAWN_TILE, playerName);
    }

    public void notifyRemoveTurnedTile(Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyRemoveTurnedTile(tile.send());
            } catch (Exception ignored) { //unhandled exception
            }
        });
    }

    public void notifyBookedTile (String playerName, Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            if(virtualView!=playersViewMap.get(playerName))
                try {
                    virtualView.notifyBookedTile(playerName, tile.send());
                } catch (Exception ignored) {//unhandled exception
                }
        });
    }

    public void notifyNewTurnedTile(Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyNewTurnedTile(tile.send());
            } catch (Exception ignored) { //unhandled exception
            }
        });
    }

    //errors check and management
    public void verifyShipCorrectness() {
        for (Player player :new ArrayList<>(game.getListOfInFlightPlayers()) ) {
            boolean correctness = player.getShipBoard().verifyCorrectness();
            ViewInterface playersView = this.getViewFromNickname(player.getPlayerName());
            if (correctness){
                if (player.IsDisconnected()) {
                    this.setCrewForDisconnectedPlayer(player);
                }
                else {
                    try{
                    playersView.setClientState(ClientState.MANAGE_CABINS);
                    } catch(Exception ignored) {}
                }
            } else {
                try{
                playersView.asksToInputCoordinates(CoordReqType.CHOOSE_TO_BREAK);
                } catch(Exception ignored) {}
                playersWithErrors.add(player.getPlayerName());
            }
        }
    }

    public void shipErrorManagement(String playerName, VirtualView playersView, ArrayList<Coordinates> toRemove) {
        Player player = game.identifyPlayerByName(playerName);

        if(player==null){
            return;
        }
        if(!playersWithErrors.contains(playerName)){
            try {
                playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        for (Coordinates coord: toRemove) {
            player.getShipBoard().destroyForCorrection(coord);
        }
        notifyBrokenTile(playerName,toRemove);

        boolean correctness = player.getShipBoard().verifyCorrectness();
        if (correctness) {
            if(game.getMode()== TRIAL) {
                game.getFlightBoard().setPlayerToLast(player);
            }
            playersWithErrors.remove(playerName);
            if (player.IsDisconnected()) {
                this.setCrewForDisconnectedPlayer(player);
            }
            else
                try{
                playersView.setClientState(ClientState.MANAGE_CABINS);
                } catch(Exception ignored) {}
            return;
        }
        try{
        playersView.asksToInputCoordinates(CoordReqType.CHOOSE_TO_BREAK);
        } catch(Exception ignored) {}
    }

    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> removed) {
        for (VirtualView view: playersViewMap.values()){
            try {
                view.notifyBrokenTile(playerName,removed);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setCrewForDisconnectedPlayer(Player player) {
        notifyModifiedTiles(player.getPlayerName(), player.setAllCrewToHuman());
        updatePlayerView(ClientState.WAIT, player.getPlayerName());
    }

    public void playerPicksCrewMembers(String playerName, VirtualView playersView ,ArrayList<Tile> cabins) {
        Player player = game.identifyPlayerByName(playerName);
        if (player == null) {
            return;
        }
        if (player.verifyAndSetupCrew(cabins)) {
            notifyModifiedTiles(playerName, cabins);
            updatePlayerView(ClientState.WAIT, player.getPlayerName());
        }
        else {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }

        ArrayList<Coordinates> cabinsToCheck;
        ShipBoard shipBoard;
        for (Player p1: activePlayers.values()) {
            shipBoard = p1.getShipBoard();
            cabinsToCheck = shipBoard.getCabinsCoordinates();
            for (Coordinates coord: cabinsToCheck) {
                if (shipBoard.getTile(coord).getCrew() == 0)
                    return;
            }
        }
        for (Player p2: disconnectedPlayers.values()) {
            shipBoard = p2.getShipBoard();
            cabinsToCheck = shipBoard.getCabinsCoordinates();
            for (Coordinates coord: cabinsToCheck) {
                if (shipBoard.getTile(coord).getCrew() == 0) {
                    if (playersWithErrors.contains(p2.getPlayerName())) {
                        removePlayer(p2.getPlayerName());
                    }
                    else {
                        p2.setAllCrewToHuman();
                    }
                }
            }
        }
        this.endShipVerification();
    }

    public void endShipVerification() {
        game.endShipVerification();
    }


    public void refuseTile(String playerName) {
        if (!playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)) {
            try {
                playersViewMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        Tile refused = game.refuseTile(playerName);
        notifyNewTurnedTile(refused);
        updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
    }

    public synchronized void lookGameCards(String playerName, ViewInterface playersView, int cardsToLookAt)  {
        if (playerStateIs(playerName, ClientState.S_END_DRAW_TILE_CARD)) {
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

    private void notifyNotAvailableCardDeck() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyNotAvailableCardDeck(notAvailableCardDecks());
            }
            catch (Exception ignored) {
            }
        }
    }

    private ArrayList<Integer> notAvailableCardDecks() {
        return new ArrayList<>(lockedSmallDecks.values());
    }

    public void stopLookingAtCards(ViewInterface playersView, String playerName) {
        if (clientsStatesMap.get(playerName) != ClientState.S_MANAGE_CARDS) {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        lockedSmallDecks.remove(playerName);
        updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
        notifyNotAvailableCardDeck();
    }

    //set drawn tile on the player's shipboard
    public void setTile (ViewInterface playersView, String playerName, Tile tile) {
        Tile settedTile;
        if (!tile.isBooked()) {
            /*if (clientsStatesMap.get(playerName) != ClientState.S_MANAGE_DRAWN_TILE) {
                try{
                playersView.showWrongInputMessage();
                } catch(Exception ignored) {}
                return;
            }*/
        }
        else {
            if (playerStateIs(playerName,ClientState.S_MANAGE_CARDS) || playerStateIs(playerName,ClientState.S_FINISHED)) {
                try{
                playersView.showWrongInputMessage();
                } catch(Exception ignored) {}
                return;
            }
            settedTile = game.drawAndPositionBookedTile(playerName, tile);
            if (settedTile == null) {
                try{
                playersView.showWrongInputMessage();
                } catch(Exception ignored) {}
                return;
            }
            notifyRemovedBookedTile(playerName, settedTile);
        }
        settedTile = game.playerSetTile(playerName, tile);

        if (settedTile != null) {
            updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
            notifyPositionedTile(playerName, settedTile.send());
        }
        else {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
    }

    private void notifyPositionedTile(String playerName, Tile tile) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyPositionedTile(playerName, tile.send());
            } catch (Exception ignored) {}
        }
    }

    private void notifyRemovedBookedTile (String playerName, Tile tile) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyRemovedBookedTile(playerName, tile.send());
            } catch (Exception ignored) {}
        }
    }

    //set currently drawn tile as booked for the player
    public void bookTile(ViewInterface playersView, String playerName) {
        if (playerStateIs(playerName,  ClientState.S_MANAGE_DRAWN_TILE)) {
            Tile toBook = game.playerBookTile(playerName);
            if (toBook != null) {
                updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
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

    //now no input except hourglass and checkShipboard work and checks if the other player have completed
    public void completed (String playerName, ViewInterface playersView)  {
        if (!playerStateIs(playerName, ClientState.S_END_DRAW_TILE_CARD)) {
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
                    //It's not important for trial flight, so 0 is a placeholder value
                    break;
                }
            }
        }
        checkIfAllPlayersReady();
    }

    public void setPosition (String playerName, VirtualView playersView, int position) {
        if (!playerStateIs(playerName, ClientState.S_END_DRAW_TILE_CARD)) {
            return;
        }
        FlightBoard flightBoard = game.getFlightBoard();
        Player player = game.identifyPlayerByName(playerName);

        if (flightBoard.addToFlightBoard(player, position)) {
            notifyPlayerMovement(playerName, player.getPlayerColor(),player.getPlayerPosition(), player.getPlayerRanking());
        }
        else {
            try{
            playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
        checkIfAllPlayersReady();
    }

    private void notifyPlayerMovement(String playerName,PlayersColor playersColor, int position, int ranking) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyPlayerMovement(playerName, playersColor, position, ranking);
            } catch (Exception ignored) {}
        }
    }

    private void notifyStartPosition(String playerName,PlayersColor playersColor) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyPlayerMovement(playerName, playersColor, 0, 0);
                view.initializeShipBoards(this.game.getMode());
            } catch (Exception ignored) {}
        }
    }

    private void notifyModifiedTiles(String playerName, ArrayList<Tile> modifiedTiles) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyModifiedTiles(playerName, modifiedTiles);
            } catch (Exception ignored) {}
        }
    }

    private void checkIfAllPlayersReady() {
        if (game.getListOfAllPlayer().size() == game.getListOfInFlightPlayers().size()) {
            this.endShipCreation();
        }
        else if (hourglassTurns == 3 && !hourglassON) {
            for (Player player : game.getListOfAllPlayer()) {
                if (player.getPlayerRanking() == 0 && player.getPlayerPosition() == 0) {
                    if (player.IsDisconnected()) {
                        removePlayer(player.getPlayerName());
                    }
                    try {
                        playersViewMap.get(player.getPlayerName()).asksToChooseStartingPosition();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void endShipCreation () {
        game.endShipCreation();
    }


    //Turns hourglass isn't on and adds 1 to the turn count,
    // if it's already been turned twice, player that turns it needs to have completed his ship
    public synchronized void turnHourglass(String playerName)  {
        ViewInterface playersView = this.getViewFromNickname(playerName);

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
                updateEveryView(ClientState.S_END_DRAW_TILE_CARD);
                startTimer();
                updateHourglass();
            }
            case 1->{
                startTimer();
                updateHourglass();
            }

            case 2-> {
                if (playerStateIs(playerName, ClientState.S_FINISHED)) {
                    startTimer();
                    updateHourglass();

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

    public void startTimer() {
        Timer hourglass = new Timer();
        this.hourglassTurns++;
        hourglassON = true;
        notifyTurnedHourglass();
        hourglass.schedule(new TimerTask() {
            @Override
            public void run() {
                hourglassON = false;
                notifyEndOfTime();
                hourglass.cancel();
                if (hourglassTurns == 3) {
                    updateEveryView(ClientState.S_FINISHED);
                    checkIfAllPlayersReady();
                }
            }
        }, 95000); //95 seconds
    }

    public void notifyTurnedHourglass() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyTurnedHourglass(hourglassTurns);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyEndOfTime() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyEndOfTime();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void askFirstPlayerToDraw() {
        updatePlayerView(ClientState.DRAW_CARD,game.getListOfInFlightPlayers().getFirst().getPlayerName());
    }

    public void initializeDrawnCard () {
        game.getDrawnCard().initializeCard(game, playersViewMap);
    }

    //Number 1 player can draw
    //Every player can check others shipboard
    //Every player can do an early landing
    //When cards are over go to CONCLUDE_GAME state
    public void drawCard (String playerName, VirtualView playersView) {
        if (game.getCardsLeft() == 0) {
            game.endCardPhase();
            concludeGame();
            return;
        }

        for (Player player: playersToEarlyLand) {
            game.getFlightBoard().earlyLanding(player);
        }
        playersToEarlyLand.clear();

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
        }
    }

    public void notifyDrawnCard (Card card) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyDrawnCard(card);
            } catch (Exception ignored) {}
        }
    }


    public void earlyLanding (String playerName, VirtualView playersView) {
        Player player = game.identifyPlayerByName(playerName);
        if (game.getGameState() == GameState.DRAW_CARD) {
            game.getFlightBoard().earlyLanding(player);
        }
        else if (game.getGameState() == GameState.CARD_EVENT) {
            playersToEarlyLand.add(player);
        }
        updatePlayerView(ClientState.WAIT, playerName);
    }

    /**
     * Card specific methods for player's choices
     */

    public void playerUsesEngines (String playerName, int numDoubleEngines, ArrayList<Coordinates>  coordinates) {
        game.getDrawnCard().engineChoice(playerName, numDoubleEngines, coordinates);
    }

    public void playerUsesCannons (String playerName, float doubleCannonPower, ArrayList<Coordinates>  coordinates) {
        game.getDrawnCard().cannonChoice(playerName, doubleCannonPower, coordinates);
    }

    public void playerManagesGoods (String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        game.getDrawnCard().manageGoods(playerName, clientCredits, updatedCargos);
    }

    public void playerMakesAChoice (String playerName, boolean choice) {
        game.getDrawnCard().choice(playerName, choice);
    }

    public void playerChoosesPlanet (String playerName, int planet) {
        game.getDrawnCard().planetChoice(playerName, planet);
    }

    public void playerRemovesCrew (String playerName, ArrayList<Coordinates> toRemoveFrom) {
        game.getDrawnCard().removeCrew(playerName, toRemoveFrom);
    }

    public void playerRemovesGoods(String playerName, ArrayList<Coordinates> toRemoveFrom) {
        game.getDrawnCard().removeGoods(playerName, toRemoveFrom);
    }

    public void playerUsesBatteries(String playerName, ArrayList<Coordinates> batteries) {
        game.getDrawnCard().useBatteries(playerName, batteries);
    }

    public void playerRollsTheDices(String playerName) {
        game.getDrawnCard().rollTheDices(playerName);
    }

    public void playerChoosesBranch(String playerName, ArrayList<Coordinates> branch) {
        game.getDrawnCard().branchChoice(playerName, branch);
    }


    //PlayerPoint calculation
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
            if (player.getShipBoard().countExposedConnectors() > coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
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
            notifyPlayerCredits(playerName, activePlayers.get(playerName).getCredit());
        }
        // Sort players based on their credits in descending order
        game.setPodium();
        showScores();
    }

    public void showScores() {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.showScores(game.getListOfAllPlayer());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyPlayerCredits (String playerName, int credits) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyGainedCredits(playerName, credits);
            } catch (Exception ignored) {}
        }
    }

    public GameInterface getGame() {
        return game;
    }

    public GameState getGameState() {
        return game.getGameState();
    }

    public Map<String, VirtualView> getPlayersViewMap() {
        return playersViewMap;
    }

    public Map<String, Player> getPlayers() {
        return activePlayers;
    }

    public String getGameName() {
        return gameName;
    }

    /**
     * @return true if there are no more active players
     */
    public boolean isGameEmpty() {
        return (activePlayers.isEmpty());
    }

    public void updateEveryView(ClientState newState) {
        for (String playerName:  playersViewMap.keySet()) {
            try{
            playersViewMap.get(playerName).setClientState(newState);
            } catch(Exception ignored) {}
            clientsStatesMap.put(playerName, newState);
        }
    }

    public void updatePlayerView (ClientState newState, String playerName) {
        try{
        playersViewMap.get(playerName).setClientState(newState);
        } catch(Exception ignored) {}
        clientsStatesMap.put(playerName, newState);
    }

    public boolean playerStateIs(String playerName,ClientState stateToVerify) {
        return clientsStatesMap.get(playerName) == stateToVerify;
    }

    /**
     * returns playersView from playerName
     *
     * @param playerName player to get view for
     * @return player's viewInterface
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
            future.get(5, TimeUnit.SECONDS);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            activePlayers.remove(playerName);
            disconnectedPlayers.put(playerName,player);
            player.setDisconnected(true);
            System.out.println(playerName+" disconnected");
            throw new RuntimeException(e);
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

    public Map<String, Player> getActivePlayers() {
        return activePlayers;
    }

    public ConcurrentHashMap<String, Integer> getLockedSmallDecks() {
        return lockedSmallDecks;
    }

    public void updateHourglass(){
        for(VirtualView view: playersViewMap.values()) {
            try {
                view.notifyTurnedHourglass(hourglassTurns);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}