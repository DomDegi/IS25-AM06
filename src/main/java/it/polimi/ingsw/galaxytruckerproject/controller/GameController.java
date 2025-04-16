package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.*;
import static it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType.*;


public class GameController {
    private final String gameName;
    private final GameInterface game;
    private final ArrayList<String> playersWithErrors;
    private final Map<String, ClientState> clientsStatesMap = new HashMap<>();
    private final Map<String, VirtualView> playersViewMap;
    private final Map<String, Player> activePlayers;
    private final Map<String, Player> disconnectedPlayers;
    private final ArrayList<Player> playersToEarlyLand = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> lockedSmallDecks = new ConcurrentHashMap<>();
    private int hourglassTurns = 0;
    private boolean hourglassON = false;


    public String toString(){
        return gameName+"\ngame state:"+game.getGameState().toString()+"\nplayer needed: "+game.getPlayerCount()+"\nplatyer in game: "+game.getNumberOfPlayers();
    }

    public GameController(GameInterface game, String gameName) {
        this.gameName = gameName;
        this.game = game;
        this.playersWithErrors = new ArrayList<>();
        this.playersViewMap = new HashMap<>();
        this.activePlayers = new HashMap<>();
        this.disconnectedPlayers = new HashMap<>();
    }

    public void processPlayerInput(Message message) {
        switch (game.getGameState()) {
            case GameState.START_GAME: {
                playerAddition(message);
                break;
            }
            case GameState.SHIPS_CREATION: {
                if (hourglassTurns > 0){
                    shipsCreation(message);
                }
                else
                    startGame();
                break;
            }
            case GameState.VERIFY_SHIP_CORRECTNESS: {
                //potremmo toglierla e lasciare le shipboard nel client o il contrario
                if (message.getMessageType().equals(SHOW_PLAYERS_SHIPBOARD_REQUEST)) {
                    checkShipBoard(this.getViewFromNickname(message.getNickname()) ,message);
                } else if  (message.getMessageType().equals(SEND_COORDINATES_RESPONSE)) {
                    shipErrorManagement(message);
                }
                if(playersWithErrors.isEmpty()){
                    verifyShipCorrectness();
                }
                break;
            }
            case GameState.DRAW_CARD: {
                drawCard(message);
                break;
            }
            case GameState.CARD_EVENT: {
                cardEvent(message);
                break;
            }
            case GameState.CONCLUDE_GAME: {
                concludeGame();
                break;
            }
        }
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
        if (reconnecting) {
            reconnectPlayer(playerName, view);
        }
        else {
            if (this.getGameState().equals(GameState.START_GAME)){
                playersViewMap.put(playerName, view);
                view.askColor();
            }
            else{
                view.showGenericMessage("Can't join a game in progress");
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
    public void reconnectPlayer(String playerName, VirtualView view) {
        if (disconnectedPlayers.containsKey(playerName)) {
            //If player disconnected during ships verification without fixing the ship
            if (playersWithErrors.contains(playerName)) {
                if (!this.getGameState().equals(GameState.VERIFY_SHIP_CORRECTNESS)) {
                    view.showErrorMessage("Can't connect because didn't finish ship creation");
                }
                else{
                    view.asksToInputCoordinates();
                }
            }
            // if player disconnected during ships verification without choosing a starting position
            if (disconnectedPlayers.get(playerName).getPlayerPosition() == 0 &&
                    !this.getGameState().equals(GameState.START_GAME) ||
                    !this.getGameState().equals(GameState.SHIPS_CREATION)) {

                if (this.getGameState() != GameState.VERIFY_SHIP_CORRECTNESS) {
                    view.showErrorMessage("Can't reconnect because player didn't complete starting position choosing");
                }
                else {
                    view.asksToChooseStartingPosition();
                }
            }
            Player reconnectingPlayer = disconnectedPlayers.get(playerName);
            playersViewMap.put(playerName, view);
            activePlayers.put (playerName, reconnectingPlayer);
            reconnectingPlayer.playerReconnects();
        }
        else {
            view.showErrorMessage("player was never connected to this game");
        }
    }

    /**
     * adds player to the gameModel after they enter a valid color for their starting cabin
     * @param message a message that has to be of type Set_color_request so that it contains
     * both nickname and color String.
     * If players number reaches the initial setted count, starts game.
     */
    public void playerAddition(Message message) {
        if (message.getMessageType() == SET_COLOR_REQUEST) {
            SetColorRequest messageReceived = (SetColorRequest) message;
            String playerName = message.getNickname();
            String color = messageReceived.getColor();
            PlayersColor playersColor = PlayersColor.valueOf(color);
            if (this.getGameState() == GameState.START_GAME && playersViewMap.containsKey(playerName)) {
                //if playerCount is still to be reached, add player to the game model
                if (game.getNumberOfPlayers() < game.getPlayerCount()) {
                    game.addPlayer(playerName, playersColor);
                    activePlayers.put(playerName, game.identifyPlayerByName(playerName));
                }
                else { //player count already reached
                    playersViewMap.get(playerName).showErrorMessage("Lobby is already full");
                }
                //if game is already out of lobby phase
            } else if (playersViewMap.containsKey(playerName)) {
                playersViewMap.get(playerName).showErrorMessage("Can't join a game that's already started");
            } // if the count has been reached starts the game
            if (game.getNumberOfPlayers() == game.getPlayerCount()) {
                this.startGame();
            }
        }
    }

    public ViewInterface removePlayer (String playerName) {
        if (activePlayers.containsKey(playerName)) {
            Player removedPlayer = activePlayers.get(playerName);
            disconnectedPlayers.put(playerName, removedPlayer);
            activePlayers.remove(playerName);
            removedPlayer.playerDisconnects();
        }
        return playersViewMap.remove(playerName);
    }

    /**
     * checks if the color chosen by the player is già taken or not
     * @param playerName player that asked the check
     * @param view player's view
     * @param color chosen color
     * @return true if color is available, false otherwise
     */
    public boolean checkColorAvailable (String playerName, ViewInterface view, String color) {
        if (! playersViewMap.containsKey(playerName)) {
            view.showErrorMessage("can't choose a color without logging in");
            return false;
        }
        try {
            PlayersColor chosenColor = PlayersColor.valueOf(color);
            for (Player player : activePlayers.values()) {
                if (player.getPlayerColor().equals(chosenColor)) {
                    view.showErrorMessage("color for starting cabin is already taken");
                    return false;
                }
            }
        } catch (IllegalArgumentException e) {
            view.showErrorMessage("invalid color for the game");
            return false;
        }
        return true;
    }

    /**
     * Starts the game as soon as one of the players turns the hourglass if LEVEL2 game
     * if TRIAL starts the game without hourglass
     */
    public synchronized void startGame() {
        game.startGame();
        if (game.getMode() == TRIAL) {
            updateEveryView(ClientState.S_END_DRAW_TILE_CARD);
        }
        else {
            updateEveryView(ClientState.START_SHIP_CREATION);
        }
    }

    public void shipsCreation(Message message) {
        String playerName = message.getNickname();
        ViewInterface playersView = this.playersViewMap.get(playerName);
        switch (message.getMessageType()) {

            //draws either from stack or from turned depending on words[1], if turned words[3] is the index of turnedTiles
            //prints the turned tiles ArrayList
            case SHOW_TURNED_TILES_REQUEST:
                playersView.showTurnedTiles(game.getTurnedTiles());
                break;

            //prints the player's currently booked tiles
            case SHOW_BOOKED_TILES_REQUEST:
                playersView.showBookedTiles
                        (activePlayers
                                .get(playerName).
                                getShipBoard().
                                getBookedTiles());
                break;

            //player refuses currently drawnTile
            case REFUSE_MESSAGE:
                refuseTile(playerName);
                break;

            //player sets currently drawnTile at coordinates passed with input
            case SEND_COORDINATES_RESPONSE:
                SendCoordinatesResponse coordinates = (SendCoordinatesResponse) message;
                setTile(playersView, coordinates);
                break;

            //player books currently drawnTile
            case BOOK_TILE_REQUEST:
                if (game.getMode() != TRIAL)
                    bookTile(playersView, message);
                else
                    playersView.showErrorMessage("You are not allowed to book a tile in TRIAL mode");
                break;

            //shows deck of 3 cards 1, 2, 3. Can only be called when no card is drawn.
            case SHOW_CARDS_REQUEST:
                if (game.getMode() != TRIAL)
                    lookGameCards(playersView, playerName, cardsToLookAt);
                else
                    playersView.showErrorMessage("You can't look at cards in TRIAL FLIGHT MODE");
                break;

            case STOP_LOOKING_AT_CARDS_REQUEST:
                stopLookingAtCards(playersView, playerName);
                break;


            //turns the hourglass, if hourglass at last possible turn, the player last input has to be completed
            case TURN_HOURGLASS_REQUEST:
                turnHourglass(playerName);
                break;

            //all other player inputs get refused afterward except for turnHourglass
            case ACCEPT_MESSAGE:
                completed(playerName, playersView);
                break;

            //player with completed ships have to position their ships on the flightboard
            case SET_POSITION_RESPONSE:
                if (game.getMode() != TRIAL) {
                    setPosition(, playersView);
                }
                break;

            //if no case is met, ignore
            default:
                if (playersView != null) {
                    playersView.showGenericMessage("input was incorrect");
                }
                checkIfAllPlayersReady();
                break;
        }
    }

    public  void drawTile(VirtualView playersView, String playerName, int index, boolean turned) {

        // Can't draw if the shipboard is completed or there is already a tile to place/book/refuse
        if (playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)
                || playerStateIs(playerName, ClientState.S_FINISHED)) {
            return;
        }
        Tile drawnTile;
        if (!turned) {
            drawnTile = game.drawTile(playerName);
            if (drawnTile == null) {
                playersView.showErrorMessage("drawn tile is null: stack is empty");
                return;
            }
            playersView.showDrawnTile(drawnTile);
        }
        else {
            drawnTile = game.drawTurnedTile(playerName, index);
                if (drawnTile == null) {
                    playersView.showErrorMessage("drawn tile is null: turned tile is empty or index out of bounds");
                    return;
                }
                playersView.showDrawnTile(drawnTile);
                notifyRemoveTurnedTile(drawnTile);
        }
        clientsStatesMap.put(playerName, ClientState.S_MANAGE_DRAWN_TILE);
    }

    public void notifyRemoveTurnedTile(Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyRemoveTurnedTile(tile);
            } catch (Exception ignored) { //unhandled exception
            }
        });
    }

    public void notifyBookedTile (String playerName, Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyBookedTile(playerName, tile);
            } catch (Exception ignored) {//unhandled exception
            }
        });
    }

    public void notifyNewTurnedTile(Tile tile) {
        playersViewMap.values().forEach(virtualView -> {
            try {
                virtualView.notifyNewTurnedTile(tile);
            } catch (Exception ignored) { //unhandled exception
            }
        });
    }

    //errors check and management
    public void verifyShipCorrectness() {
        for (Player player : game.getListOfInFlightPlayers()) {
            boolean correctness = player.getShipBoard().verifyCorrectness();
            ViewInterface playersView = this.getViewFromNickname(player.getPlayerName());
            if (correctness){
                playersView.showGenericMessage("player's ship is correct");
            } else {
                playersView.showGenericMessage("player's ship has errors: input coordinates to remove mistakes");
                playersView.asksToInputCoordinates();
                playersWithErrors.add(player.getPlayerName());
            }
        }
    }

    public void shipErrorManagement(Message message){
        String playerName =  message.getNickname();
        Player player = game.identifyPlayerByName(playerName);
        ViewInterface playersView = this.getViewFromNickname(playerName);
        SendCoordinatesResponse coordinates = (SendCoordinatesResponse) message;

        if(player==null){
            return;
        }
        if(!playersWithErrors.contains(playerName)){
            playersView.showErrorMessage("player's ship is already correct");
            return;
        }
        player.getShipBoard().destroyTile(coordinates.getFirst());

        boolean correctness = player.getShipBoard().verifyCorrectness();
        if (correctness) {
            if(game.getMode()== TRIAL) {
                game.getFlightBoard().setPlayerToLast(player);
            }
            playersWithErrors.remove(playerName);
            return;
        }

        playersView.showGenericMessage("player's ship is still incorrect: input coordinates to remove tile from");
        playersView.asksToInputCoordinates();
    }

    public void refuseTile(String playerName) {
        if (playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)) {
            return;
        }
        Tile refused = game.refuseTile(playerName);
        notifyNewTurnedTile(refused);
        updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
    }

    public synchronized void lookGameCards(String playerName, ViewInterface playersView, int cardsToLookAt) {
        if (playerStateIs(playerName, ClientState.S_END_DRAW_TILE_CARD)) {
            for (Integer integer: lockedSmallDecks.values()) {
                if (integer == cardsToLookAt) {
                    playersView.showErrorMessage("Error: deck is already been looked at by another player");
                    return;
                }
            }
            lockedSmallDecks.put(playerName, cardsToLookAt);
            playersView.showInGameCards(game.getInGameCards(cardsToLookAt));
            updatePlayerView(ClientState.MANAGE_CARDS, playerName);
            notifyAvailableCardDeck(lockedSmallDecks);
        }
        else {
            playersView.showErrorMessage("can't look at the event cards while you still have your drawn tile, " +
                    "you completed your ship or you are already looking at cards");
        }
    }

    private void notifyAvailableCardDeck(ConcurrentHashMap<String, Integer> lockedSmallDecks) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyAvailableCardDeck(lockedSmallDecks);
            }
            catch (Exception ignored) {
            }
        }
    }

    public void stopLookingAtCards(ViewInterface playersView, String playerName) {
        if (clientsStatesMap.get(playerName) != ClientState.MANAGE_CARDS) {
            playersView.showErrorMessage("You are not looking at cards");
            return;
        }
        lockedSmallDecks.remove(playerName);
        updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
        notifyAvailableCardDeck(lockedSmallDecks);
    }

    //set drawn tile on the player's shipboard
    public void setTile (ViewInterface playersView, String playerName, Coordinates coordinates, boolean booked, int key) {
        Tile tile;
        if (!booked) {
            if (clientsStatesMap.get(playerName) != ClientState.S_MANAGE_DRAWN_TILE) {
                playersView.showErrorMessage("error: no drawn tile");
                return;
            }
        }
        else {
            if (clientsStatesMap.get(playerName) != ClientState.MANAGE_CARDS) {
                playersView.showErrorMessage("can't set a booked tile when you have drawn a tile");
            }
            tile = game.drawBookedTile(playerName, coordinates, key);
            if (tile == null) {
                playersView.showErrorMessage("error: tile isn't in booked tile");
                return;
            }
        }
        tile = game.playerSetTile(playerName, coordinates, key);

        if (tile != null) {
            updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
            notifyPositionedTile(tile);
        }
        else {
            playersView.showErrorMessage("was not able to set tile");
        }
    }

    private void notifyPositionedTile(Tile tile) {
        for (VirtualView view: playersViewMap.values()) {
            try {
                view.notifyPositionedTile(tile);
            } catch (Exception ignored) {}
        }
    }

    //set currently drawn tile as booked for the player
    public void bookTile(ViewInterface playersView, String playerName) {
        if (playerStateIs(playerName,  ClientState.S_MANAGE_DRAWN_TILE)) {
            Tile toBook = game.playerBookTile(playerName);
            if (toBook != null) {
                updatePlayerView(ClientState.S_END_DRAW_TILE_CARD, playerName);
                for (VirtualView view: playersViewMap.values()) {
                    try {
                        view.notifyBookedTile(playerName, toBook);
                    }
                    catch(Exception ignored) {
                    }
                }
            }
            else {
                playersView.showErrorMessage("no drawn tile or booked tile is already full!");
            }
        }
        else  {
            playersView.showErrorMessage("can't book tile, no drawn tile");
        }
    }

    public void checkShipBoard(ViewInterface playersView, Message message) {
        ShowPlayersShipboardRequest messageReceived = (ShowPlayersShipboardRequest) message;
        ShipBoard shipBoardToCheck = game.getPlayerShipBoard(messageReceived.getPlayerName());
        if (shipBoardToCheck == null) {
            playersView.showErrorMessage("there is no player with the name " + messageReceived.getNickname());
            return;
        }
        playersView.showPlayersBoard(messageReceived.getPlayerName(), shipBoardToCheck);
    }

    //now no input except hourglass and checkShipboard work and checks if the other player have completed
    public void completed (String playerName, ViewInterface playersView) {
        if (playerStateIs(playerName, ClientState.S_MANAGE_DRAWN_TILE)) {
            refuseTile(playerName);
        }
        updatePlayerView(ClientState.S_FINISHED, playerName);

        if(game.getMode() == TRIAL) {
            for (Player player : game.getFlightBoard().getInGamePlayers()) {
                if (player.getPlayerName().equals(playerName)) {
                    game.getFlightBoard().addToTrialFlightBoard(player);
                    //It's not important for trial flight, so 0 is a placeholder value
                    updatePlayerView(ClientState.WAIT_OTHER_PLAYER_ACTION, playerName);
                    break;
                }
            }
        }else{
            playersView.asksToSetPosition();
        }
        checkIfAllPlayersReady();
    }

    public void setPosition (String playerName, ViewInterface playersView, int position) {
        if (!playerStateIs(playerName, ClientState.S_FINISHED)) {
            return;
        }
        FlightBoard flightBoard = game.getFlightBoard();
        Player player = game.identifyPlayerByName(playerName);

        if (flightBoard.addToFlightBoard(player, position)) {
            updatePlayerView(ClientState.WAIT_OTHER_PLAYER_ACTION, playerName);
        }
        else {
            playersView.showErrorMessage("Position is taken or input is wrong");
            playersView.asksToSetPosition();
        }
        checkIfAllPlayersReady();
    }

    private void checkIfAllPlayersReady() {
        for (String playerName : activePlayers.keySet()) {
            Player player = game.identifyPlayerByName(playerName);
            ClientState state = clientsStatesMap.get(playerName);
            if (state == null || !state.equals(ClientState.WAIT_OTHER_PLAYER_ACTION)) {
                if (!player.IsDisconnected()) {
                    return;
                }
                else {
                    removePlayer(playerName);
                }
            }
        }
        game.endShipCreation();
        broadcastMessage("All the players have completed the ship creation and positioned themselves\n");
    }


    //Turns hourglass isn't on and adds 1 to the turn count,
    // if it's already been turned twice, player that turns it needs to have completed his ship
    public synchronized void turnHourglass(String playerName) {
        ViewInterface playersView = this.getViewFromNickname(playerName);

        if (!hourglassON) {
            playersView.showErrorMessage("hourglass is already trickling");
            return;
        }
        switch (hourglassTurns) {
            case 0:
                broadcastMessage(playerName + " starts the game: GO!");
                updateEveryView(ClientState.S_END_DRAW_TILE_CARD);
                startTimer();
                break;
            case 1:
                broadcastMessage(playerName + " has flipped the hourglass");
                startTimer();
                break;
            case 2:
                if (playerStateIs(playerName, ClientState.S_FINISHED)
                    || playerStateIs(playerName, ClientState.WAIT_OTHER_PLAYER_ACTION)) {
                    startTimer();
                }
                else {
                    playersView.showErrorMessage("Can't make the last hourglass turn when your shipBoard isn't complete");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + hourglassTurns + "\n");
        }
    }

    public void startTimer() {
        Timer hourglass = new Timer();
        this.hourglassTurns++;
        hourglass.schedule(new TimerTask() {
            @Override
            public void run() {
                hourglassON = false;
                System.out.println("hourglass is exhausted\n");
                hourglass.cancel();
                if (hourglassTurns == 3) {
                    updateEveryView(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    broadcastMessage("Time is up!");
                    checkIfAllPlayersReady();
                    System.out.println("The time is up, ship creation is over\n");
                }
            }
        }, 95000); //95 seconds
    }

    //Number 1 player can draw
    //Every player can check others shipboard
    //Every player can do an early landing
    //When cards are over go to CONCLUDE_GAME state
    public void drawCard (Message message){
        String playerName = message.getNickname();
        ViewInterface playersView = this.getViewFromNickname(playerName);

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

        switch(message.getMessageType()) {
            case DRAW_CARD_REQUEST:
                if (game.identifyPlayerByName(playerName).equals(game.getListOfInFlightPlayers().getFirst())){
                    game.drawCard(playersViewMap);
                    this.broadcastUpdate(new UpdateDrawnCard(game.getDrawnCard()));

                }
                else {
                    broadcastMessage("only the first ranked player can draw");
                }
                break;
            case SHOW_PLAYERS_SHIPBOARD_REQUEST:
                checkShipBoard(playersView, message);
                break;
            case EARLY_LANDING_REQUEST:
                game.getFlightBoard().earlyLanding(game.identifyPlayerByName(playerName));
                broadcastMessage(playerName + " made an early landing\n");
                break;
            default: break;
        }
    }

    //Gets the drawnCard from main and sends the input to each Card, depending on return value gives errors
    public void cardEvent(Message message) {
        Player player = game.identifyPlayerByName(message.getNickname());
        if (player.isLanded()) {
            return;
        }
        if (message.getMessageType().equals(EARLY_LANDING_REQUEST)) {
            playersToEarlyLand.add(player);
        } else {
            game.cardEvent(message);
        }
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
        // Sort players based on their credits in descending order
        game.setPodium();
        System.out.println("Podium:");
        for (int i = 0; i < game.getListOfAllPlayer().size(); i++) {
            Player player = game.getListOfAllPlayer().get(i);
            System.out.println((i + 1) + ". " + player.getPlayerName() + " - Credits: " + player.getCredit());
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

    /**
     * sends a string message to every player's view
     * @param messageString what gets shown on the views
     */
    public void broadcastMessage(String messageString) {
        for (ViewInterface view : playersViewMap.values()) {
            view.showGenericMessage(messageString);
        }
    }

    public void broadcastUpdate(Message message) {
        for (ViewInterface view : playersViewMap.values()) {
            view.updateLightModel(message);
        }
    }

    public void updateEveryView(ClientState newState) {
        for (String playerName:  playersViewMap.keySet()) {
            playersViewMap.get(playerName).setClientState(newState);
            clientsStatesMap.put(playerName, newState);
        }
    }

    public void updatePlayerView (ClientState newState, String playerName) {
        playersViewMap.get(playerName).setClientState(newState);
        clientsStatesMap.put(playerName, newState);
    }

    public boolean playerStateIs(String playerName,ClientState stateToVerify) {
        return clientsStatesMap.get(playerName) == stateToVerify;
    }

    /**
     * returns playersView from playerName
     * @param playerName player to get view for
     * @return player's viewInterface
     */
    public ViewInterface getViewFromNickname(String playerName) {
        return playersViewMap.get(playerName);
    }
}