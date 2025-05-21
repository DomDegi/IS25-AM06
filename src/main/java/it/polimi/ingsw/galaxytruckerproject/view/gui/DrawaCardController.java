package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;

import java.io.IOException;

public class DrawaCardController {
    @FXML
    public void draw() throws IOException {
       GUI.drawCard();
    }
}