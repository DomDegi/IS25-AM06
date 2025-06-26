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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Represents a penalty involving one or more projectile impacts on a player's ship.
 * This class handles the logic for determining whether a projectile hits or is defended
 * against, updates the ship state accordingly, and notifies the player.
 */
public class ProjectilePenalty extends Penalty {
    private final ArrayList<Projectile> listOfProjectiles;
    private int diceRoll = -1;
    private Defense defenseStatus = null;
    private ArrayList<Set<Coordinates>> branch;
    private Coordinates destroyedTile = null;
    private int previousDiceRoll = 0;

    /**
     * Constructs a ProjectilePenalty with the given list of projectiles.
     *
     * @param listOfShots the list of projectiles to be processed
     */
    @JsonCreator
    public ProjectilePenalty(@JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        this.listOfProjectiles = listOfShots;
    }

    /**
     * Returns the list of projectiles in this penalty.
     *
     * @return the list of projectiles
     */
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

    /**
     * Applies a projectile impact to the given player and returns the destroyed tile if any.
     *
     * @param view   the virtual view used for notifications
     * @param player the player being targeted
     * @return the coordinates of the destroyed tile or null if no damage
     */
    @Override
    public Coordinates hitOrMiss(VirtualView view, Player player) {
        if (defenseStatus == null) {
            this.defenseStatus = listOfProjectiles.getFirst().throwProjectile(player, diceRoll, game);
        }

        if (!player.IsDisconnected()) {
            switch (defenseStatus) {
                case PROTECTED -> resetForNextProjectile();
                case HIT -> {
                    this.destroyedTile = playerGetsHit(player, view);
                    ArrayList<Coordinates> toRemove = new ArrayList<>();
                    toRemove.add(destroyedTile);
                    try {
                        view.notifyBrokenTile(player.getPlayerName(), toRemove);
                    } catch (java.rmi.RemoteException e) {
                    }
                    if (branch == null) {
                        resetForNextProjectile();
                        return destroyedTile;
                    } else {
                        try {
                            view.asksToInputCoordinates(CoordReqType.CHOOSE_TO_MAINTAIN);
                        }catch(Exception ignored) {}
                    }
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
        else if (defenseStatus == Defense.HIT || defenseStatus == Defense.CHOOSETOUSEBATTERY) {
            destroyedTile = playerGetsHit(player, view);
            return destroyedTile;
        }
        return null;
    }

    /**
     * Initializes the penalty and requests dice input from the player, if necessary.
     *
     * @param game   the game context
     * @param view   the virtual view used for communication
     * @param player the player subject to the penalty
     * @return true if further interaction is needed, false otherwise
     */
    @Override
    public boolean initializePenalty(GameInterface game, VirtualView view, Player player) {
        this.defenseStatus = null;
        if (this.game == null) {
            this.game = game;
        }

        if (listOfProjectiles.isEmpty()) {
            return false;
        }

        if (player.IsDisconnected()) {
            game.getDrawnCard().notifyBrokenTiles(player.getPlayerName(), automaticProjectilePenalty(player, view));
            return false;
        }
        if (this.diceRoll != -1) {
            System.out.println("Per qualche motivo sono qua!");
            Coordinates destroyed = hitOrMiss(view, player);
            if (destroyed != null) {
                return false;
            }
            return defenseStatus != Defense.PROTECTED;
        }
        else {
            try {
                view.setClientState(ClientState.ROLL_DICE);
            }catch(Exception ignored) {}
        }
        return true;
    }

    /**
     * Prints detailed information about all projectiles in the penalty.
     */
    public void printInfoOnAllProjectiles() {
        for (Projectile projectile : listOfProjectiles) {
            System.out.println(projectile.toString() + "\n");
        }
    }

    /**
     * Destroys the specified tile on the player's ship and handles early landing if needed.
     *
     * @param player the player affected
     * @param view   the interface to notify about early landing
     * @return the coordinates of the destroyed tile
     */
    public Coordinates playerGetsHit (Player player, ViewInterface view) {
        Coordinates toDestroy = this.getBrokenTile();
        ArrayList<Set<Coordinates>> returned = player.getShipBoard().destroyTile(toDestroy);
        if (!returned.isEmpty()) {
            this.branch = returned;
        }
        else{
            try {
                view.notifyEarlyLanding();
            } catch (RemoteException e) {
            }
            game.getFlightBoard().earlyLanding(player);
        }
        return toDestroy;
    }

    /**
     * Prepares the penalty object for processing the next projectile.
     */
    public void resetForNextProjectile () {
        this.listOfProjectiles.removeFirst();
        diceRoll = -1;
        branch = null;
        destroyedTile = null;
    }

    /**
     * Returns the coordinates of the tile targeted by the next projectile.
     *
     * @return the coordinates of the tile
     */
    public Coordinates getBrokenTile () {
        return listOfProjectiles.getFirst().getCoordinatesToDestroy();
    }

    /**
     * Performs a random dice roll and applies the projectile impact.
     *
     * @param view   the virtual view
     * @param player the player being attacked
     * @return the destroyed tile coordinates or null
     */
    @Override
    public Coordinates randomRollForOne (VirtualView view, Player player) {
        Random rand = new Random();
        this.diceRoll = 2 + rand.nextInt(11);
        this.previousDiceRoll = diceRoll;
        return hitOrMiss(view, player);
    }

    /**
     * Allows the player to choose which disconnected branch of the ship to maintain.
     *
     * @param player   the player affected
     * @param received the coordinates indicating the branch to keep
     * @return the list of coordinates to remove
     */
    @Override
    public ArrayList<Coordinates> chooseToMaintain(Player player, ArrayList<Coordinates> received) {
        boolean foundBranch = false;
        ArrayList<Coordinates> toRemove = new ArrayList<>();
        ArrayList <Set<Coordinates>> b2 = new ArrayList<Set<Coordinates>>();
        for (Set<Coordinates> set : branch) {
            if (set.contains(received.getFirst())) {
                player.getShipBoard().SetNewShip(set);
                foundBranch = true;
                b2.add(set);
            }
        }
        for (Set<Coordinates> set : b2) {
            branch.remove(set);
        }
        if (foundBranch) {
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

    /**
     * Allows the player to use a battery to defend against a projectile.
     *
     * @param player    the player defending
     * @param batteries the list of battery coordinates
     * @return the tile used to defend, or null if defense failed
     */
    @Override
    public Tile playerUsesBatteryToDefend(Player player, ArrayList<Coordinates> batteries) {
        if (player.getShipBoard().getNumBatteries() < 1) {
            return null;
        }
        if (batteries.isEmpty()) {
            this.defenseStatus = Defense.HIT;
            return null;
        }
        if (player.getShipBoard().chooseBatteryUse(batteries.getFirst())) {
            resetForNextProjectile();
            return player.getShipBoard().getTile(batteries.getFirst());
        }
        else
            return null;
    }

    /**
     * Sets the dice roll result manually.
     *
     * @param diceRoll the dice roll result to set
     */
    public void setDiceRoll ( int diceRoll){
        this.diceRoll = diceRoll;
    }

    /**
     * Returns the dice roll result.
     *
     * @return the dice roll value
     */
    @Override
    public int getDiceRoll(){
        return previousDiceRoll;
    }

    /**
     * Adds a new projectile to the penalty queue.
     *
     * @param projectile the projectile to add
     */
    public void addProjectile (Projectile projectile) {
        this.listOfProjectiles.add(projectile);
    }

    /**
     * Applies the entire penalty automatically, used in cases like disconnected players.
     *
     * @param player the player affected
     * @param view   the virtual view
     * @return list of coordinates of all destroyed tiles
     */
    public ArrayList<Coordinates> automaticProjectilePenalty(Player player, VirtualView view) {
        ArrayList<Coordinates> firstBranchCoordinates = new ArrayList<>();
        ArrayList<Coordinates> totalRemovedTiles = new ArrayList<>();
        ArrayList<Coordinates> removedTiles = new ArrayList<>();

        while (!listOfProjectiles.isEmpty()) {
            if (this.diceRoll == -1) {
                randomRollForOne(view, player);
            }
            if (defenseStatus == Defense.HIT || defenseStatus == Defense.CHOOSETOUSEBATTERY) {
                if (branch.size() > 1){
                    firstBranchCoordinates.clear();
                    boolean foundCabin = false;

                    for (Set<Coordinates> set : branch) {
                        if (set.contains(new Coordinates(2, 3))) {
                            firstBranchCoordinates.addAll(set);
                            foundCabin = true;
                            break;
                        }
                    }
                    if (!foundCabin && !branch.isEmpty()) {
                        firstBranchCoordinates.addAll(branch.getFirst());
                    }

                    removedTiles = chooseToMaintain(player, firstBranchCoordinates);
                    totalRemovedTiles.addAll(removedTiles);
                    resetForNextProjectile();
                }
                else {
                    totalRemovedTiles.add(destroyedTile);
                    resetForNextProjectile();
                }
                defenseStatus = null;
            }
            else {
                defenseStatus = null;
                resetForNextProjectile();
            }
        }

        return totalRemovedTiles;
    }

    /**
     * Returns the current defense status.
     *
     * @return the defense result
     */
    @Override
    public Defense getDefenseStatus() {
        return defenseStatus;
    }

    /**
     * Returns the current branches resulting from tile destruction.
     *
     * @return the list of disconnected ship branches
     */
    @Override
    public ArrayList<Set<Coordinates>>  getBranch() {
        return branch;
    }

    /**
     * Returns the coordinates of the most recently destroyed tile.
     *
     * @return the coordinates of the destroyed tile
     */
    @Override
    public Coordinates getDestroyedTile() {
        return destroyedTile;
    }
}
