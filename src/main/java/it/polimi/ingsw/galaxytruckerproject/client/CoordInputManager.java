package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.*;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CoordInputManager {
    private final LightShipboard lightShipboard;
    private final ClientController client;
    private CoordReqType coordReqType;
    private ArrayList<Coordinates> coordinates;
    public CoordInputManager(LightShipboard lightShipBoard, ClientController client)
    {
        this.lightShipboard = lightShipBoard;
        this.client = client;
        coordinates = new ArrayList<>();
    }

    public void setCoordReqType(CoordReqType coordReqType) {
        coordinates.clear();
        this.coordReqType = coordReqType;
    }

    public boolean checkCoord(Coordinates coordinate) {
        Tile tile = lightShipboard.getTile(coordinate);
        if(tile == null) {
            System.out.println("Tile not found");
            return false;
        }
        if(!tile.fillable()){
            System.out.println("this Tile is not fillable");
            return false;
        }
        switch (coordReqType) {
            case CHOOSE_TO_BREACK:{
                coordinates.add(coordinate);
                return true;
            }
            case CHOOSE_TO_MANTAIN:{
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
            int Strength = 0;
            for(Coordinates coordinate : coordinates) {
                Strength+= lightShipboard.getTile(coordinate).getStrength();
            }
            client.getVirtualController().sendStrenghtDoubleCannonUsed(Strength);
            coordinates.clear();
            return;
        }
        if (coordReqType == CoordReqType.CHOOSE_DOUBLE_ENGINE) {
            client.getVirtualController().sendNumDoubleEngineUsed(coordinates.size());
            coordinates.clear();
            return;
        }
        client.getVirtualController().sendCoordinates(coordinates);
        coordinates.clear();
    }
}
