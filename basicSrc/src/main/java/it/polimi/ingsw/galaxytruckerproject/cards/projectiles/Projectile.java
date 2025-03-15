package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Random;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Meteor.class, name = "Meteor"),
        @JsonSubTypes.Type(value = CannonShot.class, name = "CannonShot")
})

public abstract class Projectile {
    protected Direction direction;
    protected final Game game;
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
    public Defense throwProjectile() {
        return Defense.PROTECTED;
    }
}
