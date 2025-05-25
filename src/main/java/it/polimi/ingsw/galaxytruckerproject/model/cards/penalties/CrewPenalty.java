package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a penalty that causes the player to lose crew members.
 * Crew can be removed manually by the player or automatically if disconnected.
 */
public class CrewPenalty extends Penalty {

    /**
     * The number of crew members to be lost due to this penalty.
     */
    private final int numberOfLostCrew;

    /**
     * The number of crew members that will be actually removed (based on player state).
     */
    private int numberOfCrew;

    /**
     * Constructs a CrewPenalty with the specified number of crew members to be lost.
     *
     * @param numberOfLostCrew number of crew members to remove
     */
    @JsonCreator
    public CrewPenalty(@JsonProperty("numberOfLostCrew") int numberOfLostCrew) {
        this.numberOfLostCrew = numberOfLostCrew;
    }

    /**
     * Returns the number of crew members to be lost.
     *
     * @return number of lost crew
     */
    public int getNumberOfLostCrew() {
        return numberOfLostCrew;
    }

    /**
     * Removes crew from specified coordinates and returns updated tiles.
     * If input is invalid, an error message is shown.
     *
     * @param player      the affected player
     * @param virtualView the view used to notify user
     * @param toRemove    coordinates where crew will be removed
     * @return updated tiles after crew removal, or null if input is invalid
     */
    @Override
    public ArrayList<Tile> removeCrew(Player player, VirtualView virtualView, ArrayList<Coordinates> toRemove) {
        if (toRemove.size() < numberOfCrew) {
            try {
                virtualView.showWrongInputMessage();
            } catch (Exception ignored) {}
            return null;
        }
        while (toRemove.size() > numberOfCrew) {
            toRemove.removeLast();
        }
        ArrayList<Tile> updatedTiles = player.removeCrew(toRemove);
        if (updatedTiles == null)  {
            try {
                virtualView.showWrongInputMessage();
            } catch (Exception ignored) {}
            return null;
        } else {
            return updatedTiles;
        }
    }

    /**
     * Automatically removes crew from a disconnected player.
     *
     * @param disconnectedPlayer the player to apply the penalty to
     * @param view               the view used to notify changes
     * @return updated tiles after automatic crew removal, or null if failed
     */
    @Override
    public ArrayList<Tile> automaticCrewPenalty(Player disconnectedPlayer, ViewInterface view) {
        ArrayList<Tile> toUpdate = new ArrayList<>();
        ArrayList<Coordinates> cabins = disconnectedPlayer.getShipBoard().getCabinsCoordinates();
        for (int i = 0; i < numberOfCrew; i++) {
            Coordinates firstCabin = cabins.getFirst();
            boolean returnValue = disconnectedPlayer.getShipBoard().chooseCrewToRemove(firstCabin);
            if (!returnValue) {
                return null;
            }
            if (!toUpdate.contains(disconnectedPlayer.getShipBoard().getTile(firstCabin))) {
                toUpdate.add(disconnectedPlayer.getShipBoard().getTile(firstCabin));
            }
        }
        return toUpdate;
    }

    /**
     * Initializes the penalty, determining how many crew to remove and triggering
     * automatic removal if the player is disconnected.
     *
     * @param game   the game interface
     * @param view   the view interface
     * @param player the player affected by the penalty
     * @return true if the player must input coordinates manually, false otherwise
     */
    @Override
    public boolean initializePenalty(GameInterface game, VirtualView view, Player player) {
        if (player.getTotalCrew() <= numberOfLostCrew) {
            player.setCrewToZero();
            try {
                view.setClientState(ClientState.WAIT);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        numberOfCrew = Math.min(player.getTotalCrew(), numberOfLostCrew);
        if (player.IsDisconnected()) {
            game.getDrawnCard().notifyModifiedTiles(player.getPlayerName(), automaticCrewPenalty(player, view));
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of this penalty.
     *
     * @return string description of the penalty
     */
    @Override
    public String toString() {
        return "CrewPenalty: " + numberOfLostCrew;
    }
}
