package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

public class S_ManageDrawTileController {
    private int index=0;

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
    public void refuseTile(){
        Button button = new Button();
        button.setOnAction(event -> {
            System.out.println("Button clicked! (Lambda)");
        });
        if(index%2==0){
            drawnTiles1.getChildren().add(button);
        }else{
            drawnTiles2.getChildren().add(button);
        }
        index++;
        GUI.refuseTile();
    }

    @FXML
    public void putTile02(){
        GUI.putTile(0,2);
    }

    @FXML
    public void putTile04(){
        GUI.putTile(0,4);
    }
    @FXML
    public void putTile11(){
        GUI.putTile(1,1);
    }
    @FXML
    public void putTile12(){
        GUI.putTile(1,2);
    }
    @FXML
    public void putTile13(){
        GUI.putTile(1,3);
    }

    @FXML
    public void putTile14(){
        GUI.putTile(1,4);
    }

    @FXML
    public void putTile15(){
        GUI.putTile(1,5);
    }

    @FXML
    public void putTile20(){
        GUI.putTile(2,0);
    }

    @FXML
    public void putTile21(){
        GUI.putTile(2,1);
    }

    @FXML
    public void putTile22(){
        GUI.putTile(2,2);
    }

    @FXML
    public void putTile23(){
        GUI.putTile(2,3);
    }

    @FXML
    public void putTile24(){
        GUI.putTile(2,4);
    }

    @FXML
    public void putTile25(){
        GUI.putTile(2,5);
    }

    @FXML
    public void putTile26(){
        GUI.putTile(2,6);
    }

    @FXML
    public void putTile30(){
        GUI.putTile(3,0);
    }

    @FXML
    public void putTile31(){
        GUI.putTile(3,1);
    }

    @FXML
    public void putTile32(){
        GUI.putTile(3,2);
    }

    @FXML
    public void putTile33(){
        GUI.putTile(3,3);
    }

    @FXML
    public void putTile34(){
        GUI.putTile(3,4);
    }

    @FXML
    public void putTile35(){
        GUI.putTile(3,5);
    }

    @FXML
    public void putTile36(){
        GUI.putTile(3,6);
    }

    @FXML
    public void putTile40(){
        GUI.putTile(4,0);
    }

    @FXML
    public void putTile41(){
        GUI.putTile(4,1);
    }

    @FXML
    public void putTile42(){
        GUI.putTile(4,2);
    }

    @FXML
    public void putTile44(){
        GUI.putTile(4,4);
    }

    @FXML
    public void putTile45(){
        GUI.putTile(4,5);
    }

    @FXML
    public void putTile46(){
        GUI.putTile(4,6);
    }

    @FXML
    public void bookTile(){
        GUI.bookTile();
    }

    @FXML
    public void rotateDx(){
        drawnTile.rotateProperty().add(-90);
        GUI.rotate();
    }

    @FXML
    public void rotateSx(){
        drawnTile.rotateProperty().add(90);
        GUI.rotate();
        GUI.rotate();
        GUI.rotate();
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
