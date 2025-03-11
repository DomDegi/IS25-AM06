package it.polimi.ingsw.galaxytruckerproject;

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
    public void setPlayerToLast(Player player) {
        for(int i=player.getPlayerRanking(); i<=numPlayer-1; i++){
            player.setPlayerRanking(numPlayer);
            posList[i].setPlayerRanking(i);
            posList[i-1]=posList[i];
            posList[i]=player;
        }
    }
    public Player[] getRanking() {
        return posList;
    }
    public void earlyLanding(int player) {
        posList[player].setPodium(freePodiumPosition);
        posList[player].setPlayerRanking(freePodiumPosition);
        Player tempPlayer = posList[player];
        posList[player]=posList[freePodiumPosition-1];
        posList[freePodiumPosition-1] = tempPlayer;
        freePodiumPosition--;
    }
    public void moveForward(int playerRank, int movement) {
        playerRank--;
        posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() + movement);
        if (posList[playerRank].getPlayerRanking() != 1) {
            for (int i = playerRank - 1; i >= 0 && (posList[playerRank].getPlayerPosition() >= posList[i].getPlayerPosition()); i--) {
                posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() + 1);
                Player tempPlayer = posList[playerRank];
                posList[playerRank].setPlayerRanking(posList[i].getPlayerRanking());
                posList[i].setPlayerRanking(tempPlayer.getPlayerRanking());
                posList[playerRank]=posList[i];
                posList[i]=tempPlayer;
                playerRank = i;
            }
        }
        if (posList[playerRank].getPlayerRanking() == 1) {
            for (int i = freePodiumPosition-1; i >= 0 ; i--) {
                if ( posList[playerRank].getPlayerPosition() >= posList[i].getPlayerPosition()+18) {
                    earlyLanding(i);
                }
            }
        }
    }
    public void moveBackward(int playerRank, int movement) {
        playerRank--;
        posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() - movement);
        if (posList[playerRank].getPlayerRanking() != 4) {
            for (int i = playerRank + 1; i <= 3 && (posList[playerRank].getPlayerPosition() <= posList[i].getPlayerPosition()); i++) {
                posList[playerRank].setPlayerPosition(posList[playerRank].getPlayerPosition() - 1);
                Player tempPlayer = posList[playerRank];
                posList[playerRank].setPlayerRanking(posList[i].getPlayerRanking());
                posList[i].setPlayerRanking(tempPlayer.getPlayerRanking());
                posList[playerRank]=posList[i];
                posList[i]=tempPlayer;
                playerRank = i;
            }
            if ( posList[0].getPlayerPosition() >= posList[playerRank].getPlayerPosition()+18) {
                earlyLanding(playerRank);
            }
        }
    }
}
