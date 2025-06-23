package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

/**
 * Controller for handling the deck check phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller is responsible for displaying the cards that are available in the deck,
 * allowing the player to check the cards before proceeding with the game.
 * </p>
 */
public class DeckCheckController extends GUIControllers{

    @FXML
    public ImageView imageView1;
    @FXML
    public ImageView imageView2;
    @FXML
    public ImageView imageView3;

    /**
     * Initializes the image views for displaying the cards in the deck.
     * The images are loaded from the paths of the displayable cards provided by the GUI.
     */
    @FXML
    public void initialize() {
        imageView1.setImage(loadImage(GUI.displayableCards().get(0).getFilePath()));
        imageView2.setImage(loadImage(GUI.displayableCards().get(1).getFilePath()));
        imageView3.setImage(loadImage(GUI.displayableCards().get(2).getFilePath()));
    }

    /**
     * Loads an image from the specified file path.
     * @param path the path to the image file
     * @return the loaded Image object
     */
    private Image loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Immagine non trovata: " + path);
            return null;
        }
        return new Image(stream);
    }

    /**
     * Notifies the GUI that the deck check is complete.
     * This method is called when the player has finished checking the cards.
     */
    @FXML
    public void done() {
        GUI.doneChecking();
    }
}
