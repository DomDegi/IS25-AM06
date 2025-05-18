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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static it.polimi.ingsw.galaxytruckerproject.model.GameState.*;

public class Game implements GameInterface{
    private final ArrayList<Observer> observers = new ArrayList<>();
    private GameMode mode;
    private GameState gameState;
    private int playerCount;
    private ArrayList<Card> inGameCards;
    private ConcurrentLinkedDeque<Tile> tileStack;
    private ConcurrentHashMap<Integer,Tile> turnedTiles;
    private FlightBoard flightBoard;
    private Card drawnCard;


    //instances a new game starting in the state START_GAME
    public Game(GameMode mode, int playerCount) {
        this.mode = mode;
        this.gameState = LOBBY_PHASE;
        this.turnedTiles = new ConcurrentHashMap<>();
        this.flightBoard = new FlightBoard(mode);
        this.tileStack = new TileFactory().getStack(TileFactory.loadTilesFromJson("Tiles.json"));

        if (this.mode == GameMode.LEVEL2) {
            CardDeck cardDeck = new CardDeck("simpleCards.json");
            this.inGameCards = cardDeck.getTier2FlightCards();
        }
        else {
            //this.inGameCards = new TrialCardDeck("trialFlightCards.json").getTrialDeck(); quello corretto
            this.inGameCards = new TrialCardDeck("simpleCards2.json").getTrialDeck();
        }
        this.playerCount = playerCount;
        this.drawnCard = null;
    }

    //returns the current GameState
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newState) {
        this.gameState = newState;
        notifyObservers(newState);
    }

    public void notifyObservers(GameState newState) {
        for (Observer observer : observers) {
            try {
                observer.update(newState);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    //set player count for the game
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    //adds player to game with the input string as name
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

    //changes game state to SHIPS_CREATION and notifies observers of it (GUI, TUI, Log)
    public void startShipCreation(){
        setGameState(SHIPS_CREATION);
    }

    //SHIPS_CREATION METHODS
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

    //sets the booked tile at index 0 or 1 as the drawn tile for player: playerName
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

    public Tile refuseTile(String playerName) {
        Player player = identifyPlayerByName(playerName);
        Tile removedTile = player.removeDrawnTile();
        turnedTiles.put(removedTile.getKey(), removedTile);
        return removedTile;
    }

    public ConcurrentHashMap<Integer, Tile> getTurnedTiles() {
        return turnedTiles;
    }


    public ArrayList<Card> getInGameCards(int bunchNumber) {
        return switch (bunchNumber) {
            case 1 -> getInGameCards1();
            case 2 -> getInGameCards2();
            case 3 -> getInGameCards3();
            default -> new ArrayList<>();
        };
    }

    public synchronized ArrayList<Card> getInGameCards1 () {
        ArrayList<Card> bunch1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            bunch1.add(inGameCards.get(i));
        }
        return bunch1;
    }

    public synchronized ArrayList<Card> getInGameCards2 () {
        ArrayList<Card> bunch2 = new ArrayList<>();
        for (int i = 3; i < 6; i++) {
            bunch2.add(inGameCards.get(i));
        }
        return bunch2;
    }

    public synchronized ArrayList<Card> getInGameCards3 () {
        ArrayList<Card> bunch3 = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            bunch3.add(inGameCards.get(i));
        }
        return bunch3;
    }

    public Tile playerSetTile (String playerName, Tile tile) {
        Player player = identifyPlayerByName(playerName);
        if (player.getDrawnTile() != null && tile.getKey() == player.getDrawnTile().getKey()) {
            if (player.getShipBoard().positionTile(Optional.of(tile), tile.getCoordinates())) {
                player.removeDrawnTile();
                return tile;
            }
            else
                return null;
        }
        return null;
    }

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


    public void endShipCreation() {
        //Collections.shuffle(inGameCards);
        setGameState(VERIFY_SHIP_CORRECTNESS);
    }

    //VERIFY_SHIP_CORRECTNESS METHODS
    public void endShipVerification() {
        setGameState(DRAW_CARD);
    }

    //DRAW_CARD METHODS

    public void drawCard() {
        this.drawnCard = inGameCards.removeFirst();
    }

    public void endCardPhase() {
        this.gameState = CONCLUDE_GAME;
    }

    //CARD_EVENT METHODS

    public void endCardEvent() {
        if(this.getFlightBoard().concludeMovement())
            setGameState(DRAW_CARD);
        else
            setGameState(CONCLUDE_GAME);
    }



    //GETTER METHODS

    //This flag is needed from some game functions
    public GameMode getMode() {
        return mode;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    //input a string and if it's the same as a player name returns the player
    public Player identifyPlayerByName(String playerName) {
        for (Player player: getListOfAllPlayer()) {
            if (playerName.equals(player.getPlayerName())) {
                return player;
            }
        }
        return null;
    }

    public ArrayList<Player> getListOfAllPlayer(){
        return flightBoard.getAllPlayers();
    }

    public ArrayList<Player> getListOfInFlightPlayers() {
        return flightBoard.getInGamePlayers();
    }

    //returns the number of player in the game
    public int getNumberOfPlayers() {
        return getListOfInFlightPlayers().size();
    }

    //returns the player's name at playerIndex (0 to 3) as a string
    public String getPlayerName(int playerIndex) {
        return getListOfInFlightPlayers().get(playerIndex).getPlayerName();
    }

    public ShipBoard getPlayerShipBoard (String playerName) {
        Player player =  identifyPlayerByName(playerName);
        if (player == null) {
            return null;
        }
        return player.getShipBoard();
    }


    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public void setPodium() {
        getListOfAllPlayer().sort((p1, p2) -> Integer.compare(p2.getCredit(), p1.getCredit())); // Sort by credit descending
    }
    public int getCardsLeft() {
        return inGameCards.size();
    }


    //For testing purposes

    public void setDrawnCard (Card card) {
        this.drawnCard = card;
        gameState = CARD_EVENT;
    }

    public Card getDrawnCard() {
        return drawnCard;
    }

    public String toStringGameData() {
        StringBuilder sb = new StringBuilder();
        sb.append(mode.toString()).append(" ").append(playerCount).append(" ").append(gameState.toString()).append(" ");
        //attributes[0] = mode; attributes[1] = playerCount; attributes[2] = gameState
        return sb.toString();
    }

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

    public String tileStackData() {
        if (tileStack.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        tileStack.forEach(tile -> {sb.append(tile.getKey()).append(" ");});
        return sb.toString();
    }

    public String turnedTileData() {
        if (turnedTiles.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        turnedTiles.keySet().forEach(key -> {sb.append(key).append(" ");});
        return sb.toString();
    }

    public Game() {
        this.tileStack = new ConcurrentLinkedDeque<>();
        this.turnedTiles = new ConcurrentHashMap<>();
    }

    public void gameLoader(String[] attributes) {
        this.mode = GameMode.valueOf(attributes[0]);
        this.playerCount = Integer.parseInt(attributes[1]);
        this.setGameState(GameState.fromString(attributes[2]));
        this.flightBoard = new FlightBoard(mode);
    }

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
            this.inGameCards = new TrialCardDeck("simpleCards2.json").deckFromIDs(cardsLeftID);
        }
    }

    public void tileStackLoader(String[] attributes) {
        ArrayList<Integer> keyStack = new ArrayList<>();
        for (String attribute : attributes) {
            keyStack.add(Integer.parseInt(attribute));
        }
        this.tileStack = new TileFactory().stackFromIDs(keyStack);
    }

    public void turnedTileLoader(String[] attributes) {
        if(Objects.equals(attributes[0], "")) {
            return;
        }
        ArrayList<Integer> keyMap = new ArrayList<>();
        for (String attribute : attributes) {
            keyMap.add(Integer.parseInt(attribute));
        }
        this.turnedTiles = new TileFactory().mapFromIDs(keyMap);
    }

    public ArrayList<Card> getInGameCards() {
        return inGameCards;
    }

    public ConcurrentLinkedDeque<Tile> getTileStack() {
        return tileStack;
    }
}
