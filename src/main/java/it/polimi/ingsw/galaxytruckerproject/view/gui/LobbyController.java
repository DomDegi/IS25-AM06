package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * Controller for handling the lobby screen in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the list of available games in the lobby, allowing the player to join an existing game
 * or create a new game. It also listens for key events to perform actions such as joining or creating a game.
 * </p>
 */
public class LobbyController {

    ObservableList<String> gamesList;

    @FXML
    public ListView<String> joinableGames;

    /**
     * Handles key events for the lobby screen.
     * Specifically listens for the "J" key to join a game and the "C" key to create a new game.
     * @param event the key event that occurred
     */
    @FXML
    public void K(KeyEvent event){
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.J)){
                join();
            }
        }
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.C)){
                showCreateNewGame();
            }
        }
    }

    /**
     * Initializes the list of joinable games by fetching the game information from the controller
     * and displaying it in the ListView.
     */
    @FXML
    public void initialize() {
        ArrayList<String> games = new ArrayList<>();
        for (GameInfo gameInfo : GUI.getController().getGameInfo()) {
            String gameName = gameInfo.getGameName();
            String game = gameName + "\t" + gameInfo.getCurrentPlayerCount() + "/" + gameInfo.getMaxPlayerCount() + "\t" + gameInfo.getGameMode();
            games.add(game);
        }
        gamesList = FXCollections.observableArrayList(games);
        joinableGames.setItems(gamesList);
    }

    /**
     * Opens the create new game screen, allowing the player to create a new game.
     */
    @FXML
    public void showCreateNewGame(){
        GUI.getController().setState(ClientState.LOBBY0);
    }

    /**
     * Updates the list of joinable games in the lobby screen.
     * @param joinableGame the list of games to be displayed in the lobby
     */
    @FXML
    public void update(ArrayList<String> joinableGame) {
        joinableGames.getItems().setAll(joinableGame);
    }

    /**
     * Joins a selected game from the list of joinable games.
     * It retrieves the selected game's name and calls the method in the GUI to join the game.
     */
    @FXML
    public void join() {
        String gameName;
        ObservableList<String> gameInfos = joinableGames.getSelectionModel().getSelectedItems();
        if (!gameInfos.isEmpty()) {
            gameName = gameInfos.getFirst().split("\t")[0];
        }else{
            return;
        }
        System.out.println(gameName);
        GUI.join(gameName);
    }
}
