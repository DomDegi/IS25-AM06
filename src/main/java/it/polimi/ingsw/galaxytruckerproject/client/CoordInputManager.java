package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.*;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;

public class CoordInputManager {
    private final LightShipboard lightShipboard;
    private CoordReqType coordReqType;
    private ArrayList<Coordinates> coordinates;
    public CoordInputManager(LightShipboard lightShipBoard)
    {
        this.lightShipboard = lightShipBoard;
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
        switch (coordReqType) {
            case CHOOSE_TO_BREACK:{
                if()
            }
            case CHOOSE_TO_MANTAIN:{

            }
            case CHOOSE_BATTERY:{

            }
            case CHOOSE_DOUBLE_CANNON:{

            }
            case CHOOSE_DOUBLE_ENGINE:{

            }


        }
        return true;
    }
}
