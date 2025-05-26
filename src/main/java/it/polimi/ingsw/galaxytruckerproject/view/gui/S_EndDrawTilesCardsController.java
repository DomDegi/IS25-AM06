package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

/**
 * Controller for managing the end of the tile drawing phase and card deck in the Galaxy Trucker game's GUI.
 * <p>
 * This controller is responsible for displaying and updating the various UI elements related to the tile drawing phase, including:
 * the progress bars (hourglasses), the ship image, the tiles and their placement, and the deck status.
 * </p>
 */
public class S_EndDrawTilesCardsController {

    @FXML
    public ProgressBar hourglass1;
    @FXML
    public ProgressBar hourglass2;
    @FXML
    public ProgressBar hourglass3;

    @FXML
    public Group flightImage;

    @FXML
    public ImageView shipImage;

    @FXML
    public Group trialFlight;

    @FXML
    public Polygon first;

    @FXML
    public Polygon second;

    @FXML
    public Polygon third;

    @FXML
    public Polygon fourth;

    @FXML
    public Polygon firstTrial;

    @FXML
    public Polygon secondTrial;

    @FXML
    public Polygon thirdTrial;

    @FXML
    public Polygon fourthTrial;

    @FXML
    public ImageView deck1;

    @FXML
    public ImageView deck2;

    @FXML
    public ImageView deck3;

    @FXML
    public GridPane tilesTable;

    @FXML
    public GridPane bookedTiles;

    @FXML
    public TilePane drawnTiles1;

    @FXML
    public TilePane drawnTiles2;

    /**
     * Handles key events for the end-of-phase screen.
     * Specifically listens for the "D" key to draw a tile.
     * @param event the key event that occurred
     */
    @FXML
    public void K(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.D)){
                drawTile();
            }
        }
    }

    /**
     * Initializes the UI for the end of the tile drawing phase.
     * This includes setting up the hourglasses, ship image, flight polygons, deck images, and tile placements based on the game mode.
     */
    @FXML
    public void initialize() {
        Polygon[] polygons = new Polygon[]{first, second, third, fourth};
        if (GUI.getController().getGameMode() == GameMode.LEVEL2) {
            hourglass1.setProgress(1 - GUI.getPercentage1());
            hourglass2.setProgress(1 - GUI.getPercentage2());
            hourglass3.setProgress(1 - GUI.getPercentage3());
            flightImage.setVisible(true);
            trialFlight.setVisible(false);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            polygons = new Polygon[]{firstTrial, secondTrial, thirdTrial, fourthTrial};
            flightImage.setVisible(false);
            trialFlight.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }

        int index = 1;
        for (Polygon p : polygons) {
            if (index > GUI.getController().getFlightBoard().getInGamePlayers().size())
                p.setVisible(false);
            p.setStroke(rgb(255, 169, 19));
            p.setFill(Color.TRANSPARENT);
            p.setStrokeWidth(5);
            p.setEffect(new DropShadow(BlurType.GAUSSIAN, rgb(255, 169, 19), 30, 0.4, 0, 0));
            if (GUI.getController().getAvailablePosition().get(index) != null) {
                switch (GUI.getController().getAvailablePosition().get(index)) {
                    case RED:
                        p.setFill(Color.RED);
                        break;
                    case YELLOW:
                        p.setFill(Color.YELLOW);
                        break;
                    case GREEN:
                        p.setFill(Color.GREEN);
                        break;
                    case BLUE:
                        p.setFill(Color.BLUE);
                        break;
                }
            }
            index++;
        }

        // Initialize the tiles table and deck visibility
        tilesTable.getChildren().clear();
        deck1.setVisible(GUI.getController().getAvailableDeck().get(1));
        deck2.setVisible(GUI.getController().getAvailableDeck().get(2));
        deck3.setVisible(GUI.getController().getAvailableDeck().get(3));

        // Set up the tiles on the player's shipboard
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if (GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].isPresent() && GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get().fillable())
                    tiles.add(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get());
            }
        }
        for (Tile tile : tiles) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setDisable(true);
                tilesTable.add(imageView, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }

        // Display the turned tiles for drawing
        index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setOnMouseClicked(event -> {
                    drawDrawnTile(tile.getKey());
                });
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageView);
                } else {
                    drawnTiles2.getChildren().add(imageView);
                }
                index++;
            }
        }

        // Display booked tiles, if applicable
        if (GUI.getController().getGameMode() != GameMode.TRIAL) {
            index = 0;
            for (Tile tile : GUI.getController().getLightShipBoard().getBookedTiles()) {
                String imagePath = tile.getImagePath();
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    Image image = new Image(imageStream);
                    ImageView imageView = new ImageView(image);
                    imageView.rotateProperty().setValue(tile.getRotation() * 90);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);
                    final int finalI = index;
                    imageView.setOnMouseClicked(event -> {
                        drawBookedTile(finalI);
                    });
                    bookedTiles.add(imageView, index, 0);
                }
                index++;
            }
        }
    }

    /**
     * Draws a tile from the deck.
     */
    @FXML
    public void drawTile() {
        GUI.drawTile();
    }

    /**
     * Draws a booked tile based on its index.
     * @param index the index of the booked tile
     */
    @FXML
    public void drawBookedTile(int index) {
        GUI.drawBookedTile(index);
    }

    /**
     * Draws a tile from the drawn tiles based on its index.
     * @param index the index of the drawn tile
     */
    @FXML
    public void drawDrawnTile(int index) {
        GUI.drawDrawnTile(index);
    }

    /**
     * Draws a tile from deck 1.
     */
    @FXML
    public void drawDeck1() {
        GUI.drawDeck(1);
    }

    /**
     * Draws a tile from deck 2.
     */
    @FXML
    public void drawDeck2() {
        GUI.drawDeck(2);
    }

    /**
     * Draws a tile from deck 3.
     */
    @FXML
    public void drawDeck3() {
        GUI.drawDeck(3);
    }

    /**
     * Ends the ship construction phase.
     */
    @FXML
    public void endShip() {
        GUI.endShip();
    }

    /**
     * Turns the hourglass for the game timer.
     */
    @FXML
    public void turnHourglass() {
        GUI.turnHourglass();
    }

    /**
     * Updates the drawn tiles UI and progress indicators.
     */
    public void update() {
        drawnTiles1.getChildren().clear();
        drawnTiles2.getChildren().clear();
        int index = 1;
        if (GUI.getController().getGameMode() == GameMode.LEVEL2) {
            for (Polygon p : new Polygon[]{first, second, third, fourth}) {
                if (index > GUI.getController().getFlightBoard().getInGamePlayers().size())
                    p.setVisible(false);
                p.setStroke(rgb(255, 169, 19));
                p.setFill(Color.TRANSPARENT);
                p.setStrokeWidth(5);
                p.setEffect(new DropShadow(BlurType.GAUSSIAN, rgb(255, 169, 19), 30, 0.4, 0, 0));
                if (GUI.getController().getAvailablePosition().get(index) != null) {
                    switch (GUI.getController().getAvailablePosition().get(index)) {
                        case RED:
                            p.setFill(Color.RED);
                            break;
                        case YELLOW:
                            p.setFill(Color.YELLOW);
                            break;
                        case GREEN:
                            p.setFill(Color.GREEN);
                            break;
                        case BLUE:
                            p.setFill(Color.BLUE);
                            break;
                    }
                }
                index++;
            }
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            for (Polygon p : new Polygon[]{firstTrial, secondTrial, thirdTrial, fourthTrial}) {
                if (index > GUI.getController().getFlightBoard().getInGamePlayers().size())
                    p.setVisible(false);
                p.setStroke(rgb(255, 169, 19));
                p.setFill(Color.TRANSPARENT);
                p.setStrokeWidth(5);
                p.setEffect(new DropShadow(BlurType.GAUSSIAN, rgb(255, 169, 19), 30, 0.4, 0, 0));
                if (GUI.getController().getAvailablePosition().get(index) != null) {
                    switch (GUI.getController().getAvailablePosition().get(index)) {
                        case RED:
                            p.setFill(Color.RED);
                            break;
                        case YELLOW:
                            p.setFill(Color.YELLOW);
                            break;
                        case GREEN:
                            p.setFill(Color.GREEN);
                            break;
                        case BLUE:
                            p.setFill(Color.BLUE);
                            break;
                    }
                }
                index++;
            }
        }

        // Update deck visibility
        if (GUI.getController().getGameMode() != GameMode.TRIAL) {
            deck1.setVisible(GUI.getController().getAvailableDeck().get(1));
            deck2.setVisible(GUI.getController().getAvailableDeck().get(2));
            deck3.setVisible(GUI.getController().getAvailableDeck().get(3));
        }

        // Update the drawn tiles UI
        index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setOnMouseClicked(event -> {
                    drawDrawnTile(tile.getKey());
                });
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageView);
                } else {
                    drawnTiles2.getChildren().add(imageView);
                }
                index++;
            }
        }
    }

    /**
     * Updates the progress bar for the game timer.
     * @param index the current index of the progress
     * @param max the maximum progress
     * @param turns the turn number (1, 2, or 3)
     */
    @FXML
    public void goProgressBar(int index, int max, int turns) {
        if (turns == 1) {
            GUI.setPercentage1((double) index / max);
            hourglass3.setProgress(1);
            hourglass2.setProgress(1);
            hourglass1.setProgress(1 - GUI.getPercentage1());
        } else if (turns == 2) {
            GUI.setPercentage2((double) index / max);
            hourglass3.setProgress(1);
            hourglass2.setProgress(1 - GUI.getPercentage2());
            hourglass1.setProgress(0);
        } else if (turns == 3) {
            GUI.setPercentage3((double) index / max);
            hourglass3.setProgress(1 - GUI.getPercentage3());
            hourglass2.setProgress(0);
            hourglass1.setProgress(0);
        }
    }
}
