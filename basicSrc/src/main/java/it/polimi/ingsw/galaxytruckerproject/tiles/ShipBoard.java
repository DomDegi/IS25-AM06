package it.polimi.ingsw.galaxytruckerproject.tiles;
import java.util.ArrayList;

public class ShipBoard {
    private Player player;
    private Tile[][] tilesTable;
    private int penaltyTiles ;
    private  Arraylist<Tile> bookedTiles;
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
    private int crew ;


    public ShipBoard(Player player){
        this.player = player;
        penaltyTiles = 0;
        bookedTiles = new ArrayList<Tile>;
        numBatteries = 0;
        singleCannonPower = 0;
        numStraightDoubleCannon = 0;
        numSidewaysDoubleCannon = 0;
        numSingleEngine = 0;
        numDoubleEngine = 0;
        shields = new ArrayList<Coverage>;
        numBrownAliens = 0;
        numPurpleAliens = 0;
        numExposedConnectors = 0;
        crew = 0;
    }

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
    public void add_breakSingleCannonPower(float num){
        this.singleCannonPower += num;
    }
    public void add_breakSingleEngine(boolean ab){
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }
    public void add_breakDoubleEngine(boolean ab){
        if (ab) numDoubleEngine++;
        else numDoubleEngine--;

    }
    public void add_breakStraightDoubleCannon(boolean ab){
        if (ab) numStraightDoubleCannon++;
        else numStraightDoubleCannon--;
    }
    public void add_breakSidewaysDoubleCannon(boolean ab){
        if (ab) numSidewaysDoubleCannon++;
        else numSidewaysDoubleCannon--;
    }

    public void add_breakBrownAliens(boolean ab){
        if (ab) numBrownAliens++;
        else numBrownAliens--;
    }

    public void add_breakPurpleAliens(boolean ab){
        if (ab) numPurpleAliens++;
        else numPurpleAliens--;
    }

    public void add_breakCrew(int num){
        crew += num;
        //Decide which crewmate to eliminate
    }

    public void add_breakBatteries(int num){
        numBatteries += num;
        //if (num<0) -> Decide which Battery to use
    }

    public void inizializeLevel2Ship (){
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

}

public void inizializeLevel1Ship (){
    tilesTable = new Tile[5][7];
    tilesTable[0][0] = new VoidTile();
    tilesTable[0][1] = new VoidTile();
    tilesTable[0][2] = new VoidTile();
    tilesTable[0][4] = new VoidTile();
    tilesTable[0][5] = new VoidTile();
    tilesTable[0][6] = new VoidTile();
    tilesTable[1][0] = new VoidTile();
    tilesTable[1][1] = new VoidTile();
    tilesTable[1][5] = new VoidTile();
    tilesTable[1][6] = new VoidTile();
    tilesTable[2][0] = new VoidTile();
    tilesTable[2][6] = new VoidTile();
    tilesTable[3][0] = new VoidTile();
    tilesTable[3][6] = new VoidTile();
    tilesTable[4][0] = new VoidTile();
    tilesTable[4][3] = new VoidTile();
    tilesTable[4][6] = new VoidTile();

    for(int i = 1; i < 5; i++){
        for(int j = 0; j < 7; j++){
            if(!(tilesTable[i][j] instanceof VoidTile)){
                tilesTable[i][j] = null;
            }
        }
    }
}

}
