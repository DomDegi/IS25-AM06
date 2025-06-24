package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * The FlightBoard class manages the positions and rankings of players during the flight phase of the game.
 * It tracks which players are still in flight, which have landed, and provides movement and ranking logic.
 */
public class FlightBoard implements Serializable {

    /**
     * List of all players that have participated in the game.
     * Players that landed are ordered by landing position.
     */
    private final ArrayList<Player> inGamePlayers;

    /**
     * Final list of players ordered by final position.
     * Players that land early are inserted here according to landing order.
     */
    private final ArrayList<Player> podium;

    /**
     * Counter for next free position in the podium list.
     */
    private int freePodiumPosition;

    /**
     * The game mode, needed to determine movement thresholds for forced landings.
     */
    private final GameMode gameMode;

    /**
     * List used to track occupied positions in flight based on ranking.
     */
    private final ArrayList<Integer> occupiedPos = new ArrayList<>();

    /**
     * Constructs a FlightBoard with the specified game mode.
     * @param gameMode the mode of the game (e.g., TRIAL or LEVEL2)
     */
    public FlightBoard(GameMode gameMode) {
        for (int i=0;i<4;i++){
            occupiedPos.add(-1);
        }
        this.gameMode = gameMode;
        this.inGamePlayers = new ArrayList<>();
        this.podium = new ArrayList<>();
        this.freePodiumPosition=0;
    }

    // ====================== GETTER METHODS ======================

    /**
     * Gets the full list of players ordered by final ranking.
     * @return list of all players on the podium
     */
    public ArrayList<Player> getAllPlayers() {
        return podium;
    }

    /**
     * Gets the list of players still in flight (not landed yet).
     * @return list of players in flight
     */
    public ArrayList<Player> getInGamePlayers() {
        if(inGamePlayers.size()>1)
            rearrange();
        return inGamePlayers;
    }

    // ====================== FLIGHTBOARD MANAGEMENT ======================

    /**
     * Adds a player to the game and increments the available podium position.
     * @param player the player to add
     */
    public void addPlayerToGame(Player player) {
        podium.add(player);
        freePodiumPosition++;
    }

    /**
     * Adds a player to the in-flight board for TRIAL mode.
     * Sets their starting position based on their order.
     * @param newPlayer the player to add
     */
    public synchronized void addToTrialFlightBoard(Player newPlayer) {
        if(!podium.contains(newPlayer)){
            System.out.println("Player not found");
            return;
        }
        if(occupiedPos.getFirst()==-1){
            occupiedPos.clear();
            occupiedPos.add(0);
        }
        inGamePlayers.remove(newPlayer);
        while (inGamePlayers.size() <= occupiedPos.getFirst()) {
            inGamePlayers.add(null);  // adds null positions
        }
        inGamePlayers.set(occupiedPos.getFirst(),newPlayer);
        switch (occupiedPos.getFirst()) {
            case 0:
                inGamePlayers.get(occupiedPos.getFirst()).setPlayerPosition(4);
                break;
            case 1:
                inGamePlayers.get(occupiedPos.getFirst()).setPlayerPosition(2);
                break;
            case 2:
                inGamePlayers.get(occupiedPos.getFirst()).setPlayerPosition(1);
                break;
            case 3:
                inGamePlayers.get(occupiedPos.getFirst()).setPlayerPosition(0);
                break;
            default:
                throw new IllegalArgumentException("Invalid position: " + occupiedPos.getFirst());
        }
        inGamePlayers.get(occupiedPos.getFirst()).setPlayerRanking(occupiedPos.getFirst()+1);
        occupiedPos.set(0,occupiedPos.getFirst()+1);
    }

    /**
     * Adds a player to the in-flight board for LEVEL2 mode at a specified position.
     * @param newPlayer the player to add
     * @param pos the desired ranking position (1-based)
     * @return true if the addition was successful, false otherwise
     */
    public synchronized boolean addToFlightBoard(Player newPlayer, int pos) {
        pos = pos - 1; // offsets to match position to arraylist indexes
        if (!podium.contains(newPlayer)) {
            System.out.println("Player not found");
            return false;
        }
        if (pos < 0 || pos >= podium.size()) {
            System.out.println("Position out of bounds or playerRanking is greater than numPlayer+1");
            return false;
        }
        if (occupiedPos.contains(pos)) {
            System.out.println("Position occupied");
            return false;
        }
        //removes only if the player is already present in inGamePlayers
        inGamePlayers.remove(newPlayer);
        //makes sure that the position chosen is from 0 to 3 (after the offset) and sets the right value for starting pos
        switch (pos) {
            case 0:
                newPlayer.setPlayerPosition(6);
                break;
            case 1:
                newPlayer.setPlayerPosition(3);
                break;
            case 2:
                newPlayer.setPlayerPosition(1);
                break;
            case 3:
                newPlayer.setPlayerPosition(0);
                break;
            default:
                //throw new IllegalArgumentException("Invalid position: " + occupiedPos.getFirst());
                return false;
        }
        // gives player a ranking
        newPlayer.setPlayerRanking(pos + 1);
        inGamePlayers.add(newPlayer);
        // makes sure that occupied pos has enough spaces just like we did with inGamePlayers with the null values
        occupiedPos.set(pos,pos);
        rearrange();
        return true;
    }

    /**
     * Removes a player from the game entirely (from both lists).
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        if (!podium.contains(player)) {
            throw new IllegalArgumentException("Invalid Player:" + player);
        }
        Integer playerRank = player.getPlayerRanking()-1;
        if(playerRank>=0){
            occupiedPos.remove(playerRank);
        }
        inGamePlayers.remove(player);
        podium.remove(player);
        freePodiumPosition--;
        rearrange();
    }

    /**
     * Sends the player to the last position in the in-flight list.
     * @param player the player to demote
     */
    public void setPlayerToLast(Player player) {
        int playerRank = player.getPlayerRanking() - 1;
        if (playerRank < 0 || playerRank >= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + player.getPlayerRanking());
        }
        inGamePlayers.remove(player);
        inGamePlayers.add(player);
        player.setPlayerRanking(inGamePlayers.size());
        player.setPlayerPosition(0);
        rearrange();
    }

    /**
     * Handles a player's early landing.
     * Updates ranking and reorders the podium accordingly.
     * @param player the player that lands early
     */
    public void earlyLanding(Player player) {
        if (player.isLanded()) {
            return;
        }
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank < 0 || playerRank >= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        if (freePodiumPosition > inGamePlayers.size() || freePodiumPosition <= 0) {
            throw new IllegalStateException("freePodiumPosition out of bounds: " + freePodiumPosition);
        }
        player.setPlayerRanking(freePodiumPosition);
        player.setLanded(true);
        inGamePlayers.remove(player);
        podium.remove(player);
        podium.add(freePodiumPosition-1, player);
        freePodiumPosition--;
        rearrange();
    }

    // ====================== MOVEMENT METHODS ======================

    /**
     * Moves a player forward by the specified amount.
     * Updates rankings accordingly if the player overtakes others.
     * @param player the player to move
     * @param movement the number of positions to move
     */
    public void moveForward(Player player, int movement) {
        if (player.isLanded()) {
            System.out.println("Player already landed: " + player);
            return;
        }
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank< 0 || playerRank>= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() + movement);
        for (int i = playerRank-1; i>=0 ; i--) {
            if (inGamePlayers.get(playerRank).getPlayerPosition() >= inGamePlayers.get(i).getPlayerPosition()) {
                inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() + 1);
                inGamePlayers.get(playerRank).setPlayerRanking(inGamePlayers.get(playerRank).getPlayerRanking() - 1);
                inGamePlayers.get(i).setPlayerRanking(inGamePlayers.get(i).getPlayerRanking() + 1);
            }
        }
    }

    /**
     * Moves a player backward by the specified amount.
     * Updates rankings accordingly if the player is overtaken by others.
     * @param player the player to move
     * @param movement the number of positions to move
     */
    public void moveBackward(Player player, int movement) {
        if (player.isLanded()) {
            System.out.println("Player already landed: " + player);
            return;
        }
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank< 0 || playerRank>= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() - movement);
        for (int i = playerRank+1; i<inGamePlayers.size() ; i++) {
            if (inGamePlayers.get(playerRank).getPlayerPosition() <= inGamePlayers.get(i).getPlayerPosition()) {
                inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() - 1);
                inGamePlayers.get(playerRank).setPlayerRanking(inGamePlayers.get(playerRank).getPlayerRanking() + 1);
                inGamePlayers.get(i).setPlayerRanking(inGamePlayers.get(i).getPlayerRanking() - 1);
            }
        }
    }

    /**
     * Reorders the in-flight player list based on positions and landing status.
     * Updates each player's ranking accordingly.
     */
    public synchronized void rearrange() {
        if(inGamePlayers.size()>1) {
            inGamePlayers.sort((player1, player2) -> {
                if (player1.isLanded() && !player2.isLanded()) {
                    return 1;
                } else if (!player1.isLanded() && player2.isLanded()) {
                    return -1;
                } else {
                    return Integer.compare(player2.getPlayerPosition(), player1.getPlayerPosition());
                }
            });
        }
        for (int i = 0; i < inGamePlayers.size(); i++) {
            inGamePlayers.get(i).setPlayerRanking(i + 1);
        }
    }

    /**
     * Checks if the movement phase should end and forces landing for players far behind.
     * @return true if the phase continues, false if game should proceed to next phase
     */
    public boolean concludeMovement() {
        ArrayList<Player> landed=new ArrayList<>();
        Player firstPlayer;
        if (inGamePlayers.size() <= 1) {
            System.out.println("All the players have landed: ending the game");
            return true;
        }
        for (Player player:  inGamePlayers) {
            if (player.getShipBoard().getNumHumanCrew() == 0)
                landed.add(player);
        }
        for (Player player:  landed) {
            earlyLanding(player);
        }
        rearrange();
        if(freePodiumPosition==0){
            return false;
        }
        firstPlayer = inGamePlayers.getFirst();
        if (firstPlayer == null) {
            System.err.println("Error: No player with rank 1 found");
            return true;
        }
        for (int i = 1; i < inGamePlayers.size(); i++) {
            Player player = inGamePlayers.get(i) ;
            if (player != null) {
                switch (gameMode) {
                    case LEVEL2:
                        if (firstPlayer.getPlayerPosition() >= player.getPlayerPosition() + 24 && !player.isLanded()) {
                            earlyLanding(player);
                        }
                    case TRIAL:
                        if (firstPlayer.getPlayerPosition() >= player.getPlayerPosition() + 18 && !player.isLanded()) {
                            earlyLanding(player);
                        }
                }

            }
        }
        return true;
    }

    /**
     * Loads a previously saved flight board from player data.
     * @param players list of players to be restored
     */
    public void loadFlightBoard(ArrayList<Player> players) {
        players.sort(Comparator.comparingInt(Player::getPlayerRanking));
        podium.addAll(players);
        for (Player player: podium) {
            if (!player.isLanded() && player.getPlayerRanking() != 0) {
                inGamePlayers.add(player);
            }
        }
        freePodiumPosition = inGamePlayers.size();
    }

    public void autoSetInFreeLastPosition(Player player, int playerCount) {
        if (gameMode == GameMode.LEVEL2) {
            for (int i = playerCount - 1; i >= 0; i--) {
                if (!occupiedPos.contains(i)) {
                    addToFlightBoard(player, i + 1);
                    return;
                }
            }
        }
        else {
            addToTrialFlightBoard(player);
            setPlayerToLast(player);
        }
    }
}