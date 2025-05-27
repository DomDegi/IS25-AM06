package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
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
    private String checking;
    private boolean positioned;

    private GameMode gameMode;
    private DisplayableView view;
    private VirtualController virtualController;
    private CoordInputManager coordInputManager;
    private GoodsManager goodsManager;
    private CabinsManager cabinsManager;
    private Map<Integer, Tile> turnedTiles;
    private final ArrayList<Tile> turnedTilesDisplayer;
    private Map<Integer, ArrayList<Card>> deck;
    private final Map<Integer, Boolean> availableDeck;
    private final Map<PlayersColor, Boolean> availableColors;
    private ArrayList<Goods> goodsList;
    private TextualInputParser inputParser;

    private int numPlayer;
    private Tile tileInHand;
    private ArrayList<Card> displayedCard;
    private int indexDeckInHandOrPlanet;
    private int hourglassTurns;

    private Thread serverWatchdogThread;

    public ClientController() {
        positioned=false;
        this.checking= null;
        this.numPlayer = 0;
        this.gameInfo = new ArrayList<>();
        this.view = new TUI();
        this.deck = new HashMap<>();
        this.me = new LightPlayer("", null);
        this.goodsList = new ArrayList<>();
        this.flightBoard = new LightFlightboard(new FlightBoard(null));
        this.inManager = false;
        this.connected = false;
        this.turnedTiles = new HashMap<>();
        this.turnedTilesDisplayer=new ArrayList<>();
        this.previousState = ClientState.CHOOSE_UI;
        this.state = ClientState.CHOOSE_UI;
        this.phase = GamePhases.LOGIN;
        this.availableDeck = new HashMap<>(3);
        availableDeck.put(1, Boolean.TRUE);
        availableDeck.put(2, Boolean.TRUE);
        availableDeck.put(3, Boolean.TRUE);
        this.availableColors = new HashMap<>(4);
        availableColors.put(PlayersColor.RED, Boolean.TRUE);
        availableColors.put(PlayersColor.YELLOW, Boolean.TRUE);
        availableColors.put(PlayersColor.GREEN, Boolean.TRUE);
        availableColors.put(PlayersColor.BLUE, Boolean.TRUE);
        this.indexDeckInHandOrPlanet = 0;
        this.hourglassTurns = 0;
        displayedCard = new ArrayList<>();
    }


    public VirtualController getVirtualController() {
        return virtualController;
    }

    public void chooseUI(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        if (words.length == 0 || words[0].isEmpty()) {
            view.wrongLocalInput();
            return;
        }
        switch (words[0]) {
            case "gui", "g" -> setGUI(new GUI());
            case "tui", "t" -> setTUI(new TUI());
            default -> view.wrongLocalInput();
        }
    }

    public boolean input(String input) {
        return inputParser.input(input);
    }

    //METODI PER FARE ANDARE TUTTO
    public boolean doneNaming() {
        if(state!=ClientState.LOGIN){
            return false;
        }
        if (!Objects.equals(me.getPlayerName(), "")) {
            setState(ClientState.WAIT);
            try {
                virtualController.login(me.getPlayerName());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    public boolean createGame(String gameName, int numberOfPlayers, GameMode mode) {
        if(state!=ClientState.LOBBY&&state!=ClientState.LOBBY0&&state!=ClientState.LOBBY1&&state!=ClientState.COLOR_CHOICE0){
            return false;
        }
        for (GameInfo games : gameInfo) {
            if (Objects.equals(gameName, games.getGameName())) {
                view.wrongLocalInput();
                return false;
            }
        }
        if (numberOfPlayers<2||numberOfPlayers>4) {
            view.wrongLocalInput();
            return false;
        }
        numPlayer = numberOfPlayers;
        gameMode = mode;
        setState(ClientState.WAIT);
        try {
            virtualController.createGame(gameName, numberOfPlayers, gameMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean joinGame(String gameName) {
        if(state!=ClientState.LOBBY&&state!=ClientState.LOBBY0&&state!=ClientState.LOBBY1){
            return false;
        }
        if (gameInfo == null) {
            view.wrongLocalInput();
            setState(ClientState.LOBBY);
            return false;
        }
        for (GameInfo games : gameInfo) {
            if (Objects.equals(gameName, games.getGameName())) {
                setState(ClientState.WAIT);
                numPlayer = games.getMaxPlayerCount();
                try {
                    virtualController.joinGame(gameName);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        }
        view.wrongLocalInput();
        setState(ClientState.LOBBY);
        return false;
    }

    public boolean leaveGame() {
        decksNotAvailable(new ArrayList<>());
        colorsNotAvailable(new ArrayList<>());
        if (gameMode != null) {
            try {
                virtualController.leaveGame();
                return true;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            view.wrongLocalInput();
            rollBackState();
        }
        return false;
    }

    public boolean colorChoice(PlayersColor color) {
        if(state!=ClientState.COLOR_CHOICE&&state!=ClientState.COLOR_CHOICE0&&state!=ClientState.WAIT){
            return false;
        }
        if(getAvailableColors().get(color)) {
            me.setColor(color);
            setState(ClientState.WAIT);
            try {
                virtualController.chooseColor(color);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    public boolean drawDeck(int chose) {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        if (gameMode == GameMode.LEVEL2) {
            if (chose > 0 && chose < 4 && availableDeck.get(chose)) {
                setState(ClientState.WAIT);
                try {
                    virtualController.lookCardsRequest(chose);
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
        return true;
    }

    public boolean drawTile() {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.reqDrawTileFromStack();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean drawBooked(int index) {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        if (!me.getShipBoard().getBookedTiles().isEmpty()) {
            if (me.getShipBoard().getBookedTiles().size() > index) {
                tileInHand = me.getShipBoard().getBookedTiles().get(index);
                try {
                    view.showDrawnTile(tileInHand);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                me.getShipBoard().removeBookedTile(index);
            } else {
                tileInHand = me.getShipBoard().getBookedTiles().getFirst();
                me.getShipBoard().removeBookedTile(0);
            }
            setState(ClientState.S_MANAGE_DRAWN_TILE);
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    public boolean drawDrawn(int chose) {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        if (chose > 0 && turnedTiles.containsKey(chose)) {
            tileInHand = turnedTiles.get(chose);
            setState(ClientState.WAIT);
            try {
                virtualController.reqDrawTileFromTurned(chose);
            } catch (RemoteException e) {
                view.wrongLocalInput();
                return false;
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    public boolean doneShipboard() {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.notifyCompleted();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean stopLookingAtCards() {
        if(state!=ClientState.S_MANAGE_CARDS){
            return false;
        }
        setState(ClientState.S_END_DRAW_TILE_CARD);
        try {
            virtualController.stopLookingAtCardsRequest();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean rotateTile() {
        if(state!=ClientState.S_MANAGE_DRAWN_TILE){
            return false;
        }
        this.tileInHand.rotate();
        try {
            view.showDrawnTile(tileInHand);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean positionTile(Coordinates coordinates) {
        if(state!=ClientState.S_MANAGE_DRAWN_TILE){
            return false;
        }
        if (coordinates != null && me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand), coordinates)) {
            setState(ClientState.WAIT);
            try {
                virtualController.notifySetTile(this.tileInHand.send());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            view.wrongLocalInput();
            return false;
        }
    }

    public boolean refuseTile() {
        if(state!=ClientState.S_MANAGE_DRAWN_TILE){
            return false;
        }
        if (this.tileInHand.isBooked()) {
            if (!me.getShipBoard().addBookedTile(this.tileInHand)) {
                view.wrongLocalInput();
                return false;
            }
            setState(ClientState.S_END_DRAW_TILE_CARD);
            return true;
        }
        this.turnedTiles.put(this.tileInHand.getKey(), this.tileInHand);
        setState(ClientState.WAIT);
        try {
            virtualController.notifyRefusedTile();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean bookTile() {
        if(state!=ClientState.S_MANAGE_DRAWN_TILE){
            return false;
        }
        if(this.gameMode == GameMode.TRIAL) {
            view.wrongLocalInput();
            return false;
        }
        if (!me.getShipBoard().addBookedTile(this.tileInHand)) {
            view.wrongLocalInput();
            return false;
        }
        if(tileInHand.isBooked()){
            setState(ClientState.S_END_DRAW_TILE_CARD);
        }else{
            this.tileInHand.setBooked(true);
            setState(ClientState.WAIT);
            try {
                virtualController.notifyTileBooking();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public boolean positionOnFlightBoard(int chose) {
        if(state!=ClientState.S_FINISHED&&positioned){
            return false;
        }
        if (gameMode == GameMode.LEVEL2) {
            if (chose > 0 && chose <= numPlayer) {
                me.getShipBoard().setGetStat();
                try {
                    positioned=true;
                    virtualController.notifySetPosition(chose);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                view.wrongLocalInput();
                return false;
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    public boolean rollDice() {
        if(state!=ClientState.ROLL_DICE){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.rollTheDices();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean drawCard() {
        if(state!=ClientState.DRAW_CARD){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.drawCards();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean sayYes() {
        if(state!=ClientState.ACTION){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.sendYes();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean sayNo() {
        if(state!=ClientState.ACTION){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.sendNo();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean choosePlanet(int chose) {
        if(state!=ClientState.PLANET_CHOICE){
            return false;
        }
        Card planet = displayedCard.getFirst();
        if (chose >= 0 && chose <= planet.getListOfPlanets().size()) {
            indexDeckInHandOrPlanet = chose;
            setState(ClientState.WAIT);
            try {
                virtualController.planetChoiceRequest(chose);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    public boolean doneGoods() {
        if(state!=ClientState.MANAGE_GOODS){
            return false;
        }
        if (inManager) {
            HashSet<CargoHold> newTilesGoods;
            newTilesGoods = goodsManager.doneGoods();
            ArrayList<CargoHold> newTiles;
            if (newTilesGoods != null) {
                newTiles = new ArrayList<>(newTilesGoods);
                if (newTiles.getFirst().getTotSpaces() == -1) {
                    newTiles.clear();
                }
                int goodsVal = me.getShipBoard().convertGoodsToCredit();
                setState(ClientState.WAIT);
                try {
                    virtualController.notifyNewGoodsArrangement(goodsVal, newTiles);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                inManager = false;
            }
        }
        return true;
    }

    public boolean chooseGoods(int chose) {
        if(state!=ClientState.MANAGE_GOODS){
            return false;
        }
        if (inManager) {
            goodsManager.chooseGoods(chose);
            return true;
        } else {
            return false;
        }
    }

    public boolean chooseCargo(Coordinates coords) {
        if(state!=ClientState.MANAGE_GOODS){
            return false;
        }
        if (inManager) {
            goodsManager.chooseCargo(coords);
            return true;
        } else {
            return false;
        }
    }

    public boolean setUpCabins() {
        if(state!=ClientState.MANAGE_CABINS){
            return false;
        }
        cabinsManager.setup();
        return true;
    }

    public boolean manageCabins(CrewType type) {
        if(state!=ClientState.MANAGE_CABINS){
            return false;
        }
        if (gameMode == GameMode.LEVEL2 && inManager) {
            ArrayList<Tile> newTiles;
            if (!(me.getShipBoard().getCabinsCoordinates().isEmpty() || me.getShipBoard().getCabinsCoordinates().size() == 1)) {
                newTiles = cabinsManager.manageCabins(type);
                if (newTiles != null) {
                    phase=GamePhases.CARDS;
                    setState(ClientState.WAIT);
                    try {
                        inManager = false;
                        me.getShipBoard().setGetStat();
                        virtualController.notifyNewCrewArrangement(newTiles);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return true;
    }

    public boolean doneCoord() {
        if(state!=ClientState.COORD_REQUEST) {
            return false;
        }
        view.sendingCoordinates();
        return coordInputManager.endCheckingFase();
    }

    public boolean checkCoord(Coordinates coords) {
        if(state!=ClientState.COORD_REQUEST) {
            return false;
        }
        return coordInputManager.checkCoord(coords);
    }

    //------------------------------------------------------------------------------------------------------------------------------

    public void setState(ClientState newState) {
        previousState = state;
        state = newState;
        switch (newState) {
            case START_SHIP_CREATION -> phase = GamePhases.SHIPBOARD;

            case S_END_DRAW_TILE_CARD -> {
                phase = GamePhases.SHIPBOARD;
                view.showTurnedTiles(turnedTiles);
                view.printShipboard(me.getShipBoard());
                view.printBooked(me.getShipBoard());
            }
            case S_MANAGE_CARDS -> {
                phase = GamePhases.SHIPBOARD;
                me.getShipBoard().setGetStat();
                if(gameMode==GameMode.TRIAL) {
                    setState(ClientState.S_END_DRAW_TILE_CARD);
                    previousState = state;
                }
            }
            case COORD_REQUEST ->
                    this.coordInputManager = new CoordInputManager(me.getShipBoard(), this);

            case MANAGE_CABINS -> {
                if (!inManager) {
                    cabinsManager = new CabinsManager(me, view);
                    inManager = true;
                }
                if (gameMode == GameMode.LEVEL2) {
                    if (me.getShipBoard().getCabinsCoordinates() == null || me.getShipBoard().getCabinsCoordinates().isEmpty() || me.getShipBoard().getCabinsCoordinates().size() == 1) {
                        setState(ClientState.WAIT);
                        setState(ClientState.WAIT_TO_DRAW);
                        try {
                            virtualController.notifyNewCrewArrangement(new ArrayList<>());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        inManager = false;
                    } else {
                        cabinsManager.setup();
                    }
                } else if (gameMode == GameMode.TRIAL) {
                    me.getShipBoard().setGetStat();
                    me.setAllCrewToHuman();
                    CrewType type = CrewType.HUMAN;
                    ArrayList<Tile> newTiles = new ArrayList<>();
                    for (Coordinates _ : me.getShipBoard().getCabinsCoordinates()) {
                        newTiles = cabinsManager.manageCabins(type);
                    }
                    view.crewPositioned();
                    setState(ClientState.WAIT);
                    try {
                        virtualController.notifyNewCrewArrangement(newTiles);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    inManager = false;
                }
            }
            case S_FINISHED -> {
                if(displayedCard!=null)
                    displayedCard.clear();
                if (gameMode == GameMode.TRIAL) {
                    try {
                        me.getShipBoard().setGetStat();
                        me.setAllCrewToHuman();
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
            case WAIT_TO_DRAW ->
                    phase = GamePhases.CARDS;
            case DRAW_CARD -> phase = GamePhases.CARDS; /*if(me.getRank()!=1){
                    setState( ClientState.WAIT);
                } //MI FIDO DI QUELLO CHE MI DICE IL SERVER(prova)*/
            case MANAGE_GOODS -> {
                if (!inManager) {
                    me.getShipBoard().setGetStat();
                    this.goodsList =displayedCard.getFirst().getChosenPlanets(indexDeckInHandOrPlanet - 1);
                    goodsManager = new GoodsManager(me, goodsList, view);
                    inManager = true;
                }
                view.goodsPrinter(goodsList);
                view.showGenericMessage("Chose for each good where to put it, input 'no' to stop:\n");
            }
        }
        try {
            view.setClientState(state);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Coordinates transformCoordinates(String[] input) {
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
        if (CoordinatesX < 0 || CoordinatesX > 4) {
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
        if (CoordinatesY < 0 || CoordinatesY > 6) {
            view.wrongLocalInput();
            return null;
        }
        return new Coordinates(CoordinatesX, CoordinatesY);
    }

    public boolean checkShipBoards(String[] input) {
        if (input[0].equals("check")) {
            if (!(input.length > 1)) {
                view.wrongLocalInput();
                return false;
            }
            int chose = numerate(scroll(input, 1));
            if (chose == -1)
                return false;
            return check(chose);
        } else {
            return false;
        }
    }

    public boolean check(int chose){
        ArrayList<LightPlayer>players=new ArrayList<>();
        for(LightPlayer player:flightBoard.getInGamePlayers()){
            if(Objects.equals(player.getPlayerName(), getMe().getPlayerName()))
                continue;
            players.add(player);
        }
        players.sort((p1, p2) -> Integer.compare(p2.getPlayerColor().toInt(), p1.getPlayerColor().toInt()));
        if (chose >= 0 && chose < flightBoard.getInGamePlayers().size()) {
            setChecking(players.get(chose-1).getPlayerName());
            view.checkShipboard(players.get(chose-1).getShipBoard());
            return true;
        } else {
            return false;
        }
    }

    public String isChecking(){
        return checking;
    }

    public void setChecking(String checking) {
        this.checking = checking;
    }

    public boolean firstHourglassTurn() {
        if(hourglassTurns == 0) {
            if (gameMode == GameMode.LEVEL2) {
                hourglassTurns = 1;
            }
            setState(ClientState.WAIT);
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            view.wrongLocalInput();
            return false;
        }
    }

    public boolean secondHourglassTurn() {
        if (hourglassTurns == 1) {
            if (gameMode == GameMode.TRIAL) {
                view.wrongLocalInput();
                return false;
            }
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    public boolean thirdHourglassTurn() {
        if (hourglassTurns >= 1 && hourglassTurns < 3) {
            if (gameMode == GameMode.TRIAL) {
                view.wrongLocalInput();
                return false;
            }
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    private LightPlayer playerFinder(String playerName) {
        LightPlayer searchedPlayer = null;
        for (LightPlayer player : flightBoard.getInGamePlayers())
            if (player.getPlayerName().equals(playerName)) {
                searchedPlayer = player;
            }
        return searchedPlayer;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        me.setShipboard(new LightShipBoard(me));
        if (gameMode == GameMode.LEVEL2) {
            me.getShipBoard().initializeLevel2();
            for (LightPlayer player : flightBoard.getInGamePlayers()) {
                new LightShipBoard(player);
                player.getShipBoard().initializeLevel2();
            }
        } else if (gameMode == GameMode.TRIAL) {
            me.getShipBoard().initializeTestFlight();
            for (LightPlayer player : flightBoard.getInGamePlayers()) {
                new LightShipBoard(player);
                player.getShipBoard().initializeTestFlight();
            }
        }
    }

    public void setGameModeWithoutInitializing(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean land(String[] input) {
        if (input[0].equals("earlyland") && !me.isLanded()) {
            try {
                virtualController.notifyEarlyLanding();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    public int numerate(String[] input) {
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

    public String[] scroll(String[] input, int times) {
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
        if (state == previousState)
            return;
        setState(previousState);
    }

    public void addToFlightboard(String name, PlayersColor color, int pos, int ranking) {
        if (Objects.equals(name, me.getPlayerName())) {
            flightBoard.addInGamePlayer(me);
            return;
        }
        LightPlayer player = new LightPlayer(name, color);
        player.setPosition(pos);
        player.setRank(ranking);
        flightBoard.addInGamePlayer(player);
    }

    public void updateFlightboard(String name, PlayersColor color, int pos, int ranking) {
        for (LightPlayer player : flightBoard.getInGamePlayers())
            if (player.getPlayerName().equals(name)) {
                player.setPosition(pos);
                player.setRank(ranking);
                view.printFlightboard(flightBoard);
                return;
            }
        addToFlightboard(name, color, pos, ranking);
    }

    public LightFlightboard getFlightBoard() {
        return flightBoard;
    }

    public void addTurnedTile(Tile tile) {
        turnedTiles.put(tile.getKey(), tile);
        turnedTilesDisplayer.add(tile);
        view.showTurnedTiles(turnedTiles);
    }

    public void removeTurnedTile(Tile tile) {
        turnedTiles.remove(tile.getKey());
        turnedTilesDisplayer.removeIf(t -> t.getKey() == tile.getKey());
        view.showTurnedTiles(turnedTiles);
    }

    public String getName() {
        return me.getPlayerName();
    }

    public void setTileInHand(Tile tile) {
        tileInHand = tile;
    }

    public void setTile(String playerName, Tile tile) {
        LightShipBoard lightShipBoard = flightBoard.getInGamePlayer(playerName).getShipBoard();
        lightShipBoard.positionTile(Optional.of(tile), tile.getCoordinates());
        if(isChecking()!=null&& Objects.equals(isChecking(), playerName)){
            LightPlayer player = playerFinder(playerName);
            view.checkShipboard(player.getShipBoard());
        }
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
    public void modifyTiles(String playerName, ArrayList<Tile> tiles) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        for (Tile modTiles : tiles) {
            player.getShipBoard().positionTile(Optional.of(modTiles), modTiles.getCoordinates());
        }
    }

    //rompe le tile nelle coordinate corrispondenti per il player selezionato
    public void brokenTiles(String playerName, ArrayList<Coordinates> coordinates) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        player.getShipBoard().destroy(coordinates);
    }

    //aggiungi booked tile al player (dalla light shipboard) con check sul senso dell'invocazione
    public void addBookedTile(String playerName, Tile tile) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        player.getShipBoard().addBookedTile(tile);
    }

    //rimuovi booked tile al player (dalla light shipboard) con check sul senso dell'invocazione
    public void removeBookedTile(String playerName, Tile tile) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        int index = 0;
        for (Tile bookedTile : player.getShipBoard().getBookedTiles()) {
            if (bookedTile == tile)
                player.getShipBoard().removeBookedTile(index);
            index++;
        }
    }

    public void gainCredit(String playerName, int credits) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        player.gainCredits(credits);
    }

    public void updateModel(String currentGameStatus) {
        UpdateDeserializer.unpack(this,currentGameStatus);
    }

    public void decksNotAvailable(ArrayList<Integer> notAvailableDecks) {
        for (int index = 0; index < 4; index++) {
            availableDeck.put(index, Boolean.TRUE);
        }
        for (int index : notAvailableDecks) {
            if (notAvailableDecks.contains(index))
                availableDeck.put(index, Boolean.FALSE);
        }
        view.showAvailableDecks();
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

    public void colorsNotAvailable(ArrayList<PlayersColor> notAvailableColors) {
        for(PlayersColor color:PlayersColor.values()){
            availableColors.put(color,Boolean.TRUE);
        }
        for(PlayersColor color:notAvailableColors){
            this.availableColors.put(color, Boolean.FALSE);
        }
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

    public ArrayList<Tile> getTurnedTilesDisplayer() {
        return turnedTilesDisplayer;
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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean connectRMI(String ip, int port) throws MalformedURLException, NotBoundException, RemoteException {
        if(state!=ClientState.CHOOSE_CONNECTION_TYPE&&state!=ClientState.CHOOSE_IP_AND_PORT_RMI){
            return false;
        }
        VirtualViewRMI viewRMI = new VirtualViewRMI(this, view);
        if(ip!=null&& !ip.isEmpty())
            ip="localhost";
        String url = String.format("rmi://%s:%d/ControllerFactory", ip, port);
        ControllerFactory controllerFactory = (ControllerFactory) Naming.lookup(url);
        this.virtualController = controllerFactory.createController();
        virtualController.setView(viewRMI);
        setState(ClientState.LOGIN);
        return true;
    }

    public boolean connectSocket(String ip, int port) throws IOException,NotBoundException {
        if(state!=ClientState.CHOOSE_CONNECTION_TYPE&&state!=ClientState.CHOOSE_IP_AND_PORT_SOCKET){
            return false;
        }
        if(ip==null|| ip.isEmpty())
            ip="localhost";
        Socket server;
        try {
            server = new Socket(ip, port);
        } catch (Exception e) {
            System.out.println("Server unreachable, check the port and the ip address");
            return false;
        }
        ServerHandler serverHandler = new ServerHandler(server);
        virtualController = new VirtualControllerSocket(view, serverHandler);
        Thread waitForSetup = new Thread(serverHandler, "wait for setup of " + server.getInetAddress().getHostAddress());
        waitForSetup.start();
        while (!serverHandler.isReady()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("waiting for view and virtual controller setup in serverHandler");
            }
        }
        serverHandler.setVirtualController(virtualController);
        serverHandler.setView(view);
        serverHandler.setClientController(this);
        setState(ClientState.LOGIN);
        return true;
    }

    public void ping() {
        try {
            virtualController.ping();  // pings server
            restartServerWatchdog();  // restarts watchdog timer
        } catch (RemoteException e) {
            this.setState(ClientState.RECONNECTING);
        }
    }

    private void restartServerWatchdog() {
        // interrupts existing thread if it exists
        if (serverWatchdogThread != null && serverWatchdogThread.isAlive()) {
            serverWatchdogThread.interrupt();
        }

        // Creates new thread that awaits timeout
        serverWatchdogThread = new Thread(() -> {
            try {
                Thread.sleep(30000);
                // Se il thread non è stato interrotto in tempo, il server è considerato down
                this.setState(ClientState.RECONNECTING);
            } catch (InterruptedException ignored) {
                // thread was interrupted from a new server ping: is all good
            }
        });

        serverWatchdogThread.start();
    }

    public void turnHourglass(int i) {
        hourglassTurns = i;
    }

    public void setGUI(GUI gui) {
        this.view =gui;
        GUI.startGui(this);
    }

    public void setTUI(TUI tui) {
        this.view = tui;
        inputParser = new TextualInputParser(this);
        setState(ClientState.CHOOSE_CONNECTION_TYPE);
    }

    public ArrayList<GameInfo> getGameInfo() {
        return gameInfo;
    }

    public Map<PlayersColor, Boolean> getAvailableColors() {
        return availableColors;
    }

    public ClientState getPreviousState() {
        return previousState;
    }

    public void setHourglassTurns(int hourglassTurns) {
        this.hourglassTurns = hourglassTurns;
    }

    public void setTurnedTiles(Map<Integer, Tile> turnedTiles) {
        this.turnedTiles = turnedTiles;
    }

    public void putInStandby () {
        this.state = ClientState.WAIT;
    }

    public void victimOfThePenalty(String playerName) {
        view.victimOfThePenalty(playerName,this.displayedCard.getFirst().getPenalty());
    }

    public Map<Integer, Boolean> getAvailableDeck() {
        return availableDeck;
    }

    public Map<Integer, PlayersColor> getAvailablePosition(){
        Map<Integer,PlayersColor> availablePosition = new HashMap<>();
        for(int i=1;i<=4;i++){
            if(i<=flightBoard.getInGamePlayers().size())
                availablePosition.put(i,null);
        }
        if(getGameMode()==GameMode.LEVEL2){
            flightBoard.getInGamePlayers().forEach(p -> {
                if (p.getPosition() == 6 && p.getRank() != 0) {
                    availablePosition.put(1, p.getPlayerColor());
                }
                if (p.getPosition() == 3 && p.getRank() != 0) {
                    availablePosition.put(2, p.getPlayerColor());
                }
                if (p.getPosition() == 1 && p.getRank() != 0) {
                    availablePosition.put(3, p.getPlayerColor());
                }
                if (p.getPosition() == 0 && p.getRank() != 0) {
                    availablePosition.put(4, p.getPlayerColor());
                }
            });
        }else if(getGameMode()==GameMode.TRIAL){
            flightBoard.getInGamePlayers().forEach(p -> {
                if (p.getPosition() == 4 && p.getRank() != 0) {
                    availablePosition.put(1, p.getPlayerColor());
                }
                if (p.getPosition() == 2 && p.getRank() != 0) {
                    availablePosition.put(2, p.getPlayerColor());
                }
                if (p.getPosition() == 1 && p.getRank() != 0) {
                    availablePosition.put(3, p.getPlayerColor());
                }
                if (p.getPosition() == 0 && p.getRank() != 0) {
                    availablePosition.put(4, p.getPlayerColor());
                }
            });
        }
        return availablePosition;
    }

    public boolean isPositioned() {
        return positioned;
    }

    public CabinsManager getCabinsManager() {
        return cabinsManager;
    }
}

