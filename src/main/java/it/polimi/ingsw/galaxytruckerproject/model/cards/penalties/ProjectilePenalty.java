package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;
import java.util.Random;

public class ProjectilePenalty extends Penalty {
    private final ArrayList<Projectile> listOfProjectiles;
    private int diceRoll = 0;
    private Game game = null;
    private Defense defenseStatus = null;

    @JsonCreator
    public ProjectilePenalty(@JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        this.listOfProjectiles = listOfShots;
    }

    public ArrayList<Projectile> getListOfProjectiles() {
        return listOfProjectiles;
    }

    @Override
    public int applyPenalty(Game game, Player player, String[] input) {
        if (this.game == null) {
            this.game = game;
        }
        if (diceRoll == 0) {
            if (input!=null && input.length>0 && !input[0].equalsIgnoreCase("roll")) {
                printInfo(player);
                System.out.println("input roll to roll the dices and find out exactly what will be hit");
            } else {
                Random rand = new Random();
                diceRoll = 2 + rand.nextInt(11);
                System.out.println("diceRoll: " + diceRoll);
                printInfo(player);
            }
            return 0;
        }
        if (defenseStatus == Defense.PROTECTED) {
            resetForNextProjectile();

        } else if (defenseStatus == Defense.HIT) {
            playerGetsHit(player);

        } else if (defenseStatus == Defense.CHOOSETOUSEBATTERY) {
            switch (playerUsesBatteryToDefend(player, input)) {
                //there are no battery to be used: player gets hit and initialize next projectile
                case 0: {
                    playerGetsHit(player);
                    resetForNextProjectile();
                    break;
                }
                //player input is wrong
                case 1:  break;

                //player successfully used a battery to defend
                case 2: {
                    resetForNextProjectile();
                }
            }
        }
        if (listOfProjectiles.isEmpty()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString () {
        StringBuilder string = new StringBuilder("Projectile Penalty: ");
        for (Projectile projectile: listOfProjectiles) {
            string.append(projectile.toString()).append(" ");
        }
        return string.toString();
    }

    @Override
    //prints next cannon shot coming and sets the defense status for it (HIT, CHOOSESHIELD or PROTECTED)
    public void printInfo (Player player){
        System.out.println(listOfProjectiles.getFirst().toString() + "\n");
        if (diceRoll != 0) {
            this.defenseStatus = listOfProjectiles.getFirst().throwProjectile(player, diceRoll, game);
            System.out.println(defenseStatus);
        }
    }


    public void printInfoOnAllProjectiles() {
        for (Projectile projectile : listOfProjectiles) {
            System.out.println(projectile.toString() + "\n");
        }
    }

    public void playerGetsHit (Player player){
        player.getShipBoard().destroyTile(listOfProjectiles.getFirst().getCoordinatesToDestroy());
        resetForNextProjectile();
    }

    public void resetForNextProjectile () {
        this.listOfProjectiles.removeFirst();
        diceRoll = 0;
        defenseStatus = null;
    }
    
    public int playerUsesBatteryToDefend (Player player, String[]input){
        if (player.getShipBoard().getNumBatteries() < 1) {
            return 0;
        }
        ArrayList<Coordinates> batteryToUse = player.parseCoordinates(input);
        if (batteryToUse.isEmpty()) {
            return 1;
        }
        if (player.getShipBoard().chooseBatteryUse(batteryToUse.getFirst()))
            return 2;
        else
            return 1;
    }
        
    //these methods are needed for meteor swarm
    public void setDiceRoll ( int diceRoll){
        this.diceRoll = diceRoll;
    }

    public void addProjectile (Projectile projectile) {
        this.listOfProjectiles.add(projectile);
    }
}

