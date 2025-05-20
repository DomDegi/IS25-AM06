package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
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
        GUI.clear();
        switch (GUI.getController().getState()){
            case S_END_DRAW_TILE_CARD ->{
                GUI.showS_EndDrawTilesCards();
            }
            case S_MANAGE_DRAWN_TILE ->{
                GUI.showS_ManageDrawTilesCards();
            }
            case S_MANAGE_CARDS -> {
                GUI.showDeckCheck();
            }
            case S_FINISHED -> {

            }
            case DRAW_CARD -> {

            }
            case ACTION -> {

            }
            case PLANET_CHOICE -> {

            }
            case MANAGE_GOODS -> {

            }
            case MANAGE_CABINS ->{

            }
            case COORD_REQUEST -> {

            }
            case ROLL_DICE -> {

            }
            case WAIT -> {
                GUI.showWait();
            }
        }
    }

    public void setPlayer(LightShipBoard player) {
        ArrayList<Tile> tiles=new ArrayList<>();
        for(int i=0;i<=6;i++){
            for(int j=0;j<=4;j++) {
                if(player.getTilesTable()[j][i].isPresent()&&player.getTilesTable()[j][i].get().fillable())
                    tiles.add(player.getTilesTable()[j][i].get());
            }
        }
        for(Tile tile:tiles){
            javafx.scene.control.Button imageButton = new Button();
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("Impossibile trovare l'immagine: " + imagePath);
                imageButton.setText("Img non trovata");
            } else {
                javafx.scene.image.Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation()*90);
                imageView.setFitWidth(136);
                imageView.setFitHeight(136);
                imageButton.setGraphic(imageView);
                imageButton.setPrefWidth(136);
                imageButton.setPrefHeight(136);
                imageButton.setMaxWidth(136);
                imageButton.setMaxHeight(136);
                imageButton.setPadding(Insets.EMPTY);
                tilesTable.add(imageButton,tile.getCoordinates().getY(),tile.getCoordinates().getX());
            }
        }
    }
}
