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

/**
 * Manages the cabins and crew placement on the player's ship during the game.
 * It allows the player to assign crew members to specific cabins, respecting
 * the alienability of each cabin, and keeps track of the modified cabins.
 * <p>
 * This class interacts with the {@link LightPlayer}'s shipboard, validating
 * the crew assignments and ensuring that only compatible crew members are
 * placed in each cabin.
 * </p>
 */
public class CabinsManager {
    /**
     * The light player object representing the current player, used for interacting
     * with the player's shipboard and modifying its state.
     */
    private final LightPlayer lightPlayer;

    /**
     * The view used to display messages and prompts to the user.
     * It provides feedback about crew placement and invalid actions.
     */
    private final DisplayableView view;

    /**
     * A list of coordinates representing the available cabins on the player's ship.
     */
    private final ArrayList<Coordinates> cabins;

    /**
     * A list of modified cabins after crew placement. This list tracks the changes made
     * to the ship's cabins during the crew assignment phase.
     */
    ArrayList<Tile> modifiedCabins;

    /**
     * The index of the current cabin being processed in the cabins list.
     * This value is used to iterate through the available cabins.
     */
    private int index;

    /**
     * Constructs a new {@link CabinsManager} with the specified player and view.
     * Initializes the cabins list based on the player's shipboard and removes
     * the cabin at coordinates (2, 3).
     *
     * @param lightPlayer the light player object representing the current player
     * @param view the view used to display messages to the user
     */
    public CabinsManager(LightPlayer lightPlayer, DisplayableView view) {
        this.view = view;
        this.lightPlayer = lightPlayer;
        this.cabins = new ArrayList<>(lightPlayer.getShipBoard().getCabinsCoordinates());
        cabins.remove(new Coordinates(2,3));
        this.modifiedCabins = new ArrayList<>();
        this.index=0;
    }

    /**
     * Manages the assignment of crew members to the cabins based on the selected
     * crew type. It checks the alienability of each cabin and only allows
     * compatible crew members to be placed in the cabins.
     *
     * @param crewType the type of crew member (e.g., human, purple alien, brown alien)
     * @return the list of modified cabins after the crew placement
     */
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

    /**
     * Displays the available cabins and their respective allowed crew types
     * to the player, guiding them through the crew placement process.
     */
    public void setup() {
        if(index >= cabins.size()) {
            return;
        }
        if(lightPlayer.getShipBoard().getTile(cabins.get(index)) != null && lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.NO){
            view.showGenericMessage(cabins.get(index).toString()+" - Only Human are allowed here");
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)) != null &&lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.PURPLE){
            view.showGenericMessage(cabins.get(index).toString()+" - Human and Purple aliens are allowed here");
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)) != null &&lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BROWN){
            view.showGenericMessage(cabins.get(index).toString()+" - Human and Brown aliens are allowed here");
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)) != null &&lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BOTH){
            view.showGenericMessage(cabins.get(index).toString()+" - Everyone is allowed here");
        }
    }

    /**
     * Returns the alienability of the current cabin, specifying which crew types
     * are allowed to be placed in it.
     *
     * @return the alienability of the current cabin (e.g., NO, PURPLE, BROWN, BOTH)
     */
    public AlienOptions crewType() {
        if(lightPlayer.getShipBoard().getTile(cabins.get(index)) != null && lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.NO){
            return AlienOptions.NO;
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)) != null &&lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.PURPLE){
            return AlienOptions.PURPLE;
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)) != null &&lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BROWN){
            return AlienOptions.BROWN;
        }else if (lightPlayer.getShipBoard().getTile(cabins.get(index)) != null &&lightPlayer.getShipBoard().getTile(cabins.get(index)).getAlienability()==AlienOptions.BOTH){
            return AlienOptions.BOTH;
        }else
            return AlienOptions.NO;
    }

    /**
     * Returns the list of available cabins that can be assigned crew members.
     *
     * @return a list of coordinates representing the available cabins
     */
    public ArrayList<Coordinates> getCabins() {
        return cabins;
    }


    /**
     * Returns the current index of the cabin being processed.
     *
     * @return the index of the cabin being processed
     */
    public int getIndex() {
        return index;
    }
}
