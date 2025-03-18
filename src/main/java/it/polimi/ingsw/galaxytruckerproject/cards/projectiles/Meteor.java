package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public abstract class Meteor extends Projectile {
    public Meteor(Direction direction) {
        super(direction);
    }
    public abstract Defense throwProjectile(ArrayList<Player> players);
}