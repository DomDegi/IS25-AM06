package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectionController{

    @FXML
    public TextField ip;
    @FXML
    public void setRMI(){
        GUI.setConnection("r",ip.getText());
    }

    @FXML
    public void setSocket(){
        GUI.setConnection("s",ip.getText());
    }
}
