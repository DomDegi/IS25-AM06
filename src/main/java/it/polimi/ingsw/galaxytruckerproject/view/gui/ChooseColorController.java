package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

/**
 * Controller for managing the color selection phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller allows players to choose their player color by clicking on the appropriate button or using
 * keyboard shortcuts. The available colors are Red, Yellow, Green, and Blue. The controller also handles the state
 * transitions based on the selected color and notifies the `GUI` to update the game state.
 * </p>
 */
public class ChooseColorController extends GUIControllers{

    @FXML
    public Button r;

    @FXML
    public Button y;

    @FXML
    public Button g;

    @FXML
    public Button b;

    /**
     * Initializes the color buttons by enabling or disabling them based on the available colors.
     * The buttons are disabled if the corresponding color is not available for selection.
     */
    @FXML
    public void initialize(){
        r.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.RED));
        y.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.YELLOW));
        g.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.GREEN));
        b.setDisable(!GUI.getController().getAvailableColors().get(PlayersColor.BLUE));
    }

    /**
     * Handles key press events for color selection.
     * The user can select a color by pressing the corresponding key (R, Y, G, B) if the button is enabled.
     * @param event the key event that occurred
     * @throws IOException if an error occurs during the color selection process
     */
    @FXML
    public void K(KeyEvent event) throws IOException {
        if(!r.isDisable() && event.getEventType().equals(KeyEvent.KEY_PRESSED) && event.getCode().equals(KeyCode.R)) {
            red();
        }
        if(!y.isDisable() && event.getEventType().equals(KeyEvent.KEY_PRESSED) && event.getCode().equals(KeyCode.Y)) {
            yellow();
        }
        if(!g.isDisable() && event.getEventType().equals(KeyEvent.KEY_PRESSED) && event.getCode().equals(KeyCode.G)) {
            green();
        }
        if(!b.isDisable() && event.getEventType().equals(KeyEvent.KEY_PRESSED) && event.getCode().equals(KeyCode.B)) {
            blue();
        }
    }

    /**
     * Sets the player's color to Red and handles the state transition if necessary.
     * If the state is `COLOR_CHOICE0`, it triggers the game creation process.
     * @throws IOException if an error occurs during the color selection process
     */
    @FXML
    public void red() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.RED);
    }

    /**
     * Sets the player's color to Yellow and handles the state transition if necessary.
     * If the state is `COLOR_CHOICE0`, it triggers the game creation process.
     * @throws IOException if an error occurs during the color selection process
     */
    @FXML
    public void yellow() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.YELLOW);
    }

    /**
     * Sets the player's color to Green and handles the state transition if necessary.
     * If the state is `COLOR_CHOICE0`, it triggers the game creation process.
     * @throws IOException if an error occurs during the color selection process
     */
    @FXML
    public void green() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.GREEN);
    }

    /**
     * Sets the player's color to Blue and handles the state transition if necessary.
     * If the state is `COLOR_CHOICE0`, it triggers the game creation process.
     * @throws IOException if an error occurs during the color selection process
     */
    @FXML
    public void blue() throws IOException {
        if(GUI.getController().getState() == ClientState.COLOR_CHOICE0){
            GUI.createGame();
        }
        GUI.setColor(PlayersColor.BLUE);
    }
}
