package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CoordInputManager {
    private final LightShipBoard lightShipBoard;
    private final ClientController clientController;
    private CoordReqType coordReqType;
    private float fireStrength;
    private int numEngine;
    private final ArrayList<Coordinates> coordinates;
    private int needed;

    public CoordInputManager(LightShipBoard lightShipBoard, ClientController clientController) {
        this.lightShipBoard = lightShipBoard;
        this.clientController = clientController;
        coordinates = new ArrayList<>();
        needed = 0;
        fireStrength = 0;
        numEngine = 0;
    }

    public void setCoordReqType(CoordReqType coordReqType) {
        this.coordinates.clear();
        switch (coordReqType) {
            case CHOOSE_DOUBLE_CANNON, CHOOSE_DOUBLE_ENGINE, CHOOSE_TO_BREAK -> needed=0;
            case CHOOSE_BATTERY, REMOVE_GOODS -> needed= clientController.getDisplayedCard().getGoodsPenalty();
            case CHOOSE_TO_MAINTAIN -> needed=1;
            case CHOOSE_CREW -> needed=clientController.getDisplayedCard().getCrewNumber();
        }
        this.coordReqType = coordReqType;
    }

    //needed serve per sapere quante coordinate servono (ad esempio per quando bisogna scegliere quali crewMate eliminare), se non è necessario un numero indicare -1
    public boolean checkCoord(Coordinates coordinate) throws RemoteException {
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
                remove.add(coordinate);
                clientController.getMe().getShipBoard().destroy(remove);
                coordinates.add(coordinate);
                needed++;
            }
            case CHOOSE_TO_MAINTAIN -> {
                if(coordinates.isEmpty()) {
                    coordinates.add(coordinate);
                } else {
                    return false;
                }
            }
            case CHOOSE_BATTERY -> {
                if(tile.getNumBatteries()>0) {
                    clientController.getView().showGenericMessage("battery added correctly");
                    clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                    coordinates.add(coordinate);
                } else {
                    return false;
                }
            }
            case CHOOSE_DOUBLE_CANNON ->{
                if(tile.getStrength()>0) {
                    clientController.getView().showGenericMessage("double cannon added correctly");
                    fireStrength += tile.getStrength();
                    needed++;
                } else if(tile.getNumBatteries()>0 && needed >0) {
                    clientController.getView().showGenericMessage("battery added correctly");
                    clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                    coordinates.add(coordinate);
                    needed--;
                } else {
                    return false;
                }
            }
            case CHOOSE_DOUBLE_ENGINE -> {
                if(tile.getEngineStrength()==2) {
                    clientController.getView().showGenericMessage("double engine added correctly");
                    numEngine++;
                    needed++;
                } else if(tile.getNumBatteries()>0 && needed >0) {
                    clientController.getView().showGenericMessage("battery added correctly");
                    clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                    coordinates.add(coordinate);
                    needed--;
                } else {
                    return false;
                }
            }
            case CHOOSE_CREW -> {
                if(tile.getCrew()>0 && !coordinates.contains(coordinate)) {
                    ArrayList<Coordinates> remove=new ArrayList<>();
                    remove.add(coordinate);
                    clientController.getMe().getShipBoard().removeCrew(remove);
                    coordinates.add(coordinate);
                } else if (tile.getCrew()>1 && coordinates.contains(coordinate)){
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
                    setCoordReqType(CoordReqType.CHOOSE_BATTERY);
                    checkCoord(coordinate);
                } else {
                    return false;
                }
            }
        }
        if(coordinates.size()==needed && (coordReqType != CoordReqType.CHOOSE_DOUBLE_ENGINE && coordReqType != CoordReqType.CHOOSE_DOUBLE_CANNON && coordReqType != CoordReqType.CHOOSE_TO_BREAK )) {
            endCheckingFase();
        }
        return true;
    }

    public boolean endCheckingFase() throws RemoteException {
        if(coordReqType == CoordReqType.CHOOSE_DOUBLE_CANNON && needed == 0 ) {
            clientController.getVirtualController().sendDoubleCannonUsed(fireStrength,coordinates);
            coordinates.clear();
            fireStrength = 0;
            numEngine = 0;
            clientController.getView().setClientState(ClientState.WAIT);
            return true;
        }
        if (coordReqType == CoordReqType.CHOOSE_DOUBLE_ENGINE && needed == 0  ) {
            clientController.getVirtualController().sendNumDoubleEngineUsed(numEngine, coordinates);
            coordinates.clear();
            numEngine = 0;
            fireStrength = 0;
            clientController.getView().setClientState(ClientState.WAIT);
            return true;
        }
        if(needed==coordinates.size()) {
            clientController.getVirtualController().sendCoordinates(coordinates);
            coordinates.clear();
            fireStrength = 0;
            needed = 0;
            numEngine = 0;
            clientController.getView().setClientState(ClientState.WAIT);
            return true;
        }
        clientController.getView().wrongLocalInput();
        return false;
    }
}
