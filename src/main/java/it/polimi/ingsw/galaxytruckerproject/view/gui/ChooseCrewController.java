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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

/**
 * Controller for handling the crew selection phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the display and interaction for choosing crew members (human, purple alien, or brown alien)
 * to assign to the available cabins on the ship. The user can select different crew types depending on the available cabins.
 * </p>
 */
public class ChooseCrewController {

    @FXML
    public Polygon p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23;
    @FXML
    public Polygon pt0, pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8, pt9, pt10, pt11, pt12, pt13, pt14, pt15, pt16, pt17;

    @FXML
    public Group flightImage;

    @FXML
    public ImageView shipImage;

    @FXML
    public Group trialFlight;
    @FXML
    public GridPane tilesTable;

    @FXML
    public Label actionLabel;

    @FXML
    public Button white;
    @FXML
    public Button purple;
    @FXML
    public Button brown;

    @FXML
    public ImageView deck;

    private ArrayList<Coordinates> cabins;

    private int index = 0;

    /**
     * Initializes the UI for the crew selection phase.
     * This method sets up the flightboard, tiles, and ship image based on the game mode,
     * and manages the crew selection buttons (human, purple, brown).
     */
    @FXML
    public void initialize() {
        deck.setDisable(true);
        Polygon[] polygons = new Polygon[]{p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23};
        int max = 24;

        if (GUI.getController().getGameMode() == GameMode.LEVEL2) {
            polygons = new Polygon[]{p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23};
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            deck.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cards/GT-cards_I_IT_0121.jpg"))));
            polygons = new Polygon[]{pt0, pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8, pt9, pt10, pt11, pt12, pt13, pt14, pt15, pt16, pt17};
            max = 18;
            flightImage.setVisible(false);
            trialFlight.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }

        int index = 0;
        for (Polygon p : polygons) {
            p.setStroke(rgb(255, 169, 19));
            p.setFill(Color.TRANSPARENT);
            p.setStrokeWidth(5);
            p.setEffect(new DropShadow(BlurType.GAUSSIAN, rgb(255, 169, 19), 30, 0.4, 0, 0));
            for (LightPlayer player : GUI.getController().getFlightBoard().getInGamePlayers()) {
                if (index * (((int) player.getPosition() / max) + 1) == player.getPosition()) {
                    if (player.getPlayerColor().equals(PlayersColor.RED)) p.setFill(Color.RED);
                    if (player.getPlayerColor().equals(PlayersColor.YELLOW)) p.setFill(Color.YELLOW);
                    if (player.getPlayerColor().equals(PlayersColor.GREEN)) p.setFill(Color.GREEN);
                    if (player.getPlayerColor().equals(PlayersColor.BLUE)) p.setFill(Color.BLUE);
                }
            }
            index++;
        }

        refresh();
        cabins = GUI.getController().getCabinsManager().getCabins();
        focus();
    }

    /**
     * Refreshes the tiles table to display the current tiles.
     * This method also handles the tiles based on the available cabins and sets up the focus for crew selection.
     */
    private void refresh() {
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
     * Focuses on the current cabin and updates the available crew buttons based on the current crew type.
     */
    public void focus() {
        refresh();
        index = GUI.getController().getCabinsManager().getIndex();
        if (index < cabins.size()) {
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
        } else {
            actionLabel.setVisible(false);
            white.setVisible(false);
            purple.setVisible(false);
            brown.setVisible(false);
            return;
        }

        Tile tile = GUI.getController().getMe().getShipBoard().getTilesTable()[cabins.get(index).getX()][cabins.get(index).getY()].get();
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
     * Assigns a human crew member to the selected cabin.
     */
    @FXML
    public void human() {
        GUI.setCabin(CrewType.HUMAN);
        if (index < cabins.size())
            focus();
    }

    /**
     * Assigns a purple alien crew member to the selected cabin.
     */
    @FXML
    public void pAlien() {
        GUI.setCabin(CrewType.PURPLE);
        if (index < cabins.size())
            focus();
    }

    /**
     * Assigns a brown alien crew member to the selected cabin.
     */
    @FXML
    public void bAlien() {
        GUI.setCabin(CrewType.BROWN);
        if (index < cabins.size())
            focus();
    }
}
