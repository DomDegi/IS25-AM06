package it.polimi.ingsw.galaxytruckerproject.network.RMI.Client;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

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
    public void showLoginResponse(boolean success) throws RemoteException {
        view.showLoginResponse(success);
    }

    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        clientController.setState(newState);
    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        view.showJoinableGamesList(joinableGames);
        clientController.setGameInfo(joinableGames);
    }

    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {view.showErrorMessage(errorMessage);}


    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        view.showDrawnTile(drawnTile);
        clientController.setTileInHand(drawnTile);
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
    public void notifyNotAvailableColor(PlayersColor color) throws RemoteException {
        clientController.colorsNotAvailable(color);
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
    public void connected() throws RemoteException{
        view.connected();
        clientController.setConnected(true);
    }

    @Override
    public void initializeShipBoards(GameMode gameMode) throws RemoteException{
        clientController.setGameMode(gameMode);
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
    public void notifyTurnedHourglass(int i) throws RemoteException {
        view.notifyTurnedHourglass(i);
        clientController.turnHourglass(i);
    }

    @Override
    public void notifyEndOfTime() throws RemoteException {
        view.notifyEndOfTime();
    }

    @Override
    public void notifyEarlyLanding() throws RemoteException {
        view.notifyEarlyLanding();
        clientController.setState(ClientState.WAIT);
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
    public void notifyPlayerMovement(String playerName, PlayersColor color, int playerPosition, int playerRanking) throws RemoteException {
        clientController.updateFlightboard(playerName, color,playerPosition, playerRanking);
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
    public void notifyChangesWhileGone(String currentGameStatus) throws RemoteException {
        clientController.updateModel(currentGameStatus);
    }

    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        clientController.setDeck(cards);
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {
        view.showCard(clientController.getDisplayedCard());
    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException{
        view.notifyYourShipIsCorrect();
        //clientController.setPhase(GamePhases.CARDS);
    }

    @Override
    public void asksToMakeAChoice() throws RemoteException {
        view.asksToMakeAChoice();
        setClientState(ClientState.ACTION);
    }

    @Override
    public void showScores(Map<String, Integer> scores) throws RemoteException {
        view.showScores(scores);
    }
}
