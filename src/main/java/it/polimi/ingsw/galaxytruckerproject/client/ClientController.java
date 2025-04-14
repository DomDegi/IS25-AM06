package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class ClientController {

    private ClientState state;
    private int turns;
    private VirtualController virtualController;
    private ArrayList<Goods> goodsList;
    private final Client client;
    private int indexDeckInHandOrPlanet;
    private int indexCard;
    private final CoordInputManager coordInputManager;
    private LightShipBoard lightShipBoard;

    public ClientController(Client client) {
        this.client = client;
        this.indexCard = 0;
        this.indexDeckInHandOrPlanet = 0;
        this.turns = 0;
        state = ClientState.LOBBY;
        this.coordInputManager=new CoordInputManager(lightShipBoard,this);
    }

    public VirtualController getVirtualController() {
        return virtualController;
    }

    public void input(String input) throws RemoteException {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        if (words.length == 0) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return;
        }
        if(words[0].isEmpty()){
            System.out.println("Empty input");
            return;
        }
        switch (state) {
            case LOBBY: {
                //CHOOSE TUI OR GUI
                switch(words[0]) {
                    case "createGame"-> {
                        String gameName = words[1];
                        int NumberOFPlayers;
                        try {
                            NumberOFPlayers = Integer.parseInt(words[2]);
                        } catch (NumberFormatException e) {
                            System.out.println("\nInvalid input format. Please provide integer values.");
                            return;
                        }

                        if(NumberOFPlayers<2||NumberOFPlayers>4)
                        {
                            System.out.println("\nInvalid input format. Please provide a different number of players.");
                        }
                        if(words[3].equals("TrialMode")){
                            virtualController.createGame(gameName,NumberOFPlayers, GameMode.TRIAL, client.getName());
                            return;
                        }
                        else if (words[3].equals("Level2Mode")){
                            virtualController.createGame(gameName,NumberOFPlayers,GameMode.LEVEL2, client.getName());
                            return;
                        }
                        System.out.println("\nInvalid input format. Please provide correct game mode.");
                    }
                    case "joinGame"-> {
                        String gameName = words[1];
                        virtualController.joinGame(gameName,client.getName());
                    }
                }

            }

            case ACTION: {
                if (checkShipBoards(words))
                    return;
                if (land(words))
                    return;
                switch (words[0]){
                    case "yes" ->
                        virtualController.sendYes(client.getName());
                    case "no" ->
                        virtualController.sendNo(client.getName());
                }
            }

            case PLANET_CHOICE: {
                int chose;
                if (checkShipBoards(words))
                    return;
                if (land(words))
                    return;
                if (words[0].equals("no")) {
                    virtualController.sendNo(client.getName());
                    return;
                }
                try {
                    chose = Integer.parseInt(words[0]);
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input format. Please provide integer values.");
                    return;
                }
                if (chose > 0 && chose </*num pianeti*/) {
                    state = ClientState.MANAGE_GOODS;
                    indexDeckInHandOrPlanet = chose;
                    //notify choose planet
                    return;
                }
            }

            case MANAGE_GOODS: {
                GoodsManager goodsManager = new GoodsManager(client.getMe(), goodsList);
                if(land(words))
                    return;
                if (checkShipBoards(words))
                    return;
                HashSet newTilesGoods = goodsManager.getReward(words);
                ArrayList<CargoHold> newTiles;
                if (!newTilesGoods.isEmpty()) {
                    newTiles = new ArrayList<>(newTilesGoods);
                }
                else{
                    newTiles= new ArrayList<>();
                }
                int goodsVal = lightShipBoard.convertGoodsToCredit();
                virtualController.notifyNewGoodsArrangement(client.getName(),goodsVal, newTiles);

            }

            case COORD_REQUEST:{
                if (land(words))
                    return;
                if(words[0].equals("done")) {
                    coordInputManager.endCheckingFase();
                }
                else
                    coordInputManager.checkCoord(transformCoordinates(words));
            }

            case MANAGE_CARDS: {
                switch (words[0]) {
                    case "previous" -> {
                        indexCard=indexCard-1;
                        if(indexCard<0)
                            indexCard=2;
                        client.getDeck().get(indexDeckInHandOrPlanet).get(indexCard);
                        return;
                    }
                    case "next" -> {
                        indexCard=indexCard+1;
                        if(indexCard>2)
                            indexCard=0;
                        client.getDeck().get(indexDeckInHandOrPlanet).get(indexCard);
                        return;
                    }
                    case "done" -> {

                        //done action
                        return;
                    }
                }
            }

            case S_END_DRAW_TILE_CARD: {
                if(firstTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case"done" -> {
                        state = ClientState.S_FINISHED;
                        virtualController.sendEndShipBoardCreation(client.getName());
                        return;
                    }
                    case "draw" -> {
                        switch (words[1]) {
                            case "card" -> {
                                int chose;
                                if (words[2].isEmpty()) {
                                    System.out.println("Choose one of the decks");
                                    return;
                                }
                                try {
                                    chose = Integer.parseInt(words[2]);
                                } catch (NumberFormatException e) {
                                    System.out.println("\nInvalid input format. Please provide integer values.");
                                    return;
                                }
                                if (chose > 0 && chose < 4) {
                                    state = ClientState.MANAGE_CARDS;
                                    indexDeckInHandOrPlanet = chose;
                                    //notify drawn card
                                    virtualController;
                                    ArrayList<Card> displayedCards = client.getDeck().get(indexDeckInHandOrPlanet);
                                    return;
                                }
                                return;
                            }
                            case "tile" -> {
                                state = ClientState.S_MANAGE_DRAWN_TILE;
                                Tile newTile;
                                switch (words[2]){
                                    case "new"->{
                                        virtualController.reqDrawTileFromPile(client.getName());
                                        newTile=/*drawn tile*/;
                                    }
                                    case "b1" ->{
                                        newTile=client.getMe().getShipBoard().getBookedTiles().getFirst();
                                        client.getMe().getShipBoard().removeBookedTile(0);
                                    }
                                    case "b2" ->{
                                        newTile=client.getMe().getShipBoard().getBookedTiles().get(1);
                                        client.getMe().getShipBoard().removeBookedTile(1);
                                    }
                                    default-> {
                                        int chose;
                                        if (words[2].isEmpty()) {
                                            System.out.println("Choose one of the decks");
                                            return;
                                        }
                                        try {
                                            chose = Integer.parseInt(words[2]);
                                        } catch (NumberFormatException e) {
                                            System.out.println("\nInvalid input format. Please provide integer values.");
                                            return;
                                        }
                                        if (chose>0&&chose<client.getDrawnTiles().size()) {
                                            indexDeckInHandOrPlanet = chose;
                                            virtualController.reqDrawTileFromTable(client.getName(),chose);
                                            newTile=client.getDrawnTiles().get(indexDeckInHandOrPlanet);
                                        }else {
                                            System.out.println("\nInvalid input format. Please provide integer values.");
                                            return;
                                        }
                                    }
                                }
                                client.setTileInHand(newTile);
                                return;
                            }
                        }
                    }
                }
            }

            case S_MANAGE_DRAWN_TILE: {
                if(firstTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case "rotate" -> {
                        client.getTileInHand().rotate();
                    }
                    case "position" -> {
                        for (int i = 0; i < words.length - 1; i++) {
                            words[i] = words[i + 1];
                        }
                        Coordinates coordinates=transformCoordinates(words);
                        if(coordinates!=null){
                            client.getMe().getShipBoard().positionTile(Optional.ofNullable(client.getTileInHand()),coordinates);
                            virtualController.notifySetTile(client.getName(),coordinates,client.getTileInHand());
                        }
                        return;
                    }
                    case "refuse" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        if(client.getTileInHand().isBooked()){
                            client.getMe().getShipBoard().addBookedTile(client.getTileInHand());
                            return;
                        }
                        client.getDrawnTiles().put( client.getTileInHand().getKey(),client.getTileInHand());
                        virtualController.notifyRefusedTile(client.getName());
                        return;
                    }
                    case "book" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        client.getTileInHand().setBooked(true);
                        client.getMe().getShipBoard().addBookedTile(client.getTileInHand());
                        virtualController.notifyTileBooking(client.getName());
                        return;
                    }
                }
            }

            case S_FINISHED: {
                if (checkShipBoards(words))
                    return;
                if (words[0].equals("turn") && turns < 2) {
                    turns++;
                    virtualController.sendTurnHourGlass(client.getName());
                }
            }
            case WAIT_OTHER_PLAYER_ACTION:{
                if (land(words))
                    return;
                if (checkShipBoards(words))
                    return;
            }
        }
    }

    public String getName() {
        return client.getName();
    }

    public Coordinates transformCoordinates(String[] input) {
        if (input.length < 2) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        int CoordinatesX;
        try {
            CoordinatesX = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        if(CoordinatesX<0||CoordinatesX>4){
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        int CoordinatesY;
        try {
            CoordinatesY = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid coordinates number.");
            return null;
        }
        if(CoordinatesY<0||CoordinatesY>6){
            System.out.println("\nInvalid coordinates number.");
            return null;
        }
        return new Coordinates(CoordinatesX, CoordinatesY);
    }

    private boolean checkShipBoards(String[] input) {
        if (input[0].equals("check")) {
            int chose;
            if (input[1].isEmpty()) {
                System.out.println("Choose one of the shipboards");
                return false;
            }
            try {
                chose = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return false;
            }
            if (chose == 0) {
                System.out.println(client.getMe().getShipBoard());
            }
            if (chose >= 1 && chose <= 4) {
                System.out.println(client.getPlayersList().get(indexDeckInHandOrPlanet).getShipBoard());
            }
            return true;
        }
        return false;
    }

    private boolean firstTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && turns==0) {
            turns++;
            virtualController.sendTurnHourGlass(client.getName());
            return true;
        }
        return false;
    }

    private boolean land(String[] input) throws RemoteException {
        if (input[0].equals("earlyland")&&!client.getMe().isLanded()){
            virtualController.notifyEarlyLanding(client.getName());
            return true;
        }
        return false;
    }
    public void setGoodsList(ArrayList<Goods> goodsList) {
        this.goodsList = goodsList;
    }

}
