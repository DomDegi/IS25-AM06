package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface VirtualView extends ViewInterface {

    void notifyNewTurnedTile(Tile tile) throws RemoteException;//add a new turned tile to the map in the clients
    void notifyRemoveTurnedTile(Tile tile) throws RemoteException;//remove a turned tile to the map in the clients
    void notifyPlayerMovement(String playerName, int playerPosition, int playerRanking) throws RemoteException;
    void notifyPositionedTile(String playerName,Tile tile) throws RemoteException;//notify that a tile was placed by a player
    void notifyBookedTile (String playerName, Tile tile) throws RemoteException;
    void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException;
    void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException; //notify which cards deck are not looked at
    void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException;
    void notifyGainedCredits (String playerName, int totalCredits) throws RemoteException;
    void  notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates);
    void notifyChangesWhileGone(Map<String, LightShipBoard> updatedShipBoards, LightFlightboard updatedFlightBoard, Card drawnCard, int hourglassTurns, Map<Integer, Tile> turnedTiles, ArrayList<Integer> notAvailable) throws RemoteException;
    void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException;
}
