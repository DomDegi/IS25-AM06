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
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

public class Slavers extends Enemies{
    private final int rewardCredits;
    private final int lostCrew;
    private int playerIndex;
    private Player currentPlayer = null;
    private VirtualView playersView = null;
    private final CrewPenalty penaltyIfLose;
    private int won = 0;

    @JsonCreator
    public Slavers(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("lostCrew") int lostCrew,
            @JsonProperty("imagePath") String filePath
    ) {
        super(level, requiredDays, cannonStrength, filePath);
        this.rewardCredits = rewardCredits;
        this.lostCrew = lostCrew;
        this.playerIndex = 0;
        this.penaltyIfLose = new CrewPenalty(lostCrew);
    }


    @JsonCreator
    public Slavers(
            @JsonProperty("level") int level,
            @JsonProperty("requiredDays") int requiredDays,
            @JsonProperty("cannonStrength") int cannonStrength,
            @JsonProperty("rewardCredits") int rewardCredits,
            @JsonProperty("lostCrew") int lostCrew
    ) {
        super(level, requiredDays, cannonStrength, null);
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
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
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
        this.playersView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
        if (singleCannonPower > 0) {
            singleCannonPower = singleCannonPower + 2*currentPlayer.getShipBoard().getNumPurpleAliens();
        }

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
                penaltyIfLose.initializePenalty(game,playersView, currentPlayer);
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                try {
                    playersView.setClientState(ClientState.ACTION);
                }catch(Exception ignored) {}
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    if(!penaltyIfLose.initializePenalty(game,playersView, currentPlayer)) {
                        nextPlayer();
                    }
                    try {
                        playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
                    }catch(Exception ignored) {}
                }
            }
            else {
                try {
                    playersView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_CANNON);
                }catch(Exception ignored) {}
            }
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
            if (player.getPlayerShip().getSingleCannonPower() > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            }
            else if (player.getPlayerShip().getSingleCannonPower()  == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                penaltyIfLose.initializePenalty(game,playersView, currentPlayer);
                nextPlayer();
            }
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
                    playersView.setClientState(ClientState.ACTION);
                }catch(Exception ignored) {}
            }
        }
        else if (cannonPower == cannonStrength) {
            nextPlayer();
        }
        else {
            won = -1;
            if(!penaltyIfLose.initializePenalty(game,playersView, currentPlayer)) {
                nextPlayer();
            }
            try {
                playersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            }catch(Exception ignored) {}
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
    public void removeCrew(String playerName, ArrayList<Coordinates> crewToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || won != -1) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updated = penaltyIfLose.removeCrew(player, playersView, crewToRemove);
        if (updated != null) {
            notifyModifiedTiles(playerName, updated);
            nextPlayer();
        }
        else {
            try {
                playersView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }

    @Override
    public int getCrewNumber() {
        return lostCrew;
    }

    @Override
    public int getGainedCredits() {
        return rewardCredits;
    }
}
