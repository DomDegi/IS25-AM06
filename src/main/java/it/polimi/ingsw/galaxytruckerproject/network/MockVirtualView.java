package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class MockVirtualView implements VirtualView {

    @Override
    public void notifyNewTurnedTile(Tile tile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyRemoveTurnedTile(Tile tile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyPlayerMovement(String playerName, PlayersColor color, int playerPosition, int playerRanking) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyPositionedTile(String playerName, Tile tile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyBookedTile(String playerName, Tile tile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyRemovedBookedTile(String playerName, Tile tile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyNotAvailableCardDeck(ArrayList<Integer> lockedSmallDecks) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyNotAvailableColor(ArrayList<PlayersColor> color) throws RemoteException {

    }

    @Override
    public void notifyModifiedTiles(String playerName, ArrayList<Tile> tiles) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyGainedCredits(String playerName, int totalCredits) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyBrokenTile(String playerName, ArrayList<Coordinates> coordinates) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyChangesWhileGone(String currentGameStatus) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void initializeShipBoards(GameMode gameMode) throws RemoteException {

    }

    @Override
    public void victimOfThePenalty(String playerName) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void notifyPlayerJoined(int expected, int current, boolean reconnected) throws RemoteException {

    }

    // --- ViewInterface methods ---

    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        // Implement mock behavior or leave empty
        switch (newState) {
            case ACTION -> System.out.println("input yes or no\n");
            case DRAW_CARD ->  System.out.println("draw card\n");
            case MANAGE_GOODS -> System.out.println("manage goods\n");
            case MANAGE_CABINS -> System.out.println("choose aliens or humans\n");
            case ROLL_DICE -> System.out.println("roll dice\n");
            case COORD_REQUEST ->  System.out.println("coord request: ");
            case PLANET_CHOICE ->   System.out.println("planet choice\n");
            case COLOR_CHOICE ->    System.out.println("color choice\n");
            case S_MANAGE_DRAWN_TILE ->  System.out.println("s manage drawn tile\n");
            case S_END_DRAW_TILE_CARD -> System.out.println("s end draw tile card\n");
            case START_SHIP_CREATION ->  System.out.println("start ship creation\n");
            case WAIT -> System.out.println("wait other player action\n");
            case S_MANAGE_CARDS ->   System.out.println("manage cards\n");
            case S_FINISHED ->  System.out.println("finished ship\n");
        }
    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showWrongInputMessage() throws RemoteException {
        System.out.println("Wrong input");
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToRollTheDices() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showDiceRoll(int diceRoll) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToChooseStartingPosition() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {
        setClientState(ClientState.COORD_REQUEST);
        // Implement mock behavior or leave empty
        switch(coordReqType) {
            case CHOOSE_CREW -> System.out.println("choose crew to remove\n");
            case CHOOSE_DOUBLE_ENGINE -> System.out.println("choose double-engine\n");
            case CHOOSE_DOUBLE_CANNON ->  System.out.println("choose double-cannon\n");
            case REMOVE_GOODS ->  System.out.println("remove goods\n");
            case CHOOSE_TO_BREAK ->  System.out.println("choose tile to break\n");
            case CHOOSE_TO_MAINTAIN ->   System.out.println("choose branch to maintain\n");
            case CHOOSE_BATTERY -> System.out.println("choose battery to use\n");
        }
    }

    @Override
    public void notifyYouCanDrawThisCardDeck() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyYourShipIsCorrect() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToMakeAChoice() throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showScores(Map<String, Integer> scores) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void connected() {

    }

    @Override
    public void setGameMode(GameMode gameMode) {

    }

    @Override
    public DisplayableView getDisplayedView(){
        return null;
    }

    @Override
    public void notifyTurnedHourglass(int i) throws RemoteException {
    }

    @Override
    public void notifyEndOfTime() throws RemoteException {

    }

    @Override
    public void notifyEarlyLanding() throws RemoteException {

    }

    @Override
    public void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException {

    }

    @Override
    public void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException {

    }

    @Override
    public void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException {

    }

    @Override
    public void notifyPodium(ArrayList<Player> players) throws RemoteException {

    }
}

