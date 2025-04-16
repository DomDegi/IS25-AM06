package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Planets;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.*;

public class ClientController {
    private ClientState state;
    private ClientState previousState;
    private String name;
    private final LightPlayer me;
    private final LightFlightboard flightBoard;
    private boolean inManager;
    private boolean connected;
    private GamePhases phase;

    private GameMode gameMode;
    private ViewInterface view;
    private VirtualController virtualController;
    private final CoordInputManager coordInputManager;
    private GoodsManager goodsManager;
    private final Map<Integer,Tile> turnedTiles;
    private final ArrayList<ArrayList<Card>> deck;
    private ArrayList<Goods> goodsList;

    private Tile tileInHand;
    private Card displayedCard;
    private int indexDeckInHandOrPlanet;
    private int indexCard;
    private int hourglassTurns;

    public ClientController(VirtualController virtualController) {
        this.deck = new ArrayList<>();
        this.me = new LightPlayer();
        this.flightBoard = new LightFlightboard();
        this.inManager = false;
        this.connected = false;
        this.turnedTiles = new HashMap<>();
        this.phase = GamePhases.LOGIN;
        this.indexCard = 0;
        this.virtualController = virtualController;
        this.indexDeckInHandOrPlanet = 0;
        this.hourglassTurns = 0;
        this.goodsList = new ArrayList<>();
        previousState=ClientState.CHOOSE_UI;
        state = ClientState.CHOOSE_UI;
        this.coordInputManager=new CoordInputManager(me.getShipBoard(),this);
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
                setState(ClientState.CHOOSE_CONNECTION_TYPE);
            }

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi"->virtualController=new VirtualControllerRMI(null);
                        //Da sistemare
                    case "socket"->virtualController= new VirtualControllerRMI(null);
                    //Da sistemare
                    default->{
                        System.out.println("Wrong input");
                        return;
                    }
                }
                setState(ClientState.LOGIN);
            }

            case LOGIN->{
                switch (words[0]) {
                    case "done" -> {
                        if(!connected){
                            //start connection
                        }
                        virtualController.login(name);
                        setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                    case "redo"-> this.name=("");
                    default-> this.name=(words[0]);
                }
            }

            case LOBBY-> {
                //CHOOSE TUI OR GUI
                switch(words[0]) {
                    case "creategame"-> {
                        String gameName = words[1];
                        int NumberOfPlayers=numerate(scroll(words,2));
                        if(NumberOfPlayers==-1)
                            return;
                        switch(words[3]) {
                            case "trialmode"->gameMode=GameMode.TRIAL;
                            case "level2mode"->gameMode=GameMode.LEVEL2;
                            default->{
                                System.out.println("Wrong input");
                                return;
                            }
                        }
                        virtualController.createGame(gameName,NumberOfPlayers,gameMode,name);
                        setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                    case "joingame"-> {
                        String gameName = words[1];
                        virtualController.joinGame(gameName,name);
                    }
                    default->
                        System.out.println("Wrong input");
                }
            }

            case COLOR_CHOICE->{
                PlayersColor color;
                switch (words[0]){
                    case "red"->
                        color = PlayersColor.RED;
                    case "yellow"->
                        color = PlayersColor.YELLOW;
                    case "green"->
                        color = PlayersColor.GREEN;
                    case "blue"->
                        color = PlayersColor.BLUE;
                    default->{
                        System.out.println("\nWrong input");
                        return;
                    }
                }
                virtualController.chooseColor(this.name,color);
                setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                    default->{
                        System.out.println("Wrong input");
                        return;
                    }
                }
                setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
            }

            case PLANET_CHOICE-> {
                if (checkShipBoards(words))
                    return;
                if (land(words))
                    return;
                if (words[0].equals("no"))
                    words[0]="0";
                int chose=numerate(words);
                if(chose==-1)
                    return;
                Planets planet=(Planets) displayedCard;
                if (chose >= 0 && chose <planet.getListOfPlanets().size()) {
                    indexDeckInHandOrPlanet = chose;
                    virtualController.planetChoiceRequest(this.name,chose);
                    setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                }
            }

            case MANAGE_GOODS-> {
                if(!inManager){
                    goodsManager = new GoodsManager(me, goodsList);
                    inManager=true;
                }
                if (checkShipBoards(words))
                    return;
                if (land(words))
                    return;
                HashSet<CargoHold> newTilesGoods = goodsManager.getReward(words);
                ArrayList<CargoHold> newTiles;
                if (!newTilesGoods.isEmpty()) {
                    newTiles = new ArrayList<>(newTilesGoods);
                    if (newTiles.getFirst().getTotSpaces() == -1){
                        newTiles.clear();
                    }
                    int goodsVal = me.getShipBoard().convertGoodsToCredit();
                    virtualController.notifyNewGoodsArrangement(this.name,goodsVal,newTiles);
                    inManager=false;
                    setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                }
            }

            case COORD_REQUEST->{
                if (checkShipBoards(words))
                    return;
                if(phase!=GamePhases.CARDS){
                    if (land(words))
                        return;
                }
                if(words[0].equals("done"))
                    coordInputManager.endCheckingFase();
                else
                    coordInputManager.checkCoord(transformCoordinates(words));
            }

            case START_SHIP_CREATION ->
                firstHourglassTurn(words);

            case S_END_DRAW_TILE_CARD-> {
                view.showTurnedTiles(turnedTiles);
                if (secondHourglassTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case"done" -> {
                        virtualController.sendEndShipBoardCreation(this.name);
                        setState(ClientState.S_FINISHED);
                    }
                    case "draw" -> {
                        switch (words[1]) {
                            case "card" -> {
                                int chose=numerate(scroll(words,2));
                                if (chose==-1)
                                    return;
                                if (chose > 0 && chose < 4) {
                                    virtualController.lookCardRequest(name,chose);
                                    indexDeckInHandOrPlanet = chose;
                                    displayedCard = this.deck.get(indexDeckInHandOrPlanet).getFirst();
                                    setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                }
                            }
                            case "tile" -> {
                                switch (words[2]){
                                    case "new"->{
                                        virtualController.reqDrawTileFromPile(this.name);
                                        setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                    }
                                    case "b1" ->{
                                        tileInHand=me.getShipBoard().getBookedTiles().getFirst();
                                        me.getShipBoard().removeBookedTile(0);
                                        setState(ClientState.S_MANAGE_DRAWN_TILE);
                                    }
                                    case "b2" ->{
                                        tileInHand=me.getShipBoard().getBookedTiles().get(1);
                                        me.getShipBoard().removeBookedTile(1);
                                        setState(ClientState.S_MANAGE_DRAWN_TILE);
                                    }
                                    default-> {
                                        int chose=numerate(scroll(words,2));
                                        if (chose==-1)
                                            return;
                                        if (chose>0&&chose<turnedTiles.size()) {
                                            virtualController.reqDrawTileFromTable(this.name,chose);
                                            setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                        }else {
                                            System.out.println("\nInvalid input format. Please provide integer values.");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            case S_MANAGE_CARDS-> {
                switch (words[0]) {
                    case "previous" -> {
                        indexCard--;
                        if(indexCard<0)
                            indexCard=2;
                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).get(indexCard);
                    }
                    case "next" -> {
                        indexCard++;
                        if(indexCard>2)
                            indexCard=0;
                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).get(indexCard);
                    }
                    case "done" -> {
                        //done action
                        virtualController.stopLookingAtCardsRequest(this.name);
                        setState(ClientState.S_END_DRAW_TILE_CARD);
                    }
                }
            }

            case S_MANAGE_DRAWN_TILE-> {
                if (secondHourglassTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case "rotate" ->
                        this.tileInHand.rotate();
                    case "position" -> {
                        Coordinates coordinates=transformCoordinates(scroll(words,1));
                        if(coordinates!=null){
                            me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand),coordinates);
                            virtualController.notifySetTile(this.name,coordinates,this.tileInHand);
                            setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        }
                    }
                    case "refuse" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        if(this.tileInHand.isBooked()){
                            me.getShipBoard().addBookedTile(this.tileInHand);
                            return;
                        }
                        this.turnedTiles.put(this.tileInHand.getKey(),this.tileInHand);
                        virtualController.notifyRefusedTile(this.name);
                        setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                    case "book" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        if(!me.getShipBoard().addBookedTile(this.tileInHand)){
                            System.out.println("\nInvalid input format. Please provide integer values.");
                            return;
                        }
                        this.tileInHand.setBooked(true);
                        virtualController.notifyTileBooking(this.name);
                        setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                }
            }

            case S_FINISHED-> {
                if (checkShipBoards(words))
                    return;
                if (thirdHourglassTurn(words))
                    return;
                int chose=numerate(scroll(words,0));
                if (chose==-1)
                    return;
                if (chose>0&&flightBoard.getInGamePlayers().size()>chose) {
                    virtualController.setFlightboard(name,chose);
                }else
                    System.out.println("\nWrong input");
            }
            case WAIT_OTHER_PLAYER_ACTION->{
                switch(phase){
                    case LOGIN -> {

                    }
                    case SHIPBOARD ->
                        checkShipBoards(words);
                    case CARDS -> {
                        if (checkShipBoards(words))
                            return;
                        land(words);
                    }
                }
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
                System.out.println(me.getShipBoard());
            }
            if (chose >= 1 && chose <= 4) {
                System.out.println(flightBoard.getInGamePlayers().get(indexDeckInHandOrPlanet).getShipBoard());
            }
            return true;
        }
        return false;
    }

    private void firstHourglassTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && hourglassTurns == 0) {
            hourglassTurns=1;
            virtualController.sendTurnHourGlass(this.name);
            setState(ClientState.WAIT_OTHER_PLAYER_ACTION);
        }
    }
    private boolean secondHourglassTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && hourglassTurns == 1) {
            hourglassTurns=2;
            virtualController.sendTurnHourGlass(this.name);
            return true;
        }
        return false;
    }
    private boolean thirdHourglassTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && hourglassTurns >= 1 && hourglassTurns < 3) {
            hourglassTurns=3;
            virtualController.sendTurnHourGlass(this.name);
            setState(ClientState.S_FINISHED);
            return true;
        }
        return false;
    }

    private boolean land(String[] input) throws RemoteException {
        if (input[0].equals("earlyland")&&!me.isLanded()){
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
    public void setState(ClientState newState){
        view.setClientState();
        previousState=state;
        state=newState;
    }
    public boolean rollBackState(){
        view.setClientState();
        if (state==previousState)
            return false;
        state=previousState;
        return true;
    }
    public void updateFlightboard(String name,int pos, int ranking) {
        for(LightPlayer player:flightBoard.getInGamePlayers())
            if (player.getPlayerName().equals(name)){
                player.setPosition(pos);
                player.setRank(ranking);
            }
    }
    public LightFlightboard getFlightBoard() {
        return flightBoard;
    }
    public void addTurnedTile(Tile tile) {
        turnedTiles.put(tile.getKey(), tile);
    }
    public void removeTurnedTile(Tile tile) {
        turnedTiles.remove(tile.getKey());
    }
}

