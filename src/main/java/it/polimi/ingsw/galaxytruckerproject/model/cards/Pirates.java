package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

public class Pirates extends Enemies {
    private final ArrayList<Projectile> listOfCannonShots;
    private final int rewardCredits;
    private int playerIndex = -1;
    private Player currentPlayer = null;
    private VirtualView currentView = null;
    private ProjectilePenalty penaltyIfLose;
    private int won = 0;

    @JsonCreator
    public Pirates(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("listOfShots") ArrayList<Projectile> listOfShots) {
        super(level, requiredDays, cannonStrength);  // Chiamata al costruttore della classe base
        this.rewardCredits = rewardCredits;
        this.listOfCannonShots = listOfShots;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Pirates: ").append(super.toString()).append("rewardCredits: ").append(rewardCredits).append(" ");
        for (Projectile projectile: listOfCannonShots) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }
    

    public void nextPlayer() {
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = currentPlayer.getPlayerName();
        this.currentView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
        if (singleCannonPower > 0) {
            singleCannonPower = singleCannonPower + currentPlayer.getShipBoard().getNumPurpleAliens()*2;
        }
        penaltyIfLose = new ProjectilePenalty(listOfCannonShots);
        won = 0;
        if (currentPlayer.IsDisconnected()) {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            }
            else if (singleCannonPower == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                if (penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                    return;
                }
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                try {
                    currentView.setClientState(ClientState.ACTION);
                }catch(Exception ignored) {}
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getNumBatteries() == 0) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    penaltyIfLose.initializePenalty(game,currentView, currentPlayer);
                }
            }
            else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                }catch(Exception ignored) {}
            }
        }
    }

    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won != 1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        if (decision) {
            currentPlayer.gainCredit(rewardCredits);
            notifyGainedCredits(currentPlayer.getPlayerName(), currentPlayer.getCredit());
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
            game.endCardEvent();
        }
        else {
            game.endCardEvent();
        }
    }

    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        Map<Float,ArrayList<Tile>> returned = player.useCannons(doubleCannonPower, batteriesToUse);
        if (returned == null) {
            try {
                currentView.showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        notifyModifiedTiles(playerName, returned.values().iterator().next());
        float cannonPower = returned.keySet().iterator().next();
        if (cannonPower > cannonStrength) {
            won = 1;
            if (currentPlayer.IsDisconnected()) {
                choice(playerName, false);
            }
            else{
                try {
                    currentView.setClientState(ClientState.ACTION);
                }catch(Exception ignored) {}
            }
        }
        else if (cannonPower == cannonStrength) {
            nextPlayer();
        }
        else {
            won = -1;
            if(!penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
                nextPlayer();
            }
            try {
                currentView.setClientState(ClientState.ROLL_DICE);
            }catch(Exception ignored) {}
        }
    }

    @Override
    public void rollTheDices(String playerName) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || penaltyIfLose.getDiceRoll() != -1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> broken =  new ArrayList<>();
        Coordinates firstBrokenTile = penaltyIfLose.randomRollForOne(currentView, currentPlayer);
        try {
            currentView.showDiceRoll(penaltyIfLose.getDiceRoll());
        } catch(Exception ignored) {}
        if (firstBrokenTile != null) {
            broken.add(firstBrokenTile);
            notifyBrokenTiles(playerName, broken);
        }
        if (!penaltyIfLose.initializePenalty(game, currentView, currentPlayer)) {
            nextPlayer();
        }
    }

    @Override
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> removedTiles = penaltyIfLose.chooseToMaintain(player, branchChoices);
        if (removedTiles == null) {
            try {
                currentView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
        else {
            notifyBrokenTiles(playerName, removedTiles);
            if (!penaltyIfLose.initializePenalty(game,currentView, currentPlayer)) {
                nextPlayer();
            }
        }
    }

    @Override
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        Tile batteryComponent = penaltyIfLose.playerUsesBatteryToDefend(player,batteries);
        if (batteries.isEmpty() && batteryComponent == null) {
            penaltyIfLose.hitOrMiss(currentView, player);
            Coordinates destroyedTile = penaltyIfLose.getDestroyedTile();
            if (penaltyIfLose.getBranch() == null && destroyedTile != null) {
                ArrayList<Coordinates> toRemove = new ArrayList<>();
                toRemove.add(destroyedTile);
                notifyBrokenTiles(playerName, toRemove);
            }
            return;
        }
        ArrayList<Tile> modifiedTiles = new ArrayList<>();
        modifiedTiles.add(batteryComponent);
        if (batteryComponent != null) {
            notifyModifiedTiles(playerName, modifiedTiles);
            if (!penaltyIfLose.initializePenalty(game,currentView, currentPlayer)) {
                nextPlayer();
            }
        }
        else {
            try {
                currentView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }

    @Override
    public ArrayList<Projectile> getListOfProjectiles() {
        return penaltyIfLose.getListOfProjectiles();
    }

    @Override
    public int getGainedCredits() {
        return rewardCredits;
    }
}