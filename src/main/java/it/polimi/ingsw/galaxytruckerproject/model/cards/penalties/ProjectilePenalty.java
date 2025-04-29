package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ProjectilePenalty extends Penalty {
    private final ArrayList<Projectile> listOfProjectiles;
    private int diceRoll = 0;
    private Defense defenseStatus = null;
    private ArrayList<Set<Coordinates>> branch;
    private Coordinates destroyedTile = null;

    @JsonCreator
    public ProjectilePenalty(@JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        this.listOfProjectiles = listOfShots;
    }

    public ArrayList<Projectile> getListOfProjectiles() {
        return listOfProjectiles;
    }

    @Override
    public String toString () {
        StringBuilder string = new StringBuilder("Projectile Penalty: ");
        for (Projectile projectile: listOfProjectiles) {
            string.append(projectile.toString()).append(" ");
        }
        return string.toString();
    }

    public Coordinates hitOrMiss(VirtualView view,Player player) {
        System.out.println(diceRoll);
        this.defenseStatus = listOfProjectiles.getFirst().throwProjectile(player, diceRoll, game);
        System.out.println(this.defenseStatus);

        if (!player.IsDisconnected()) {
            switch (defenseStatus) {
                case PROTECTED -> resetForNextProjectile();
                case HIT -> {
                    this.destroyedTile = playerGetsHit(player, view);
                    ArrayList<Coordinates> toRemove = new ArrayList<>();
                    toRemove.add(destroyedTile);
                    view.notifyBrokenTile(player.getPlayerName(), toRemove);
                    if (branch == null) {
                        resetForNextProjectile();
                        return destroyedTile;
                    } else {
                        try {
                            view.asksToInputCoordinates(CoordReqType.CHOOSE_TO_MAINTAIN);
                        }catch(Exception ignored) {}
                    }
                    System.out.println('d');
                }
                case CHOOSETOUSEBATTERY -> {
                    if (player.getShipBoard().getNumBatteries() == 0) {
                        playerGetsHit(player, view);
                    } else {
                        try {
                            view.asksToInputCoordinates(CoordReqType.CHOOSE_BATTERY);
                        }catch(Exception ignored) {}
                    }
                }
            }
        }
        return null;
    }


    //asks the player to roll the dices
    @Override
    public boolean initializePenalty(GameInterface game, VirtualView view, Player player) {
        this.defenseStatus = null;
        if (this.game == null) {
            this.game = game;
        }

        if (player.IsDisconnected()) {
            game.getDrawnCard().notifyBrokenTiles(player.getPlayerName(), automaticProjectilePenalty(player, view));
            return false;
        }

        if (getListOfProjectiles().isEmpty()) {
            return false;
        }
        System.out.println(diceRoll);
        if (this.diceRoll != 0) {
            hitOrMiss(view, player);
            System.out.println("gogo");
            System.out.println(defenseStatus);

            //RETURNS TRUE IF THE SHIPBOARD GETS HIT,
            // FALSE OTHERWISE.
            return (defenseStatus != Defense.PROTECTED);

        }
        else {
            try {
                view.setClientState(ClientState.ROLL_DICE);
            }catch(Exception ignored) {}
        }
        return true;
    }


    public void printInfoOnAllProjectiles() {
        for (Projectile projectile : listOfProjectiles) {
            System.out.println(projectile.toString() + "\n");
        }
    }

    public Coordinates playerGetsHit (Player player, ViewInterface view) {
        Coordinates toDestroy = this.getBrokenTile();
        this.branch = player.getShipBoard().destroyTile(toDestroy);
        return toDestroy;
    }

    public void resetForNextProjectile () {
        this.listOfProjectiles.removeFirst();
        diceRoll = 0;
        //defenseStatus = null;
        branch = null;
        destroyedTile = null;
    }

    public Coordinates getBrokenTile () {
        return listOfProjectiles.getFirst().getCoordinatesToDestroy();
    }

    //modificato!!!!!!!!
    @Override
    public Coordinates randomRollForOne (VirtualView view, Player player) {
        //this.game = game;
        Random rand = new Random();
        this.diceRoll = 2 + rand.nextInt(11);

        return hitOrMiss(view, player);
    }



    @Override
    public ArrayList<Coordinates> chooseToMaintain(Player player, ArrayList<Coordinates> received) {
        int i = 0;
        ArrayList<Coordinates> toRemove = new ArrayList<>();

        while (branch.size()>1 && i==0) {
            for (Set<Coordinates> set : branch) {
                if (set.contains(received.getFirst())) {
                    player.getShipBoard().SetNewShip(set);
                    i=1;
                    branch.remove(set);
                }
            }
        }
        if (i == 1) {
            for (Set<Coordinates> set : branch) {
                toRemove.addAll(set);
            }
            toRemove.add(destroyedTile);
            resetForNextProjectile();
            return toRemove;
        }
        else {
            return null;
        }
    }

    @Override
    public Tile playerUsesBatteryToDefend(Player player, ArrayList<Coordinates> batteries) {
        if (player.getShipBoard().getNumBatteries() < 1) {
            //shouldn't happen
            return null;
        }
        if (batteries.isEmpty()) {
            return null;
        }
        if (player.getShipBoard().chooseBatteryUse(batteries.getFirst())) {
            resetForNextProjectile();
            return player.getShipBoard().getTile(batteries.getFirst());
        }
        else
            return null;
    }
        
    //these methods are needed for meteor swarm
    public void setDiceRoll ( int diceRoll){
        this.diceRoll = diceRoll;
    }

    @Override
    public int getDiceRoll(){
        return diceRoll;
    }

    public void addProjectile (Projectile projectile) {
        this.listOfProjectiles.add(projectile);
    }

    public ArrayList<Coordinates> automaticProjectilePenalty(Player player, VirtualView view) {
        ArrayList<Coordinates> startingCabinBranch = new ArrayList<>();
        startingCabinBranch.add(new Coordinates(2, 3));
        ArrayList<Coordinates> firstBranchCoordinates = new ArrayList<>();
        ArrayList<Coordinates> totalRemovedTiles = new ArrayList<>();
        ArrayList<Coordinates> removedTiles;

        while (!listOfProjectiles.isEmpty()) {
            if (this.diceRoll == 0) {
                randomRollForOne(view, player);
            }
            if (defenseStatus == Defense.HIT || defenseStatus == Defense.CHOOSETOUSEBATTERY) {
                // if true: kept the branch with the starting cabin, else kept the first branch
                if (branch != null){
                    removedTiles = chooseToMaintain(player, startingCabinBranch);
                    if (removedTiles != null) {
                        totalRemovedTiles.addAll(removedTiles);
                    }
                    else {
                        firstBranchCoordinates.addAll(branch.getFirst());
                        removedTiles = chooseToMaintain(player, firstBranchCoordinates);
                        totalRemovedTiles.addAll(removedTiles);
                    }
                }
                else {
                    totalRemovedTiles.add(destroyedTile);
                }
            }
            else {
                resetForNextProjectile();
            }
        }
        return totalRemovedTiles;
    }
}

