package it.polimi.ingsw.galaxytruckerproject.view.gui;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {
    @FXML
    public TextField username;

    @FXML
    public void K(KeyEvent event){
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.ENTER)){
                setName();
            }
        }
    }

    @FXML
    public void setName() {
        GUI.setName(username.getText());
    }
}
