package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.AlienOptions;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CabinsManager {
    private final LightPlayer lightPlayer;
    private final DisplayableView view;
    private final ArrayList<Coordinates> cabins;
    ArrayList<Tile> modifiedCabins;
    private int index;

    public CabinsManager(LightPlayer lightPlayer, DisplayableView view) {
        this.view = view;
        this.lightPlayer = lightPlayer;
        this.cabins = lightPlayer.getShipBoard().getCabinsCoordinates();
        this.modifiedCabins = new ArrayList<>();
        this.index=0;
    }

    public ArrayList<Tile> manageCabins(CrewType crewType) {
        if (index >= cabins.size()) {
            return modifiedCabins;
        }
        if(lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.NO){
            if(crewType!=CrewType.HUMAN) {
                view.wrongLocalInput();
                setup();
                return null;
            }
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.PURPLE){
            if(crewType==CrewType.BROWN) {
                view.wrongLocalInput();

                setup();
                return null;
            }
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BROWN){
            if(crewType==CrewType.PURPLE) {
                view.wrongLocalInput();
                setup();
                return null;
            }
        }
        lightPlayer.getShipBoard().getTile(cabins.get(index)).setCrewType(crewType);
        modifiedCabins.add(lightPlayer.getShipBoard().getTile(cabins.get(index)).send());
        index++;
        if (index >= cabins.size()) {
            return modifiedCabins;
        }
        setup();
        return null;
    }

    public void setup() {
        if(cabins.get(index).equals( new Coordinates(2, 3))) {
            index++;
        }
        if(index >= cabins.size()) {
            return;
        }
        if(lightPlayer.getShipBoard().getTile(cabins.get(index)) != null && lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.NO){
            view.showGenericMessage(cabins.get(index).toString()+" - Only Human are allowed here");
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.PURPLE){
            view.showGenericMessage(cabins.get(index).toString()+" - Human and Purple aliens are allowed here");
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BROWN){
            view.showGenericMessage(cabins.get(index).toString()+" - Human and Brown aliens are allowed here");
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BOTH){
            view.showGenericMessage(cabins.get(index).toString()+" - Everyone is allowed here");
        }
    }
}
