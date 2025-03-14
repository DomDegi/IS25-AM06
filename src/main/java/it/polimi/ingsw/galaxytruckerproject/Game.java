package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.observers.GameObserver;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.galaxytruckerproject.GameState.*;

public class Game {
    private GameState gameState;
    private final ArrayList<GameObserver> observerList = new ArrayList<>();
    private final ArrayList<Player> listOfPlayers;
    private int hourglassTurns;
    private CardDeck cardDeck;
    private ArrayList<Card> inGameCards;
    private TilesDeck tileDeck;
    private Stack<Tile> tileStack;
    private ArrayList<Tile> turnedTiles;
    private FlightBoard flightBoard;
    private Card drawnCard;
    private ScheduledExecutorService hourglass = Executors.newScheduledThreadPool(1);


    //instances a new game starting in the state START_GAME
    public Game() {
        this.gameState = GameState.START_GAME;
        this.listOfPlayers = new ArrayList<>();
        this.turnedTiles = new ArrayList<>();
        this.flightBoard = new FlightBoard();
        this.hourglassTurns = 0;
        this.tileDeck = new TileDeck("tiles.json");
        this.tileStack = tileDeck.getStack();
        this.cardDeck = new CardDeck("cards.json");
        this.inGameCards = cardDeck.getTier2FlightCards();
    }

    //add a new observer type to the game
    public void AddObserver(GameObserver observer) {
        observerList.add(observer);
    }

    //notify all the instanced observers about a change in GameState
    public void notifyObservers(GameState newGameState) {
        for (GameObserver observer: observerList){
            observer.notifyChanges(newGameState);
        }
    }

    //adds player to game with the input string as name
    public void AddPlayer(String playerName, PlayersColor color) {
        for (Player player: listOfPlayers){
            if (color.equals(player.getPlayerColor())){
                System.out.println(color + "has already been chosen");
            }
        }
        Player player = new Player(playerName, color);
        this.listOfPlayers.add(player);
    }

    //changes game state to SHIPS_CREATION and notifies observers of it (GUI, TUI, Log)
    public void StartGame(){
        this.gameState = SHIPS_CREATION;
        notifyObservers(gameState);
    }

    //SHIPS_CREATION METHODS

    public Tile DrawTile (String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        if (player == null) { return null;}
        Tile drawnTile = tileStack.pop();
        try {
            tileStack.pop();
        } catch (Exception e) {
            System.out.println("Tile stack empty");
            return null;
        }
        player.hasDrawnTile(drawnTile);
        return drawnTile;
    }

    public Tile DrawTurnedTile (String playerName, int index) {
        Player player = IdentifyPlayerByName(playerName);
        if (player == null || (index < 0 || index >= turnedTiles.size())) { return null;}
        Tile drawnTile = turnedTiles.remove(index);
        player.hasDrawnTile(drawnTile);
        return drawnTile;
    }

    //sets the booked tile at index 0 or 1 as the drawn tile for player: playerName
    public Tile DrawBookedTile (String playerName, int index) {
        Player player = IdentifyPlayerByName(playerName);
        if ((player == null) || ((index != 0) && (index != 1))) { return null;}
        Tile drawnTile = player.getShipBoard().removeBookedTile(index);
        if (drawnTile == null) { return null;}
        player.hasDrawnTile(drawnTile);
        return drawnTile;
    }

    public void RefuseTile(String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        Tile removedTile = player.removeDrawnTile();
        turnedTiles.add(removedTile);
    }

    public void printTurnedTiles() {
        for (int i = 0; i < turnedTiles.size(); i++) {
            System.out.printf("%s (%d), ", turnedTiles.get(i).toString(), i);
            if (i % 5 == 0) {
                System.out.println("\n");
            }
        }
    }

    public void printBookedTiles(String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        ArrayList<Tile> bookedTiles = player.getShipBoard().getBookedTiles();
        int size = bookedTiles.size();
        for (Tile bookedTile : bookedTiles) {
            System.out.println(bookedTile.toString() + " ");
        }
        System.out.println("\n");
    }

    public synchronized void lookInGameCards1 (String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        System.out.println(player.getPlayerName() + " here are the 3 cards contained in flightBoard deck 1: ");
        for (int i = 0; i < 3; i++) {
            System.out.println(inGameCards.get(i).toString());
        }
        System.out.println("\n");
    }

    public synchronized void lookInGameCards2 (String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        System.out.println(player.getPlayerName() + " here are the 3 cards contained in flightBoard deck 2: ");
        for (int i = 3; i < 6; i++) {
            System.out.println(inGameCards.get(i).toString());
        }
        System.out.println("\n");
    }

    public synchronized void lookInGameCards3 (String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        System.out.println(player.getPlayerName() + " here are the 3 cards contained in flightBoard deck 3: ");
        for (int i = 6; i < 9; i++) {
            System.out.println(inGameCards.get(i).toString());
        }
        System.out.println("\n");
    }

    public boolean playerSetTile (String playerName, Coordinates coordinates) {
        Player player = IdentifyPlayerByName(playerName);
        return player.getShipBoard.positionTile(player.getDrawnTile, coordinates);
    }

    public boolean playerBookTile (String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        if (player.getDrawnTile() == null) {
            return false;
        }
        player.getShipBoard().addBookedTile(player.removeDrawnTile());
        return true;
    }


    //GETTER METHODS

    //input a string and if it's the same as a player name returns the player
    public Player IdentifyPlayerByName (String playerName) {
        for (Player player: listOfPlayers) {
            if (playerName.equals(player.getPlayerName())) {
                return player;
            }
        }
        System.out.println("There is no player with that name");
        return null;
    }

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    //returns the number of player in the game
    public int getNumberOfPlayers() {
        return listOfPlayers.size();
    }

    //returns the current GameState
    public GameState getGameState() {
        return gameState;
    }

    //returns the player's name at playerIndex (0 to 3) as a string
    public String getPlayerName(int playerIndex) {
        return listOfPlayers.get(playerIndex).getPlayerName();
    }

    public ShipBoard getPlayerShipBoard (String playerName) {
        Player player =  IdentifyPlayerByName(playerName);
        if (player == null) {
            return null;
        }
        return player.getShipBoard();
    }

    public int getHourglassTurns() {
        return hourglassTurns;
    }

    public void StartTimer() {
        hourglass.schedule(() -> {
            60, TimeUnit.SECONDS;
        })
    }
}
