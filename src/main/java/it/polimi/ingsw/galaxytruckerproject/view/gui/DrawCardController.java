package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.Group;
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
 * Controller for handling the card drawing phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the display of available cards and the player positions on the flightboard,
 * allowing the player to draw cards and interact with the game based on the current game mode (LEVEL2 or TRIAL).
 * </p>
 */
public class DrawCardController {

    @FXML
    public Polygon p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23;
    @FXML
    public Polygon pt0, pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8, pt9, pt10, pt11, pt12, pt13, pt14, pt15, pt16, pt17;
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

    /**
     * Initializes the UI for the card drawing phase.
     * This includes setting up the game board with player positions, the card deck, and the ship image
     * based on the game mode (LEVEL2 or TRIAL).
     */
    @FXML
    public void initialize() {
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

        if (GUI.getController().getState() == ClientState.WAIT) {
            deck.setDisable(true);
        }
        showCard();
    }

    /**
     * Displays the current card in the card image view.
     */
    public void showCard() {
        if (!GUI.displayableCards().isEmpty())
            card.setImage(loadImage(GUI.displayableCards().get(0).getFilePath()));
    }

    /**
     * Loads an image from the specified path.
     * @param path the path to the image file
     * @return the loaded image
     */
    private Image loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return null;
        }
        return new Image(stream);
    }

    /**
     * Triggers the drawing of a card in the game.
     */
    @FXML
    public void draw() {
        GUI.drawCard();
    }
}
