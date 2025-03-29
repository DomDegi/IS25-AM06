package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.cards.TrialCardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory;

import java.util.*;

import static it.polimi.ingsw.galaxytruckerproject.model.GameState.*;

public class Game implements GameInterface {
    private final GameMode mode;
    private GameState gameState;
    private int playerCount;
    private final ArrayList<Card> inGameCards;
    private final ArrayDeque<Tile> tileStack;
    private final ArrayList<Tile> turnedTiles;
    private final FlightBoard flightBoard;
    private Card drawnCard;
    private int hourglassTurns;
    private boolean hourglassON;


    //instances a new game starting in the state START_GAME
    public Game(GameMode mode) {
        this.mode = mode;
        this.gameState = START_GAME;
        this.turnedTiles = new ArrayList<>();
        this.flightBoard = new FlightBoard(mode);
        this.hourglassTurns = 0;
        this.tileStack = new TileFactory().getStack(TileFactory.loadTilesFromJson("Tiles.json"));

        if (this.mode == GameMode.LEVEL2) {
            CardDeck cardDeck = new CardDeck("cards.json");
            this.inGameCards = cardDeck.getTier2FlightCards();
        }
        else {
            this.inGameCards = new TrialCardDeck("trialFlightCards.json").getTrialDeck();
        }
        this.hourglassON = false;
        this.playerCount = 0;
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
    public Tile drawTile (String playerName) {
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

    public Tile drawTurnedTile (String playerName, int index) {
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
        Player player = identifyPlayerByName(playerName);
        ArrayList<Tile> bookedTiles = player.getShipBoard().getBookedTiles();
        int i = 0;
        for (Tile bookedTile : bookedTiles) {
            System.out.printf("%s (%d), ", bookedTile.toString(), i);
        }
        System.out.println("\n");
    }

    public synchronized void lookInGameCards1 (String playerName) {
        Player player = identifyPlayerByName(playerName);
        System.out.println(player.getPlayerName() + " here are the 3 cards contained in flightBoard deck 1: \n");
        for (int i = 0; i < 3; i++) {
            System.out.println(inGameCards.get(i).toString());
        }
        System.out.println("\n");
    }

    public synchronized void lookInGameCards2 (String playerName) {
        Player player = identifyPlayerByName(playerName);
        System.out.println(player.getPlayerName() + " here are the 3 cards contained in flightBoard deck 2: \n");
        for (int i = 3; i < 6; i++) {
            System.out.println(inGameCards.get(i).toString());
        }
        System.out.println("\n");
    }

    public synchronized void lookInGameCards3 (String playerName) {
        Player player = identifyPlayerByName(playerName);
        System.out.println(player.getPlayerName() + " here are the 3 cards contained in flightBoard deck 3: \n");
        for (int i = 6; i < 9; i++) {
            System.out.println(inGameCards.get(i).toString());
        }
        System.out.println("\n");
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
                    endShipCreation();
                    System.out.println("The time is up, ship creation is over\n");
                }
            }
        }, 95000); //95 seconds
    }

    public void endShipCreation() {
        this.gameState = VERIFY_SHIP_CORRECTNESS;
    }

    //VERIFY_SHIP_CORRECTNESS METHODS

    //DRAW_CARD METHODS

    public void drawCard() {
        this.drawnCard = inGameCards.removeFirst();
        drawnCard.initializeCard(this);
        this.gameState = CARD_EVENT;
    }

    public void endCardPhase() {
        this.gameState = CONCLUDE_GAME;
    }

    //CARD_EVENT METHODS

    public void cardEvent(String playerName, String[] input) {
        drawnCard.executeCard(this, playerName, input);
    }

    public void endCardEvent() {
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

    public ArrayList<Player> getListOfPlayers() {
        return flightBoard.getInGamePlayers();
    }

    //returns the number of player in the game
    public int getNumberOfPlayers() {
        return getListOfPlayers().size();
    }

    //returns the current GameState
    public GameState getGameState() {
        return gameState;
    }

    //returns the player's name at playerIndex (0 to 3) as a string
    public String getPlayerName(int playerIndex) {
        return getListOfPlayers().get(playerIndex).getPlayerName();
    }

    public ShipBoard getPlayerShipBoard (String playerName) {
        Player player =  identifyPlayerByName(playerName);
        if (player == null) {
            return null;
        }
        return player.getShipBoard();
    }

    public int getHourglassTurns() {
        return hourglassTurns;
    }

    public boolean getHourglassState() {
        return hourglassON;
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
