package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Planets;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Client.VirtualViewRMI;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Server.VirtualControllerRMI;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.net.MalformedURLException;
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

    private GameMode gameMode;
    private VirtualView view;
    private VirtualController virtualController;
    private final CoordInputManager coordInputManager;
    private GoodsManager goodsManager;
    private CabinsManager cabinsManager;
    private Map<Integer,Tile> turnedTiles;
    private Map<Integer, ArrayList<Card>> deck;
    private final Map<Integer,Boolean> availableDeck;
    private final ArrayList<Goods> goodsList;

    private Tile tileInHand;
    private Card displayedCard;
    private int indexDeckInHandOrPlanet;
    private int indexCard;
    private int hourglassTurns;

    public ClientController() {
        try {
            this.view=new VirtualViewRMI(this,new TUI());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        this.deck = new HashMap<>();
        this.goodsList = new ArrayList<>();
        this.me = new LightPlayer("",null);
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
        this.indexCard = 0;
        this.indexDeckInHandOrPlanet = 0;
        this.hourglassTurns = 0;
        this.coordInputManager=new CoordInputManager(me.getShipBoard(),this);
    }


    public VirtualController getVirtualController() {
        return virtualController;
    }

    public boolean input(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        if (words.length == 0||words[0].isEmpty()) {
            try {
                view.wrongLocalInput();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return false;
        }

        switch (state) {
            case CHOOSE_UI->{
                switch(words[0]) {
                    case "gui"-> {
                        try {
                            this.view=new VirtualViewRMI(this,new GUI());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "tui"-> {
                        try {
                            this.view=new VirtualViewRMI(this,new TUI());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    default->{
                        try {
                            view.wrongLocalInput();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                }
                try {
                    view.setClientState(ClientState.CHOOSE_CONNECTION_TYPE);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi"->{
                        try {
                            connectRMI();
                        }
                        catch (MalformedURLException | NotBoundException | RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "socket"-> {
                        try {
                            virtualController= new VirtualControllerRMI(null);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //Da sistemare
                    default->{
                        try {
                            view.wrongLocalInput();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                }
                try {
                    view.setClientState(ClientState.LOGIN);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case LOGIN->{
                switch (words[0]) {
                    case "done" -> {
                        if(!connected){
                            //start connection
                        }
                        try {
                            virtualController.login(me.getPlayerName());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    case "redo"-> me.setPlayerName("");
                    default-> me.setPlayerName(words[0]);
                }
            }

            case LOBBY-> {
                //CHOOSE TUI OR GUI
                switch(words[0]) {
                    case "creategame"-> {
                        String gameName = words[1];
                        int NumberOfPlayers;
                        NumberOfPlayers = numerate(scroll(words,2));
                        if(NumberOfPlayers==-1||NumberOfPlayers>4)
                            return false;
                        switch(words[3]) {
                            case "trialmode"-> gameMode=GameMode.TRIAL;
                            case "level2mode"-> gameMode=GameMode.LEVEL2;
                            default->{
                                try {
                                    view.wrongLocalInput();
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                return false;
                            }
                        }
                        try {
                            virtualController.createGame(gameName,NumberOfPlayers,gameMode);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "joingame"-> {
                        String gameName = words[1];
                        try {
                            virtualController.joinGame(gameName);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    default-> {
                        try {
                            view.wrongLocalInput();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
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
                        try {
                            view.wrongLocalInput();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                }
                me.setColor(color);
                try {
                    virtualController.chooseColor(color);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case START_SHIP_CREATION -> {
                return firstHourglassTurn(words);
            }

            case S_END_DRAW_TILE_CARD -> {
                if (secondHourglassTurn(words))
                    return true;
                if (checkShipBoards(words))
                    return true;
                switch (words[0]) {
                    case"done" -> {
                        try {
                            virtualController.notifyCompleted();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.S_FINISHED);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "draw" -> {
                        switch (words[1]) {
                            case "card" -> {
                                if(gameMode==GameMode.LEVEL2) {
                                    int chose ;
                                    chose = numerate(scroll(words, 2));
                                    if (chose == -1)
                                        return false;
                                    if (chose > 0 && chose < 4 && availableDeck.get(chose)) {
                                        try {
                                            virtualController.lookCardsRequest( chose);
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                        indexDeckInHandOrPlanet = chose;
                                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).getFirst();
                                        try {
                                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else {
                                        try {
                                            view.wrongLocalInput();
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return false;
                                    }
                                } else {
                                    try {
                                        view.wrongLocalInput();
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return false;
                                }
                            }
                            case "tile" -> {
                                switch (words[2]){
                                    case "new"->{
                                        try {
                                            virtualController.reqDrawTileFromStack();
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                        try {
                                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                        } catch (RemoteException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    case "b1" ->{
                                        if(!me.getShipBoard().getBookedTiles().isEmpty()) {
                                            tileInHand = me.getShipBoard().getBookedTiles().getFirst();
                                            me.getShipBoard().removeBookedTile(0);
                                            try {
                                                view.setClientState(ClientState.S_MANAGE_DRAWN_TILE);
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }else{
                                            try {
                                                view.wrongLocalInput();
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
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
                                            try {
                                                view.setClientState(ClientState.S_MANAGE_DRAWN_TILE);
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }else{
                                            try {
                                                view.wrongLocalInput();
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
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
                                            try {
                                                virtualController.reqDrawTileFromTurned(chose);
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }else {
                                            try {
                                                view.wrongLocalInput();
                                            } catch (RemoteException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return false;
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
                        try {
                            view.showCard(displayedCard);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "next" -> {
                        indexCard++;
                        if(indexCard>2)
                            indexCard=0;
                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).get(indexCard);
                        try {
                            view.showCard(displayedCard);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "done" -> {
                        //done action
                        try {
                            virtualController.stopLookingAtCardsRequest();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.S_END_DRAW_TILE_CARD);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            case S_MANAGE_DRAWN_TILE-> {
                if (secondHourglassTurn(words))
                    return true;
                if (checkShipBoards(words))
                    return true;
                switch (words[0]) {
                    case "rotate" ->
                        this.tileInHand.rotate();
                    case "position" -> {
                        Coordinates coordinates;
                        coordinates = transformCoordinates(scroll(words,1));
                        if(coordinates!=null){
                            me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand),coordinates);
                            try {
                                virtualController.notifySetTile(this.tileInHand);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }else
                            return false;
                    }
                    case "refuse" -> {
                        if(this.tileInHand.isBooked()){
                            me.getShipBoard().addBookedTile(this.tileInHand);
                            try {
                                view.setClientState(ClientState.S_END_DRAW_TILE_CARD);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                        this.turnedTiles.put(this.tileInHand.getKey(),this.tileInHand);
                        try {
                            virtualController.notifyRefusedTile();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "book" -> {
                        if(!me.getShipBoard().addBookedTile(this.tileInHand)){
                            try {
                                view.wrongLocalInput();
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            return false;
                        }
                        this.tileInHand.setBooked(true);
                        try {
                            virtualController.notifyTileBooking();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                    if (chose > 0 &&  chose < flightBoard.getInGamePlayers().size()) {
                        try {
                            virtualController.notifySetPosition(chose);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            view.wrongLocalInput();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                } else {
                    try {
                        view.wrongLocalInput();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return false;
                }
            }

            case ROLL_DICE->{
                try {
                    virtualController.rollTheDices();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case DRAW_CARD ->{
                try {
                    virtualController.drawCards();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                        try {
                            virtualController.sendYes();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "no" -> {
                        try {
                            virtualController.sendNo();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    default->{
                        try {
                            view.wrongLocalInput();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        return false;
                    }
                }
                try {
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
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
                Planets planet=(Planets) displayedCard;
                if (chose >= 0 && chose <planet.getListOfPlanets().size()) {
                    indexDeckInHandOrPlanet = chose;
                    try {
                        virtualController.planetChoiceRequest(chose);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                        try {
                            virtualController.notifyNewGoodsArrangement( goodsVal, newTiles);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        inManager = false;
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
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
                            try {
                                view.wrongLocalInput();
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
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
                            try {
                                virtualController.notifyNewCrewArrangement(newTiles);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            inManager = false;
                            try {
                                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
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
                    try {
                        return coordInputManager.endCheckingFase();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    //gestire il -1 in base alla carta
                    Coordinates coords;
                    coords = transformCoordinates(words);
                    if(coords==null)
                        return false;
                    try {
                        if(!coordInputManager.checkCoord(coords)) {
                            view.wrongLocalInput();
                            return false;
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            case WAIT_OTHER_PLAYER_ACTION->{
                switch(phase){
                    case LOGIN -> {

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
                me.setShipboard(new LightShipBoard(me));
                phase = GamePhases.SHIPBOARD;
            }
            case S_END_DRAW_TILE_CARD -> {
                try {
                    view.showTurnedTiles(turnedTiles);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            case S_MANAGE_CARDS -> {
                if(gameMode==GameMode.TRIAL) {
                    try {
                        view.setClientState(ClientState.S_END_DRAW_TILE_CARD);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    previousState=state;
                }
            }
            case MANAGE_CABINS -> {
                if (!inManager) {
                    cabinsManager = new CabinsManager(me,view);
                    inManager = true;
                }
                try {
                    cabinsManager.setup();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                if (gameMode == GameMode.LEVEL2) {
                    if (me.getShipBoard().getCabinsCoordinates()==null||me.getShipBoard().getCabinsCoordinates().isEmpty()||me.getShipBoard().getCabinsCoordinates().size()==1) {
                        try {
                            virtualController.notifyNewCrewArrangement(new ArrayList<>());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        inManager = false;
                        try {
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                    try {
                        virtualController.notifyNewCrewArrangement(newTiles);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    inManager = false;
                    try {
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
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
                try {
                    view.printProjectile(displayedCard.getListOfProjectiles().getFirst());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                displayedCard.getListOfProjectiles().removeFirst();
            }
            case DRAW_CARD -> {
                phase = GamePhases.CARDS;
                if(me.getRank()!=1){
                    try {
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case MANAGE_GOODS -> {
                if(!inManager){
                    goodsManager = new GoodsManager(me, goodsList,view);
                    inManager=true;
                }
                try {
                    view.goodsPrinter(goodsList);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    view.showGenericMessage("Chose for each good where to put it, input 'no' to stop:\n");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Coordinates transformCoordinates(String[] input) {
        if (input.length < 2) {
            try {
                view.wrongLocalInput();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        int CoordinatesX;
        try {
            CoordinatesX = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            try {
                view.wrongLocalInput();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
        if(CoordinatesX<0||CoordinatesX>4){
            try {
                view.wrongLocalInput();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        int CoordinatesY;
        try {
            CoordinatesY = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            try {
                view.wrongLocalInput();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
        if(CoordinatesY<0||CoordinatesY>6){
            try {
                view.wrongLocalInput();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
        return new Coordinates(CoordinatesX, CoordinatesY);
    }

    private boolean checkShipBoards(String[] input){
        if (input[0].equals("check")) {
            int chose=numerate(scroll(input,1));
            if (chose==-1)
                return false;
            if (chose == 0) {
                try {
                    view.printShipboard(me.getShipBoard());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            if (chose >= 1 && chose <= 4) {
                try {
                    view.printShipboard(flightBoard.getInGamePlayers().get(indexDeckInHandOrPlanet).getShipBoard());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        }
        return false;
    }

    private boolean firstHourglassTurn(String[] input) {
        if (((gameMode==GameMode.LEVEL2 && input[0].equals("turn")) || input[0].equals("start")) && hourglassTurns == 0) {
            hourglassTurns=1;
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                try {
                    view.wrongLocalInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
            hourglassTurns=2;
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                view.setClientState(state);
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
                try {
                    view.wrongLocalInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
            if(hourglassTurns==1)
                hourglassTurns=2;
            else
                hourglassTurns=3;
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                view.setClientState(ClientState.S_FINISHED);
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
            try {
                view.wrongLocalInput();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return -1;
        }
        try {
            chose = Integer.parseInt(input[0]);
            return chose;
        } catch (NumberFormatException e) {
            try {
                view.wrongLocalInput();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
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

    public boolean rollBackState() {
        try {
            view.setClientState(previousState);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
    public void setPhase(GamePhases phase) {
        this.phase = phase;
    }

    public LightShipBoard getLightShipBoard() {
        return me.getShipBoard();
    }

    public void setDisplayedCard(Card displayedCard) {
        this.displayedCard = displayedCard;
    }
    public Card getDisplayedCard() {
        return displayedCard;
    }

    //deve copiare e incollare quelle tiles nelle loro coordinate per il player corrispondente
    //verrà usato in caso di batterie usate, goods o crewmate dispersi
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
        displayedCard=card;
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

    public ViewInterface getView() {
        return view;
    }

    public void setDeck(Map<Integer, ArrayList<Card>> deck) {
        this.deck = deck;
    }


    //Test getter
    public ClientState getState() {
        return state;
    }

    public ClientState getPreviousState() {
        return previousState;
    }

    public LightPlayer getMe() {
        return me;
    }

    public boolean isInManager() {
        return inManager;
    }

    public boolean isConnected() {
        return connected;
    }

    public GamePhases getPhase() {
        return phase;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public GoodsManager getGoodsManager() {
        return goodsManager;
    }

    public CabinsManager getCabinsManager() {
        return cabinsManager;
    }

    public Map<Integer, Tile> getTurnedTiles() {
        return turnedTiles;
    }

    public Map<Integer, ArrayList<Card>> getDeck() {
        return deck;
    }

    public Map<Integer, Boolean> getAvailableDeck() {
        return availableDeck;
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

    public int getIndexCard() {
        return indexCard;
    }

    public int getHourglassTurns() {
        return hourglassTurns;
    }

    public void setConnected(boolean connected) {this.connected = connected;}

    public void setGameMode(GameMode gameMode) {this.gameMode = gameMode;}

    void connectRMI() throws MalformedURLException, NotBoundException, RemoteException {
        ControllerFactory controllerFactory=(ControllerFactory) Naming.lookup("rmi://localhost/ControllerFactory");
        virtualController=controllerFactory.createController();
        virtualController.setView(this.view);
    }

    public void ping()  {
        try {
            virtualController.ping();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

