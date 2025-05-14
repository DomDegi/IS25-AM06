package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;

import java.io.IOException;

public class WelcomeController {
    @FXML
    public void startNewGame() throws IOException {
        GUI.startNewGame();
    }
}
