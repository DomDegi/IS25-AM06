package it.polimi.ingsw.galaxytruckerproject.cards;

public abstract class Enemies extends Card {
    protected final int cannonStrenght;

    public Enemies(int level, int requiredDays, int cannonStrenght) {
        super(level, requiredDays);
        this.cannonStrenght = cannonStrenght;
    }

    public int getCannonStrenght(){
        return cannonStrenght;
    }

    public abstract void executeCard(FlightBoard flightBoard);
}
