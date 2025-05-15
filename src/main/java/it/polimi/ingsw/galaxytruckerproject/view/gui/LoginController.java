package it.polimi.ingsw.galaxytruckerproject.view.gui;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    public TextField username;

    @FXML
    public void setName() {
        GUI.setName(username.getText());
    }
}
