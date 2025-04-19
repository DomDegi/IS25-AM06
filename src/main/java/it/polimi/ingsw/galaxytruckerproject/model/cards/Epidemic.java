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

    public void executeCard() {
        for(Player player: game.getListOfInFlightPlayers()){
            ArrayList<Tile> modifiedCabins = player.getShipBoard().epidemic();
            notifyModifiedTiles(player.getPlayerName(),  modifiedCabins);
        }
        game.endCardEvent();
    }

    @Override
    public String toString() {
        return "Epidemic";
    }
}
