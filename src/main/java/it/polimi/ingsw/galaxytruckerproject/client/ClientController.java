package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.ControllerFactory;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightFlightboard;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.Penalty;
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

/**
 * This class is responsible for managing the client-side logic of the game.
 * It handles user input, game state transitions, communication with the server,
 * and various game actions such as creating or joining a game, selecting a color,
 * managing cabins, handling goods, and more.
 * <p>
 * It works in conjunction with the `VirtualController` to facilitate communication with the server.
 * </p>
 */
public class ClientController {
    /**
     * A constant boolean flag indicating if a process, task, or state
     * has ended or completed. The default value is set to {@code false},
     * representing that the process is not concluded.
     */
    private boolean ended=false;

    /**
     * Represents the current state of the client (e.g., login, waiting for server, etc.).
     */
    private ClientState state;

    /**
     * Represents the previous state of the client before the current state.
     */
    private ClientState previousState;

    /**
     * The player associated with this client.
     */
    private LightPlayer me;

    /**
     * The flight board displaying the positions of all players in the game.
     */
    private LightFlightboard flightBoard;

    /**
     * A flag indicating whether the client is in the cabin manager phase.
     */
    private boolean inManager;

    /**
     * A flag indicating whether the client is connected to the server.
     */
    private boolean connected;

    /**
     * The current phase of the game (e.g., ship creation, drawing cards, etc.).
     */
    private GamePhases phase;

    /**
     * A list containing information about available games.
     */
    private ArrayList<GameInfo> gameInfo;

    /**
     * A string to store the name of the player being checked for validity.
     */
    private String checking;

    /**
     * A flag indicating whether the player has been positioned on the flight board.
     */
    private boolean positioned;

    /**
     * The game mode (e.g., Trial or Level 2).
     */
    private GameMode gameMode;

    /**
     * The view used to interact with the player (either TUI or GUI).
     */
    private DisplayableView view;

    /**
     * The virtual controller that facilitates communication with the server.
     */
    private VirtualController virtualController;

    /**
     * The coordinate input manager, used during the coordinate request phase.
     */
    private CoordInputManager coordInputManager;

    /**
     * The goods manager, responsible for managing the player's goods during the game.
     */
    private GoodsManager goodsManager;

    /**
     * The cabins manager, responsible for managing the player's crew cabins.
     */
    private CabinsManager cabinsManager;

    /**
     * A map of turned tiles, which are tiles that have been drawn or turned during gameplay.
     */
    private Map<Integer, Tile> turnedTiles;

    /**
     * A list of turned tiles for display purposes.
     */
    private ArrayList<Tile> turnedTilesDisplayer;

    /**
     * A map representing the card deck, categorized by deck type.
     */
    private Map<Integer, ArrayList<Card>> deck;

    /**
     * A map indicating the availability of each deck category (1, 2, 3).
     */
    private Map<Integer, Boolean> availableDeck;

    /**
     * A map tracking the availability of player colors for the game.
     */
    private Map<PlayersColor, Boolean> availableColors;

    /**
     * A list of goods available for the player to manage.
     */
    private ArrayList<Goods> goodsList;

    /**
     * The input parser used for handling textual input from the user.
     */
    private TextualInputParser inputParser;

    /**
     * The number of players currently in the game.
     */
    private int numPlayer;

    /**
     * The tile currently in the player's hand.
     */
    private Tile tileInHand;

    /**
     * A list of cards currently displayed to the player.
     */
    private ArrayList<Card> displayedCard;

    /**
     * The index for the deck being displayed or chosen from.
     */
    private int indexDeckInHandOrPlanet;

    /**
     * The number of hourglass turns remaining in the current phase.
     */
    private int hourglassTurns;

    /**
     * A thread responsible for monitoring the server connection status.
     */
    private Thread serverWatchdogThread;

    /**
     * a flag to use the buildShip comand only one time
     */
    private boolean built=false;
    private String ipR=null;
    private String ipS=null;

    /**
     * Default constructor, initializing the client controller with default values.
     */
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

    /**
     * Returns the virtual controller for server communication.
     *
     * @return the virtual controller
     */
    public VirtualController getVirtualController() {
        return virtualController;
    }

    /**
     * Set the virtual controller for server communication.
     *
     * @return the virtual controller
     */
    public void setVirtualController(VirtualController virtualController) {
        this.virtualController=virtualController;
    }
    /**
     * Handles the user's choice of user interface (GUI or TUI).
     *
     * @param input the user's input for UI selection
     */
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
    /**
     * Processes user input and passes it to the textual input parser for handling.
     *
     * @param input the user input to be processed
     * @return true if the input was successfully processed, false otherwise
     */
    public boolean input(String input) {
        return inputParser.input(input);
    }

    /**
     * Finalizes the player's name and attempts to log in to the game server.
     *
     * @return true if the login is successful, false otherwise
     */
    public boolean doneNaming() {
        if(state!=ClientState.LOGIN){
            return false;
        }
        if (!Objects.equals(me.getPlayerName(), "")) {
            setState(ClientState.WAIT);
            try {
                virtualController.login(me.getPlayerName());
            } catch (RemoteException e) {
                setOffline();
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }
    /**
     * Creates a new game with the specified name, number of players, and game mode.
     *
     * @param gameName the name of the new game
     * @param numberOfPlayers the number of players in the new game
     * @param mode the game mode (e.g., Trial, Level 2)
     * @return true if the game creation is successful, false otherwise
     */
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
            setOffline();
        }
        return true;
    }
    /**
     * Joins an existing game by its name.
     *
     * @param gameName the name of the game to join
     * @return true if the join is successful, false otherwise
     */
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
                    setOffline();
                }
                return true;
            }
        }
        view.wrongLocalInput();
        setState(ClientState.LOBBY);
        return false;
    }
    /**
     * Leaves the current game.
     *
     * @return true if the leave operation is successful, false otherwise
     */
    public boolean leaveGame() {
        if (gameMode != null) {
            try {
                readyToPlay();
                me.setPlayerName("");
                this.connected = false;
                virtualController.leaveGame();
                this.previousState = ClientState.CHOOSE_UI;
                return true;
            } catch (RemoteException e) {
                setOffline();
            }
        } else {
            view.wrongLocalInput();
            rollBackState();
        }
        return false;
    }

    public void readyToPlay() {
            this.ended=false;
            this.phase = GamePhases.LOGIN;
            positioned=false;
            this.checking= null;
            this.numPlayer = 0;
            this.gameInfo = new ArrayList<>();
            this.deck = new HashMap<>();
            this.me = new LightPlayer("", null);
            this.goodsList = new ArrayList<>();
            this.flightBoard = new LightFlightboard(new FlightBoard(null));
            this.inManager = false;
            this.turnedTiles = new HashMap<>();
            this.turnedTilesDisplayer=new ArrayList<>();
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
    /**
     * Allows the player to choose their color for the game.
     *
     * @param color the color the player wants to choose
     * @return true if the color choice is successful, false otherwise
     */
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
                setOffline();
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }
    /**
     * Draws a card from the deck for the player.
     *
     * @param chose the index of the deck to draw from
     * @return true if the draw operation is successful, false otherwise
     */
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
                    setOffline();
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
    /**
     * Draws a tile from the stack for the player.
     *
     * @return true if the draw operation is successful, false otherwise
     */
    public boolean drawTile() {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.reqDrawTileFromStack();
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Draws a booked tile from the player's list of booked tiles.
     *
     * @param index the index of the booked tile to draw
     * @return true if the draw operation is successful, false otherwise
     */
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
                    setOffline();
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
    /**
     * Draws a drawn tile from the list of turned tiles.
     *
     * @param chose the index of the turned tile to draw
     * @return true if the draw operation is successful, false otherwise
     */
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
    /**
     * Finalizes the shipboard construction and notifies the server.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean doneShipboard() {
        if(state!=ClientState.S_END_DRAW_TILE_CARD){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.notifyCompleted();
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Stops looking at the cards and proceeds to the next phase of the game.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean stopLookingAtCards() {
        if(state!=ClientState.S_MANAGE_CARDS){
            return false;
        }
        setState(ClientState.S_END_DRAW_TILE_CARD);
        try {
            virtualController.stopLookingAtCardsRequest();
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Rotates the tile currently in the player's hand.
     *
     * @return true if the rotation is successful, false otherwise
     */
    public boolean rotateTile() {
        if(state!=ClientState.S_MANAGE_DRAWN_TILE){
            return false;
        }
        this.tileInHand.rotate();
        try {
            view.showDrawnTile(tileInHand);
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Positions the tile at the specified coordinates on the shipboard.
     *
     * @param coordinates the coordinates where the tile should be placed
     * @return true if the operation is successful, false otherwise
     */
    public boolean positionTile(Coordinates coordinates) {
        if(state!=ClientState.S_MANAGE_DRAWN_TILE){
            return false;
        }
        if (coordinates != null && me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand), coordinates)) {
            setState(ClientState.WAIT);
            try {
                virtualController.notifySetTile(this.tileInHand.send());
            } catch (RemoteException e) {
                setOffline();
            }
            built=true;
            return true;
        } else {
            view.wrongLocalInput();
            return false;
        }
    }
    /**
     * Refuses the tile currently in the player's hand.
     *
     * @return true if the operation is successful, false otherwise
     */
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
            setOffline();
        }
        return true;
    }

    /**
     * Books the tile currently in the player's hand.
     *
     * @return true if the operation is successful, false otherwise
     */
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
                setOffline();
            }
        }
        return true;
    }
    /**
     * Positions the player on the flight board at the specified position.
     *
     * @param chose the position on the flight board
     * @return true if the operation is successful, false otherwise
     */
    public boolean positionOnFlightBoard(int chose) {
        if(state!=ClientState.S_FINISHED||positioned){
            return false;
        }
        if (gameMode == GameMode.LEVEL2) {
            if (chose > 0 && chose <= numPlayer) {
                me.getShipBoard().setGetStat();
                try {
                    positioned=true;
                    virtualController.notifySetPosition(chose);
                } catch (RemoteException e) {
                    setOffline();
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
    /**
     * Rolls the dice for the player.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean rollDice() {
        if(state!=ClientState.ROLL_DICE){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.rollTheDices();
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Draws a card for the player.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean drawCard() {
        if(state!=ClientState.DRAW_CARD){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.drawCards();
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Responds "yes" to the current action prompt.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean sayYes() {
        if(state!=ClientState.ACTION){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.sendYes();
        } catch (RemoteException e) {
            setOffline();
        }
        return false;
    }
    /**
     * Responds "no" to the current action prompt.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean sayNo() {
        if(state!=ClientState.ACTION){
            return false;
        }
        setState(ClientState.WAIT);
        try {
            virtualController.sendNo();
        } catch (RemoteException e) {
            setOffline();
        }
        return true;
    }
    /**
     * Chooses a planet for landing.
     *
     * @param chose the index of the planet to land on
     * @return true if the operation is successful, false otherwise
     */
    public boolean choosePlanet(int chose) {
        if(state!=ClientState.PLANET_CHOICE){
            return false;
        }
        Card planet = displayedCard.getFirst();
        if (chose >= 0 && chose <= planet.getListOfPlanets().size()) {
            indexDeckInHandOrPlanet = chose;
            displayedCard.getFirst().setGoodsList(me.getPlayerName(),chose);
            setState(ClientState.WAIT);
            try {
                virtualController.planetChoiceRequest(chose);
            } catch (RemoteException e) {
                setOffline();
            }
        } else {
            view.wrongLocalInput();
            return false;
        }
        return true;
    }

    /**
     * Completes the goods management phase and notifies the server.
     *
     * @return true if the operation is successful, false otherwise
     */
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
                    setOffline();
                }
                inManager = false;
            }
        }
        return true;
    }
    /**
     * Chooses a good for management during the goods phase.
     *
     * @param chose the index of the good to choose
     * @return true if the operation is successful, false otherwise
     */
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
    /**
     * Chooses cargo for the player to manage.
     *
     * @param coords the coordinates of the cargo to choose
     * @return true if the operation is successful, false otherwise
     */
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
    /**
     * Sets up the cabins for the player during the cabin management phase.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean setUpCabins() {
        if(state!=ClientState.MANAGE_CABINS){
            return false;
        }
        cabinsManager.setup();
        return true;
    }
    /**
     * Manages the cabins for the player by assigning crew members to cabins.
     *
     * @param type the crew type to assign (e.g., human, purple alien, brown alien)
     * @return true if the operation is successful, false otherwise
     */
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
                        setOffline();
                    }
                }
            }
        }
        return true;
    }
    /**
     * Completes the coordinate request phase and sends the coordinates to the server.
     *
     * @return true if the operation is successful, false otherwise
     */
    public boolean doneCoord() {
        if(state!=ClientState.COORD_REQUEST) {
            return false;
        }
        view.sendingCoordinates();
        return coordInputManager.endCheckingFase();
    }
    /**
     * Checks if the specified coordinates are valid for the current action.
     *
     * @param coords the coordinates to check
     * @return true if the coordinates are valid, false otherwise
     */
    public boolean checkCoord(Coordinates coords) {
        if(state!=ClientState.COORD_REQUEST) {
            return false;
        }
        return coordInputManager.checkCoord(coords);
    }

    //------------------------------------------------------------------------------------------------------------------------------

    /**
     * Sets the current state of the client and performs actions depending on the state transition.
     * This method handles state transitions and updates the game phase.
     *
     * @param newState the new state to transition to
     */
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
            case COORD_REQUEST ->{
                    this.coordInputManager = new CoordInputManager(me.getShipBoard(), this);
                    view.printShipboard(me.getShipBoard());
            }

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
                            setOffline();
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
                        setOffline();
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
                        setOffline();
                    }
                }
            }
            case ROLL_DICE -> {
                view.printProjectile(displayedCard.getFirst().getListOfProjectiles().getFirst());
                displayedCard.getFirst().getListOfProjectiles().removeFirst();
            }
            case WAIT_TO_DRAW ->
                    phase = GamePhases.CARDS;
            case DRAW_CARD -> phase = GamePhases.CARDS;
            case MANAGE_GOODS -> {
                if (!inManager) {
                    me.getShipBoard().setGetStat();
                    this.goodsList =displayedCard.getFirst().getGoodsList(me.getPlayerName());
                    goodsManager = new GoodsManager(me, goodsList, view);
                    inManager = true;
                }
                view.goodsPrinter(goodsList);
                view.showGenericMessage("Chose for each good where to put it, input 'no' to stop:\n");
            }
            case RECONNECTING ->
                previousState=state;
        }
        try {
            view.setClientState(state);
        } catch (RemoteException e) {
            setOffline();
        }
    }
    /**
     * Transforms the input coordinates into a `Coordinates` object.
     * The coordinates should be within the valid bounds (x: 0-4, y: 0-6).
     *
     * @param input the input array containing the x and y coordinates
     * @return a `Coordinates` object if the input is valid, null otherwise
     */
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
    /**
     * Checks the validity of the player's shipboard based on the input.
     * Allows the player to check another player's shipboard.
     *
     * @param input the user's input containing the player's name to check
     * @return true if the check is successful, false otherwise
     */
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
    /**
     * Displays the shipboard of the selected player for review.
     *
     * @param chose the index of the player whose shipboard is to be checked
     * @return true if the check is successful, false otherwise
     */
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

    public String orderedPlayer(int chose){
        ArrayList<LightPlayer>players=new ArrayList<>();
        for(LightPlayer player:flightBoard.getInGamePlayers()){
            if(Objects.equals(player.getPlayerName(), getMe().getPlayerName()))
                continue;
            players.add(player);
        }
        players.sort((p1, p2) -> Integer.compare(p2.getPlayerColor().toInt(), p1.getPlayerColor().toInt()));
        if (chose >= 0 && chose < flightBoard.getInGamePlayers().size()) {
            return players.get(chose-1).getPlayerName();
        }else{
            return new String();
        }
    }
    /**
     * Returns the name of the player being checked, if any.
     *
     * @return the name of the player being checked, or null if no player is being checked
     */
    public String isChecking(){
        return checking;
    }
    /**
     * Sets the name of the player to be checked.
     *
     * @param checking the name of the player to be checked
     */
    public void setChecking(String checking) {
        this.checking = checking;
    }
    /**
     * Handles the first hourglass turn in the game. It transitions to the next phase and notifies the server.
     *
     * @return true if the turn is successful, false otherwise
     */
    public boolean firstHourglassTurn() {
        if(hourglassTurns == 0) {
            if (gameMode == GameMode.LEVEL2) {
                hourglassTurns = 1;
            }
            setState(ClientState.WAIT);
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                setOffline();
            }
            return true;
        } else {
            view.wrongLocalInput();
            return false;
        }
    }
    /**
     * Handles the second hourglass turn in the game. It transitions to the next phase and notifies the server.
     *
     * @return true if the turn is successful, false otherwise
     */
    public boolean secondHourglassTurn() {
        if (hourglassTurns == 1) {
            if (gameMode == GameMode.TRIAL) {
                view.wrongLocalInput();
                return false;
            }
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                setOffline();
            }
            return true;
        }
        return false;
    }
    /**
     * Handles the third hourglass turn in the game. It transitions to the next phase and notifies the server.
     *
     * @return true if the turn is successful, false otherwise
     */
    public boolean thirdHourglassTurn() {
        if (hourglassTurns >= 1 && hourglassTurns < 3) {
            if (gameMode == GameMode.TRIAL) {
                view.wrongLocalInput();
                return false;
            }
            try {
                virtualController.sendTurnHourGlass();
            } catch (RemoteException e) {
                setOffline();
            }
            return true;
        }
        return false;
    }
    /**
     * Finds a player by their name from the flight board.
     *
     * @param playerName the name of the player to find
     * @return the `LightPlayer` object if found, or null if not found
     */
    private LightPlayer playerFinder(String playerName) {
        LightPlayer searchedPlayer = null;
        for (LightPlayer player : flightBoard.getInGamePlayers())
            if (player.getPlayerName().equals(playerName)) {
                searchedPlayer = player;
            }
        return searchedPlayer;
    }
    /**
     * Sets the game mode for the client and initializes the player's shipboard accordingly.
     *
     * @param gameMode the game mode to set (e.g., LEVEL2, TRIAL)
     */
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
    /**
     * Sets the game mode without initializing the shipboard (used for non-level2 and non-trial phases).
     *
     * @param gameMode the game mode to set
     */
    public void setGameModeWithoutInitializing(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Attempts to land the current instance if it is not already landed.
     * If the instance is not landed, it triggers a notification for early landing.
     *
     * @return true if the landing process was successfully initiated; false if already landed
     */
    public boolean land() {
        if (!me.isLanded()) {
            try {
                virtualController.notifyEarlyLanding();
            } catch (RemoteException e) {
                setOffline();
            }
            return true;
        }
        return false;
    }
    /**
     * Converts the user's input into an integer. If the input is invalid, it returns -1.
     *
     * @param input the user's input to be converted
     * @return the converted integer if valid, -1 otherwise
     */
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
    /**
     * Scrolls the input array by a given number of times.
     *
     * @param input the array of input to be scrolled
     * @param times the number of times to scroll the input
     * @return the scrolled input array
     */
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
    /**
     * Rolls back the client to the previous state if the current state is different from the previous one.
     */
    public void rollBackState() {
        if (state == previousState)
            return;
        setState(previousState);
    }
    /**
     * Adds a new player to the flight board, setting their position and rank.
     *
     * @param name the name of the player to add
     * @param color the color of the player's spaceship
     * @param pos the position of the player on the flight board
     * @param ranking the rank of the player
     */
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
    /**
     * Updates the flight board with the new position and ranking of a player.
     *
     * @param name the name of the player to update
     * @param color the color of the player's spaceship
     * @param pos the new position of the player
     * @param ranking the new ranking of the player
     */
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
    /**
     * Returns the current flight board.
     *
     * @return the flight board
     */
    public LightFlightboard getFlightBoard() {
        return flightBoard;
    }
    /**
     * Adds a turned tile to the collection of turned tiles and updates the view.
     *
     * @param tile the tile to add to the collection of turned tiles
     */
    public void addTurnedTile(Tile tile) {
        turnedTiles.put(tile.getKey(), tile);
        turnedTilesDisplayer.add(tile);
        view.showTurnedTiles(turnedTiles);
    }
    /**
     * Removes a turned tile from the collection of turned tiles and updates the view.
     *
     * @param tile the tile to remove from the collection of turned tiles
     */
    public void removeTurnedTile(Tile tile) {
        turnedTiles.remove(tile.getKey());
        turnedTilesDisplayer.removeIf(t -> t.getKey() == tile.getKey());
        view.showTurnedTiles(turnedTiles);
    }
    /**
     * Returns the name of the player associated with the client.
     *
     * @return the player's name
     */
    public String getName() {
        return me.getPlayerName();
    }
    /**
     * Sets the tile currently in the player's hand.
     *
     * @param tile the tile to set in the player's hand
     */
    public void setTileInHand(Tile tile) {
        tileInHand = tile;
    }
    /**
     * Sets the tile for a player at a given position on the shipboard.
     *
     * @param playerName the name of the player whose shipboard is being updated
     * @param tile the tile to place on the player's shipboard
     */
    public void setTile(String playerName, Tile tile) {
        if(playerName.equals(me.getPlayerName())) {
            built=true;
        }
        LightShipBoard lightShipBoard = flightBoard.getInGamePlayer(playerName).getShipBoard();
        lightShipBoard.positionTile(Optional.of(tile), tile.getCoordinates());
        if(isChecking()!=null&& Objects.equals(isChecking(), playerName)){
            LightPlayer player = playerFinder(playerName);
            view.checkShipboard(player.getShipBoard());
        }
    }
    /**
     * Returns the coordinate input manager responsible for managing coordinate-related actions.
     *
     * @return the coordinate input manager
     */
    public CoordInputManager getCoordInputManager() {
        return coordInputManager;
    }
    /**
     * Returns the shipboard associated with the player.
     *
     * @return the player's shipboard
     */
    public LightShipBoard getLightShipBoard() {
        return me.getShipBoard();
    }

    /**
     * Sets the displayed card for the player.
     *
     * @param displayedCard the card to be displayed
     */
    public void setDisplayedCard(Card displayedCard) {
        this.displayedCard.clear();
        this.displayedCard.add(displayedCard);
    }
    /**
     * Returns the list of displayed cards for the player.
     *
     * @return the list of displayed cards
     */
    public ArrayList<Card> getDisplayedCard() {
        return displayedCard;
    }

    /**
     * Modifies the tiles for a specific player by setting new tiles at the player's shipboard.
     *
     * @param playerName the name of the player whose tiles are being modified
     * @param tiles the list of tiles to modify for the player
     */
    public void modifyTiles(String playerName, ArrayList<Tile> tiles) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        for (Tile modTiles : tiles) {
            player.getShipBoard().swapTile(Optional.of(modTiles), modTiles.getCoordinates());
        }
    }

    /**
     * Breaks the tiles at the specified coordinates for a particular player.
     *
     * @param playerName the name of the player whose tiles are being broken
     * @param coordinates the coordinates of the tiles to be broken
     */
    public void brokenTiles(String playerName, ArrayList<Coordinates> coordinates) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        player.getShipBoard().destroy(coordinates);
    }

    /**
     * Adds a booked tile to the player's shipboard.
     *
     * @param playerName the name of the player whose shipboard will receive the booked tile
     * @param tile the tile to add to the player's shipboard
     */
    public void addBookedTile(String playerName, Tile tile) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        player.getShipBoard().addBookedTile(tile);
    }

    /**
     * Removes a booked tile from the player's shipboard.
     *
     * @param playerName the name of the player whose shipboard will have the booked tile removed
     * @param tile the tile to remove from the player's shipboard
     */
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
    /**
     * Increases the player's credit by the specified amount.
     *
     * @param playerName the name of the player whose credits are being increased
     * @param credits the number of credits to add to the player's total
     */
    public void gainCredit(String playerName, int credits) {
        LightPlayer player = playerFinder(playerName);
        if (player == null)
            return;
        player.gainCredits(credits);
    }
    /**
     * Updates the game model based on the serialized game status received from the server.
     *
     * @param currentGameStatus the serialized game status to unpack
     */
    public void updateModel(String playerName, String currentGameStatus) {
        UpdateDeserializer.unpack(this,playerName,currentGameStatus);
    }
    /**
     * Updates the availability of the decks based on the given list of unavailable decks.
     *
     * @param notAvailableDecks the list of unavailable deck indices
     */
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
    /**
     * Returns the view used to interact with the player.
     *
     * @return the displayable view (either TUI or GUI)
     */
    public DisplayableView getView() {
        return view;
    }
    /**
     * Sets the deck with the specified cards.
     *
     * @param deck the new deck to set
     */
    public void setDeck(Map<Integer, ArrayList<Card>> deck) {
        this.deck = deck;
    }
    /**
     * Sets the game information (such as available games) for the client.
     *
     * @param gameInfo the list of game information
     */
    public void setGameInfo(ArrayList<GameInfo> gameInfo) {
        this.gameInfo = gameInfo;
    }
    /**
     * Updates the availability of the colors based on the given list of unavailable colors.
     *
     * @param notAvailableColors the list of unavailable colors
     */
    public void colorsNotAvailable(ArrayList<PlayersColor> notAvailableColors) {
        for(PlayersColor color:PlayersColor.values()){
            availableColors.put(color,Boolean.TRUE);
        }
        for(PlayersColor color:notAvailableColors){
            this.availableColors.put(color, Boolean.FALSE);
        }
    }

    //Test getter
    /**
     * Retrieves the current state of the client.
     *
     * @return the current state of the client
     */
    public ClientState getState() {
        return state;
    }

    /**
     * Retrieves the `LightPlayer` object associated with the client.
     *
     * @return the `LightPlayer` object of the client
     */
    public LightPlayer getMe() {
        return me;
    }

    /**
     * Retrieves the current game phase.
     *
     * @return the current game phase
     */
    public GamePhases getPhase() {
        return phase;
    }

    /**
     * Retrieves the current game mode.
     *
     * @return the current game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Retrieves the map of turned tiles.
     *
     * @return a map of turned tiles, where keys are tile keys and values are tiles
     */
    public Map<Integer, Tile> getTurnedTiles() {
        return turnedTiles;
    }

    /**
     * Retrieves the list of turned tiles to be displayed.
     *
     * @return a list of turned tiles to be displayed
     */
    public ArrayList<Tile> getTurnedTilesDisplayer() {
        return turnedTilesDisplayer;
    }

    /**
     * Retrieves the deck of cards.
     *
     * @return a map representing the deck, where keys are deck numbers and values are lists of cards
     */
    public Map<Integer, ArrayList<Card>> getDeck() {
        return deck;
    }

    /**
     * Retrieves the list of goods available to the client.
     *
     * @return a list of goods available to the client
     */
    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    /**
     * Retrieves the tile currently held by the client.
     *
     * @return the tile currently held by the client
     */
    public Tile getTileInHand() {
        return tileInHand;
    }

    /**
     * Retrieves the index of the deck (or planet) the client is currently interacting with.
     *
     * @return the index of the current deck or planet
     */
    public int getIndexDeckInHandOrPlanet() {
        return indexDeckInHandOrPlanet;
    }

    /**
     * Retrieves the current number of hourglass turns the client has taken.
     *
     * @return the number of hourglass turns
     */
    public int getHourglassTurns() {
        return hourglassTurns;
    }

    /**
     * Sets the connection status of the client.
     *
     * @param connected the new connection status of the client
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Connects to the server using the RMI protocol.
     *
     * @param ip the IP address of the server
     * @param port the port number of the server
     * @return true if the connection is successful, false otherwise
     * @throws MalformedURLException if the RMI URL is malformed
     * @throws NotBoundException if the RMI binding cannot be found
     * @throws RemoteException if a remote exception occurs during connection
     */
    public boolean connectRMI(String ip, int port) throws MalformedURLException, NotBoundException, RemoteException {
        if(state != ClientState.CHOOSE_CONNECTION_TYPE && state != ClientState.CHOOSE_IP_AND_PORT_RMI&& state != ClientState.RECONNECTING){
            return false;
        }
        VirtualViewRMI viewRMI = new VirtualViewRMI(this, view);
        if(ip == null || ip.isEmpty())
            ip="localhost";
        if (port == 0)
            port=1099;
        this.ipR=ip;
        String url = String.format("rmi://%s:%d/ControllerFactory", ip, port);
        ControllerFactory controllerFactory = (ControllerFactory) Naming.lookup(url);
        this.virtualController = controllerFactory.createController();
        virtualController.setView(viewRMI);
        setConnected(true);
        setState(ClientState.LOGIN);
        return true;
    }

    /**
     * Connects to the server using the socket protocol.
     *
     * @param ip the IP address of the server
     * @param port the port number of the server
     * @return true if the connection is successful, false otherwise
     * @throws IOException if an I/O exception occurs during connection
     * @throws NotBoundException if the socket cannot be bound
     */
    public boolean connectSocket(String ip, int port) throws IOException, NotBoundException {
        if(state != ClientState.CHOOSE_CONNECTION_TYPE && state != ClientState.CHOOSE_IP_AND_PORT_SOCKET&& state != ClientState.RECONNECTING){
            return false;
        }
        if(ip == null || ip.isEmpty())
            ip="localhost";
        if (port == 0)
            port=12345;
        this.ipS=ip;
        Socket server;
        try {
            server = new Socket(ip, port);
        } catch (Exception e) {
            view.showGenericMessage("Server offline");
            setConnected(false);
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
                System.out.println("Waiting for view and virtual controller setup in serverHandler");
            }
        }
        serverHandler.setVirtualController(virtualController);
        serverHandler.setView(view);
        serverHandler.setClientController(this);
        setConnected(true);
        setState(ClientState.LOGIN);
        return true;
    }

    /**
     * Pings the server to check if the connection is still alive.
     */
    public void ping() {
        try {
            virtualController.ping();  // Pings the server
            restartServerWatchdog();  // Restarts the watchdog timer
        } catch (RemoteException e) {
            setOffline();
        }
    }

    public void setOffline(){
        readyToPlay();
        connected=false;
        this.setState(ClientState.RECONNECTING);
    }

    /**
     * Restarts the server watchdog timer. If the server does not respond within a certain time frame,
     * the client will assume the server is down and transition to the "reconnecting" state.
     */
    private void restartServerWatchdog() {
        if (serverWatchdogThread != null && serverWatchdogThread.isAlive()) {
            serverWatchdogThread.interrupt();
        }

        serverWatchdogThread = new Thread(() -> {
            try {
                Thread.sleep(20000);
                setOffline();
            } catch (InterruptedException ignored) {
            }
        });
        serverWatchdogThread.start();
    }

    /**
     * Sets the hourglass turn count to the specified value.
     *
     * @param i the number of hourglass turns
     */
    public void turnHourglass(int i) {
        hourglassTurns = i;
    }

    /**
     * Sets the view of the client to the GUI.
     *
     * @param gui the GUI instance to set
     */
    public void setGUI(GUI gui) {
        this.view = gui;
        GUI.startGui(this);
    }

    /**
     * Sets the view of the client to the TUI (Textual User Interface).
     *
     * @param tui the TUI instance to set
     */
    public void setTUI(TUI tui) {
        this.view = tui;
        inputParser = new TextualInputParser(this);
        setState(ClientState.CHOOSE_CONNECTION_TYPE);
    }

    /**
     * Retrieves the list of available game information.
     *
     * @return the list of available game information
     */
    public ArrayList<GameInfo> getGameInfo() {
        return gameInfo;
    }

    /**
     * Retrieves the map of available player colors.
     *
     * @return a map of available player colors
     */
    public Map<PlayersColor, Boolean> getAvailableColors() {
        return availableColors;
    }

    /**
     * Retrieves the previous state of the client.
     *
     * @return the previous state of the client
     */
    public ClientState getPreviousState() {
        return previousState;
    }

    /**
     * Sets the number of hourglass turns for the client.
     *
     * @param hourglassTurns the number of hourglass turns
     */
    public void setHourglassTurns(int hourglassTurns) {
        this.hourglassTurns = hourglassTurns;
    }

    /**
     * Sets the turned tiles for the client.
     *
     * @param turnedTiles the map of turned tiles
     */
    public void setTurnedTiles(Map<Integer, Tile> turnedTiles) {
        this.turnedTiles = turnedTiles;
    }

    /**
     * Puts the client in a standby state.
     */
    public void putInStandby() {
        this.state = ClientState.WAIT;
    }

    /**
     * Notifies the view that the player is the victim of a penalty.
     *
     * @param playerName the name of the player who is the victim of the penalty
     */
    public void victimOfThePenalty(String playerName) {
        Penalty currentPenalty = this.displayedCard.getFirst().getPenalty();
        if (currentPenalty != null) {
            view.victimOfThePenalty(playerName, currentPenalty);
        }
    }

    /**
     * Retrieves the map of available decks.
     *
     * @return the map of available decks
     */
    public Map<Integer, Boolean> getAvailableDeck() {
        return availableDeck;
    }

    /**
     * Retrieves the available positions on the flight board.
     *
     * @return a map of available positions
     */
    public Map<Integer, PlayersColor> getAvailablePosition() {
        Map<Integer, PlayersColor> availablePosition = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            if (i <= flightBoard.getInGamePlayers().size())
                availablePosition.put(i, null);
        }
        if (getGameMode() == GameMode.LEVEL2) {
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
        } else if (getGameMode() == GameMode.TRIAL) {
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

    /**
     * Checks if the client has positioned on the flight board.
     *
     * @return true if the client is positioned, false otherwise
     */
    public boolean isPositioned() {
        return positioned;
    }

    /**
     * Sets the client's position status on the flight board.
     *
     * @param positioned the position status to set
     */
    public void setPositioned(boolean positioned) {
        this.positioned = positioned;
    }

    /**
     * Retrieves the cabins manager responsible for managing the crew and cabins.
     *
     * @return the cabins manager
     */
    public CabinsManager getCabinsManager() {
        return cabinsManager;
    }

    /**
     * Retrieves the instance of {@code GoodsManager}.
     *
     * @return the {@code GoodsManager} object associated with this class
     */
    public GoodsManager getGoodsManager() {
        return goodsManager;
    }

    /**
     * Automatically builds the player's ship if it has not been built yet.
     * <p>
     * If the {@code built} flag is {@code false}, the method checks the current game mode:
     * <ul>
     *   <li>If the mode is {@link GameMode#TRIAL}, it calls {@code buildShipTrial()}.</li>
     *   <li>Otherwise, it calls {@code buildShiplv2()}.</li>
     * </ul>
     * After building the ship, it sets {@code built} to {@code true}.
     * <p>
     * If the ship has already been built ({@code built} is {@code true}), the method notifies
     * the user of an invalid local input by calling {@code view.wrongLocalInput()}.
     */
    public void buildShip(){
        if(!built) {
            if (getGameMode() == GameMode.TRIAL) {
                buildShipTrial();
            } else {
                buildShiplv2();
            }
            built = true;
        }
        if (built){
            view.wrongLocalInput();
        }
    }

    /**
     * Automatically builds the player's ship if it has not been built yet.
     * <p>
     * If the {@code built} flag is {@code false}, the method checks the current game mode:
     * <ul>
     *   <li>If the mode is {@link GameMode#TRIAL}, it calls {@code buildShipTrial()}.</li>
     *   <li>Otherwise, it calls {@code buildShiplv2()}.</li>
     * </ul>
     * After building the ship, it sets {@code built} to {@code true}.
     * <p>
     * If the ship has already been built ({@code built} is {@code true}), the method notifies
     * the user of an invalid local input by calling {@code view.wrongLocalInput()}.
     */
    public void buildShip2(){
        if(!built) {
            if (getGameMode() == GameMode.TRIAL) {
                buildShipTrial();
            } else {
                buildShiplv2_2();
            }
            built = true;
        }
        if (built){
            view.wrongLocalInput();
        }
    }

    /**
     * Automatically builds a predefined ship layout for the player in LEVEL2 game mode.
     * <p>
     * This method places a specific set of tiles (e.g., cabins, engines, cannons, batteries, etc.)
     * at fixed coordinates on the player's {@code ShipBoard}, forming a complete and functional ship design.
     * Each tile is instantiated with its own connectors, image path, and metadata, and is placed using
     * {@code positionTile}. After placing each tile, the {@code virtualController} is notified via
     * {@code notifySetTile} to update the remote view or game state.
     * <p>
     * If a {@code RemoteException} occurs during the notification phase, it is rethrown as a {@code RuntimeException}.
     * <p>
     * This method is intended to be called only in {@link GameMode#LEVEL2}.
     */
    public void buildShiplv2(){
        /*
        me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand), coordinates)
        try {
                virtualController.notifySetTile(tile.send());
            } catch (RemoteException e) {
                                setOffline();
            }
         */
        Tile tile4=new CargoBlue(2, new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web26.jpg",2,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile4), new Coordinates(1,3));
        try {
            virtualController.notifySetTile(tile4.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web156.jpg",0,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile10), new Coordinates(2,4));
        try {
            virtualController.notifySetTile(tile10.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web46.jpg", 0,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile12), new Coordinates(2,5));
        try {
            virtualController.notifySetTile(tile12.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web136.jpg",0,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile11), new Coordinates(2,6));
        try {
            virtualController.notifySetTile(tile11.send());
        } catch (RemoteException e) {
            setOffline();
        }


        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web51.jpg",1,0);
        me.getShipBoard().positionTile(Optional.of(tile9), new Coordinates(2,2));
        try {
            virtualController.notifySetTile(tile9.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web78.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile15), new Coordinates(3,3));
        try {
            virtualController.notifySetTile(tile15.send());
        } catch (RemoteException e) {
            setOffline();
        }

        Tile tile3=new AlienLifeSupportsSystem( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web140.jpg",0,0,CrewType.BROWN);
        me.getShipBoard().positionTile(Optional.of(tile3), new Coordinates(1,2));
        try {
            virtualController.notifySetTile(tile3.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web49.jpg",3,0);
        me.getShipBoard().positionTile(Optional.of(tile2), new Coordinates(1,1));
        try {
            virtualController.notifySetTile(tile2.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile8=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web42.jpg",3,0);
        me.getShipBoard().positionTile(Optional.of(tile8), new Coordinates(2,1));
        try {
            virtualController.notifySetTile(tile8.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web32.jpg",1,0);
        me.getShipBoard().positionTile(Optional.of(tile5), new Coordinates(1,4));
        try {
            virtualController.notifySetTile(tile5.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web118.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile6), new Coordinates(1,5));
        try {
            virtualController.notifySetTile(tile6.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web128.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile7), new Coordinates(2,0));
        try {
            virtualController.notifySetTile(tile7.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web7.jpg",1,0,2);
        me.getShipBoard().positionTile(Optional.of(tile13), new Coordinates(3,0));
        try {
            virtualController.notifySetTile(tile13.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web47.jpg",3,0);
        me.getShipBoard().positionTile(Optional.of(tile14), new Coordinates(3,2));
        try {
            virtualController.notifySetTile(tile14.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web66.jpg",2,0);
        me.getShipBoard().positionTile(Optional.of(tile16), new Coordinates(3,4));
        try {
            virtualController.notifySetTile(tile16.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web98.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile17), new Coordinates(3,5));
        try {
            virtualController.notifySetTile(tile17.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web8.jpg",1,0,2);
        me.getShipBoard().positionTile(Optional.of(tile18), new Coordinates(3,6));
        try {
            virtualController.notifySetTile(tile18.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web11.jpg",1,0,2);
        me.getShipBoard().positionTile(Optional.of(tile19), new Coordinates(4,0));
        try {
            virtualController.notifySetTile(tile19.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web117.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile20), new Coordinates(4,1));
        try{
            virtualController.notifySetTile(tile20.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web96.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile21), new Coordinates(4,2));
        try {
            virtualController.notifySetTile(tile21.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web120.jpg",0,0);
        tile22.rotate();
        tile22.rotate();
        me.getShipBoard().positionTile(Optional.of(tile22), new Coordinates(4,4));
        try {
            virtualController.notifySetTile(tile22.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web103.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile1), new Coordinates(0,4));
        try {
            virtualController.notifySetTile(tile1.send());
        } catch (RemoteException e) {
            setOffline();
        }
        return;

    }

    /**
     * Automatically builds a predefined ship layout for the player in LEVEL2 game mode.
     * <p>
     * This method places a specific set of tiles (e.g., cabins, engines, cannons, batteries, etc.)
     * at fixed coordinates on the player's {@code ShipBoard}, forming a complete and functional ship design.
     * Each tile is instantiated with its own connectors, image path, and metadata, and is placed using
     * {@code positionTile}. After placing each tile, the {@code virtualController} is notified via
     * {@code notifySetTile} to update the remote view or game state.
     * <p>
     * If a {@code RemoteException} occurs during the notification phase, it is rethrown as a {@code RuntimeException}.
     * <p>
     * This method is intended to be called only in {@link GameMode#LEVEL2}.
     */
    public void buildShiplv2_2(){
        /*
        me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand), coordinates)
        try {
                virtualController.notifySetTile(tile.send());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
         */
        Tile tile4=new CargoBlue(2, new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web26.jpg",2,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile4), new Coordinates(1,3));
        try {
            virtualController.notifySetTile(tile4.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile10=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web156.jpg",0,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile10), new Coordinates(2,4));
        try {
            virtualController.notifySetTile(tile10.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile12=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web46.jpg", 0,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile12), new Coordinates(2,5));
        try {
            virtualController.notifySetTile(tile12.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile11=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web136.jpg",0,0);
        me.getShipBoard().positionTile(Optional.ofNullable(tile11), new Coordinates(2,6));
        try {
            virtualController.notifySetTile(tile11.send());
        } catch (RemoteException e) {
            setOffline();
        }


        Tile tile9=new EquipCabin( new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web51.jpg",1,0);
        me.getShipBoard().positionTile(Optional.of(tile9), new Coordinates(2,2));
        try {
            virtualController.notifySetTile(tile9.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile15=new SingleEngine( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web78.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile15), new Coordinates(3,3));
        try {
            virtualController.notifySetTile(tile15.send());
        } catch (RemoteException e) {
            setOffline();
        }

        Tile tile3=new CargoBlue( 2,new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web18.jpg",0,0);
        tile3.rotate();
        tile3.rotate();
        me.getShipBoard().positionTile(Optional.of(tile3), new Coordinates(1,2));
        try {
            virtualController.notifySetTile(tile3.send());
        } catch (RemoteException e) {
            setOffline();
        }

        Tile tile29=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web114.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile29), new Coordinates(0,2));
        try {
            virtualController.notifySetTile(tile29.send());
        } catch (RemoteException e) {
            setOffline();
        }

        Tile tile2=new EquipCabin( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web49.jpg",3,0);
        me.getShipBoard().positionTile(Optional.of(tile2), new Coordinates(1,1));
        try {
            virtualController.notifySetTile(tile2.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile8=new CargoRed( 1,new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web67.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile8), new Coordinates(2,1));
        try {
            virtualController.notifySetTile(tile8.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile5=new CargoBlue(3, new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web32.jpg",1,0);
        me.getShipBoard().positionTile(Optional.of(tile5), new Coordinates(1,4));
        try {
            virtualController.notifySetTile(tile5.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile6=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web118.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile6), new Coordinates(1,5));
        try {
            virtualController.notifySetTile(tile6.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile7=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web128.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile7), new Coordinates(2,0));
        try {
            virtualController.notifySetTile(tile7.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile13=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web7.jpg",1,0,2);
        me.getShipBoard().positionTile(Optional.of(tile13), new Coordinates(3,0));
        try {
            virtualController.notifySetTile(tile13.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile14=new EquipCabin( new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web47.jpg",3,0);
        me.getShipBoard().positionTile(Optional.of(tile14), new Coordinates(3,2));
        try {
            virtualController.notifySetTile(tile14.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile16=new CargoRed(1, new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web66.jpg",2,0);
        me.getShipBoard().positionTile(Optional.of(tile16), new Coordinates(3,4));
        try {
            virtualController.notifySetTile(tile16.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile17=new DoubleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web98.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile17), new Coordinates(3,5));
        try {
            virtualController.notifySetTile(tile17.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile18=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web8.jpg",1,0,2);
        me.getShipBoard().positionTile(Optional.of(tile18), new Coordinates(3,6));
        try {
            virtualController.notifySetTile(tile18.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile19=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web11.jpg",1,0,2);
        me.getShipBoard().positionTile(Optional.of(tile19), new Coordinates(4,0));
        try {
            virtualController.notifySetTile(tile19.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile20=new SingleCannon(new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web117.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile20), new Coordinates(4,1));
        try{
            virtualController.notifySetTile(tile20.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile21=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web96.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile21), new Coordinates(4,2));
        try {
            virtualController.notifySetTile(tile21.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile22=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web120.jpg",0,0);
        tile22.rotate();
        tile22.rotate();
        me.getShipBoard().positionTile(Optional.of(tile22), new Coordinates(4,4));
        try {
            virtualController.notifySetTile(tile22.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile1=new SingleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web103.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile1), new Coordinates(0,4));
        try {
            virtualController.notifySetTile(tile1.send());
        } catch (RemoteException e) {
            setOffline();
        }
        return;

    }

    /**
     * Automatically builds a predefined ship layout for the player in TRIAL game mode.
     * <p>
     * This method places a fixed and simplified set of tiles (e.g., cabins, engines, cannons,
     * batteries, cargo holds, shields, etc.) at specific coordinates on the player's
     * {@code ShipBoard}. This layout is used as a default configuration for testing or trial purposes.
     * <p>
     * Each tile is created with specified connectors, image references, and any necessary
     * configuration (such as crew type), then placed using {@code positionTile}.
     * After placement, the tile is sent to the {@code virtualController} using {@code notifySetTile}
     * to synchronize the game state remotely.
     * <p>
     * If a {@code RemoteException} occurs during the tile notification, it is caught and rethrown
     * as a {@code RuntimeException}.
     * <p>
     * This method is only intended to be used when the current game mode is {@link GameMode#TRIAL}.
     */
    public void buildShipTrial(){
        /*
        me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand), coordinates)
        try {
                virtualController.notifySetTile(tile.send());
            } catch (RemoteException e) {
                setOffline();
            }
         */
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web126.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile1), new Coordinates(1,3));
        try {
            virtualController.notifySetTile(tile1.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web19.jpg",2,0);
        me.getShipBoard().positionTile(Optional.of(tile2), new Coordinates(2,2));
        try {
            virtualController.notifySetTile(tile2.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web36.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile3), new Coordinates(2,4));
        tile3.setCrewType(CrewType.HUMAN);
        try {
            virtualController.notifySetTile(tile3.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web156.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile4), new Coordinates(2,5));
        try {
            virtualController.notifySetTile(tile4.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web16.jpg",3,0,3);
        me.getShipBoard().positionTile(Optional.of(tile5), new Coordinates(3,2));
        try {
            virtualController.notifySetTile(tile5.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web4.jpg",0,0,2);
        me.getShipBoard().positionTile(Optional.of(tile7), new Coordinates(3,3));
        try {
            virtualController.notifySetTile(tile7.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web96.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile6), new Coordinates(3,4));
        try {
            virtualController.notifySetTile(tile6.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile8= new SingleCannon ( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web102.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile8), new Coordinates(1,2));
        try {
            virtualController.notifySetTile(tile8.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile9=new BatteryComponents( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web11.jpg",2,0,2);
        me.getShipBoard().positionTile(Optional.of(tile9), new Coordinates(2,1));
        try {
            virtualController.notifySetTile(tile9.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile10= new SingleCannon ( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web102.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile10), new Coordinates(1,4));
        try {
            virtualController.notifySetTile(tile10.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile11=new SingleEngine( new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web72.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile11), new Coordinates(3,1));
        try {
            virtualController.notifySetTile(tile11.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile12=new SingleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web82.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile12), new Coordinates(3,5));
        try {
            virtualController.notifySetTile(tile12.send());
        } catch (RemoteException e) {
            setOffline();
        }
        return;
    }

    public void buildShipTrial2(){
        /*
        me.getShipBoard().positionTile(Optional.ofNullable(this.tileInHand), coordinates)
        try {
                virtualController.notifySetTile(tile.send());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
         */
        Tile tile1=new DoubleCannon( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web126.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile1), new Coordinates(1,3));
        try {
            virtualController.notifySetTile(tile1.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile2=new CargoBlue(2, new Link(Connectors.UNIVERSAL),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web19.jpg",2,0);
        me.getShipBoard().positionTile(Optional.of(tile2), new Coordinates(2,2));
        try {
            virtualController.notifySetTile(tile2.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile3=new EquipCabin( new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web36.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile3), new Coordinates(2,4));
        tile3.setCrewType(CrewType.HUMAN);
        try {
            virtualController.notifySetTile(tile3.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile4=new Shields( new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE),new Link(Connectors.DOUBLE),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web156.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile4), new Coordinates(2,5));
        try {
            virtualController.notifySetTile(tile4.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile5=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.DOUBLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web16.jpg",3,0,3);
        me.getShipBoard().positionTile(Optional.of(tile5), new Coordinates(3,2));
        try {
            virtualController.notifySetTile(tile5.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile7=new BatteryComponents( new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE),new Link(Connectors.DOUBLE),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web4.jpg",0,0,2);
        me.getShipBoard().positionTile(Optional.of(tile7), new Coordinates(3,3));
        try {
            virtualController.notifySetTile(tile7.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile6=new DoubleEngine( new Link(Connectors.SINGLE),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web96.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile6), new Coordinates(3,4));
        try {
            virtualController.notifySetTile(tile6.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile8= new SingleCannon ( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web102.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile8), new Coordinates(1,2));
        try {
            virtualController.notifySetTile(tile8.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile9=new CargoRed(1, new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web63.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile9), new Coordinates(2,1));
        try {
            virtualController.notifySetTile(tile9.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile10= new SingleCannon ( new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web102.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile10), new Coordinates(1,4));
        try {
            virtualController.notifySetTile(tile10.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile11=new SingleEngine( new Link(Connectors.SMOOTH),new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web72.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile11), new Coordinates(3,1));
        try {
            virtualController.notifySetTile(tile11.send());
        } catch (RemoteException e) {
            setOffline();
        }
        Tile tile12=new SingleEngine( new Link(Connectors.UNIVERSAL),new Link(Connectors.SMOOTH),new Link(Connectors.SMOOTH),new Link(Connectors.SINGLE), "/images/grafiche/grafiche/tiles/GT-new_tiles_16_for web82.jpg",0,0);
        me.getShipBoard().positionTile(Optional.of(tile12), new Coordinates(3,5));
        try {
            virtualController.notifySetTile(tile12.send());
        } catch (RemoteException e) {
            setOffline();
        }
        return;
    }

    public void reconnect() {
        if(ipR!=null) {
            try {
                connectRMI(ipR, 0);
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                view.showGenericMessage("Server offline");
                setConnected(false);
            }
        }else if(ipS!=null){
            try {
                connectSocket(ipS,0);
            } catch (IOException | NotBoundException e) {
                view.showGenericMessage("Server offline");
                setConnected(false);
            }
        }
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended =ended;
    }
}

