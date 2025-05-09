package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.*;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.LoginResponseMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.SetClientStateMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.ShowJoinableGamesMessage;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class VirtualViewSocket implements VirtualView {

    private final ClientHandler clientHandler;

    private ControllerInterface controller;


    public VirtualViewSocket(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    @Override
    public void notifyNewTurnedTile(Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NewTurnedTileMessage(tile));
    }

    @Override
    public void notifyRemoveTurnedTile(Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new RemoveTurnedTileMessage(tile));
    }

    @Override
    public void notifyPlayerMovement(String playerName, PlayersColor playersColor, int playerPosition, int playerRanking) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerMovementMessage(playerName,playersColor,  playerPosition, playerRanking));
    }

    @Override
    public void notifyPositionedTile(String playerName, Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PositionedTileMessage(playerName, tile));
    }

    @Override
    public void notifyBookedTile(String playerName, Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new BookedTileMessage(playerName, tile));
    }

    @Override
    public void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new RemovedBookedTileMessage(playerName, tile));
    }

    @Override
    public void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NotAvailableCardDeckMessage(lockedSmallDecks));
    }

    @Override
    public void notifyNotAvailableColor(PlayersColor color) throws RemoteException {
        clientHandler.sendServerMessageToClient(new NotAvailableColorMessage(color));
    }

    @Override
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ModifiedTilesMessage(playerName, tiles));
    }

    @Override
    public void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException {
        clientHandler.sendServerMessageToClient(new GainedCreditsMessage(playerName, totalCredits));
    }

    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException {
        clientHandler.sendServerMessageToClient(new BrokenTileMessage(playerName, coordinates));
    }

    // DA FARE CON JSON
    @Override
    public void notifyChangesWhileGone(Map<String, LightShipBoard> updatedShipBoards, LightFlightboard updatedFlightBoard, Card drawnCard, int hourglassTurns, Map<Integer, Tile> turnedTiles, ArrayList<Integer> notAvailable) throws RemoteException {

    }

    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        clientHandler.sendServerMessageToClient(new FlightBoardCardsMessage(cards));
    }

    @Override
    public void initializeShipBoards(GameMode gameMode) throws RemoteException {
        clientHandler.sendServerMessageToClient(new SetGameModeMessage(gameMode));
    }

    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        LoginResponseMessage message = new LoginResponseMessage(success);
        clientHandler.sendServerMessageToClient(message);
    }

    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        SetClientStateMessage message = new SetClientStateMessage(newState);
        clientHandler.sendServerMessageToClient(message);
    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShowJoinableGamesMessage(joinableGames));
    }

    @Override
    public void showGenericMessage(String genericMessage) throws RemoteException {
        clientHandler.sendServerMessageToClient(new GenericServerMessage(genericMessage));
    }

    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ErrorMessage(errorMessage));
    }

    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShowDrawnTileMessage(drawnTile));
    }

    @Override
    public void showWrongInputMessage() throws RemoteException {
        clientHandler.sendServerMessageToClient(new WrongInputMessage());
    }

    @Override
    public void asksToRollTheDices() throws RemoteException {
        clientHandler.sendServerMessageToClient(new AskToRollTheDicesMessage());
    }

    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShowDiceRollMessage(diceRoll));
    }

    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        clientHandler.sendServerMessageToClient(new AskToChooseStartingPositionMessage());
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {
        clientHandler.sendServerMessageToClient(new AskToInputCoordinatesMessage(coordReqType));
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        clientHandler.sendServerMessageToClient(new FlightboardCardsResponse());
    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {
        clientHandler.sendServerMessageToClient(new ShipIsCorrectMessage());
    }

    @Override
    public void asksToMakeAChoice() throws RemoteException {

    }

    @Override
    public void showScores(ArrayList<Player> players) throws RemoteException {
        clientHandler.sendServerMessageToClient(new FinalScoresMessage(players));
    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        clientHandler.sendServerMessageToClient(new DrawnCardMessage(card));
    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        clientHandler.sendServerMessageToClient(new PlayerLandedOnPlanetMessage(playerName,planet));
    }

    @Override
    public void connected() throws RemoteException {

    }

    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException {
        clientHandler.sendServerMessageToClient(new SetGameModeMessage(gameMode));
    }

    @Override
    public void ping() throws RemoteException {
        clientHandler.sendServerMessageToClient(new ServerPingMessage());
    }

    @Override
    public void notifyTurnedHourglass(int turns) throws RemoteException {
        clientHandler.sendServerMessageToClient(new TurnedHourglassMessage(turns));
    }

    @Override
    public void notifyEndOfTime() throws RemoteException {
        clientHandler.sendServerMessageToClient(new TimeExpiredMessage());
    }

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return null;
    }
}
