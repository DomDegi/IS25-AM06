package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrewPenalty.class, name = "CrewPenalty"),
        @JsonSubTypes.Type(value = ProjectilePenalty.class, name = "CannonPenalty"),
        @JsonSubTypes.Type(value = GoodsPenalty.class, name = "GoodsPenalty"),
        @JsonSubTypes.Type(value = FlightDaysPenalty.class, name = "FlightDaysPenalty")
})

public abstract class Penalty implements Serializable {
    GameInterface game;

    @Override
    public abstract String toString();

    public Coordinates hitOrMiss(VirtualView view, Player player) { return null; }

    public abstract boolean initializePenalty(GameInterface game, VirtualView view, Player player);

    public ArrayList<Tile> removeGoods(Player player, VirtualView view, ArrayList<Coordinates> toRemove) {
        return null;
    }
    public ArrayList<Tile> removeCrew(Player player, VirtualView view, ArrayList<Coordinates> toRemove) {
        return null;
    }

    public ArrayList<Tile> automaticGoodsPenalty(Player disconnectedPlayer, ViewInterface view) {
        return null;
    }

    public ArrayList<Tile> automaticCrewPenalty(Player disconnectedPlayer, ViewInterface view) {
        return null;
    }

    public Coordinates randomRollForOne(VirtualView view, Player player) { return null; }

    public ArrayList<Coordinates> chooseToMaintain(Player player, ArrayList<Coordinates> received){
        return null;
    }

    public Tile playerUsesBatteryToDefend(Player player, ArrayList<Coordinates> batteries) { return null; }

    public int getDiceRoll() { return 0; }


    public Defense getDefenseStatus() { return null; }

    public ArrayList<Set<Coordinates>>  getBranch() { return null; }

    public Coordinates getDestroyedTile() { return null; }
}
