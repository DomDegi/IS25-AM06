package it.polimi.ingsw.galaxytruckerproject.tiles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
    private ArrayList<Coordinates> batteryCoordinates;
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
    public ArrayList<Coordinates> getBatteryCoordinates() {return batteryCoordinates;}
    public ArrayList<Coverage> getCoverageShields(){return shields;}
    public Tile[][] getTilesTable(){return tilesTable;}
    public float getCannonStrenght(){
        float fireStrenght=singleCannonPower;
        System.out.println("you have "+numStraightDoubleCannon+" straight double cannons, how many of them will be fired?");
        Scanner scanner = new Scanner(System.in);
        int i=scanner.nextInt();
        if(i<=numStraightDoubleCannon)
        {
            fireStrenght+= 2*i;
            chooseBatteryUse(-i);
        }
        else System.out.println("error: too many uses");
        System.out.println("you have "+numSidewaysDoubleCannon+" straight double cannons, how many of them will be fired?");
        i=scanner.nextInt();
        if(i<=numSidewaysDoubleCannon)
        {
            fireStrenght+= i;
            chooseBatteryUse(-i);
        }
        else System.out.println("error: too many uses");
        if (fireStrenght>0)
        {
            fireStrenght += 2*numPurpleAliens;
        }
        return fireStrenght;

    }
    public int getEngineStrenght(){
        int engineStrenght=numSingleEngine;
        System.out.println("you have "+numDoubleEngine+" straight double cannons, how many of them will be fired?");
        Scanner scanner = new Scanner(System.in);
        int i=scanner.nextInt();
        if(i<=numDoubleEngine)
        {
            engineStrenght+= 2*i;
            chooseBatteryUse(-i);
        }
        else System.out.println("error: too many uses");
        if (engineStrenght>0)
        {
            engineStrenght += 2*numBrownAliens;
        }
        return engineStrenght;

    }



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
    public void addBookedTile (Tile tile){
        if(bookedTiles.size()==2){
            System.out.println("can't add booked tile");
            return;
        }
        bookedTiles.add(tile);
        return;
    }
    public Tile removeBookedTile (int num){
        if(num>1||num<0)
        {
            System.out.println("the tile do not exist");
            return null;
        }
        Tile tile = bookedTiles.get(num);
        bookedTiles.remove(num);
        return tile;
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
                if (!(tilesTable[i][j] instanceof VoidTile)) {
                    tilesTable[i][j] = null;
                }
            }
        }
    }

    //destroy algorithm with choice
    public void destroyTile(Coordinates coordinates){
        if(coordinates.x == 0 && coordinates.y == 0){ System.out.println("Can't destroy ");}
        tilesTable[coordinates.x][coordinates.y].destroy();
        tilesTable[coordinates.getX()][coordinates.getY()] = null;
        Set<Coordinates> set1 = null;
        Set<Coordinates> set2 = null;
        Set<Coordinates> set3 = null;
        Set<Coordinates> set4 = null;
        //southSet
        int x=coordinates.getX();
        int y=coordinates.getY();

        //south
        if(tilesTable[x+1][y]!= null  &&  !(tilesTable[x+1][y] instanceof VoidTile)){
            set1=( brokenGraph(new Coordinates(x+1,y)));
        }
        //east
        if(tilesTable[x][y+1]!= null  &&  !(tilesTable[x][y+1] instanceof VoidTile) && !set1.contains(tilesTable[x][y+1])){
            set2 = brokenGraph(new Coordinates(x,y+1));
        }
        //north
        if(tilesTable[x-1][y]!= null  &&  !(tilesTable[x-1][y] instanceof VoidTile) && !set1.contains(tilesTable[x-1][y]) && !set2.contains(tilesTable[x-1][y])){
            set3 = brokenGraph(new Coordinates(x-1,y));
        }
        //west
        if(tilesTable[x][y-1]!= null  &&  !(tilesTable[x][y-1] instanceof VoidTile) && !set1.contains(tilesTable[x][y-1]) && !set2.contains(tilesTable[x][y-1]) && !set3.contains(tilesTable[x][y-1])){
           set4 = brokenGraph(new Coordinates(x,y-1));
        }
        ArrayList<Set<Coordinates>> array = new ArrayList<>();
        System.out.println("choose a tile set:");
        if(set1!=null){
            array.add(set1);
            System.out.println ("set"+array.size() );
            for(Coordinates c : set1) c.print();
            System.out.print("\n");
        }
        if(set2!=null){
            array.add(set2);
            System.out.println ("set"+array.size());
            for(Coordinates c : set2) c.print();
            System.out.print("\n");
        }
        if(set3!=null){
            array.add(set3);
            System.out.println ("set"+array.size());
            for(Coordinates c : set3) c.print();
            System.out.print("\n");
        }
        if(set4!=null){
            array.add(set4);
            System.out.println ("set"+array.size());
            for(Coordinates c : set4) c.print();
            System.out.print("\n");
        }
        System.out.println("which number do you choose?");
        System.out.print("set");
        Scanner scanner = new Scanner(System.in);
        int i=scanner.nextInt();
        i--;
        for(int j = 0; j < array.size(); j++){
            if(j!=i)
            {
                for(Coordinates c : array.get(j)){
                    tilesTable[c.getX()][c.getY()].destroy();
                    tilesTable[c.getX()][c.getY()] = null;

                }
            }
        }


    }

    //RETURN THE SET OF TILES LINKED WITH THE STARTING ONE
    //i assume that the correctness of the links has already been verified
    //in the case of construction errors this method will be played for each error
    public Set<Coordinates> brokenGraph (Coordinates start){
        Set<Coordinates> set = new HashSet<Coordinates>();
        return brokenGraph2(start, set);
    }
    public Set<Coordinates> brokenGraph2 (Coordinates start, Set<Coordinates> set ){
        int x = start.x;
        int y = start.y;
        set.add(new Coordinates(x,y));
        //south
        if(tilesTable[x][y].south.getConnectorsType()!= Connectors.SMOOTH && tilesTable[x+1][y]!= null  &&  !(tilesTable[x+1][y] instanceof VoidTile) && !set.contains(tilesTable[x+1][y])){
            brokenGraph2(new Coordinates(x+1,y), set);
        }
        //east
        if(tilesTable[x][y].east.getConnectorsType()!= Connectors.SMOOTH && tilesTable[x][y+1]!= null  &&  !(tilesTable[x][y+1] instanceof VoidTile) && !set.contains(tilesTable[x][y+1])){
            brokenGraph2(new Coordinates(x,y+1), set);
        }
        //north
        if(tilesTable[x][y].nord.getConnectorsType()!= Connectors.SMOOTH && tilesTable[x-1][y]!= null  &&  !(tilesTable[x-1][y] instanceof VoidTile) && !set.contains(tilesTable[x-1][y])){
            brokenGraph2(new Coordinates(x-1,y), set);
        }
        //west
        if(tilesTable[x][y].west.getConnectorsType()!= Connectors.SMOOTH && tilesTable[x][y-1]!= null  &&  !(tilesTable[x][y-1] instanceof VoidTile) && !set.contains(tilesTable[x][y-1])){
            brokenGraph2(new Coordinates(x,y-1), set);
        }
        return set;
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

    public void verifyCorretness(){
        Scanner scanner = new Scanner(System.in);
        int x =300;
        int y =300;
        ArrayList<Coordinates> array = new ArrayList();
        for(int i=0; i<5; i++)
            for(int j=0; j<7; j++){
                if(!tilesTable[i][j].isCorrect())
                    array.add(new Coordinates(i,j));
            }
        if(array.size()==0)
        {
            System.out.println("the shipboard is correct");
            return;
        }
        System.out.println("the wrong tiles are:");
        for(Coordinates c : array){
            System.out.println("x: "+c.x+" y: "+c.y);
        }
        System.out.println("choose to destroy");
        x=scanner.nextInt();
        y=scanner.nextInt();

    }

    //Il metodo riceve in ingresso le coordinate della casella, e il numero di crewmate che vuole eliminare da
    //quela casella. Il giocatore infatti, nel caso in cui debba eliminarne due, ha la possibilità di rimuovere
    // uno solo cremate da due caselle
    public void chooseCrewtoRemove(Coordinates coordinatesCrew, int crewtoRemove){
        if(tilesTable[coordinatesCrew.x][coordinatesCrew.y] instanceof Cabin){

        }
        //collegamento tra tiles e numCrew (how to do it?)
    }



    public void chooseBatteryUse(int batteryConsum){
        for(Coordinates coordinates : batteryCoordinates)
            System.out.println(coordinates);

        for(int i=0; i<batteryConsum; i++) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Insert coordinate X: ");
            int x = scanner.nextInt();
            System.out.println("Insert coordinate Y: ");
            int y = scanner.nextInt();
            if(!(tilesTable[x][y] instanceof BatteryComponents)){
                System.out.println("THE TILE IS NOT A BATTERYCOMPONENT");
                i--;
            }
            else if(((BatteryComponents) tilesTable[x][y]).getNumBatteries() == 0){
                System.out.println("THE TILE IS NOT A BATTERYCOMPONENT");
                i--;
            }
            else{
                ((BatteryComponents) tilesTable[x][y]).consumeNumBatteries();
            }
        }
    }


}