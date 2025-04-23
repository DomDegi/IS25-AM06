package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.client.CoordReqType;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.cards.penalties.GoodsPenalty;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;

import java.util.ArrayList;
import java.util.Map;

public class Smugglers extends Enemies{
    private final GoodsPenalty lostGoods;
    private final ArrayList<Goods> rewardGoods;
    private ArrayList<Player> playerToInteract;
    private Player currentPlayer;
    private VirtualView playersView;
    private boolean initialized;
    private int won;
    private GoodsChecker goodsChecker;

    @JsonCreator
    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods) {
        super(level, requiredDays, cannonStrength);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.initialized = false;
        this.currentPlayer = null;
        this.playerToInteract = new ArrayList<>();
        this.won = 0;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    public void nextPlayer() {
        if (!initialized) {
            this.initialized = true;
            this.playerToInteract = game.getListOfInFlightPlayers();
        } else {
            if (playerToInteract.isEmpty()) {
                game.endCardEvent();
                return;
            }
            playerToInteract.removeFirst();
        }
        currentPlayer = playerToInteract.getFirst();
        String playerName = currentPlayer.getPlayerName();
        this.playersView = viewsMap.get(playerName);
        float singleCannonPower = currentPlayer.getShipBoard().getSingleCannonPower();
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
                notifyModifiedTiles(currentPlayer.getPlayerName(), lostGoods.automaticGoodsPenalty(game, currentPlayer, playersView));
                nextPlayer();
            }
        }
        else {
            if (singleCannonPower > cannonStrength) {
                won = 1;
                playersView.setClientState(ClientState.ACTION);
            }
            else if (currentPlayer.getShipBoard().getDoubleCannon().isEmpty() ||
                    currentPlayer.getShipBoard().getBatteryCoordinates().isEmpty()) {
                if (singleCannonPower == cannonStrength) {
                    nextPlayer();
                }
                else {
                    won = -1;
                    lostGoods.initializePenalty(playersView, currentPlayer);
                    playersView.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
                }
            }
            else {
                playersView.asksToInputCoordinates(CoordReqType.CHOOSE_DOUBLE_ENGINE);
            }
        }
    }

    @Override
    public void cannonChoice(String playerName, float doubleCannonPower, ArrayList<Coordinates> batteriesToUse) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName())) {
            viewsMap.get(playerName).setClientState(ClientState.ACTION);
            return;
        }
        Map<Float,ArrayList<Tile>> returned = player.useCannons(doubleCannonPower, batteriesToUse);
        if (returned == null) {
            playersView.setClientState(ClientState.ACTION);
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
            playersView.setClientState(ClientState.ACTION);
            }
        }
        else if (cannonPower == cannonStrength) {
            nextPlayer();
        }
        else {
            won = -1;
            lostGoods.initializePenalty(playersView, currentPlayer);
            playersView.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
        }
    }

    @Override
    public void choice(String playerName, boolean decision) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || won != 1) {
            viewsMap.get(playerName).setClientState(ClientState.ACTION);
            return;
        }
        if (decision) {
            goodsChecker =new GoodsChecker(currentPlayer,rewardGoods);
            playersView.setClientState(ClientState.MANAGE_GOODS);
        }
        else {
            game.endCardEvent();
        }
    }

    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        if (goodsChecker.check(clientCredits, updatedCargos)) {
            game.getFlightBoard().moveBackward(currentPlayer, requiredDays);
            notifyMovement(currentPlayer);
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            game.endCardEvent();
        }
        else {
            playersView.showWrongInputMessage();
        }
    }

    @Override
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || won != -1) {
            viewsMap.get(playerName).showWrongInputMessage();
            return;
        }
        ArrayList<Tile> updatedTiles = lostGoods.removeGoods(currentPlayer,playersView, goodsToRemove);
        if (updatedTiles == null) {
            playersView.showWrongInputMessage();
        }
        else {
            notifyModifiedTiles(playerName, updatedTiles);
            nextPlayer();
        }
    }

    @Override
    public ArrayList<Goods> getGoodsList(String playerName) {
        return rewardGoods;
    }

    @Override
    public int getGoodsPenalty() {
        return lostGoods.getNumberOfLostGoods();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Smugglers: ").append(super.toString()).append(" lostGoods: ").append(this.lostGoods).append(" ");
        string.append("rewardGoods: ");
        for (Goods goods : rewardGoods) {
            string.append(goods.toString()).append(" ");
        }
        return string.toString();
    }
}