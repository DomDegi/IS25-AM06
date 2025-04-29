package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Planets;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.Client.VirtualViewRMI;
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
    private LightFlightboard flightBoard;
    private boolean inManager;
    private boolean connected;
    private GamePhases phase;

    private GameMode gameMode;
    private ViewInterface view;
    private VirtualController virtualController;
    private final CoordInputManager coordInputManager;
    private GoodsManager goodsManager;
    private CabinsManager cabinsManager;
    private Map<Integer,Tile> turnedTiles;
    private Map<Integer, ArrayList<Card>> deck;
    private final Map<Integer,Boolean> availableDeck;
    private ArrayList<Goods> goodsList;

    private Tile tileInHand;
    private Card displayedCard;
    private int indexDeckInHandOrPlanet;
    private int indexCard;
    private int hourglassTurns;

    public ClientController(VirtualController virtualController) throws RemoteException {
        this.virtualController = virtualController;
        this.view=new VirtualViewRMI(this,new TUI());
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
        this.virtualController = virtualController;
        this.indexCard = 0;
        this.indexDeckInHandOrPlanet = 0;
        this.hourglassTurns = 0;
        this.coordInputManager=new CoordInputManager(me.getShipBoard(),this);
    }

    public VirtualController getVirtualController() {
        return virtualController;
    }

    public boolean input(String input) throws RemoteException {
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
                    case "gui"->
                            this.view=new VirtualViewRMI(this,new GUI());
                    case "tui"->
                            this.view=new VirtualViewRMI(this,new TUI());
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                view.setClientState(ClientState.CHOOSE_CONNECTION_TYPE);
            }

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi"->{}
                        //Da sistemare
                    case "socket"->{}
                    //Da sistemare
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                view.setClientState(ClientState.LOGIN);
            }

            case LOGIN->{
                switch (words[0]) {
                    case "done" -> {
                        if(!connected){
                            //start connection
                        }
                        virtualController.login(name);
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                        if(NumberOfPlayers==-1||NumberOfPlayers>4)
                            return false;
                        switch(words[3]) {
                            case "trialmode"-> gameMode=GameMode.TRIAL;
                            case "level2mode"-> gameMode=GameMode.LEVEL2;
                            default->{
                                view.wrongLocalInput();
                                return false;
                            }
                        }
                        virtualController.createGame(gameName,NumberOfPlayers,gameMode,name);
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                    case "joingame"-> {
                        String gameName = words[1];
                        virtualController.joinGame(gameName,name);
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                    case "red"->
                        color = PlayersColor.RED;
                    case "yellow"->
                        color = PlayersColor.YELLOW;
                    case "green"->
                        color = PlayersColor.GREEN;
                    case "blue"->
                        color = PlayersColor.BLUE;
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                virtualController.chooseColor(this.name,color);
                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
            }

            case DRAW_CARD ->{
                virtualController.drawCards(this.name);
                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
            }

            case ACTION-> {
                if (checkShipBoards(words))
                    return true;
                if (land(words))
                    return true;
                switch (words[0]){
                    case "yes" ->
                        virtualController.sendYes(this.name);
                    case "no" ->
                        virtualController.sendNo(this.name);
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
            }

            case PLANET_CHOICE-> {
                if (checkShipBoards(words))
                    return true;
                if (land(words))
                    return true;
                if (words[0].equals("no"))
                    words[0]="0";
                int chose=numerate(words);
                if(chose==-1)
                    return false;
                Planets planet=(Planets) displayedCard;
                if (chose >= 0 && chose <planet.getListOfPlanets().size()) {
                    indexDeckInHandOrPlanet = chose;
                    virtualController.planetChoiceRequest(this.name,chose);
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                }
            }

            case MANAGE_GOODS-> {
                if(!inManager){
                    goodsManager = new GoodsManager(me, goodsList);
                    inManager=true;
                }
                if (checkShipBoards(words))
                    return true;
                if (land(words))
                    return true;
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
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                }
            }

            case MANAGE_CABINS -> {
                if(gameMode==GameMode.LEVEL2){
                    if (!inManager) {
                    cabinsManager = new CabinsManager(me);
                    inManager = true;
                    }
                    CrewType type;
                    ArrayList<Tile> newTiles = new ArrayList<>();
                    switch (words[0]) {
                        case "humans" -> type = CrewType.HUMAN;
                        case "brownalien" -> type = CrewType.BROWN;
                        case "purplealien" -> type = CrewType.PURPLE;
                        default -> {
                            view.wrongLocalInput();
                            return false;
                        }
                    }
                    if (me.getShipBoard().getCabinsCoordinates().isEmpty()) {
                        virtualController.notifyNewCrewArrangement(this.name, newTiles);
                        inManager = false;
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    } else {
                        newTiles = cabinsManager.manageCabins(type);
                        if (!newTiles.isEmpty()) {
                            virtualController.notifyNewCrewArrangement(this.name, newTiles);
                            inManager = false;
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        }
                    }
                }
            }

            case COORD_REQUEST -> {
                if (checkShipBoards(words))
                    return true;
                if (phase != GamePhases.CARDS) {
                    if (land(words))
                        return true;
                }
                if (words[0].equals("done"))
                    return coordInputManager.endCheckingFase();
                else {
                    //gestire il -1 in base alla carta
                    Coordinates coords = transformCoordinates(words);
                    if(coords==null)
                        return false;
                    if(!coordInputManager.checkCoord(coords, -1)) {
                        view.wrongLocalInput();
                        return false;
                    }
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
                        virtualController.notifyCompleted(this.name);
                        view.setClientState(ClientState.S_FINISHED);
                    }
                    case "draw" -> {
                        switch (words[1]) {
                            case "card" -> {
                                if(gameMode==GameMode.LEVEL2) {
                                    int chose = numerate(scroll(words, 2));
                                    if (chose == -1)
                                        return false;
                                    if (chose > 0 && chose < 4 && availableDeck.get(chose)) {
                                        virtualController.lookCardsRequest(name, chose);
                                        indexDeckInHandOrPlanet = chose;
                                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).getFirst();
                                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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
                                switch (words[2]){
                                    case "new"->{
                                        virtualController.reqDrawTileFromStack(this.name);
                                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                    }
                                    case "b1" ->{
                                        if(!me.getShipBoard().getBookedTiles().isEmpty()) {
                                            tileInHand = me.getShipBoard().getBookedTiles().getFirst();
                                            me.getShipBoard().removeBookedTile(0);
                                            view.setClientState(ClientState.S_MANAGE_DRAWN_TILE);
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
                                            view.setClientState(ClientState.S_MANAGE_DRAWN_TILE);
                                        }else{
                                            view.wrongLocalInput();
                                            return false;
                                        }
                                    }
                                    default-> {
                                        int chose=numerate(scroll(words,2));
                                        if (chose==-1)
                                            return false;
                                        if (chose>0&&turnedTiles.containsKey(chose)) {
                                            tileInHand=turnedTiles.get(chose);
                                            virtualController.reqDrawTileFromTurned(this.name,chose);
                                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                                        }else {
                                            view.wrongLocalInput();
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
                        view.showCard(displayedCard);
                    }
                    case "next" -> {
                        indexCard++;
                        if(indexCard>2)
                            indexCard=0;
                        displayedCard = this.deck.get(indexDeckInHandOrPlanet).get(indexCard);
                        view.showCard(displayedCard);
                    }
                    case "done" -> {
                        //done action
                        virtualController.stopLookingAtCardsRequest(this.name);
                        view.setClientState(ClientState.S_END_DRAW_TILE_CARD);
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
                        Coordinates coordinates=transformCoordinates(scroll(words,1));
                        if(coordinates!=null){
                            me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand),coordinates);
                            virtualController.notifySetTile(this.name,this.tileInHand);
                            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                        }else
                            return false;
                    }
                    case "refuse" -> {
                        if(this.tileInHand.isBooked()){
                            me.getShipBoard().addBookedTile(this.tileInHand);
                            view.setClientState(ClientState.S_END_DRAW_TILE_CARD);
                            return true;
                        }
                        this.turnedTiles.put(this.tileInHand.getKey(),this.tileInHand);
                        virtualController.notifyRefusedTile(this.name);
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                    case "book" -> {
                        if(!me.getShipBoard().addBookedTile(this.tileInHand)){
                            view.wrongLocalInput();
                            return false;
                        }
                        this.tileInHand.setBooked(true);
                        virtualController.notifyTileBooking(this.name);
                        view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                    }
                }
            }

            case S_FINISHED-> {
                if (checkShipBoards(words))
                    return true;
                if (thirdHourglassTurn(words))
                    return true;
                if(gameMode==GameMode.LEVEL2) {
                    int chose = numerate(scroll(words, 0));
                    if (chose == -1)
                        return false;
                    if (chose > 0 && flightBoard.getInGamePlayers().size() > chose) {
                        virtualController.notifySetPosition(name, chose);
                    } else {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case ROLL_DICE->{
                virtualController.rollTheDices(name);
                view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
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

    public void setState(ClientState newState) throws RemoteException {
        previousState=state;
        state=newState;
        switch(newState){
            case START_SHIP_CREATION -> {
                me.setShipboard(new LightShipBoard(me));
                phase = GamePhases.SHIPBOARD;
            }
            case DRAW_CARD -> {
                phase = GamePhases.CARDS;
                if(me.getRank()!=1){
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                }
            }
            case MANAGE_GOODS -> {
                if(!inManager){
                    goodsManager = new GoodsManager(me, goodsList);
                    inManager=true;
                }
                goodsManager.goodsPrinter(goodsList);
                System.out.print("Chose for each good where to put it, input 'no' to stop:\n");
            }
            case S_END_DRAW_TILE_CARD ->
                    view.showTurnedTiles(turnedTiles);
            case S_MANAGE_CARDS -> {
                if(gameMode==GameMode.TRIAL) {
                    view.setClientState(ClientState.S_END_DRAW_TILE_CARD);
                    previousState=state;
                }
            }
            case MANAGE_CABINS -> {
                if (gameMode == GameMode.TRIAL) {
                    if (!inManager) {
                        cabinsManager = new CabinsManager(me);
                        inManager = true;
                    }
                    CrewType type = CrewType.HUMAN;
                    ArrayList<Tile> newTiles = new ArrayList<>();
                    for (Coordinates cabinsCoordinates : me.getShipBoard().getCabinsCoordinates()) {
                        newTiles = cabinsManager.manageCabins(type);
                    }
                    virtualController.notifyNewCrewArrangement(this.name, newTiles);
                    inManager = false;
                    view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
                }
            }
            case S_FINISHED-> {
                if (gameMode == GameMode.TRIAL) {
                    virtualController.notifySetPosition(name, 0);
                }
            }
            case ROLL_DICE -> {
                view.printProjectile(displayedCard.getListOfProjectiles().getFirst());
                displayedCard.getListOfProjectiles().removeFirst();
            }
        }
    }

    private Coordinates transformCoordinates(String[] input) throws RemoteException {
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

    private boolean checkShipBoards(String[] input) throws RemoteException {
        if (input[0].equals("check")) {
            int chose=numerate(scroll(input,1));
            if (chose==-1)
                return false;
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

    private boolean firstHourglassTurn(String[] input) throws RemoteException {
        if (((gameMode==GameMode.LEVEL2 && input[0].equals("turn")) || input[0].equals("start")) && hourglassTurns == 0) {
            hourglassTurns=1;
            virtualController.sendTurnHourGlass(this.name);
            view.setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);
            return true;
        }
        return false;
    }

    private boolean secondHourglassTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && hourglassTurns == 1) {
            if(gameMode==GameMode.TRIAL){
                view.wrongLocalInput();
                return false;
            }
            hourglassTurns=2;
            virtualController.sendTurnHourGlass(this.name);
            return true;
        }
        return false;
    }

    private boolean thirdHourglassTurn(String[] input) throws RemoteException {
        if (input[0].equals("turn") && hourglassTurns >= 1 && hourglassTurns < 3) {
            if(gameMode==GameMode.TRIAL){
                view.wrongLocalInput();
                return false;
            }
            hourglassTurns=3;
            virtualController.sendTurnHourGlass(this.name);
            view.setClientState(ClientState.S_FINISHED);
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

    private boolean land(String[] input) throws RemoteException {
        if (input[0].equals("earlyland")&&!me.isLanded()){
            virtualController.notifyEarlyLanding(this.name);
            return true;
        }
        return false;
    }

    private int numerate(String[] input) throws RemoteException {
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

    public boolean rollBackState() throws RemoteException {
        view.setClientState(previousState);
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
        return name;
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

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}

