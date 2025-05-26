package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Controller for handling the ship checking phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller displays the shipboard of the player and allows the player to check their ship.
 * It also handles updating the displayed ship image and tiles based on the game mode (LEVEL2 or TRIAL).
 * </p>
 */
public class CheckShipController {

    @FXML
    public ImageView shipImage;
    @FXML
    public GridPane tilesTable;

    /**
     * Finalizes the ship checking phase.
     * This method updates the client state and resets the checking status in the controller.
     */
    @FXML
    public void done() {
        GUI.displayClientState(GUI.getController().getState());
        GUI.getController().setChecking(null);
    }

    /**
     * Sets the player's shipboard for display in the GUI.
     * This method updates the ship image and tiles based on the current game mode (LEVEL2 or TRIAL).
     * It then populates the ship's tiles in the grid based on the shipboard information.
     *
     * @param shipBoard the shipboard of the player to be displayed
     */
    public void setPlayer(LightShipBoard shipBoard) {
        // Set the ship image based on the game mode
        if (GUI.getController().getGameMode() == GameMode.LEVEL2) {
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }

        // Clear the tiles table and populate it with the ship's tiles
        tilesTable.getChildren().clear();
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if (shipBoard.getTilesTable()[j][i].isPresent() && shipBoard.getTilesTable()[j][i].get().fillable()) {
                    tiles.add(shipBoard.getTilesTable()[j][i].get());
                }
            }
        }

        // Add each tile to the grid
        for (Tile tile : tiles) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                javafx.scene.image.Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(136);
                imageView.setFitHeight(136);
                imageView.setDisable(true);
                tilesTable.add(imageView, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }
    }
}
