package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class GoodsPenalty extends Penalty {

    private final int numberOfLostGoods;

    public GoodsPenalty(int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {
        return numberOfLostGoods;
    }

    @Override
    public void applyPenalty(Player player, FlightBoard flightBoard){
        player.loseGoods(numberOfLostGoods);
    }

    @Override
    public String toString() {
        return "GoodsPenalty";
    }
}
