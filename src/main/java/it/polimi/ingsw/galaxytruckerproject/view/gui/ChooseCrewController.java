package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class ChooseCrewController {
    /**
     * Represents a Polygon shape used in the user interface of the ChooseCrewController class.
     * It is defined as a FXML element, likely linked to a corresponding element in the FXML layout file.
     * The purpose of this polygon is to serve as a graphical or interactive component within the UI.
     */
    @FXML
    public Polygon p0;
    /**
     * Represents a Polygon element in the ChooseCrewController.
     * This field is associated with the FXML layout and is typically
     * used for rendering or interaction within the user interface.
     */
    @FXML
    public Polygon p1;
    /**
     * Represents a Polygon element in the ChooseCrewController class.
     * This field is annotated with @FXML, indicating its use in an FXML file
     * to define graphical components associated with the application's user interface.
     * The p2 polygon is likely used for defining a specific shape or section
     * in the graphical layout.
     */
    @FXML
    public Polygon p2;
    /**
     * Represents the Polygon node annotated with FXML, most likely used within the ChooseCrewController class
     * as part of its UI structure. It may correspond to a visual element within the associated FXML file.
     */
    @FXML
    public Polygon p3;
    /**
     * Represents a polygon object defined in the FXML file, which is part of the graphical user interface
     * in the ChooseCrewController class. This polygon can be used for creating or modifying shapes within
     * the user interface.
     *
     * It is annotated with @FXML for interaction with the JavaFX framework, allowing this field to be injected
     * and managed by the FXMLLoader.
     */
    @FXML
    public Polygon p4;
    /**
     * Represents the fifth polygon shape in the UI managed by the ChooseCrewController class.
     * This variable is linked to a Polygon element in the FXML file and is used for UI manipulation
     * or interaction within the controller.
     */
    @FXML
    public Polygon p5;
    /**
     * Represents the sixth Polygon component in the context of the ChooseCrewController.
     * This is a JavaFX element that might be used for graphical representation
     * or interaction within the application's user interface.
     *
     * It is annotated with @FXML, indicating it is injected by the JavaFX framework
     * and defined in the FXML file associated with the ChooseCrewController.
     */
    @FXML
    public Polygon p6;
    /**
     * The `p7` field represents a polygon in the user interface,
     * likely defined in the corresponding FXML file.
     *
     * It is annotated with `@FXML`, indicating it is injected
     * by the FXML loader at runtime. This polygon may be used
     * within the graphical representation or functionality of
     * the ChooseCrewController class.
     */
    @FXML
    public Polygon p7;
    /**
     * A Polygon element defined in the FXML file, representing one of the selectable or interactive components
     * within the ChooseCrewController interface. This variable is connected to the FXML layout for proper
     * initialization and interaction within the controller.
     */
    @FXML
    public Polygon p8;
    /**
     * The Polygon object representing a customizable geometric shape in the UI
     * of the ChooseCrewController. It is likely used to display or interact with
     * graphical elements specific to crew selection.
     *
     * This field is annotated with @FXML, indicating that it is linked to an
     * element defined in the associated FXML file.
     */
    @FXML
    public Polygon p9;
    /**
     * Represents a polygon shape within the ChooseCrewController context,
     * specifically associated with the user interface. The naming convention
     * suggests it is the tenth polygon element (p10) in a sequence of polygon
     * objects. It is referenced via the FXML file associated with the controller.
     */
    @FXML
    public Polygon p10;
    /**
     * The p11 variable represents a Polygon element in the ChooseCrewController.
     * It is defined as an FXML field, and its value is typically injected by the JavaFX framework
     * during the loading of the associated FXML file.
     *
     * This field might be used for graphical representation or interaction within the application's
     * user interface, specifically for crew selection or visual feedback.
     */
    @FXML
    public Polygon p11;
    /**
     * The p12 variable represents a user interface element of type {@link Polygon}.
     * It is defined within the ChooseCrewController class and is annotated with {@code @FXML},
     * indicating that it is linked with an element defined in an FXML file.
     * This variable is typically used to represent a specific polygon shape
     * in the UI that may be visually or programmatically manipulated during the application runtime.
     */
    @FXML
    public Polygon p12;
    /**
     * Represents the 14th polygon element in the UI, managed by the JavaFX framework.
     * This polygon may be used for graphical representation or user interaction within the
     * ChooseCrewController class.
     *
     * It is annotated with @FXML, indicating that this variable is injected and
     * referenced from an associated FXML file.
     */
    @FXML
    public Polygon p13;
    /**
     * Represents the Polygon shape with identifier `p14` used within the graphical user interface
     * of the ChooseCrewController. Typically linked to FXML files for UI representation and manipulation.
     */
    @FXML
    public Polygon p14;
    /**
     * Represents an FXML-injected Polygon object, identified as p15, within the ChooseCrewController.
     * This variable is designed to be used within the JavaFX application as part of the controller's
     * graphical structure, potentially serving as a visual element such as a shape that forms part of
     * the application UI.
     *
     * It is linked to an FXML definition and is managed by the JavaFX framework during runtime.
     */
    @FXML
    public Polygon p15;
    /**
     * Represents a specific polygon shape used within the ChooseCrewController.
     * This field is linked to the corresponding FXML component and can be manipulated
     * programmatically to modify its properties or behavior in the user interface.
     *
     * The exact purpose or usage of this polygon (p16) may relate to a visual representation
     * or user interaction element defined in the associated FXML layout.
     */
    @FXML
    public Polygon p16;
    /**
     * Represents the 18th polygon element (p17) used in the ChooseCrewController class.
     * This graphical object is likely part of the user interface, possibly designating
     * a specific crew member, role, or zone within the visual layout of the application.
     *
     * This field is marked with the @FXML annotation, indicating it is injected by the
     * JavaFX FXMLLoader during runtime and corresponds to an element defined in the FXML
     * layout of the corresponding scene.
     */
    @FXML
    public Polygon p17;
    /**
     * The Polygon instance representing the GUI element identified as p18.
     * This variable is linked to the corresponding FXML element in the
     * ChooseCrewController class, and it is used to define and manipulate
     * polygonal shapes in the user interface.
     *
     * Decorated with @FXML to enable interaction between the FXML file and
     * the controller class.
     */
    @FXML
    public Polygon p18;
    /**
     * Represents a specific polygon shape used in the ChooseCrewController.
     * The purpose of this polygon within the scene or its functional role
     * in the controller is determined by the application logic.
     *
     * This field is annotated with @FXML, indicating it is linked to an
     * element defined in the corresponding FXML file for ChooseCrewController
     * and is injected by the JavaFX framework.
     */
    @FXML
    public Polygon p19;
    /**
     * Represents a Polygon element in the JavaFX interface managed by the ChooseCrewController.
     * This polygon, identified as "p20", may be used for graphical representation or user interaction
     * within the controller's GUI. Details of its specific usage or behavior can be defined in the
     * controller's methods or the associated FXML file.
     *
     * This variable is annotated with @FXML to indicate that it is linked with an FXML element,
     * allowing it to be injected and manipulated within the controller.
     */
    @FXML
    public Polygon p20;
    /**
     * Represents the polygon shape labeled as "p21" in the UI.
     * This field is linked to an FXML component, and it is used to manage
     * or manipulate the corresponding polygon element in the scene.
     */
    @FXML
    public Polygon p21;
    /**
     * Represents the Polygon object labeled as p22 in the ChooseCrewController.
     * This variable is intended to be used within the FXML context, typically for graphical or interactive
     * elements within the application's user interface.
     */
    @FXML
    public Polygon p22;
    /**
     * Represents a specific polygon element in the user interface of the ChooseCrewController.
     * The polygon may be used for graphical display purposes and is initialized using
     * JavaFX's FXML framework.
     */
    @FXML
    public Polygon p23;
    /**
     * The pt0 variable is a Polygon element defined within the ChooseCrewController class.
     * It is annotated with @FXML, indicating it is linked to an element in the associated FXML file.
     * This variable is likely used to represent a specific graphical shape or component in the user interface
     * and may be controlled or modified during the application's execution.
     */
    @FXML
    public Polygon pt0;
    /**
     * Represents the polygon shape used as a graphical component in the ChooseCrewController.
     * This field is annotated with @FXML, indicating it is linked to an element in the FXML file.
     * It is intended for use in the UI as part of the crew selection or related functionality.
     */
    @FXML
    public Polygon pt1;
    /**
     * A Polygon represented by the variable pt2, defined within the ChooseCrewController class.
     * This variable is annotated with @FXML, indicating that it corresponds to a UI element
     * in the associated FXML file for the controller.
     *
     * It is likely used to represent or manipulate a specific polygonal graphical element in the UI.
     */
    @FXML
    public Polygon pt2;
    /**
     * Represents the third selectable polygon shape within the user interface of
     * the ChooseCrewController. This polygon may be used to visually indicate or
     * select a specific option related to crew selection or assignment.
     *
     * It is marked with the @FXML annotation, indicating that it is linked to a
     * corresponding component in the FXML layout file.
     */
    @FXML
    public Polygon pt3;
    /**
     * The pt4 field represents a Polygon element in the FXML file associated with this controller.
     * It is used to define and manipulate a polygon shape within the application's user interface.
     * This field is populated by the JavaFX framework at runtime based on the defined FXML structure.
     */
    @FXML
    public Polygon pt4;
    /**
     * The pt5 field represents a Polygon element in the corresponding FXML file.
     * It is annotated with @FXML, indicating it is injected by the JavaFX framework.
     * This Polygon is likely used as a graphical component within the UI managed
     * by the ChooseCrewController class.
     */
    @FXML
    public Polygon pt5;
    /**
     * The Polygon pt6 represents one of the interactive graphical elements
     * in the ChooseCrewController class. It is annotated with @FXML,
     * indicating that it is injected from an FXML file and is part
     * of the JavaFX application interface.
     */
    @FXML
    public Polygon pt6;
    /**
     * Represents a specific polygonal graphical element within the user interface
     * of the ChooseCrewController. This variable is linked to an FXML component
     * and is typically utilized to represent or interact with a specific part of
     * the UI layout.
     *
     * The name pt7 suggests that the polygon may be part of a series or group of
     * similar elements (e.g., pt0, pt1, ..., ptN), potentially used for visualizing
     * or selecting specific parts or options in the UI.
     *
     * This field is annotated with @FXML, indicating that it is injected or
     * initialized by the JavaFX framework during the loading process of the
     * corresponding FXML layout.
     */
    @FXML
    public Polygon pt7;
    /**
     * Represents the eighth polygon (pt8) in the ChooseCrewController class.
     *
     * This field is linked to the corresponding UI element through the use of the {@code @FXML} annotation.
     * It is used to define and manipulate a specific polygon shape as part of the user interface.
     */
    @FXML
    public Polygon pt8;
    /**
     * The pt9 variable represents a Polygon shape component in the ChooseCrewController class.
     * It is annotated with @FXML, indicating that it is defined in an associated FXML file
     * and is injected during the loading of the FXML layout.
     *
     * The pt9 Polygon can be used for graphical representation or user interaction
     * within the scene defined by the corresponding FXML file.
     *
     * This variable is expected to be manipulated or referenced in the controller
     * to implement specific functionality related to the application's GUI behavior.
     */
    @FXML
    public Polygon pt9;
    /**
     * The pt10 variable is an FXML-injected Polygon object representing
     * one of the visual elements in the ChooseCrewController class.
     * It is used to define and manage the graphical representation of a specific
     * polygon on the user interface, which is a part of the crew selection process.
     */
    @FXML
    public Polygon pt10;
    /**
     * Represents the Polygon shape associated with the identifier "pt11" in the UI layout.
     * Annotated with @FXML to denote its usage in JavaFX as a reference to a corresponding UI element
     * defined in an FXML file.
     * Can be used for graphical representation or user interaction within the application.
     */
    @FXML
    public Polygon pt11;
    /**
     * Represents the twelfth polygon element within the ChooseCrewController class.
     * It is annotated with @FXML, indicating that it is linked to an element
     * in the corresponding FXML file to allow interaction between the FXML UI and controller logic.
     * Typically used for graphical representation or interactive components on the user interface.
     */
    @FXML
    public Polygon pt12;
    /**
     * The {@code pt13} field represents a Polygon instance associated with the
     * {@code ChooseCrewController} class. It is marked with the {@code @FXML} annotation,
     * indicating its linkage to an FXML file for UI representation or interaction.
     *
     * This field is expected to be initialized via FXML and is used to define or manage
     * graphical elements within the user interface, such as shapes or regions of a scene.
     *
     * The exact purpose and role of {@code pt13} within the application depend on the specific
     * FXML file and corresponding controller logic it is bound to.
     */
    @FXML
    public Polygon pt13;
    /**
     * Represents a Polygon shape (pt14) in the ChooseCrewController class.
     * This field is annotated with @FXML, indicating it is associated with a
     * UI element in the corresponding FXML file. It is likely used as a graphical
     * element within the user interface to represent or interact with the crew selection process.
     */
    @FXML
    public Polygon pt14;
    /**
     * Represents a specific polygon within the ChooseCrewController context.
     * This polygon might be used as part of the graphical user interface
     * to represent a visual element or a selectable component related to
     * the crew selection process in the application.
     */
    @FXML
    public Polygon pt15;
    /**
     * A UI component represented as a Polygon in the associated FXML file.
     * The `pt16` field is linked via the `@FXML` annotation for interaction within the FXML controller.
     *
     * This field is part of the ChooseCrewController and may be used to visually represent
     * or interact with specific elements on the application's user interface.
     */
    @FXML
    public Polygon pt16;
    /**
     * Represents the Polygon component identified as pt17 in the user interface.
     * This element is connected to the FXML layout and can be manipulated or accessed
     * for graphical representation or other functionalities in the context of
     * ChooseCrewController's operations.
     */
    @FXML
    public Polygon pt17;

    /**
     * Represents a JavaFX Group node that visually depicts the flight image in the application's user interface.
     * It is declared as an FXML variable to be linked with the corresponding element in the FXML layout file.
     * Typically associated with managing and displaying graphical content related to the flight in the application.
     */
    @FXML
    public Group flightImage;

    /**
     * Represents the ImageView component used to display the ship's image
     * in the ChooseCrewController class. This field is injected via
     * the JavaFX FXML loader.
     */
    @FXML
    public ImageView shipImage;

    /**
     * The trialFlight variable represents a JavaFX Group component
     * that is part of the ChooseCrewController. It is used within
     * the context of the Trial Flight feature in the GUI.
     *
     * This field is annotated with @FXML, indicating that it is
     * defined in the associated FXML file and injected at runtime.
     * It serves as a container for child nodes, forming part of the
     * user interface for the controller.
     */
    @FXML
    public Group trialFlight;
    /**
     * The GridPane structure used to represent and manage the layout of tiles displayed
     * in the user interface. This field is annotated with @FXML, signifying it is linked
     * to an FXML-defined element for interaction within the controller.
     *
     * This member belongs to the ChooseCrewController and is essential in enabling
     * interactive visual representation and arrangement of specific content related to
     * tiles in the application's interface.
     */
    @FXML
    public GridPane tilesTable;

    /**
     * A VBox component defined in the FXML file, which is linked to this controller.
     * This variable represents a vertical layout container within the associated UI,
     * typically used to organize and display other graphical elements in a stacked manner.
     * It is marked with the @FXML annotation to enable interaction between the FXML file
     * and this controller.
     */
    @FXML
    public VBox box;

    /**
     * A JavaFX Label element marked with the @FXML annotation, indicating
     * that it is injected from the associated FXML file. This Label typically
     * represents a text component in the user interface which can display
     * static or dynamic text.
     */
    @FXML
    public Label text;

    /**
     * Represents a Button in the user interface labeled or styled to
     * denote the color "white". This button is likely associated with
     * functionality or actions related to the concept of "white" in the
     * application's context.
     *
     * This field is annotated with @FXML to indicate its connection to
     * an FXML file, allowing the JavaFX framework to inject it during
     * runtime.
     */
    @FXML
    public Button white;
    /**
     * The {@code purple} variable represents a Button in the JavaFX user interface
     * that is linked to the FXML file. This Button can be used to handle user interactions
     * or trigger specific actions within the application when clicked or interacted with.
     *
     * This variable is annotated with {@code @FXML}, indicating that it is accessible
     * and managed by the FXML loader at runtime.
     */
    @FXML
    public Button purple;
    /**
     * Represents the "brown" button in the ChooseCrewController.
     * This UI element is annotated with @FXML, indicating it is defined in the
     * corresponding FXML file and injected during the user interface's initialization.
     * The button may be used to trigger specific actions or represent a selectable option
     * in the context of crew selection.
     */
    @FXML
    public Button brown;

    /**
     * Represents an ImageView element in the ChooseCrewController associated with the deck.
     * This field is used within the FXML file to reference and manipulate the corresponding UI component.
     * It allows graphical representation or dynamic updates related to the deck visual in the user interface.
     */
    @FXML
    public ImageView deck;

    /**
     * Represents a collection of cabin coordinates on a ship or flight board.
     * Each {@link Coordinates} instance in the list specifies a cabin's position
     * through its x (row) and y (column) values.
     *
     * This list is utilized to track and manage the available or assigned cabin locations.
     */
    private ArrayList<Coordinates> cabins;

    /**
     * Represents the current index used to track or manage a specific state
     * or position within the ChooseCrewController class.
     * This variable may be used internally to determine the current selection
     * or progress in a particular workflow.
     */
    private int index=0;

    /**
     * Initializes the GUI elements of the ChooseCrewController class.
     * This method is automatically invoked when the associated FXML file is loaded.
     *
     * The initialization involves configuring visual components and their behaviors
     * based on the current game mode, as managed by the controller. It also enables
     * the setup of the flight board, ship board, and cabin management.
     *
     * Key operations performed include:
     * - Disabling the deck control initially.
     * - Configuring display elements, such as images and visibility, based on game modes (LEVEL2 or TRIAL).
     * - Loading and assigning appropriate images to the ship and deck components.
     * - Initializing other related components for gameplay setup.
     */
    @FXML
    public void initialize() {
        deck.setDisable(true);
        Polygon[] polygons = new Polygon[]{p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23};
        int max = 24;
        if (GUI.getController().getGameMode() == GameMode.LEVEL2) {
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            deck.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cards/GT-cards_I_IT_0121.jpg"))));
            flightImage.setVisible(false);
            trialFlight.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }
        flightBoard();
        shipBoard();
        cabins=GUI.getController().getCabinsManager().getCabins();
        focus();
    }

    /**
     * Updates the flight board's graphical representation based on the current game mode and players' positions.
     *
     * This method modifies the appearance of polygons representing tiles on the flight board to reflect
     * player positions and highlights them with specific colors based on their assigned player color.
     * If the game is in trial mode, it adjusts for a different maximum number of positions and uses a different set of polygons.
     *
     * The method loops through all tiles, setting their appearance using stroke, fill color, stroke width,
     * and shadow effect. It then checks each player's position on the flight board and updates the respective tile's
     * fill color to match the player's assigned color (Red, Yellow, Green, or Blue).
     *
     * Note: The polygons used to represent tiles and the maximum positions are determined based on the game mode.
     * In standard mode, all 24 positions are used, while in trial mode, only the first 18 are relevant.
     */
    private void flightBoard(){
        int max=24;
        Polygon[] polygons = new Polygon[]{p0,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23};
        if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            polygons=new Polygon[]{pt0,pt1,pt2,pt3,pt4,pt5,pt6,pt7,pt8,pt9,pt10,pt11,pt12,pt13,pt14,pt15,pt16,pt17};
            max=18;
        }
        int index=0;
        for(Polygon p:polygons){
            p.setStroke(rgb(255, 169, 19));
            p.setFill(Color.TRANSPARENT);
            p.setStrokeWidth(5);
            p.setEffect(new DropShadow(BlurType.GAUSSIAN,rgb(255, 169, 19), 30, 0.4, 0, 0));
            for (LightPlayer player : GUI.getController().getFlightBoard().getInGamePlayers()) {
                int position=player.getPosition();
                while (position>max)
                    position-=max;
                if (index==player.getPosition()) {
                    if (player.getPlayerColor().equals(PlayersColor.RED))
                        p.setFill(Color.RED);
                    if (player.getPlayerColor().equals(PlayersColor.YELLOW))
                        p.setFill(Color.YELLOW);
                    if (player.getPlayerColor().equals(PlayersColor.GREEN))
                        p.setFill(Color.GREEN);
                    if (player.getPlayerColor().equals(PlayersColor.BLUE))
                        p.setFill(Color.BLUE);
                }
            }
            index++;
        }
    }

    /**
     * Populates the tilesTable with graphical representations of tiles from the shipBoard.
     * This method retrieves only the tiles that are present and fillable, based on the player's
     * current ship board, and adds them as images to the tilesTable UI component. The method
     * clears the tilesTable before populating it with new data. Each tile's image is displayed
     * with the appropriate rotation, dimensions, and coordinates.
     *
     * The process includes obtaining the tiles from the ship board, loading their associated
     * image resources, and placing them at the specified (X, Y) positions on the tilesTable.
     * Any missing image resources are logged to the console.
     */
    private void shipBoard(){
        tilesTable.getChildren().clear();
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
    }
    /**
     * Handles the focus logic for interacting with cabins on the ship board based on the crew type
     * and updates the GUI accordingly. It determines the crew type and enables or disables controls.
     * If the index is within the bounds of available cabins, it processes the corresponding tile.
     * Otherwise, it invokes waiting logic.
     *
     * The method ultimately fetches a tile image associated with the current cabin and updates
     * its properties such as rotation, dimensions, and position in the GUI's tile table grid.
     * If the image path for the tile is invalid, an error message is printed to the standard error stream.
     *
     * Key functional areas include:
     * - Enabling/disabling controls based on crew type.
     * - Handling cabin interactions and validating cabin indices.
     * - Loading and rendering tile images for the ship board in the GUI.
     */
    public void focus(){
        shipBoard();
        index=GUI.getController().getCabinsManager().getIndex();
        if(index<cabins.size()) {
            switch (GUI.getController().getCabinsManager().crewType()) {
                case NO -> {
                    white.setDisable(false);
                    purple.setDisable(true);
                    brown.setDisable(true);
                }
                case PURPLE -> {
                    white.setDisable(false);
                    purple.setDisable(false);
                    brown.setDisable(true);
                }
                case BROWN -> {
                    white.setDisable(false);
                    purple.setDisable(true);
                    brown.setDisable(false);
                }
                case BOTH -> {
                    white.setDisable(false);
                    purple.setDisable(false);
                    brown.setDisable(false);
                }
            }
        }else {
            waitOthers();
            return;
        }
        Tile tile=GUI.getController().getMe().getShipBoard().getTilesTable()[cabins.get(index).getX()][cabins.get(index).getY()].get();
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
            imageView.setDisable(false);
            tilesTable.add(imageView, tile.getCoordinates().getY(), tile.getCoordinates().getX());
        }
    }
    /**
     * Updates the cabin type to human for the GUI and adjusts the focus if applicable.
     *
     * This method is triggered by the corresponding user action in the GUI.
     * It sets the cabin type to "HUMAN" within the system configuration and
     * ensures that the user interface element is focused if the index variable
     * is within the bounds of the cabins list.
     */
    @FXML
    public void human(){
        GUI.setCabin(CrewType.HUMAN);
        if(index<cabins.size())
            focus();
    }
    /**
     * Handles the action for setting a specific cabin type and performing focus actions if certain conditions are met.
     *
     * This method is triggered via an FXML event. When invoked, it updates the cabin type in the GUI to "PURPLE"
     * and checks whether the current index is within the bounds of the list of cabins. If the condition is satisfied,
     * it invokes the `focus` method to perform any additional required operations.
     */
    @FXML
    public void pAlien(){
        GUI.setCabin(CrewType.PURPLE);
        if(index<cabins.size())
            focus();
    }
    /**
     * Handles the action for the "bAlien" event. This method sets the current cabin to one of type "BROWN"
     * using the GUI's setCabin method. If the index is less than the size of the cabins list, it invokes
     * the focus method to perform additional operations such as updating or focusing on the element.
     */
    @FXML
    public void bAlien(){
        GUI.setCabin(CrewType.BROWN);
        if(index<cabins.size())
            focus();
    }
    /**
     * Clears the current content of the container and displays a message
     * indicating that the system is waiting for other players to proceed.
     *
     * This method modifies the user interface by clearing all the children
     * of the box container and setting new text to a specific text display.
     * It is likely called in scenarios where synchronous actions are required
     * between multiple players or participants.
     */
    private void waitOthers(){
        box.getChildren().clear();
        text.setText("Waiting for other players...");
    }
}
