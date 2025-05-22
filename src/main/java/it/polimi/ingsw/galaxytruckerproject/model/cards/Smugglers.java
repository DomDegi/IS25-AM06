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
    private Player currentPlayer;
    private VirtualView playersView;
    private int playerIndex = -1;
    private int won;
    private GoodsChecker goodsChecker;

    @JsonCreator
    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods,  @JsonProperty("imagePath") String filePath) {
        super(level, requiredDays, cannonStrength, filePath);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.currentPlayer = null;
        this.won = 0;
    }

    public Smugglers(@JsonProperty("level") int level, @JsonProperty("requiredDays") int requiredDays, @JsonProperty("cannonStrength") int cannonStrength, @JsonProperty("lostGoods") int lostGoods, @JsonProperty("rewardGoods") ArrayList<Goods> rewardGoods) {
        super(level, requiredDays, cannonStrength, null);
        this.lostGoods =new GoodsPenalty(lostGoods);
        this.rewardGoods = rewardGoods;
        this.currentPlayer = null;
        this.won = 0;
    }

    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        nextPlayer();
    }

    public void nextPlayer() {
        playerIndex++;
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
                lostGoods.initializePenalty(game, playersView, currentPlayer);
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
                    if (!lostGoods.initializePenalty(game,playersView, currentPlayer)) {
                        nextPlayer();
                    }
                    else {
                        try {
                            playersView.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
                        } catch (Exception ignored) {
                        }
                    }
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
            if (player.getShipBoard().getSingleCannonPower() > cannonStrength) {
                won = 1;
                cannonChoice(playerName, 0, new ArrayList<>());
            }
            else if (player.getShipBoard().getSingleCannonPower()  == cannonStrength) {
                nextPlayer();
            }
            else {
                won = -1;
                lostGoods.initializePenalty(game, playersView, currentPlayer);
                nextPlayer();
            }
            return;
        }
        float cannonPower = returned.keySet().iterator().next();
        notifyModifiedTiles(playerName, returned.get(cannonPower));
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
            if (!lostGoods.initializePenalty(game,playersView, currentPlayer)) {
                nextPlayer();
            }
            try {
                playersView.asksToInputCoordinates(CoordReqType.REMOVE_GOODS);
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
            goodsChecker =new GoodsChecker(currentPlayer,rewardGoods);
            try {
                playersView.setClientState(ClientState.MANAGE_GOODS);
            }catch(Exception ignored) {}
        }
        else {
            game.endCardEvent();
        }
    }

    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        if (!playerName.equals(currentPlayer.getPlayerName())) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
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
            try {
                playersView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
    }

    @Override
    public void removeGoods(String playerName, ArrayList<Coordinates> goodsToRemove) {
        Player player = game.identifyPlayerByName(playerName);
        if (!player.getPlayerName().equals(currentPlayer.getPlayerName()) || won != -1) {
            System.out.println(currentPlayer.getPlayerName());
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            }catch(Exception ignored) {}
            return;
        }
        ArrayList<Tile> updatedTiles = lostGoods.removeGoods(currentPlayer,playersView, goodsToRemove);
        if (updatedTiles == null) {
            try {
                playersView.showWrongInputMessage();
            }catch(Exception ignored) {}
        }
        else {
            notifyModifiedTiles(playerName, updatedTiles);
            nextPlayer();
        }
    }

    @Override

    public ArrayList<Goods> getChosenPlanets(int c) {
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