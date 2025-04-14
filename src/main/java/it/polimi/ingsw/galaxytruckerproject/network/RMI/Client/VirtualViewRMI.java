package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
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
    private final ClientController clientController;
    private final ViewInterface view;

    public VirtualViewRMI(ClientController clientController, ViewInterface view) throws RemoteException {
        super();
        this.clientController = clientController;
        this.view = view;
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
        view.askNickname();
    }

    @Override
    public void askColor() {

    }

    @Override
    public void showLoginResponse(boolean success) {
       view.showLoginResponse(success);
       if (success) {
           clientController.setState(ClientState.LOBBY);
       }
    }

    @Override
    public void asksJoinOrCreate() {

    }

    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) {

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
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }

    @Override
    public void showInGamePlayers(ArrayList<Player> players) {

    }

    @Override
    public void asksPlayersInfo() {

    }

    @Override
    public void showShipsErrors() {

    }

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {

    }

    @Override
    public void showDrawnTile(Tile drawnTile) {

    }

    @Override
    public void showTurnedTiles(ArrayList<Tile> turnedTiles) {

    }

    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) {

    }

    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) {

    }

    @Override
    public void asksToRollTheDices() {

    }

    @Override
    public void asksToChooseStartingPosition() {

    }

    @Override
    public void asksToInputCoordinates() {

    }

    @Override
    public void asksToTurnTheHourglass() {

    }

    @Override
    public void asksToMakeAChoice() {

    }

    @Override
    public void asksPlanetChoice() {

    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {

    }

    @Override
    public void asksToUseEngines() {

    }

    @Override
    public void asksToUseCannons() {

    }

    @Override
    public void asksToUseBatteries() {

    }

    @Override
    public void asksToRemoveGoods() {

    }

    @Override
    public void asksToRemoveCrew() {

    }

    @Override
    public void showScores() {

    }

    @Override
    public void asksChosenMode() {

    }

    @Override
    public void updateLightModel(Message message) {

    }
}
