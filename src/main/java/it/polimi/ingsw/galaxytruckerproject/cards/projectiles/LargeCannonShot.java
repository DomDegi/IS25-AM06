package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;
import it.polimi.ingsw.galaxytruckerproject.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class LargeCannonShot extends Projectile  {
    public LargeCannonShot(Game game, Direction direction){
        super(game,direction);
    }

    @Override
    public Defense throwProjectile(Player player, int DiceRoll) {
        Throw(player, DiceRoll);
        if(coordinatesToDestroy.isEmpty()){
            return Defense.PROTECTED;
        }
        return Defense.HIT;
    }
}
