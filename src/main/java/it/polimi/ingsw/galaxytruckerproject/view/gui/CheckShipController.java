package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Controller for handling the ship checking phase in the Galaxy Trucker game's GUI.
 * <p>
 * This controller displays the shipboard of the player and allows the player to check their ship.
 * It also handles updating the displayed ship image and tiles based on the game mode (LEVEL2 or TRIAL).
 * </p>
 */
public class CheckShipController {

    @FXML
    public ImageView shipImage;
    @FXML
    public GridPane tilesTable;

    /**
     * Finalizes the ship checking phase.
     * This method updates the client state and resets the checking status in the controller.
     */
    @FXML
    public void done() {
        GUI.displayClientState(GUI.getController().getState());
        GUI.getController().setChecking(null);
    }

    /**
     * Sets the player's shipboard for display in the GUI.
     * This method updates the ship image and tiles based on the current game mode (LEVEL2 or TRIAL).
     * It then populates the ship's tiles in the grid based on the shipboard information.
     *
     * @param shipBoard the shipboard of the player to be displayed
     */
    public void setPlayer(LightShipBoard shipBoard) {
        // Set the ship image based on the game mode
        shipBoard.setGetStat();
        if (GUI.getController().getGameMode() == GameMode.LEVEL2) {
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode() == GameMode.TRIAL) {
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }

        // Clear the tiles table and populate it with the ship's tiles
        tilesTable.getChildren().clear();
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 4; j++) {
                if (shipBoard.getTilesTable()[j][i].isPresent() && shipBoard.getTilesTable()[j][i].get().fillable()) {
                    tiles.add(shipBoard.getTilesTable()[j][i].get());
                }
            }
        }

        // Add each tile to the grid
        for (Tile tile : tiles) {
            StackPane stackPane=new StackPane();
            stackPane.setPrefSize(80,80);
            HBox hBox=new HBox();
            hBox.setPrefSize(136,136);
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER);
            VBox vBox=new VBox();
            vBox.setPrefSize(136,136);
            vBox.setSpacing(5);
            vBox.setAlignment(Pos.CENTER);
            HBox hBox1=new HBox();
            hBox1.setPrefSize(136,136);
            hBox1.setSpacing(5);
            hBox1.setAlignment(Pos.CENTER);
            String imagePath = tile.getImagePath();
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("not found" + imagePath);
            } else {
                javafx.scene.image.Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.rotateProperty().setValue(tile.getRotation() * 90);
                imageView.setFitWidth(136);
                imageView.setFitHeight(136);
                imageView.setDisable(true);
                stackPane.getChildren().add(imageView);
                for (int i=tile.getCrew();i!=0;i--){
                    ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/human.png"))));
                    switch (tile.getCrewType()) {
                        case HUMAN -> element= new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/human.png"))));
                        case BROWN -> element= new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/brown.png"))));
                        case PURPLE ->element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/purple.png"))));
                    }
                    element.setDisable(true);
                    element.setFitWidth(51);
                    element.setFitHeight(51);
                    hBox.getChildren().add(element);
                }
                for (int i=tile.getNumBatteries();i!=0;i--){
                    ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/battery.png"))));
                    element.setDisable(true);
                    element.setFitWidth(51);
                    element.setFitHeight(102);
                    hBox.rotateProperty().setValue(tile.getRotation()*90);
                    hBox.getChildren().add(element);
                }
                if(tile.getCargo()!=null&&!tile.getCargo().isEmpty()) {
                    ArrayList<Goods> cargo = tile.getCargo();
                    if (tile.getCargo().size()<=2){
                        for (int i=cargo.size();i!=0;i--){
                            ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                            switch (cargo.get(i-1).getColor()){
                                case RED -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                                case YELLOW -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoYellow.png"))));
                                case GREEN -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoGreen.png"))));
                                case BLUE -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoBlue.png"))));
                            }
                            element.setDisable(true);
                            element.setFitWidth(34);
                            element.setFitHeight(34);
                            hBox.getChildren().add(element);
                        }
                        stackPane.getChildren().add(hBox);
                    }else{
                        for (int i=cargo.size();i>2;i--){
                            ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                            switch (cargo.get(i-1).getColor()){
                                case RED -> element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoRed.png")));
                                case YELLOW -> element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoYellow.png")));
                                case GREEN ->element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoGreen.png")));
                                case BLUE -> element=new ImageView(new Image(getClass().getResourceAsStream("/images/grafiche/cargoBlue.png")));
                            }
                            element.setDisable(true);
                            element.setFitWidth(34);
                            element.setFitHeight(34);
                            hBox1.getChildren().add(element);
                            hBox1.setPrefSize(68,68);
                        }for (int i=cargo.size()-2;i!=0;i--){
                            ImageView element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                            switch (cargo.get(i-1).getColor()){
                                case RED -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoRed.png"))));
                                case YELLOW -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoYellow.png"))));
                                case GREEN ->element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoGreen.png"))));
                                case BLUE -> element=new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/cargoBlue.png"))));
                            }
                            element.setDisable(true);
                            element.setFitWidth(34);
                            element.setFitHeight(34);
                            hBox.getChildren().add(element);
                            hBox1.setPrefSize(68,68);
                        }
                        vBox.rotateProperty().setValue(tile.getRotation()*90);
                        vBox.getChildren().addAll(hBox1,hBox);
                        stackPane.getChildren().add(vBox);
                    }
                } else {
                    stackPane.getChildren().add(hBox);
                }
                tilesTable.add(stackPane, tile.getCoordinates().getY(), tile.getCoordinates().getX());
            }
        }
    }
}
