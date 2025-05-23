package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.InputStream;
import java.util.ArrayList;

public class CheckShipController {

    @FXML
    public GridPane tilesTable;

    @FXML
    public void done(){
        GUI.displayClientState(GUI.getController().getState());
        GUI.getController().setChecking(null);
    }

    public void setPlayer(LightShipBoard shipBoard) {
        tilesTable.getChildren().clear();
        ArrayList<Tile> tiles=new ArrayList<>();
        for(int i=0;i<=6;i++){
            for(int j=0;j<=4;j++) {
                if(shipBoard.getTilesTable()[j][i].isPresent()&&shipBoard.getTilesTable()[j][i].get().fillable())
                    tiles.add(shipBoard.getTilesTable()[j][i].get());
            }
        }
        for(Tile tile:tiles){
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                javafx.scene.image.Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation()*90);
                imageView.setFitWidth(136);
                imageView.setFitHeight(136);
                tilesTable.add(imageView,tile.getCoordinates().getY(),tile.getCoordinates().getX());
            }
        }
    }
}
