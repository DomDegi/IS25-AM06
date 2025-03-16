package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;

import java.util.Optional;
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

    //torna le coordinate della tile che VERREBBE colpita
    public Optional<Coordinates> Throw(Player player, int diceRoll){
        ShipBoard ship = player.getPlayerShip();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        int i;// num of iteration
        if(direction==Direction.SOUTH){
            if(diceRoll<4 ||diceRoll>10){
                return Optional.empty();
            }
            diceRoll = diceRoll-4;
            i=5;
            Temp= tileTable[i][diceRoll];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i--;
                if(i<0)
                    return Optional.empty();
                Temp= tileTable[i][diceRoll];
            }
            return Optional.of(Temp.get().getCoordinates());
        }
        if(direction==Direction.NORTH){
            if(diceRoll<4 ||diceRoll>10){
                return Optional.empty();
            }
            diceRoll = diceRoll-4;
            i=0;
            Temp= tileTable[i][diceRoll];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i++;
                if(i>5)
                    return Optional.empty();
                Temp= tileTable[i][diceRoll];
            }
            return Optional.of(Temp.get().getCoordinates());
        }

        if(direction==Direction.EAST){
            if(diceRoll<5 ||diceRoll>9){
                return Optional.empty();
            }
            diceRoll = diceRoll-5;
            i=6;
            Temp= tileTable[diceRoll][i];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i--;
                if(i<0)
                    return Optional.empty();
                Temp= tileTable[diceRoll][i];
            }
            return Optional.of(Temp.get().getCoordinates());
        }
        if(direction==Direction.WEST){
            if(diceRoll<5 ||diceRoll>9){
                return Optional.empty();
            }
            diceRoll = diceRoll-5;
            i=0;
            Temp= tileTable[diceRoll][i];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i++;
                if(i>6)
                    return Optional.empty();
                Temp= tileTable[diceRoll][i];
            }
            return Optional.of(Temp.get().getCoordinates());
        }
        return Optional.empty();



    }
}
}
