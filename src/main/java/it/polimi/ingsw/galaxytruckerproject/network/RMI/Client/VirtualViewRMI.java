package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class VirtualViewRMI extends UnicastRemoteObject implements VirtualView {
    private final VirtualController server;
    public ViewInterface view;

    public VirtualViewRMI(VirtualController server) throws RemoteException {
        super();
        this.server = server;
    }

    public void runCli() throws RemoteException {
        Scanner scan=new Scanner(System.in);
        while (true) {
            System.out.print(">  ");
            int command = scan.nextInt();
            //da adattare al comando in ingresso
        }
    }

    @Override
    public void askNickname() throws IOException {

    }

    @Override
    public void askColor() {
        view.askColor();
    }

    @Override
    public void showLoginResponse(boolean success, boolean connected) {
        view.showLoginResponse(success, connected);
    }

    @Override
    public void asksJoinOrCreate() {
        view.asksJoinOrCreate();


    }

    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) {
        view.showJoinableGamesList(joinableGames);

    }

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
    public void showErrorMessage(String errorMessage) {
        view.showErrorMessage(errorMessage);
    }

    @Override
    public void showInGamePlayers(ArrayList<Player> players) {
        view.showInGamePlayers(players);
    }

    @Override
    public void asksPlayersInfo() {
        view.asksPlayersInfo();
    }

    @Override
    public void showShipsErrors() {
        view.showShipsErrors();
    }

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {
        view.showPlayersBoard(player, shipBoard);
    }

    @Override
    public void showDrawnTile(Tile drawnTile) {
        view.showDrawnTile(drawnTile);
    }

    @Override
    public void showTurnedTiles(ArrayList<Tile> turnedTiles) {
        view.showTurnedTiles(turnedTiles);
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
    public void asksToRollTheDices() {
        view.asksToRollTheDices();
    }

    @Override
    public void asksToChooseStartingPosition() {
        view.asksToChooseStartingPosition();
    }

    @Override
    public void asksToInputCoordinates() {
        view.asksToInputCoordinates();
    }

    @Override
    public void asksToTurnTheHourglass() {
        view.asksToTurnTheHourglass();
    }

    @Override
    public void asksToMakeAChoice() {
        view.asksToMakeAChoice();
    }

    @Override
    public void asksPlanetChoice() {
        view.asksPlanetChoice();
    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {
        view.asksToManageGoods(goods);
    }

    @Override
    public void asksToUseEngines() {
        view.asksToUseEngines();
    }

    @Override
    public void asksToUseCannons() {
        view.asksToUseCannons();
    }

    @Override
    public void asksToUseBatteries() {
        view.asksToUseBatteries();
    }

    @Override
    public void asksToRemoveGoods() {
        view.asksToRemoveGoods();
    }

    @Override
    public void asksToRemoveCrew() {
        view.asksToRemoveCrew();
    }

    @Override
    public void showScores() {
        view.showScores();
    }

    @Override
    public void asksChosenMode() {
        view.asksChosenMode();
    }

    @Override
    public void updateLightModel(Message message) {
        view.updateLightModel(message);
    }
}
