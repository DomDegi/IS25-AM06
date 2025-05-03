package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.client.GamePhases;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class VirtualViewRMI extends UnicastRemoteObject implements VirtualView, Remote {
    //private final VirtualController server;
    private final ClientController clientController;
    private final DisplayableView view;

    public VirtualViewRMI(ClientController clientController, DisplayableView view)throws RemoteException{
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
    public void askColor()throws RemoteException {
        view.askColor();
    }

    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        view.showLoginResponse(success);
        if (success) {
            clientController.setState(ClientState.LOBBY);
        }
    }

    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        view.setClientState(newState);
        clientController.setState(newState);
    }

    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) throws RemoteException {
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
    public void showGenericMessage(String genericMessage)throws RemoteException {
        view.showGenericMessage(genericMessage);
    }

    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {view.showErrorMessage(errorMessage);}

    @Override
    public void showInGamePlayers(ArrayList<Player> players) throws RemoteException {view.showInGamePlayers(players);}

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) throws RemoteException {
        view.showPlayersBoard(player, shipBoard);
    }

    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        view.showDrawnTile(drawnTile);
        clientController.setState(ClientState.S_MANAGE_DRAWN_TILE);
        clientController.setTileInHand(drawnTile);
    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles)throws RemoteException {
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
        clientController.setState(ClientState.WAIT);
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
    public void wrongLocalInput()throws RemoteException {
        view.wrongLocalInput();
    }

    @Override
    public void showCard(Card card)throws RemoteException {
        view.showCard(card);
    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard)throws RemoteException {
        view.printFlightboard(lightFlightboard);
    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard)throws RemoteException {
        view.printShipboard(lightShipBoard);
    }

    @Override
    public void printProjectile(Projectile projectile)throws RemoteException {
        view.printProjectile(projectile);
    }

    @Override
    public void printCabins(Tile cabins) throws RemoteException{
        view.printCabins(cabins);
    }

    @Override
    public void connected() throws RemoteException{
        clientController.setConnected(true);
    }

    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException{
        view.setGameMode(gameMode);
        clientController.setGameMode(gameMode);
    }

    @Override
    public void ping() throws RemoteException {
        clientController.ping();
    }

    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) throws RemoteException {
        view.goodsPrinter(goodsArray);
    }

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return view;
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
    public void showBookedTiles(ArrayList<Tile> bookedTiles)throws RemoteException {
        view.showBookedTiles(bookedTiles);
    }

    @Override
    public void showInGameCards(ArrayList<Card> inGameCards)throws RemoteException {
        view.showInGameCards(inGameCards);
    }

    @Override
    public void showDiceRoll(int diceRoll)throws RemoteException {
        view.showDiceRoll(diceRoll);
    }

    @Override
    public void showWrongInputMessage() throws RemoteException {
        view.showWrongInputMessage();
        clientController.rollBackState();
    }

    @Override
    public void asksToRollTheDices() throws RemoteException {
        view.asksToRollTheDices();
    }

    @Override
    public void asksToChooseStartingPosition() throws RemoteException {
        view.asksToChooseStartingPosition();
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {
        clientController.setState(ClientState.COORD_REQUEST);
        clientController.getCoordInputManager().setCoordReqType(coordReqType);
        view.asksToInputCoordinates(coordReqType);
    }

    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates)throws RemoteException {
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
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        clientController.setDeck(cards);
    }

    @Override
    public void asksToTurnTheHourglass() throws RemoteException {
        view.asksToTurnTheHourglass();
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        try {
            view.showCard(clientController.getDisplayedCard());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException{
        view.notifyYourShipIsCorrect();
        clientController.setPhase(GamePhases.CARDS);
    }

    @Override
    public void asksToMakeAChoice() throws RemoteException {
        view.asksToMakeAChoice();
        setClientState(ClientState.ACTION);
    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) throws RemoteException {
        view.asksToManageGoods(goods);
        setClientState(ClientState.MANAGE_GOODS);
    }

    @Override
    public void showScores(ArrayList<Player> players) throws RemoteException {
        view.showScores(players);
    }

    @Override
    public void asksToRemoveGoods() throws RemoteException{
        view.asksToRemoveGoods();

    }// aggiorna: deve chiamare il nuovo rimuovitore di goods dal coordinate request handler

    @Override
    public void asksToRemoveCrew() throws RemoteException {
        view.asksToRemoveCrew();
    }// aggiorna: deve chiamare il nuovo rimuovitore di crew dal coordinate request handler

}
