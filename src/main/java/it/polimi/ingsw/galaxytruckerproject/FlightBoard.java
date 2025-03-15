package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class FlightBoard {
    private final Player[] posList = new Player[4];
    private int numPlayer = 0;
    private int freePodiumPosition=1;

    public void addToFlightBoard(Player newPlayer,int pos) {
        pos = pos - 1;
        posList[pos] = newPlayer;
        switch (pos) {
            case 0:
                posList[pos].setPlayerPosition(9);
                break;
            case 1:
                posList[pos].setPlayerPosition(5);
                break;
            case 2:
                posList[pos].setPlayerPosition(2);
                break;
            case 3:
                posList[pos].setPlayerPosition(0);
                break;
            default:
                throw new IllegalArgumentException("Invalid position: " + pos);
        }
        posList[pos].setPlayerRanking(pos);
        numPlayer++;
        freePodiumPosition++;
    }
    public int getNumPlayer() {
        return numPlayer;
    }
    public void setPlayerToLast(Player player) {
        int currentIndex = player.getPlayerRanking() - 1; // Ranking is 1-based
        if (currentIndex < 0 || currentIndex >= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + player.getPlayerRanking());
        }
        for (int i = currentIndex; i < numPlayer - 1; i++) {
            posList[i] = posList[i + 1]; // Shift players forward
            posList[i].setPlayerRanking(i + 1); // Update rankings
        }
        posList[numPlayer - 1] = player; // Move the player to last
        player.setPlayerRanking(numPlayer); // Update their rank
    }
    public Player[] getRanking() {
        rearrange();
        return posList;
    }
    public void earlyLanding(int playerRank) {
        playerRank = playerRank - 1;
        if (playerRank < 0 || playerRank >= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        if (freePodiumPosition >= posList.length || freePodiumPosition <= 0) {
            throw new IllegalStateException("freePodiumPosition out of bounds: " + freePodiumPosition);
        }
        posList[playerRank].setPlayerRanking(freePodiumPosition);
        posList[playerRank].setLanded(true);
        Player tempPlayer = posList[playerRank];
        posList[playerRank] = posList[freePodiumPosition - 1];
        posList[freePodiumPosition - 1] = tempPlayer;
        freePodiumPosition--;
        rearrange();
    }

    public void moveForward(int playerRank, int movement) {
        playerRank = playerRank - 1;
        if (playerRank< 0 || playerRank>= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() + movement);
        for (int i = playerRank-1; i>=0 ; i--) {
            if (posList[playerRank].getPlayerPosition() >= posList[i].getPlayerPosition()) {
                posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() + 1);
            }
        }
    }
    public void moveBackward(int playerRank, int movement) {
        playerRank = playerRank - 1;
        if (playerRank< 0 || playerRank>= numPlayer) {
            throw new IllegalArgumentException("Invalid player ranking: " + playerRank+1);
        }
        posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() - movement);
        for (int i = playerRank+1; i<numPlayer ; i++) {
            if (posList[playerRank].getPlayerPosition() <= posList[i].getPlayerPosition()) {
                posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() - 1);
            }
        }
    }
    public void rearrange(){
        for(int i=0;i<numPlayer;i++){
            for(int j=0;j<numPlayer;j++){
                if (posList[i].getPlayerPosition()>posList[j].getPlayerPosition()&&(!posList[i].isLanded()&&!posList[j].isLanded())){
                    Player tempPlayer = posList[i];
                    posList[i]=posList[j];
                    posList[j]= tempPlayer;
                    posList[i].setPlayerRanking(i+1);
                    posList[j].setPlayerRanking(j+1);
                }
            }
        }
    }

    public void concludeMovement() {
        Player firstPlayer = null;
        if (posList.length < numPlayer) {
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
        for (int i = 1; i < Math.min(numPlayer, posList.length); i++) {
            Player player = posList[i];
            if (player != null) {
                if (firstPlayer.getPlayerPosition() >= player.getPlayerPosition() + 18 && !player.isLanded()) {
                    earlyLanding(i);
                }
            }
        }
        rearrange();
    }
}
