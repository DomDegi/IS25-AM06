package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Planets;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Client.VirtualViewRMI;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.ServerHandler;
import it.polimi.ingsw.galaxytruckerproject.network.Socket.VirtualControllerSocket;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class ClientController {
    private ClientState state;
    private ClientState previousState;
    private final LightPlayer me;
    private LightFlightboard flightBoard;
    private boolean inManager;
    private boolean connected;
    private GamePhases phase;
    private ArrayList<GameInfo> gameInfo;

    private GameMode gameMode;
    private DisplayableView view;
    private VirtualController virtualController;
    private CoordInputManager coordInputManager;
    private GoodsManager goodsManager;
    private CabinsManager cabinsManager;
    private Map<Integer,Tile> turnedTiles;
    private Map<Integer, ArrayList<Card>> deck;
    private final Map<Integer,Boolean> availableDeck;
    private final Map<PlayersColor,Boolean> availableColors;
    private final ArrayList<Goods> goodsList;

    private int numPlayer;
    private Tile tileInHand;
    private ArrayList<Card> displayedCard;
    private int indexDeckInHandOrPlanet;
    private int hourglassTurns;

    public ClientController() {
        this.numPlayer=0;
        this.gameInfo=new ArrayList<>();
        this.view=new TUI();
        this.deck = new HashMap<>();
        this.me = new LightPlayer("",null);
        this.goodsList = new ArrayList<>();
        this.flightBoard = new LightFlightboard(new FlightBoard(null));
        this.inManager = false;
        this.connected = false;
        this.turnedTiles = new HashMap<>();
        this.previousState=ClientState.CHOOSE_UI;
        this.state = ClientState.CHOOSE_UI;
        this.phase = GamePhases.LOGIN;
        this.availableDeck = new HashMap<>(3);
        availableDeck.put(1,Boolean.TRUE);
        availableDeck.put(2,Boolean.TRUE);
        availableDeck.put(3,Boolean.TRUE);
        this.availableColors = new HashMap<>(4);
        availableColors.put(PlayersColor.RED,Boolean.TRUE);
        availableColors.put(PlayersColor.YELLOW,Boolean.TRUE);
        availableColors.put(PlayersColor.GREEN,Boolean.TRUE);
        availableColors.put(PlayersColor.BLUE,Boolean.TRUE);
        this.indexDeckInHandOrPlanet = 0;
        this.hourglassTurns = 0;
    }


    public VirtualController getVirtualController() {
        return virtualController;
    }

    public boolean input(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        if (words.length == 0||words[0].isEmpty()) {
            view.wrongLocalInput();
            return false;
        }

        switch (state) {
            case CHOOSE_UI->{
                switch(words[0]) {
                    case "gui","g"->
                            this.view= new GUI();
                    case "tui","t"->
                            this.view=new TUI();
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                setState(ClientState.CHOOSE_CONNECTION_TYPE);
            }

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi","r"->{
                        try {
                            connectRMI();
                        } catch (MalformedURLException | NotBoundException | RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "socket","s"-> {
                        try {
                            connectSocket();
                        } catch (IOException e) {
                            System.out.println("Error connecting to server via socket");
                        }
                    }
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                setState(ClientState.LOGIN);
            }

            case LOGIN->{
                switch (words[0]) {
                    case "done","d" -> {
                        if(!Objects.equals(me.getPlayerName(), "")) {
                            setState(ClientState.WAIT);
                            try {
                                virtualController.login(me.getPlayerName());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            view.wrongLocalInput();
                        }
                    }
                    case "redo"-> me.setPlayerName("");
                    default-> me.setPlayerName(words[0]);
                }
            }

            case LOBBY-> {
                //CHOOSE TUI OR GUI
                switch(words[0]) {
                    case "creategame","c"-> {
                        if(!(words.length > 1)){
                            view.wrongLocalInput();
                            return false;
                        }
                        String gameName = words[1];
                        int NumberOfPlayers;
                        if(!(words.length > 2)){
                            view.wrongLocalInput();
                            return false;
                        }
                        NumberOfPlayers = numerate(scroll(words,2));
                        if(NumberOfPlayers==-1||NumberOfPlayers>4)
                            return false;
                        numPlayer=NumberOfPlayers;
                        if(!(words.length > 3)){
                            view.wrongLocalInput();
                            return false;
                        }
                        switch(words[3]) {
                            case "trialmode","t"-> gameMode=GameMode.TRIAL;
                            case "level2mode","2"-> gameMode=GameMode.LEVEL2;
                            default->{
                                view.wrongLocalInput();
                                return false;
                            }
                        }
                        setState(ClientState.WAIT);
                        try {
                            virtualController.createGame(gameName,NumberOfPlayers,gameMode);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "joingame","j"-> {
                        if(!(words.length > 1)){
                            view.wrongLocalInput();
                            return false;
                        }
                        String gameName = words[1];
                        if(gameInfo==null){
                            view.wrongLocalInput();
                            return false;
                        }
                        for(GameInfo games:gameInfo) {
                            if(Objects.equals(gameName, games.getGameName())){
                                setState(ClientState.WAIT);
                                numPlayer=games.getMaxPlayerCount();
                                try {
                                    virtualController.joinGame(gameName);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                return true;
                            }
                        }
                        view.wrongLocalInput();
                        return false;
                    }
                    default-> {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case COLOR_CHOICE->{
                PlayersColor color;
                switch (words[0]){
                    case "red","r"->
                        color = PlayersColor.RED;
                    case "yellow","y"->
                        color = PlayersColor.YELLOW;
                    case "green","g"->
                        color = PlayersColor.GREEN;
                    case "blue","b"->
                        color = PlayersColor.BLUE;
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                me.setColor(color);
                setState(ClientState.WAIT);
                try {
                    virtualController.chooseColor(color);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case START_SHIP_CREATION -> {
                if (firstHourglassTurn(words))
                    return true;
                view.wrongLocalInput();
                return false;
            }

            case S_END_DRAW_TILE_CARD -> {
                if (secondHourglassTurn(words))
                    return true;
                if (checkShipBoards(words))
                    return true;
                switch (words[0]) {
                    case"done","d" -> {
                        setState(ClientState.WAIT);
                        try {
                            virtualController.notifyCompleted();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "draw" -> {
                        if(!(words.length > 1)){
                            view.wrongLocalInput();
                            return false;
                        }
                        switch (words[1]) {
                            case "card" -> {
                                if(gameMode==GameMode.LEVEL2) {
                                    int chose ;
                                    if(!(words.length > 2)){
                                        view.wrongLocalInput();
                                        return false;
                                    }
                                    chose = numerate(scroll(words, 2));
                                    if (chose == -1)
                                        return false;
                                    if (chose > 0 && chose < 4 && availableDeck.get(chose)) {
                                       setState(ClientState.WAIT);
                                        try {
                                            virtualController.lookCardsRequest( chose);
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                        indexDeckInHandOrPlanet = chose;
                                        displayedCard = this.deck.get(indexDeckInHandOrPlanet);
                                    } else {
                                        view.wrongLocalInput();
                                        return false;
                                    }
                                } else {
                                    view.wrongLocalInput();
                                    return false;
                                }
                            }
                            case "tile" -> {
                                if(!(words.length > 2)){
                                    setState(ClientState.WAIT);
                                    try {
                                        virtualController.reqDrawTileFromStack();
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return true;
                                }
                                switch (words[2]){
                                    case "new" -> {
                                        setState(ClientState.WAIT);
                                        try {
                                            virtualController.reqDrawTileFromStack();
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    case "b1" ->{
                                        if(!me.getShipBoard().getBookedTiles().isEmpty()) {
                                            tileInHand = me.getShipBoard().getBookedTiles().getFirst();
                                            me.getShipBoard().removeBookedTile(0);
                                            setState(ClientState.S_MANAGE_DRAWN_TILE);
                                        }else{
                                            view.wrongLocalInput();
                                            return false;
                                        }
                                    }
                                    case "b2" ->{
                                        if(!me.getShipBoard().getBookedTiles().isEmpty()) {
                                            if(me.getShipBoard().getBookedTiles().size() > 1) {
                                                tileInHand=me.getShipBoard().getBookedTiles().get(1);
                                                me.getShipBoard().removeBookedTile(1);
                                            }else{
                                                tileInHand = me.getShipBoard().getBookedTiles().getFirst();
                                                me.getShipBoard().removeBookedTile(0);
                                            }
                                            setState(ClientState.S_MANAGE_DRAWN_TILE);
                                        }else{
                                            view.wrongLocalInput();
                                            return false;
                                        }
                                    }
                                    default-> {
                                        int chose;
                                        chose = numerate(scroll(words,2));
                                        if (chose==-1)
                                            return false;
                                        if (chose>0&&turnedTiles.containsKey(chose)) {
                                            tileInHand=turnedTiles.get(chose);
                                            setState(ClientState.WAIT);
                                            try {
                                                virtualController.reqDrawTileFromTurned(chose);
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }else {
                                            view.wrongLocalInput();
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    default -> {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case S_MANAGE_CARDS-> {
                if(words[0].equals("done")) {
                    setState(ClientState.S_END_DRAW_TILE_CARD);
                    try {
                        virtualController.stopLookingAtCardsRequest();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            case S_MANAGE_DRAWN_TILE-> {
                if (secondHourglassTurn(words))
                    return true;
                if (checkShipBoards(words))
                    return true;
                switch (words[0]) {
                    case "rotate" -> {
                        this.tileInHand.rotate();
                        try {
                            view.showDrawnTile(tileInHand);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "position" -> {
                        Coordinates coordinates;
                        if(!(words.length > 2)){
                            view.wrongLocalInput();
                            return false;
                        }
                        coordinates = transformCoordinates(scroll(words,1));
                        if(coordinates!=null){
                            me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand),coordinates);
                            setState(ClientState.WAIT);
                            try {
                                virtualController.notifySetTile(this.tileInHand.send());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }else
                            return false;
                    }
                    case "refuse" -> {
                        if(this.tileInHand.isBooked()){
                            me.getShipBoard().addBookedTile(this.tileInHand);
                            setState(ClientState.S_END_DRAW_TILE_CARD);
                            return true;
                        }
                        this.turnedTiles.put(this.tileInHand.getKey(),this.tileInHand);
                        setState(ClientState.WAIT);
                        try {
                            virtualController.notifyRefusedTile();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "book" -> {
                        if(!me.getShipBoard().addBookedTile(this.tileInHand)){
                            view.wrongLocalInput();
                            return false;
                        }
                        this.tileInHand.setBooked(true);
                        setState(ClientState.WAIT);
                        try {
                            virtualController.notifyTileBooking();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            case S_FINISHED-> {
                if (checkShipBoards(words))
                    return true;
                if (thirdHourglassTurn(words))
                    return true;
                if(gameMode==GameMode.LEVEL2) {
                    int chose;
                    chose = numerate(scroll(words, 0));
                    if (chose == -1)
                        return false;
                    if (chose > 0 &&  chose <= numPlayer) {
                        setState(ClientState.WAIT);
                        try {
                            virtualController.notifySetPosition(chose);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    } else {
                        view.wrongLocalInput();
                        return false;
                    }
                }
                view.wrongLocalInput();
                return false;
            }

            case ROLL_DICE->{
                setState(ClientState.WAIT);
                try {
                    virtualController.rollTheDices();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case DRAW_CARD ->{
                setState(ClientState.WAIT);
                try {
                    virtualController.drawCards();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case ACTION-> {
                if (checkShipBoards(words))
                    return true;
                if (land(words))
                    return true;
                switch (words[0]){
                    case "yes" -> {
                        setState(ClientState.WAIT);
                        try {
                            virtualController.sendYes();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "no" -> {
                        setState(ClientState.WAIT);
                        try {
                            virtualController.sendNo();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case PLANET_CHOICE-> {
                if (checkShipBoards(words))
                    return true;
                if (land(words))
                    return true;
                if (words[0].equals("no"))
                    words[0]="0";
                int chose;
                chose = numerate(words);
                if(chose==-1)
                    return false;
                Planets planet=(Planets) displayedCard.getFirst();
                if (chose >= 0 && chose <planet.getListOfPlanets().size()) {
                    indexDeckInHandOrPlanet = chose;
                    setState(ClientState.WAIT);
                    try {
                        virtualController.planetChoiceRequest(chose);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            case MANAGE_GOODS-> {
                if (checkShipBoards(words))
                    return true;
                if (land(words))
                    return true;
                if(inManager){
                    HashSet<CargoHold> newTilesGoods;
                    try {
                        newTilesGoods = goodsManager.getReward(words);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    ArrayList<CargoHold> newTiles;
                    if (newTilesGoods != null) {
                        newTiles = new ArrayList<>(newTilesGoods);
                        if (newTiles.getFirst().getTotSpaces() == -1) {
                            newTiles.clear();
                        }
                        int goodsVal = me.getShipBoard().convertGoodsToCredit();
                        setState(ClientState.WAIT);
                        try {
                            virtualController.notifyNewGoodsArrangement( goodsVal, newTiles);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        inManager = false;
                    }
                }
            }

            case MANAGE_CABINS -> {
                if(gameMode==GameMode.LEVEL2&& inManager){
                    CrewType type;
                    ArrayList<Tile> newTiles;
                    switch (words[0]) {
                        case "humans" -> type = CrewType.HUMAN;
                        case "brownalien" -> type = CrewType.BROWN;
                        case "purplealien" -> type = CrewType.PURPLE;
                        default -> {
                            view.wrongLocalInput();
                            try {
                                cabinsManager.setup();
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            return false;
                        }
                    }
                    if (!(me.getShipBoard().getCabinsCoordinates().isEmpty()||me.getShipBoard().getCabinsCoordinates().size()==1)) {
                        try {
                            newTiles = cabinsManager.manageCabins(type);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        if (newTiles!=null) {
                            setState(ClientState.WAIT);
                            try {
                                virtualController.notifyNewCrewArrangement(newTiles);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            inManager = false;
                        }
                    }
                }
            }

            case COORD_REQUEST -> {
                if (checkShipBoards(words))
                    return true;
                if (phase == GamePhases.CARDS) {
                    if (land(words))
                        return true;
                }
                if (words[0].equals("done")) {
                    return coordInputManager.endCheckingFase();
                }
                else {
                    //gestire il -1 in base alla carta
                    Coordinates coords;
                    coords = transformCoordinates(words);
                    if(coords==null)
                        return false;
                    if(!coordInputManager.checkCoord(coords)) {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case WAIT ->{
                switch(phase){
                    case LOGIN -> {
                        try {
                            view.showGenericMessage("Wrong input in wait");
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case SHIPBOARD -> {
                        if (checkShipBoards(words))
                            return true;
                    }
                    case CARDS -> {
                        if (checkShipBoards(words))
                            return true;
                        if(land(words))
                            return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public void setState(ClientState newState){
        previousState=state;
        state=newState;
        switch(newState){
            case START_SHIP_CREATION -> {
                this.coordInputManager = new CoordInputManager(me.getShipBoard(), this);
                //me.setShipboard(new LightShipBoard(me));
                phase = GamePhases.SHIPBOARD;
            }

            case S_END_DRAW_TILE_CARD -> {
                view.showTurnedTiles(turnedTiles);
                view.printShipboard(me.getShipBoard());
                view.printBooked(me.getShipBoard());
            }
            case S_MANAGE_CARDS -> {
                if(gameMode==GameMode.TRIAL) {
                    setState(ClientState.S_END_DRAW_TILE_CARD);
                    previousState=state;
                }
            }
            case MANAGE_CABINS -> {
                if (!inManager) {
                    cabinsManager = new CabinsManager(me,view);
                    inManager = true;
                }
                if (gameMode == GameMode.LEVEL2) {
                    if (me.getShipBoard().getCabinsCoordinates()==null||me.getShipBoard().getCabinsCoordinates().isEmpty()||me.getShipBoard().getCabinsCoordinates().size()==1) {
                        setState(ClientState.WAIT);
                        try {
                            virtualController.notifyNewCrewArrangement(new ArrayList<>());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        inManager = false;
                    } else {
                        try {
                            cabinsManager.setup();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }else if (gameMode == GameMode.TRIAL) {
                    CrewType type = CrewType.HUMAN;
                    ArrayList<Tile> newTiles = new ArrayList<>();
                    for (Coordinates _ : me.getShipBoard().getCabinsCoordinates()) {
                        try {
                            newTiles = cabinsManager.manageCabins(type);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    setState (ClientState.WAIT);
                    try {
                        virtualController.notifyNewCrewArrangement(newTiles);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    inManager = false;
                }
            }
            case S_FINISHED-> {
                if (gameMode == GameMode.TRIAL) {
                    try {
                        virtualController.notifySetPosition(0);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case ROLL_DICE -> {
                view.printProjectile(displayedCard.getFirst().getListOfProjectiles().getFirst());
                displayedCard.getFirst().getListOfProjectiles().removeFirst();
            }
            case DRAW_CARD -> {
                phase = GamePhases.CARDS;
                if(me.getRank()!=1){
                    setState( ClientState.WAIT);
                }
            }
            case MANAGE_GOODS -> {
                if(!inManager){
                    goodsManager = new GoodsManager(me, goodsList,view);
                    inManager=true;
                }
                view.goodsPrinter(goodsList);
                try {
                    view.showGenericMessage("Chose for each good where to put it, input 'no' to stop:\n");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            view.setClientState(state);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private Coordinates transformCoordinates(String[] input) {
        if (input.length < 2) {
            view.wrongLocalInput();
            return null;
        }
        int CoordinatesX;
        try {
            CoordinatesX = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            view.wrongLocalInput();
            return null;
        }
        if(CoordinatesX<0||CoordinatesX>4){
            view.wrongLocalInput();
            return null;
        }
        int CoordinatesY;
        try {
            CoordinatesY = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            view.wrongLocalInput();
            return null;
        }
        if(CoordinatesY<0||CoordinatesY>6){
            view.wrongLocalInput();
            return null;
        }
        return new Coordinates(CoordinatesX, CoordinatesY);
    }

    private boolean checkShipBoards(String[] input){
        if (input[0].equals("check")) {
            if(!(input.length > 1)){
                view.wrongLocalInput();
                return false;
            }
            int chose=numerate(scroll(input,1));
            if (chose==-1)
                return false;
            if (chose == 0) {
                view.printShipboard(me.getShipBoard());
            }
            if (chose >= 1 && chose <= 4) {
                view.printShipboard(flightBoard.getInGamePlayers().get(indexDeckInHandOrPlanet).getShipBoard());
            }
            return true;
        }
        return false;
    }

    private boolean firstHourglassTurn(String[] input) {
        if (((gameMode==GameMode.LEVEL2 && input[0].equals("turn")) || input[0].equals("start")) && hourglassTurns == 0) {
            hourglassTurns=1;
            setState(ClientState.WAIT);
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    private boolean secondHourglassTurn(String[] input) {
        if (input[0].equals("turn") && hourglassTurns == 1) {
            if(gameMode==GameMode.TRIAL){
                view.wrongLocalInput();
                return false;
            }
            hourglassTurns=2;
            setState(state);
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    private boolean thirdHourglassTurn(String[] input) {
        if (input[0].equals("turn") && hourglassTurns >= 1 && hourglassTurns < 3) {
            if(gameMode==GameMode.TRIAL){
                view.wrongLocalInput();
                return false;
            }
            if(hourglassTurns==1)
                hourglassTurns=2;
            else
                hourglassTurns=3;
            setState(ClientState.S_FINISHED);
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    private LightPlayer playerFinder(String playerName){
        LightPlayer searchedPlayer=null;
        for(LightPlayer player:flightBoard.getInGamePlayers())
            if (player.getPlayerName().equals(playerName)) {
                searchedPlayer = player;
            }
        return searchedPlayer;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        me.setShipboard(new LightShipBoard(me));
        if(gameMode==GameMode.LEVEL2){
            me.getShipBoard().initializeLevel2();
            for (LightPlayer player : flightBoard.getInGamePlayers()) {
                new LightShipBoard(player);
                player.getShipBoard().initializeLevel2();
            }
        }
        else if(gameMode==GameMode.TRIAL) {
            me.getShipBoard().initializeTestFlight();
            for (LightPlayer player : flightBoard.getInGamePlayers()) {
                new LightShipBoard(player);
                player.getShipBoard().initializeTestFlight();
            }
        }
    }

    private boolean land(String[] input){
        if (input[0].equals("earlyland")&&!me.isLanded()){
            try {
                virtualController.notifyEarlyLanding();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    private int numerate(String[] input) {
        int chose;
        if (input[0].isEmpty()) {
            view.wrongLocalInput();
            return -1;
        }
        try {
            chose = Integer.parseInt(input[0]);
            return chose;
        } catch (NumberFormatException e) {
            view.wrongLocalInput();
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

    public void rollBackState() {
        if (state==previousState)
            return;
        setState(previousState);
    }

    public void addToFlightboard(String name,PlayersColor color,int pos, int ranking){
        if(Objects.equals(name, me.getPlayerName())){
            flightBoard.addInGamePlayer(me);
            return;
        }
        LightPlayer player = new LightPlayer(name,color);
        player.setPosition(pos);
        player.setRank(ranking);
        flightBoard.addInGamePlayer(player);
    }

    public void updateFlightboard(String name,PlayersColor color,int pos, int ranking) {
        for(LightPlayer player:flightBoard.getInGamePlayers())
            if (player.getPlayerName().equals(name)){
                player.setPosition(pos);
                player.setRank(ranking);
                return;
            }
        addToFlightboard(name,color,pos,ranking);
    }

    public LightFlightboard getFlightBoard() {
        return flightBoard;
    }
    public void addTurnedTile(Tile tile) {
        turnedTiles.put(tile.getKey(), tile);
        view.showTurnedTiles(turnedTiles);
    }
    public void removeTurnedTile(Tile tile) {
        turnedTiles.remove(tile.getKey());
    }
    public String getName() {
        return me.getPlayerName();
    }
    public void setTileInHand(Tile tile) {
        tileInHand = tile;
    }

    public void setTile(String playerName,Tile tile) {
        LightShipBoard lightShipBoard= flightBoard.getInGamePlayer(playerName).getShipBoard();
        lightShipBoard.positionTile(Optional.of(tile),tile.getCoordinates());
    }

    public CoordInputManager getCoordInputManager() {
        return coordInputManager;
    }

    public LightShipBoard getLightShipBoard() {
        return me.getShipBoard();
    }

    public void setDisplayedCard(Card displayedCard) {
        this.displayedCard.clear();
        this.displayedCard.add(displayedCard);
    }
    public ArrayList<Card> getDisplayedCard() {
        return displayedCard;
    }

    //deve copiare e incollare quelle tiles nelle loro coordinate per il player corrispondente
    //verrà usato in caso di batterie usate, goods o crewMate dispersi
    public void modifyTiles(String playerName,ArrayList<Tile> tiles){
        LightPlayer player=playerFinder(playerName);
        if(player==null)
            return;
        for(Tile modTiles:tiles){
            player.getShipBoard().positionTile(Optional.of(modTiles),modTiles.getCoordinates());
        }
    }

    //rompe le tile nelle coordinate corrispondenti per il player selezionato
    public void brokenTiles(String playerName, ArrayList<Coordinates> coordinates){
        LightPlayer player=playerFinder(playerName);
        if(player==null)
            return;
        player.getShipBoard().destroy(coordinates);
    }

    //aggiungi booked tile al player (dalla light shipboard) con check sul senso dell'invocazione
    public void addBookedTile(String playerName, Tile tile){
        LightPlayer player=playerFinder(playerName);
        if(player==null)
            return;
        player.getShipBoard().addBookedTile(tile);
    }

    //rimuovi booked tile al player (dalla light shipboard) con check sul senso dell'invocazione
    public void removeBookedTile(String playerName, Tile tile){
        LightPlayer player=playerFinder(playerName);
        if(player==null)
            return;
        int index=0;
        for(Tile bookedTile:player.getShipBoard().getBookedTiles()){
            if(bookedTile==tile)
                player.getShipBoard().removeBookedTile(index);
            index++;
        }
    }

    public void gainCredit (String playerName, int credits){
        LightPlayer player=playerFinder(playerName);
        if(player==null)
            return;
        player.gainCredits(credits);
    }

    public void updateModel(Map<String,LightShipBoard> lightShipBoardMap, LightFlightboard flightBoard,Card card, int hourglassTurns, Map<Integer,Tile> newTurnedTiles, ArrayList<Integer>notAvailableDecks) {
        this.flightBoard=flightBoard;
        for(LightPlayer player:flightBoard.getInGamePlayers()){
            player.setShipboard(lightShipBoardMap.get(player.getPlayerName()));
        }
        displayedCard.add(card);
        this.hourglassTurns=hourglassTurns;
        turnedTiles=newTurnedTiles;
        decksNotAvailable(notAvailableDecks);
    }

    public void decksNotAvailable(ArrayList<Integer> notAvailableDecks) {
        for(int index=0;index<4;index++){
            availableDeck.put(index,Boolean.TRUE);
        }
        for(int index:notAvailableDecks){
            if(notAvailableDecks.contains(index))
                availableDeck.put(index,Boolean.FALSE);
        }
    }

    public DisplayableView getView() {
        return view;
    }

    public void setDeck(Map<Integer, ArrayList<Card>> deck) {
        this.deck = deck;
    }

    public void setGameInfo(ArrayList<GameInfo> gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void colorsNotAvailable(PlayersColor notAvailableColors) {
        this.availableColors.put(notAvailableColors,Boolean.FALSE);
    }

    //Test getter
    public ClientState getState() {
        return state;
    }

    public LightPlayer getMe() {
        return me;
    }

    public GamePhases getPhase() {
        return phase;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Map<Integer, Tile> getTurnedTiles() {
        return turnedTiles;
    }

    public Map<Integer, ArrayList<Card>> getDeck() {
        return deck;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public Tile getTileInHand() {
        return tileInHand;
    }

    public int getIndexDeckInHandOrPlanet() {
        return indexDeckInHandOrPlanet;
    }

    public int getHourglassTurns() {
        return hourglassTurns;
    }

    public void setConnected(boolean connected) {this.connected = connected;}

    void connectRMI() throws MalformedURLException, NotBoundException, RemoteException {
        VirtualViewRMI viewRMI=new VirtualViewRMI(this,view);
        ControllerFactory controllerFactory=(ControllerFactory) Naming.lookup("rmi://localhost/ControllerFactory");
        this.virtualController=controllerFactory.createController();
        virtualController.setView(viewRMI);
    }

    void connectSocket() throws IOException {
        Socket server;
        try{
            server = new Socket("localhost",12345);
        } catch(Exception e){
            System.out.println("Server unreachable, check the port and the ip address");
            return;
        }
        ServerHandler serverHandler = new ServerHandler(server);
        virtualController = new VirtualControllerSocket(view,serverHandler);
        Thread waitForSetup = new Thread(serverHandler, "wait for setup of " + server.getInetAddress().getHostAddress());
        waitForSetup.start();
        while(!serverHandler.isReady()){
            try{
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("waiting for view and virtual controller setup in serverHandler");
            }
        }
        serverHandler.setVirtualController(virtualController);
        serverHandler.setView(view);
        serverHandler.setClientController(this);
    }

    public void ping()  {
        try {
            virtualController.ping();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void turnHourglass(int i) {
        hourglassTurns= i;
    }
}

