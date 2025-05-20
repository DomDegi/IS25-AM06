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
    private int playerIndex = -1;
    private Player currentPlayer;
    private ViewInterface currentPlayersView = null;
    private boolean won;
    private GoodsChecker goodsChecker = null;

    @JsonCreator
    public AbandonedStation(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("crewNumberRequired") int crewNumberRequired, @JsonProperty("possibleGoodsGain") ArrayList<Goods> possibleGoodsGain) {
        super(level, requiredDays);
        this.crewNumberRequired = crewNumberRequired;
        this.possibleGoodsGain = possibleGoodsGain;
        this.currentPlayer = null;
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
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        if (!decision) {
            nextPlayer();
        }
        else {
            try {
                currentPlayersView.asksToInputCoordinates(CoordReqType.CHOOSE_CREW);
            } catch(Exception ignored) {}
            won = true;
            goodsChecker = new GoodsChecker(currentPlayer,possibleGoodsGain);
        }
    }

    @Override
    public void removeCrew(String playerName, ArrayList<Coordinates> toRemoveFrom) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || !won) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        if (toRemoveFrom.size() != this.crewNumberRequired) {
            try{
                currentPlayersView.showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updatedTiles = player.removeCrew(toRemoveFrom);
        if (updatedTiles != null) {
            notifyModifiedTiles(playerName, updatedTiles);
            try {
                currentPlayersView.setClientState(ClientState.MANAGE_GOODS);
            } catch(Exception ignored) {}
        }
        else {
            try {
                currentPlayersView.showWrongInputMessage();
            } catch(Exception ignored) {}
        }
    }

    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        Player player = game.identifyPlayerByName(playerName);
        if (!won || !player.equals(currentPlayer)) {
            try{
                viewsMap.get(playerName).showWrongInputMessage();
            } catch(Exception ignored) {}
            return;
        }
        if (!goodsChecker.check(clientCredits, updatedCargos)) {
            try{
                currentPlayersView.showWrongInputMessage();
            } catch(Exception ignored) {}
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
        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1) {
            game.endCardEvent();
            return;
        }
        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        this.currentPlayersView = viewsMap.get(currentPlayer.getPlayerName());

        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
        }
        if (currentPlayer.getTotalCrew() < crewNumberRequired) {
            nextPlayer();
        }
        else {
            try {
                currentPlayersView.setClientState(ClientState.ACTION);
            } catch (Exception ignored) {}
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

    @Override
    public ArrayList<Goods> getGoodsList(String playerName) {
        return possibleGoodsGain;
    }

    @Override
    public int getCrewNumber() {
        return crewNumberRequired;
    }

    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && playerName.equals(currentPlayer.getPlayerName())) {
            playerIndex--;
            nextPlayer();
        }
    }
}