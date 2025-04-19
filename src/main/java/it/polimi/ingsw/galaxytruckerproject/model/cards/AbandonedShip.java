package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.CrewPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.Message;
import it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message.MessageType;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class AbandonedShip extends Card {
    private final int crewNumberRequired;
    private final int possibleCreditGains;
    private int playerIndex;
    private final CrewPenalty penaltyIfAccept;
    private Player playerToPlay = null;
    private ViewInterface playersView = null;
    private boolean playerAccepted;

    @JsonCreator
    public AbandonedShip(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleCreditGains") int possibleCreditGains
    ) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
        this.playerIndex = 0;
        this.penaltyIfAccept = new CrewPenalty(crewNumberRequired);
        this.playerAccepted = false;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    @Override
    public void executeCard(Message message) {
        String playerName = message.getNickname();

        if (playerToPlay == null || !playerToPlay.getPlayerName().equalsIgnoreCase(playerName)) {
            return;
        }
        if (!playerAccepted) {
            if (message.getMessageType().equals(MessageType.ACCEPT_MESSAGE)) {
                playerAccepted = true;
                sendMessageToPlayer(playersView,"input a pair of number x y for every crew to remove\n");
                if (!penaltyIfAccept.initializePenalty(playersView, playerToPlay)) {
                    game.endCardEvent();
                }
            } else if (message.getMessageType().equals(MessageType.REFUSE_MESSAGE)) {
                this.nextPlayer();
            }

        } else {
            if (penaltyIfAccept.applyPenalty(game, playerToPlay, playersView, message) == 1) {
                playerToPlay.gainCredit(possibleCreditGains);
                game.getFlightBoard().moveBackward(playerToPlay, requiredDays);
                game.endCardEvent();
            } else {
                penaltyIfAccept.initializePenalty(playersView, playerToPlay);
            }
        }
    }

    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove){
        Player player = game.identifyPlayerByName(playerName);
        if (!playerAccepted || !player.equals(playerToPlay)) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        ArrayList<Tile> updatedTile = player.removeCrew(crewToRemove);
        if (updatedTile !=  null) {
            notifyModifiedTiles(playerName,updatedTile);
            game.getFlightBoard().moveBackward(playerToPlay, requiredDays);
            notifyMovement(player);
            game.endCardEvent();
        }
        else {
            playersView.showWrongInputMessage();
        }
    }

    public void choice (String playerName, boolean choice) {
        if (!playerName.equals(playerToPlay.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (choice) {
            playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            playerAccepted = true;
        }
        else {
            nextPlayer();
        }
    }

    public void nextPlayer() {
        if (playerToPlay != null) {
            playerIndex++;
        }
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        this.playerToPlay = game.getListOfInFlightPlayers().get(playerIndex);
        String playerName = playerToPlay.getPlayerName();
        this.playersView = viewsMap.get(playerName);

        if (playerToPlay.IsDisconnected()) {
            this.nextPlayer();
            return;
        }
        if (playerToPlay.getTotalCrew() < crewNumberRequired) {
            sendMessageToPlayer(playersView, playerName + " you don't have enough crew members: " +
                    "needed " + crewNumberRequired);
            this.nextPlayer();
        }
        else if (playerToPlay.getTotalCrew() >= crewNumberRequired) {
            playersView.setClientState(ClientState.ACTION);
        }
    }

    @Override
    public String toString() {
        return
                "AbandonedShip: " + super.toString() + " crewNumberRequired "
                        + crewNumberRequired + " possibleCreditsGain " + possibleCreditGains;
    }

    public Player getPlayerToPlay() {
        return playerToPlay;
    }
}
