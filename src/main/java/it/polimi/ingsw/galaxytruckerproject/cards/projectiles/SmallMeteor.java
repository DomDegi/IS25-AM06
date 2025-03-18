package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;

import java.util.Optional;

public class SmallMeteor extends Projectile {
    public SmallMeteor(Direction direction) {
        super(direction);
    }

    public  Defense throwProjectile(Player player, int diceRoll) {
        Throw(player, diceRoll);
        ShipBoard ship = player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        if(coordinatesToDestroy.isEmpty()){
            return Defense.PROTECTED;
        }
        if(this.direction==Direction.NORTH){
            if(tileTable[coordinatesToDestroy.get().getY()][coordinatesToDestroy.get().getX()].get().getNorth().getConnectorsType()==Connectors.SMOOTH){
                return Defense.PROTECTED;
            }
            if(ship.getCoverageShields().contains(Coverage.NORTH_EAST)||ship.getCoverageShields().contains(Coverage.NORTH_WEST)){
                return Defense.CHOOSETOUSEBATTERY;
            }
            return Defense.HIT;
        }
        else if(this.direction==Direction.SOUTH){
            if(tileTable[coordinatesToDestroy.get().getY()][coordinatesToDestroy.get().getX()].get().getSouth().getConnectorsType()==Connectors.SMOOTH){
                return Defense.PROTECTED;
            }
            if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.SOUTH_WEST)){
                return Defense.CHOOSETOUSEBATTERY;
            }
            return Defense.HIT;

        }
        else if(this.direction==Direction.EAST){
            if(tileTable[coordinatesToDestroy.get().getY()][coordinatesToDestroy.get().getX()].get().getEast().getConnectorsType()==Connectors.SMOOTH){
                return Defense.PROTECTED;
            }
            if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.SOUTH_WEST)){
                return Defense.CHOOSETOUSEBATTERY;
            }
            return Defense.HIT;
        }
        else if(this.direction==Direction.WEST){
            if(tileTable[coordinatesToDestroy.get().getY()][coordinatesToDestroy.get().getX()].get().getWest().getConnectorsType()==Connectors.SMOOTH){

                return Defense.PROTECTED;
            }
            if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.SOUTH_WEST)){
                return Defense.CHOOSETOUSEBATTERY;
            }
            return Defense.HIT;
        }
        return Defense.PROTECTED;
    }




}