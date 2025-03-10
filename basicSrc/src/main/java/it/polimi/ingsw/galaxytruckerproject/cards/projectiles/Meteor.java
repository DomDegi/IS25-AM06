package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import java.util.Arrays;
import it.polimi.ingsw.galaxytruckerproject.Player;

public class Meteor extends Projectile {

    Size size;

    public Meteor(Direction direction, Size size) {
        super(direction);
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    @Override
    public Direction getDirection() {
        return super.getDirection();
    }

    //generates a random number from 2 to 12 (simulates 2 dice roll)
    @Override
    public int rollTheDices() {
        return super.rollTheDices();
    }

    public void throwMeteor(Player[] ranking) {
        diceRoll = rollTheDices();

        //calls defendFromProjectile on every player on the board on the tile number diceRoll from direction
        Arrays.stream(ranking).forEach(player -> {
            player.defendFromProjectile(this);
        });
    }
}
