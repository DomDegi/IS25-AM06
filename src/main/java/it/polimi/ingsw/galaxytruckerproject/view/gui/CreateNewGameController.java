package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class CreateNewGameController {

    ObservableList<String> gameModes= FXCollections.observableArrayList("Trial","Level1","Level2","Level3");

    @FXML
    public TextField gameName;
    @FXML
    public TextField playerNum;
    @FXML
    public ChoiceBox<String> gameMode;

    @FXML
    public void initialize() {
        gameMode.setValue("Level2");
        gameMode.setItems(gameModes);
    }

    @FXML
    public void back(){
        GUI.getController().setState(ClientState.LOBBY);
    }

    @FXML
    public void createNewGame() {
        try {
            int i= Integer.parseInt(playerNum.getText());
            GUI.setGameName(gameName.getText());
            GUI.setNumberOfPlayers(i);
            if (gameMode.getValue().equals("Trial")){
                GUI.setMode(GameMode.TRIAL);
                GUI.getController().setState(ClientState.COLOR_CHOICE0);
            }else if (gameMode.getValue().equals("Level2")) {
                GUI.setMode(GameMode.LEVEL2);
                GUI.getController().setState(ClientState.COLOR_CHOICE0);
            }else{
                GUI.showMessage("Upcoming feature, try something else");
            }
        }catch (NumberFormatException _) {

        }

    }
}
