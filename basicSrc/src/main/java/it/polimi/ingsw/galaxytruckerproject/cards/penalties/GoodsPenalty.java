package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

public class GoodsPenalty extends Penalty {

    private final int numberOfLostGoods;

    public GoodsPenalty(int numberOfLostGoods) {
        this.numberOfLostGoods = numberOfLostGoods;
    }

    public int getNumberOfLostGoods() {}

    @Override
    public void applyPenalty(Player player, FlightBoard flightBoard){
        player.loseGoods(numberOfLostGoods);
    }
}
