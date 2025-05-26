package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller for managing the connection setup screen in the Galaxy Trucker game's GUI.
 * <p>
 * This controller handles the user input for setting the connection type and the server IP address.
 * The user can choose between using RMI or Socket for communication, and the controller sets up the connection accordingly.
 * </p>
 */
public class ConnectionController {

    @FXML
    public TextField ip;

    /**
     * Sets the connection type to RMI (Remote Method Invocation) and specifies the IP address.
     * This method is called when the user chooses to connect via RMI.
     */
    @FXML
    public void setRMI() {
        GUI.setConnection("r", ip.getText());
    }

    /**
     * Sets the connection type to Socket and specifies the IP address.
     * This method is called when the user chooses to connect via Socket.
     */
    @FXML
    public void setSocket() {
        GUI.setConnection("s", ip.getText());
    }
}
