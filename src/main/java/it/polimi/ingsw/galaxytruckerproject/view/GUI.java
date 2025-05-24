package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.client.GamePhases;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.gui.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class GUI extends Application implements DisplayableView {

    private static String gameName;
    private static int numberOfPlayers;
    private static GameMode mode;
    private static String css;
    private static String css2;
    private static String css3;
    private static String css4;
    private static Stage primaryStage;
    private static BorderPane layout;
    private static ClientController controller;
    private static FXMLLoader loader;
    private static Button ship1;
    private static Button ship2;
    private static Button ship3;
    private int counter = 0;
    private Timer timer;

    public static void startGui(ClientController controller) {
        GUI.controller =controller;
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GUI.primaryStage = primaryStage;
        GUI.primaryStage.setTitle("Galaxy Trucker");
        css= Objects.requireNonNull(GUI.class.getResource("/gui/css/WelcomeStyle.css")).toExternalForm();
        css2= Objects.requireNonNull(GUI.class.getResource("/gui/css/LobbyStyle.css")).toExternalForm();
        css3= Objects.requireNonNull(GUI.class.getResource("/gui/css/ShipStyle.css")).toExternalForm();
        showMainView();
        showWelcome();
        GUI.primaryStage.setFullScreen(true);
    }

    private void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/mainView.fxml"));
        layout = loader.load();
        layout.getStylesheets().add(css);
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

    public static void setConnection(String connectionTipe,String ip) {
        if (connectionTipe.equals("r")) {
            try {
                controller.connectRMI(ip);
            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                controller.connectSocket(ip,12345);
            } catch (NotBoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setName(String name) {
        controller.getMe().setPlayerName(name);
        controller.doneNaming();
    }

    public static void createGame(){
        controller.createGame(gameName,numberOfPlayers,mode);
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
    public static void drawTile(){
        controller.drawTile();
    }

    public static void drawBookedTile(int index){
        controller.drawBooked(index);
    }

    public static void drawDrawnTile(int index){
        controller.drawDrawn(index);
    }

    public static void refuseTile(){
        controller.refuseTile();
    }

    public static void putTile(int x, int y){
        Coordinates coordinates = new Coordinates(x,y);
        controller.positionTile(coordinates);
    }

    public static void selectTile(int x, int y){
        Coordinates coordinates = new Coordinates(x,y);
        controller.checkCoord(coordinates);
    }

    public static void doneCoord(){
        controller.doneCoord();
    }

    public static void bookTile(){
        controller.bookTile();
    }

    public static void drawDeck(int index){
        controller.drawDeck(index);
    }

    public static void endShip(){
        controller.doneShipboard();
    }

    public static void turnHourglass(){
        switch (controller.getState()){
            case S_END_DRAW_TILE_CARD,S_MANAGE_DRAWN_TILE ->{
                controller.secondHourglassTurn();
            }
            case S_FINISHED -> {
                controller.thirdHourglassTurn();
            }
            case WAIT -> {
                if(controller.getPreviousState()==ClientState.S_FINISHED)
                    controller.thirdHourglassTurn();
            }
        }
    }

    public static void checkShip(int index){
        controller.check(index);
    }

    public static void rotate(){
        controller.rotateTile();
    }

    public static void doneChecking(){
        controller.stopLookingAtCards();
    }

    public static void position(int index){
        controller.positionOnFlightBoard(index);
    }

    public static void drawCard() {
        controller.drawCard();
    }

    public static ArrayList<Card> displayableCards(){
         return  controller.getDisplayedCard();
    }

//showMethods---------------------------------------------------------------------------------------------------------------
    public static void clear(){
        showMessage("");
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
            if(controller.getPhase()== GamePhases.LOGIN){
                ProgressIndicator circle = new ProgressIndicator();
                circle.setPrefSize(100, 100);
                circle.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                layout.setCenter(circle);
            }else{

                    ProgressIndicator circle = new ProgressIndicator();
                    circle.setPrefSize(20, 20);
                    circle.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                    layout.setBottom(circle);

            }
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

    public static void showS_EndDrawTilesCards() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/s_EndDrawTilesCards.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showS_ManageDrawTilesCards() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/s_ManageDrawnTile.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showDeckCheck() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/deckCheck.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    public static void showEndShip() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/endShipBoard.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    private static void showDrawCard() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/drawCard.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    private static void showCoordRequest() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/cordShipRequest.fxml"));
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
            Label stringLabel = new Label(message);
            AnchorPane anchorPane = new AnchorPane();
            stringLabel.setAlignment(Pos.CENTER);
            stringLabel.setPrefHeight(50);
            stringLabel.setStyle("fx-font-size: 16px;");
            anchorPane.getChildren().add(stringLabel);
            anchorPane.setPrefHeight(50);
            anchorPane.setPrefWidth(300);
            AnchorPane.setBottomAnchor(stringLabel, 20.0);
            AnchorPane.setLeftAnchor(stringLabel, 0.0);
            AnchorPane.setRightAnchor(stringLabel, 0.0);
            layout.setBottom(anchorPane);
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

    public static void addCheckShip(){
        Platform.runLater(() -> {
            VBox top=new VBox();
            VBox right=new VBox();
            VBox left=new VBox();
            top.setAlignment(Pos.CENTER);
            right.setAlignment(Pos.CENTER);
            left.setAlignment(Pos.CENTER);
            if (controller.getFlightBoard().getInGamePlayers().size() == 2) {
                ship1 = new Button();
                ship1.setOnAction(event -> {
                    checkShip(1);
                });
                ship1.setPrefHeight(52);
                ship1.setPrefWidth(614);
                ship1.setAlignment(Pos.CENTER);
                top.getChildren().add(ship1);
                layout.setTop(top);
            }
            if (controller.getFlightBoard().getInGamePlayers().size() == 3) {
                ship1 = new Button();
                ship2 = new Button();
                ship1.setOnAction(event -> {
                    checkShip(1);
                });
                ship2.setOnAction(event -> {
                    checkShip(2);
                });
                ship1.setPrefWidth(52);
                ship1.setPrefHeight(614);
                ship2.setPrefWidth(52);
                ship2.setPrefHeight(614);
                ship1.setAlignment(Pos.CENTER);
                ship2.setAlignment(Pos.CENTER);
                left.getChildren().add(ship1);
                right.getChildren().add(ship2);
                layout.setLeft(left);
                layout.setRight(right);
            }
            if (controller.getFlightBoard().getInGamePlayers().size() == 4) {
                ship1 = new Button();
                ship2 = new Button();
                ship3 = new Button();
                ship1.setOnAction(event -> {
                    checkShip(1);
                });
                ship2.setOnAction(event -> {
                    checkShip(2);
                });
                ship3.setOnAction(event -> {
                    checkShip(3);
                });
                ship1.setPrefWidth(52);
                ship1.setPrefHeight(614);
                ship2.setPrefHeight(52);
                ship2.setPrefWidth(614);
                ship3.setPrefWidth(52);
                ship3.setPrefHeight(614);
                ship1.setAlignment(Pos.CENTER);
                ship2.setAlignment(Pos.CENTER);
                ship3.setAlignment(Pos.CENTER);
                top.getChildren().add(ship2);
                left.getChildren().add(ship1);
                right.getChildren().add(ship3);
                layout.setLeft(left);
                layout.setTop(top);
                layout.setRight(right);
            }
        });
    }
//Overrides--------------------------------------------------------------------------------------------------------------
    public static void displayClientState(ClientState newState) {
        clear();
        switch (newState){
            case CHOOSE_CONNECTION_TYPE ->{
                showConnection();
                layout.getStylesheets().add(css2);
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
            case COLOR_CHOICE,COLOR_CHOICE0 ->{
                showChooseColor();
            }
            case START_SHIP_CREATION -> {
                showStart();
            }
            case S_END_DRAW_TILE_CARD ->{
                showS_EndDrawTilesCards();
                layout.getStylesheets().add(css3);
                addCheckShip();
            }
            case S_MANAGE_DRAWN_TILE ->{
                showS_ManageDrawTilesCards();
            }
            case S_MANAGE_CARDS -> {
                showDeckCheck();
            }
            case S_FINISHED -> {
                showEndShip();
            }
            case DRAW_CARD -> {
                showDrawCard();
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
                showCoordRequest();
            }
            case ROLL_DICE -> {

            }
            case WAIT -> {
                showWait();
            }
        }
    }

    @Override
    public void setClientState(ClientState newState){
        displayClientState(newState);
    }

    @Override
    public void sendingCoordinates() {

    }

    @Override
    public void showTurnedTiles(Map<Integer, Tile> turnedTiles) {
        Platform.runLater(() -> {
            try {
                if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                    S_EndDrawTilesCardsController sceneController = loader.getController();
                    if (sceneController != null)
                        sceneController.update();
                } else if (controller.getState() == ClientState.S_MANAGE_DRAWN_TILE) {
                    S_ManageDrawTileController sceneController = loader.getController();
                    if (sceneController != null)
                        sceneController.update();
                }
            } catch (Exception e) {
                return;
            }
        });
    }

    @Override
    public void cargoSelected(Coordinates coordinates) {

    }

    @Override
    public void wrongLocalInput() {
        showMessage("Wrong input!");
    }

    @Override
    public void coordinateSelected() {

    }

    @Override
    public void showCard(ArrayList<Card> cards) {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/showCard.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });

    }

    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {
        Platform.runLater(() -> {
        if(controller.getState()==ClientState.S_FINISHED||(controller.getPreviousState()==ClientState.S_FINISHED&&controller.getState()==ClientState.WAIT)) {
            EndShipController endShipController=loader.getController();
                endShipController.update();
            }
        });
    }

    @Override
    public void checkShipboard(LightShipBoard lightShipBoard) {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/checkShip.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CheckShipController checkShipController=loader.getController();
            checkShipController.setPlayer(lightShipBoard);
            layout.setCenter(newLayer);
        });
    }

    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {

    }

    @Override
    public void showAvailableDecks(){
        Platform.runLater(() -> {
            if(controller.getState()==ClientState.S_END_DRAW_TILE_CARD){
                S_EndDrawTilesCardsController sEndDrawTilesCardsController=loader.getController();
                sEndDrawTilesCardsController.update();
            }
        });
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
        showMessage(genericMessage);
    }

    @Override
    public void victimOfThePenalty(String playerName, Penalty penalty) {

    }

    @Override
    public void notifyPlayerJoined(int expectedPlayer, int currentPlayer, boolean reconnected) {

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
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/showCard.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
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
        timer= new Timer();
        counter=0;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                counter++;
                Platform.runLater(() -> {
                    if(controller.isChecking()==null) {
                        if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                            S_EndDrawTilesCardsController sceneController = loader.getController();
                            if (sceneController != null) {
                                sceneController.goProgressBar(counter, 100, controller.getHourglassTurns());
                            }
                        } else if(controller.getState()==ClientState.S_FINISHED||(controller.getPreviousState()==ClientState.S_FINISHED&&controller.getState()==ClientState.WAIT&&controller.isChecking()==null)) {
                            EndShipController sceneController = loader.getController();
                            if (sceneController != null) {
                                sceneController.goProgressBar(counter, 100, controller.getHourglassTurns());
                            }
                        }
                    }
                });
                if (counter == 100) {
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    @Override
    public void notifyEndOfTime() throws RemoteException {
        counter=100;
        Platform.runLater(() -> {
            if(controller.isChecking()==null) {
                if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                    S_EndDrawTilesCardsController sceneController = loader.getController();
                    if (sceneController != null) {
                        sceneController.goProgressBar(counter, 100, controller.getHourglassTurns());
                    }
                } else if(controller.getState()==ClientState.S_FINISHED||(controller.getPreviousState()==ClientState.S_FINISHED&&controller.getState()==ClientState.WAIT)) {
                    EndShipController sceneController = loader.getController();
                    if (sceneController != null) {
                        sceneController.goProgressBar(counter, 100, controller.getHourglassTurns());
                    }
                }
            }
        });
        timer.cancel();
        timer.purge();
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

    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return null;
    }

    public static ClientController getController() {
        return controller;
    }

    public static void setGameName(String gameName) {
        GUI.gameName = gameName;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        GUI.numberOfPlayers = numberOfPlayers;
    }

    public static void setMode(GameMode mode) {
        GUI.mode = mode;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static String getGameName() {
        return gameName;
    }

    public static GameMode getMode() {
        return mode;
    }
}

