package it.polimi.ingsw.galaxytruckerproject.network.RMI.Server;

import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class VirtualControllerRMI extends UnicastRemoteObject implements VirtualController, Remote , Serializable {


    //private final MultiGameController controller;
    //final List<ViewInterface> clients = new ArrayList<>();
    //private final HashMap <String, Controller> clients;
    private Controller controller;

    public VirtualControllerRMI(Controller controller) throws RemoteException {
        super();
        //this.multiController = multiController;
        //this.clients = new HashMap<String, Controller>();
        this.controller = controller;
    }

    //PHASE OF LOGIN/CREATION OF MATCHES METHODS
    public void login(String playerName) throws RemoteException {
        controller.login(playerName);
    }
    /*public void connect(VirtualView client, String playerName) throws RemoteException {

        Controller controllerPlayer = new Controller(multiController, client);

        synchronized (this.clients) {
            this.clients.put(playerName, controllerPlayer);
            controllerPlayer.login(playerName);

        }
    }
    */
    public void createGame(String gameName, int playerCount, GameMode chooseMode) throws RemoteException {
        controller.createGame(gameName, playerCount, chooseMode);
    }
    public void joinGame(String gameName) throws RemoteException {
        controller.joinGame(gameName);
    }
    public void leaveGame() throws RemoteException {
        controller.leaveGame();
    }

    public void chooseColor( PlayersColor color) throws RemoteException {
        controller.chooseColor(color);
    }

    //COORDINATES MANAGMENT METHODS
    public void sendCoordinates( ArrayList<Coordinates> coordinates) throws RemoteException{
        controller.sendCoordinates(coordinates);
    }

    public void sendDoubleCannonUsed( float doublePower, ArrayList<Coordinates> batteries) throws RemoteException {
        controller.useCannons(doublePower, batteries);
    }

    public void sendNumDoubleEngineUsed( int numEngine, ArrayList<Coordinates> batteries) throws RemoteException {
        controller.useEngines(numEngine, batteries);

    }

    public void sendYes( ) throws RemoteException{
        controller.makeAChoice(true);
    }

    public void sendNo( ) throws RemoteException{
        controller.makeAChoice(false);
    }



    //TILE METHODS
    public void notifySetTile(Tile tile){
        try {
            controller.setTile(tile);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void notifyTileBooking() throws RemoteException {
        controller.bookTile();
    }
    public void notifyRefusedTile() throws RemoteException {
        controller.refuseTile();
    }
    public void reqDrawTileFromStack() throws RemoteException {
        controller.drawTileFromStack();
    }
    public void reqDrawTileFromTurned( int index) throws RemoteException {
        controller.drawTileFromTurned(index);
    }

    //CARD METHODS
    public void lookCardsRequest( int deckToLookAt) throws RemoteException {
        controller.lookGameCards(deckToLookAt);
    }
    public void stopLookingAtCardsRequest() throws RemoteException {
        controller.stopLookingAtCards();
    }
    public void drawCards() throws RemoteException {
        controller.drawCard();
    }

    //DONE
    public void sendTurnHourGlass() throws RemoteException {
        controller.turnHourglass();
    }

    public void notifyEarlyLanding() throws RemoteException {
        controller.earlyLanding();
    }




    //DONE
    public void notifyCompleted() throws RemoteException {
        controller.completedShip();
    }

    //DONE
    public void notifySetPosition( int position) throws RemoteException {
        controller.setPosition(position);
    }

    //DONE
    public void planetChoiceRequest(int planet) throws RemoteException {
        controller.choosePlanet(planet);
    }

    public void notifyNewGoodsArrangement( int creditsToVerify, ArrayList<CargoHold> updatedCargos) throws RemoteException {
        controller.manageGoods(creditsToVerify, updatedCargos);
    }

    public void notifyNewCrewArrangement( ArrayList<Tile> cabins) throws RemoteException {
        controller.pickCrewMembers(cabins);
    }

    public void rollTheDices() throws RemoteException {
        controller.rollTheDices();
    }

    @Override
    public void setView(VirtualView virtualView) throws RemoteException {
        System.out.println("setView");
        controller.setView(virtualView);
    }

    @Override
    public void shipErrorManagement(ArrayList<Coordinates> toRemove) throws RemoteException {
        controller.shipErrorManagement(toRemove);
    }

    @Override
    public void ping() throws RemoteException{
        controller.ping();
    }

    @Override
    public void removeGoods(ArrayList<Coordinates> fromHere) throws RemoteException {
        controller.removeGoods(fromHere);
    }

    @Override
    public void chooseBranch(ArrayList<Coordinates> thisOne) throws RemoteException {
        controller.chooseBranch(thisOne);
    }

    @Override
    public void useBattery(ArrayList<Coordinates> batteries) throws RemoteException {
        controller.useBatteries(batteries);
    }

    @Override
    public void removeCrew(ArrayList<Coordinates> toRemoveFrom) throws RemoteException {
        controller.removeCrew(toRemoveFrom);
    }


}
