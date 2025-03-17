package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Projectile;

import java.util.ArrayList;

public class MeteorSwarm extends Card {
    private final ArrayList<Projectile> listOfMeteors;

    public MeteorSwarm(int level, ArrayList<Projectile> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {
        listOfMeteors.forEach((meteor) -> Projectile.throwProjectile());
    }

}
