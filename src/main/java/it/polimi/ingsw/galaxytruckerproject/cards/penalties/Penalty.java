package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrewPenalty.class, name = "CrewPenalty"),
        @JsonSubTypes.Type(value = ProjectilePenalty.class, name = "CannonPenalty"),
        @JsonSubTypes.Type(value = GoodsPenalty.class, name = "GoodsPenalty"),
        @JsonSubTypes.Type(value = FlightDaysPenalty.class, name = "FlightDaysPenalty")
})

public abstract class Penalty {

    public abstract int applyPenalty(Game game, Player player, String[] input);

    @Override
    public abstract String toString();

    public abstract void printInfo(Player player);
}
