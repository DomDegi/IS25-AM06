package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

/**
 * Controller for managing the drawn tiles in the Galaxy Trucker game's GUI.
 * <p>
 * This controller handles the drawing and management of tiles, allowing the player to place, refuse, or book tiles.
 * It also provides functionality for rotating tiles and interacting with the shipboard based on the game mode (LEVEL2 or TRIAL).
 * </p>
 */
public class S_ManageDrawTileController {

    @FXML
    public ImageView shipImage;

    @FXML
    public Button drawnTile;

    @FXML
    public TilePane drawnTiles1;

    @FXML
    public TilePane drawnTiles2;

    @FXML
    public GridPane tilesTable;

    @FXML
    public GridPane bookedTiles;

    /**
     * Initializes the view for managing drawn tiles.
     * Configures the ship image, drawn tiles, and tile placement buttons based on the game mode (LEVEL2 or TRIAL).
     * Sets up the tiles on the player's shipboard and available tiles to be drawn.
     */
    @FXML
    public void initialize() {
        // Set ship image and configure visibility based on the game mode
        if(GUI.getController().getGameMode() == GameMode.LEVEL2){
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
            bookedTiles.setVisible(false);
        }

        // Set image for the drawn tile
        String inHandImagePath = GUI.getController().getTileInHand().getImagePath();
        InputStream inHandImageStream = getClass().getResourceAsStream(inHandImagePath);
        if (inHandImageStream == null) {
            System.err.println("not found" + inHandImagePath);
            drawnTile.setText("MCS");
        } else {
            Image inHandImage = new Image(inHandImageStream);
            ImageView inHandImageView = new ImageView(inHandImage);
            inHandImageView.rotateProperty().setValue(GUI.getController().getTileInHand().getRotation() * 90);
            inHandImageView.setFitWidth(175);
            inHandImageView.setFitHeight(175);
            drawnTile.setPadding(Insets.EMPTY);
            drawnTile.setGraphic(inHandImageView);
        }

        // Initialize the tile placement table with buttons
        tilesTable.getChildren().clear();
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if ((GUI.getController().getGameMode() == GameMode.LEVEL2 && !((j == 0 && (i <= 1 || i == 3 || i >= 5)) || (j == 1 && (i == 0 || i == 6)) || (j == 4 && i == 3))) ||
                        (GUI.getController().getGameMode() == GameMode.TRIAL && !(i == 0 || i == 6 || ((j == 0 && (i != 3)) || (j == 1 && (i < 2 || i > 4)) || (j == 4 && i == 3))))) {
                    Button button = new Button();
                    button.setPrefHeight(80);
                    button.setPrefWidth(80);
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(event -> {
                        GUI.putTile(finalJ, finalI);
                    });
                    tilesTable.add(button, finalI, finalJ);
                }
            }
        }

        // Display tiles already placed on the shipboard
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if (GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].isPresent() &&
                        GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get().fillable()) {
                    tiles.add(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get());
                }
            }
        }

        // Display placed tiles on the board
        for (Tile tile : tiles) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setDisable(true);
                tilesTable.add(imageView, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }

        // Display turned tiles and allow player to refuse them
        int index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setOnMouseClicked(event -> {
                    refuse();
                });
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageView);
                } else {
                    drawnTiles2.getChildren().add(imageView);
                }
            }
            index++;
        }

        // Display booked tiles, allowing the player to book a tile
        for (int i = 0; i < 2; i++) {
            ImageView imageView = new ImageView();
            if (GUI.getController().getLightShipBoard().getBookedTiles().size() == i + 1) {
                Tile tile = GUI.getController().getLightShipBoard().getBookedTiles().get(i);
                String imagePath = tile.getImagePath();
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream == null) {
                    System.err.println("Impossibile trovare l'immagine: " + imagePath);
                } else {
                    Image image = new Image(imageStream);
                    imageView = new ImageView(image);
                    imageView.rotateProperty().setValue(tile.getRotation() * 90);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);
                }
            } else {
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setOnMouseClicked(event -> {
                    bookTile();
                });
            }
            bookedTiles.add(imageView, i, 0);
        }
    }

    /**
     * Handles key events for the drawn tile management screen.
     * Specifically listens for the "R" key to refuse the drawn tile.
     * @param event the key event that occurred
     */
    @FXML
    public void K(KeyEvent event) {
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if (event.getCode().equals(KeyCode.R)) {
                refuse();
            }
        }
    }

    /**
     * Refuses the currently drawn tile.
     * Calls the method in GUI to perform the refuse action.
     */
    @FXML
    public void refuse() {
        GUI.refuseTile();
    }

    /**
     * Books the current tile for placement.
     * Calls the method in GUI to perform the booking action.
     */
    @FXML
    public void bookTile() {
        GUI.bookTile();
    }

    /**
     * Rotates the drawn tile 90 degrees clockwise.
     * Calls the method in GUI to rotate the tile.
     */
    @FXML
    public void rotateDx() {
        drawnTile.rotateProperty().setValue(drawnTile.rotateProperty().getValue() + 90);
        GUI.rotate();
    }

    /**
     * Rotates the drawn tile 90 degrees counterclockwise (3 times).
     * Calls the method in GUI to rotate the tile.
     */
    @FXML
    public void rotateSx() {
        drawnTile.rotateProperty().setValue(drawnTile.rotateProperty().getValue() - 90);
        GUI.rotate();
        GUI.rotate();
        GUI.rotate();
    }

    /**
     * Updates the drawn tiles displayed in the UI.
     * Refreshes the tiles displayed in the drawn tiles panes.
     */
    public void update() {
        drawnTiles1.getChildren().clear();
        drawnTiles2.getChildren().clear();
        int index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageView);
                } else {
                    drawnTiles2.getChildren().add(imageView);
                }
            }
            index++;
        }
    }
}
