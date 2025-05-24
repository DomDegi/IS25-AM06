package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class S_EndDrawTilesCardsController {

    @FXML
    public ProgressBar hourglass1;
    @FXML
    public ProgressBar hourglass2;
    @FXML
    public ProgressBar hourglass3;

    @FXML
    public Group flightImage;

    @FXML
    public ImageView shipImage;

    @FXML
    public Group trialFlight;

    @FXML
    public Polygon first;

    @FXML
    public Polygon second;

    @FXML
    public Polygon third;

    @FXML
    public Polygon fourth;

    @FXML
    public Polygon firstTrial;

    @FXML
    public Polygon secondTrial;

    @FXML
    public Polygon thirdTrial;

    @FXML
    public Polygon fourthTrial;

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
        if(GUI.getController().getGameMode()==GameMode.LEVEL2){
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            flightImage.setVisible(false);
            trialFlight.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }
        int index=1;
        if(GUI.getController().getGameMode()== GameMode.LEVEL2){
            for(Polygon p:new Polygon[]{first,second,third,fourth}){
                if(index>GUI.getController().getFlightBoard().getInGamePlayers().size())
                    p.setVisible(false);
                p.setStroke(rgb(255, 169, 19));
                p.setFill(Color.TRANSPARENT);
                p.setStrokeWidth(5);
                p.setEffect(new DropShadow(BlurType.GAUSSIAN,rgb(255, 169, 19), 30, 0.4, 0, 0));
                if(GUI.getController().getAvailablePosition().get(index)!=null) {
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.RED)
                        p.setFill(Color.RED);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.YELLOW)
                        p.setFill(Color.YELLOW);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.GREEN)
                        p.setFill(Color.GREEN);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.BLUE)
                        p.setFill(Color.BLUE);
                }
                index++;
            }
        } else if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            for(Polygon p:new Polygon[]{firstTrial,secondTrial,thirdTrial,fourthTrial}){
                if(index>GUI.getController().getFlightBoard().getInGamePlayers().size())
                    p.setVisible(false);
                p.setStroke(rgb(255, 169, 19));
                p.setFill(Color.TRANSPARENT);
                p.setStrokeWidth(5);
                p.setEffect(new DropShadow(BlurType.GAUSSIAN,rgb(255, 169, 19), 30, 0.4, 0, 0));
                if(GUI.getController().getAvailablePosition().get(index)!=null) {
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.RED)
                        p.setFill(Color.RED);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.YELLOW)
                        p.setFill(Color.YELLOW);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.GREEN)
                        p.setFill(Color.GREEN);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.BLUE)
                        p.setFill(Color.BLUE);
                }
                index++;
            }
        }
        tilesTable.getChildren().clear();
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
        index=0;
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
        if(GUI.getController().getGameMode()!=GameMode.TRIAL) {
            index = 0;
            for (Tile tile : GUI.getController().getLightShipBoard().getBookedTiles()) {
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
                    int finalI = index;
                    imageView.setOnMouseClicked(event -> {
                        drawBookedTile(finalI);
                    });
                    bookedTiles.add(imageView, index, 0);
                }
                index++;
            }
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
        int index=1;
        if(GUI.getController().getGameMode()== GameMode.LEVEL2){
            for(Polygon p:new Polygon[]{first,second,third,fourth}){
                if(index>GUI.getController().getFlightBoard().getInGamePlayers().size())
                    p.setVisible(false);
                p.setStroke(rgb(255, 169, 19));
                p.setFill(Color.TRANSPARENT);
                p.setStrokeWidth(5);
                p.setEffect(new DropShadow(BlurType.GAUSSIAN,rgb(255, 169, 19), 30, 0.4, 0, 0));
                if(GUI.getController().getAvailablePosition().get(index)!=null) {
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.RED)
                        p.setFill(Color.RED);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.YELLOW)
                        p.setFill(Color.YELLOW);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.GREEN)
                        p.setFill(Color.GREEN);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.BLUE)
                        p.setFill(Color.BLUE);
                }
                index++;
            }
        } else if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            for(Polygon p:new Polygon[]{firstTrial,secondTrial,thirdTrial,fourthTrial}){
                if(index>GUI.getController().getFlightBoard().getInGamePlayers().size())
                    p.setVisible(false);
                p.setStroke(rgb(255, 169, 19));
                p.setFill(Color.TRANSPARENT);
                p.setStrokeWidth(5);
                p.setEffect(new DropShadow(BlurType.GAUSSIAN,rgb(255, 169, 19), 30, 0.4, 0, 0));
                if(GUI.getController().getAvailablePosition().get(index)!=null) {
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.RED)
                        p.setFill(Color.RED);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.YELLOW)
                        p.setFill(Color.YELLOW);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.GREEN)
                        p.setFill(Color.GREEN);
                    if(GUI.getController().getAvailablePosition().get(index)== PlayersColor.BLUE)
                        p.setFill(Color.BLUE);
                }
                index++;
            }
        }
        if(GUI.getController().getGameMode()!= GameMode.TRIAL) {
            deck1.setVisible(GUI.getController().getAvailableDeck().get(1));
            deck2.setVisible(GUI.getController().getAvailableDeck().get(2));
            deck3.setVisible(GUI.getController().getAvailableDeck().get(3));
            drawnTiles1.getChildren().clear();
            drawnTiles2.getChildren().clear();
        }
        index=0;
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

    @FXML
    public void goProgressBar(int index,int max,int turns){
        double percentage= (double) index /max;
        if(turns==1){
            hourglass3.setProgress(1);
            hourglass2.setProgress(1);
            hourglass1.setProgress(1-percentage);
        }else if(turns==2){
            hourglass3.setProgress(1);
            hourglass2.setProgress(1-percentage);
        }else if(turns==3){
            hourglass3.setProgress(1-percentage);
        }
    }
}
