package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller for handling the login screen in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the player's input for their username and listens for key events,
 * specifically the ENTER key, to submit the username and set it in the game.
 * </p>
 */
public class LoginController {

    @FXML
    public TextField username;

    /**
     * Handles key events for the login screen.
     * Specifically listens for the ENTER key being pressed to submit the username.
     * @param event the key event that occurred
     */
    @FXML
    public void K(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.ENTER)){
                setName();
            }
        }
    }

    /**
     * Sets the player's name in the game based on the input from the username text field.
     * This method is called when the ENTER key is pressed or when the user confirms the input.
     */
    @FXML
    public void setName() {
        GUI.setName(username.getText());
    }
}
