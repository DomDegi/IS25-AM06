package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;

import java.io.IOException;

public class ChooseColorController {

    @FXML
    public void red() throws IOException {
        GUI.setColor(PlayersColor.RED);
    }
    @FXML
    public void yellow() throws IOException {
        GUI.setColor(PlayersColor.YELLOW);
    }
    @FXML
    public void green() throws IOException {
        GUI.setColor(PlayersColor.GREEN);
    }
    @FXML
    public void blue() throws IOException {
        GUI.setColor(PlayersColor.BLUE);
    }
}
