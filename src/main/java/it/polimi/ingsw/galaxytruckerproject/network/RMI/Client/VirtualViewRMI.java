package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.client.GamePhases;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class VirtualViewRMI extends UnicastRemoteObject implements VirtualView {
    //private final VirtualController server;
    private final ClientController clientController;
    private final DisplayableView view;

    public VirtualViewRMI(ClientController clientController, DisplayableView view) throws RemoteException {
        super();
        this.clientController = clientController;
        this.view = view;
    }

    public void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print(">  ");
            int command = scan.nextInt();
            //da adattare al comando in ingresso
        }
    }

    @Override
    public void askNickname() throws IOException {
        view.askNickname();
    }

    @Override
    public void askColor() {
        view.askColor();
    }

    @Override
    public void showLoginResponse(boolean success) {
        view.showLoginResponse(success);
        if (success) {
            clientController.setState(ClientState.LOBBY);
        }
    }

    @Override
    public void setClientState(ClientState newState) {
        view.setClientState(newState);
        clientController.setState(newState);
    }

    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) {
        view.showJoinableGamesList(joinableGames);
    }

    //A CHE SERVE
    @Override
    public void askPlayerCount() throws IOException {
        view.askPlayerCount();
    }

    @Override
    public void createGame() throws IOException {
        view.createGame();
    }

    @Override
    public void joinGame() throws IOException {
        view.joinGame();
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        view.showGenericMessage(genericMessage);
    }

    @Override
    public void showErrorMessage(String errorMessage) {view.showErrorMessage(errorMessage);}

    @Override
    public void showInGamePlayers(ArrayList<Player> players) {view.showInGamePlayers(players);}

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {
        view.showPlayersBoard(player, shipBoard);
    }

    @Override
    public void showDrawnTile(Tile drawnTile) {
        view.showDrawnTile(drawnTile);
        clientController.setState(ClientState.S_MANAGE_DRAWN_TILE);
        clientController.setTileInHand(drawnTile);
    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {
        view.showTurnedTiles(turnedTiles);
    }

    public void notifyNewTurnedTile(Tile tile) throws RemoteException {
        clientController.addTurnedTile(tile);
    }

    @Override
    public void notifyRemoveTurnedTile(Tile tile) throws RemoteException {
        clientController.removeTurnedTile(tile);
    }

    @Override
    public void notifyPositionedTile(String playerName, Tile tile) throws RemoteException {
        clientController.setTile(playerName, tile);
    }

    @Override
    public void notifyBookedTile(String playerName, Tile tile) throws RemoteException {
        clientController.addBookedTile(playerName, tile);
    }

    @Override
    public void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException {
        clientController.removeBookedTile(playerName, tile);
    }

    @Override
    public void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException {
        clientController.decksNotAvailable(lockedSmallDecks);
    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        view.notifyDrawnCard(card);
        clientController.setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
        clientController.setDisplayedCard(card);
    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        if (playerName.equals(clientController.getName())) {
            clientController.setState(ClientState.MANAGE_GOODS);
        }
        view.notifyPlayerLandedOnPlanet(playerName, planet);
    }

    @Override
    public void wrongLocalInput() {
        view.wrongLocalInput();
    }

    @Override
    public void showCard(Card card) {
        view.showCard(card);
    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {
        view.printFlightboard(lightFlightboard);
    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {
        view.printShipboard(lightShipBoard);
    }

    @Override
    public void printProjectile(Projectile projectile) {
        view.printProjectile(projectile);
    }

    @Override
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException {
        clientController.modifyTiles(playerName, tiles);
        if (playerName.equals(clientController.getName())) {
            view.printShipboard(clientController.getLightShipBoard());
        }
    }

    @Override
    public void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException {
        clientController.gainCredit(playerName, totalCredits);
    }

    @Override
    public void notifyPlayerMovement(String playerName, int playerPosition, int playerRanking) throws RemoteException {
        clientController.updateFlightboard(playerName, playerPosition, playerRanking);
    }

    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) {
        view.showBookedTiles(bookedTiles);
    }

    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) {
        view.showInGameCards(inGameCards);
    }

    @Override
    public void showDiceRoll(int diceRoll) {
        view.showDiceRoll(diceRoll);
    }

    @Override
    public void showWrongInputMessage() {
        view.showWrongInputMessage();
        clientController.rollBackState();
    }

    @Override
    public void asksToRollTheDices() {
        view.asksToRollTheDices();
    }

    @Override
    public void asksToChooseStartingPosition() {
        view.asksToChooseStartingPosition();
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {
        clientController.setState(ClientState.COORD_REQUEST);
        clientController.getCoordInputManager().setCoordReqType(coordReqType);
        view.asksToInputCoordinates(coordReqType);
    }

    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) {
        clientController.brokenTiles(playerName, coordinates);
        if (playerName.equals(clientController.getName())) {
            view.printShipboard(clientController.getLightShipBoard());
        }
    }

    @Override
    public void notifyChangesWhileGone(Map<String, LightShipBoard> updatedShipBoards, LightFlightboard updatedFlightBoard, Card drawnCard, int hourglassTurns, Map<Integer, Tile> turnedTiles, ArrayList<Integer> notAvailable) throws RemoteException {
        clientController.updateModel(updatedShipBoards, updatedFlightBoard, drawnCard, hourglassTurns, turnedTiles, notAvailable);
    }

    @Override
    public void asksToTurnTheHourglass() {
        view.asksToTurnTheHourglass();
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() {view.showCard(clientController.getDisplayedCard());}

    @Override
    public void notifyYourShipIsCorrect() {
        view.notifyYourShipIsCorrect();
        clientController.setPhase(GamePhases.CARDS);
    }

    @Override
    public void asksToMakeAChoice() {
        view.asksToMakeAChoice();
        setClientState(ClientState.ACTION);
    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {
        view.asksToManageGoods(goods);
        setClientState(ClientState.MANAGE_GOODS);
    }

    @Override
    public void showScores(ArrayList<Player> players) {
        view.showScores(players);
    }

    @Override
    public void asksToRemoveGoods() {
        view.asksToRemoveGoods();

    }// aggiorna: deve chiamare il nuovo rimuovitore di goods dal coordinate request handler

    @Override
    public void asksToRemoveCrew() {
        view.asksToRemoveCrew();
    }// aggiorna: deve chiamare il nuovo rimuovitore di crew dal coordinate request handler
}
