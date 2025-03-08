package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

public class FlightDaysPenalty extends Penalty{
    private final int numberOfLostDays;

    public FlightDaysPenalty(int numberOfLostDays) {
        this.numberOfLostDays = numberOfLostDays;
    }

    @Override
    public void applyPenalty(Player player, FlightBoard flightBoard) {
        flightBoard.moveBackward(player, numberOfLostDays);
    }
}
