package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MeteorSwarm extends Card {
    private final ArrayList<Projectile> listOfMeteors;
    private Player currentPlayerRolling;
    private ViewInterface currentView;
    private List<Player> inFlightPlayers;
    private final Map<Player, ProjectilePenalty> activePenalties = new HashMap<>();
    private int currentDiceRoll;
    private GameInterface game;
    private int currentMeteorIndex = 0;

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
        currentView = viewsMap.get(currentPlayerRolling.getPlayerName());
        currentView.asksToRollTheDices();
    }

    public void applyMeteorToPlayers() {
        Projectile currentMeteor = listOfMeteors.get(currentMeteorIndex);
        ArrayList<Projectile> singleMeteorList = new ArrayList<>();
        singleMeteorList.add(currentMeteor);

        for (Player player : inFlightPlayers) {
            ProjectilePenalty newPenalty = new ProjectilePenalty(singleMeteorList);
            newPenalty.setDiceRoll(currentDiceRoll);
            if (!newPenalty.initializePenalty(viewsMap.get(player.getPlayerName()), player)) {
                activePenalties.put(player, newPenalty);
            }
        }
    }

    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();
        Player player = game.identifyPlayerByName(playerName);

        if (currentDiceRoll == 0) {
            if (player == currentPlayerRolling && message.getMessageType() == MessageType.ROLL_THE_DICES_REQUEST) {
                diceRoll();
                currentView.showDiceRoll(currentDiceRoll);
                applyMeteorToPlayers();
                // If no active penalties, move to the next meteor immediately (edge case)
                if (activePenalties.isEmpty()) {
                    currentMeteorIndex++;
                    startMeteorPhase();
                }
            }
            return;
        }

        if (activePenalties.containsKey(player)) {
            if (activePenalties.get(player).applyPenalty(game, player, viewsMap.get(playerName), message) == 1) {
                activePenalties.remove(player);
                // Check if all players have resolved their penalty for the current meteor
                if (activePenalties.isEmpty()) {
                    currentMeteorIndex++;
                    startMeteorPhase();
                }
            }
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
    }

    // For testing purposes
    public void setDiceRoll(int diceRoll) {
        this.currentDiceRoll = diceRoll;
    }
}