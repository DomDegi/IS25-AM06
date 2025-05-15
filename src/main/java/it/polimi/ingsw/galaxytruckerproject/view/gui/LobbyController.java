package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LobbyController {
    @FXML
    public TextField gameName;

    @FXML
    public void showCreateNewGame() throws IOException {
        GUI.showCreateNewGame();
    }

}
