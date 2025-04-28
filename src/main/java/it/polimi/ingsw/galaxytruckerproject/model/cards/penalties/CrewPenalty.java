package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

public class CrewPenalty extends Penalty {

    private final int numberOfLostCrew;
    private int numberOfCrew;

    @JsonCreator
    public CrewPenalty(@JsonProperty("numberOfLostCrew") int numberOfLostCrew) {
        this.numberOfLostCrew = numberOfLostCrew;
    }

    public int getNumberOfLostCrew() {
        return numberOfLostCrew;
    }

    @Override
    public ArrayList<Tile> removeCrew(Player player, VirtualView virtualView, ArrayList<Coordinates> toRemove) {
        if (toRemove.size() < numberOfCrew) {
            try {
                virtualView.showWrongInputMessage();
            }catch(Exception ignored) {}
            return null;
        }
        while (toRemove.size() > numberOfCrew) {
            toRemove.removeLast();
        }
        ArrayList<Tile> updatedTiles = player.removeCrew(toRemove);
        if (updatedTiles == null)  {
            try {
                virtualView.showWrongInputMessage();
            }catch(Exception ignored) {}
            return null;
        }
        else {
            return updatedTiles;
        }
    }

    @Override
    public ArrayList<Tile> automaticCrewPenalty(Player disconnectedPlayer, ViewInterface view) {
        ArrayList<Tile> toUpdate= new ArrayList<>();
        for (int i = 0; i < numberOfLostCrew && disconnectedPlayer.getTotalCrew() > 0; i++) {
            Coordinates firstCabin = disconnectedPlayer.getShipBoard().getCabinsCoordinates().getFirst();
            disconnectedPlayer.getShipBoard().chooseCrewToRemove(firstCabin);
            if (!toUpdate.contains(disconnectedPlayer.getShipBoard().getTile(firstCabin))) {
                toUpdate.add(disconnectedPlayer.getShipBoard().getTile(firstCabin));
            }
        }
        return toUpdate;
    }

    @Override
    public boolean initializePenalty(GameInterface game, VirtualView view, Player player) {
        if (player.getTotalCrew() == 0) {
            return false;
        }
        if (player.IsDisconnected()) {
            game.getDrawnCard().notifyModifiedTiles(player.getPlayerName(), automaticCrewPenalty(player, view));
            return false;
        }
        numberOfCrew = Math.min(player.getTotalCrew(), numberOfLostCrew);
        return true;
    }

    @Override
    public String toString() {
        return "CrewPenalty: " + numberOfLostCrew;
    }
}
