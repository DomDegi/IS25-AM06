package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.MultiGameController;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Cabin;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController{


    private final MultiGameController multiController;
    //final List<ViewInterface> clients = new ArrayList<>();
    private final HashMap <String, Controller> clients;

    public VirtualControllerRMI(MultiGameController multiController) throws RemoteException {
        super();
        this.multiController = multiController;
        this.clients = new HashMap<>();
    }

    public void login(String playerName) throws RemoteException {
            if (clients.containsKey(playerName)) {
                clients.get(playerName).login(playerName);
            }
    }

    @Override
    public void drawCards(String name) throws RemoteException {

    }

    public void connect(VirtualView client, String playerName) throws RemoteException {

        Controller controllerPlayer = new Controller(multiController, client);

        synchronized (this.clients) {
            this.clients.put(playerName, controllerPlayer);
            controllerPlayer.login(playerName);

        }
    }

    @Override
    public void connect(ViewInterface client, String playerName) throws RemoteException {

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

    public void chooseColor(String playerName, PlayersColor color) throws RemoteException {
        clients.get(playerName).chooseColor(color);
    }

    @Override
    public void notifySetTile(String playerName, Coordinates coordinates, boolean booked, int key) throws RemoteException {

    }

    /*public void setFlightBoard(String playerName, int chosen) throws RemoteException {
        clients.get(playerName).setFlightBoard(chosen);
    }*/

    public void sendCoordinates(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException{

    }

    public void sendDoubleCannonUsed( String playerName ,float doublePower, ArrayList<Coordinates> batteries) throws RemoteException {
        clients.get(playerName).useCannons(doublePower, batteries);
    }

    public void sendNumDoubleEngineUsed(String playerName , int numEngine, ArrayList<Coordinates> batteries) throws RemoteException {
        clients.get(playerName).useEngines(numEngine, batteries);

    }

    @Override
    public void notifySetTile(String playerName, Coordinates coordinates, Tile tile) throws RemoteException {

    }

    public void sendYes( String playerName) throws RemoteException{
        clients.get(playerName).makeAChoice(true);
    }

    public void sendNo(String playerName) throws RemoteException{
        clients.get(playerName).makeAChoice(false);
    }

    @Override
    public void sendEndShipBoardCreation(String playerName) throws RemoteException {

    }

    public void notifySetTile(String playerName, Tile tile) throws RemoteException {
        clients.get(playerName).setTile(tile);
    }

    /*public void sendEndShipBoardCreation(String playerName) throws RemoteException {
        AcceptMessage message = new AcceptMessage(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }*/

    public void notifyTileBooking(String playerName) throws RemoteException {
        clients.get(playerName).bookTile();
    }

    public void notifyRefusedTile(String playerName) throws RemoteException {
        clients.get(playerName).refuseTile();
    }

    //è inutile(sì)
    /*
    public void drawTileRequest(String playerName) throws RemoteException {
        DrawTileFromStackRequest message = new DrawTileFromStackRequest(playerName);
        clients.get(playerName).playerChoiceThroughMessage(message);
    }*/

    //DONE
    public void reqDrawTileFromStack(String playerName) throws RemoteException {
        clients.get(playerName).drawTileFromStack();
    }

    //DONE
    public void reqDrawTileFromTurned(String playerName, int index) throws RemoteException {
        clients.get(playerName).drawTileFromTurned(index);
    }

    @Override
    public void refuseTile(int index) throws RemoteException {

    }

    @Override
    public void showCardsRequest(int index) throws RemoteException {

    }

    @Override
    public void stopLookingAtCardsRequest(String playerName) throws RemoteException {

    }

    //DONE(in teoria non serve dato che vengono gestite in locale)
    /*public void reqDrawTileFromBooked(String playerName, int index) throws RemoteException {
        clients.get(playerName).drawTileFromBooked(index);
    }*/

    //DONE
    public void sendTurnHourGlass(String playerName) throws RemoteException {
        clients.get(playerName).turnHourglass();
    }

    @Override
    public void reqDrawTileFromPile(String playerName) throws RemoteException {

    }

    @Override
    public void reqDrawTileFromTable(String playerName, int index) throws RemoteException {

    }

    public void notifyEarlyLanding(String playerName) throws RemoteException {

    }

    @Override
    public void notifyNewGoodsArrangement(String playerName, int clientGoodsValue, ArrayList<CargoHold> modifiedCargoHold) throws RemoteException {

    }


    //DONE
    public void lookCardsRequest(String playerName, int deckToLookAt) throws RemoteException {
        clients.get(playerName).lookGameCards(deckToLookAt);
    }

    @Override
    public void rolldice(String name) throws RemoteException {

    }

    //DONE
    public void StopLookingAtCardsRequest(String playerName) throws RemoteException {
        clients.get(playerName).stopLookingAtCards();
    }

    //DONE
    public void notifyCompleted(String playerName) throws RemoteException {
        clients.get(playerName).completedShip();
    }

    //DONE
    public void notifySetPosition(String playerName, int position) throws RemoteException {
        clients.get(playerName).setPosition(position);
    }

    //DONE
    public void planetChoiceRequest(String playerName,int planet) throws RemoteException {
        clients.get(playerName).choosePlanet(planet);
    }

    @Override
    public void notifyNewCrewArrangement(String name, ArrayList<Cabin> newTiles) {

    }

    public void newGoodsArrangement(String playerName, int creditsToVerify, ArrayList<CargoHold> cargosToUpdate) throws Exception {
        clients.get(playerName).manageGoods(creditsToVerify, cargosToUpdate);
    }






}
