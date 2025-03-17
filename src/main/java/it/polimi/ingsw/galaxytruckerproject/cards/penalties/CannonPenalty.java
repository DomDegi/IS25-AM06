package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.CannonShot;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class CannonPenalty extends Penalty {

    private final ArrayList<CannonShot> listOfShots;

    public CannonPenalty(ArrayList<CannonShot> listOfShots) {
        this.listOfShots = listOfShots;
    }

    public ArrayList<CannonShot> getListOfShots() {
        return listOfShots;
    }

    @Override
    public void applyPenalty(Player player, FlightBoard flightBoard){
        listOfShots.forEach(cannonShot->cannonShot.throwCannonShot(player));
    }
}
