package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;

public interface GameInterface {

    //Methods that add player and setups the game + other getter and setter methods for the player list
    void setPlayerCount(int playerCount);
    void addPlayer(String playerName, PlayersColor color);
    void startGame();
    int getPlayerCount();
    Player identifyPlayerByName(String playerName);
    ArrayList<Player> getListOfPlayers();
    void setPodium();
    GameState getGameState();
    ArrayList<Player> getListOfAllPlayer();
    int getNumberOfPlayers();
    ShipBoard getPlayerShipBoard(String s);
    FlightBoard getFlightBoard();
    GameMode getMode();


    //Methods for ships creation phase
    Tile drawTile(String playerName);
    Tile drawTurnedTile(String playerName, int index);
    Tile drawBookedTile(String playerName, int index);
    void refuseTile(String playerName);
    void startTimer();
    void endShipCreation();
    void drawCard();
    void endCardPhase();
    void endCardEvent();
    void lookInGameCards1(String playerName);
    void lookInGameCards2(String playerName);
    void lookInGameCards3(String playerName);
    boolean getHourglassState();
    int getHourglassTurns();
    void printTurnedTiles();
    void printBookedTiles(String playerName);
    boolean playerSetTile(String playerName, Coordinates coordinates);
    boolean playerBookTile(String playerName);

    //Card phase cards
    int getCardsLeft();
    void cardEvent(String playerName, String[] input);
}
