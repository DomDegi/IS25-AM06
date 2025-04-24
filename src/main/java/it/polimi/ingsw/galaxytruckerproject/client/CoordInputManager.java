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
    private ArrayList<Coordinates> coordinates;
    private int numBattery;
    private int needed;
    public CoordInputManager(LightShipBoard lightShipBoard, ClientController clientController)
    {
        this.lightShipBoard = lightShipBoard;
        this.clientController = clientController;
        coordinates = new ArrayList<>();
        numBattery = 0;
        fireStrength = 0;
        numEngine = 0;
    }

    public void setCoordReqType(CoordReqType coordReqType) {
        coordinates.clear();
        this.coordReqType = coordReqType;
    }

    //needed serve per sapere quande coordinate servono ( ad esempio per quando bisogna scegliere quali   crewmate eliminare), se non è necessario un numero indicare -1
    public boolean checkCoord(Coordinates coordinate, int needed) throws RemoteException {
        this.needed = needed;
        Tile tile = lightShipBoard.getTile(coordinate);
        if(tile == null) {
            System.out.println("Tile not found");
            return false;
        }
        if(!tile.fillable()){
            clientController.getView().wrongLocalInput();
            return false;
        }
        switch (coordReqType) {
            case CHOOSE_TO_BREAK->{
                coordinates.add(coordinate);
            }
            case CHOOSE_TO_MAINTAIN->{
                if(coordinates.isEmpty()) {
                    coordinates.add(coordinate);
                }
                else{
                    clientController.getView().wrongLocalInput();
                    return false;
                }
            }
            case CHOOSE_BATTERY->{
                if(tile.getNumBatteries()>0) {
                    System.out.println("battery added correctly");
                    coordinates.add(coordinate);
                }
                else {
                    clientController.getView().wrongLocalInput();
                    return false;
                }

            }
            case CHOOSE_DOUBLE_CANNON->{
                if(tile.getStrength()>0) {
                    System.out.println("double cannon added correctly");
                    fireStrength += tile.getStrength();
                }
                if(tile.getNumBatteries()>0) {
                    System.out.println("battery added correctly");
                    coordinates.add(coordinate);
                }
                else {
                    clientController.getView().wrongLocalInput();
                    return false;
                }

            }
            case CHOOSE_DOUBLE_ENGINE-> {
                if(tile.getEngineStrength()==2) {
                    System.out.println("double engine added correctly");
                    numEngine++;
                }
                if(tile.getNumBatteries()>0) {
                    System.out.println("battery added correctly");
                    coordinates.add(coordinate);
                }
                else {
                    clientController.getView().wrongLocalInput();
                    return false;
                }
            }
            case CHOOSE_CREW -> {
                if(tile.getCrew()>0 && !coordinates.contains(coordinate)) {
                    coordinates.add(coordinate);
                }
                else if (tile.getCrew()>1 && coordinates.contains(coordinate)){
                    coordinates.add(coordinate);
                }
                else{
                    clientController.getView().wrongLocalInput();
                    return false;
                }
            }
            case REMOVE_GOODS -> {
                //DA GUARDARE
                if(lightShipBoard.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()){

                }
            }



        }
        if(coordinates.size()==needed) {
            endCheckingFase();
        }
        return true;
    }

    public void endCheckingFase() throws RemoteException {
        if(coordReqType == CoordReqType.CHOOSE_DOUBLE_CANNON) {
            if(numBattery*2 == coordinates.size() ) {
            clientController.getVirtualController().sendDoubleCannonUsed(clientController.getName(),fireStrength,coordinates);
            coordinates.clear();
            fireStrength = 0;
            numEngine = 0;}
            else {
                clientController.getView().wrongLocalInput();
            }
            return;

        }
        if (coordReqType == CoordReqType.CHOOSE_DOUBLE_ENGINE) {
            if(numBattery*2 == coordinates.size() ) {
                clientController.getVirtualController().sendNumDoubleEngineUsed(clientController.getName(), numEngine, coordinates);
                coordinates.clear();
                numEngine = 0;
                fireStrength = 0;
            }
            else {
                clientController.getView().wrongLocalInput();
            }
            return;
        }
        if(needed==-1 || needed==coordinates.size()) {
        clientController.getVirtualController().sendCoordinates(clientController.getName(), coordinates);
        coordinates.clear();
        fireStrength = 0;
        numBattery=0;
        numEngine = 0;
        clientController.getView().setClientState(ClientState.WAIT_OTHER_PLAYER_ACTION);

        }
        else
        {
            clientController.getView().wrongLocalInput();
        }
        return;
    }
}
