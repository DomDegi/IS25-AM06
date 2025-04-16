package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.*;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CoordInputManager {
    private final LightShipBoard lightShipBoard;
    private final ClientController client;
    private final LightShipBoard lightShipBoard;
    private final ClientController clientController;
    private CoordReqType coordReqType;
    private float fireStrength =0;
    private int numEngine =0;
    private ArrayList<Coordinates> coordinates;
    public CoordInputManager(LightShipBoard lightShipBoard, ClientController clientController)
    {
        this.lightShipBoard = lightShipBoard;
        this.clientController = clientController;
        coordinates = new ArrayList<>();
    }

    public void setCoordReqType(CoordReqType coordReqType) {
        coordinates.clear();
        this.coordReqType = coordReqType;
    }

    public boolean checkCoord(Coordinates coordinate) {
        Tile tile = lightShipBoard.getTile(coordinate);
        if(tile == null) {
            System.out.println("Tile not found");
            return false;
        }
        if(!tile.fillable()){
            System.out.println("this Tile is not fillable");
            return false;
        }
        switch (coordReqType) {
            case CHOOSE_TO_BREAK:{
                coordinates.add(coordinate);
                return true;
            }
            case CHOOSE_TO_MAINTAIN:{
                if(coordinates.isEmpty()) {
                    coordinates.add(coordinate);
                    return true;
                }
                else{
                    System.out.println("you already made your choice");
                    return false;
                }
            }
            case CHOOSE_BATTERY:{
                if(tile.getNumBatteries()>0) {
                    System.out.println("battery added correctly");
                    coordinates.add(coordinate);
                    return true;
                }
                else {
                    System.out.println("no battery cells here");
                    return false;
                }

            }
            case CHOOSE_DOUBLE_CANNON:{
                if(tile.getStrength()>0) {
                    System.out.println("double cannon added correctly");
                    fireStrength += tile.getStrength();
                    return true;
                }
                if(tile.getNumBatteries()>0) {
                    System.out.println("battery added correctly");
                    coordinates.add(coordinate);
                    return true;
                }
                else {
                    System.out.println("no double cannon here");
                    return false;
                }

            }
            case CHOOSE_DOUBLE_ENGINE:{
                if(tile.getEngineStrength()==2) {
                    System.out.println("double engine added correctly");
                    numEngine++;
                    return true;
                }
                if(tile.getNumBatteries()>0) {
                    System.out.println("battery added correctly");
                    coordinates.add(coordinate);
                    return true;
                }
                else {
                    System.out.println("no double engine here");
                    return false;
                }
            }


        }
        return true;
    }

    public void endCheckingFase() throws RemoteException {
        if(coordReqType == CoordReqType.CHOOSE_DOUBLE_CANNON) {
            clientController.getVirtualController().sendDoubleCannonUsed(clientController.getName(),fireStrength,coordinates);
            coordinates.clear();
            fireStrength = 0;
            numEngine = 0;
            return;
        }
        if (coordReqType == CoordReqType.CHOOSE_DOUBLE_ENGINE) {
            clientController.getVirtualController().sendNumDoubleEngineUsed(clientController.getName(), numEngine,coordinates);
            coordinates.clear();
            numEngine = 0;
            fireStrength = 0;
            return;
        }
        clientController.getVirtualController().sendCoordinates(clientController.getName(), coordinates);
        coordinates.clear();
        fireStrength = 0;
        numEngine = 0;
    }
}
