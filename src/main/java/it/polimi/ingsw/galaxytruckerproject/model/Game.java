package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.cards.TrialCardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static it.polimi.ingsw.galaxytruckerproject.model.GameState.*;

public class Game implements GameInterface {
    private final GameMode mode;
    private GameState gameState;
    private int playerCount;
    private final ArrayList<Card> inGameCards;
    private final ConcurrentLinkedDeque<Tile> tileStack;
    private final ConcurrentHashMap<Integer,Tile> turnedTiles;
    private final FlightBoard flightBoard;
    private Card drawnCard;


    //instances a new game starting in the state START_GAME
    public Game(GameMode mode, int playerCount) {
        this.mode = mode;
        this.gameState = START_GAME;
        this.turnedTiles = new ConcurrentHashMap<>();
        this.flightBoard = new FlightBoard(mode);
        this.tileStack = new TileFactory().getStack(TileFactory.loadTilesFromJson("Tiles.json"));

        if (this.mode == GameMode.LEVEL2) {
            CardDeck cardDeck = new CardDeck("cards.json");
            this.inGameCards = cardDeck.getTier2FlightCards();
        }
        else {
            this.inGameCards = new TrialCardDeck("trialFlightCards.json").getTrialDeck();
        }
        this.playerCount = playerCount;
        this.drawnCard = null;
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
                System.out.println(color + "has already been chosen\n");
                return;
            }
        }
        Player player = new Player(playerName, color);
        flightBoard.addPlayerToGame(player);
    }

    //changes game state to SHIPS_CREATION and notifies observers of it (GUI, TUI, Log)
    public void startGame(){
        this.gameState = SHIPS_CREATION;
    }

    //SHIPS_CREATION METHODS
    public synchronized Tile drawTile (String playerName) {
        Player player = identifyPlayerByName(playerName);
        if (player == null) { return null;}
        Tile drawnTile = tileStack.pop();
        try {
            tileStack.pop();
        } catch (Exception e) {
            System.out.println("Tile stack empty\n");
            return null;
        }
        player.hasDrawnTile(drawnTile);
        return drawnTile;
    }

    public synchronized Tile drawTurnedTile (String playerName, int index) {
        Player player = identifyPlayerByName(playerName);
        if (player == null || (index < 0 || index >= turnedTiles.size())) { return null;}
        Tile drawnTile = turnedTiles.remove(index);
        player.hasDrawnTile(drawnTile);
        return drawnTile;
    }

    //sets the booked tile at index 0 or 1 as the drawn tile for player: playerName
    public Tile drawBookedTile (String playerName, int index) {
        Player player = identifyPlayerByName(playerName);
        if ((player == null) || ((index != 0) && (index != 1))) { return null;}
        Tile drawnTile = player.getShipBoard().removeBookedTile(index);
        if (drawnTile == null) { return null;}
        player.hasDrawnTile(drawnTile);
        return drawnTile;
    }

    public void refuseTile(String playerName) {
        Player player = identifyPlayerByName(playerName);
        Tile removedTile = player.removeDrawnTile();
        turnedTiles.put(removedTile.getKey(), removedTile);
    }

    public void printTurnedTiles() {
        for (int i: turnedTiles.keySet()) {
            System.out.printf("%s (%d), ", turnedTiles.get(i).toString(), i);
            if (i % 5 == 0) {
                System.out.println("\n");
            }
        }
    }

    public Map<Integer, Tile> getTurnedTiles() {
        return turnedTiles;
    }

    public void printBookedTiles(String playerName) {
        Player player = identifyPlayerByName(playerName);
        ArrayList<Tile> bookedTiles = player.getShipBoard().getBookedTiles();
        int i = 0;
        for (Tile bookedTile : bookedTiles) {
            System.out.printf("%s (%d), ", bookedTile.toString(), i);
        }
        System.out.println("\n");
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

    public boolean playerSetTile (String playerName, Coordinates coordinates) {
        Player player = identifyPlayerByName(playerName);
        return player.getShipBoard().positionTile(Optional.ofNullable(player.getDrawnTile()), coordinates);
    }

    public boolean playerBookTile (String playerName) {
        Player player = identifyPlayerByName(playerName);
        if (player.getDrawnTile() == null) {
            return false;
        }
        if (player.getShipBoard().addBookedTile(player.removeDrawnTile()))
            return true;
        else {
            System.out.println("Booked tiles are full\n");
            return false;
        }
    }


    public void endShipCreation() {
        this.gameState = VERIFY_SHIP_CORRECTNESS;
    }

    //VERIFY_SHIP_CORRECTNESS METHODS

    //DRAW_CARD METHODS

    public void drawCard(Map<String, VirtualView> viewsMap) {
        this.drawnCard = inGameCards.removeFirst();
        drawnCard.initializeCard(this, viewsMap);
        this.gameState = CARD_EVENT;
    }

    public void endCardPhase() {
        this.gameState = CONCLUDE_GAME;
    }

    //CARD_EVENT METHODS

    public void cardEvent(Message message) {
        drawnCard.executeCard(message);
    }

    public void endCardEvent() {
        System.out.print("Card Finished\n");
        this.getFlightBoard().concludeMovement();
        this.gameState = DRAW_CARD;
    }



    //GETTER METHODS

    //This flag is needed from some game functions
    public GameMode getMode() {
        return mode;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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

    //returns the current GameState
    public GameState getGameState() {
        return gameState;
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
    }

    public Card getDrawnCard() {
        return drawnCard;
    }
}
