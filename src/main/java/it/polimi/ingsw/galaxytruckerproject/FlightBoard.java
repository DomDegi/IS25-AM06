package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class FlightBoard {
    private final ArrayList<Player> inGamePlayers;
    private final ArrayList<Player> podium;
    private int freePodiumPosition;
    //needed to check if pos in inGamePlayers is free or not
    private final ArrayList<Integer> occupiedPos = new ArrayList<>();

    public FlightBoard() {
        this.inGamePlayers = new ArrayList<>();
        this.podium = new ArrayList<>();
        this.freePodiumPosition=1;
    }
    //getter
    public ArrayList<Player> getAllPlayers() {
        return podium;
    }
    public ArrayList<Player> getInGamePlayers() {
        rearrange();
        return inGamePlayers;
    }
    //FlightBoard management
    public void addPlayerToGame(Player player) {
        podium.add(player);
        freePodiumPosition++;
    }
    public boolean addToTrialFlightBoard(Player newPlayer) {
        if(!podium.contains(newPlayer)){
            System.out.println("Player not found");
            return false;
        }
        if(occupiedPos.isEmpty()){
            int pos=0;
            occupiedPos.add(pos);
        }
        inGamePlayers.remove(newPlayer);
        inGamePlayers.add(occupiedPos.getFirst(),newPlayer);
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
        return true;
    }
    public boolean addToFlightBoard(Player newPlayer,int pos) {
        pos = pos - 1;
        if(!podium.contains(newPlayer)){
            System.out.println("Player not found");
            return false;
        }
        if(pos<0 || pos>= inGamePlayers.size()){
            System.out.println("Position out of bounds or playerRanking is greater than numPlayer+1");
            return false;
        }
        if (occupiedPos.contains(pos)) {
            System.out.println("Position occupied");
            return false;
        }
        inGamePlayers.remove(newPlayer);
        inGamePlayers.add(pos,newPlayer);
        switch (pos) {
            case 0:
                inGamePlayers.get(pos).setPlayerPosition(9);
                occupiedPos.set(pos,pos);
                break;
            case 1:
                inGamePlayers.get(pos).setPlayerPosition(5);
                occupiedPos.set(pos,pos);
                break;
            case 2:
                inGamePlayers.get(pos).setPlayerPosition(2);
                occupiedPos.set(pos,pos);
                break;
            case 3:
                inGamePlayers.get(pos).setPlayerPosition(0);
                occupiedPos.set(pos,pos);
                break;
            default:
                throw new IllegalArgumentException("Invalid position: " + pos);
        }
        inGamePlayers.get(pos).setPlayerRanking(pos+1);
        return true;
    }
    public void removePlayer(Player player) {
        if (!inGamePlayers.contains(player)) {
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
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank < 0 || playerRank >= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        if (freePodiumPosition >= inGamePlayers.size() || freePodiumPosition <= 0) {
            throw new IllegalStateException("freePodiumPosition out of bounds: " + freePodiumPosition);
        }

        player.setPlayerRanking(freePodiumPosition);
        player.setLanded(true);
        inGamePlayers.remove(player);
        podium.remove(player);
        inGamePlayers.add(freePodiumPosition-1, player);
        freePodiumPosition--;
        rearrange();
    }
    //moving methods
    public void moveForward(Player player, int movement) {
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank< 0 || playerRank>= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() + movement);
        for (int i = playerRank-1; i>=0 ; i--) {
            if (inGamePlayers.get(playerRank).getPlayerPosition() >= inGamePlayers.get(i).getPlayerPosition()) {
                inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() + 1);
                inGamePlayers.get(playerRank).setPlayerRanking(inGamePlayers.get(playerRank).getPlayerRanking() + 1);
                inGamePlayers.get(i).setPlayerRanking(inGamePlayers.get(i).getPlayerRanking() - 1);
            }
        }
    }
    public void moveBackward(Player player, int movement) {
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank< 0 || playerRank>= inGamePlayers.size()) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() - movement);
        for (int i = playerRank+1; i<inGamePlayers.size() ; i++) {
            if (inGamePlayers.get(playerRank).getPlayerPosition() <= inGamePlayers.get(i).getPlayerPosition()) {
                inGamePlayers.get(playerRank).setPlayerPosition(inGamePlayers.get(playerRank).getPlayerPosition() - 1);
                inGamePlayers.get(playerRank).setPlayerRanking(inGamePlayers.get(playerRank).getPlayerRanking() - 1);
                inGamePlayers.get(i).setPlayerRanking(inGamePlayers.get(i).getPlayerRanking() + 1);
            }
        }
    }
    //Array-structure changing method
    public void rearrange() {
        inGamePlayers.sort((player1, player2) -> {
            if (player1.isLanded() && !player2.isLanded()) {
                return 1;
            } else if (!player1.isLanded() && player2.isLanded()) {
                return -1;
            } else {
                return Integer.compare(player2.getPlayerPosition(), player1.getPlayerPosition());
            }
        });
        for (int i = 0; i < inGamePlayers.size(); i++) {
            inGamePlayers.get(i).setPlayerRanking(i + 1);
        }
    }
    public void concludeMovement() {
        Player firstPlayer = null;
        if (inGamePlayers.size() < 2) {
            System.err.println("Error: inGamePlayers is either null or does not have enough players0");
            return;
        }
        for (Player player : inGamePlayers) {
            if (player != null && player.getPlayerRanking() == 1) {
                firstPlayer = player;
                break;
            }
        }
        if (firstPlayer == null) {
            System.err.println("Error: No player with rank 1 found");
            return;
        }
        for (int i = 1; i < inGamePlayers.size(); i++) {
            Player player = inGamePlayers.get(i) ;
            if (player != null) {
                if (firstPlayer.getPlayerPosition() >= player.getPlayerPosition() + 18 && !player.isLanded()) {
                    earlyLanding(player);
                }
            }
        }
        rearrange();
    }
}