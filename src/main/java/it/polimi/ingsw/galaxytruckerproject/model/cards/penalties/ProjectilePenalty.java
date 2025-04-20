package it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Defense;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.GenericMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.SendCoordinatesResponse;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ProjectilePenalty extends Penalty {
    private final ArrayList<Projectile> listOfProjectiles;
    private int diceRoll = 0;
    private GameInterface game = null;
    private Defense defenseStatus = null;
    private ArrayList<Set<Coordinates>> branch = null;

    @JsonCreator
    public ProjectilePenalty(@JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        this.listOfProjectiles = listOfShots;
    }

    public ArrayList<Projectile> getListOfProjectiles() {
        return listOfProjectiles;
    }

    @Override
    public int applyPenalty(GameInterface game, Player player, ViewInterface playersView, Message message) {
        if (this.game == null) {
            this.game = game;
        }
        if (diceRoll == 0) {
            if (message.getMessageType().equals(MessageType.ROLL_THE_DICES_REQUEST)) {
                initializePenalty(playersView, player);
                playersView.showGenericMessage("input roll to roll the dices and find out exactly where you will be hit");
            } else {
                this.randomRoll();
                playersView.showGenericMessage("diceRoll: " + diceRoll);
                initializePenalty(playersView, player);
            }
            //return 0;
        } else {
            initializePenalty(playersView, player);
        }
        if (defenseStatus == Defense.PROTECTED) {
            resetForNextProjectile();

        } else if (defenseStatus == Defense.HIT) {
            System.out.println("hit");
            playerGetsHit(player, playersView, message);

        } else if (defenseStatus == Defense.CHOOSETOUSEBATTERY) {
            switch (playerUsesBatteryToDefend(player, playersView, message)) {
                //there are no battery to be used: player gets hit and initialize next projectile
                case 0: {
                    playerGetsHit(player, playersView, message);
                    resetForNextProjectile();
                    break;
                }
                //player input is wrong
                case 1: {
                    System.out.println("wrong input");
                    break;
                }
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

    //asks the player to roll the dices
    @Override
    public boolean initializePenalty(VirtualView view, Player player) {
        view.setClientState(ClientState.ACTION);
        return true;
    }



    //prints next cannon shot coming and sets the defense status for it (HIT, CHOOSESHIELD or PROTECTED)
    public boolean hitMissOrShield(ViewInterface view, Player player){
        if (diceRoll != 0) {
            this.defenseStatus = listOfProjectiles.getFirst().throwProjectile(player, diceRoll, game);
            if (defenseStatus == Defense.PROTECTED) {
                view.showGenericMessage("Protected from projectile");
                resetForNextProjectile();
                return true;
            }
            else if (defenseStatus == Defense.CHOOSETOUSEBATTERY) {
                if (player.getShipBoard().getNumBatteries() == 0) {
                    this.defenseStatus = Defense.HIT;
                    playerGetsHit(player, view, new GenericMessage("w/e"));
                    view.asksWhichBranchToKeep(this.branch);
                }
                view.asksToUseBatteries();
            }
            else if  (defenseStatus == Defense.HIT) {
                if (!playerGetsHit(player, view, new GenericMessage("w/e"))) {
                    view.asksWhichBranchToKeep(this.branch);
                }
                else {
                    resetForNextProjectile();
                    return this.applyPenalty(game, player, view, new GenericMessage("w/e")) != 1;
                }
            }
        }
        return true;
    }


    public void printInfoOnAllProjectiles() {
        for (Projectile projectile : listOfProjectiles) {
            System.out.println(projectile.toString() + "\n");
        }
    }

    public boolean playerGetsHit (Player player, ViewInterface view, Message message) {
        if (this.branch == null) {
            this.branch = player.getShipBoard().destroyTile(listOfProjectiles.getFirst().getCoordinatesToDestroy());
        }
        if (!message.getMessageType().equals(MessageType.SEND_COORDINATES_RESPONSE) && branch.size() > 1) {
            return false;
        }
        SendCoordinatesResponse received =  (SendCoordinatesResponse) message;
        int i = 0;
        while (branch.size()>1 && i==0) {
            for (Set<Coordinates> set : branch) {
                if (set.contains(received.getFirst())) {
                    player.getShipBoard().SetNewShip(set);
                    i=1;
                }
            }
        }
        if (i == 1 || branch.size() == 1) {
            resetForNextProjectile();
            return true;
        }
        return false;
    }

    public void resetForNextProjectile () {
        this.listOfProjectiles.removeFirst();
        diceRoll = 0;
        defenseStatus = null;
        branch = null;
    }
    
    public int playerUsesBatteryToDefend (Player player, ViewInterface view, Message message) {
        if (player.getShipBoard().getNumBatteries() < 1) {
            return 0;
        }
        if (!message.getMessageType().equals(MessageType.SEND_COORDINATES_RESPONSE)) {
            return 1;
        }
        SendCoordinatesResponse received =  (SendCoordinatesResponse) message;
        ArrayList<Coordinates> batteryToUse = received.getCoordinates();
        if (batteryToUse.isEmpty()) {
            return 1;
        }
        if (player.getShipBoard().chooseBatteryUse(batteryToUse.getFirst()))
            return 2;
        else
            return 1;
    }

    public void randomRollForOne (VirtualView view, Player player) {
        Random rand = new Random();
        this.diceRoll = 2 + rand.nextInt(11);
        hitMissOrShield(view, player);
    }

        
    //these methods are needed for meteor swarm
    public void setDiceRoll ( int diceRoll){
        this.diceRoll = diceRoll;
    }

    public void addProjectile (Projectile projectile) {
        this.listOfProjectiles.add(projectile);
    }

    public void automaticProjectilePenalty(GameInterface game, Player player, ViewInterface view) {
        ArrayList<Coordinates> startingCabinBranch = new ArrayList<>();
        startingCabinBranch.add(new Coordinates(2, 3));
        ArrayList<Coordinates> firstBranchCoordinates = new ArrayList<>();
        while (!listOfProjectiles.isEmpty()) {
            this.randomRoll();
            this.defenseStatus = listOfProjectiles.getFirst().throwProjectile(player, diceRoll, game);
            if (defenseStatus == Defense.HIT || defenseStatus == Defense.CHOOSETOUSEBATTERY) {
                // if true: kept the branch with the starting cabin, else kept the first branch
                if (! playerGetsHit(player, view, new SendCoordinatesResponse(player.getPlayerName(), startingCabinBranch)))  {
                    firstBranchCoordinates.addAll(branch.getFirst());
                    playerGetsHit(player, view, new SendCoordinatesResponse(player.getPlayerName(),firstBranchCoordinates));
                }
            }
            else {
                resetForNextProjectile();
            }
        }
    }
}

