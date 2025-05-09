package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GUI implements DisplayableView {

    @Override
    public void showLoginResponse(boolean success) {

    }

    @Override
    public void setClientState(ClientState newState) {

    }

    public void asksJoinOrCreate() {

    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {

    }

    @Override
    public void showGenericMessage(String genericMessage) throws RemoteException {

    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }
    public void asksPlayersInfo() {

    }

    public void showShipsErrors() {

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
    public void showWrongInputMessage() {

    }

    @Override
    public void asksToRollTheDices() throws RemoteException {

    }

    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {

    }

    @Override
    public void asksToChooseStartingPosition() throws RemoteException {

    }

    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) {

    }

    public void asksToInputCoordinates() {

    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {

    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {

    }

    public void asksToSetPosition() {

    }

    @Override
    public void asksToMakeAChoice() {

    }

    public void asksPlanetChoice() {

    }

    public void asksToUseEngines() {

    }

    public void asksToUseCannons() {

    }

    public void asksToUseBatteries() {

    }

    public void asksWhichBranchToKeep(ArrayList<Set<Coordinates>> branch) {

    }

    @Override
    public void showScores(ArrayList<Player> players) throws RemoteException {

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
    public void showCard(ArrayList<Card> cards) {

    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {

    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard){

    }

    @Override
    public void printBooked(LightShipBoard lightShipBoard) {

    }

    @Override
    public void printProjectile(Projectile projectile) {

    }

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return new GUI();
    }

    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) {

    }

    @Override
    public void notifyTurnedHourglass(int i) throws RemoteException {

    }

    @Override
    public void notifyEndOfTime() throws RemoteException {

    }

    @Override
    public void printCabins(Tile cabins) {

    }

    @Override
    public void connected() {

    }

    @Override
    public void setGameMode(GameMode gameMode) {

    }

    public void start() {
        try{
            Stage stage = new Stage();
            stage.setTitle("Galaxy Trucker");
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root,400,300);
            scene.setFill(Color.AQUAMARINE);
            stage.setScene(scene);
            stage.show();

            stage.setMaxHeight(1000);
            stage.setMinHeight(200);
            stage.setMaxWidth(600);
            stage.setMinWidth(200);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
