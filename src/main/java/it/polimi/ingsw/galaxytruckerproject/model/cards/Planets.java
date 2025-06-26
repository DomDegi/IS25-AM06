package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.galaxytruckerproject.client.ClientState;
import it.polimi.ingsw.galaxytruckerproject.model.GameInterface;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsChecker;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoHold;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.network.VirtualView;
import it.polimi.ingsw.galaxytruckerproject.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a card containing a set of planets, each with associated goods.
 * Players can choose an unoccupied planet and optionally pick up its goods.
 * Afterward, they must update their cargo accordingly.
 */
public class Planets extends Card {

    /** List of available planets on the card. */
    private final ArrayList<Planet> listOfPlanets;

    /** Player currently interacting with the planets. */
    private Player currentPlayer;

    /** View of the current player. */
    private ViewInterface currentPlayerView = null;

    /** Flag indicating whether the current player has chosen a planet. */
    private boolean chosen;

    /** Map of player names to goods checkers for validating cargo. */
    private Map<String, GoodsChecker> goodsChecker;

    /** Map of player names to their chosen planets. */
    public Map<String, Planet> playerChosenPlanets = new HashMap<>();

    /** Index of the player currently taking action. */
    private int playerIndex = -1;

    /**
     * JSON constructor with image path.
     *
     * @param level          card level
     * @param requiredDays   days lost after managing goods
     * @param listOfPlanets  list of planets available
     * @param filePath       image path for the card
     */
    @JsonCreator
    public Planets(@JsonProperty("level") int level,
                   @JsonProperty("requiredDays") int requiredDays,
                   @JsonProperty("listOfPlanets") ArrayList<Planet> listOfPlanets,
                   @JsonProperty("imagePath") String filePath) {
        super(level, requiredDays, filePath);
        this.goodsChecker = new HashMap<>();
        this.listOfPlanets = listOfPlanets;
        this.currentPlayer = null;
        this.chosen = false;
    }

    /**
     * Constructor without image path.
     */
    public Planets(int level, int requiredDays, ArrayList<Planet> listOfPlanets) {
        super(level, requiredDays, null);
        this.goodsChecker = new HashMap<>();
        this.listOfPlanets = listOfPlanets;
        this.currentPlayer = null;
        this.chosen = false;
    }

    /**
     * Initializes the card and starts player interaction.
     */
    @Override
    public void initializeCard(GameInterface game, Map<String, VirtualView> viewsMap) {
        this.game = game;
        this.viewsMap = viewsMap;
        this.nextPlayer();
    }

    /**
     * Handles goods validation and cargo update after a planet has been chosen.
     */
    @Override
    public void manageGoods(String playerName, int clientCredits, ArrayList<CargoHold> updatedCargos) {
        if (!chosen) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        Player player = game.identifyPlayerByName(playerName);
        if(player == null)
            return;
        if (goodsChecker.get(playerName).check(clientCredits, updatedCargos)) {
            game.getFlightBoard().moveBackward(player, requiredDays);
            notifyMovement(player);
            ArrayList<Tile> updatedTiles = new ArrayList<>(updatedCargos);
            notifyModifiedTiles(playerName, updatedTiles);
            playerIndex++;
            if (playerIndex >= goodsChecker.size()) {
                game.endCardEvent();
            }
        } else {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Called when a player chooses a planet.
     * Index starts from 1; input 0 means no planet is chosen.
     */
    @Override
    public void planetChoice(String playerName, int planet) {
        if (!playerName.equals(currentPlayer.getPlayerName()) || chosen) {
            try {
                viewsMap.get(playerName).showWrongInputMessage();
            } catch (Exception ignored) {}
            return;
        }
        if (planet > 0) {
            if (listOfPlanets.get(planet - 1).getOccupationStatus()) {
                try {
                    currentPlayerView.showWrongInputMessage();
                } catch (Exception ignored) {}
                return;
            }
            listOfPlanets.get(planet - 1).setOccupationStatus();
            playerChosenPlanets.put(playerName, listOfPlanets.get(planet - 1));
            notifyPlayerLanded(playerName, planet);
            goodsChecker.put(playerName, new GoodsChecker(currentPlayer, listOfPlanets.get(planet - 1).getListOfGoods()));

        }
        if (!goodsChecker.isEmpty()&& playerIndex >= game.getListOfInFlightPlayers().size() - 1) {
            chosen = true;
            playerIndex = 0;
            for (Player player : game.getListOfInFlightPlayers()) {
                try {
                    viewsMap.get(player.getPlayerName()).setClientState(
                            goodsChecker.containsKey(player.getPlayerName()) ?
                                    ClientState.MANAGE_GOODS :
                                    ClientState.WAIT
                    );
                } catch (Exception ignored) {}
            }
            return;
        }

        nextPlayer();
    }

    /**
     * Notifies all players that a player has landed on a planet.
     */
    public void notifyPlayerLanded(String playerName, int planet) {
        for (VirtualView view : viewsMap.values()) {
            try {
                view.notifyPlayerLandedOnPlanet(playerName, planet);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Advances to the next player in the list.
     */
    public void nextPlayer() {

        playerIndex++;
        if (playerIndex > game.getNumberOfPlayers() - 1&&!chosen) {
            game.endCardEvent();
            return;
        }

        currentPlayer = game.getListOfInFlightPlayers().get(playerIndex);
        currentPlayerView = viewsMap.get(currentPlayer.getPlayerName());

        if (currentPlayer.IsDisconnected()) {
            nextPlayer();
            return;
        }

        try {
            currentPlayerView.setClientState(ClientState.PLANET_CHOICE);
        } catch (Exception ignored) {}
    }

    /**
     * Gets the list of goods from the planet chosen by the player.
     */
    @Override
    public ArrayList<Goods> getGoodsList(String playerName) {
        return playerChosenPlanets.get(playerName).getListOfGoods();
    }

    /**
     * Sets the planet chosen by the player for managing goods.
     *
     * @param playerName the name of the player selecting the planet
     * @param planet the index of the chosen planet (starting from 1)
     */
    @Override
    public void setGoodsList(String playerName, int planet) {
        playerChosenPlanets.put(playerName, listOfPlanets.get(planet - 1));
    }

    /**
     * Returns a string representation of the card and its planets.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Planets: ").append(super.toString()).append(" ");
        int i = 0;
        for (Planet planet : listOfPlanets) {
            i++;
            string.append("planet ").append(i).append(" - ").append(planet.toString());
        }
        return string.toString();
    }

    /**
     * Prints the list of unoccupied planets and their available goods.
     *
     * @return formatted string of available planets and goods
     */
    public String printListOfPlanets() {
        int index = 1;
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED = "\u001B[31m";

        StringBuilder sb = new StringBuilder();
        for (Planet planet : listOfPlanets) {
            if (!planet.getOccupationStatus()) {
                sb.append("Planet: ").append(index).append(" ");
                for (Goods goods : planet.getListOfGoods()) {
                    String color = switch (goods.getColor()) {
                        case BLUE -> ANSI_BLUE;
                        case GREEN -> ANSI_GREEN;
                        case YELLOW -> ANSI_YELLOW;
                        case RED -> ANSI_RED;
                    };
                    sb.append(color).append(goods.getColor())
                            .append("(").append(goods.getValue()).append(") ").append(ANSI_RESET);
                }
                sb.append("\n");
            }
            index++;
        }
        return sb.toString();
    }

    /**
     * Returns the list of goods for a specific planet by index.
     *
     * @param index the index of the planet
     * @return list of goods
     */
    public ArrayList<Goods> getChosenPlanets(int index) {
        return this.listOfPlanets.get(index).getListOfGoods();
    }

    /**
     * Gets the full list of planets available on this card.
     *
     * @return list of planets
     */
    public ArrayList<Planet> getListOfPlanets() {
        return listOfPlanets;
    }

    /**
     * Handles disconnection of the current player.
     *
     * @param playerName name of the disconnected player
     */
    @Override
    public void playerDisconnected(String playerName) {
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            planetChoice(playerName,0);
        }
        else if (playerChosenPlanets.containsKey(playerName)) {
            // Phase: managing goods — apply auto-assignment
            Player player = game.identifyPlayerByName(playerName);
            Planet chosenPlanet = playerChosenPlanets.get(playerName);
            if (chosenPlanet != null) {
                ArrayList<Tile> updatedTiles = player.automaticGoodsPositioner(chosenPlanet.getListOfGoods());
                game.getFlightBoard().moveBackward(player, requiredDays);
                notifyMovement(player);
                notifyModifiedTiles(playerName, updatedTiles);
            }
            playerIndex++;
            if (playerIndex == goodsChecker.size()) {
                game.endCardEvent();
            }
        }
    }

    /**
     * Handles the scenario when a player lands on the 'Smugglers' card and resolves
     * the corresponding sequence of events based on game conditions.
     *
     * @param playerName the name of the player who landed on the card
     */
    @Override
    public void playerLanded(String playerName) {
        if (currentPlayer != null && currentPlayer.getPlayerName().equals(playerName)) {
            planetChoice(playerName,0);
        }
        else if (playerChosenPlanets.containsKey(playerName)) {
            // Phase: managing goods — apply auto-assignment
            Player player = game.identifyPlayerByName(playerName);
            Planet chosenPlanet = playerChosenPlanets.get(playerName);
            if (chosenPlanet != null) {
                ArrayList<Tile> updatedTiles = player.automaticGoodsPositioner(chosenPlanet.getListOfGoods());
                game.getFlightBoard().moveBackward(player, requiredDays);
                notifyMovement(player);
                notifyModifiedTiles(playerName, updatedTiles);
            }
            playerIndex++;
            if (playerIndex == goodsChecker.size()) {
                game.endCardEvent();
            }
        }
    }

    public Map<String, Planet> getPlayerChosenPlanets() {
        return playerChosenPlanets;
    }
}
