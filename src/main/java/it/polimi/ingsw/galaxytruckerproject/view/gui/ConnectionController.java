package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * The ConnectionController class is responsible for managing the input and configuration
 * of connection settings in the GUI. It provides functionality to set up connections
 * using either RMI or Socket protocols.
 */
public class ConnectionController{

    /**
     * Represents the input field for the IP address in the GUI.
     * This field is used to specify the IP address required for establishing a connection.
     * The value entered by the user is retrieved and utilized when creating connections
     * via RMI or Socket protocols.
     */
    @FXML
    public TextField ip;

    /**
     * The port TextField represents the input field within the user interface
     * for specifying the connection port. It is used to configure the port
     * number for establishing connections through RMI or Socket protocols.
     * The entered value is validated and processed while attempting to set up
     * a connection.
     */
    @FXML
    public TextField port;

    /**
     * Sets up a connection using the RMI (Remote Method Invocation) protocol.
     *
     * This method retrieves the IP address and port number entered in the
     * respective text fields. If the port field is left blank, it defaults to 0.
     * The method invokes the GUI's setConnection feature with "r" as the protocol.
     *
     * If the input for the port number is invalid (i.e., not a valid integer),
     * an error message is displayed to inform the user of the invalid port entry.
     *
     * Exceptions:
     * - NumberFormatException: Caught when the port field contains a non-integer value.
     */
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

    /**
     * Sets up a socket connection using the provided IP address and port number
     * from the GUI input fields. If the port field is left empty, a default value
     * of 0 is used. Displays an error message if the port input is invalid.
     *
     * This method retrieves user input from the 'ip' and 'port' text fields,
     * converts the port input to an integer, and invokes the GUI.setConnection
     * method to configure the connection with the socket protocol. If the port
     * input is not a valid integer, a 'Invalid Port' message is shown.
     */
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
