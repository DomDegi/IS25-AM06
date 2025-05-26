package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller for handling the start screen in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the actions that occur on the start screen, such as starting the game
 * when the user clicks a button or presses the ENTER key.
 * </p>
 */
public class StartController {

    /**
     * Starts the game when triggered.
     * This method is called when the "Start" button is clicked.
     */
    @FXML
    public void start() {
        GUI.start();
    }

    /**
     * Handles key events for the start screen.
     * Specifically listens for the ENTER key being pressed and triggers the start of the game.
     * @param event the key event that occurred
     */
    @FXML
    public void K(KeyEvent event) {
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                start();
            }
        }
    }
}
