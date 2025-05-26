package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Polygon;

import static javafx.scene.paint.Color.rgb;

/**
 * Controller for handling the end-of-ship phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller manages the display and interaction for the end of the ship phase,
 * where players are assigned to available positions and the progress is tracked using hourglasses.
 * It also handles updating the game state based on player actions, such as positioning and turning the hourglass.
 * </p>
 */
public class EndShipController {

    @FXML
    public ProgressBar hourglass1;
    @FXML
    public ProgressBar hourglass2;
    @FXML
    public ProgressBar hourglass3;

    @FXML
    public Group flightImage;

    @FXML
    public Group trialFlight;

    @FXML
    public Polygon firstTrial;

    @FXML
    public Polygon secondTrial;

    @FXML
    public Polygon thirdTrial;

    @FXML
    public Polygon fourthTrial;

    @FXML
    public Polygon first;

    @FXML
    public Polygon second;

    @FXML
    public Polygon third;

    @FXML
    public Polygon fourth;

    /**
     * Initializes the UI for the end-of-ship phase.
     * This includes setting up the hourglasses, trial or flight images, and player positions based on the game mode.
     */
    @FXML
    public void initialize() {
        Polygon[] polygons = new Polygon[]{first, second, third, fourth};
        int i = 1;
        if (GUI.getController().getGameMode().equals(GameMode.LEVEL2)) {
            hourglass1.setProgress(1 - GUI.getPercentage1());
            hourglass2.setProgress(1 - GUI.getPercentage2());
            hourglass3.setProgress(1 - GUI.getPercentage3());
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            polygons = new Polygon[]{first, second, third, fourth};
        } else if (GUI.getController().getGameMode().equals(GameMode.TRIAL)) {
            trialFlight.setVisible(true);
            flightImage.setVisible(false);
            polygons = new Polygon[]{firstTrial, secondTrial, thirdTrial, fourthTrial};
        }
        for (Polygon p : polygons) {
            if (i > GUI.getController().getFlightBoard().getInGamePlayers().size())
                p.setVisible(false);
            p.setStroke(rgb(255, 169, 19));
            p.setFill(Color.TRANSPARENT);
            p.setStrokeWidth(5);
            p.setEffect(new DropShadow(BlurType.GAUSSIAN, rgb(255, 169, 19), 30, 0.4, 0, 0));
            if (GUI.getController().getAvailablePosition().get(i) != null) {
                if (GUI.getController().getAvailablePosition().get(i) == PlayersColor.RED)
                    p.setFill(Color.RED);
                if (GUI.getController().getAvailablePosition().get(i) == PlayersColor.YELLOW)
                    p.setFill(Color.YELLOW);
                if (GUI.getController().getAvailablePosition().get(i) == PlayersColor.GREEN)
                    p.setFill(Color.GREEN);
                if (GUI.getController().getAvailablePosition().get(i) == PlayersColor.BLUE)
                    p.setFill(Color.BLUE);
            }
            i++;
        }
    }

    /**
     * Positions the player in the first available slot.
     */
    @FXML
    public void pos1() {
        if (GUI.getController().getAvailablePosition().get(1) == null && !GUI.getController().isPositioned()) {
            GUI.position(1);
            update();
        }
    }

    /**
     * Positions the player in the second available slot.
     */
    @FXML
    public void pos2() {
        if (GUI.getController().getAvailablePosition().get(2) == null && !GUI.getController().isPositioned()) {
            GUI.position(2);
            update();
        }
    }

    /**
     * Positions the player in the third available slot.
     */
    @FXML
    public void pos3() {
        if (GUI.getController().getAvailablePosition().get(3) == null && !GUI.getController().isPositioned()) {
            GUI.position(3);
            update();
        }
    }

    /**
     * Positions the player in the fourth available slot.
     */
    @FXML
    public void pos4() {
        if (GUI.getController().getAvailablePosition().get(4) == null && !GUI.getController().isPositioned()) {
            GUI.position(4);
            update();
        }
    }

    /**
     * Turns the hourglass to indicate the passing of time in the game.
     */
    @FXML
    public void turnHourglass() {
        GUI.turnHourglass();
    }

    /**
     * Updates the progress bar for the game timer based on the current progress.
     * @param index the current index of progress
     * @param max the maximum progress value
     * @param turns the current turn number (1, 2, or 3)
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

    /**
     * Updates the UI after a player has been positioned or the state has changed.
     */
    public void update() {
        initialize();
    }
}
