package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;

public class ReconnectController extends GUIControllers{
    @FXML
    public void quit(){
        System.exit(0);
    }

    @FXML
    public void reconnect(){
        GUI.getController().reconnect();
    }
}
