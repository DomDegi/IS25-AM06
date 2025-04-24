package it.polimi.ingsw.galaxytruckerproject.view;

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
import java.util.Set;

public class GUI implements ViewInterface {

    @Override
    public void askNickname() throws IOException {

    }

    @Override
    public void askColor() {

    }

    @Override
    public void showLoginResponse(boolean success) {

    }

    @Override
    public void setClientState(ClientState newState) {

    }

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

    public void asksPlayersInfo() {

    }

    public void showShipsErrors() {

    }

    @Override
    public void showPlayersBoard(String player, ShipBoard shipBoard) {

    }

    @Override
    public void showDrawnTile(Tile drawnTile) {

    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {

    }

    public void showTurnedTiles(ArrayList<Tile> turnedTiles) {

    }

    @Override
    public void showBookedTiles(ArrayList<Tile> bookedTiles) {

    }

    @Override
    public void showWrongInputMessage() {

    }

    @Override
    public void showInGameCards(ArrayList<Card> inGameCards) {

    }

    @Override
    public void asksToRollTheDices() {

    }

    @Override
    public void showDiceRoll(int diceRoll) {

    }

    @Override
    public void asksToChooseStartingPosition() {

    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {

    }

    public void asksToInputCoordinates() {

    }

    @Override
    public void asksToTurnTheHourglass() {

    }

    @Override
    public void notifyYouCanDrawThisCardDeck() {

    }

    @Override
    public void notifyYourShipIsCorrect() {

    }

    public void asksToSetPosition() {

    }

    @Override
    public void asksToMakeAChoice() {

    }

    public void asksPlanetChoice() {

    }

    @Override
    public void asksToManageGoods(ArrayList<Goods> goods) {

    }

    public void asksToUseEngines() {

    }

    public void asksToUseCannons() {

    }

    public void asksToUseBatteries() {

    }

    @Override
    public void asksToRemoveGoods() {

    }

    @Override
    public void asksToRemoveCrew() {

    }

    public void asksWhichBranchToKeep(ArrayList<Set<Coordinates>> branch) {

    }

    @Override
    public void showScores(ArrayList<Player> players) {

    }

    public void showScores() {
    }

    public void asksChosenMode() {

    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {

    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {

    }

    @Override
    public void wrongLocalInput() {

    }

    @Override
    public void showCard(Card card) {

    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {

    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {

    }

    @Override
    public void printProjectile(Projectile projectile) {

    }

}
