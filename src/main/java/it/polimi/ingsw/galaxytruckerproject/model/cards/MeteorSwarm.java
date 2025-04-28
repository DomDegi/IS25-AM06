package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.*;

public class MeteorSwarm extends Card {
    private final ArrayList<Projectile> listOfMeteors;
    private List<Player> inFlightPlayers;
    private final Map<Player, ProjectilePenalty> activePenalties = new HashMap<>();
    private int currentDiceRoll;
    private GameInterface game;
    private int currentMeteorIndex = 0;
    Player currentPlayerRolling = null;

    @JsonCreator
    public MeteorSwarm(@JsonProperty("level") int level,
                       @JsonProperty("listOfMeteors") ArrayList<Projectile> listOfMeteors) {
        super(level, 0);
        this.listOfMeteors = listOfMeteors;
        this.currentDiceRoll = 0;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.inFlightPlayers = game.getListOfInFlightPlayers();
        this.currentMeteorIndex = 0;
        startMeteorPhase();
    }

    private void startMeteorPhase() {
        if (currentMeteorIndex >= listOfMeteors.size()) {
            game.endCardEvent();
            return;
        }

        activePenalties.clear();
        currentDiceRoll = 0;
        currentPlayerRolling = inFlightPlayers.getFirst();
        VirtualView currentView = viewsMap.get(currentPlayerRolling.getPlayerName());
        try {
            currentView.setClientState(ClientState.ROLL_DICE);
        }catch(Exception ignored) {}
    }

    public void applyMeteorToPlayers() {
        Projectile currentMeteor = listOfMeteors.get(currentMeteorIndex);
        ArrayList<Projectile> singleMeteorList = new ArrayList<>();
        singleMeteorList.add(currentMeteor);

        for (Player player : inFlightPlayers) {
            ProjectilePenalty newPenalty = new ProjectilePenalty(singleMeteorList);
            newPenalty.setDiceRoll(currentDiceRoll);
            if (newPenalty.initializePenalty(game, viewsMap.get(player.getPlayerName()), player)) {
                activePenalties.put(player, newPenalty);
            }
        }
        prepareNextMeteor();
    }

    @Override
    public void rollTheDices(String playerName) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayerRolling.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        diceRoll();
        applyMeteorToPlayers();
    }

    public void notifyDiceRoll() {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.showDiceRoll(currentDiceRoll);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void useBatteries(String playerName, ArrayList<Coordinates> batteries) {
        Player player = game.identifyPlayerByName(playerName);
        if (!activePenalties.containsKey(player)) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        Tile batteryComponent = activePenalties.get(player).playerUsesBatteryToDefend(player,batteries);
        ArrayList<Tile> modifiedTiles = new ArrayList<>();
        modifiedTiles.add(batteryComponent);
        if (batteryComponent != null) {
            notifyModifiedTiles(playerName, modifiedTiles);
            if (!activePenalties.get(player).initializePenalty(game,viewsMap.get(playerName), player)) {
                playerCompletedMeteor(player);
            }
        }
        else {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }

    @Override
    public void branchChoice(String playerName, ArrayList<Coordinates> branchChoices) {
        Player player = game.identifyPlayerByName(playerName);
        if (!activePenalties.containsKey(player)) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Coordinates> removedTiles = activePenalties.get(player).chooseToMaintain(player, branchChoices);
        if (removedTiles == null) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
        }
        else {
            notifyBrokenTiles(playerName, removedTiles);
            if (!activePenalties.get(player).initializePenalty(game,viewsMap.get(playerName), player)) {
                playerCompletedMeteor(player);
            }
        }
    }

    public void playerCompletedMeteor (Player player) {
        activePenalties.remove(player);
        prepareNextMeteor();
    }

    public void prepareNextMeteor() {
        if (activePenalties.isEmpty()) {
            currentMeteorIndex++;
            startMeteorPhase();
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("MeteorSwarm: ");
        for (Projectile projectile : listOfMeteors) {
            string.append(projectile.toString());
        }
        return string.toString();
    }

    public void diceRoll() {
        Random random = new Random();
        this.currentDiceRoll = 2 + random.nextInt(11);
        notifyDiceRoll();
    }

    // For testing purposes
    public void setDiceRoll(int diceRoll) {
        this.currentDiceRoll = diceRoll;
        applyMeteorToPlayers();
    }

    @Override
    public ArrayList<Projectile> getListOfProjectiles() {
        return listOfMeteors;
    }
}