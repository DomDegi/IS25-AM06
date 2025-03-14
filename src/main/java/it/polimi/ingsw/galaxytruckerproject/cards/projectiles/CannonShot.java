package it.polimi.ingsw.galaxytruckerproject.cards.projectiles;

import it.polimi.ingsw.galaxytruckerproject.Player;

public class CannonShot extends Projectile {
    private final Size size;

    public CannonShot(Direction direction, Size size){
        super(direction);
        this.size = size;
    }

    public Size getSize(){
        return size;
    }

    @Override
    public Direction getDirection() {
        return super.getDirection();
    }

    //generates a random number from 2 to 12 (simulates 2 dice roll)
    @Override
    public int rollTheDices() {
        return super.rollTheDices();
    }

    public void throwCannonShot(Player penalizedPlayer){

        diceRoll = rollTheDices();
        //Calls defendFromProjectile on the player that lost in CombatZone or against Pirates
        penalizedPlayer.defendFromProjectile(this);
    }
}
