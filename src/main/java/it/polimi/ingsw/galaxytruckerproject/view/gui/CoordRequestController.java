package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
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

import static it.polimi.ingsw.galaxytruckerproject.client.CoordReqType.*;
import static javafx.scene.paint.Color.rgb;

/**
 * Controller for handling the coordinate request phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the display of tiles, player positions on the flightboard, and user interactions during
 * the coordinate request phase. It also handles the display of actions based on the type of coordinate request,
 * such as choosing tiles to break, choosing to maintain, or selecting batteries and cannons.
 * </p>
 */
public class CoordRequestController {

    @FXML
    public Polygon p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23;
    @FXML
    public Polygon pt0, pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8, pt9, pt10, pt11, pt12, pt13, pt14, pt15, pt16, pt17;

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
    public Label actionLabel;
    @FXML
    public Button doneButton;

    /**
     * Initializes the UI for the coordinate request phase.
     * This method sets up the flightboard, the tiles, the ship image, and adjusts the UI based on the game mode
     * (LEVEL2 or TRIAL). It also manages the actions and labels based on the coordinate request type.
     */
    @FXML
    public void initialize() {
        doneButton.setVisible(true);
        actionLabel.setVisible(true);
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
                imageView.setOnMouseClicked(event -> {
                    GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                    initialize();
                });
                tilesTable.add(imageView, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }

        CoordReqType type = GUI.getController().getCoordInputManager().getCoordReqType();
        switch (type) {
            case CHOOSE_TO_BREAK -> {
                doneButton.setVisible(true);
                actionLabel.setText("Choose Tiles to Break");
            }
            case CHOOSE_TO_MAINTAIN -> {
                doneButton.setVisible(false);
                actionLabel.setText("Choose branch to Maintain");
            }
            case CHOOSE_BATTERY -> {
                doneButton.setVisible(true);
                actionLabel.setText("Choose Battery to Use");
            }
            case CHOOSE_DOUBLE_CANNON -> {
                doneButton.setVisible(true);
                actionLabel.setText("Choose Double Cannons to Use and then Batteries to Activate them");
            }
            case CHOOSE_DOUBLE_ENGINE -> {
                doneButton.setVisible(true);
                actionLabel.setText("Choose Double Engines to Use and then Batteries to Activate them");
            }
            case CHOOSE_CREW -> {
                doneButton.setVisible(false);
                actionLabel.setText("Choose Crewmates");
            }
            case REMOVE_GOODS -> {
                doneButton.setVisible(false);
                actionLabel.setText("Choose Goods to Remove");
            }
        }
    }

    /**
     * Finalizes the coordinate request and hides the action label and done button.
     * This method notifies the GUI that the coordinate request has been completed.
     */
    @FXML
    public void doneCoord() {
        doneButton.setVisible(false);
        actionLabel.setVisible(false);
        GUI.doneCoord();
    }
}
