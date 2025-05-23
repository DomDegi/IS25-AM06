package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class EndShipController {

    @FXML
    public ProgressBar hourglass1;
    @FXML
    public ProgressBar hourglass2;
    @FXML
    public ProgressBar hourglass3;

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
        for(Polygon p:new Polygon[]{first,second,third,fourth}){
            p.opacityProperty().setValue(0);
            if(i>GUI.getController().getFlightBoard().getInGamePlayers().size())
                p.setVisible(false);
            if(GUI.getController().getAvailablePosition().get(i)!=null) {
                p.opacityProperty().setValue(100);
                if(GUI.getController().getAvailablePosition().get(i)== PlayersColor.RED)
                    p.setFill(Color.RED);
                if(GUI.getController().getAvailablePosition().get(i)== PlayersColor.YELLOW)
                    p.setFill(Color.YELLOW);
                if(GUI.getController().getAvailablePosition().get(i)== PlayersColor.GREEN)
                    p.setFill(Color.GREEN);
                if(GUI.getController().getAvailablePosition().get(i)== PlayersColor.BLUE)
                    p.setFill(Color.BLUE);
            }
            i++;
        }
    }

    @FXML
    public void pos1(){
        GUI.position(1);
        update();
    }

    @FXML
    public void pos2(){
        GUI.position(2);
        update();
    }

    @FXML
    public void pos3(){
        GUI.position(3);
        update();
    }

    @FXML
    public void pos4(){
        GUI.position(4);
        update();
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
