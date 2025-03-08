package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import java.util.Random;

public abstract class Projectile {
    protected final Direction direction;
    protected int diceRoll;

    public Projectile(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    //generates a random number from 2 to 12 (simulates 2 dice roll)
    public int rollTheDices(){
        diceRoll = new Random().nextInt(11) + 2;
        return diceRoll;
    }
}
