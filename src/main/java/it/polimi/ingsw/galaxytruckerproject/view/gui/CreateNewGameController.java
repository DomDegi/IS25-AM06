package it.polimi.ingsw.galaxytruckerproject.view.gui;

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
    public void createNewGame() {
        try {
            int i= Integer.parseInt(playerNum.getText());
            if (gameMode.getValue().equals("Trial")){
                GUI.createGame(gameName.getText(),i,GameMode.TRIAL);
            }else if (gameMode.getValue().equals("Level2")) {
                try {
                    GUI.createGame(gameName.getText(),i,GameMode.LEVEL2);
                }catch (NumberFormatException _) {

                }
            }else{
                GUI.showMessage("Upcoming feature, try something else");
            }
        }catch (NumberFormatException _) {

        }

    }
}
