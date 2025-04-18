package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.util.Optional;

public class SmallMeteor extends Projectile {

    @JsonCreator
    public SmallMeteor(@JsonProperty("direction") Direction direction) {
        super(direction);
    }

    public  Defense throwProjectile(Player player, int diceRoll, GameInterface game) {
        Throw(player, diceRoll);

        if(coordinatesToDestroy.isEmpty()){
            return Defense.PROTECTED;
        }
        if(this.direction.equals(Direction.NORTH)){
            return checkNorth(player, diceRoll);
        }
        else if(this.direction.equals(Direction.SOUTH)){
            return checkSouth(player, diceRoll);
        }
        else if(this.direction.equals(Direction.EAST)){
            return checkEast(player, diceRoll);
        }
        else if(this.direction.equals(Direction.WEST)){
            return checkWest(player, diceRoll);
        }
        return Defense.PROTECTED;
    }

    /**
     *
     * @param player
     * @param diceRoll
     * @return
     */
    private Defense checkNorth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();

        if(ship.getTile(coordinatesToDestroy.get()).getNorth().getConnectorsType()== Connectors.SMOOTH){
            return Defense.PROTECTED;
        }
        if(ship.getCoverageShields().contains(Coverage.NORTH_EAST)||ship.getCoverageShields().contains(Coverage.NORTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }
    private Defense checkSouth(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        if(ship.getTile(coordinatesToDestroy.get()).getSouth().getConnectorsType()==Connectors.SMOOTH){
            return Defense.PROTECTED;
        }
        if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.SOUTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }
    private Defense checkEast(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        if(ship.getTile(coordinatesToDestroy.get()).getEast().getConnectorsType()==Connectors.SMOOTH){
            return Defense.PROTECTED;
        }
        if(ship.getCoverageShields().contains(Coverage.SOUTH_EAST)||ship.getCoverageShields().contains(Coverage.NORTH_EAST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;
    }
    private Defense checkWest(Player player, int diceRoll) {
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        if(ship.getTile(coordinatesToDestroy.get()).getWest().getConnectorsType()==Connectors.SMOOTH){

            return Defense.PROTECTED;
        }
        if(ship.getCoverageShields().contains(Coverage.SOUTH_WEST)||ship.getCoverageShields().contains(Coverage.NORTH_WEST)){
            return Defense.CHOOSETOUSEBATTERY;
        }
        return Defense.HIT;

    }

    @Override
    public String toString() {
        return "Small Meteor " + direction+ " ";
    }
}