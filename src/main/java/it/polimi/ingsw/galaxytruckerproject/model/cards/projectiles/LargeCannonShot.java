package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;

public class LargeCannonShot extends Projectile  {

    @JsonCreator
    public LargeCannonShot(@JsonProperty("direction") Direction direction) {
        super(direction);
    }

    @Override
    public Defense throwProjectile(Player player, int DiceRoll, GameInterface game) {
        Throw(player, DiceRoll);
        if(coordinatesToDestroy.isEmpty()){
            return Defense.PROTECTED;
        }
        return Defense.HIT;
    }

    @Override
    public String toString() {
        return "Large CannonShot " + direction + " ";
    }
}
