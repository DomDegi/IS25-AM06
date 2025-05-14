package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;

public class GUI extends Application {
    private static Stage primaryStage;
    private static BorderPane layout;
    private static final ClientController controller=new ClientController();

    public static void startGui() {
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
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/gui/welcome.fxml"));
        BorderPane newLayer=loader.load();
        layout.setCenter(newLayer);
    }

    public static void startNewGame() throws IOException {
        showConnection();
    }

    public static void setRMI() throws IOException {
        try {
            controller.connectRMI();
            showLogin();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setSocket() throws IOException {
        try {
            controller.connectSocket("localhost",12345);
            showLogin();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setName(String name) throws IOException {
        controller.getMe().setPlayerName(name);
        if(controller.doneNaming()){
            showLobby();
        }
    }

    public static void createGame(String gameName, int  playerNum, GameMode gameMode) throws IOException {
        controller.createGame(gameName,playerNum,gameMode);
    }

//showMethods---------------------------------------------------------------------------------------------------------------
    private static void showConnection() throws IOException {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/gui/connection.fxml"));
        BorderPane newLayer=loader.load();
        layout.setCenter(newLayer);
    }

    public static void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/gui/login.fxml"));
        BorderPane newLayer=loader.load();
        layout.setCenter(newLayer);
    }

    public static void showLobby() throws IOException {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/gui/lobby.fxml"));
        BorderPane newLayer=loader.load();
        layout.setCenter(newLayer);
    }

    public static void showCreateNewGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/gui/createNewGame.fxml"));
        BorderPane newLayer=loader.load();
        layout.setCenter(newLayer);
    }


}

