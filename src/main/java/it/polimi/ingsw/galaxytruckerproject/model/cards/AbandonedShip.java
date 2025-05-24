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
            @JsonProperty("possibleCreditGains") int possibleCreditGains,
            @JsonProperty("imagePath") String filePath
    ) {
        super(level, requiredDays, filePath);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleCreditGains = possibleCreditGains;
        this.playerIndex = 0;
        this.playerAccepted = false;
    }

    public AbandonedShip(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("crewNumberRequired") int crewNumberRequired,
            @JsonProperty("possibleCreditGains") int possibleCreditGains
    ) {
        super(level, requiredDays, null);
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
            try{
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        if (crewToRemove.size() != this.crewNumberRequired) {
            try{
                playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
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
            try{
                playersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
    }

    public void choice (String playerName, boolean choice) {
        if (!playerName.equals(playerToPlay.getPlayerName())) {
            try{
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        if (choice) {
            try {
                playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            } catch(Exception ignored) {}
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
            this.nextPlayer();
        }
        else if (playerToPlay.getTotalCrew() >= crewNumberRequired) {
            try {
                playersView.setClientState(ClientState.ACTION);
            } catch(Exception ignored) {}
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

    @Override
    public void playerDisconnected(String playerName) {
        if (playerToPlay != null && playerToPlay.getPlayerName().equals(playerName)) {
            playerIndex--;
            nextPlayer();
        }
    }
}
