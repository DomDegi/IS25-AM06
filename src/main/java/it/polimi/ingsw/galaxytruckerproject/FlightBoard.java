package it.polimi.ingsw.galaxytruckerproject;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class FlightBoard {
    private final Player[] posList = new Player[4];
    private int numPlayer = 0;
    private int freePodiumPosition = 4;

    public void addToFlightBoard(Player newPlayer,int pos) {
        posList[pos] = newPlayer;
        switch (pos) {
            case 1:
                posList[pos].setPlayerPosition(9);
            case 2:
                posList[pos].setPlayerPosition(5);
            case 3:
                posList[pos].setPlayerPosition(2);
            case 4:
                posList[pos].setPlayerPosition(0);
        }
        posList[pos].setPlayerRanking(pos);
        numPlayer++;
    }
    public int getNumPlayer() {
        return numPlayer;
    }
    public void setPlayerToLast(Player player) {
        for(int i=player.getPlayerRanking(); i<=numPlayer-1; i++){
            player.setPlayerRanking(numPlayer);
            posList[i].setPlayerRanking(i);
            posList[i-1]=posList[i];
            posList[i]=player;
        }
    }
    public Player[] getRanking() {
        rearrange();
        return posList;
    }
    public void earlyLanding(int playerRanking) {
        posList[playerRanking].setPlayerRanking(freePodiumPosition);
        posList[playerRanking].setLanded(true);
        Player tempPlayer = posList[playerRanking];
        posList[playerRanking]=posList[freePodiumPosition-1];
        posList[freePodiumPosition-1] = tempPlayer;
        freePodiumPosition--;
        rearrange();
    }

    public void moveForward(int playerRank, int movement) {
        playerRank = playerRank - 1;
        posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() + movement);
        for (int i = playerRank-1; i>=0 ; i--) {
            if (posList[playerRank].getPlayerPosition() == posList[i].getPlayerPosition()) {
                posList[playerRank].setPlayerPosition(posList[i].getPlayerPosition() + 1);
            }
        }
    }
    public void moveBackward(int playerRank, int movement) {
        playerRank = playerRank - 1;
        posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() - movement);
        for (int i = playerRank+1; i<numPlayer ; i++) {
            if (posList[playerRank].getPlayerPosition() == posList[i].getPlayerPosition()) {
                posList[playerRank].setPlayerPosition(posList[i].getPlayerPosition() - 1);
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
}
