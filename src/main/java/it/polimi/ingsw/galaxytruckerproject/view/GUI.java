package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.gui.LobbyController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class GUI extends Application implements DisplayableView {
    private static Stage primaryStage;
    private static BorderPane layout;
    private static ClientController controller;
    private static FXMLLoader loader;

    public static void startGui(ClientController controller) {
        GUI.controller =controller;
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GUI.primaryStage = primaryStage;
        GUI.primaryStage.setTitle("Galaxy Trucker");
        showMainView();
        showWelcome();
    }

    private void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/mainView.fxml"));
        layout = loader.load();
        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }

    public static void showWelcome() throws IOException {
        loader = new FXMLLoader(GUI.class.getResource("/gui/welcome.fxml"));
        BorderPane newLayer=loader.load();
        layout.setCenter(newLayer);
    }

//GUICommands----------------------------------
    public static void startNewGame() {
        controller.setState(ClientState.CHOOSE_CONNECTION_TYPE);
    }

    public static void setConnection(String connectionTipe) {
        if (connectionTipe.equals("r")) {
            try {
                controller.connectRMI();
            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                controller.connectSocket("localhost",12345);
            } catch (NotBoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setName(String name) {
        controller.getMe().setPlayerName(name);
        controller.doneNaming();
    }

    public static void createGame(String gameName, int  playerNum, GameMode gameMode){
        controller.createGame(gameName,playerNum,gameMode);
    }

    public static void join(String gameName){
        controller.joinGame(gameName);
    }

    public static void setColor(PlayersColor color){
        controller.colorChoice(color);
    }
    public static void start(){
        controller.firstHourglassTurn();
    }
//showMethods---------------------------------------------------------------------------------------------------------------
    private static void clear(){
        showMessage("");
        layout.setLeft(null);
        layout.setRight(null);
        layout.setTop(null);
    }

    private static void showConnection() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/connection.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showLogin() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/login.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showLobby() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/lobby.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showCreateNewGame(){
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/createNewGame.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showChooseColor(){
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/chooseColor.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showWait(){
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/wait.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    private static void showStart() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/start.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    private static void showS_EndDrawTilesCards() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/sEndDrawTilesCards.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showMessage(String message) {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/message.fxml"));
            AnchorPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Label stringLabel = new Label(message);
            AnchorPane.setBottomAnchor(stringLabel, 20.0);
            AnchorPane.setLeftAnchor(stringLabel, 0.0);
            AnchorPane.setRightAnchor(stringLabel, 0.0);
            stringLabel.setAlignment(Pos.CENTER);
            try {
                newLayer.getChildren().add(1, stringLabel);
            } catch (IndexOutOfBoundsException e) {
                newLayer.getChildren().add(stringLabel);
            }
            layout.setBottom(newLayer);
        });
    }

    public static void addJoinableGame(ArrayList<GameInfo> joinableGames) {
        if (joinableGames == null) {
            return;
        }
        if(controller.getState()==ClientState.LOBBY) {
            ArrayList<String> games = new ArrayList<>();
            for (GameInfo gameInfo : joinableGames) {
                String gameName = gameInfo.getGameName();
                String game = gameName + "\t" + gameInfo.getCurrentPlayerCount() + "/" + gameInfo.getMaxPlayerCount() + "\t" + gameInfo.getGameMode();
                games.add(game);
            }
            LobbyController lobbyController = loader.getController();
            if (lobbyController != null)
                lobbyController.update(games);
        }
    }
//Overrides--------------------------------------------------------------------------------------------------------------

    @Override
    public void setClientState(ClientState newState){
        clear();
        switch (newState){
            case CHOOSE_CONNECTION_TYPE ->{
                showConnection();
            }
            case LOGIN ->{
                showLogin();
            }
            case LOBBY ->{
                showLobby();
            }
            case LOBBY0 -> {
                showCreateNewGame();
            }
            case COLOR_CHOICE ->{
                showChooseColor();
            }
            case START_SHIP_CREATION -> {
                showStart();
            }
            case S_END_DRAW_TILE_CARD ->{
                showS_EndDrawTilesCards();
            }
            case S_MANAGE_DRAWN_TILE ->{

            }
            case S_MANAGE_CARDS -> {

            }
            case S_FINISHED -> {

            }
            case DRAW_CARD -> {

            }
            case ACTION -> {

            }
            case PLANET_CHOICE -> {

            }
            case MANAGE_GOODS -> {

            }
            case MANAGE_CABINS ->{

            }
            case COORD_REQUEST -> {

            }
            case ROLL_DICE -> {

            }
            case WAIT -> {
                showWait();
            }
        }
    }

    @Override
    public void sendingCoordinates() {

    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {

    }

    @Override
    public void wrongLocalInput() {
        showMessage("Wrong input!");
    }

    @Override
    public void showCard(ArrayList<Card> cards) {

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

    @Override
    public void printCabins(Tile cabins) {

    }

    @Override
    public void printDrawnTiles(Map<Integer, Tile> drawnTiles) {

    }

    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) {

    }

    @Override
    public void printBooked(LightShipBoard shipBoard) {

    }

    @Override
    public void setClientController(ClientController clientController) {

    }

    @Override
    public void crewPositioned() {

    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void showLoginResponse(boolean success) throws RemoteException {

    }

    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        addJoinableGame(joinableGames);
    }

    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {
        showMessage(errorMessage);
    }

    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {

    }

    @Override
    public void showWrongInputMessage() throws RemoteException {
        showMessage("wrong input!");
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
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {

    }

    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {

    }

    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {

    }

    @Override
    public void asksToMakeAChoice() throws RemoteException {

    }

    @Override
    public void showScores(Map<String, Integer> scores) throws RemoteException {

    }

    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {

    }

    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {

    }

    @Override
    public void connected() throws RemoteException {

    }

    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException {

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
    public DisplayableView getDisplayedView() throws RemoteException {
        return null;
    }

    public static ClientController getController() {
        return controller;
    }
}

