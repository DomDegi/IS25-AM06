package it.polimi.ingsw.galaxytruckerproject.cards;

import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Meteor;
import java.util.ArrayList;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;

public class MeteorSwarm extends Card {
    private final ArrayList<Meteor> listOfMeteors;

    public MeteorSwarm(int level, ArrayList<Meteor> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
    }

    @Override
    public void executeCard(FlightBoard flightBoard) {
        listOfMeteors.forEach((meteor) -> {meteor.throwMeteor(flightBoard.getRanking());});
    }
}
