package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;

public abstract class Penalty {
    public abstract void applyPenalty(Player player, FlightBoard flightBoard);
}
