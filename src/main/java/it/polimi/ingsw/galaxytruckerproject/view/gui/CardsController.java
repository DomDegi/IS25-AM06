package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Planet;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.galaxytruckerproject.client.CoordReqType.*;
import static javafx.scene.paint.Color.*;

/**
 * The CardsController class represents the controller in a JavaFX application responsible
 * for managing card-related functionalities in a game.
 * <p>
 * This class interacts with the UI elements and handles various card-related actions such
 * as drawing cards, rolling dice, selecting actions, choosing planets, landing, and
 * managing game states. It also provides methods for updating the game interface and
 * displaying relevant game information.
 * <p>
 * Fields:
 * - Contains various UI elements such as card decks, images, buttons, text areas, and tables
 *   used to interact with the game interface.
 * - Includes variables for tracking visible states, rolled dice, planetary choices, and player actions.
 * <p>
 * Methods:
 * - Public Methods:
 *   - initialize(): Initializes the controller and prepares the UI for interaction.
 *   - draw(): Handles the action of drawing a card.
 *   - yes(): Processes a positive action response.
 *   - no(): Processes a negative action response.
 *   - roll(): Manages the dice rolling action.
 *   - doneCoord(): Concludes coordinate selection actions.
 *   - choosePlanet(int index): Handles the selection of a planet by index.
 *   - land(): Handles the action of landing on a planet.
 *   - showRoll(int index): Displays information related to a dice roll for a specific player.
 *   - chooseGood(int index): Allows selection of goods by index.

 * - Private Methods:
 *   - waitOthers(): Waits for other players' actions to finish.
 *   - showCard(): Displays the card currently interacted with.
 *   - loadImage(String path): Loads an image from a given file path.
 *   - modeChange(): Changes the UI or application mode.
 *   - flightBoard(): Updates the state of the flight board based on the current game phase.
 *   - shipBoard(ClientState state): Updates the display of the shipboard according to the client's state.
 *   - showDrawCard(): Displays the option or result of drawing a card.
 *   - showCoordRequest(): Displays the details of a coordinate request.
 *   - showAction(): Shows the available actions to the player.
 *   - showRollDice(): Displays the result of a dice roll.
 *   - showPlanetChoice(): Displays the options for planetary selection.
 *   - showManageGoods(): Manages the display and interaction for goods management.
 * <p>
 * Responsibilities:
 * - This class is responsible for managing all interactions related to cards and game mechanics
 *   involving the cards UI. It coordinates various player actions, updates the game state,
 *   and manages the visibility of UI elements.
 */
public class CardsController {

    /**
     * Indicates whether a certain element or component is visible.
     * This boolean*/
    public boolean visible= true;

    /**
     * A Label element initialized with an empty string ("") to display text in the
     * user interface. Typically represents the result or value of an action, such
     * as rolling a dice or similar activity.

     * The @FXML annotation indicates that this variable is associated with an
     * element defined in the FXML file and is*/
    @FXML
    public Label rolled=new Label("");


    /**
     * Polygons for the flight board
     */
    @FXML
    public Polygon p0;
    @FXML
    public Polygon p1;
    @FXML
    public Polygon p2;
    @FXML
    public Polygon p3;
    @FXML
    public Polygon p4;
    @FXML
    public Polygon p5;
    @FXML
    public Polygon p6;
    @FXML
    public Polygon p7;
    @FXML
    public Polygon p8;
    @FXML
    public Polygon p9;
    @FXML
    public Polygon p10;
    @FXML
    public Polygon p11;
    @FXML
    public Polygon p12;
    @FXML
    public Polygon p13;
    @FXML
    public Polygon p14;
    @FXML
    public Polygon p15;
    @FXML
    public Polygon p16;
    @FXML
    public Polygon p17;
    @FXML
    public Polygon p18;
    @FXML
    public Polygon p19;
    @FXML
    public Polygon p20;
    @FXML
    public Polygon p21;
    @FXML
    public Polygon p22;
    @FXML
    public Polygon p23;
    @FXML
    public Polygon pt0;
    @FXML
    public Polygon pt1;
    @FXML
    public Polygon pt2;
    @FXML
    public Polygon pt3;
    @FXML
    public Polygon pt4;
    @FXML
    public Polygon pt5;
    @FXML
    public Polygon pt6;
    @FXML
    public Polygon pt7;
    @FXML
    public Polygon pt8;
    @FXML
    public Polygon pt9;
    @FXML
    public Polygon pt10;
    @FXML
    public Polygon pt11;
    @FXML
    public Polygon pt12;
    @FXML
    public Polygon pt13;
    @FXML
    public Polygon pt14;
    @FXML
    public Polygon pt15;
    @FXML
    public Polygon pt16;
    @FXML
    public Polygon pt17;

    @FXML
    public ImageView card;

    @FXML
    public ImageView deck;

    @FXML
    public Group flightImage;

    @FXML
    public ImageView shipImage;

    @FXML
    public Group trialFlight;

    @FXML
    public GridPane tilesTable;

    @FXML
    public Button landButton=new Button("Land");

    @FXML
    public VBox mainBox=new VBox();

    @FXML
    public Label text=new Label();

    /**
     * Initializes the user interface components and updates their state based
     * on the current client state in the game. This method is invoked automatically
     * when the corresponding FXML file is loaded.

     * Responsibilities:
     * - Clears all child elements from the main container.
     * - Sets the initial text indicating the player's waiting status.
     * - Invokes methods to update various game components such as mode,
     *   flight board, and ship board based on the current client state.
     * - Configures the visibility and interactivity of UI elements like
     *   the land button and deck based on the client state.
     * - Handles the specific actions or UI updates depending on
     *   the different possible client states through a switch statement.

     * Client states handled:
     * - DRAW_CARD: Displays the draw card interface.
     * - ACTION: Displays the action screen.
     * - PLANET_CHOICE: Displays the planet choice interface.
     * - MANAGE_GOODS: Displays the manage goods screen.
     * - COORD_REQUEST: Displays the coordinate request interface.
     * - ROLL_DICE: Displays the roll dice screen.
     * - WAIT_TO_DRAW: Updates the text field to indicate waiting for other players.
     */
    @FXML
    public void initialize() {
        ClientState state = GUI.getController().getState();
        mainBox.getChildren().clear();
        text.setText("Waiting for other players...");
        modeChange();
        flightBoard();
        shipBoard(state);
        showCard();
        deck.setDisable(GUI.getController().getState()!=ClientState.DRAW_CARD);
        landButton.setVisible(true);
        landButton.setDisable(GUI.getController().getState()==ClientState.DRAW_CARD||GUI.getController().getState()==ClientState.WAIT||GUI.getController().getState()==ClientState.WAIT_TO_DRAW);
        switch (state){
            case DRAW_CARD-> showDrawCard();
            case ACTION -> showAction();
            case PLANET_CHOICE -> showPlanetChoice();
            case MANAGE_GOODS -> showManageGoods();
            case COORD_REQUEST -> showCoordRequest();
            case ROLL_DICE -> showRollDice();
            case WAIT_TO_DRAW-> text.setText("Waiting for other players...");
        }
    }

    /**
     * Executes the logic for the "draw" action within the game.

     * Responsibilities:
     * - Invokes the {@code waitOthers()} method to update the user interface,
     *   clearing specific UI components and notifying the user to wait for other players.
     * - Calls the {@code drawCard()} method from the {@code GUI} utility to handle
     *   the graphical display and mechanics of drawing a card.

     * This method is bound to an FXML element and is executed in response to
     * an associated user interaction or event within the game's interface.
     */
    @FXML
    public void draw() {
        waitOthers();
        GUI.drawCard();
    }

    /**
     * Handles the confirmation action triggered by the user and updates the game state accordingly.

     * Responsibilities:
     * - Invokes the {@code waitOthers()} method to update the user interface, clearing specific UI elements
     *   and notifying the user to wait for other players.
     * - Calls {@code GUI.action(true)} to perform the required action logic in the game's graphical user interface.

     * This method is bound to an FXML element and is executed in response to a corresponding user interaction
     * or event within the game's interface.
     */
    @FXML
    public void yes() {
        waitOthers();
        GUI.action(true);
    }

    /**
     * Executes the logic for the "no" action within the game's user interface.

     * Responsibilities:
     * - Invokes the {@code waitOthers()} method to update the interface, clearing specific UI components
     *   and notifying the user to wait for other players.
     * - Calls {@code GUI.action(false)} to execute the corresponding action logic
     *   in the graphical user interface with a parameter indicating a "no" response.

     * This method is connected to an FXML element and is triggered by a related user action
     * or event within the game's interface.
     */
    @FXML
    public void no() {
        waitOthers();
        GUI.action(false);
    }

    /**
     * Executes the dice roll action within the game.

     * Responsibilities:
     * - Invokes the {@code waitOthers()} method to update the user interface by clearing specific
     *   UI components and notifying the user to wait for other players.
     * - Calls {@code GUI.roll()} to perform the dice roll logic and update the graphical
     *   representation in the game's interface.

     * This method is bound to an FXML element and is executed in response to a user action
     * or event within the game's user interface.
     */
    @FXML
    public void roll() {
        waitOthers();
        GUI.roll();
    }

    /**
     * Handles the completion of the coordinate request action within the game's interface.

     * Responsibilities:
     * - Invokes the {@code waitOthers()} method to update the user interface,
     *   clearing specific UI elements and notifying the user to wait for other players.
     * - Calls {@code GUI.doneCoord()} to execute the required logic for completing
     *   the coordinate request in the graphical user interface.

     * This method is bound to an FXML element and is executed in response to a user action
     * or event associated with the completion of the coordinate request.
     */
    @FXML
    public void doneCoord(){
        waitOthers();
        GUI.doneCoord();
    }

    /**
     * Handles the user's selection of a planet to proceed in the game's interface.
     * <br>
     * Responsibilities:
     * - Invokes {@code waitOthers()} to update the user interface, clearing specific UI components and
     *   notifying the user to wait for other players.
     * - Calls {@code GUI.choosePlanet(index)} to process the logic of planet selection in the
     *   graphical user interface.
     *
     * @param index the index of the selected planet, indicating the player's choice.
     */
    @FXML
    public void choosePlanet(int index){
        waitOthers();
        GUI.choosePlanet(index);
    }

    /**
     * Handles the "land" action within the game's user interface.

     * Responsibilities:
     * - Invokes the {@code land()} method from the {@code GUI} utility to perform
     *   the necessary logic and updates for the landing action in the game's graphical interface.

     * This method is bound to an FXML element and is executed in response to
     * a user interaction or event related to landing within the game.
     */
    @FXML
    public void land(){
        GUI.land();
    }

    /**
     * Updates the user interface to indicate that the current player is waiting
     * for other players to complete their actions.

     * Responsibilities:
     * - Clears all child elements from the main container (`mainBox`).
     * - Updates the text component (`text`) to display a message notifying the
     *   user that they are waiting for other players.
     */
    private void waitOthers(){
        mainBox.getChildren().clear();
        text.setText("Waiting for other players...");
    }

    /**
     * Displays the top card from a list of displayable cards in the graphical user interface.

     * If the list of displayable cards is not empty, this method retrieves the
     * first card from the list, loads its associated image based on its file
     * path, and sets the image to the card display.

     * Responsibilities:
     * - Checks if there are any cards in the displayable list provided by the
     *   {@code GUI.displayableCards()} method.
     * - Retrieves the first card in the list and determines its associated image
     *   file path.
     * - Loads the image from the file system using the {@code loadImage()} method.
     * - Updates the graphical component to display the loaded card image.
     */
    private void showCard(){
        if(!GUI.displayableCards().isEmpty())
            card.setImage(loadImage(GUI.displayableCards().getFirst().getFilePath()));
    }

    /**
     * Loads an image from the specified path relative to the class's resource directory.
     * If the image cannot be found or loaded, the method returns null.
     *
     * @param path the relative path to the image file within the resource directory
     * @return the loaded Image object, or null if the image could not be loaded
     */
    private Image loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return null;
        }
        return new Image(stream);
    }

    /**
     * Updates the graphical user interface elements and resources based on the current game mode.

     * This method checks the current game mode retrieved from the game's controller and adjusts
     * the visibility and appearance of various UI components accordingly.

     * Responsibilities:
     * - For GameMode.LEVEL2:
     *   - Sets the `trialFlight` component to be invisible.
     *   - Sets the `flightImage` component to be visible.
     *   - Updates the `shipImage` to use an appropriate image resource for this mode.
     * - For GameMode.TRIAL:
     *   - Updates the `deck` component to use an appropriate image resource for this mode.
     *   - Sets the `flightImage` component to be invisible.
     *   - Sets the `trialFlight` component to be visible.
     *   - Updates the `shipImage` to use an appropriate image resource for this mode.
     */
    private void modeChange(){
        if(GUI.getController().getGameMode()==GameMode.LEVEL2){
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            deck.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cards/GT-cards_I_IT_0121.jpg"))));
            flightImage.setVisible(false);
            trialFlight.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }
    }

    /**
     * Updates the flight board by setting the visual properties of board segments
     * based on the players' positions and their assigned colors.

     * This method iterates through a predefined array of polygons representing
     * board segments and updates their appearance depending on whether they are
     * occupied by a player. Different sets of polygons and a maximum index value
     * are used depending on the game mode (standard or trial mode).

     * Key functionality:
     * 1. Determines the appropriate polygon array and max index depending on the game mode.
     * 2. Resets the visual properties of all polygons (stroke, fill, stroke width, and effect).
     * 3. For each player's position, assigns a fill color to the corresponding polygon
     *    based on the player's color (e.g., red, yellow, green, blue).
     * 4. Updates the polygon fill only for players' positions that match the current index.

     * Note:
     * Positions exceeding the maximum index value will be wrapped around.
     * Utilizes RGB and Color settings for stroke and fill management to
     * visually represent the board state.
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
     * Updates and displays the graphical representation of tiles on the ship's board based on the provided client state.
     *
     * @param state the current state of the client, which determines the interactive behavior and conditions
     *              of the displayed tiles on the ship's board
     */
    private void shipBoard(ClientState state){
        tilesTable.getChildren().clear();

        ArrayList<Tile> tiles=new ArrayList<>();
        for(int i=0;i<=6;i++){
            for(int j=0;j<=4;j++) {
                if(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].isPresent()&&GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get().fillable())
                    tiles.add(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get());
            }
        }
        for(Tile tile:tiles){
            StackPane stackPane=new StackPane();
            stackPane.setPrefSize(80,80);
            HBox hBox=new HBox();
            hBox.setPrefSize(80,80);
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER);
            VBox vBox=new VBox();
            vBox.setPrefSize(80,80);
            vBox.setSpacing(5);
            vBox.setAlignment(Pos.CENTER);
            HBox hBox1=new HBox();
            hBox1.setPrefSize(80,80);
            hBox1.setSpacing(5);
            hBox1.setAlignment(Pos.CENTER);
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation()*90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setDisable(true);
                switch (state){
                    case COORD_REQUEST-> {
                        CoordReqType type=GUI.getController().getCoordInputManager().getCoordReqType();
                        if(type!=null)
                            switch (type){
                                case CHOOSE_TO_BREAK,CHOOSE_TO_MAINTAIN -> {
                                    imageView.setDisable(false);
                                    imageView.setOnMouseClicked(_ -> {
                                        GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                        initialize();
                                    });
                                    hBox.setDisable(false);
                                    hBox.setOnMouseClicked(_ -> {
                                        GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                        initialize();
                                    });
                                    hBox1.setDisable(false);
                                    hBox1.setOnMouseClicked(_ -> {
                                        GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                        initialize();
                                    });
                                    vBox.setDisable(false);
                                    vBox.setOnMouseClicked(_ -> {
                                        GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                        initialize();
                                    });
                                }
                                case CHOOSE_BATTERY -> {
                                    if(tile instanceof BatteryComponents && tile.getNumBatteries()!=0) {
                                        imageView.setDisable(false);
                                        imageView.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        hBox.setDisable(false);
                                        hBox.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        hBox1.setDisable(false);
                                        hBox1.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        vBox.setDisable(false);
                                        vBox.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                    }
                                }
                                case CHOOSE_DOUBLE_CANNON ->{
                                    if(tile instanceof BatteryComponents && tile.getNumBatteries()!=0||tile instanceof DoubleCannon) {
                                        imageView.setDisable(false);
                                        imageView.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        hBox.setDisable(false);
                                        hBox.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        hBox1.setDisable(false);
                                        hBox1.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        vBox.setDisable(false);
                                        vBox.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                    }
                                }
                                case CHOOSE_DOUBLE_ENGINE -> {
                                    if (tile instanceof BatteryComponents && tile.getNumBatteries()!=0 || tile instanceof DoubleEngine) {
                                        imageView.setDisable(false);
                                        imageView.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        hBox.setDisable(false);
                                        hBox.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        hBox1.setDisable(false);
                                        hBox1.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                        vBox.setDisable(false);
                                        vBox.setOnMouseClicked(_ -> {
                                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                            initialize();
                                        });
                                    }
                                }
                                case CHOOSE_CREW -> {
                                   if(tile instanceof Cabin && tile.getCrew()!=0){
                                       imageView.setDisable(false);
                                       imageView.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                       hBox.setDisable(false);
                                       hBox.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                       hBox1.setDisable(false);
                                       hBox1.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                       vBox.setDisable(false);
                                       vBox.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                   }
                                }
                                case REMOVE_GOODS -> {
                                   if(tile instanceof CargoHold && !tile.getCargo().isEmpty()||tile instanceof BatteryComponents && tile.getNumBatteries()!=0){
                                       imageView.setDisable(false);
                                       imageView.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                       hBox.setDisable(false);
                                       hBox.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                       hBox1.setDisable(false);
                                       hBox1.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                       vBox.setDisable(false);
                                       vBox.setOnMouseClicked(_ -> {
                                           GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                           initialize();
                                       });
                                   }
                                }
                            }

                        }
                    case MANAGE_GOODS -> {
                        if(tile instanceof CargoHold) {
                            imageView.setDisable(false);
                            imageView.setOnMouseClicked(_ -> {
                                GUI.chooseCargo(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                visible=true;
                                initialize();
                            });
                            hBox.setDisable(false);
                            hBox.setOnMouseClicked(_ -> {
                                GUI.chooseCargo(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                visible=true;
                                initialize();
                            });
                            hBox1.setDisable(false);
                            hBox1.setOnMouseClicked(_ -> {
                                GUI.chooseCargo(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                visible=true;
                                initialize();
                            });
                            vBox.setDisable(false);
                            vBox.setOnMouseClicked(_ -> {
                                GUI.chooseCargo(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                                visible=true;
                                initialize();
                            });
                        }
                    }
                }
                stackPane.getChildren().add(imageView);
                for (int i=tile.getCrew();i!=0;i--){
                    ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/human.png"))));
                    switch (tile.getCrewType()) {
                        case HUMAN -> element= new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/human.png"))));
                        case BROWN -> element= new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/brown.png"))));
                        case PURPLE ->element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/purple.png"))));
                    }
                    element.setDisable(true);
                    element.setFitWidth(30);
                    element.setFitHeight(30);
                    hBox.getChildren().add(element);
                }
                for (int i=tile.getNumBatteries();i!=0;i--){
                    ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/battery.png"))));
                    element.setDisable(true);
                    element.setFitWidth(30);
                    element.setFitHeight(60);
                    hBox.rotateProperty().setValue(tile.getRotation()*90);
                    hBox.getChildren().add(element);
                }
                if(tile.getCargo()!=null&&!tile.getCargo().isEmpty()) {
                    ArrayList<Goods> cargo = tile.getCargo();
                    if (tile.getCargo().size()<=2){
                        for (int i=cargo.size();i!=0;i--){
                            ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                            switch (cargo.get(i-1).getColor()){
                                case RED -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                                case YELLOW -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoYellow.png"))));
                                case GREEN -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoGreen.png"))));
                                case BLUE -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoBlue.png"))));
                            }
                            element.setDisable(true);
                            element.setFitWidth(30);
                            element.setFitHeight(30);
                            hBox.getChildren().add(element);
                        }
                        stackPane.getChildren().add(hBox);
                    }else{
                        for (int i=cargo.size();i>2;i--){
                            ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                            switch (cargo.get(i-1).getColor()){
                                case RED -> element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoRed.png")));
                                case YELLOW -> element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoYellow.png")));
                                case GREEN ->element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoGreen.png")));
                                case BLUE -> element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoBlue.png")));
                            }
                            element.setDisable(true);
                            element.setFitWidth(30);
                            element.setFitHeight(30);
                            hBox1.getChildren().add(element);
                            hBox1.setPrefSize(40,40);
                        }for (int i=cargo.size()-2;i!=0;i--){
                            ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                            switch (cargo.get(i-1).getColor()){
                                case RED -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                                case YELLOW -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoYellow.png"))));
                                case GREEN ->element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoGreen.png"))));
                                case BLUE -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoBlue.png"))));
                            }
                            element.setDisable(true);
                            element.setFitWidth(30);
                            element.setFitHeight(30);
                            hBox.getChildren().add(element);
                            hBox1.setPrefSize(40,40);
                        }
                        vBox.rotateProperty().setValue(tile.getRotation()*90);
                        stackPane.getChildren().add(vBox);
                        vBox.getChildren().addAll(hBox1,hBox);
                    }
                } else {
                    stackPane.getChildren().add(hBox);
                }
                tilesTable.add(stackPane,tile.getCoordinates().getY(),tile.getCoordinates().getX());
            }
        }
    }

    /**
     * Hides the main UI box and updates the display text to prompt the user to draw a card.
     * This method ensures that UI changes are executed on the JavaFX Application Thread
     * by using the {@code Platform.runLater} method.
     */
    //Loaders---------------------
    private void showDrawCard() {
        Platform.runLater(() -> {
            mainBox.setVisible(false);
            text.setText("Draw a Card");
        });
    }

    /**
     * Displays the coordinate request interface based on the type of request
     * retrieved from the CoordInputManager. This method updates the UI on
     * the JavaFX application thread and modifies the components accordingly.

     * The method performs the following:
     * 1. Retrieves the {@link CoordReqType} from the CoordInputManager.
     * 2. Checks if the `CoordReqType` is one of the predefined types such as
     *    CHOOSE_TO_BREAK, CHOOSE_BATTERY, CHOOSE_DOUBLE_CANNON, or CHOOSE_DOUBLE_ENGINE.
     * 3. If the type matches one of the valid values, a "Done" button is created
     *    and added to the main UI box. Clicking this button triggers the
     *    {@code doneCoord} method for further processing.
     * 4. Adjusts the visibility of UI elements, such as hiding the landButton
     *    when the type is CHOOSE_TO_BREAK.
     * 5. Updates a displayed text field to reflect the current type as a string.

     * The method uses {@link Platform#runLater(Runnable)} to ensure all UI
     * updates are performed on the JavaFX application thread to avoid thread
     * concurrency issues.
     */
    private void showCoordRequest() {
        Platform.runLater(() -> {
            CoordReqType type = GUI.getController().getCoordInputManager().getCoordReqType();
            if(GUI.getController().getCoordInputManager().getCoordReqType()!=null) {
                if (type == CHOOSE_TO_BREAK || type == CHOOSE_BATTERY || type == CHOOSE_DOUBLE_CANNON || type == CHOOSE_DOUBLE_ENGINE) {
                    Button doneButton = new Button("Done");
                    doneButton.setPrefSize(100, 75);
                    doneButton.setOnAction(_ -> doneCoord());
                    mainBox.getChildren().add(doneButton);
                }
                if(type==CHOOSE_TO_BREAK)
                    landButton.setVisible(false);
                text.setText(type.toString());
            }
        });
    }

    /**
     * Displays a dialog with Yes and No buttons and a prompt text asking for acceptance.
     * This method uses JavaFX's Platform.runLater to ensure that UI updates
     * are performed on the JavaFX Application Thread.

     * The Yes button triggers the {@code yes()} method when clicked, and the No button
     * triggers the {@code no()} method.

     * The size of each button is set to a preferred width of 100 and height of 75.
     * The mainBox UI container is updated by adding the Yes and No buttons to it.
     * Additionally, a text label is updated to display the message "Accept?".
     */
    private void showAction() {
        Platform.runLater(() -> {
            Button yesButton=new Button("Yes");
            yesButton.setPrefSize(100,75);
            yesButton.setOnAction(_->yes());
            Button noButton=new Button("no");
            noButton.setPrefSize(100,75);
            noButton.setOnAction(_->no());
            mainBox.getChildren().addAll(yesButton,noButton);
            text.setText("Accept?");
        });
    }

    /**
     * Displays a button labeled "Roll" on the user interface that allows the user to roll the dice.
     * This method executes on the JavaFX Application Thread using Platform.runLater to ensure
     * proper updates to the UI components.

     * Within the UI updates:
     * - A new Button is created and configured with a label "Roll" and a preferred size of 100x75.
     * - An action handler is added to the button, which calls the roll() method when the button is clicked.
     * - The button is added to the mainBox container.
     * - The text of the related Text node is updated to inform the user to "Roll the Dices".
     */
    private void showRollDice(){
        Platform.runLater(() -> {
            Button rollButton=new Button("Roll");
            rollButton.setPrefSize(100,75);
            rollButton.setOnAction(_->roll());
            mainBox.getChildren().add(rollButton);
            if(rolled!=null){
                mainBox.getChildren().add(rolled);
            }
            text.setText("Roll the Dices");
        });
    }


    /**
     * Displays the rolled value and updates the UI accordingly.
     * Clears the mainBox's children if the number of children is less than or equal to two,
     * sets the text and preferred size of the rolled object, and adds it to mainBox.
     *
     * @param index the integer value representing the rolled value to be displayed
     */
    public void showRoll(int index){
        if(mainBox.getChildren().size()<=2) {
            mainBox.getChildren().clear();
        }
        rolled.setPrefSize(100,50);
        rolled.setText(index+"");
        mainBox.getChildren().add(rolled);
    }

    /**
     * Displays a list of planets with their respective buttons for selection.
     * Each button represents a planet and is labeled with its index (1-based).
     * Disabled buttons indicate that the corresponding planet is already occupied.
     * Updates the UI dynamically using the JavaFX Platform.runLater method to ensure thread safety.

     * The method performs the following steps:
     * 1. Clears the children of the mainBox container.
     * 2. Iterates through the list of planets retrieved from the displayable cards.
     * 3. Creates and configures a button for each planet, including setting its label, size, and action event.
     * 4. Disables the button if the planet's occupation status is true.
     * 5. Adds the configured button to the mainBox container.
     * 6. Updates the text displayed to prompt the user to choose a planet.

     * The selection action is handled by invoking the choosePlanet method,
     * passing the index of the selected planet as an argument.
     */
    private void showPlanetChoice(){
        Platform.runLater(() -> {
            int index=0;
            mainBox.getChildren().clear();
            for(Planet planet:GUI.displayableCards().getFirst().getListOfPlanets()){
                if(GUI.getController().getAvailablePlanets().get(index)) {
                    Button button = new Button();
                    button.setPrefSize(200, 75);
                    button.setText((index + 1) + "° Planet");
                    int finalIndex = index;
                    button.setOnAction(_ -> choosePlanet(finalIndex));
                    button.setDisable(planet.getOccupationStatus());
                    mainBox.getChildren().add(button);
                }
                index++;
            }
            text.setText("Choose a Planet");
        });
    }

    /**
     * Displays the "Manage Goods" interface in the application.
     * This method dynamically creates and updates the UI components for managing goods,
     * depending on whether the user is managing their own cargo or the goods available on the planet.
     * Buttons are generated for each good, allowing the user to interact with individual goods,
     * and a "Done" button is provided for completing the management process.

     * Behavior:
     * - If the user's cargo is not empty, displays a list of goods from the player's inventory.
     * - If the user's cargo is empty, displays a list of goods available on the planet.
     * - Each good is displayed as a button, labeled with its corresponding color (BLUE, GREEN,
     *   RED, or YELLOW). Clicking a button triggers actions to select the good and update the UI.
     * - The "Done" button becomes visible only if the user's cargo is empty and is used to
     *   finalize the process.

     * UI Elements:
     * - Buttons representing each good, dynamically sized and labeled based on the goods' attributes.
     * - A "Done" button to complete goods management, which is conditionally visible.
     * - Labels displaying context-based information ("Your Goods" or "Planet's Goods").
     * - A layout consisting of an HBox for organizing the goods and controls, and a VBox for
     *   grouping individual good buttons.

     * Updates:
     * - The view is updated based on user actions (e.g., selecting a good or finalizing management).
     * - Text description of the current activity is updated to "Manage your Goods."

     * Threading:
     * - Utilizes `Platform.runLater` to ensure updates to the JavaFX UI occur on the JavaFX Application Thread.
     */
    private void showManageGoods(){
        Platform.runLater(() -> {
            Button doneButton=new Button("Done");
            doneButton.setPrefSize(100,75);
            doneButton.setOnAction(_->GUI.doneGoods());
            HBox box=new HBox();
            box.setAlignment(Pos.CENTER);
            box.setSpacing(30);
            VBox goodsBox=new VBox();
            goodsBox.setSpacing(30);
            int index=1;
            if(GUI.getController().getGoodsManager().getCargo()!=null&&!GUI.getController().getGoodsManager().getCargo().isEmpty()) {
                doneButton.setVisible(false);
                goodsBox.getChildren().add(new Label("Yours Goods:"));
                for (Goods goods : GUI.getController().getGoodsManager().getCargo()) {
                    Button button = new Button();
                    button.setPrefSize(200, 75);
                    if (goods.getColor() == GoodsColor.BLUE)
                        button.setText("BLUE");
                    else if (goods.getColor() == GoodsColor.GREEN)
                        button.setText("GREEN");
                    else if (goods.getColor() == GoodsColor.RED)
                        button.setText("RED");
                    else if (goods.getColor() == GoodsColor.YELLOW)
                        button.setText("YELLOW");
                    int finalIndex1 = index;
                    button.setOnAction(_ -> {
                        chooseGood(finalIndex1);
                        initialize();
                    });
                    goodsBox.getChildren().add(button);
                    index++;
                }
            }else{
                doneButton.setVisible(true);
                goodsBox.getChildren().add(new Label("Planet's Goods:"));
                for(Goods goods:GUI.getController().getGoodsList()){
                    Button button= new Button();
                    button.setPrefSize(200,75);
                    if(goods.getColor()== GoodsColor.BLUE)
                        button.setText("BLUE");
                    else if(goods.getColor()== GoodsColor.GREEN)
                        button.setText("GREEN");
                    else if(goods.getColor()== GoodsColor.RED)
                        button.setText("RED");
                    else if(goods.getColor()== GoodsColor.YELLOW)
                        button.setText("YELLOW");
                    int finalIndex = index;
                    button.setOnAction(_ -> {
                        chooseGood(finalIndex);
                        visible = false;
                        initialize();
                    });
                    goodsBox.getChildren().add(button);
                    index++;
                }
            }
            box.getChildren().addAll(goodsBox,doneButton);
            mainBox.getChildren().add(box);
            mainBox.setVisible(visible);
            text.setText("Manage your Goods");
        });
    }

    /**
     * Selects a specific item or option based on the provided index.
     *
     * @param index the index of the item to be chosen, typically starting from 0
     */
    public void chooseGood(int index){
        GUI.chooseGood(index);
    }
}