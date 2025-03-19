package it.polimi.ingsw.galaxytruckerproject.cards.penalties;

import it.polimi.ingsw.galaxytruckerproject.Game;
import it.polimi.ingsw.galaxytruckerproject.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class CannonPenalty extends Penalty {

    private final ArrayList<Projectile> listOfShots;

    public CannonPenalty(ArrayList<Projectile> listOfShots) {
        this.listOfShots = listOfShots;
    }

    public ArrayList<Projectile> getListOfShots() {
        return listOfShots;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input){

    }

    @Override
    public String toString() {
        return "CannonPenalty";
    }

    @Override
    public void printInfo(Player player) {
        System.out.println(listOfShots.getFirst().toString() + "\n");
    }

    public void printInfoOnALLProjectiles(){
        for (Projectile projectile : listOfShots){
            System.out.println(projectile.toString() + "\n");
        }
    }
}
