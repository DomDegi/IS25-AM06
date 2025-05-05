package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ClientMessage.*;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VirtualControllerSocket implements VirtualController{

    private DisplayableView view;

    private ServerHandler serverHandler;

    public VirtualControllerSocket(DisplayableView view, ServerHandler serverHandler) {
        this.view = view;
        this.serverHandler = serverHandler;
    }

    @Override
    public void login(String playerName) throws RemoteException {
        serverHandler.sendClientMessage(new LoginMessage(playerName));
    }

    @Override
    public void createGame(String gameName, int playerCount, GameMode chooseMode) throws RemoteException {
        CreateGameMessage message = new CreateGameMessage(gameName, playerCount, chooseMode);
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void joinGame(String gameName) throws RemoteException {
        JoinGameMessage message = new JoinGameMessage(gameName);
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void leaveGame() throws RemoteException {
        LeaveGameMessage message = new LeaveGameMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void leave() throws RemoteException {
        LeaveMessage message = new LeaveMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void chooseColor(PlayersColor color) throws RemoteException {
        serverHandler.sendClientMessage(new ChooseColorMessage(color));
    }

    @Override
    public void notifySetTile(Tile tile) throws RemoteException {

    }

    @Override
    public void notifyRefusedTile() throws RemoteException {

    }

    @Override
    public void reqDrawTileFromTurned(int index) throws RemoteException {
        DrawTileTurnedMessage message = new DrawTileTurnedMessage(index);
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void reqDrawTileFromStack() throws RemoteException {
        DrawTileStackMessage message = new DrawTileStackMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void notifyTileBooking() throws RemoteException {

    }

    @Override
    public void lookCardsRequest(int deckToLookAt) throws RemoteException {
        LookGameCardMessage message = new LookGameCardMessage(deckToLookAt);
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void stopLookingAtCardsRequest() throws RemoteException {
        StopLookingCardsMessage message = new StopLookingCardsMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void drawCards() throws RemoteException {

    }

    @Override
    public void sendCoordinates(ArrayList<Coordinates> coordinates) throws RemoteException {

    }

    @Override
    public void sendDoubleCannonUsed(float Strength, ArrayList<Coordinates> coordinates) throws RemoteException {

    }

    @Override
    public void sendNumDoubleEngineUsed(int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException {

    }

    @Override
    public void sendYes() throws RemoteException {

    }

    @Override
    public void sendNo() throws RemoteException {

    }

    @Override
    public void sendTurnHourGlass() throws RemoteException {

    }

    @Override
    public void rollTheDices() throws RemoteException {

    }

    @Override
    public void planetChoiceRequest(int choice) throws RemoteException {

    }

    @Override
    public void notifyEarlyLanding() throws RemoteException {

    }

    @Override
    public void notifyCompleted() throws RemoteException {

    }

    @Override
    public void notifySetPosition(int position) throws RemoteException {

    }

    @Override
    public void notifyNewGoodsArrangement(int clientGoodsValue, ArrayList<CargoHold> updatedCargos) throws RemoteException {

    }

    @Override
    public void notifyNewCrewArrangement(ArrayList<Tile> updatedCabin) throws RemoteException {

    }

    @Override
    public void setView(VirtualView virtualView) throws RemoteException {
    }

    @Override
    public void ping() throws RemoteException {

    }
}
