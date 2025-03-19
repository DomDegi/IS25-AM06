package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.tiles.Direction;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;

import java.util.Optional;

public class SmallCannonShot extends Projectile {


    public SmallCannonShot( Direction direction){
        super(game, direction);
    }

    public Defense throwProjectile(Game game, Player player, int diceRoll) {
        Throw(player,diceRoll);
        if(coordinatesToDestroy.isEmpty()){
            //miss
            return Defense.PROTECTED;
        }
        if(direction.equals(Direction.NORTH)) {
            return checkNorth(player,diceRoll);
        }
        if(direction.equals(Direction.SOUTH)){
            return checkSouth(player,diceRoll);
        }
        if(direction.equals(Direction.EAST)){
            return checkEast(player,diceRoll);
        }
        return checkWest(player,diceRoll);
    }

    private Defense checkNorth(Player player, int diceRoll){
        ShipBoard ship = player.getShipBoard();
        if(ship.getCoverageShields().contains(Coverage.NORTH_EAST)||ship.getCoverageShields().contains(Coverage.NORTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }
    private Defense checkSouth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.SOUTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }
    private Defense checkEast(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.SOUTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }
    private Defense checkWest(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        if(ship.getCoverageShields().contains(Coverage.SOUTH_WEST)||ship.getCoverageShields().contains(Coverage.NORTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;

    }
}
