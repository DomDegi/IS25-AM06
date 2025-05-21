package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class S_EndDrawTilesCardsController {

    @FXML
    public GridPane tilesTable;

    @FXML
    public GridPane bookedTiles;

    @FXML
    public TilePane drawnTiles1;

    @FXML
    public TilePane drawnTiles2;

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
                System.err.println("Impossibile trovare l'immagine: " + imagePath);
                imageButton.setText("Img non trovata");
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
        int index=0;
        for(Tile tile:GUI.getController().getTurnedTilesDisplayer()){
            Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("Impossibile trovare l'immagine: " + imagePath);
                imageButton.setText("Img non trovata");
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
                imageButton.setOnAction(event -> {
                    drawDrawnTile(tile.getKey());
                });
                if(index%2==0){
                    drawnTiles1.getChildren().add(imageButton);
                }else {
                    drawnTiles2.getChildren().add(imageButton);
                }
                index++;
            }
        }

        int i=0;
        for(Tile tile:GUI.getController().getLightShipBoard().getBookedTiles()){
            Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("Impossibile trovare l'immagine: " + imagePath);
                imageButton.setText("Img non trovata");
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
                int finalI = i;
                imageButton.setOnAction(event -> {
                    drawBookedTile(finalI);
                });
                bookedTiles.add(imageButton,i,0);
            }
            i++;
        }
    }

    @FXML
    public void drawTile(){
        GUI.drawTile();
    }

    @FXML
    public void drawBookedTile(int index){
        GUI.drawBookedTile(index);
    }

    @FXML
    public void drawDrawnTile(int index){
        GUI.drawDrawnTile(index);
    }

    @FXML
    public void drawDeck1(){
        GUI.drawDeck(1);
    }

    @FXML
    public void drawDeck2(){
        GUI.drawDeck(2);
    }

    @FXML
    public void drawDeck3(){
        GUI.drawDeck(3);
    }

    @FXML
    public void endShip(){
        GUI.endShip();
    }

    @FXML
    public void turnHourglass(){
        GUI.turnHourglass();
    }

}
