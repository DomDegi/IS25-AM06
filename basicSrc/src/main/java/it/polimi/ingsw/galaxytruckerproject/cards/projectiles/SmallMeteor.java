package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.tiles.VoidTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class SmallMeteor extends Meteor {
    public SmallMeteor(Direction direction) {
        super(direction);
    }
    public  Defense throwProjectile(Player player, int diceRoll) {
        ShipBoard ship= player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        int i;// num of iteration
        if(direction==Direction.SOUTH){
            if(diceRoll<4 ||diceRoll>10){
                return Defense.PROTECTED;
            }
            while(Temp.isEmpty() || (Temp.get() instanceof VoidTile))
        }
        if(direction==Direction.NORTH){
            if(diceRoll<4 ||diceRoll>10){
                return Defense.PROTECTED;
            }

        }
        if(direction==Direction.EAST){
            if(diceRoll<5 ||diceRoll>9){
                return Defense.PROTECTED;
            }

        }
        if(direction==Direction.WEST){
            if(diceRoll<5 ||diceRoll>9){
                return Defense.PROTECTED;
            }
        }



    }


}