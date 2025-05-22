package it.polimi.ingsw.galaxytruckerproject.view.gui;

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

public class ShowDrawnCardController {

    @FXML
    public ImageView card;

    @FXML
    public GridPane tilesTable;

    @FXML
    public void initialize() {

        ArrayList<Tile> tiles=new ArrayList<>();
        for(int i=0;i<=6;i++){
            for(int j=0;j<=4;j++) {
                if(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].isPresent()&&GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get().fillable())
                    tiles.add(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get());
            }
        }
        for(Tile tile:tiles){
            Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
                imageButton.setText("MCS");
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation()*90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageButton.setGraphic(imageView);
                imageButton.setPrefWidth(80);
                imageButton.setPrefHeight(80);
                imageButton.setMaxWidth(80);
                imageButton.setMaxHeight(80);
                imageButton.setPadding(Insets.EMPTY);
                tilesTable.add(imageButton,tile.getCoordinates().getY(),tile.getCoordinates().getX());
            }
        }

        card.setImage(loadImage(GUI.displayableCards().get(0).getFilePath()));
    }

    private Image loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Immagine non trovata: " + path);
            return null;
        }
        return new Image(stream);
    }



}
