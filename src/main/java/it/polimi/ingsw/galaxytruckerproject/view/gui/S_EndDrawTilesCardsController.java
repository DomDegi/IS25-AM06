package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class S_EndDrawTilesCardsController {

    @FXML
    public ImageView deck1;

    @FXML
    public ImageView deck2;

    @FXML
    public ImageView deck3;

    @FXML
    public GridPane tilesTable;

    @FXML
    public GridPane bookedTiles;

    @FXML
    public TilePane drawnTiles1;

    @FXML
    public TilePane drawnTiles2;

    @FXML
    public void K(KeyEvent event){
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.D)){
                drawTile();
            }
        }
    }

    @FXML
    public void initialize() {
        deck1.setVisible(GUI.getController().getAvailableDeck().get(1));
        deck2.setVisible(GUI.getController().getAvailableDeck().get(2));
        deck3.setVisible(GUI.getController().getAvailableDeck().get(3));
        ArrayList<Tile> tiles=new ArrayList<>();
        for(int i=0;i<=6;i++){
            for(int j=0;j<=4;j++) {
                if(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].isPresent()&&GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get().fillable())
                    tiles.add(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get());
            }
        }
        for(Tile tile:tiles){
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
                tilesTable.add(imageView,tile.getCoordinates().getY(),tile.getCoordinates().getX());
            }
        }
        int index=0;
        for(Tile tile:GUI.getController().getTurnedTilesDisplayer()){
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
                    drawDrawnTile(tile.getKey());
                });
                if(index%2==0){
                    drawnTiles1.getChildren().add(imageView);
                }else {
                    drawnTiles2.getChildren().add(imageView);
                }
                index++;
            }
        }

        int i=0;
        for(Tile tile:GUI.getController().getLightShipBoard().getBookedTiles()){
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
                int finalI = i;
                imageView.setOnMouseClicked(event -> {
                    drawBookedTile(finalI);
                });
                bookedTiles.add(imageView,i,0);
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

    public void update(){
        deck1.setVisible(GUI.getController().getAvailableDeck().get(1));
        deck2.setVisible(GUI.getController().getAvailableDeck().get(2));
        deck3.setVisible(GUI.getController().getAvailableDeck().get(3));
        drawnTiles1.getChildren().clear();
        drawnTiles2.getChildren().clear();
        int index=0;
        for(Tile tile:GUI.getController().getTurnedTilesDisplayer()){
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
                    drawDrawnTile(tile.getKey());
                });
                if(index%2==0){
                    drawnTiles1.getChildren().add(imageView);
                }else {
                    drawnTiles2.getChildren().add(imageView);
                }
                index++;
            }
        }
    }
}
