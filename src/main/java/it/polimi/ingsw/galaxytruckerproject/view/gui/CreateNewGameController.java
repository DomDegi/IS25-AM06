package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Controller for managing the creation of a new game in the Galaxy Trucker game's GUI.
 * <p>
 * This controller handles the input from the user for setting up a new game, including
 * the game name, number of players, and the selected game mode. It also handles the transition
 * to the next phase based on the selected settings.
 * </p>
 */
public class CreateNewGameController extends GUIControllers{

    ObservableList<String> gameModes = FXCollections.observableArrayList("Trial", "Level1", "Level2", "Level3");

    @FXML
    public TextField gameName;
    @FXML
    public TextField playerNum;
    @FXML
    public ChoiceBox<String> gameMode;

    /**
     * Initializes the controller by setting the default game mode and populating the game mode options.
     */
    @FXML
    public void initialize() {
        gameMode.setValue("Level2");
        gameMode.setItems(gameModes);
    }

    /**
     * Navigates back to the lobby screen when the user chooses to go back.
     */
    @FXML
    public void back() {
        GUI.getController().setState(ClientState.LOBBY);
    }

    /**
     * Creates a new game based on the user's input.
     * This method sets the game name, number of players, and game mode, and transitions to the appropriate state
     * for color choice. If the game mode is not supported, it shows an informational message.
     */
    @FXML
    public void createNewGame() {
        try {
            int i = Integer.parseInt(playerNum.getText());
            GUI.setGameName(gameName.getText());
            GUI.setNumberOfPlayers(i);
            if (gameMode.getValue().equals("Trial")) {
                GUI.setMode(GameMode.TRIAL);
                GUI.getController().setState(ClientState.COLOR_CHOICE0);
            } else if (gameMode.getValue().equals("Level2")) {
                GUI.setMode(GameMode.LEVEL2);
                GUI.getController().setState(ClientState.COLOR_CHOICE0);
            } else {
                GUI.showMessage("Upcoming feature, try something else");
            }
        } catch (NumberFormatException _) {
            // Handle invalid input for the number of players
        }
    }
}
