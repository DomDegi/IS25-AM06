package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Polygon;

import static javafx.scene.paint.Color.rgb;

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

    @FXML
    public void initialize(){
        int i=1;
        if(GUI.getController().getGameMode().equals(GameMode.LEVEL2)) {
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            for (Polygon p : new Polygon[]{first, second, third, fourth}) {
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
        }else if(GUI.getController().getGameMode().equals(GameMode.TRIAL)){
            trialFlight.setVisible(true);
            flightImage.setVisible(false);
            for (Polygon p : new Polygon[]{firstTrial, secondTrial, thirdTrial, fourthTrial}) {
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
    }

    @FXML
    public void pos1(){
        if(GUI.getController().getAvailablePosition().get(1)==null&&!GUI.getController().isPositioned()) {
            GUI.position(1);
            update();
        }
    }

    @FXML
    public void pos2(){
        if(GUI.getController().getAvailablePosition().get(2)==null&&!GUI.getController().isPositioned()) {
            GUI.position(2);
            update();
        }
    }

    @FXML
    public void pos3(){
        if(GUI.getController().getAvailablePosition().get(3)==null&&!GUI.getController().isPositioned()) {
            GUI.position(3);
            update();
        }
    }

    @FXML
    public void pos4(){
        if(GUI.getController().getAvailablePosition().get(4)==null&&!GUI.getController().isPositioned()) {
            GUI.position(4);
            update();
        }

    }

    @FXML
    public void turnHourglass(){
        GUI.turnHourglass();
    }

    @FXML
    public void goProgressBar(int index,int max,int turns){
        double percentage= (double) index /max;
        if(turns==1){
            hourglass3.setProgress(1);
            hourglass2.setProgress(1);
            hourglass1.setProgress(1-percentage);
        }else if(turns==2){
            hourglass3.setProgress(1);
            hourglass2.setProgress(1-percentage);
        }else if(turns==3){
            hourglass3.setProgress(1-percentage);
        }
    }

    public void update(){
        initialize();
    }
}
