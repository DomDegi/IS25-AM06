package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Cabin;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;

import java.util.ArrayList;

public class CabinsManager {
    private LightPlayer lightPlayer;
    private ArrayList<Coordinates> cabins;
    ArrayList<Cabin> modifiedCabins = new ArrayList<>();
    private int index;
    private boolean initialized;
    public CabinsManager(LightPlayer lightPlayer) {
        this.lightPlayer = lightPlayer;
        this.cabins = lightPlayer.getShipBoard().getCabinsCoordinates();
        initialized = false;
    }

    public ArrayList<Cabin> manageCabins(CrewType crewType) {
        if (!initialized) {
            setup();
        }else {
            lightPlayer.getShipBoard().getTile(cabins.get(index)).setCrewType(crewType);
            modifiedCabins.add((Cabin) lightPlayer.getShipBoard().getTile(cabins.get(index)));
            index++;
            if (index >= cabins.size()) {
                return modifiedCabins;
            }
        }
        return null;
    }

    public void setup() {
        System.out.println(cabins.get(index));
    }
}
