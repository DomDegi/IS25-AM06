package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import java.io.IOException;

public class ConnectionController{

    @FXML
    public void setRMI() throws IOException{
        GUI.setConnection("r");
    }

    @FXML
    public void setSocket() throws IOException{
        GUI.setConnection("s");
    }
}
