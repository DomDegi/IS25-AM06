package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

public class S_EndDrawTilesCardsController {
    private int index=0;

    @FXML
    public GridPane tilesTable;

    @FXML
    public GridPane bookedTiles;

    @FXML
    public TilePane drawnTiles1;

    @FXML
    public TilePane drawnTiles2;

    @FXML
    public void initialize() {}

    @FXML
    public void drawTile(){
        GUI.drawTile();
    }

    @FXML
    public void drawBookedTile1(){
        GUI.drawBookedTile(0);
    }

    public void drawBookedTile2(){
        GUI.drawBookedTile(1);
    }

    @FXML
    public void drawDrawnTile(){
        GUI.drawTile();
    }

    @FXML
    public void drawDeck1(){
        GUI.drawDeck(0);
    }

    @FXML
    public void drawDeck2(){
        GUI.drawDeck(1);
    }

    @FXML
    public void drawDeck3(){
        GUI.drawDeck(2);
    }

    @FXML
    public void endShip(){
        GUI.endShip();
    }

    @FXML
    public void turnHourglass(){
        GUI.turnHourglass();
    }

    @FXML
    public void checkShip1(){
        GUI.checkShip(1);
    }

    @FXML
    public void checkShip2(){
        GUI.checkShip(2);
    }

    @FXML
    public void checkShip3(){
        GUI.checkShip(3);
    }
}
