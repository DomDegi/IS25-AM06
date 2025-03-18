package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class LargeMeteor extends Meteor {
    public LargeMeteor(Direction direction) {
        super(direction);
    }

    @Override
    public Defense throwProjectile(ArrayList<Player> players) {
        return null;
    }

    @Override
    public Direction getDirection() {
        return super.getDirection();
    }
}