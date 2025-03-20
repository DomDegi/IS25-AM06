package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.GameMode;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.Connectors;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.tiles.Direction;
import java.util.ArrayList;
import java.util.Optional;

public class LargeMeteor extends Projectile {
    public LargeMeteor(Direction direction) {

        super(game, direction);
    }


    public Defense throwProjectile(Game game, Player player,int diceRoll) {
        Throw(player,diceRoll);

        if(coordinatesToDestroy.isEmpty()){
            //miss
            return Defense.PROTECTED;
        }
        else if(game.getMode().equals(GameMode.TRIAL))
        {
            if(direction.equals(Direction.NORTH))
                return checkNorthLev1(player,diceRoll);
            if(direction.equals(Direction.SOUTH))
                return checkSouthLev1(player,diceRoll);
            if (direction.equals(Direction.EAST))
                return checkEastLev1(player,diceRoll);
            if(direction.equals(Direction.WEST))
                return checkWestLev1(player,diceRoll);
        }
        if(direction.equals(Direction.NORTH))
        {
            return checkNorthLev1(player,diceRoll);
        }
        if(direction.equals(Direction.SOUTH))
        {
            //same between lv1 e lv2
            return checkSouthLev1(player,diceRoll);
        }
        if(direction.equals(Direction.EAST))
        {
            return checkEastLev2(player,diceRoll);
        }
        return checkWestLev2(player,diceRoll);
    }


    private Defense checkNorthLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        boolean doubleCannon = false;
        int i = 0;// num of iteration
        diceRoll = diceRoll-4;
        while(i<=5) {
            Temp = tileTable[i][diceRoll];
            if (Temp.isPresent() && Temp.get().fillable()) {
                if (Temp.get().getStrength() == 2)
                    doubleCannon = true;
                else if (Temp.get().getStrength()==-2){
                    return Defense.PROTECTED;
                }
            }
            i++;
        }
        if(doubleCannon)
            return Defense.CHOOSETOUSEBATTERY;
        return Defense.HIT;
    }
    private Defense checkSouthLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        boolean doubleCannon = false;
        int i = 5;// num of iteration
        diceRoll = diceRoll-4;
        while(i>=0) {
            Temp = tileTable[i][diceRoll];
            if (Temp.isPresent() && Temp.get().fillable()) {
                if (Temp.get().getStrength() == 1 && Temp.get().getDirection()== Direction.SOUTH)
                    doubleCannon = true;
                else if (Temp.get().getStrength()==-1 && Temp.get().getDirection()== Direction.SOUTH){
                    return Defense.PROTECTED;
                }
            }
            i--;
        }
        if(doubleCannon)
            return Defense.CHOOSETOUSEBATTERY;
        return Defense.HIT;
    }
    private Defense checkEastLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        boolean doubleCannon = false;
        int i = 6;// num of iteration
        diceRoll = diceRoll-4;
        while(i>=0) {
            Temp = tileTable[diceRoll][i];
            if (Temp.isPresent() && Temp.get().fillable()) {
                if (Temp.get().getStrength() == 1 && Temp.get().getDirection()== Direction.EAST)
                    doubleCannon = true;
                else if (Temp.get().getStrength()==-1 && Temp.get().getDirection()== Direction.EAST){
                    return Defense.PROTECTED;
                }
            }
            i--;
        }
        if(doubleCannon)
            return Defense.CHOOSETOUSEBATTERY;
        return Defense.HIT;
    }
    private Defense checkWestLev1(Player player, int diceRoll) {
        ShipBoard ship = player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        boolean doubleCannon = false;
        int i = 0;// num of iteration
        diceRoll = diceRoll-4;
        while(i<=6) {
            Temp = tileTable[i][diceRoll];
            if (Temp.isPresent() && Temp.get().fillable()) {
                if (Temp.get().getStrength() == 1 && Temp.get().getDirection()== Direction.WEST)
                    doubleCannon = true;
                else if (Temp.get().getStrength()==-1 && Temp.get().getDirection()== Direction.WEST){
                    return Defense.PROTECTED;
                }
            }
            i++;
        }
        if(doubleCannon)
            return Defense.CHOOSETOUSEBATTERY;
        return Defense.HIT;

    }

    /*private Defense checkNorthLev2(Player player, int diceRoll) {
        Defense defense = Defense.HIT;
        Defense temp = Defense.HIT;
        temp = checkNorthLev1(player,diceRoll);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        temp = checkNorthLev1(player,diceRoll+1);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        temp = checkNorthLev1(player,diceRoll-1);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        return defense;
    }*/
    private Defense checkEastLev2(Player player, int diceRoll) {
        Defense defense = Defense.HIT;
        Defense temp = Defense.HIT;
        temp = checkEastLev1(player,diceRoll);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        temp = checkEastLev1(player,diceRoll+1);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        temp = checkEastLev1(player,diceRoll-1);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        return defense;
    }
    private Defense checkWestLev2(Player player, int diceRoll) {
        Defense defense = Defense.HIT;
        Defense temp = Defense.HIT;
        temp = checkWestLev1(player,diceRoll);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        temp = checkWestLev1(player,diceRoll+1);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        temp = checkWestLev1(player,diceRoll-1);
        if(temp==Defense.PROTECTED)
            return temp;
        if(temp==Defense.CHOOSETOUSEBATTERY)
            defense = temp;
        return defense;
    }
    /* private Defense checkSouthLev2(Player player, int diceRoll) {
            Defense defense = Defense.HIT;
            Defense temp = Defense.HIT;
            temp = checkSouthLev1(player,diceRoll);
            if(temp==Defense.PROTECTED)
                return temp;
            if(temp==Defense.CHOOSETOUSEBATTERY)
                defense = temp;
            temp = checkSouthLev1(player,diceRoll+1);
            if(temp==Defense.PROTECTED)
                return temp;
            if(temp==Defense.CHOOSETOUSEBATTERY)
                defense = temp;
            temp = checkSouthLev1(player,diceRoll-1);
            if(temp==Defense.PROTECTED)
                return temp;
            if(temp==Defense.CHOOSETOUSEBATTERY)
                defense = temp;
            return defense;
        }*/


    @Override
    public Direction getDirection() {
        return super.getDirection();
    }
}