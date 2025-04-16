package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.RefuseMessage;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.UseCannonResponse;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.Map;

public class Slavers extends Enemies{
    private final int rewardCredits;
    private final int lostCrew;
    private int playerIndex;
    private Player currentPlayer = null;
    private ViewInterface playersView = null;
    private final CrewPenalty penaltyIfLose;
    private int won = 0;

    @JsonCreator
    public Slavers(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("lostCrew") int lostCrew
    ) {
        super(level, requiredDays, cannonStrength);
        this.rewardCredits = rewardCredits;
        this.lostCrew = lostCrew;
        this.playerIndex = 0;
        this.penaltyIfLose = new CrewPenalty(lostCrew);
    }

    public String toString() {
        return "Slavers " + super.toString() + " rewardCredits: " + rewardCredits + "  lostCrew: " + lostCrew;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        if (playerIndex > game.getNumberOfPlayers() - 1){
            System.out.println("No player beat the slavers\n");
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        System.out.println(currentPlayer.getPlayerName() + ", you are face to face with a ship of Slavers\n");
        System.out.println("their cannon strength is " + cannonStrength + "\n");
        System.out.println("if yours is lower than theirs, you will lose "+ lostCrew + "crew members\n");
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
                            broadcastMessage(playerName + "has beaten the slavers!");
                            sendMessageToPlayer(playersView, "Input yes or no to decide if you want the reward");
                            playersView.asksToMakeAChoice();
                        }
                        else if (totalStrength == cannonStrength) {
                            nextPlayer();
                        }
                        else {
                            won = -1;
                            sendMessageToPlayer(playersView, "You've lost the slavers, decide which crew members to lose");
                            if (!penaltyIfLose.initializePenalty(playersView, currentPlayer)) {
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
            if (penaltyIfLose.applyPenalty(game, currentPlayer, playersView, message) == 1) {
                nextPlayer();
            }
            else {
                penaltyIfLose.initializePenalty(playersView, currentPlayer);
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
        this.playersView = viewsMap.get(playerName);
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
                penaltyIfLose.automaticPenalty(game, currentPlayer, playersView);
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                sendMessageToPlayer(playersView, "You've beaten the slavers, input yes or no" +
                        " to claim the rewards or not");
                playersView.asksToMakeAChoice();
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    sendMessageToPlayer(playersView,"You've lost to the slavers:\n");
                    sendMessageToPlayer(playersView,"input a pair of number x y for every crew to remove\n");
                    if (!penaltyIfLose.initializePenalty(playersView, currentPlayer)) {
                        nextPlayer();
                    }
                }
            }
            else {
                sendMessageToPlayer(playersView, "You can't beat the slavers with only your single cannons, " +
                        "use your double cannons in exchange for batteries?");
                playersView.asksToUseCannons();
            }
        }
    }
}
