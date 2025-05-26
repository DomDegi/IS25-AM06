package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectionController{

    @FXML
    public TextField ip;

    @FXML
    public TextField port;

    @FXML
    public void setRMI(){
        int portInt;
        try {
            if(port.getText().equals(""))
                portInt=0;
            else
                portInt=Integer.getInteger(port.getText());
            GUI.setConnection("r",ip.getText(),portInt);
        }catch (NumberFormatException _){
            GUI.showMessage("Invalid Port");
        }
    }

    @FXML
    public void setSocket(){
        int portInt;
        try {
            if(port.getText().equals(""))
                portInt=0;
            else
                portInt=Integer.getInteger(port.getText());
            GUI.setConnection("s",ip.getText(),portInt);
        }catch (NumberFormatException _){
            GUI.showMessage("Invalid Port");
        }
    }
}
