package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

public interface GameInterface {

    //Methods that add player and setups the game + other getter and setter methods for the player list
    void setPlayerCount(int playerCount);
    void addPlayer(String playerName, PlayersColor color);
    void startGame();
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


    //Methods for ships creation phase
    Tile drawTile(String playerName);
    Tile drawTurnedTile(String playerName, int index);
    Tile drawBookedTile(String playerName, Coordinates coordinates, int key);
    Tile refuseTile(String playerName);
    void endShipCreation();
    void drawCard(Map<String, VirtualView> playersView);
    void endCardPhase();
    void endCardEvent();
    ArrayList<Card> getInGameCards (int number);
    Map<Integer,Tile> getTurnedTiles();
    Tile playerSetTile(String playerName, Coordinates coordinates, int key);
    Tile playerBookTile(String playerName);

    //Card phase cards
    int getCardsLeft();
    void cardEvent(Message message);
    Card getDrawnCard();
}
