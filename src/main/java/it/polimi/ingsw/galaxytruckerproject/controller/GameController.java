package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.GameState;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static it.polimi.ingsw.galaxytruckerproject.model.GameMode.*;
import static it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType.*;


public class GameController {
    private final String gameName;
    private final GameInterface game;
    private final ArrayList<String> playersWithErrors;
    private final Map<String, Message> playerInputs;
    private final Map<String, ViewInterface> playersViewMap;
    private final Map<String, Player> activePlayers;
    private final Map<String, Player> disconnectedPlayers;
    private final ArrayList<Player> playersToEarlyLand = new ArrayList<>();
    private Map<String, Integer> lockedSmallDecks;


    public GameController(GameInterface game, String gameName) {
        this.gameName = gameName;
        this.game = game;
        playerInputs = new HashMap<>();
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
                if (game.getHourglassTurns() > 0){
                    shipsCreation(message);
                }
                else
                    startGame(message);
                break;
            }
            case GameState.VERIFY_SHIP_CORRECTNESS: {
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
    public void addToPlayersViewMap(String playerName, ViewInterface view, boolean reconnecting) {
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
    public void reconnectPlayer(String playerName, ViewInterface view) {
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
                this.startGame(new GenericMessage("whatever"));
                game.startGame();
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
     * Starts the game as soon as one of the players sends a turn hourglass request message
     * @param message received message that can start the game or makes the controller ask for
     * the turn hourglass request message
     */
    public synchronized void startGame(Message message) {
        if (message.getMessageType() == TURN_HOURGLASS_REQUEST) {
            turnHourglass(message.getNickname());
        }
        else {
            playersViewMap.values().forEach(ViewInterface::asksToTurnTheHourglass);
        }
    }

    public void shipsCreation(Message message) {
        String playerName = message.getNickname();
        ViewInterface playersView = this.playersViewMap.get(playerName);
        switch (message.getMessageType()) {

            //draws either from stack or from turned depending on words[1], if turned words[3] is the index of turnedTiles
            case DRAW_TILE_REQUEST:
                drawTile((DrawTileRequest) message);
                break;

            //prints the turned tiles ArrayList
            case SHOW_TURNED_TILES_REQUEST:
                playersView.showTurnedTiles(game.getTurnedTiles());
                break;

            //prints the player's currently booked tiles
            case SHOW_BOOKED_TILES_REQUEST:
                playersView.showBookedTiles
                        (activePlayers
                                .get(playerName).
                                getPlayerShip().
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
                    lookGameCards(playersView, message);
                else
                    playersView.showErrorMessage("You can't look at cards in TRIAL FLIGHT MODE");
                break;

            case STOP_LOOKING_AT_CARDS_REQUEST:
                stopLookingAtCards(playersView, message);
                break;

            //turns the hourglass, if hourglass at last possible turn, the player last input has to be completed
            case TURN_HOURGLASS_REQUEST:
                turnHourglass(playerName);
                break;

            //all other player inputs get refused afterward except for turnHourglass
            case ACCEPT_MESSAGE:
                completed(playerName, playersView);
                break;

            //looks at other player ship, if no second word, looks at yours
            case SHOW_PLAYERS_SHIPBOARD_REQUEST:
                checkShipBoard(playersView, message);
                break;

            //player with completed ships have to position their ships on the flightboard
            case SET_POSITION_RESPONSE:
                if (game.getMode() != TRIAL) {
                    setPosition(playersView, message);
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

    public  void drawTile(DrawTileRequest message) {

        String playerName = message.getNickname();
        MessageType drawFromWhere = message.DrawFromWhere();
        ViewInterface playersView = this.playersViewMap.get(playerName);

        // Can't draw if the shipboard is completed or there is already a tile to place/book/refuse
        if (Objects.equals(playerInputs.get(playerName).getMessageType(), DRAW_TILE_REQUEST) ||
                Objects.equals(playerInputs.get(playerName).getMessageType(), ACCEPT_MESSAGE)||
                Objects.equals(playerInputs.get(playerName).getMessageType(), SHOW_CARDS_REQUEST)) {
            return;
        }
        switch (drawFromWhere) {
            case DRAW_TILE_FROM_STACK_REQUEST: {
                Tile drawnTile = game.drawTile(playerName);
                if (drawnTile == null) {
                    playersView.showErrorMessage("drawn tile is null: stack is empty");
                    return;
                }
                playersView.showDrawnTile(drawnTile);
                playerInputs.put(playerName, message);
                break;
            }
            case DRAW_TILE_FROM_TURNED_REQUEST: {
                Tile drawnTile = game.drawTurnedTile(playerName, message.getTileIndex());
                if (drawnTile == null) {
                    playersView.showErrorMessage("drawn tile is null: turned tile is empty or index out of bounds");
                    return;
                }
                playersView.showDrawnTile(drawnTile);
                playerInputs.put(playerName, message);
                break;
            }
            case DRAW_TILE_FROM_BOOKED_REQUEST : {
                Tile drawnTile = game.drawBookedTile (playerName, message.getTileIndex());

                //the index has to be either 0 or 1
                if (drawnTile == null) {
                    playersView.showErrorMessage("drawn tile is null: booked tile is empty or index out of bounds");
                    return;
                }
                playersView.showDrawnTile(drawnTile);
                playerInputs.put(playerName, message);
            }
        }
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
        if (Objects.equals(playerInputs.get(playerName).getMessageType(), DRAW_TILE_REQUEST)) {
            DrawTileRequest drawTileRequest = (DrawTileRequest) playerInputs.get(playerName);
            if (drawTileRequest.DrawFromWhere().equals(DRAW_TILE_FROM_BOOKED_REQUEST)) {
                game.playerBookTile(playerName);
            }
            else {
                game.refuseTile(playerName);
            }
            playerInputs.put(playerName, new RefuseMessage(playerName));
        }
        else {
            ViewInterface playersView = this.getViewFromNickname(playerName);
            playersView.showErrorMessage("can't refuse tile when still have to draw one");
        }
    }

    public synchronized void lookGameCards(ViewInterface playersView, Message message) {
        String playerName = message.getNickname();
        ShowCardsRequest messageReceived = (ShowCardsRequest) message;
        if (Objects.equals(playerInputs.get(playerName).getMessageType(), DRAW_TILE_REQUEST)
                || Objects.equals(playerInputs.get(playerName).getMessageType(), ACCEPT_MESSAGE)
                || Objects.equals(playerInputs.get(playerName).getMessageType(), SHOW_CARDS_REQUEST)) {
            playersView.showErrorMessage("can't look at the event cards while you still have your drawn tile, " +
                    "you completed your ship or you are already looking at cards");
            return;
        }
        for (Integer integer: lockedSmallDecks.values()) {
            if (integer == messageReceived.getCardsToLookAt()) {
                playersView.showErrorMessage("Error: deck is already been looked at by another player");
                return;
            }
        }
        playersView.showInGameCards(game.getInGameCards(messageReceived.getCardsToLookAt()));
        playerInputs.put(playerName, message);
    }

    public void stopLookingAtCards(ViewInterface playersView, Message message) {
        String playerName = message.getNickname();
        if (!Objects.equals(playerInputs.get(playerName).getMessageType(), SHOW_CARDS_REQUEST)) {
            playersView.showErrorMessage("You are not looking at cards");
            return;
        }
        lockedSmallDecks.remove(playerName);
        playerInputs.put(playerName, message);
    }

    //set drawn tile on the player's shipboard
    public void setTile (ViewInterface playersView, SendCoordinatesResponse message) {
        String playerName = message.getNickname();
        if (Objects.equals(playerInputs.get(playerName).getMessageType(), DRAW_TILE_REQUEST)) {
            if (!game.playerSetTile(playerName, message.getFirst())) {
                playersView.showErrorMessage("coordinates input weren't valid");
                return;
            }
            playerInputs.put(playerName, message);
        }
    }

    //set currently drawn tile as booked for the player
    public void bookTile(ViewInterface playersView, Message message) {
        if (Objects.equals(playerInputs.get(message.getNickname()).getMessageType(), DRAW_TILE_REQUEST)) {
            if (!game.playerBookTile (message.getNickname())) {
                playersView.showErrorMessage("either your booked tiles are full or your input was out of bounds");
                return;
            }
            playerInputs.put(message.getNickname(), message);
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
        if (Objects.equals(playerInputs.get(playerName).getMessageType(), DRAW_TILE_REQUEST)) {
            refuseTile(playerName);
        }
        AcceptMessage acceptMessage = new AcceptMessage(playerName);
        playerInputs.put(playerName, acceptMessage);

        if(game.getMode() == TRIAL) {
            for (Player player : game.getFlightBoard().getInGamePlayers()) {
                if (player.getPlayerName().equals(playerName)) {
                    game.getFlightBoard().addToTrialFlightBoard(player);
                    //It's not important for trial flight, so 0 is a placeholder value
                    playerInputs.put(playerName, new SetPositionResponse(playerName, 0));
                    break;
                }
            }
        }else{
            playersView.asksToSetPosition();
        }
        checkIfAllPlayersReady();
    }

    public void setPosition (ViewInterface playersView, Message message) {
        if (!playerInputs.get(message.getNickname()).getMessageType().equals(ACCEPT_MESSAGE)) {
            return;
        }
        FlightBoard flightBoard = game.getFlightBoard();
        Player player = game.identifyPlayerByName(message.getNickname());
        if (message.getMessageType().equals(SET_POSITION_RESPONSE)) {
            SetPositionResponse setPositionResponse = (SetPositionResponse) message;
            if (flightBoard.addToFlightBoard(player, setPositionResponse.getPosition())) {
                playerInputs.put(message.getNickname(), setPositionResponse);
            }
            else {
                playersView.showErrorMessage("Position is taken or input is wrong");
                playersView.asksToSetPosition();
            }
        }
        checkIfAllPlayersReady();
    }

    private void checkIfAllPlayersReady() {
        for (String playerName : activePlayers.keySet()) {
            Message input = playerInputs.get(playerName);
            if (input == null || !input.getMessageType().equals(SET_POSITION_RESPONSE)) {
                return;
            }
        }
        game.endShipCreation();
        broadcastMessage("All the players have completed the ship creation and positioned themselves\n");
    }


    //Turns hourglass isn't on and adds 1 to the turn count,
    // if it's already been turned twice, player that turns it needs to have completed his ship
    public void turnHourglass(String playerName) {
        ViewInterface playersView = this.getViewFromNickname(playerName);

        if (!game.getHourglassState()) {
            playersView.showErrorMessage("hourglass is already trickling");
            return;
        }
        switch (game.getHourglassTurns()) {
            case 0:
                broadcastMessage(playerName + " starts the game: GO!");
                game.startTimer();
                break;
            case 1:
                broadcastMessage(playerName + " has flipped the hourglass");
                game.startTimer();
                break;
            case 2:
                if (playerInputs.get(playerName).getMessageType().equals(ACCEPT_MESSAGE) ||
                playerInputs.get(playerName).getMessageType().equals(SET_POSITION_RESPONSE)) {
                    game.startTimer();
                }
                else {
                    playersView.showErrorMessage("Can't make the last hourglass turn when your shipBoard isn't complete");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + game.getHourglassTurns() + "\n");
        }
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

    public Map<String, ViewInterface> getPlayersViewMap() {
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

    /**
     * returns playersView from playerName
     * @param playerName player to get view for
     * @return player's viewInterface
     */
    public ViewInterface getViewFromNickname(String playerName) {
        return playersViewMap.get(playerName);
    }
}