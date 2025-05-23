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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.io.InputStream;
import java.util.ArrayList;

public class S_ManageDrawTileController {

    @FXML
    public Button drawnTile;

    @FXML
    public TilePane drawnTiles1;

    @FXML
    public TilePane drawnTiles2;

    @FXML
    public GridPane tilesTable;

    @FXML
    public GridPane bookedTiles;

    @FXML
    public void initialize() {
        String inHandImagePath = GUI.getController().getTileInHand().getImagePath();
        InputStream inHandImageStream = getClass().getResourceAsStream(inHandImagePath);
        if (inHandImageStream == null) {
            System.err.println("not found" + inHandImagePath);
            drawnTile.setText("MCS");
        } else {
            Image inHandImage = new Image(inHandImageStream);
            ImageView inHandImageView = new ImageView(inHandImage);
            inHandImageView.rotateProperty().setValue(GUI.getController().getTileInHand().getRotation() * 90);
            inHandImageView.setFitWidth(175);
            inHandImageView.setFitHeight(175);
            drawnTile.setPadding(Insets.EMPTY);
            drawnTile.setGraphic(inHandImageView);
        }
        tilesTable.getChildren().clear();
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if(!((j==0&&(i<=1||i==3||i>=5))||(j==1&&(i==0||i==6))||(j==4&&i==3))) {
                    Button button = new Button();
                    button.setPrefHeight(80);
                    button.setPrefWidth(80);
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(event -> {
                        GUI.putTile(finalJ, finalI);
                    });
                    tilesTable.add(button, finalI, finalJ);
                }
            }
        }
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
                tilesTable.add(imageView, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }
        int index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
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
                    refuse();
                });
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageView);
                } else {
                    drawnTiles2.getChildren().add(imageView);
                }
            }
            index++;
        }

        for (int i = 0; i < 2; i++) {
            ImageView imageView= new ImageView();
            if (GUI.getController().getLightShipBoard().getBookedTiles().size() == i + 1) {
                Tile tile = GUI.getController().getLightShipBoard().getBookedTiles().get(i);
                String imagePath = tile.getImagePath();
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream == null) {
                    System.err.println("Impossibile trovare l'immagine: " + imagePath);
                } else {
                    Image image = new Image(imageStream);
                    imageView = new ImageView(image);
                    imageView.rotateProperty().setValue(tile.getRotation() * 90);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);
                }
            } else {
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setOnMouseClicked(event -> {
                    bookTile();
                });
            }
            bookedTiles.add(imageView, i, 0);
        }
    }
    @FXML
    public void K(KeyEvent event){
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            if(event.getCode().equals(KeyCode.R)){
                refuse();
            }
        }
    }

    @FXML
    public void refuse() {
        GUI.refuseTile();
    }

    @FXML
    public void bookTile() {
        GUI.bookTile();
    }

    @FXML
    public void rotateDx() {
        drawnTile.rotateProperty().setValue(drawnTile.rotateProperty().getValue() + 90);
        GUI.rotate();
    }

    @FXML
    public void rotateSx() {
        drawnTile.rotateProperty().setValue(drawnTile.rotateProperty().getValue() - 90);
        GUI.rotate();
        GUI.rotate();
        GUI.rotate();
    }

    public void update () {
        drawnTiles1.getChildren().clear();
        drawnTiles2.getChildren().clear();
        int index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
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
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageView);
                } else {
                    drawnTiles2.getChildren().add(imageView);
                }
            }
            index++;
        }
    }
}
