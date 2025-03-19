package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;
import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.player.Player;
import it.polimi.ingsw.galaxytruckerproject.tiles.*;
import java.util.Optional;
import it.polimi.ingsw.galaxytruckerproject.tiles.Direction;
import java.util.Random;
/*
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Meteor.class, name = "Meteor"),
        @JsonSubTypes.Type(value = CannonShot.class, name = "CannonShot")
})
*/
public abstract class Projectile {
    protected Direction direction;
    protected final Game game;
    protected int diceRoll;
    protected Optional<Coordinates> coordinatesToDestroy;


    public Projectile(Game game, Direction direction) {
        this.direction = direction;
            this.game = game;
    }
    public Direction getDirection() {
        return direction;
    }

    public Defense throwProjectile(Player player, int DiceRoll) {
        return Defense.PROTECTED;
    }

    //torna le coordinate della tile che VERREBBE colpita
    public Coordinates getCoordinatesToDestroy() {
        return coordinatesToDestroy.get();
    }

    protected void Throw(Player player, int diceRoll){
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp = Optional.empty();
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        int i;// num of iteration
        if(direction==Direction.SOUTH){
            if(diceRoll<4 ||diceRoll>10){
                coordinatesToDestroy=Optional.empty();
                return;
            }
            diceRoll = diceRoll-4;
            i=5;
            Temp= tileTable[i][diceRoll];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i--;
                if(i<0) {
                    coordinatesToDestroy=Optional.empty();
                    return;
                }
                Temp= tileTable[i][diceRoll];
            }
            coordinatesToDestroy= Optional.of(Temp.get().getCoordinates());
            return;
        }
        if(direction==Direction.NORTH){
            if(diceRoll<4 ||diceRoll>10){
                coordinatesToDestroy=Optional.empty();
                return;
            }
            diceRoll = diceRoll-4;
            i=0;
            Temp= tileTable[i][diceRoll];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i++;
                if(i>5) {
                    coordinatesToDestroy=Optional.empty();
                    return;
                }
                Temp= tileTable[i][diceRoll];
            }
            coordinatesToDestroy= Optional.of(Temp.get().getCoordinates());
            return;
        }

        if(direction==Direction.EAST){
            if(diceRoll<5 ||diceRoll>9){
                coordinatesToDestroy=Optional.empty();
                return;
            }
            diceRoll = diceRoll-5;
            i=6;
            Temp= tileTable[diceRoll][i];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i--;
                if(i<0) {
                    coordinatesToDestroy=Optional.empty();
                    return;
                }
                Temp= tileTable[diceRoll][i];
            }
            coordinatesToDestroy= Optional.of(Temp.get().getCoordinates());
            return;
        }
        if(direction==Direction.WEST){
            if(diceRoll<5 ||diceRoll>9){
                coordinatesToDestroy=Optional.empty();
                return ;
            }
            diceRoll = diceRoll-5;
            i=0;
            Temp= tileTable[diceRoll][i];
            while(Temp.isEmpty() || !Temp.get().fillable())
            {
                i++;
                if(i>6) {
                    coordinatesToDestroy = Optional.empty();
                    return;
                }
                Temp= tileTable[diceRoll][i];
            }
            coordinatesToDestroy= Optional.of(Temp.get().getCoordinates());
            return;
        }
        coordinatesToDestroy= Optional.empty();
        return;



    }
}