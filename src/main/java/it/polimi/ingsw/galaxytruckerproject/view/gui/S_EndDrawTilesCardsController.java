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
public class S_EndDrawTilesCardsController extends GUIControllers{

    /**
     * The ProgressBar UI control representing the first hourglass in the game.
     * This field is linked to a corresponding component in the FXML file and is used to display
     * the progress or state of a specific game timer or activity.
     *
     * This field is managed and potentially updated during certain game actions or events
     * within the controller.
     */
    @FXML
    public ProgressBar hourglass1;
    /**
     * Represents the second hourglass progress bar in the S_EndDrawTilesCardsController context.
     * It is an FXML-bound field that may indicate a time-based or progress-based element
     * within the user interface of the application.
     */
    @FXML
    public ProgressBar hourglass2;
    /**
     * An FXML-linked ProgressBar object representing the third hourglass component in the UI.
     * This variable may be used to visually display progress or timing information within
     * the S_EndDrawTilesCardsController class.
     */
    @FXML
    public ProgressBar hourglass3;

    /**
     * The flightImage variable represents a JavaFX Group object used in the
     * S_EndDrawTilesCardsController class. It is marked with the @FXML annotation,
     * indicating that it is linked to an element in the associated FXML file.
     * This field is intended for managing and interacting with graphical elements
     * representing flight-related visuals in the user interface.
     */
    @FXML
    public Group flightImage;

    /**
     * Represents the ImageView component for displaying the ship image in the UI.
     * This field is associated with the FXML layout and is used within the
     * S_EndDrawTilesCardsController class to manage and manipulate the ship's image.
     */
    @FXML
    public ImageView shipImage;

    /**
     * Represents a JavaFX Group node associated with the "trial flight" graphic or UI element.
     * This component is likely used within the user interface of the S_EndDrawTilesCardsController
     * to display or manage visual elements related to a trial flight functionality.
     * It is annotated with @FXML, indicating it is injected and controlled by the associated FXML file.
     */
    @FXML
    public Group trialFlight;

    /**
     * Represents the first {@code Polygon} element in the associated FXML file.
     * This variable is linked to the FXML declaration and can be manipulated
     * programmatically to represent a specific polygonal graphical object within
     * the user interface managed by the {@code S_EndDrawTilesCardsController} class.
     */
    @FXML
    public Polygon first;

    /**
     * Represents the second polygon element in the user interface.
     * This variable is linked to a corresponding Polygon component in the FXML file
     * and can be used for UI manipulations or updates as part of the controller logic.
     */
    @FXML
    public Polygon second;

    /**
     * Represents the third visual marker, modeled as a Polygon,
     * likely used to indicate a specific state or selection within the GUI.
     * This field is linked to an FXML component, and its behavior or visibility
     * may be manipulated by the controller methods during application runtime.
     */
    @FXML
    public Polygon third;

    /**
     * Represents the fourth polygon element in the UI, which can be manipulated or updated
     * during the program's operation. This field is linked to the corresponding component in the FXML file.
     */
    @FXML
    public Polygon fourth;

    /**
     * Represents a Polygon node associated with the first trial in the UI.
     * This field is linked to an FXML element defined in the corresponding layout file.
     * It may be used to display or manipulate the visual representation of
     * the first trial for the user.
     *
     * The specific functionality or behavior of this element is determined
     * by the controller methods and any associated event handlers.
     */
    @FXML
    public Polygon firstTrial;

    /**
     * A Polygon object initialized as `secondTrial` that may represent
     * graphical elements or tiles within the controller's functionality.
     * It is used within the FXML-defined user interface of the `S_EndDrawTilesCardsController` class.
     * This variable is likely tied to FXML elements in the associated layout file.
     */
    @FXML
    public Polygon secondTrial;

    /**
     * Represents the third visual trial element in the UI, designed as a Polygon.
     * This variable is annotated with @FXML, indicating that it is injected
     * by the JavaFX framework, typically corresponding to an element defined in
     * the FXML file of the controller.
     */
    @FXML
    public Polygon thirdTrial;

    /**
     * Represents the fourth trial polygon within the context of the user interface.
     * This variable is linked to an FXML element and typically used to render or manipulate
     * a graphical representation of the fourth trial in the application.
     */
    @FXML
    public Polygon fourthTrial;

    /**
     * Represents the graphical element for the first deck, displayed as an ImageView.
     * This field is part of the S_EndDrawTilesCardsController class and is directly associated
     * with the user interface (FXML). It can be used for rendering, updating, or interacting
     * with the visual depiction of the first deck in the application's UI.
     */
    @FXML
    public ImageView deck1;

    /**
     * A JavaFX ImageView component representing the second deck of cards or tiles
     * in the game interface. It is used to display the visual representation of
     * the second deck and can be manipulated or updated during gameplay.
     */
    @FXML
    public ImageView deck2;

    /**
     * Represents the third deck image within the UI, intended for display purposes in the
     * FXML layout file. This ImageView may be used to visually represent or interact with
     * the state of the third deck in the application's gameplay or interface logic.
     */
    @FXML
    public ImageView deck3;

    /**
     * Represents a GridPane used for displaying and organizing tiles in the GUI.
     * This variable is managed by JavaFX's @FXML annotation to link with the associated FXML layout file.
     * It is intended for use within the S_EndDrawTilesCardsController class to handle tile-related operations
     * such as drawing, booking, and visual representation.
     */
    @FXML
    public GridPane tilesTable;

    /**
     * Represents a JavaFX GridPane that displays or manages the tiles that are booked.
     * It is connected to the corresponding FXML file and is typically used in the
     * context of managing GUI components, where users can view or interact with booked tiles.
     *
     * This variable is annotated with @FXML, which signifies that it is injected by
     * the FXML loader based on an associated FXML file.
     */
    @FXML
    public GridPane bookedTiles;

    /**
     * Represents a TilePane that is used in the JavaFX application.
     * The drawnTiles1 variable is annotated with @FXML, indicating that it is
     * an element defined in the associated FXML file and is injected into the
     * controller at runtime.
     * This component manages a collection of tiles and is typically used
     * to visualize or interact with a set of drawn tiles in the user interface.
     */
    @FXML
    public TilePane drawnTiles1;

    /**
     * A TilePane element defined in the JavaFX FXML file.
     * This variable represents a container for arranging and displaying
     * multiple Tile objects in a grid-like layout.
     * It is annotated with @FXML, indicating that it is injected
     * and linked to a corresponding component in the FXML layout.
     */
    @FXML
    public TilePane drawnTiles2;

    /**
     * Handles the KeyEvent triggered during interaction with the UI.
     * If the "D" key is pressed, the method calls {@code drawTile()} to execute its functionality.
     *
     * @param event the KeyEvent triggered by user interaction that contains
     *              information such as the event type and the key code.
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
     * Initializes the UI elements and game state for the end draw tiles/cards phase.
     * This method is automatically invoked by the JavaFX framework when the associated
     * FXML file is loaded.
     *
     * The initialization includes:
     * - Setting up hourglasses, polygons, and visibility of images based on the current game mode.
     * - Configuring the visual appearance and colors of polygons corresponding to available positions.
     * - Clearing and reinitializing the tiles table based on the player's shipboard tiles.
     * - Displaying tiles available for drawing and handling tile click events.
     * - Showing booked tiles, if applicable, and configuring their click interactions.
     *
     * The method adapts the UI behavior dynamically depending on whether the game mode
     * is LEVEL2 or TRIAL, ensuring that the displayed elements and their functionalities
     * align with the corresponding game rules and state.
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
     * Draws a tile using the graphical user interface.
     *
     * This method is triggered via a JavaFX FXML action, initiating
     * the process of displaying or interacting with a tile in the UI.
     * It delegates the task to the GUI class to handle the drawing operation.
     */
    @FXML
    public void drawTile() {
        GUI.drawTile();
    }

    /**
     * Draws a booked tile on the GUI at the specified index.
     *
     * @param index the index of the booked tile to be drawn
     */
    @FXML
    public void drawBookedTile(int index) {
        GUI.drawBookedTile(index);
    }

    /**
     * Invokes the GUI's method to draw a tile that has already been drawn.
     *
     * @param index the index of the drawn tile to be displayed
     */
    @FXML
    public void drawDrawnTile(int index) {
        GUI.drawDrawnTile(index);
    }

    /**
     * Handles the action of drawing a card from deck 1.
     * This method is triggered via an associated GUI event and
     * delegates the action to the GUI utility method for drawing a card
     * specifically from deck 1.
     */
    @FXML
    public void drawDeck1() {
        GUI.drawDeck(1);
    }

    /**
     * Draws the second deck in the GUI.
     * This method is triggered via a JavaFX FXML event handler and invokes the GUI's
     * {@code drawDeck} method with the deck number 2 as the parameter.
     */
    @FXML
    public void drawDeck2() {
        GUI.drawDeck(2);
    }

    /**
     * Handles the action of drawing the third deck in the graphical user interface.
     * This method is triggered by a user interaction with the corresponding UI component.
     * It delegates the drawing process to the GUI class with an identifier for the third deck.
     */
    @FXML
    public void drawDeck3() {
        GUI.drawDeck(3);
    }

    @FXML
    public void autoBuild() {
        GUI.autoCreateShip(1);
    }

    public void autoBuild2() {
        GUI.autoCreateShip(2);
    }

    /**
     * Ends the current ship operation by invoking the endShip method within the GUI class.
     * This method is triggered in the application's user interface.
     */
    @FXML
    public void endShip() {
        GUI.endShip();
    }

    /**
     * Triggers the "turn hourglass" action in the GUI.
     * This method invokes the `turnHourglass` functionality of the graphical user interface (GUI).
     * Typically, this is used to represent flipping an hourglass visually within the application.
     *
     * This method is annotated with `@FXML`, indicating it is linked to a specific
     * user interface component or event handler in a JavaFX application.
     */
    @FXML
    public void turnHourglass() {
        GUI.turnHourglass();
    }

    /**
     * Updates the game UI elements based on the current state of the game controller.
     *
     * This method refreshes elements such as tiles, player indicators, and decks depending
     * on the game mode and the players currently in the game. It divides tiles into two groups
     * and updates both their appearance and event handlers for interaction. Visibility of
     * decks is also adjusted as required.
     *
     * The method handles two specific game modes:
     * - LEVEL2: Updates the properties for the player indicators and assigns colors
     *   based on the available positions in the controller.
     * - TRIAL: Performs similar updates to a different set of trial-specific
     *   indicators.
     *
     * Steps performed within the method:
     * - Clears previously drawn tiles.
     * - Updates player position-based indicators based on game mode and available positions.
     * - Conditionally updates deck visibility for non-TRIAL game modes.
     * - Refreshes the UI for drawn tiles, assigning appropriate images, rotation,
     *   and click event handlers for further interactions.
     */
    public void updateEDTC() {
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
     * Updates the progress bars and percentages for different hourglass components based on the current index, maximum value, and turn number.
     *
     * @param index the current index value, typically representing progress.
     * @param max the maximum value, used to calculate the progress percentage.
     * @param turns indicates which set of progress bars to update (1, 2, or 3).
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
