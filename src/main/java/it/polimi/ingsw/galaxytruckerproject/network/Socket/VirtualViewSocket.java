package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.Controller;
import it.polimi.ingsw.galaxytruckerproject.controller.interfaces.ControllerInterface;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.AskColorMessage;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerMessage.LoginResponseMessage;
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

    }

    @Override
    public void notifyRemoveTurnedTile(Tile tile) throws RemoteException {

    }

    @Override
    public void notifyPlayerMovement(String playerName, int playerPosition, int playerRanking) throws RemoteException {

    }

    @Override
    public void notifyPositionedTile(String playerName, Tile tile) throws RemoteException {

    }

    @Override
    public void notifyBookedTile(String playerName, Tile tile) throws RemoteException {

    }

    @Override
    public void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException {

    }

    @Override
    public void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException {

    }

    @Override
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException {

    }

    @Override
    public void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException {

    }

    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) throws RemoteException {

    }

    @Override
    public void notifyChangesWhileGone(Map<String, LightShipBoard> updatedShipBoards, LightFlightboard updatedFlightBoard, Card drawnCard, int hourglassTurns, Map<Integer, Tile> turnedTiles, ArrayList<Integer> notAvailable) throws RemoteException {

    }

    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {

    }

    @Override
    public void askNickname() throws IOException {

    }

    @Override
    public void askColor() throws RemoteException {
        AskColorMessage message = new AskColorMessage();
        clientHandler.sendServerMessageToClient(message);
    }

    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        LoginResponseMessage message = new LoginResponseMessage(success);
        clientHandler.sendServerMessageToClient(message);
    }

    @Override
    public void setClientState(ClientState newState) throws RemoteException {

    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {

    }

    @Override
    public void askPlayerCount() throws IOException {

    }

    @Override
    public void createGame() throws IOException {

    }

    @Override
    public void joinGame() throws IOException {

    }

    @Override
    public void showGenericMessage(String genericMessage) throws RemoteException {

    }

    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {

    }

    @Override
    public void showInGamePlayers(ArrayList<Player> players) throws RemoteException {

    }

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) throws RemoteException {

    }

    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {

    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) throws RemoteException {

    }

    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) throws RemoteException {

    }

    @Override
    public void showWrongInputMessage() throws RemoteException {

    }

    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) throws RemoteException {

    }

    @Override
    public void asksToRollTheDices() throws RemoteException {

    }

    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {

    }

    @Override
    public void asksToChooseStartingPosition() throws RemoteException {

    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {

    }

    @Override
    public void asksToTurnTheHourglass() throws RemoteException {

    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {

    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {

    }

    @Override
    public void asksToMakeAChoice() throws RemoteException {

    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) throws RemoteException {

    }

    @Override
    public void asksToRemoveGoods() throws RemoteException {

    }

    @Override
    public void asksToRemoveCrew() throws RemoteException {

    }

    @Override
    public void showScores(ArrayList<Player> players) throws RemoteException {

    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {

    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {

    }

    @Override
    public void wrongLocalInput() throws RemoteException {

    }

    @Override
    public void showCard(Card card) throws RemoteException {

    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) throws RemoteException {

    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard) throws RemoteException {

    }

    @Override
    public void printProjectile(Projectile projectile) throws RemoteException {

    }

    @Override
    public void printCabins(Tile cabins) throws RemoteException {

    }

    @Override
    public void connected() throws RemoteException {

    }

    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) throws RemoteException {

    }

    @Override
    public void notifyTurnedHourglass() throws RemoteException {

    }

    @Override
    public void notifyEndOfTime() throws RemoteException {

    }

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return null;
    }
}
