package it.polimi.ingsw.galaxytruckerproject.network;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
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

import java.io.IOException;
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
    public void notifyPlayerMovement(String playerName, int playerPosition, int playerRanking) throws RemoteException {
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
    public void notifyChangesWhileGone(Map<String, LightShipBoard> updatedShipBoards, LightFlightboard updatedFlightBoard, Card drawnCard, int hourglassTurns, Map<Integer, Tile> turnedTiles, ArrayList<Integer> notAvailable) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void notifyFlightBoardCards(Map<Integer, ArrayList<Card>> cards) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    // --- ViewInterface methods ---

    @Override
    public void askNickname() throws IOException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void askColor() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showLoginResponse(boolean success) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void setClientState(ClientState newState) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showJoinableGamesList(Map<String, GameController> joinableGames) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void askPlayerCount() throws IOException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void createGame() throws IOException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void joinGame() throws IOException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showInGamePlayers(ArrayList<Player> players) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showWrongInputMessage() throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) {
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
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToTurnTheHourglass() {
        // Implement mock behavior or leave empty
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
    public void asksToManageGoods(ArrayList<Goods> goods) throws RemoteException {
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToRemoveGoods() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void asksToRemoveCrew() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showScores(ArrayList<Player> players) {
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
    public void wrongLocalInput() {
        // Implement mock behavior or leave empty
    }

    @Override
    public void showCard(Card card) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void printProjectile(Projectile projectile) {
        // Implement mock behavior or leave empty
    }

    @Override
    public void connected() {

    }

    @Override
    public void printCabins(Tile cabins) {

    }
}

