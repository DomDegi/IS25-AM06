package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.Map;

public class AbandonedStation extends Card {
    private final int crewNumberRequired;
    private final ArrayList<Goods> possibleGoodsGain;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private ViewInterface currentPlayersView = null;
    private boolean initialized;
    private boolean won;
    private GoodsChecker goodsChecker = null;

    @JsonCreator
    public AbandonedStation(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("crewNumberRequired") int crewNumberRequired, @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = false;
    }
    //asks every player in order of ranking that meets the requirements if they want to spend days to gain the goods
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (!decision) {
            nextPlayer();
        }
        else {
            currentPlayersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            won = true;
            goodsChecker = new GoodsChecker(currentPlayer,possibleGoodsGain);
        }
    }

    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> toRemoveFrom) {
        Player player = game.identifyPlayerByName(playerName);
        if (!playerName.equals(currentPlayer.getPlayerName()) || won) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (toRemoveFrom.size() != this.crewNumberRequired) {
            currentPlayersView.showWrongInputMessage();
            return;
        }
        ArrayList<Tile> updatedTiles = player.removeCrew(toRemoveFrom);
        if (updatedTiles != null) {
            notifyModifiedTiles(playerName, updatedTiles);
            currentPlayersView.setClientState(ClientState.MANAGE_GOODS);
        }
        else {
            currentPlayersView.showWrongInputMessage();
        }
    }

    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        Player player = game.identifyPlayerByName(playerName);
        if (!won || !player.equals(currentPlayer)) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (!goodsChecker.check(clientCredits, updatedCargos)) {
            currentPlayersView.showWrongInputMessage();
        }
        else {
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
            game.endCardEvent();
        }
    }

    /**
     * If the array has to be initialized, sets the non-landed player as an array
     * removes first player in the arraylist and sets the new first as current player.
     * if the arraylist is empty ends the card event.
     */
    public void nextPlayer() {
        if (!initialized) {
            playerToInteract = game.getListOfInFlightPlayers();
            initialized = true;
        } else {
            if (playerToInteract.isEmpty()) {
                game.endCardEvent();
                return;
            }
            playerToInteract.removeFirst();
        }
        currentPlayer = playerToInteract.getFirst();
        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
        }
        this.currentPlayersView = viewsMap.get(currentPlayer.getPlayerName());

        if (currentPlayer.getTotalCrew() < crewNumberRequired) {
            nextPlayer();
        }
        else {
            currentPlayersView.setClientState(ClientState.ACTION);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbandonedStation: ").append(super.toString()).append(" ").
                append("crewNumberRequired: ").append(crewNumberRequired).
                append(" possibleGoodsGain: ");
        for (Goods good: possibleGoodsGain)
            sb.append(good.toString()).append(" ");
        return sb.toString();
    }
}