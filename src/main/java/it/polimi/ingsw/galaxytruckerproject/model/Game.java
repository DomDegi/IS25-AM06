package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.Observer;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.cards.TrialCardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static it.polimi.ingsw.galaxytruckerproject.model.GameState.*;


/**
 * Main implementation of the Galaxy Trucker game logic.
 * Handles player management, ship creation, tile drawing, card drawing, and game state transitions.
 */
public class Game implements GameInterface{

    /** List of observers to notify on game state changes */
    private final ArrayList<Observer> observers = new ArrayList<>();

    /** Current game mode (e.g., TRIAL or LEVEL2) */
    private GameMode mode;

    /** Current state of the game (e.g., LOBBY_PHASE, SHIPS_CREATION, etc.) */
    private GameState gameState;

    /** Number of players the game starts with */
    private int playerCount;

    /** List of all cards that will be used during gameplay */
    private ArrayList<Card> inGameCards;

    /** Stack of available tiles to be drawn */
    private ConcurrentLinkedDeque<Tile> tileStack;

    /** Map of turned tiles (refused tiles made visible again) */
    private ConcurrentHashMap<Integer,Tile> turnedTiles;

    /** Object that keeps track of player flight order and rankings */
    private FlightBoard flightBoard;

    /** Currently drawn card from the deck */
    private Card drawnCard;


    /**
     * Constructs a new Game instance with the specified mode and player count.
     * Initializes the card deck, tile stack, and flight board.
     *
     * @param mode the selected game mode
     * @param playerCount total number of players
     */
    public Game(GameMode mode, int playerCount) {
        this.mode = mode;
        this.gameState = LOBBY_PHASE;
        this.turnedTiles = new ConcurrentHashMap<>();
        this.flightBoard = new FlightBoard(mode);
        this.tileStack = new TileFactory().getStack(TileFactory.loadTilesFromJson("Tiles.json"));

        if (this.mode == GameMode.LEVEL2) {
            CardDeck cardDeck = new CardDeck("cards.json");
            this.inGameCards = cardDeck.getTier2FlightCards();
        }
        else {
            //this.inGameCards = new TrialCardDeck("trialFlightCards.json").getTrialDeck(); quello corretto
            this.inGameCards = new TrialCardDeck("trialFlightCards.json").getTrialDeck();
        }
        this.playerCount = playerCount;
        this.drawnCard = null;
    }

    /** Returns the current game state. */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Updates the current game state and notifies all observers.
     *
     * @param newState the new game state to set
     */
    public void setGameState(GameState newState) {
        this.gameState = newState;
        notifyObservers(newState);
    }

    /**
     * Notifies all registered observers of a game state change.
     *
     * @param newState the new state to notify observers about
     */
    public void notifyObservers(GameState newState) {
        for (Observer observer : observers) {
            try {
                observer.update(newState);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /** Adds a new observer to receive game updates. */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /** Removes an existing observer from notifications. */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /** Sets the number of players for this game. */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * Adds a new player to the game with the given name and color.
     * Initializes their ship depending on the game mode.
     *
     * @param playerName the player's name
     * @param color the selected player color
     */
    public void addPlayer(String playerName, PlayersColor color) {

        if (identifyPlayerByName(playerName) != null) {
            System.out.println("player with this name is already registered\n");
            return;
        }
        for (Player player: flightBoard.getAllPlayers()){
            if (color.equals(player.getPlayerColor())){
                System.out.println(color + " has already been chosen\n");
                return;
            }
        }
        Player player = new Player(playerName, color);
        flightBoard.addPlayerToGame(player);
        if (mode == GameMode.LEVEL2) {
            player.getShipBoard().initializeLevel2();
        } else
            player.getShipBoard().initializeTestFlight();
        if (this.getListOfAllPlayer().size() == playerCount) {
            setGameState(START_GAME);
        }
    }

    /** Begins the ship creation phase and notifies observers. */
    public void startShipCreation(){
        setGameState(SHIPS_CREATION);
    }


    /**
     * Allows a player to draw a tile from the stack.
     *
     * @param playerName the player drawing the tile
     * @return the drawn Tile object, or null if the stack is empty
     */
    public synchronized Tile drawTile (String playerName) {
        Player player = identifyPlayerByName(playerName);
        if (player == null) { return null;}
        Tile drawnTile;
        try {
            drawnTile = tileStack.pop();
        } catch (Exception e) {
            System.out.println("Tile stack empty\n");
            return null;
        }
        if (drawnTile != null) {
            player.hasDrawnTile(drawnTile);
        }
        return drawnTile;
    }

    /**
     * Allows a player to draw a tile from the turned tiles.
     *
     * @param playerName the player drawing the tile
     * @param index index of the tile in the turned map
     * @return the drawn tile or null if unavailable
     */
    public synchronized Tile drawTurnedTile (String playerName, int index) {
        Player player = identifyPlayerByName(playerName);
        if (player == null || !turnedTiles.containsKey(index)) {
            return null;}
        Tile drawnTile = turnedTiles.remove(index);
        if (drawnTile != null) {
            player.hasDrawnTile(drawnTile);
        }
        return drawnTile;
    }

    /**
     * Allows a player to use a previously booked tile.
     *
     * @param playerName the player using the tile
     * @param tile the tile to be used
     * @return the tile, if successfully positioned
     */
    public Tile drawAndPositionBookedTile (String playerName, Tile tile) {
        Player player = identifyPlayerByName(playerName);
        ArrayList<Tile> bookedTiles = player.getShipBoard().getBookedTiles();
        for (int i = 0; i < bookedTiles.size(); i++) {
            if (bookedTiles.get(i).getKey() == tile.getKey()) {
                player.getShipBoard().removeBookedTile(i);
                player.hasDrawnTile(tile);
            }
        }
        return tile;
    }

    /**
     * Removes a player's drawn tile and puts it back into the turned tiles.
     *
     * @param playerName the player refusing the tile
     * @return the refused tile
     */
    public Tile refuseTile(String playerName) {
        Player player = identifyPlayerByName(playerName);
        Tile removedTile = player.removeDrawnTile();
        turnedTiles.put(removedTile.getKey(), removedTile);
        return removedTile;
    }

    /** Returns the map of turned (refused) tiles. */
    public ConcurrentHashMap<Integer, Tile> getTurnedTiles() {
        return turnedTiles;
    }

    /**
     * Returns a specific group of in-game cards.
     *
     * @param bunchNumber which group to retrieve (1, 2, or 3)
     * @return list of cards in that group
     */
    public ArrayList<Card> getANumberOfInGameCards(int bunchNumber) {
        return switch (bunchNumber) {
            case 1 -> getInGameCards1();
            case 2 -> getInGameCards2();
            case 3 -> getInGameCards3();
            default -> new ArrayList<>();
        };
    }

    /** Returns the first group of 3 in-game cards. */
    public synchronized ArrayList<Card> getInGameCards1 () {
        ArrayList<Card> bunch1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            bunch1.add(inGameCards.get(i));
        }
        return bunch1;
    }

    /** Returns the second group of 3 in-game cards. */
    public synchronized ArrayList<Card> getInGameCards2 () {
        ArrayList<Card> bunch2 = new ArrayList<>();
        for (int i = 3; i < 6; i++) {
            bunch2.add(inGameCards.get(i));
        }
        return bunch2;
    }

    /** Returns the third group of 3 in-game cards. */
    public synchronized ArrayList<Card> getInGameCards3 () {
        ArrayList<Card> bunch3 = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            bunch3.add(inGameCards.get(i));
        }
        return bunch3;
    }

    /**
     * Places a drawn tile on the player's ship board.
     *
     * @param playerName the player placing the tile
     * @param tile the tile to place
     * @return the placed tile if successful, null otherwise
     */
    public Tile playerSetTile (String playerName, Tile tile) {
        Player player = identifyPlayerByName(playerName);
        if (tile.getKey() == 0 || (player.getDrawnTile() != null && tile.getKey() == player.getDrawnTile().getKey())) {
            if (player.getShipBoard().positionTile(Optional.of(tile), tile.getCoordinates())) {
                if (tile.getKey() == 0) {
                    player.removeDrawnTile();
                }
                return tile;
            }
            else
                return null;
        }
        return null;
    }

    /**
     * Books the currently drawn tile for later use.
     *
     * @param playerName the player booking the tile
     * @return the booked tile
     */
    public Tile playerBookTile (String playerName) {
        Player player = identifyPlayerByName(playerName);
        Tile drawnTile = player.getDrawnTile();
        if (drawnTile == null) {
            return null;
        }
        if (player.getShipBoard().addBookedTile(player.removeDrawnTile()))
            return drawnTile;
        else {
            System.out.println("Booked tiles are full\n");
            return null;
        }
    }


    /** Ends the ship creation phase and proceeds to verification. */
    public void endShipCreation() {
        Collections.shuffle(inGameCards);
        setGameState(VERIFY_SHIP_CORRECTNESS);
    }

    /** Ends the ship correctness verification and proceeds to drawing cards. */
    public void endShipVerification() {
        setGameState(DRAW_CARD);
    }

    /** Draws the next card in the current card phase. */
    public void drawCard() {
        this.drawnCard = inGameCards.removeFirst();
    }

    /** Ends the current card phase and transitions to game conclusion. */
    public void endCardPhase() {
        this.gameState = CONCLUDE_GAME;
    }

    /** Ends the current card event and moves back to the drawing phase. */
    public void endCardEvent() {

        setGameState(DRAW_CARD);
    }



    //GETTER METHODS


    /** Returns the game mode (e.g., TRIAL or LEVEL2). */
    public GameMode getMode() {
        return mode;
    }

    /** Returns the total number of players. */
    public int getPlayerCount() {
        return playerCount;
    }


    /**
     * Searches for a player by name.
     *
     * @param playerName name of the player to find
     * @return the matching Player object or null
     */
    public Player identifyPlayerByName(String playerName) {
        for (Player player: getListOfAllPlayer()) {
            if (playerName.equals(player.getPlayerName())) {
                return player;
            }
        }
        return null;
    }

    /** Returns a list of all players. */
    public ArrayList<Player> getListOfAllPlayer(){
        return flightBoard.getAllPlayers();
    }

    /** Returns the list of players currently in flight. */
    public ArrayList<Player> getListOfInFlightPlayers() {
        return flightBoard.getInGamePlayers();
    }

    /** Returns the number of players in flight. */
    public int getNumberOfPlayers() {
        return getListOfInFlightPlayers().size();
    }

    /** Gets a player's name by index. */
    public String getPlayerName(int playerIndex) {
        return getListOfInFlightPlayers().get(playerIndex).getPlayerName();
    }

    /**
     * Returns the ship board of a specified player.
     *
     * @param playerName the name of the player
     * @return the player's ship board
     */
    public ShipBoard getPlayerShipBoard (String playerName) {
        Player player =  identifyPlayerByName(playerName);
        if (player == null) {
            return null;
        }
        return player.getShipBoard();
    }

    /** Returns the current flight board. */
    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    /** Sorts the final rankings of the players by credits. */
    public void setPodium() {
        getListOfAllPlayer().sort((p1, p2) -> Integer.compare(p2.getCredit(), p1.getCredit())); // Sort by credit descending
    }

    /** Returns the number of remaining cards in the deck. */
    public int getCardsLeft() {
        return inGameCards.size();
    }


    /** Sets the current drawn card and updates game state to CARD_EVENT. */
    public void setDrawnCard (Card card) {
        this.drawnCard = card;
        gameState = CARD_EVENT;
    }

    /** Returns the currently drawn card. */
    public Card getDrawnCard() {
        return drawnCard;
    }

    /** Serializes basic game data as a string. */
    public String toStringGameData() {
        StringBuilder sb = new StringBuilder();
        sb.append(mode.toString()).append(" ").append(playerCount).append(" ").append(gameState.toString()).append(" ");
        //attributes[0] = mode; attributes[1] = playerCount; attributes[2] = gameState
        return sb.toString();
    }

    /** Serializes the IDs of remaining in-game cards. */
    public String cardsToDrawData() {
        if (inGameCards.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Card card: inGameCards) {
            sb.append(card.getId()).append(" ");
        }
        return sb.toString();
    }

    /** Serializes the current tile stack. */
    public String tileStackData() {
        if (tileStack.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        tileStack.forEach(tile -> {sb.append(tile.getKey()).append(" ");});
        return sb.toString();
    }

    /** Serializes the turned tiles as string. */
    public String turnedTileData() {
        if (turnedTiles.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        turnedTiles.keySet().forEach(key -> {sb.append(key).append(" ");});
        return sb.toString();
    }

    /** Empty constructor for deserialization. */
    public Game() {
        this.tileStack = new ConcurrentLinkedDeque<>();
        this.turnedTiles = new ConcurrentHashMap<>();
    }

    /**
     * Initializes the game using attributes from a save file.
     *
     * @param attributes serialized data split by space
     */
    public void gameLoader(String[] attributes) {
        this.mode = GameMode.valueOf(attributes[0]);
        this.playerCount = Integer.parseInt(attributes[1]);
        this.setGameState(GameState.fromString(attributes[2]));
        this.flightBoard = new FlightBoard(mode);
    }

    /**
     * Loads remaining cards from serialized IDs.
     *
     * @param attributes array of card IDs
     */
    public void cardLoader(String[] attributes) {
        ArrayList<Integer> cardsLeftID = new ArrayList<>();
        for (String attribute : attributes) {
            cardsLeftID.add(Integer.parseInt(attribute));
        }
        if (mode == GameMode.LEVEL2) {
            this.inGameCards = new CardDeck("cards.json").deckFromIDs(cardsLeftID);
        }
        else {
            //this.inGameCards = new TrialCardDeck("trialFlightCards.json").deckFromIDs(cardsLeftID);
            this.inGameCards = new TrialCardDeck("trialFLightCards.json").deckFromIDs(cardsLeftID);
        }
    }

    /**
     * Loads the tile stack from serialized tile keys.
     *
     * @param attributes tile keys
     */
    public void tileStackLoader(String[] attributes) {
        ArrayList<Integer> keyStack = new ArrayList<>();
        for (String attribute : attributes) {
            keyStack.add(Integer.parseInt(attribute));
        }
        this.tileStack = new TileFactory().stackFromIDs(keyStack);
    }

    /**
     * Loads turned tiles from serialized keys.
     *
     * @param attributes tile keys
     */
    public void turnedTileLoader(String[] attributes) {
        if (attributes[0].isEmpty()) {
            return;
        }
        ArrayList<Integer> keyMap = new ArrayList<>();
        for (String attribute : attributes) {
            keyMap.add(Integer.parseInt(attribute));
        }
        this.turnedTiles = new TileFactory().mapFromIDs(keyMap);
    }

    /** Returns all in-game cards. */
    public ArrayList<Card> getANumberOfInGameCards() {
        return inGameCards;
    }

    /** Returns the stack of drawable tiles. */
    public ConcurrentLinkedDeque<Tile> getTileStack() {
        return tileStack;
    }

    /**
     * Sets the game state without notifying observers (used during loading).
     *
     * @param gameState new game state
     */
    public void setGameStateWithoutUpdating(GameState gameState) {
        this.gameState = gameState;
    }
}
