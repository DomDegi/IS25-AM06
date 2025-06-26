package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Class responsible for managing coordinate-based inputs from the player during various phases of the game.
 * This includes handling actions such as selecting tiles to break, choosing batteries or crew members,
 * and managing goods and other game-related objects. The class coordinates with the client controller
 * to ensure that actions are properly communicated to the server.
 * <p>
 * The class also ensures that all necessary conditions are met before proceeding to the next phase of the game.
 * </p>
 */
public class CoordInputManager {
    /**
     * The light ship board associated with the current player, used for accessing tiles, cargo, and other game elements.
     */
    private final LightShipBoard lightShipBoard;

    /**
     * The client controller that handles the game state and communicates with the server.
     */
    private final ClientController clientController;

    /**
     * The current coordinate request type, which defines what the player is being asked to do (e.g., choose a battery or break a tile).
     */
    private CoordReqType coordReqType;

    /**
     * The fire strength accumulated during the action of choosing double cannons.
     * This is used to calculate the total firepower of the double cannon during the game.
     */
    private float fireStrength;

    /**
     * The number of double engines selected by the player during the game.
     */
    private int numEngine;

    /**
     * A list of coordinates selected by the player during the current action.
     * These coordinates are used to identify specific tiles or items in the game (e.g., crew, goods, batteries).
     */
    private final ArrayList<Coordinates> coordinates;

    /**
     * A list of coordinates selected by the player during the current action.
     * These coordinates are used to identify specific tiles or items in the game (e.g., crew, goods, batteries).
     */
    private final ArrayList<Coordinates> coordinatesUsed;

    /**
     * The number of coordinates that need to be selected by the player for the current action.
     * This value is dynamically updated depending on the type of action being performed (e.g., removing goods or selecting crew).
     */
    private int needed;

    /**
     * A boolean flag used to track whether the crew should be set to human during the trial mode phase.
     * This is only relevant for certain actions where the crew configuration needs to be initialized.
     */
    private boolean set = true;


    /**
     * Constructs a new {@link CoordInputManager} for the given ship board and client controller.
     *
     * @param lightShipBoard the light ship board associated with the player
     * @param clientController the client controller that manages the game state
     */
    public CoordInputManager(LightShipBoard lightShipBoard, ClientController clientController) {
        this.lightShipBoard = lightShipBoard;
        this.clientController = clientController;
        this.coordinatesUsed = new ArrayList<>();
        coordinates = new ArrayList<>();
        needed = 0;
        fireStrength = 0;
        numEngine = 0;
    }
    /**
     * Sets the current coordinate request type and adjusts the number of needed coordinates based on it.
     *
     * @param coordReqType the coordinate request type (e.g., choosing a battery, crew, or breaking tiles)
     */
    public void setCoordReqType(CoordReqType coordReqType) {
        this.coordinates.clear();
        this.coordReqType = coordReqType;
        switch (coordReqType) {
            case CHOOSE_DOUBLE_CANNON, CHOOSE_DOUBLE_ENGINE, CHOOSE_TO_BREAK, CHOOSE_BATTERY -> needed=0;
            case REMOVE_GOODS ->{
                needed= clientController.getDisplayedCard().getFirst().getGoodsPenalty();
                if (lightShipBoard.isCargoEmpty()) {
                    if (lightShipBoard.getNumBatteries() == 0) {
                        needed = 0;
                        clientController.setState(ClientState.WAIT);
                        return;
                    }
                    this.needed = Math.min(lightShipBoard.getNumBatteries(),needed);
                } else if (lightShipBoard.getAllGoods().size() < needed) {
                    needed= lightShipBoard.getAllGoods().size();
                    needed += needed - lightShipBoard.getAllGoods().size();
                    needed = Math.min(lightShipBoard.getNumBatteries()+lightShipBoard.getAllGoods().size(),needed);
                }
            }
            case CHOOSE_TO_MAINTAIN -> needed=1;
            case CHOOSE_CREW -> {
                needed = clientController.getDisplayedCard().getFirst().getCrewNumber();
                System.out.println("needed: "+needed);
                needed = Math.min (lightShipBoard.getNumTotalCrew(),needed);
                System.out.println("needed: "+needed + " lightShipBoard.getNumTotalCrew()"+ lightShipBoard.getNumTotalCrew());
            }
        }
    }

    /**
     * Checks if a coordinate is valid for the current action based on the coordinate request type.
     *
     * @param coordinate the coordinate to check
     * @return true if the coordinate is valid for the current action, false otherwise
     */
    public boolean checkCoord(Coordinates coordinate) {
        Tile tile = lightShipBoard.getTile(coordinate);
        if(tile == null) {
            return false;
        }
        if(!tile.fillable()){
            return false;
        }
        switch (coordReqType) {
            case CHOOSE_TO_BREAK -> {
                ArrayList<Coordinates> remove=new ArrayList<>();
                if(coordinate.equals(new Coordinates(2, 3)))
                    return false;
                remove.add(coordinate);
                clientController.getMe().getShipBoard().destroy(remove);
                coordinates.add(coordinate);
                needed++;
            }
            case CHOOSE_TO_MAINTAIN -> {
                if(clientController.getMe().getShipBoard().getTilesTable()[coordinate.getX()][coordinate.getY()].isPresent()) {
                    coordinates.add(coordinate);
                } else {
                    return false;
                }
            }
            case CHOOSE_BATTERY -> {
                if(tile.getNumBatteries()>0) {
                    if(clientController.getMe().getShipBoard().chooseBatteryUse(coordinate))
                        coordinates.add(coordinate);
                } else {
                    return false;
                }
            }
            case CHOOSE_DOUBLE_CANNON ->{
                if(tile.getStrength()>0&&!coordinatesUsed.contains(coordinate)) {
                    coordinatesUsed.add(coordinate);
                    fireStrength += tile.getStrength();
                    needed++;
                } else if(tile.getNumBatteries()>0 && needed >0) {
                    clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                    coordinates.add(coordinate);
                    needed--;
                } else {
                    return false;
                }
            }
            case CHOOSE_DOUBLE_ENGINE -> {
                if(tile.getEngineStrength()==2&& !coordinatesUsed.contains(coordinate)) {
                    coordinatesUsed.add(coordinate);
                    numEngine++;
                    needed++;
                } else if(tile.getNumBatteries()>0 && needed >0) {
                    clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                    coordinates.add(coordinate);
                    needed--;
                } else {
                    return false;
                }
            }
            case CHOOSE_CREW -> {
                if(set && clientController.getGameMode()==GameMode.TRIAL)
                {
                    lightShipBoard.getPlayer().setAllCrewToHuman();
                    set = false;
                }
                if(tile.getCrew()>0) {
                    ArrayList<Coordinates> remove=new ArrayList<>();
                    remove.add(coordinate);
                    clientController.getMe().getShipBoard().removeCrew(remove);
                    coordinates.add(coordinate);
                } else{
                    return false;
                }
            }
            case REMOVE_GOODS -> {
                if(!lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()){
                   if(lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).contains(coordinate)) {
                       clientController.getMe().getShipBoard().removeGood(new Goods(GoodsColor.RED),coordinate);
                       coordinates.add(coordinate);
                   }else {
                       return false;
                   }
                } else if(!lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()){
                    if(lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).contains(coordinate)) {
                        clientController.getMe().getShipBoard().removeGood(new Goods(GoodsColor.YELLOW),coordinate);
                        coordinates.add(coordinate);
                    }else {
                        return false;
                    }
                } else if(!lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).isEmpty()){
                    if(lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).contains(coordinate)) {
                        clientController.getMe().getShipBoard().removeGood(new Goods(GoodsColor.GREEN),coordinate);
                        coordinates.add(coordinate);
                    }else {
                        return false;
                    }
                } else if(!lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).isEmpty()) {
                    if (lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).contains(coordinate)) {
                        clientController.getMe().getShipBoard().removeGood(new Goods(GoodsColor.BLUE),coordinate);
                        coordinates.add(coordinate);
                    } else {
                        return false;
                    }
                } else if(lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).isEmpty()&&lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).isEmpty()){
                    if(tile.getNumBatteries()>0) {
                        clientController.getView().showGenericMessage("battery added correctly");
                        clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                        coordinates.add(coordinate);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        clientController.getView().coordinateSelected();
        if(coordinates.size()==needed && (coordReqType != CoordReqType.CHOOSE_DOUBLE_ENGINE && coordReqType != CoordReqType.CHOOSE_DOUBLE_CANNON && coordReqType != CoordReqType.CHOOSE_TO_BREAK && coordReqType != CoordReqType.CHOOSE_BATTERY )) {
            endCheckingFase();
        }
        return true;
    }

    /**
     * Ends the checking phase and performs the necessary actions based on the selected coordinates.
     *
     * @return true if the checking phase is successfully ended, false otherwise
     */
    public boolean endCheckingFase() {
        if (coordReqType == CoordReqType.CHOOSE_DOUBLE_CANNON && needed == 0) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().sendDoubleCannonUsed(fireStrength, coordinates);
            } catch (RemoteException e) {
            }
        } else if (coordReqType == CoordReqType.CHOOSE_DOUBLE_ENGINE && needed == 0) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().sendNumDoubleEngineUsed(numEngine, coordinates);
            } catch (RemoteException e) {
            }
        } else if (coordReqType == CoordReqType.CHOOSE_TO_BREAK && needed != 0) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().shipErrorManagement(coordinates);
            } catch (RemoteException e) {
            }
        } else if (coordReqType == CoordReqType.CHOOSE_TO_MAINTAIN && needed == coordinates.size()) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().chooseBranch(coordinates);
            } catch (RemoteException e) {
            }
        } else if (coordReqType == CoordReqType.CHOOSE_BATTERY ) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().useBattery(coordinates);
            } catch (RemoteException e) {
            }
        } else if (coordReqType == CoordReqType.REMOVE_GOODS && needed == coordinates.size()) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().removeGoods(coordinates);
            } catch (RemoteException e) {
            }
        } else if (coordReqType == CoordReqType.CHOOSE_CREW & needed == coordinates.size()) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().removeCrew(coordinates);
            } catch (RemoteException e) {
            }
        } else {
            clientController.getView().wrongLocalInput();
            return false;
        }
        coordinates.clear();
        numEngine = 0;
        fireStrength = 0;
        return true;
    }

    /**
     * Gets the current coordinate request type.
     *
     * @return the current coordinate request type
     */
    public CoordReqType getCoordReqType() {
        return coordReqType;
    }

    public ArrayList<Coordinates> getCoordinatesUsed() {
        return coordinatesUsed;
    }

    public int getNeeded() {
        return needed;
    }
}
