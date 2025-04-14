package it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import java.util.Optional;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Direction;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LargeMeteor.class, name = "LargeMeteor"),
        @JsonSubTypes.Type(value = LargeCannonShot.class, name = "LargeCannonShot"),
        @JsonSubTypes.Type(value = SmallMeteor.class, name = "SmallMeteor"),
        @JsonSubTypes.Type(value = SmallCannonShot.class, name = "SmallCannonShot")
})

public abstract class Projectile {
    protected Direction direction;
    protected int diceRoll;
    protected Optional<Coordinates> coordinatesToDestroy = Optional.empty();


    public Projectile(Direction direction) {
        this.direction = direction;

    }

    public Direction getDirection() {
        return direction;
    }

    /**
     *
     * @param player
     * @param DiceRoll
     * @param game
     * @return
     */
    public Defense throwProjectile(Player player, int DiceRoll, GameInterface game) {
        return Defense.PROTECTED;
    }

    //returns the coordinates of the tile that would get hit
    public Coordinates getCoordinatesToDestroy() {
        return coordinatesToDestroy.orElseGet(() -> new Coordinates(0, 0));
    }

    /**
     *
     * @param player
     * @param diceRoll
     */
    protected void Throw(Player player, int diceRoll){
        ShipBoard ship = player.getShipBoard();
        Optional<Tile> Temp;
        Optional<Tile>[][] tileTable= ship.getTilesTable();
        int i;// num of iteration
        if(direction==Direction.SOUTH){
            if(diceRoll<4 ||diceRoll>10){
                coordinatesToDestroy=Optional.empty();
                return;
            }
            diceRoll = diceRoll-4;
            i=4;
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
                if(i>4) {
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
    }

    @Override
    public abstract String toString();
}