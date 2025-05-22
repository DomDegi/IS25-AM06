package it.polimi.ingsw.galaxytruckerproject.model;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class FlightBoard implements Serializable {
    private final ArrayList<Player> inGamePlayers;
    private final ArrayList<Player> podium;
    private int freePodiumPosition;
    private final GameMode gameMode;    //needed to check if pos in inGamePlayers is free or not
    private final ArrayList<Integer> occupiedPos = new ArrayList<>();
    public FlightBoard(GameMode gameMode) {
        for (int i=0;i<4;i++){
            occupiedPos.add(-1);
        }
        this.gameMode = gameMode;
        this.inGamePlayers = new ArrayList<>();
        this.podium = new ArrayList<>();
        this.freePodiumPosition=0;
    }
    //getter
    public ArrayList<Player> getAllPlayers() {
        return podium;
    }
    public ArrayList<Player> getInGamePlayers() {
        if(inGamePlayers.size()>1)
            rearrange();
        return inGamePlayers;
    }
    //FlightBoard management
    public void addPlayerToGame(Player player) {
        podium.add(player);
        freePodiumPosition++;
    }
    public void addToTrialFlightBoard(Player newPlayer) {
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

    public boolean addToFlightBoard(Player newPlayer, int pos) {
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

    public void removePlayer(Player player) {
        if (!podium.contains(player)) {
            throw new IllegalArgumentException("Invalid Player:" + player);
        }
        int playerRank = player.getPlayerRanking() - 1;
        occupiedPos.remove(playerRank);
        inGamePlayers.remove(player);
        podium.remove(player);
        freePodiumPosition--;
    }
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
    //moving methods
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
    //Array-structure changing method
    public void rearrange() {
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

    public boolean concludeMovement() {
        ArrayList<Player> landed=new ArrayList<>();
        Player firstPlayer;
        if (inGamePlayers.size() <= 1) {
            System.err.println("Error: inGamePlayers is either null or does not have enough players");
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

    public void loadFlightBoard(ArrayList<Player> players) {
        players.sort(Comparator.comparingInt(Player::getPlayerRanking));
        podium.addAll(players);
        for (Player player: podium) {
            if (!player.isLanded() && player.getPlayerRanking() == 0) {
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