package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
    public void K(KeyEvent event) throws IOException {
        if(!r.isDisable())
            if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
                if(event.getCode().equals(KeyCode.R)){
                    red();
                }
            }
        if(!y.isDisable())
            if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
                if(event.getCode().equals(KeyCode.Y)){
                    yellow();
                }
            }
        if(!g.isDisable())
            if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
                if(event.getCode().equals(KeyCode.G)){
                    green();
                }
            }
        if(!b.isDisable())
            if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
                if(event.getCode().equals(KeyCode.B)){
                    blue();
                }
            }
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
