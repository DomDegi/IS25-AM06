package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.Observer;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Map;

public interface GameInterface{

    //observer methods
    void addObserver(Observer o);
    void removeObserver(Observer o);

    //Methods that add player and setups the game + other getter and setter methods for the player list
    void setPlayerCount(int playerCount);
    void addPlayer(String playerName, PlayersColor color);
    void startShipCreation();
    int getPlayerCount();
    Player identifyPlayerByName(String playerName);
    ArrayList<Player> getListOfInFlightPlayers();
    void setPodium();
    GameState getGameState();
    ArrayList<Player> getListOfAllPlayer();
    int getNumberOfPlayers();
    ShipBoard getPlayerShipBoard(String s);
    FlightBoard getFlightBoard();
    GameMode getMode();
    Player getFirstRankedPlayer();


    //Methods for ships creation phase
    Tile drawTile(String playerName);
    Tile drawTurnedTile(String playerName, int index);
    Tile drawAndPositionBookedTile(String playerName, Tile tile);
    Tile refuseTile(String playerName);
    void endShipCreation();
    void endShipVerification();
    void drawCard();
    void endCardPhase();
    void endCardEvent();
    ArrayList<Card> getInGameCards (int number);
    Map<Integer,Tile> getTurnedTiles();
    Tile playerSetTile(String playerName, Tile tile);
    Tile playerBookTile(String playerName);

    //Card phase cards
    int getCardsLeft();
    Card getDrawnCard();
}
