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
    private final VirtualController server;
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

    //DONE
    @Override
    public void askNickname() throws IOException {
        view.askNickname();
    }

    //DONE
    @Override
    public void askColor() {
        view.askColor();
    }

    //DONE
    @Override
    public void showLoginResponse(boolean success) {
       view.showLoginResponse(success);
       if (success) {
           clientController.setState(ClientState.LOBBY);
       }
    }

    @Override
    //A CHE SERVE?
    public void asksJoinOrCreate() {
        view.asksJoinOrCreate();
    }

    //DONE
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
    public void showErrorMessage(String errorMessage) {
        view.showErrorMessage(errorMessage);
    }

    //DONE
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

    //DONE
    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {
        view.showPlayersBoard(player, shipBoard);
    }

    //DONE
    @Override
    public void showDrawnTile(Tile drawnTile) {
        view.showDrawnTile(drawnTile);
    }

    @Override
    public void showTurnedTiles(ArrayList<Tile> turnedTiles) {
        view.showTurnedTiles(turnedTiles);
    }

    //DONE
    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) {
        view.showBookedTiles(bookedTiles);
    }

    //DONE
    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) {
        view.showInGameCards(inGameCards);
    }

    //DONE
    @Override
    public void asksToRollTheDices() {
        view.asksToRollTheDices();
    }

    //DONE
    @Override
    public void asksToChooseStartingPosition() {
        view.asksToChooseStartingPosition();
    }

    //DONE
    @Override
    public void asksToInputCoordinates() {
        view.asksToInputCoordinates();
    }

    //DONE
    @Override
    public void asksToTurnTheHourglass() {
        view.asksToTurnTheHourglass();
    }

    //D0NE
    @Override
    public void asksToMakeAChoice() {
        view.asksToMakeAChoice();
    }

    //DONE
    @Override
    public void asksPlanetChoice() {
        view.asksPlanetChoice();
    }


    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {
        view.asksToManageGoods(goods);
    }

    //DONE
    @Override
    public void asksToUseEngines() {
        view.asksToUseEngines();
    }

    //DONE
    @Override
    public void asksToUseCannons() {
        view.asksToUseCannons();
    }

    //DONE
    @Override
    public void asksToUseBatteries() {
        view.asksToUseBatteries();
    }

    //DONE
    @Override
    public void asksToRemoveGoods() {
        view.asksToRemoveGoods();
    }

    //DONE
    @Override
    public void asksToRemoveCrew() {
        view.asksToRemoveCrew();
    }

    @Override
    public void showScores() {
        view.showScores();
    }

    //DONE
    @Override
    public void asksChosenMode() {
        view.asksChosenMode();
    }

    //DONE
    @Override
    public void updateLightModel(Message message) {
        view.updateLightModel(message);
    }
}
