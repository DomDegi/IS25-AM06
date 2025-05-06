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
        SetTileMessage message = new SetTileMessage(tile);
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void notifyRefusedTile() throws RemoteException {
        RefuseTileMessage message = new RefuseTileMessage();
        serverHandler.sendClientMessage(message);
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
        TileBookingMessage message = new TileBookingMessage();
        serverHandler.sendClientMessage(message);
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
        serverHandler.sendClientMessage(new DrawCardMessage());
    }

    @Override
    public void sendCoordinates(ArrayList<Coordinates> coordinates) throws RemoteException {
        //??
    }

    @Override
    public void sendDoubleCannonUsed(float Strength, ArrayList<Coordinates> coordinates) throws RemoteException {
        serverHandler.sendClientMessage(new CannonMessage(Strength, coordinates));
    }

    @Override
    public void sendNumDoubleEngineUsed(int NumEngine, ArrayList<Coordinates> coordinates) throws RemoteException {
        serverHandler.sendClientMessage(new EngineMessage(NumEngine, coordinates));
    }

    @Override
    public void sendYes() throws RemoteException {
        SendYesMessage message = new SendYesMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void sendNo() throws RemoteException {
        SendNoMessage message = new SendNoMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void sendTurnHourGlass() throws RemoteException {
        TurnHourglassMessage message = new TurnHourglassMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void rollTheDices() throws RemoteException {
        RollDicesMessage message = new RollDicesMessage();
        serverHandler.sendClientMessage(message);
    }

    @Override
    public void planetChoiceRequest(int choice) throws RemoteException {
        serverHandler.sendClientMessage(new PlanetChoiceMessage(choice));
    }

    @Override
    public void notifyEarlyLanding() throws RemoteException {
        serverHandler.sendClientMessage(new EarlyLandingMessage());
    }

    @Override
    public void notifyCompleted() throws RemoteException {
        serverHandler.sendClientMessage(new CompletedShipMessage());
    }

    @Override
    public void notifySetPosition(int position) throws RemoteException {
        serverHandler.sendClientMessage(new SetFlightBoardPositionMessage(position));
    }

    @Override
    public void notifyNewGoodsArrangement(int clientGoodsValue, ArrayList<CargoHold> updatedCargos) throws RemoteException {
        serverHandler.sendClientMessage(new GoodsArrangementMessage(clientGoodsValue, updatedCargos));
    }

    @Override
    public void notifyNewCrewArrangement(ArrayList<Tile> updatedCabin) throws RemoteException {
        serverHandler.sendClientMessage(new CrewArrangementMessage(updatedCabin));
    }

    @Override
    public void setView(VirtualView virtualView) throws RemoteException {
    }

    @Override
    public void ping() throws RemoteException {
        serverHandler.sendClientMessage(new ClientPingMessage());
    }
}
