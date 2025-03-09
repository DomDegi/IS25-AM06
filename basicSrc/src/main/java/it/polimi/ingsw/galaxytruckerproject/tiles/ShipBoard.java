package it.polimi.ingsw.galaxytruckerproject.tiles;
import java.util.ArrayList;
import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class ShipBoard {
    protected final Player player; //protected because it need to be called in StartingCabin (Tiles)
    private Tile[][] tilesTable;
    private int penaltyTiles ;
    private ArrayList<Tile> bookedTiles;
    private int numBatteries;
    private float singleCannonPower;
    private int numStraightDoubleCannon;
    private int numSidewaysDoubleCannon;
    private int numSingleEngine;
    private int numDoubleEngine;
    private ArrayList<Coverage> shields;
    private int numBrownAliens;
    private int numPurpleAliens;
    private int numExposedConnectors ;
    private int numCrew;


    public ShipBoard(Player player){
        this.player = player;
        penaltyTiles = 0;
        bookedTiles = new ArrayList<Tile>();
        numBatteries = 0;
        singleCannonPower = 0;
        numStraightDoubleCannon = 0;
        numSidewaysDoubleCannon = 0;
        numSingleEngine = 0;
        numDoubleEngine = 0;
        shields = new ArrayList<Coverage>();
        numBrownAliens = 0;
        numPurpleAliens = 0;
        numExposedConnectors = 0;
        numCrew = 0;
    }
    //GETTER METHODS
    public int getNumPenaltyTiles (){
        return penaltyTiles;
    }
    public int getNumBatteries() {
        return numBatteries;
    }
    public float getSingleCannonPower() {
        return singleCannonPower;
    }
    public int getNumStraightDoubleCannon() {
        return numStraightDoubleCannon;
    }
    public int getNumSidewaysDoubleCannon() {
        return numSidewaysDoubleCannon;
    }
    public int getNumSingleEngine() {
        return numSingleEngine;
    }
    public int getNumDoubleEngine() {
        return numDoubleEngine;
    }
    public int getNumBrownAliens() {
        return numBrownAliens;
    }
    public int getNumPurpleAliens() {
        return numPurpleAliens;
    }
    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }
    public ArrayList<Coverage> getCoverageShields(){return shields;}

    //METODO INIZIALIZZAZIONE
    public void addBreakSingleCannonPower(float num){
        this.singleCannonPower += num;
    }
    public void addBreakSingleEngine(boolean ab){
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }
    public void addBreakDoubleEngine(boolean ab){
        if (ab) numDoubleEngine++;
        else numDoubleEngine--;

    }
    public void addBreakStraightDoubleCannon(boolean ab){
        if (ab) numStraightDoubleCannon++;
        else numStraightDoubleCannon--;
    }
    public void addBreakSidewaysDoubleCannon(boolean ab){
        if (ab) numSidewaysDoubleCannon++;
        else numSidewaysDoubleCannon--;
    }
    public void addBreakBrownAliens(boolean ab){
        if (ab) numBrownAliens++;
        else numBrownAliens--;
    }
    public void addBreakPurpleAliens(boolean ab){
        if (ab) numPurpleAliens++;
        else numPurpleAliens--;
    }
    public void addBreakCrew(int num){
        numCrew += num;
        //Decide which crewmate to eliminate
    }
    public void addBreakBatteries(int num){
        numBatteries += num;
        //if (num<0) -> Decide which Battery to use
    }

    //inizializzazione shipboard volo di prova e primo livello
    public void inizializeLevel2 (){
        tilesTable = new Tile[5][7];
        tilesTable[0][0] = new VoidTile();
        tilesTable[0][1] = new VoidTile();
        tilesTable[1][0] = new VoidTile();
        tilesTable[4][3] = new VoidTile();
        tilesTable[0][3] = new VoidTile();
        tilesTable[0][5] = new VoidTile();
        tilesTable[0][6] = new VoidTile();
        tilesTable[1][6] = new VoidTile();

        for(int i = 1; i < 5; i++){
            for(int j = 0; j < 7; j++){
                if(!(tilesTable[i][j] instanceof VoidTile)){
                    tilesTable[i][j] = null;
                }
            }
        }
    }
    public void inizializeTestFlight(){
        // Coordinate delle VoidTile
        int[][] voidPositions = {
                {0,0}, {0,1}, {0,2}, {0,4}, {0,5}, {0,6},
                {1,0}, {1,1}, {1,5}, {1,6},
                {2,0}, {2,6}, {3,0}, {3,6},
                {4,0}, {4,3}, {4,6}
        };

        // Inizializza le VoidTile
        for (int[] pos : voidPositions) {
            tilesTable[pos[0]][pos[1]] = new VoidTile();
        }

        // Inizializza le caselle riempibili a null
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j] == null) {
                    tilesTable[i][j] = null;
                }
            }
        }
    }

    //I am not sure about this method
    public void destroyTile(Coordinates coordinates){
        if(coordinates.x == 0 && coordinates.y == 0){ System.out.println("Can't destroy ");}
        tilesTable[coordinates.x][coordinates.y].destroy();
        tilesTable[coordinates.getX()][coordinates.getY()] = null;
    }

    public void checkBorderTile(int i, int j){

        //if the tile at its LEFT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if(i-1<0 || tilesTable[i-1][j] instanceof VoidTile || tilesTable[i+1][j].equals(null))
            if(!tilesTable[i][j].getWest().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;

        //if the tile at its RIGHT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if(i+1>6 || tilesTable[i+1][j] instanceof VoidTile || tilesTable[i+1][j].equals(null))
            if(!tilesTable[i][j].getEast().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;

        //if the tile UNDER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if(j+1>4 || tilesTable[i][j+1] instanceof VoidTile || tilesTable[i][j+1].equals(null))
            if(!tilesTable[i][j].getSouth().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;

        //if the tile OVER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if(j-1<0 || tilesTable[i][j-1] instanceof VoidTile || tilesTable[i][j+1].equals(null))
            if(!tilesTable[i][j].getNord().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;
    }
    public void countExsposedConnectors(){
        for(int i=0; i<5; i++)
            for(int j=0; j<7; j++){
                checkBorderTile(i,j);
            }
    }

    //Il metodo riceve in ingresso le coordinate della casella, e il numero di crewmate che vuole eliminare da
    //quela casella. Il giocatore infatti, nel caso in cui debba eliminarne due, ha la possibilità di rimuovere
    // uno solo cremate da due caselle
    public void chooseCrewtoRemove(Coordinates coordinatesCrew, int crewtoRemove){
        if(tilesTable[coordinatesCrew.x][coordinatesCrew.y] instanceof Cabin){
            //
        }
        //collegamento tra tiles e numCrew (how to do it?)
    }

}