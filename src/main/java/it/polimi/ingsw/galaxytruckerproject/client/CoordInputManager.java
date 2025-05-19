package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
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
    private boolean set=true;

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
        this.coordReqType = coordReqType;
        switch (coordReqType) {
            case CHOOSE_DOUBLE_CANNON, CHOOSE_DOUBLE_ENGINE, CHOOSE_TO_BREAK, CHOOSE_BATTERY -> needed=0;
            case REMOVE_GOODS ->{
                needed= clientController.getDisplayedCard().getFirst().getGoodsPenalty();
                if (lightShipBoard.isCargoEmpty()) {
                    if (lightShipBoard.getNumBatteries() == 0) {
                        needed = 0;
                        return;
                    }
                    this.needed = Math.min(lightShipBoard.getNumBatteries(),needed);
                }
                else if (lightShipBoard.getAllGoods().size() < needed) {
                        needed= lightShipBoard.getAllGoods().size();
                        needed += needed - lightShipBoard.getAllGoods().size();
                        needed = Math.min(lightShipBoard.getNumBatteries()+lightShipBoard.getAllGoods().size(),needed);
                    }
                return;
            }
            case CHOOSE_TO_MAINTAIN -> needed=1;
            case CHOOSE_CREW -> {
                needed = clientController.getDisplayedCard().getFirst().getCrewNumber();
                needed = Math.min (lightShipBoard.getNumTotalCrew(),needed);
            }
        }
    }

    //needed serve per sapere quante coordinate servono (ad esempio per quando bisogna scegliere quali crewMate eliminare), se non è necessario un numero indicare -1
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
                    clientController.getMe().getShipBoard().chooseBatteryUse(coordinate);
                    coordinates.add(coordinate);
                } else {
                    return false;
                }
            }
            case CHOOSE_DOUBLE_CANNON ->{
                if(tile.getStrength()>0) {
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
                if(tile.getEngineStrength()==2) {
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

    public boolean endCheckingFase() {
        if (coordReqType == CoordReqType.CHOOSE_DOUBLE_CANNON && needed == 0) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().sendDoubleCannonUsed(fireStrength, coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (coordReqType == CoordReqType.CHOOSE_DOUBLE_ENGINE && needed == 0) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().sendNumDoubleEngineUsed(numEngine, coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (coordReqType == CoordReqType.CHOOSE_TO_BREAK && needed != 0) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().shipErrorManagement(coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (coordReqType == CoordReqType.CHOOSE_TO_MAINTAIN && needed == coordinates.size()) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().chooseBranch(coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (coordReqType == CoordReqType.CHOOSE_BATTERY ) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().useBattery(coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (coordReqType == CoordReqType.REMOVE_GOODS && needed == coordinates.size()) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().removeGoods(coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (coordReqType == CoordReqType.CHOOSE_CREW & needed == coordinates.size()) {
            clientController.setState(ClientState.WAIT);
            try {
                clientController.getVirtualController().removeCrew(coordinates);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
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
}
