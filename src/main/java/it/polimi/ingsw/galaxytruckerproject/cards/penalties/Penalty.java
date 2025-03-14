package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrewPenalty.class, name = "CrewPenalty"),
        @JsonSubTypes.Type(value = CannonPenalty.class, name = "CannonPenalty"),
        @JsonSubTypes.Type(value = GoodsPenalty.class, name = "GoodsPenalty"),
        @JsonSubTypes.Type(value = FlightDaysPenalty.class, name = "FlightDaysPenalty")
})

public abstract class Penalty {
    public abstract void applyPenalty(Player player, FlightBoard flightBoard);
}
