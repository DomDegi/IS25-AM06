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
    private int playerIndex;
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
        this.playerIndex = 0;
        this.listOfCannonShots = listOfShots;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Pirates: ").append(super.toString()).append("rewardCredits: ").append(rewardCredits).append(" ");
        for (Projectile projectile: penaltyIfLose.getListOfProjectiles()) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
        if (playerIndex > game.getNumberOfPlayers() - 1){
            System.out.println("No player beat the pirates\n");
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        System.out.println(currentPlayer.getPlayerName() + ", you are face to face with a ship of Pirates\n");
        System.out.println("their cannon strength is " + cannonStrength + "\n");
        System.out.println("if yours is lower than theirs, you will get hit by a series of cannon shots\n");
        System.out.println("input if needed first the double cannons coordinates and after the batteries coordinates\n");
        currentPlayer.printCurrentInfoCannons();
        currentPlayer.printCurrentInfoBatteries();
    }
    

    public void nextPlayer() {
        if (currentPlayer != null){
            playerIndex++;
        }
        won = 0;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = currentPlayer.getPlayerName();
        this.currentView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
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
                penaltyIfLose.initializePenalty(game, currentView, currentPlayer);
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
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    penaltyIfLose.initializePenalty(game,currentView, currentPlayer);
                    try {
                        currentView.asksToInputCoordinates(CoordReqType.CHOOSE_TO_MAINTAIN);
                    }catch(Exception ignored) {}
                }
            }
            else {
                try {
                    currentView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
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
                viewsMap.get(playerName).setClientState(ClientState.ACTION);
            }catch(Exception ignored) {}
            return;
        }
        Map<Float,ArrayList<Tile>> returned = player.useCannons(doubleCannonPower, batteriesToUse);
        if (returned == null) {
            try {
                currentView.setClientState(ClientState.ACTION);
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
                currentView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            }catch(Exception ignored) {}
        }
    }

    @Override
    public void rollTheDices(String playerName) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
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
            if (!penaltyIfLose.initializePenalty(game,currentView, currentPlayer)) {
                nextPlayer();
            }
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