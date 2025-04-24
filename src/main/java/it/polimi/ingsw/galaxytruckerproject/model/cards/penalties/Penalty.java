package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrewPenalty.class, name = "CrewPenalty"),
        @JsonSubTypes.Type(value = ProjectilePenalty.class, name = "CannonPenalty"),
        @JsonSubTypes.Type(value = GoodsPenalty.class, name = "GoodsPenalty"),
        @JsonSubTypes.Type(value = FlightDaysPenalty.class, name = "FlightDaysPenalty")
})

public abstract class Penalty {

    @Override
    public abstract String toString();

    public abstract boolean initializePenalty(VirtualView view, Player player);

    public ArrayList<Tile> removeGoods(Player player, VirtualView view, ArrayList<Coordinates> toRemove) {
        return null;
    }
    public ArrayList<Tile> removeCrew(Player player, VirtualView view, ArrayList<Coordinates> toRemove) {
        return null;
    }

    public ArrayList<Tile> automaticGoodsPenalty(GameInterface game, Player disconnectedPlayer, ViewInterface view) {
        return null;
    }

    public ArrayList<Tile> automaticCrewPenalty(GameInterface game, Player disconnectedPlayer, ViewInterface view) {
        return null;
    }

}
