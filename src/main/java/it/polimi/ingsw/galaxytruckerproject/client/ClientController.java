package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.*;

public class ClientController {

    private LightPlayer me;
    private ViewInterface view;
    private VirtualController controller;
    private ArrayList<LightPlayer> playersList;
    private ArrayList<ArrayList<Card>> deck;
    private LightFlightboard flightBoard;
    private HashMap<Integer, Tile> turnedTiles;
    private Tile tileInHand;
    private Map<Integer,Tile> TurnedTiles;
    private ClientState state;
    private int turns;
    private final VirtualController virtualController;
    private ArrayList<Goods> goodsList;
    private int indexDeckInHandOrPlanet;
    private int indexCard;
    private final CoordInputManager coordInputManager;
    private LightShipBoard lightShipBoard;
    private String name;
    private Card displayedCard;
    private boolean inGame = false;

    public ClientController(VirtualController virtualController) {
        this.indexCard = 0;
        this.virtualController = virtualController;
        this.indexDeckInHandOrPlanet = 0;
        this.turns = 0;
        this.goodsList = new ArrayList<>();
        state = ClientState.CHOOSE_UI;
        this.coordInputManager=new CoordInputManager(lightShipBoard,this);
    }

    public VirtualController getVirtualController() {
        return virtualController;
    }

    public void input(String input) throws RemoteException {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        if (words.length == 0||words[0].isEmpty()) {
            System.out.println("Empty input");
            return;
        }

        switch (state) {
            case CHOOSE_UI->{
                switch(words[0]) {
                    case "gui"->
                        this.view=new GUI();
                    case "tui"->
                        this.view=new TUI();
                    default->{
                        System.out.println("Wrong input");
                        return;
                    }
                }
                state = ClientState.CHOOSE_CONNECTION_TYPE;
            }

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi"->
                        //rmi
                    case "socket"->
                    //socket
                    default->{
                        System.out.println("Wrong input");
                        return;
                    }
                }
                state = ClientState.LOBBY;
            }

            case LOBBY-> {
                //CHOOSE TUI OR GUI
                switch(words[0]) {
                    case "createGame"-> {
                        String gameName = words[1];
                        int NumberOfPlayers=numerate(scroll(words,2));
                        if(NumberOfPlayers==-1)
                            return;
                        if(words[3].equals("TrialMode")){
                            virtualController.createGame(gameName,NumberOfPlayers, GameMode.TRIAL, this.name);
                            state = ClientState.LOGIN;
                            return;
                        } else if (words[3].equals("Level2Mode")){
                            virtualController.createGame(gameName,NumberOfPlayers,GameMode.LEVEL2, this.name);
                            state = ClientState.LOGIN;
                            return;
                        }
                        System.out.println("\nInvalid input format. Please provide correct game mode.");
                    }
                    case "joinGame"-> {
                        String gameName = words[1];
                        virtualController.joinGame(gameName,this.name);
                    }
                }
            }

            case LOGIN->{
                switch (words[0]) {
                    case "done" -> {
                        if(!Objects.equals(this.name, ""))
                            virtualController.joinGame(words[1],this.name);
                    }
                    case "redo"->
                            this.name=("");
                    default->
                            this.name=(words[0]);
                }
            }

            case COLOR_CHOICE->{
                PlayersColor color;
                switch (words[0]){
                    case "red"->{
                        color = PlayersColor.RED;
                    }
                    case "yellow"->{
                        color = PlayersColor.YELLOW;
                    }
                    case "green"->{
                        color = PlayersColor.GREEN;
                    }
                    case "blue"->{
                        color = PlayersColor.BLUE;
                    }
                }
            }

            case ACTION-> {
                if (checkShipBoards(words))
                    return;
                if (land(words))
                    return;
                switch (words[0]){
                    case "yes" ->
                            virtualController.sendYes(this.name);
                    case "no" ->
                            virtualController.sendNo(this.name);
                }
            }

            case PLANET_CHOICE-> {
                if (checkShipBoards(words))
                    return;
                if (land(words))
                    return;
                if (words[0].equals("no")) {
                    virtualController.sendNo(this.name);
                    return;
                }
                int chose=numerate(words);
                if(chose==-1)
                    return;
                if (chose > 0 && chose </*num pianeti*/) {
                    state = ClientState.MANAGE_GOODS;
                    indexDeckInHandOrPlanet = chose;
                    //notify choose planet
                }
            }

            case MANAGE_GOODS-> {
                GoodsManager goodsManager = new GoodsManager(this.me, goodsList);
                if(land(words))
                    return;
                if (checkShipBoards(words))
                    return;
                HashSet<CargoHold> newTilesGoods = goodsManager.getReward(words);
                ArrayList<CargoHold> newTiles;
                if (!newTilesGoods.isEmpty()) {
                    newTiles = new ArrayList<>(newTilesGoods);
                    if (newTiles.getFirst().getTotSpaces()==-1 ){
                        newTiles.clear();
                    }
                    int goodsVal = lightShipBoard.convertGoodsToCredit();
                    virtualController.notifyNewGoodsArrangement(this.name,goodsVal, newTiles);
                }
            }

            case COORD_REQUEST->{
                if (land(words))
                    return;
                if(words[0].equals("done")) {
                    coordInputManager.endCheckingFase();
                }
                else
                    coordInputManager.checkCoord(transformCoordinates(words));
            }

            case S_MANAGE_CARDS -> {
                switch (words[0]) {
                    case "previous" -> {
                        indexCard=indexCard-1;
                        if(indexCard<0)
                            indexCard=2;
                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).get(indexCard);
                    }
                    case "next" -> {
                        indexCard=indexCard+1;
                        if(indexCard>2)
                            indexCard=0;
                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).get(indexCard);
                    }
                    case "done" -> {
                        //done action
                        virtualController;
                    }
                }
            }

            case S_END_DRAW_TILE_CARD-> {
                virtualController.show
                if(firstTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case"done" -> {
                        state = ClientState.S_FINISHED;
                        virtualController.sendEndShipBoardCreation(this.name);
                    }
                    case "draw" -> {
                        switch (words[1]) {
                            case "card" -> {
                                int chose=numerate(scroll(words,2));
                                if (chose==-1)
                                    return;
                                if (chose > 0 && chose < 4) {
                                    state = ClientState.S_MANAGE_CARDS;
                                    indexDeckInHandOrPlanet = chose;
                                    //notify drawn card
                                    virtualController;
                                    displayedCard = this.deck.get(indexDeckInHandOrPlanet).getFirst();
                                }
                            }
                            case "tile" -> {
                                state = ClientState.S_MANAGE_DRAWN_TILE;
                                Tile newTile;
                                switch (words[2]){
                                    case "new"->{
                                        virtualController.reqDrawTileFromPile(this.name);
                                        newTile=/*drawn tile*/;
                                    }
                                    case "b1" ->{
                                        newTile=this.me.getShipBoard().getBookedTiles().getFirst();
                                        this.me.getShipBoard().removeBookedTile(0);
                                    }
                                    case "b2" ->{
                                        newTile=this.me.getShipBoard().getBookedTiles().get(1);
                                        this.me.getShipBoard().removeBookedTile(1);
                                    }
                                    default-> {
                                        int chose=numerate(scroll(words,2));
                                        if (chose==-1)
                                            return;
                                        if (chose>0&&chose<this.turnedTiles.size()) {
                                            indexDeckInHandOrPlanet = chose;
                                            virtualController.reqDrawTileFromTable(this.name,chose);
                                            newTile=this.turnedTiles.get(indexDeckInHandOrPlanet);
                                        }else {
                                            System.out.println("\nInvalid input format. Please provide integer values.");
                                            return;
                                        }
                                    }
                                }
                                this.tileInHand=(newTile);
                            }
                        }
                    }
                }
            }

            case S_MANAGE_DRAWN_TILE-> {
                if(firstTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case "rotate" ->
                        this.tileInHand.rotate();
                    case "position" -> {
                        Coordinates coordinates=transformCoordinates(scroll(words,1));
                        if(coordinates!=null){
                            this.me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand),coordinates);
                            virtualController.notifySetTile(this.name,coordinates,this.tileInHand);
                        }
                    }
                    case "refuse" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        if(this.tileInHand.isBooked()){
                            this.me.getShipBoard().addBookedTile(this.tileInHand);
                            return;
                        }
                        this.turnedTiles.put(this.tileInHand.getKey(),this.tileInHand);
                        virtualController.notifyRefusedTile(this.name);
                    }
                    case "book" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        this.tileInHand.setBooked(true);
                        this.me.getShipBoard().addBookedTile(this.tileInHand);
                        virtualController.notifyTileBooking(this.name);
                    }
                }
            }

            case S_FINISHED-> {
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case "turn" -> {
                        if (turns<2){
                            turns++;
                            virtualController.sendTurnHourGlass(this.name);
                        }
                    }
                    case "1"->{
                        if (playersList.size()>1){
                            //add to flightBoard
                        }
                    }
                    case "2"->{
                        if (playersList.size()>2){
                            //add to flightBoard
                        }
                    }
                    case "3"->{
                        if (playersList.size()>3){
                            //add to flightBoard
                        }
                    }
                    case "4"->{
                        if (playersList.size()>4){
                            //add to flightBoard
                        }
                    }
                    default->
                        System.out.println("\nInvalid input format");
                }
            }
            case WAIT_OTHER_PLAYER_ACTION->{
                if (land(words))
                    return;
                checkShipBoards(words);
            }
        }
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
                System.out.println(this.me.getShipBoard());
            }
            if (chose >= 1 && chose <= 4) {
                System.out.println(this.playersList.get(indexDeckInHandOrPlanet).getShipBoard());
            }
            return true;
        }
        return false;
    }

    private boolean firstTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && turns==0) {
            turns++;
            virtualController.sendTurnHourGlass(this.name);
            return true;
        }
        return false;
    }

    private boolean land(String[] input) throws RemoteException {
        if (input[0].equals("earlyland")&&!this.me.isLanded()){
            virtualController.notifyEarlyLanding(this.name);
            return true;
        }
        return false;
    }

    private int numerate(String[] input){
        int chose;
        if (input[0].isEmpty()) {
            System.out.println("Choose one of the decks");
            return -1;
        }
        try {
            chose = Integer.parseInt(input[0]);
            return chose;
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return -1;
        }
    }

    private String[] scroll(String[] input ,int times){
        String[] copiedInput = Arrays.copyOf(input, input.length);
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < copiedInput.length - 1; j++) {
                copiedInput[j] = copiedInput[j + 1];
            }
            if (copiedInput.length > 0) {
                copiedInput[copiedInput.length - 1] = null;
            }
        }
        return copiedInput;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LightFlightboard getFlightBoard() {
        return flightBoard;
    }

    public void setFlightBoard(LightFlightboard flightBoard) {
        this.flightBoard = flightBoard;
    }

    public void addTurnedTile(Tile tile) {
        TurnedTiles.put(tile.getKey(), tile);
    }
    public void removeTurnedTile(Tile tile) {
        TurnedTiles.remove(tile.getKey());
    }
    public void setTileInHand(Tile tile) {
        tileInHand = tile;
    }

    public void setTile(String playerName,Tile tile) {
        LightShipBoard lightShipboard= flightBoard.getIngamePlayer(playerName).getShipBoard();
        lightShipBoard.positionTile(Optional.of(tile),tile.getCoordinates());
    }

    public CoordInputManager getCoordInputManager() {
        return coordInputManager;
    }
}

