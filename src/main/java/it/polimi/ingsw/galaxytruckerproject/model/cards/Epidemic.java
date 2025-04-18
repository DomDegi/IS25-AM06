package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

public class Epidemic extends Card {

    @JsonCreator
    public Epidemic(@JsonProperty("level") int level) {
        super(level, 0);
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        executeCard();
    }

    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {}
    @Override
    public void engineChoice(String playerName, int numDoubleEngine, ArrayList<Coordinates> batteriesToUse) {}
    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {}
    @Override
    public void choice(String playerName, boolean decision) {}

    public void executeCard() {
        for(Player player: game.getListOfInFlightPlayers()){
            ArrayList<Tile> modifiedCabins = player.getShipBoard().epidemic();
            notifyRemovedCrew(player.getPlayerName(),modifiedCabins);
        }
        game.endCardEvent();
    }

    public void notifyRemovedCrew (String playerName, ArrayList<Tile> modifiedCabins) {
        for (VirtualView view: viewsMap.values()) {
            try {
                view.notifyModifiedTiles(playerName, modifiedCabins);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public String toString() {
        return "Epidemic";
    }
}
