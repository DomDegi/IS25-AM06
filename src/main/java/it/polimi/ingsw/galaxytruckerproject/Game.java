package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;
import java.util.Stack;

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
            observer.notifyStateChange(newGameState);
        }
    }

    //adds player to game with the input string as name
    public void AddPlayer(String playerName){
        Player player = new Player(playerName);
        this.listOfPlayers.add(player);
    }

    //changes game state to SHIPS_CREATION and notifies observers of it (GUI, TUI, Log)
    public void StartGame(){
        System.out.println("Game Started with" + listOfPlayers.size() + " players");
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

    public void RefuseTile(String playerName) {
        Player player = IdentifyPlayerByName(playerName);
        Tile removedTile = player.removeDrawnTile();
        turnedTiles.add(removedTile);
    }

    public void printTurnedTiles() {
        int i;
        for (i = 0; i < turnedTiles.size(); i++) {
            System.out.printf("%s (%d), ", turnedTiles.get(i).toString(), i);
            if (i % 5 == 0) {
                System.out.println("\n");
            }
        }
    }

    public synchronized void lookInGameCards1 (String playerName) {
        Player player = IdentifyPlayerByName(playerName);

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
}
