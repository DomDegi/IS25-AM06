package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ChooseColorController {

    @FXML
    public Button r;

    @FXML
    public Button y;

    @FXML
    public Button g;

    @FXML
    public Button b;

    @FXML
    public void initialize(){
        r.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.RED));
        y.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.YELLOW));
        g.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.GREEN));
        b.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.BLUE));
    }

    @FXML
    public void red() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.RED);
    }
    @FXML
    public void yellow() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.YELLOW);
    }
    @FXML
    public void green() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.GREEN);
    }
    @FXML
    public void blue() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.BLUE);
    }
}
