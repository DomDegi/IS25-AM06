package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if (GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].isPresent() && GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get().fillable())
                    tiles.add(GUI.getController().getMe().getShipBoard().getTilesTable()[j][i].get());
            }
        }
        for (Tile tile : tiles) {
            Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
                imageButton.setText("MCS");
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageButton.setGraphic(imageView);
                imageButton.setPrefWidth(80);
                imageButton.setPrefHeight(80);
                imageButton.setMaxWidth(80);
                imageButton.setMaxHeight(80);
                imageButton.setPadding(Insets.EMPTY);
                tilesTable.add(imageButton, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }
        int index = 0;
        for (Tile tile : GUI.getController().getTurnedTilesDisplayer()) {
            Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
                imageButton.setText("MCS");
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageButton.setGraphic(imageView);
                imageButton.setPrefWidth(80);
                imageButton.setPrefHeight(80);
                imageButton.setMaxWidth(80);
                imageButton.setMaxHeight(80);
                imageButton.setPadding(Insets.EMPTY);
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageButton);
                } else {
                    drawnTiles2.getChildren().add(imageButton);
                }
            }
            index++;
        }

        for (int i = 0; i < 2; i++) {
            Button imageButton = new Button();
            if (GUI.getController().getLightShipBoard().getBookedTiles().size() == i + 1) {
                Tile tile = GUI.getController().getLightShipBoard().getBookedTiles().get(i);
                String imagePath = tile.getImagePath();
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream == null) {
                    System.err.println("Impossibile trovare l'immagine: " + imagePath);
                    imageButton.setText("Img non trovata");
                } else {
                    Image image = new Image(imageStream);
                    ImageView imageView = new ImageView(image);
                    imageView.rotateProperty().setValue(tile.getRotation() * 90);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);
                    imageButton.setGraphic(imageView);
                    imageButton.setPrefWidth(80);
                    imageButton.setPrefHeight(80);
                    imageButton.setMaxWidth(80);
                    imageButton.setMaxHeight(80);
                    imageButton.setPadding(Insets.EMPTY);
                }
            } else {
                imageButton.setBackground(new Background(new BackgroundFill(null, null, null)));
                imageButton.setPrefWidth(80);
                imageButton.setPrefHeight(80);
                imageButton.setMaxWidth(80);
                imageButton.setMaxHeight(80);
                imageButton.setPadding(Insets.EMPTY);
                imageButton.setOnAction(event -> {
                    bookTile();
                });
            }
            bookedTiles.add(imageButton, i, 0);
        }
    }

    @FXML
    public void refuse() {
        GUI.refuseTile();
    }

    @FXML
    public void putTile02() {
        GUI.putTile(0, 2);
    }

    @FXML
    public void putTile04() {
        GUI.putTile(0, 4);
    }

    @FXML
    public void putTile11() {
        GUI.putTile(1, 1);
    }

    @FXML
    public void putTile12() {
        GUI.putTile(1, 2);
    }

    @FXML
    public void putTile13() {
        GUI.putTile(1, 3);
    }

    @FXML
    public void putTile14() {
        GUI.putTile(1, 4);
    }

    @FXML
    public void putTile15() {
        GUI.putTile(1, 5);
    }

    @FXML
    public void putTile20() {
        GUI.putTile(2, 0);
    }

    @FXML
    public void putTile21() {
        GUI.putTile(2, 1);
    }

    @FXML
    public void putTile22() {
        GUI.putTile(2, 2);
    }

    @FXML
    public void putTile23() {
        GUI.putTile(2, 3);
    }

    @FXML
    public void putTile24() {
        GUI.putTile(2, 4);
    }

    @FXML
    public void putTile25() {
        GUI.putTile(2, 5);
    }

    @FXML
    public void putTile26() {
        GUI.putTile(2, 6);
    }

    @FXML
    public void putTile30() {
        GUI.putTile(3, 0);
    }

    @FXML
    public void putTile31() {
        GUI.putTile(3, 1);
    }

    @FXML
    public void putTile32() {
        GUI.putTile(3, 2);
    }

    @FXML
    public void putTile33() {
        GUI.putTile(3, 3);
    }

    @FXML
    public void putTile34() {
        GUI.putTile(3, 4);
    }

    @FXML
    public void putTile35() {
        GUI.putTile(3, 5);
    }

    @FXML
    public void putTile36() {
        GUI.putTile(3, 6);
    }

    @FXML
    public void putTile40() {
        GUI.putTile(4, 0);
    }

    @FXML
    public void putTile41() {
        GUI.putTile(4, 1);
    }

    @FXML
    public void putTile42() {
        GUI.putTile(4, 2);
    }

    @FXML
    public void putTile44() {
        GUI.putTile(4, 4);
    }

    @FXML
    public void putTile45() {
        GUI.putTile(4, 5);
    }

    @FXML
    public void putTile46() {
        GUI.putTile(4, 6);
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
            Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
                imageButton.setText("MCS");
            } else {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageButton.setGraphic(imageView);
                imageButton.setPrefWidth(80);
                imageButton.setPrefHeight(80);
                imageButton.setMaxWidth(80);
                imageButton.setMaxHeight(80);
                imageButton.setPadding(Insets.EMPTY);
                if (index % 2 == 0) {
                    drawnTiles1.getChildren().add(imageButton);
                } else {
                    drawnTiles2.getChildren().add(imageButton);
                }
            }
            index++;
        }
    }
}
