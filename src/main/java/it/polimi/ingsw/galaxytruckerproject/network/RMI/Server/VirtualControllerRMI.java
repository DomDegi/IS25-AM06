package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.*;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.ArrayList;

public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController{


    private final MultiGameController multiController;
    //final List<ViewInterface> clients = new ArrayList<>();
    private final HashMap <String, Controller> clients;

    protected VirtualControllerRMI(MultiGameController multiController, HashMap<String, Controller> clients) throws RemoteException {
        super();
        this.multiController = multiController;
        clients = new HashMap<String, Controller>();
    }

    public void connect(ViewInterface client, String playerName) throws RemoteException {

        Controller controllerPlayer = new Controller(multiController, client);

        synchronized (this.clients) {
            this.clients.put(playerName, controllerPlayer);
            controllerPlayer.login(playerName);

        }
    }

    public void createGame(String gameName, int playerCount, GameMode chooseMode, String playerName) throws RemoteException {
        clients.get(playerName).createGame(gameName, playerCount, chooseMode);
    }

    public void joinGame(String gameName, String playerName) throws RemoteException {
        clients.get(playerName).joinGame(gameName);
    }

    public void leaveGame(String playerName) throws RemoteException {
        clients.get(playerName).leaveGame();
    }

    public void leave(String playerName) throws RemoteException {
        clients.get(playerName).leave();
    }

    public void chooseColor(String playerName, String color) throws RemoteException {
        clients.get(playerName).chooseColor(color);
    }


    public void sendCoordinates(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new SendCoordinatesResponse(playerName, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }
    public void sendDoubleCannonUsed( String playerName ,float Strength, ArrayList<Coordinates> coordinates) throws RemoteException {
        Message message = new UseCannonResponse(playerName, Strength, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendNumDoubleEngineUsed(String playerName , int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException{
        Message message = new UseEngineResponse(playerName, NumEngine, coordinates);
        clients.get(playerName).playerChoiceThroughMessage(message);

    }


    public void sendYes( String playerName) throws RemoteException{
        Message message = new AcceptMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendNo(String playerName) throws RemoteException{
        Message message = new RefuseMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifySetTile(String playerName, Coordinates coordinates, Tile tile) throws RemoteException {
        Message message= new SetTileRequest(playerName, coordinates, tile);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendEndShipBoardCreation(String playerName) throws RemoteException {
        AcceptMessage message = new AcceptMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyTileBooking(String playerName) throws RemoteException {
        BookTileRequest message = new BookTileRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyRefusedTile(String playerName) throws RemoteException {
        RefuseMessage message = new RefuseMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }


    public void drawTileRequest(String playerName) throws RemoteException {
        DrawTileFromStackRequest message = new DrawTileFromStackRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }


    public void reqDrawTileFromPile(String playerName) throws RemoteException {
        DrawTileFromStackRequest message = new DrawTileFromStackRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void reqDrawTileFromTable(String playerName, int index) throws RemoteException {
        DrawTileFromTurnedRequest message = new DrawTileFromTurnedRequest(playerName, index);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void sendTurnHourGlass(String playerName) throws RemoteException {
        TurnHourglassRequest message = new TurnHourglassRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyEarlyLanding(String playerName) throws RemoteException {
        EarlyLandingRequest message = new EarlyLandingRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void notifyNewGoodsArrangement(String playerName,int clientGoodsValue, ArrayList<CargoHold> modifiedCargoHold) throws RemoteException{
        Message message = new ManageGoodsResponse(playerName,clientGoodsValue, modifiedCargoHold);
        clients.get(playerName).playerChoiceThroughMessage(message);

    }

    public void showCardsRequest(String playerName, int deckToLookAt) throws RemoteException {
        ShowCardsRequest message = new ShowCardsRequest(playerName, deckToLookAt);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void StopLookingAtCardsRequest(String playerName) throws RemoteException {
        StopLookingAtCardsRequest message = new StopLookingAtCardsRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }

    public void PlanetChoiceRequest(String playerName) throws RemoteException {
        PlanetChoiceRequest message = new PlanetChoiceRequest();
        clients.get(playerName).playerChoiceThroughMessage(message);
    }







}
