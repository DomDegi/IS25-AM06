package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

/**
 * Controller for handling the welcome screen in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the actions that occur on the welcome screen, such as starting a new game
 * when the user clicks a button or presses the ENTER key.
 * </p>
 */
public class WelcomeController extends GUIControllers {

    /**
     * Starts a new game when triggered.
     * This method is called when the "Start New Game" button is clicked.
     */
    @FXML
    public void startNewGame() {
        GUI.startNewGame();
    }

    /**
     * Handles key events for the welcome screen.
     * Specifically listens for the ENTER key being pressed and triggers the start of a new game.
     * @param event the key event that occurred
     */
    @FXML
    public void K(KeyEvent event) {
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode().equals(KeyCode.ENTER)) {
                startNewGame();
            }
        }
    }
}
