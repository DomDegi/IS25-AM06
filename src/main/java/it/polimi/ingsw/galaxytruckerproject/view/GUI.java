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
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Timer;

/**
 * Main class for the GUI of the Galaxy Trucker game.
 * <p>
 * This class initializes and manages the game's graphical user interface (GUI), providing the main entry point
 * for launching the game. It handles scene transitions, client-server communication, and user interaction for
 * different game phases. The GUI class is responsible for showing various game states, such as the lobby, login,
 * game board, and others.
 * </p>
 */
public class GUI extends Application implements DisplayableView {

    /**
     * The name of the current game.
     */
    private static String gameName;

    /**
     * The number of players participating in the current game.
     */
    private static int numberOfPlayers;

    /**
     * The game mode for the current session, represented by the {@link GameMode} enum.
     */
    private static GameMode mode;

    /**
     * The CSS stylesheet used for the welcome screen styling.
     */
    private static String css1;

    /**
     * The CSS stylesheet used for the lobby screen styling.
     */
    private static String css2;

    /**
     * The CSS stylesheet used for the ship creation and management screens styling.
     */
    private static String css3;
    private static Stage primaryStage;

    /**
     * The main layout of the application, typically used to manage screen transitions and positioning of elements.
     */
    private static BorderPane layout;

    /**
     * The client controller that handles the logic and communication with the backend during the game.
     */
    private static ClientController controller;

    /**
     * The FXMLLoader used to load the different FXML screens for the application.
     */
    private static FXMLLoader loader;

    /**
     * A button representing the first ship in the game.
     */
    private static Button ship1;

    /**
     * A button representing the second ship in the game.
     */
    private static Button ship2;

    /**
     * A button representing the third ship in the game.
     */
    private static Button ship3;

    /**
     * A counter variable used for various timed events or actions during the game.
     */
    private int counter = 0;

    /**
     * A Timer instance used to manage time-dependent events in the game, such as hourglasses or delays.
     */
    private Timer timer;
    private static Button quit;
    private static Button fullScreen;

    /**
     * The percentage value representing the progress of the first hourglass or a timed event.
     */
    private static double percentage1 = 0;

    /**
     * The percentage value representing the progress of the second hourglass or a timed event.
     */
    private static double percentage2 = 0;

    /**
     * The percentage value representing the progress of the third hourglass or a timed event.
     */
    private static double percentage3 = 0;

    /**
     * Launches the GUI for the game.
     *
     * @param controller the client controller responsible for handling game logic
     */
    public static void startGui(ClientController controller) {
        GUI.controller = controller;
        launch();
    }

    /**
     * Initializes the primary stage (window) for the GUI, sets up the window properties,
     * and loads the necessary CSS stylesheets for the different scenes.
     * It then calls the method to display the main view and the welcome screen.
     *
     * @param primaryStage The main stage for the application, passed by the JavaFX runtime.
     * @throws IOException if the FXML files or CSS resources cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        GUI.primaryStage = primaryStage;
        GUI.primaryStage.setTitle("Galaxy Trucker");
        css1= Objects.requireNonNull(GUI.class.getResource("/gui/css/WelcomeStyle.css")).toExternalForm();
        css2= Objects.requireNonNull(GUI.class.getResource("/gui/css/LobbyStyle.css")).toExternalForm();
        css3= Objects.requireNonNull(GUI.class.getResource("/gui/css/ShipStyle.css")).toExternalForm();
        showMainView();
        showWelcome();
        GUI.primaryStage.setFullScreen(true);
    }

    /**
     * Loads the main view of the application by loading the corresponding FXML file and applying the stylesheets.
     * The main view is displayed in the primary stage (window).
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/mainView.fxml"));
        layout = loader.load();
        layout.getStylesheets().add(css1);
        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }

    /**
     * Displays the welcome screen of the game by loading the corresponding FXML file.
     * The welcome screen is set as the center content of the layout.
     *
     * @throws IOException if the FXML file for the welcome screen cannot be loaded.
     */
    public static void showWelcome() throws IOException {
        loader = new FXMLLoader(GUI.class.getResource("/gui/welcome.fxml"));
        BorderPane newLayer = loader.load();
        layout.setCenter(newLayer);
    }


    //GUICommands----------------------------------

    /**
     * Starts a new game by setting the client's state to {@link ClientState#CHOOSE_CONNECTION_TYPE}.
     * This method is typically called when the user selects to start a new game.
     */
    public static void startNewGame() {
        controller.setState(ClientState.CHOOSE_CONNECTION_TYPE);
    }

    /**
     * Sets up the connection to the server using either RMI or a socket.
     *
     * @param connectionType The type of connection to use ("r" for RMI, other values for socket).
     * @param ip             The IP address of the server.
     * @throws RuntimeException if there is an error in establishing the connection.
     */
    public static void setConnection(String connectionType,String ip, int port) {
        if (connectionType.equals("r")) {
            try {
                if(port==0)
                    port=1099;
                controller.connectRMI(ip,port);
            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                if(port==0)
                    port=12345;
                controller.connectSocket(ip,port);
            } catch (NotBoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sets the player's name.
     *
     * @param name The name of the player.
     */
    public static void setName(String name) {
        controller.getMe().setPlayerName(name);
        controller.doneNaming();
    }

    /**
     * Creates a new game with the specified game name, number of players, and game mode.
     */
    public static void createGame() {
        controller.createGame(gameName, numberOfPlayers, mode);
    }

    /**
     * Joins an existing game with the given game name.
     *
     * @param gameName The name of the game to join.
     */
    public static void join(String gameName) {
        controller.joinGame(gameName);
    }

    /**
     * Sets the player's color by choosing one from the available colors.
     *
     * @param color The color to assign to the player.
     */
    public static void setColor(PlayersColor color) {
        controller.colorChoice(color);
    }

    /**
     * Starts the first hourglass turn, marking the beginning of a new game phase.
     */
    public static void start() {
        controller.firstHourglassTurn();
    }

    /**
     * Draws a tile for the player.
     */
    public static void drawTile() {
        controller.drawTile();
    }

    /**
     * Draws a booked tile for the player from a previously reserved tile.
     *
     * @param index The index of the booked tile to draw.
     */
    public static void drawBookedTile(int index) {
        controller.drawBooked(index);
    }

    /**
     * Draws a drawn tile for the player.
     *
     * @param index The index of the drawn tile to draw.
     */
    public static void drawDrawnTile(int index) {
        controller.drawDrawn(index);
    }

    /**
     * Refuses the drawn tile, preventing it from being used by the player.
     */
    public static void refuseTile() {
        controller.refuseTile();
    }

    /**
     * Places a tile at the specified coordinates on the game board.
     *
     * @param x The x-coordinate of the tile to place.
     * @param y The y-coordinate of the tile to place.
     */
    public static void putTile(int x, int y) {
        Coordinates coordinates = new Coordinates(x, y);
        controller.positionTile(coordinates);
    }

    /**
     * Selects a tile at the specified coordinates for further action.
     *
     * @param x The x-coordinate of the tile to select.
     * @param y The y-coordinate of the tile to select.
     */
    public static void selectTile(int x, int y) {
        Coordinates coordinates = new Coordinates(x, y);
        controller.checkCoord(coordinates);
    }

    /**
     * Marks the completion of a coordinate selection process.
     */
    public static void doneCoord() {
        controller.doneCoord();
    }

    /**
     * Books a tile for later use.
     */
    public static void bookTile() {
        controller.bookTile();
    }

    /**
     * Draws a tile from a specific deck, based on the index.
     *
     * @param index The index of the deck to draw from.
     */
    public static void drawDeck(int index) {
        controller.drawDeck(index);
    }

    /**
     * Marks the completion of the ship creation phase.
     */
    public static void endShip() {
        controller.doneShipboard();
    }

    /**
     * Handles the turn of the hourglass based on the current game state.
     * This method advances the hourglass to the second or third turn depending on the state.
     * <p>
     * If the game is in the {@link ClientState#S_END_DRAW_TILE_CARD} or {@link ClientState#S_MANAGE_DRAWN_TILE} state,
     * the second hourglass turn is triggered. If the game is in the {@link ClientState#S_FINISHED} state, the third hourglass turn is triggered.
     * If the game is in the {@link ClientState#WAIT} state and the previous state was {@link ClientState#S_FINISHED},
     * the third hourglass turn is triggered as well.
     * </p>
     */
    public static void turnHourglass(){
        switch (controller.getState()){
            case S_END_DRAW_TILE_CARD,S_MANAGE_DRAWN_TILE -> controller.secondHourglassTurn();
            case S_FINISHED -> controller.thirdHourglassTurn();
            case WAIT -> {
                if (controller.getPreviousState() == ClientState.S_FINISHED)
                    controller.thirdHourglassTurn();
            }
        }
    }

    /**
     * Checks the ship at a specific index for validation or display purposes.
     *
     * @param index The index of the ship to check.
     */
    public static void checkShip(int index) {
        controller.check(index);
    }

    /**
     * Rotates the selected tile.
     */
    public static void rotate() {
        controller.rotateTile();
    }

    /**
     * Ends the process of checking the cards and notifies the controller to stop looking at the cards.
     */
    public static void doneChecking() {
        controller.stopLookingAtCards();
    }

    /**
     * Positions the player on the flight board at the specified index.
     *
     * @param index The index representing the player's position on the flight board.
     */
    public static void position(int index) {
        controller.positionOnFlightBoard(index);
    }

    /**
     * Draws a card for the player.
     */
    public static void drawCard() {
        controller.drawCard();
    }

    /**
     * Returns the list of cards that are currently displayed for the player.
     *
     * @return The list of cards that are currently displayed.
     */
    public static ArrayList<Card> displayableCards() {
        return controller.getDisplayedCard();
    }

    /**
     * Manages the cabins for the player by assigning a specific crew type.
     *
     * @param type The type of crew to assign to the player's cabin.
     */
    public static void setCabin(CrewType type) {
        controller.manageCabins(type);
    }

    public static void action(boolean action){
        if(action)
            controller.sayYes();
        else
            controller.sayNo();
    }
    public static void roll(){
        controller.rollDice();
    }

    public static void choosePlanet(int index){
        controller.choosePlanet(index);
    }

    public static void chooseCargo(int x, int y){
        Coordinates coordinates = new Coordinates(x,y);
        controller.chooseCargo(coordinates);
    }

    public static void chooseGood(int index){
        controller.chooseGoods(index);
    }

    public static void doneGoods(){
        controller.doneGoods();
    }
//showMethods---------------------------------------------------------------------------------------------------------------

    /**
     * Clears the current message displayed to the user by setting it to an empty string.
     */
    public static void clear() {
        showMessage("");
    }

    /**
     * Displays the connection screen by loading the corresponding FXML and setting it as the center content.
     * The screen provides options for the user to choose the connection type.
     */
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

    /**
     * Displays the login screen by loading the corresponding FXML and setting it as the center content.
     * The login screen prompts the user for their credentials.
     */
    private static void showLogin() {
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

    /**
     * Displays the lobby screen by loading the corresponding FXML and setting it as the center content.
     * The lobby screen shows a list of available games and allows the user to join a game.
     */
    private static void showLobby() {
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

    /**
     * Displays the create new game screen by loading the corresponding FXML and setting it as the center content.
     * The screen allows the user to create a new game by specifying game details.
     */
    private static void showCreateNewGame(){
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
    /**
     * Displays the choose color screen by loading the corresponding FXML and setting it as the center content.
     * The screen allows the user to choose a color for their player.
     */
    private static void showChooseColor(){
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

    /**
     * Displays a waiting indicator on the screen. The size and position of the indicator
     * depend on the current game phase. If the phase is {@link GamePhases#LOGIN}, a larger
     * progress indicator is displayed at the center of the screen. Otherwise, a smaller
     * indicator is displayed at the bottom.
     */
    private static void showWait() {
        Platform.runLater(() -> {
            ProgressIndicator circle = new ProgressIndicator();
            if(controller.getPhase()== GamePhases.LOGIN){
                circle.setPrefSize(100, 100);
                circle.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                layout.setCenter(circle);
            }else{
                circle.setPrefSize(50, 50);
                circle.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                layout.setBottom(circle);
            }
        });
    }

    /**
     * Displays the start screen by loading the corresponding FXML file and setting it as the center content.
     * This screen is shown at the beginning of the game.
     */
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

    /**
     * Displays the "End Draw Tiles Cards" screen by loading the corresponding FXML file and setting it as the center content.
     * This screen is shown when the player finishes drawing tiles.
     */
    private static void showS_EndDrawTilesCards() {
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

    /**
     * Displays the "Manage Draw Tiles Cards" screen by loading the corresponding FXML file and setting it as the center content.
     * This screen is shown when the player needs to manage their drawn tiles.
     */
    private static void showS_ManageDrawTilesCards() {
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

    /**
     * Displays the "Deck Check" screen by loading the corresponding FXML file and setting it as the center content.
     * This screen allows the player to check their deck of cards.
     */
    private static void showDeckCheck() {
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

    /**
     * Displays the "End Ship" screen by loading the corresponding FXML file and setting it as the center content.
     * This screen is shown when the shipboard creation phase is completed.
     */
    private static void showEndShip() {
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

    /**
     * Displays the "Draw Card" screen by loading the corresponding FXML file and setting it as the center content.
     * This screen is shown when the player needs to draw a card.
     */
    private static void showCard() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/card.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    /**
     * Displays the "Choose Crew" screen by loading the corresponding FXML file and setting it as the center content.
     * This screen is shown when the player needs to select their crew for the ship.
     */
    private static void showChooseCrew() {
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/chooseCrew.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    private static void showEnd(){
        Platform.runLater(() -> {
            loader = new FXMLLoader(GUI.class.getResource("/gui/end.fxml"));
            BorderPane newLayer;
            try {
                newLayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            layout.setCenter(newLayer);
        });
    }

    /**
     * Displays a message to the user by setting it in a label at the bottom of the screen.
     *
     * @param message The message to display.
     */
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

    /**
     * Adds the list of joinable games to the lobby view.
     * Updates the list of games in the lobby screen if there are available games to join.
     *
     * @param joinableGames The list of games available to join.
     */
    private static void addJoinableGame(ArrayList<GameInfo> joinableGames) {
        if (joinableGames == null) {
            return;
        }
        if (controller.getState() == ClientState.LOBBY) {
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

    /**
     * Adds ship buttons to the screen for players to check their ships.
     * The number of ship buttons depends on the number of players in the game.
     */
    public static void addCheckShip() {
        Platform.runLater(() -> {
            HBox top=new HBox();
            VBox right=new VBox();
            VBox left=new VBox();
            top.setSpacing(500);
            quit=new Button("QUIT");
            fullScreen=new Button("⛶");
            quit.setOnAction(_ -> {
                top.getChildren().clear();
                left.getChildren().clear();
                right.getChildren().clear();
                layout.setTop(null);
                layout.setCenter(null);
                layout.setLeft(null);
                layout.setRight(null);
                ship1 = new Button();
                ship2 = new Button();
                ship3 = new Button();
                ship1.setPrefWidth(52);
                ship1.setPrefHeight(614);
                ship2.setPrefHeight(52);
                ship2.setPrefWidth(614);
                ship3.setPrefWidth(52);
                ship3.setPrefHeight(614);
                ship1.setAlignment(Pos.CENTER);
                ship2.setAlignment(Pos.CENTER);
                ship3.setAlignment(Pos.CENTER);
                ship1.setVisible(false);
                ship2.setVisible(false);
                ship3.setVisible(false);
                top.getChildren().add(ship2);
                left.getChildren().add(ship1);
                right.getChildren().add(ship3);
                layout.setTop(top);
                layout.setLeft(left);
                layout.setRight(right);
                layout.getStylesheets().set(0,css2);
                controller.leaveGame();
            });
            quit.setPrefWidth(100);
            quit.setBorder(new Border((BorderStroke) null));
            quit.setFont(new Font("Franklin Gothic Heavy", 10));
            fullScreen.setOnAction(_ -> primaryStage.setFullScreen(!primaryStage.isFullScreen()));
            fullScreen.setPrefWidth(100);
            fullScreen.setBorder(new Border((BorderStroke) null));
            fullScreen.setFont(new Font("Franklin Gothic Heavy", 10));
            top.getChildren().add(fullScreen);
            top.setAlignment(Pos.CENTER);
            right.setAlignment(Pos.CENTER);
            left.setAlignment(Pos.CENTER);

            if (controller.getFlightBoard().getInGamePlayers().size() == 2) {
                ship1 = new Button();
                ship1.setOnAction(_ -> checkShip(1));
                ship1.setPrefHeight(52);
                ship1.setPrefWidth(614);
                ship1.setAlignment(Pos.CENTER);
                top.getChildren().add(ship1);
            }
            if (controller.getFlightBoard().getInGamePlayers().size() == 3) {
                ship1 = new Button();
                ship2 = new Button();
                ship1.setOnAction(_ -> checkShip(1));
                ship2.setOnAction(_ -> checkShip(2));
                ship1.setPrefWidth(52);
                ship1.setPrefHeight(614);
                ship2.setPrefWidth(52);
                ship2.setPrefHeight(614);
                ship1.setAlignment(Pos.CENTER);
                ship2.setAlignment(Pos.CENTER);
                ship3=new Button();
                ship3.setPrefHeight(52);
                ship3.setPrefWidth(614);
                ship3.setVisible(false);
                top.getChildren().add(ship3);
                left.getChildren().add(ship1);
                right.getChildren().add(ship2);
                layout.setLeft(left);
                layout.setRight(right);
            }
            if (controller.getFlightBoard().getInGamePlayers().size() == 4) {
                ship1 = new Button();
                ship2 = new Button();
                ship3 = new Button();
                ship1.setOnAction(_ -> checkShip(1));
                ship2.setOnAction(_ -> checkShip(2));
                ship3.setOnAction(_ -> checkShip(3));
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
                layout.setRight(right);
            }
            top.getChildren().add(quit);
            layout.setTop(top);
        });
    }

    /**
     * Displays the appropriate screen based on the current client state.
     * This method updates the view by loading the corresponding FXML file based on the state.
     *
     * @param newState The new client state that dictates which screen to display.
     */
    public static void displayClientState(ClientState newState) {
        clear();
        if(GUI.getController().getPhase()==GamePhases.CARDS){
            showCard();
        }else {
            switch (newState) {
                case CHOOSE_CONNECTION_TYPE -> {
                    showConnection();
                    layout.getStylesheets().set(0, css2);
                }
                case LOGIN -> {
                    showLogin();
                    layout.getStylesheets().set(0, css2);
                }
                case LOBBY -> {
                    showLobby();
                    layout.getStylesheets().set(0, css2);
                }
                case LOBBY0 -> {
                    showCreateNewGame();
                    layout.getStylesheets().set(0, css2);
                }
                case COLOR_CHOICE, COLOR_CHOICE0 -> {
                    showChooseColor();
                    layout.getStylesheets().set(0, css2);
                }
                case START_SHIP_CREATION -> {
                    showStart();
                    layout.getStylesheets().set(0, css2);
                }
                case S_END_DRAW_TILE_CARD -> {
                    showS_EndDrawTilesCards();
                    layout.getStylesheets().set(0, css3);
                    addCheckShip();
                }
                case S_MANAGE_DRAWN_TILE -> {
                    showS_ManageDrawTilesCards();
                    layout.getStylesheets().set(0, css3);
                }
                case S_MANAGE_CARDS -> {
                    showDeckCheck();
                    layout.getStylesheets().set(0, css3);
                }
                case S_FINISHED -> {
                    showEndShip();
                    layout.getStylesheets().set(0, css3);
                }
                case MANAGE_CABINS-> {
                    showChooseCrew();
                    layout.getStylesheets().set(0, css3);
                }
                case COORD_REQUEST -> {
                    showCard();
                    layout.getStylesheets().set(0, css3);
                }
                case WAIT -> {
                    showWait();
                }
                case RECONNECTING -> {
                }
            }
        }
    }

    /**
     * Sets the client state and updates the view accordingly.
     *
     * @param newState The new client state to set.
     */
    @Override
    public void setClientState(ClientState newState) {
        displayClientState(newState);
    }


    /**
     * Handles the event when coordinates need to be sent. This method is a placeholder
     * and doesn't perform any action in the current implementation.
     */
    @Override
    public void sendingCoordinates() {

    }

    /**
     * Displays the turned tiles. Depending on the current game state, this method will
     * update the UI to show the turned tiles during the tile drawing phase.
     *
     * @param turnedTiles A map of turned tiles to display.
     */
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
            } catch (Exception _) {
            }
        });
    }

    /**
     * Handles the event when cargo is selected. This method is a placeholder
     * and doesn't perform any action in the current implementation.
     *
     * @param coordinates The coordinates of the selected cargo.
     */
    @Override
    public void cargoSelected(Coordinates coordinates) {

    }

    /**
     * Displays an error message indicating that the user has entered incorrect input.
     */
    @Override
    public void wrongLocalInput() {
        showMessage("Wrong input!");
    }

    /**
     * Handles the event when a coordinate is selected. This method is a placeholder
     * and doesn't perform any action in the current implementation.
     */
    @Override
    public void coordinateSelected() {

    }

    /**
     * Displays a card to the player. If the current state is drawing cards, this method will
     * update the UI to show the drawn card.
     *
     * @param cards A list of cards to display.
     */
    @Override
    public void showDeck(ArrayList<Card> cards) {
    }

    /**
     * Prints the flight board. If the game is finished or the previous state was finished,
     * this method updates the UI to display the flight board.
     *
     * @param lightFlightboard The light flight board to display.
     */
    @Override
    public void printFlightboard(LightFlightboard lightFlightboard) {
        Platform.runLater(() -> {
            if (controller.isChecking() == null)
                if (controller.getState() == ClientState.S_FINISHED || (controller.getPreviousState() == ClientState.S_FINISHED && controller.getState() == ClientState.WAIT)) {
                    EndShipController endShipController = loader.getController();
                    endShipController.update();
                } else if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                    S_EndDrawTilesCardsController sceneController = loader.getController();
                    sceneController.update();
                }
        });
    }

    /**
     * Displays the ship board for a particular player. This method loads the FXML for the ship board
     * and updates the UI to display it.
     *
     * @param lightShipBoard The light ship board to display.
     */
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
            CheckShipController checkShipController = loader.getController();
            checkShipController.setPlayer(lightShipBoard);
            layout.setCenter(newLayer);
        });
    }

    /**
     * Placeholder method for printing the ship board. Currently, this method does nothing.
     *
     * @param lightShipBoard The light ship board.
     */
    @Override
    public void printShipboard(LightShipBoard lightShipBoard) {

    }

    /**
     * Displays the available decks to the player. If the current state is managing drawn tiles,
     * this method will update the UI to show the available decks.
     */
    @Override
    public void showAvailableDecks() {
        Platform.runLater(() -> {
            if (controller.isChecking() == null)
                if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                    S_EndDrawTilesCardsController sEndDrawTilesCardsController = loader.getController();
                    sEndDrawTilesCardsController.update();
                }
        });
    }

    /**
     * Placeholder method for printing a projectile. Currently, this method does nothing.
     *
     * @param projectile The projectile to print.
     */
    @Override
    public void printProjectile(Projectile projectile) {

    }

    /**
     * Placeholder method for printing cabins. Currently, this method does nothing.
     *
     * @param cabins The cabins to print.
     */
    @Override
    public void printCabins(Tile cabins) {

    }

    /**
     * Placeholder method for printing drawn tiles. Currently, this method does nothing.
     *
     * @param drawnTiles The drawn tiles to print.
     */
    @Override
    public void printDrawnTiles(Map<Integer, Tile> drawnTiles) {

    }

    /**
     * Placeholder method for printing goods. Currently, this method does nothing.
     *
     * @param goodsArray The array of goods to print.
     */
    @Override
    public void goodsPrinter(ArrayList<Goods> goodsArray) {

    }

    /**
     * Placeholder method for printing booked tiles. Currently, this method does nothing.
     *
     * @param shipBoard The ship board to print.
     */
    @Override
    public void printBooked(LightShipBoard shipBoard) {

    }

    /**
     * Sets the client controller for the GUI. This method is a placeholder and doesn't perform any action.
     *
     * @param clientController The client controller to set.
     */
    @Override
    public void setClientController(ClientController clientController) {

    }

    /**
     * Placeholder method for crew positioning. Currently, this method does nothing.
     */
    @Override
    public void crewPositioned() {

    }

    /**
     * Displays a generic message to the player.
     *
     * @param genericMessage The message to display.
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        showMessage(genericMessage);
    }

    /**
     * Placeholder method for handling penalties. Currently, this method does nothing.
     *
     * @param playerName The name of the player.
     * @param penalty    The penalty to apply.
     */
    @Override
    public void victimOfThePenalty(String playerName, Penalty penalty) {
        if(controller.isChecking()!=null&& Objects.equals(controller.isChecking(), playerName)){
            LightShipBoard lightShipBoard = controller.getFlightBoard().getInGamePlayer(playerName).getShipBoard();
            checkShipboard(lightShipBoard);
        }
    }

    /**
     * Placeholder method for notifying when a player joins. Currently, this method does nothing.
     *
     * @param expectedPlayer The expected player count.
     * @param currentPlayer  The current player count.
     * @param reconnected    A flag indicating whether the player reconnected.
     */
    @Override
    public void notifyPlayerJoined(int expectedPlayer, int currentPlayer, boolean reconnected) {

    }

    /**
     * Placeholder method for handling login responses. Currently, this method does nothing.
     *
     * @param success The success status of the login.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showLoginResponse(boolean success) throws RemoteException {

    }

    /**
     * Displays the list of joinable games in the lobby.
     *
     * @param joinableGames The list of joinable games to display.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showJoinableGamesList(ArrayList<GameInfo> joinableGames) throws RemoteException {
        addJoinableGame(joinableGames);
    }

    /**
     * Displays an error message in the lobby.
     *
     * @param errorMessage The error message to display.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showErrorMessage(String errorMessage) throws RemoteException {
        showMessage(errorMessage);
    }

    /**
     * Placeholder method for displaying a drawn tile. Currently, this method does nothing.
     *
     * @param drawnTile The drawn tile.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showDrawnTile(Tile drawnTile) throws RemoteException {

    }

    /**
     * Displays a wrong input message.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showWrongInputMessage() throws RemoteException {
        showMessage("wrong input!");
    }

    /**
     * Placeholder method for asking the player to roll the dice. Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void asksToRollTheDices() throws RemoteException {

    }

    /**
     * Displays the dice roll result.
     *
     * @param diceRoll The result of the dice roll.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showDiceRoll(int diceRoll) throws RemoteException {
        if(controller.isChecking()==null){
            if(controller.getPhase()==GamePhases.CARDS){
                CardsController cardsController = loader.getController();
                cardsController.showRoll(diceRoll);
            }
        }
    }

    /**
     * Placeholder method for asking the player to choose a starting position. Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void asksToChooseStartingPosition() throws RemoteException {

    }

    /**
     * Placeholder method for asking the player to input coordinates. Currently, this method does nothing.
     *
     * @param coordReqType The type of coordinate request.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void asksToInputCoordinates(CoordReqType coordReqType) throws RemoteException {

    }


    /**
     * Placeholder method for notifying the player that they can draw a card deck.
     * Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyYouCanDrawThisCardDeck() throws RemoteException {

    }

    /**
     * Placeholder method for notifying the player that their ship is correct.
     * Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyYourShipIsCorrect() throws RemoteException {

    }

    /**
     * Placeholder method for asking the player to make a choice.
     * Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void asksToMakeAChoice() throws RemoteException {

    }

    /**
     * Displays the scores of all players.
     *
     * @param scores A map containing the player names as keys and their respective scores as values.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void showScores(Map<String, Integer> scores) throws RemoteException {
        showEnd();
    }

    /**
     * Notifies the player about the drawn card.
     *
     * @param card The card that was drawn.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyDrawnCard(Card card) throws RemoteException {
        if(controller.getState()!=ClientState.DRAW_CARD)
            controller.setState(ClientState.WAIT_TO_DRAW);
    }

    /**
     * Placeholder method for notifying that the player landed on a planet.
     * Currently, this method does nothing.
     *
     * @param playerName The name of the player who landed on the planet.
     * @param planet     The ID of the planet the player landed on.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyPlayerLandedOnPlanet(String playerName, int planet) throws RemoteException {
        Platform.runLater(() -> {
            if (controller.isChecking()==null)
                if(controller.getState()==ClientState.PLANET_CHOICE){
                    CardsController cardController=loader.getController();
                    cardController.initialize();
                }
        });
    }

    /**
     * Placeholder method for notifying when the client is connected.
     * Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void connected() throws RemoteException {

    }

    /**
     * Sets the game mode for the client.
     *
     * @param gameMode The game mode to set.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void setGameMode(GameMode gameMode) throws RemoteException {

    }

    /**
     * Notifies the player when the hourglass has turned.
     *
     * @param i The index of the hourglass turn.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyTurnedHourglass(int i) throws RemoteException {
        timer = new Timer();
        counter = 0;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                counter++;
                Platform.runLater(() -> {
                    if (controller.isChecking() == null) {
                        if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                            S_EndDrawTilesCardsController sceneController = loader.getController();
                            if (sceneController != null) {
                                sceneController.goProgressBar(counter, 100, controller.getHourglassTurns());
                            }
                        } else if (controller.getState() == ClientState.S_FINISHED || (controller.getPreviousState() == ClientState.S_FINISHED && controller.getState() == ClientState.WAIT && controller.isChecking() == null)) {
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

    /**
     * Notifies the player when the time has ended and updates the progress bar.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyEndOfTime() throws RemoteException {
        counter = 100;
        Platform.runLater(() -> {
            if (controller.isChecking() == null) {
                if (controller.getState() == ClientState.S_END_DRAW_TILE_CARD) {
                    S_EndDrawTilesCardsController sceneController = loader.getController();
                    if (sceneController != null) {
                        sceneController.goProgressBar(counter, 100, controller.getHourglassTurns());
                    }
                } else if (controller.getState() == ClientState.S_FINISHED || (controller.getPreviousState() == ClientState.S_FINISHED && controller.getState() == ClientState.WAIT)) {
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

    /**
     * Placeholder method for notifying early landing. Currently, this method does nothing.
     *
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyEarlyLanding() throws RemoteException {
        showEnd();
    }

    /**
     * Placeholder method for notifying the strength of a combat zone. Currently, this method does nothing.
     *
     * @param playerName The name of the player.
     * @param strength   The strength of the combat zone.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyCombatZoneStrength(String playerName, float strength) throws RemoteException {

    }

    /**
     * Placeholder method for notifying the engine strength of a combat zone. Currently, this method does nothing.
     *
     * @param playerName The name of the player.
     * @param strength   The engine strength of the combat zone.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyCombatZoneEngine(String playerName, float strength) throws RemoteException {

    }

    /**
     * Placeholder method for notifying the crew strength of a combat zone. Currently, this method does nothing.
     *
     * @param playerName The name of the player.
     * @param crew       The crew strength of the combat zone.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyCombatZoneCrew(String playerName, int crew) throws RemoteException {

    }

    /**
     * Notifies the player about the podium at the end of the game.
     *
     * @param players A list of players in podium order.
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public void notifyPodium(ArrayList<Player> players) throws RemoteException {

    }

    /**
     * Returns the displayed view. This method is a placeholder and currently returns null.
     *
     * @return null
     * @throws RemoteException If a remote exception occurs.
     */
    @Override
    public DisplayableView getDisplayedView() throws RemoteException {
        return null;
    }

    /**
     * Gets the current controller instance.
     *
     * @return The client controller.
     */
    public static ClientController getController() {
        return controller;
    }

    /**
     * Sets the game name for the current game session.
     *
     * @param gameName The name of the game to set.
     */
    public static void setGameName(String gameName) {
        GUI.gameName = gameName;
    }

    /**
     * Sets the number of players for the current game session.
     *
     * @param numberOfPlayers The number of players to set.
     */
    public static void setNumberOfPlayers(int numberOfPlayers) {
        GUI.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Sets the game mode for the current game session.
     *
     * @param mode The game mode to set.
     */
    public static void setMode(GameMode mode) {
        GUI.mode = mode;
    }

    /**
     * Returns the number of players for the current game session.
     *
     * @return The number of players.
     */
    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Returns the name of the current game session.
     *
     * @return The game name.
     */
    public static String getGameName() {
        return gameName;
    }

    /**
     * Returns the game mode for the current game session.
     *
     * @return The game mode.
     */
    public static GameMode getMode() {
        return mode;
    }

    /**
     * Returns the percentage for the first hourglass.
     *
     * @return The first hourglass percentage.
     */
    public static double getPercentage1() {
        return percentage1;
    }

    /**
     * Returns the percentage for the second hourglass.
     *
     * @return The second hourglass percentage.
     */
    public static double getPercentage2() {
        return percentage2;
    }

    /**
     * Returns the percentage for the third hourglass.
     *
     * @return The third hourglass percentage.
     */
    public static double getPercentage3() {
        return percentage3;
    }

    /**
     * Sets the percentage for the first hourglass.
     *
     * @param percentage1 The percentage to set for the first hourglass.
     */
    public static void setPercentage1(double percentage1) {
        GUI.percentage1 = percentage1;
    }

    /**
     * Sets the percentage for the second hourglass.
     *
     * @param percentage2 The percentage to set for the second hourglass.
     */
    public static void setPercentage2(double percentage2) {
        GUI.percentage2 = percentage2;
    }

    /**
     * Sets the percentage for the third hourglass.
     *
     * @param percentage3 The percentage to set for the third hourglass.
     */
    public static void setPercentage3(double percentage3) {
        GUI.percentage3 = percentage3;
    }
}