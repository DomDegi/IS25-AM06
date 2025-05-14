package it.polimi.ingsw.galaxytruckerproject.view.gui;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField username;

    @FXML
    public void setName() throws IOException {
        GUI.setName(username.getText());
    }
}
