package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;

import java.util.Optional;

public class SmallMeteor extends Projectile {
    public SmallMeteor(Direction direction) {
        super(direction);
    }

    public  Defense throwProjectile(Player player, int diceRoll) {
        Optional<Coordinates> c=Throw(player, diceRoll);
        if(c.isEmpty()){
            return Defense.PROTECTED;
        }
        if(this.direction==Direction.NORTH){
           if
        }
        else if(this.direction==Direction.SOUTH){

        }
        else if(this.direction==Direction.EAST){

        }
        else if(this.direction==Direction.WEST){

        }
    }




}