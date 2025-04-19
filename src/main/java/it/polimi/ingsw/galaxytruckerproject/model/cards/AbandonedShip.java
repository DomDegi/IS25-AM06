package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class AbandonedShip extends Card {
    private final int crewNumberRequired;
    private final int possibleCreditGains;
    private int playerIndex;
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
        this.playerAccepted = false;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove){
        Player player = game.identifyPlayerByName(playerName);
        if (!playerAccepted || !player.equals(playerToPlay)) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (crewToRemove.size() < this.crewNumberRequired) {
            playersView.showWrongInputMessage();
            return;
        }
        ArrayList<Tile> updatedTile = player.removeCrew(crewToRemove);
        if (updatedTile !=  null) {
            notifyModifiedTiles(playerName,updatedTile);
            game.getFlightBoard().moveBackward(playerToPlay, requiredDays);
            notifyMovement(player);
            player.gainCredit(possibleCreditGains);
            notifyGainedCredits(playerName, player.getCredit());
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
    public int getCrewNumber() {
        return crewNumberRequired;
    }

    @Override
    public int getGainedCredits() {
        return possibleCreditGains;
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
