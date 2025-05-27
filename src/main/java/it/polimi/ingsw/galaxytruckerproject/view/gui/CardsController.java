package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Planet;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.galaxytruckerproject.client.CoordReqType.*;
import static javafx.scene.paint.Color.MAGENTA;
import static javafx.scene.paint.Color.rgb;

public class CardsController {

    @FXML
    public Label rolled=new Label("");

    @FXML
    public Polygon p0;
    @FXML
    public Polygon p1;
    @FXML
    public Polygon p2;
    @FXML
    public Polygon p3;
    @FXML
    public Polygon p4;
    @FXML
    public Polygon p5;
    @FXML
    public Polygon p6;
    @FXML
    public Polygon p7;
    @FXML
    public Polygon p8;
    @FXML
    public Polygon p9;
    @FXML
    public Polygon p10;
    @FXML
    public Polygon p11;
    @FXML
    public Polygon p12;
    @FXML
    public Polygon p13;
    @FXML
    public Polygon p14;
    @FXML
    public Polygon p15;
    @FXML
    public Polygon p16;
    @FXML
    public Polygon p17;
    @FXML
    public Polygon p18;
    @FXML
    public Polygon p19;
    @FXML
    public Polygon p20;
    @FXML
    public Polygon p21;
    @FXML
    public Polygon p22;
    @FXML
    public Polygon p23;
    @FXML
    public Polygon pt0;
    @FXML
    public Polygon pt1;
    @FXML
    public Polygon pt2;
    @FXML
    public Polygon pt3;
    @FXML
    public Polygon pt4;
    @FXML
    public Polygon pt5;
    @FXML
    public Polygon pt6;
    @FXML
    public Polygon pt7;
    @FXML
    public Polygon pt8;
    @FXML
    public Polygon pt9;
    @FXML
    public Polygon pt10;
    @FXML
    public Polygon pt11;
    @FXML
    public Polygon pt12;
    @FXML
    public Polygon pt13;
    @FXML
    public Polygon pt14;
    @FXML
    public Polygon pt15;
    @FXML
    public Polygon pt16;
    @FXML
    public Polygon pt17;

    @FXML
    public ImageView card;

    @FXML
    public ImageView deck;

    @FXML
    public Group flightImage;

    @FXML
    public ImageView shipImage;

    @FXML
    public Group trialFlight;

    @FXML
    public GridPane tilesTable;

    @FXML
    public VBox mainBox=new VBox();

    @FXML
    public Label text=new Label();

    @FXML
    public void initialize() {
        ClientState state = GUI.getController().getState();
        mainBox.getChildren().clear();
        text.setText("Waiting for other players...");
        modeChange();
        flightBoard();
        shipBoard(state);
        showCard();
        deck.setDisable(GUI.getController().getState()!=ClientState.DRAW_CARD);
        switch (state){
            case DRAW_CARD-> showDrawCard();
            case ACTION -> showAction();
            case PLANET_CHOICE -> showPlanetChoice();
            case MANAGE_GOODS -> showManageGoods();
            case COORD_REQUEST -> showCoordRequest();
            case ROLL_DICE -> showRollDice();
            case WAIT_TO_DRAW-> text.setText("Waiting for other players...");
        }
    }

    @FXML
    public void draw() {
        waitOthers();
        GUI.drawCard();
    }

    @FXML
    public void yes() {
        waitOthers();
        GUI.action(true);
    }

    @FXML
    public void no() {
        waitOthers();
        GUI.action(false);
    }

    @FXML
    public void roll() {
        waitOthers();
        GUI.roll();
    }

    @FXML
    public void doneCoord(){
        waitOthers();
        GUI.doneCoord();
    }

    @FXML
    public void choosePlanet(int index){
        waitOthers();
        GUI.choosePlanet(index);
    }

    private void waitOthers(){
        mainBox.getChildren().clear();
        text.setText("Waiting for other players...");
    }

    private void showCard(){
        if(!GUI.displayableCards().isEmpty())
            card.setImage(loadImage(GUI.displayableCards().getFirst().getFilePath()));
    }

    private Image loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return null;
        }
        return new Image(stream);
    }

    private void modeChange(){
        if(GUI.getController().getGameMode()==GameMode.LEVEL2){
            trialFlight.setVisible(false);
            flightImage.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1b.jpg"))));
        } else if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            deck.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cards/GT-cards_I_IT_0121.jpg"))));
            flightImage.setVisible(false);
            trialFlight.setVisible(true);
            shipImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grafiche/grafiche/cardboard/cardboard-1.jpg"))));
        }
    }

    private void flightBoard(){
        int max=24;
        Polygon[] polygons = new Polygon[]{p0,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23};
        if (GUI.getController().getGameMode()==GameMode.TRIAL) {
            polygons=new Polygon[]{pt0,pt1,pt2,pt3,pt4,pt5,pt6,pt7,pt8,pt9,pt10,pt11,pt12,pt13,pt14,pt15,pt16,pt17};
            max=18;
        }
        int index=0;
        for(Polygon p:polygons){
            p.setStroke(rgb(255, 169, 19));
            p.setFill(Color.TRANSPARENT);
            p.setStrokeWidth(5);
            p.setEffect(new DropShadow(BlurType.GAUSSIAN,rgb(255, 169, 19), 30, 0.4, 0, 0));
            for (LightPlayer player : GUI.getController().getFlightBoard().getInGamePlayers()) {
                if (index*((player.getPosition() /max)+1)==player.getPosition()) {
                    if (player.getPlayerColor().equals(PlayersColor.RED))
                        p.setFill(Color.RED);
                    if (player.getPlayerColor().equals(PlayersColor.YELLOW))
                        p.setFill(Color.YELLOW);
                    if (player.getPlayerColor().equals(PlayersColor.GREEN))
                        p.setFill(Color.GREEN);
                    if (player.getPlayerColor().equals(PlayersColor.BLUE))
                        p.setFill(Color.BLUE);
                }
            }
            index++;
        }
    }

    private void shipBoard(ClientState state){
        tilesTable.getChildren().clear();
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
                imageView.setDisable(true);
                switch (state){
                    case COORD_REQUEST-> {
                        imageView.setDisable(false);
                        imageView.setOnMouseClicked(event -> {
                            GUI.selectTile(tile.getCoordinates().getX(), tile.getCoordinates().getY());
                            initialize();
                        });
                    }
                    case MANAGE_GOODS -> {
                    }
                }
                tilesTable.add(imageView,tile.getCoordinates().getY(),tile.getCoordinates().getX());
            }
        }
    }

    //Loaders---------------------
    private void showDrawCard() {
        Platform.runLater(() -> {
            mainBox.setVisible(false);
            text.setText("Draw a Card");
        });
    }

    private void showCoordRequest() {
        Platform.runLater(() -> {
            CoordReqType type = GUI.getController().getCoordInputManager().getCoordReqType();
            if(type==CHOOSE_TO_BREAK||type==CHOOSE_BATTERY||type==CHOOSE_DOUBLE_CANNON||type==CHOOSE_DOUBLE_ENGINE){
                Button doneButton=new Button("Done");
                doneButton.setPrefSize(100,75);
                doneButton.setOnAction(_->doneCoord());
                mainBox.getChildren().add(doneButton);
            }
            text.setText(type.toString());
        });
    }

    private void showAction() {
        Platform.runLater(() -> {
            Button yesButton=new Button("Yes");
            yesButton.setPrefSize(100,75);
            yesButton.setOnAction(_->yes());
            Button noButton=new Button("no");
            noButton.setPrefSize(100,75);
            noButton.setOnAction(_->no());
            mainBox.getChildren().addAll(yesButton,noButton);
            text.setText("Accept?");
        });
    }

    private void showRollDice(){
        Platform.runLater(() -> {
            Button rollButton=new Button("Roll");
            rollButton.setPrefSize(100,75);
            rollButton.setOnAction(_->roll());
            mainBox.getChildren().add(rollButton);
            rolled.setPrefSize(100,50);
            mainBox.getChildren().add(rolled);
            text.setText("Roll the Dices");
        });
    }

    public void showRoll(int index){
        if(mainBox.getChildren().size()<=1) {
            mainBox.getChildren().clear();
            mainBox.getChildren().add(rolled);
        }
        rolled.setPrefSize(100,50);
        rolled.setText(index+"");
    }

    private void showPlanetChoice(){
        Platform.runLater(() -> {
            int index=0;
            mainBox.getChildren().clear();
            for(Planet planet:GUI.displayableCards().getFirst().getListOfPlanets()){
                Button button= new Button();
                button.setPrefSize(75,75);
                button.setText((index+1)+"° Planet ");
                int finalIndex = index;
                button.setOnAction(_ -> choosePlanet(finalIndex));
                button.setDisable(planet.getOccupationStatus());
                mainBox.getChildren().add(button);
                index++;
            }
            text.setText("Choose a Planet");
        });
    }

    private void showManageGoods(){
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/gui/manageGoods.fxml"));
            VBox box;
            try {
                box = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mainBox= box;
            text.setText("Manage your Goods");
        });
    }
}