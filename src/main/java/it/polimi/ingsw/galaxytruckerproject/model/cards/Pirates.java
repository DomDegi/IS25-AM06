package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.ProjectilePenalty;
import it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.RefuseMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.UseCannonResponse;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class Pirates extends Enemies {
    private final int rewardCredits;
    private int playerIndex;
    private Player currentPlayer = null;
    private ViewInterface currentView = null;
    private final ProjectilePenalty penaltyIfLose;
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
        this.penaltyIfLose = new ProjectilePenalty(listOfShots);
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
    public void initializeCard(GameInterface game, Map<String, ViewInterface> viewsMap) {
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


    //pay crew penalty
    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();
        if (currentPlayer == null || !playerName.equalsIgnoreCase(currentPlayer.getPlayerName())) {
            return;
        }
        if (won == 0) {
            if (message.getMessageType().equals(MessageType.USE_CANNON_RESPONSE)) {
                UseCannonResponse messageReceived = (UseCannonResponse) message;
                float totalStrength = currentPlayer.useCannons(messageReceived.getStrength(), messageReceived.getCoordinates());
                if (totalStrength != -1) {
                    if (totalStrength > cannonStrength) {
                        won = 1;
                        broadcastMessage(playerName + "has beaten the pirates!");
                        sendMessageToPlayer(currentView, "Input yes or no to decide if you want the reward");
                        currentView.asksToMakeAChoice();
                    }
                    else if (totalStrength == cannonStrength) {
                        nextPlayer();
                    }
                    else {
                        won = -1;
                        sendMessageToPlayer(currentView, "You've lost the pirates, prepare for the cannon shots");
                        if (!penaltyIfLose.initializePenalty(currentView, currentPlayer)) {
                            nextPlayer();
                        }
                    }
                }
            }
        }
        else if (won == 1){
            if (message.getMessageType().equals(MessageType.REFUSE_MESSAGE)) {
                game.endCardEvent();
            }
            else if (message.getMessageType().equals(MessageType.ACCEPT_MESSAGE)) {
                game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
                currentPlayer.gainCredit(rewardCredits);
                game.endCardEvent();
            }
        }
        else if (won == -1){
            if (penaltyIfLose.applyPenalty(game, currentPlayer, currentView, message) == 1) {
                nextPlayer();
            }
            else {
                penaltyIfLose.initializePenalty(currentView, currentPlayer);
            }
        }
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
        this.currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = currentPlayer.getPlayerName();
        this.currentView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();

        if (currentPlayer.IsDisconnected()) {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                executeCard(new RefuseMessage(playerName));
            }
            else if (singleCannonPower == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                penaltyIfLose.automaticPenalty(game, currentPlayer, currentView);
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                sendMessageToPlayer(currentView, "You've beaten the pirates, input yes or no" +
                        " to claim the rewards or not");
                currentView.asksToMakeAChoice();
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    sendMessageToPlayer(currentView,"You've lost to the pirates:\n");
                    sendMessageToPlayer(currentView,"Prepare for the cannon shots\n");
                    if (!penaltyIfLose.initializePenalty(currentView, currentPlayer)) {
                        nextPlayer();
                    }
                }
            }
            else {
                sendMessageToPlayer(currentView, "You can't beat the pirates with only your single cannons, " +
                        "use your double cannons in exchange for batteries?");
                currentView.asksToUseCannons();
            }
        }
    }
}