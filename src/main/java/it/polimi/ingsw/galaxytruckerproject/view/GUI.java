package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
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
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {

    }

    @Override
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

    @Override
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

    @Override
    public void asksToSetPosition() {

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
    public void asksWhichBranchToKeep(ArrayList<Set<Coordinates>> branch) {

    }

    @Override
    public void showScores(ArrayList<Player> players) {

    }

    @Override
    public void showScores() {

    }

    @Override
    public void asksChosenMode() {

    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {

    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {

    }

}
