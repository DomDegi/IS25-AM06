package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Cabin;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.util.ArrayList;
import java.util.Objects;

public class CabinsManager {
    private final LightPlayer lightPlayer;
    private ArrayList<Coordinates> cabins;
    ArrayList<Tile> modifiedCabins = new ArrayList<>();
    private int index;
    private boolean initialized;
    public CabinsManager(LightPlayer lightPlayer) {
        this.lightPlayer = lightPlayer;
        this.cabins = lightPlayer.getShipBoard().getCabinsCoordinates();
        initialized = false;
    }

    public ArrayList<Tile> manageCabins(CrewType crewType) {
        if (!initialized) {
            setup();
        }else {
            if(!cabins.get(index).equals( new Coordinates(2, 3))) {
                index++;
            }
            lightPlayer.getShipBoard().getTile(cabins.get(index)).setCrewType(crewType);
            modifiedCabins.add(lightPlayer.getShipBoard().getTile(cabins.get(index)));
            index++;
            if (index >= cabins.size()) {
                initialized = false;
                return modifiedCabins;
            }
        }
        return null;
    }

    public void setup() {
        System.out.println(cabins.get(index));
        initialized = true;
    }
}
