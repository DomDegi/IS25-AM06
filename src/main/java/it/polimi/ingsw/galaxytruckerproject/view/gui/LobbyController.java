package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

public class LobbyController {

    ObservableList<String> gamesList;

    @FXML
    public ListView<String> joinableGames;

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

    @FXML
    public void showCreateNewGame(){
        GUI.getController().setState(ClientState.LOBBY0);
    }

    @FXML
    public void update(ArrayList<String> joinableGame) {
        joinableGames.getItems().setAll(joinableGame);
    }

    @FXML
    public void join(MouseEvent mouseEvent) {
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
