package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import java.io.IOException;

public class ConnectionController{

    @FXML
    public void setRMI(){
        GUI.setConnection("r");
    }

    @FXML
    public void setSocket(){
        GUI.setConnection("s");
    }
}
