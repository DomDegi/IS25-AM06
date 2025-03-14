package it.polimi.ingsw.galaxytruckerproject;


import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class FlightBoard {
    private final Player[] posList = new Player[4];
    private int numPlayer = 0;
    private int freePodiumPosition = 4;

    public void addToFlightBoard(Player newPlayer) {
        posList[numPlayer] = newPlayer;
        switch (numPlayer) {
            case 1:
                posList[numPlayer].setPlayerPosition(9);
            case 2:
                posList[numPlayer].setPlayerPosition(5);
            case 3:
                posList[numPlayer].setPlayerPosition(2);
            case 4:
                posList[numPlayer].setPlayerPosition(0);
        }
        posList[numPlayer].setPlayerRanking(numPlayer);
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
    public void earlyLanding(int player) {
        posList[player].setPlayerRanking(freePodiumPosition);
        posList[player].setPlayerRanking(freePodiumPosition);
        Player tempPlayer = posList[player];
        posList[player]=posList[freePodiumPosition-1];
        posList[freePodiumPosition-1] = tempPlayer;
        freePodiumPosition--;
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
                if (posList[i].getPlayerPosition()>posList[j].getPlayerPosition()){
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
