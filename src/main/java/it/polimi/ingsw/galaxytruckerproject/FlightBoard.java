package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

import java.util.ArrayList;

public class FlightBoard {
    private final GameMode gameMode;
    private final ArrayList<Player> posList;
    private int numPlayer;
    private int freePodiumPosition;
    //needed to check if pos in posList is free or not
    private final ArrayList<Integer> occupiedPos = new ArrayList<>();

    public FlightBoard(GameMode gameMode) {
        this.gameMode = gameMode;
        this.posList = new ArrayList<>();
        this.numPlayer=0;
        this.freePodiumPosition=1;
    }
    //getter
    public ArrayList<Player> getRanking() {
        rearrange();
        return posList;
    }
    public int getNumPlayer() {
        return numPlayer;
    }
    //FlightBoard management
    public void initialAddToFlightBoard(Player player) {
        posList.add(player);
    }
    public boolean addToTrialFlightBoard(Player newPlayer) {
        if(!posList.contains(newPlayer)){
            System.out.println("Player not found");
            return false;
        }
        if(occupiedPos.isEmpty()){
            int pos=0;
            occupiedPos.add(pos);
        }
        posList.remove(newPlayer);
        posList.add(occupiedPos.getFirst(),newPlayer);
        switch (occupiedPos.getFirst()) {
            case 0:
                posList.get(occupiedPos.getFirst()).setPlayerPosition(4);
                break;
            case 1:
                posList.get(occupiedPos.getFirst()).setPlayerPosition(2);
                break;
            case 2:
                posList.get(occupiedPos.getFirst()).setPlayerPosition(1);
                break;
            case 3:
                posList.get(occupiedPos.getFirst()).setPlayerPosition(0);
                break;
            default:
                throw new IllegalArgumentException("Invalid position: " + occupiedPos.getFirst());
        }
        posList.get(occupiedPos.getFirst()).setPlayerRanking(occupiedPos.getFirst()+1);
        numPlayer++;
        occupiedPos.set(0,occupiedPos.getFirst()+1);
        freePodiumPosition++;
        return true;
    }
    public boolean addToFlightBoard(Player newPlayer,int pos) {
        pos = pos - 1;
        if(!posList.contains(newPlayer)){
            System.out.println("Player not found");
            return false;
        }
        if(pos<0 || pos>=posList.size()){
            System.out.println("Position out of bounds or playerRanking is greater than numPlayer+1");
            return false;
        }
        if (occupiedPos.contains(pos)) {
            System.out.println("Position occupied");
            return false;
        }
        posList.remove(newPlayer);
        posList.add(pos,newPlayer);
        switch (pos) {
            case 0:
                posList.get(pos).setPlayerPosition(9);
                occupiedPos.set(pos,pos);
                break;
            case 1:
                posList.get(pos).setPlayerPosition(5);
                occupiedPos.set(pos,pos);
                break;
            case 2:
                posList.get(pos).setPlayerPosition(2);
                occupiedPos.set(pos,pos);
                break;
            case 3:
                posList.get(pos).setPlayerPosition(0);
                occupiedPos.set(pos,pos);
                break;
            default:
                throw new IllegalArgumentException("Invalid position: " + pos);
        }
        posList.get(pos).setPlayerRanking(pos+1);
        numPlayer++;
        freePodiumPosition++;
        return true;
    }
    public void removeFromFlightBoard(Player player) {
        if (!posList.contains(player)) {
            throw new IllegalArgumentException("Invalid Player:" + player);
        }
        int playerRank = player.getPlayerRanking() - 1;
        occupiedPos.remove(playerRank);
        posList.remove(playerRank);
    }
    public void setPlayerToLast(Player player) {
        int playerRank = player.getPlayerRanking() - 1; // Ranking is 1-based
        if (playerRank < 0 || playerRank >= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + player.getPlayerRanking());
        }
        posList.remove(playerRank);
        posList.add(player);
        player.setPlayerRanking(numPlayer); // Update their rank
    }
    public void earlyLanding(Player player) {
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank < 0 || playerRank >= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        if (freePodiumPosition >= posList.size() || freePodiumPosition <= 0) {
            throw new IllegalStateException("freePodiumPosition out of bounds: " + freePodiumPosition);
        }

        player.setPlayerRanking(freePodiumPosition);
        player.setLanded(true);
        posList.remove(playerRank);
        posList.add(freePodiumPosition-1, player);
        freePodiumPosition--;
        rearrange();
    }
    //moving methods
    public void moveForward(Player player, int movement) {
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank< 0 || playerRank>= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        posList.get(playerRank).setPlayerPosition(posList.get(playerRank).getPlayerPosition() + movement);
        for (int i = playerRank-1; i>=0 ; i--) {
            if (posList.get(playerRank).getPlayerPosition() >= posList.get(i).getPlayerPosition()) {
                posList.get(playerRank).setPlayerPosition(posList.get(playerRank).getPlayerPosition() + 1);
                posList.get(playerRank).setPlayerRanking(posList.get(playerRank).getPlayerRanking() + 1);
                posList.get(i).setPlayerRanking(posList.get(i).getPlayerRanking() - 1);
            }
        }
    }
    public void moveBackward(Player player, int movement) {
        int playerRank=player.getPlayerRanking()-1;
        if (playerRank< 0 || playerRank>= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        posList.get(playerRank).setPlayerPosition(posList.get(playerRank).getPlayerPosition() - movement);
        for (int i = playerRank+1; i<numPlayer ; i++) {
            if (posList.get(playerRank).getPlayerPosition() <= posList.get(i).getPlayerPosition()) {
                posList.get(playerRank).setPlayerPosition(posList.get(playerRank).getPlayerPosition() - 1);
                posList.get(playerRank).setPlayerRanking(posList.get(playerRank).getPlayerRanking() - 1);
                posList.get(i).setPlayerRanking(posList.get(i).getPlayerRanking() + 1);
            }
        }
    }
    //Array-structure changing method
    public void rearrange() {
        posList.sort((player1, player2) -> {
            if (player1.isLanded() && !player2.isLanded()) {
                return 1;
            } else if (!player1.isLanded() && player2.isLanded()) {
                return -1;
            } else {
                return Integer.compare(player2.getPlayerPosition(), player1.getPlayerPosition());
            }
        });
        for (int i = 0; i < posList.size(); i++) {
            posList.get(i).setPlayerRanking(i + 1);
        }
    }
    public void concludeMovement() {
        Player firstPlayer = null;
        if (posList.size() < numPlayer) {
            System.err.println("Error: posList is either null or does not have enough players0");
            return;
        }
        for (Player player : posList) {
            if (player != null && player.getPlayerRanking() == 1) {
                firstPlayer = player;
                break;
            }
        }
        if (firstPlayer == null) {
            System.err.println("Error: No player with rank 1 found");
            return;
        }
        for (int i = 1; i < Math.min(numPlayer, posList.size()); i++) {
            Player player = posList.get(i) ;
            if (player != null) {
                if (firstPlayer.getPlayerPosition() >= player.getPlayerPosition() + 18 && !player.isLanded()) {
                    earlyLanding(player);
                }
            }
        }
        rearrange();
    }
}