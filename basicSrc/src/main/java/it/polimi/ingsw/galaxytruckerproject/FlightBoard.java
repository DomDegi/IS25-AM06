package it.polimi.ingsw.galaxytruckerproject;

public class FlightBoard {
    private final Player[] posList = new Player[4];
    private int freePodiumPosition = 4;
    public FlightBoard() {
        //insert initial positioning algorithm
        posList[0].setPlayerPosition(9);
        posList[0].setPlayerRanking(1);

        posList[1].setPlayerPosition(5);
        posList[1].setPlayerRanking(2);

        posList[2].setPlayerPosition(2);
        posList[2].setPlayerRanking(3);

        posList[3].setPlayerPosition(0);
        posList[3].setPlayerRanking(4);
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

    public void moveForward(int player, int movement) {
        posList[player].setPlayerPosition(posList[player].getPlayerPosition() + movement);
        if (posList[player].getPlayerRanking() != 1) {
            for (int i = player - 1; i >= 0 && (posList[player].getPlayerPosition() >= posList[i].getPlayerPosition()); i--) {
                posList[player].setPlayerPosition(posList[player].getPlayerPosition() + 1);
                Player tempPlayer = posList[player];
                posList[player].setPlayerRanking(posList[i].getPlayerRanking());
                posList[i].setPlayerRanking(tempPlayer.getPlayerRanking());
                posList[player]=posList[i];
                posList[i]=tempPlayer;
                player = i;
            }
        }
        if (posList[player].getPlayerRanking() == 1) {
            for (int i = freePodiumPosition-1; i >= 0 ; i--) {
                if ( posList[player].getPlayerPosition() >= posList[i].getPlayerPosition()+18) {
                    earlyLanding(i);
                }
            }
        }
    }

    public void moveBackward(int player, int movement) {
        posList[player].setPlayerPosition(posList[player].getPlayerPosition() - movement);
        if (posList[player].getPlayerRanking() != 4) {
            for (int i = player + 1; i <= 3 && (posList[player].getPlayerPosition() <= posList[i].getPlayerPosition()); i++) {
                posList[player].setPlayerPosition(posList[player].getPlayerPosition() - 1);
                Player tempPlayer = posList[player];
                posList[player].setPlayerRanking(posList[i].getPlayerRanking());
                posList[i].setPlayerRanking(tempPlayer.getPlayerRanking());
                posList[player]=posList[i];
                posList[i]=tempPlayer;
                player = i;
            }
            if ( posList[0].getPlayerPosition() >= posList[player].getPlayerPosition()+18) {
                earlyLanding(player);
            }
        }
    }
}
