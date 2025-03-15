package it.polimi.ingsw.galaxytruckerproject.tiles;
import java.util.*;

import it.polimi.ingsw.galaxytruckerproject.player.Player;

public class ShipBoard {
    protected final Player player; //protected because it need to be called in StartingCabin (Tiles)
    private Optional<Tile>[][] tilesTable;
    private int penaltyTiles ;
    private ArrayList<Tile> bookedTiles;
    private int numBatteries;
    private float singleCannonPower;
    private ArrayList<Coordinates> DoubleCannon;
    private int numSingleEngine;
    private ArrayList<Coordinates> DoubleEngine;
    private ArrayList<Coverage> shields;
    private ArrayList<Coordinates> batteryCoordinates;
    private ArrayList<Coordinates> crewCoordinates;
    private ArrayList<Coordinates> cargoHoldCoordinates;
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
        DoubleCannon= new ArrayList<Coordinates>();
        numSingleEngine = 0;
        DoubleEngine = new ArrayList<Coordinates>();
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
    public ArrayList<Coordinates> getDoubleCannon() {
        return DoubleCannon;
    }
    public int getNumSingleEngine() {
        return numSingleEngine;
    }
    public ArrayList<Coordinates> getDoubleEngine() {
        return DoubleEngine;
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

    //Getter methods for managing the COORDINATES of tile groups of type SHIELD, EQUIP CABIN, and CARGO HOLD.
    public ArrayList<Coordinates> getBatteryCoordinates() {return batteryCoordinates;}
    public ArrayList<Coverage> getCoverageShields(){return shields;}
    public ArrayList<Coordinates> getCargoHoldCoordinates(){return cargoHoldCoordinates;}

    public Optional<Tile>[][] getTilesTable(){return  tilesTable;}

    //METODO INIZIALIZZAZIONE
    public void addBreakSingleCannonPower(float num){
        this.singleCannonPower += num;
    }
    public void addBreakSingleEngine(boolean ab){
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }
    public void addBreakDoubleEngine(boolean ab,Coordinates coordinates){
        if (ab) DoubleEngine.add(coordinates);
        DoubleEngine.remove(coordinates);
    }
    public void addBreakDoubleCannon(boolean ab, Coordinates coordinates){
        if(ab)
        {
            DoubleCannon.add(coordinates);
        }
        else DoubleCannon.remove(coordinates);
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
    public boolean addBookedTile (Tile tile){
        if(bookedTiles.size()==2){
            System.out.println("can't add booked tile");
            return false;
        }
        bookedTiles.add(tile);
        return true;
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
    //true: tile added correctly
    //false: tile occupied
    public boolean positionTile (Optional<Tile> tile, Coordinates coordinates){
        if(tilesTable[coordinates.getX()][coordinates.getY()].isEmpty()){
            tilesTable[coordinates.getX()][coordinates.getY()] = tile;
            return true;
        }
        return false;
    }
/*
    private ThreadLocal<Object> tilesTable() {
    }
*/
    //inizializzazione shipboard volo di prova e primo livello
    public void inizializeLevel2 (){
        tilesTable = new Tile[5][7];
        tilesTable[0][0] = Optional.of(new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH)));
        tilesTable[0][1] = Optional.of(new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH)));
        tilesTable[1][0] = new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
        tilesTable[4][3] = new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
        tilesTable[0][3] = new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
        tilesTable[0][5] = new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
        tilesTable[0][6] = new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));
        tilesTable[1][6] = new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH));

        for(int i = 1; i < 5; i++){
            for(int j = 0; j < 7; j++){
                if(!(tilesTable[i][j].get() instanceof VoidTile)){
                    tilesTable[i][j] = Optional.empty();
                }
            }
        }
    }
    public void inizializeTestFlight(){
        tilesTable = new Optional[5][7];
        // Coordinate delle VoidTile
        int[][] voidPositions = {
                {0,0}, {0,1}, {0,2}, {0,4}, {0,5}, {0,6},
                {1,0}, {1,1}, {1,5}, {1,6},
                {2,0}, {2,6}, {3,0}, {3,6},
                {4,0}, {4,3}, {4,6}
        };

        // Inizializza le VoidTile
        for (int[] pos : voidPositions) {
            tilesTable[pos[0]][pos[1]] = Optional.of(new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH)));
        }

        // Inizializza le caselle riempibili a null
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(tilesTable[i][j].get() instanceof VoidTile)) {
                    tilesTable[i][j] = Optional.empty();
                }
            }
        }
    }



    //destroy algorithm with choice
    public void destroyTile(Coordinates coordinates){
        if(coordinates.x == 0 && coordinates.y == 0){ System.out.println("Can't destroy ");}
        tilesTable[coordinates.x][coordinates.y].get().destroy();
        tilesTable[coordinates.getX()][coordinates.getY()] = Optional.empty();
        Set<Coordinates> set1 = null;
        Set<Coordinates> set2 = null;
        Set<Coordinates> set3 = null;
        Set<Coordinates> set4 = null;
        //southSet
        int x=coordinates.getX();
        int y=coordinates.getY();

        //south
        if(!tilesTable[x+1][y].isEmpty() &&  !(tilesTable[x+1][y].get() instanceof VoidTile)){
            set1=( brokenGraph(new Coordinates(x+1,y)));
        }
        //east
        if(!tilesTable[x][y+1].isEmpty()  &&  !(tilesTable[x][y+1].get() instanceof VoidTile) && (set1==null || !set1.contains(tilesTable[x][y+1].get().getCoordinates()))){
            set2 = brokenGraph(new Coordinates(x,y+1));
        }
        //north
        if(!tilesTable[x-1][y].isEmpty()  &&  !(tilesTable[x-1][y].get() instanceof VoidTile) && (set1==null||  !set1.contains(tilesTable[x-1][y].get().getCoordinates())) && (set2==null || !set2.contains(tilesTable[x-1][y].get().getCoordinates()))){
            set3 = brokenGraph(new Coordinates(x-1,y));
        }
        //west
        if(!tilesTable[x][y-1].isEmpty()  &&  !(tilesTable[x][y-1].get() instanceof VoidTile) && (set1==null || !set1.contains(tilesTable[x][y-1].get().getCoordinates())) && (set2==null || !set2.contains(tilesTable[x][y-1].get().getCoordinates())) && (set3==null || !set3.contains(tilesTable[x][y-1].get().getCoordinates()))){
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
                    tilesTable[c.getX()][c.getY()].get().destroy();
                    tilesTable[c.getX()][c.getY()]=Optional.empty();
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
        if(tilesTable[x][y].get().south.getConnectorsType()!= Connectors.SMOOTH && !tilesTable[x+1][y].isEmpty()  &&  !(tilesTable[x+1][y].get() instanceof VoidTile) && !set.contains(tilesTable[x+1][y].get().getCoordinates())){
            brokenGraph2(new Coordinates(x+1,y), set);
        }
        //east
        if(tilesTable[x][y].get().east.getConnectorsType()!= Connectors.SMOOTH && !tilesTable[x][y+1].isEmpty()  &&  !(tilesTable[x][y+1].get() instanceof VoidTile) && !set.contains(tilesTable[x][y+1].get().getCoordinates())){
            brokenGraph2(new Coordinates(x,y+1), set);
        }
        //north
        if(tilesTable[x][y].get().north.getConnectorsType()!= Connectors.SMOOTH && !tilesTable[x-1][y].isEmpty()  &&  !(tilesTable[x-1][y].get() instanceof VoidTile) && !set.contains(tilesTable[x-1][y].get().getCoordinates())){
            brokenGraph2(new Coordinates(x-1,y), set);
        }
        //west
        if(tilesTable[x][y].get().west.getConnectorsType()!= Connectors.SMOOTH && !tilesTable[x][y-1].isEmpty()  &&  !(tilesTable[x][y-1].get() instanceof VoidTile) && !set.contains(tilesTable[x][y-1].get().getCoordinates())){
            brokenGraph2(new Coordinates(x,y-1), set);
        }
        return set;
    }



    public void countExsposedConnectors(){
        for(int i=0; i<5; i++)
            for(int j=0; j<7; j++){
                checkBorderTile(i,j);
            }
    }
    //ADDITONARY METHOD THAT GETS IMPLEMENTED IN COUNT EXSPOSEDCONNECTORS
    public void checkBorderTile(int i, int j){
        //if the tile at its LEFT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if(i-1<0 || tilesTable[i-1][j].get() instanceof VoidTile || tilesTable[i+1][j].isEmpty())
            if(!tilesTable[i][j].get().getWest().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;

        //if the tile at its RIGHT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if(i+1>6 || tilesTable[i+1][j].get() instanceof VoidTile || tilesTable[i+1][j].isEmpty())
            if(!tilesTable[i][j].get().getEast().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;

        //if the tile UNDER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if(j+1>4 || tilesTable[i][j+1].get() instanceof VoidTile || tilesTable[i][j+1].isEmpty())
            if(!tilesTable[i][j].get().getSouth().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;

        //if the tile OVER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if(j-1<0 || tilesTable[i][j-1].get() instanceof VoidTile || tilesTable[i][j+1].isEmpty())
            if(!tilesTable[i][j].get().getNorth().getConnectorsType().equals(Connectors.SMOOTH))
                penaltyTiles++;
    }

    public ArrayList<Coordinates> verifyCorrectness(){
        ArrayList<Coordinates> array = new ArrayList();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && !tilesTable[i][j].get().isCorrect()) {
                    array.add(new Coordinates(i, j));
                    return array;
                }
            }
        return true;
    }


    public void chooseHowToFillCabins(){
        for(Coordinates coordinates: crewCoordinates){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Isert crewType: ");
            String user = scanner.nextLine();
            CrewType crewType = CrewType.valueOf(user.toUpperCase());
            tilesTable[coordinates.getX()][coordinates.getY()].get().setCrewType(crewType);
        }
    }


    //TRY TO DON'T USE THE INSTANCE OF (I NEED TO RETHINK THE CICLE)
    public void chooseCrewtoRemove(int crewtoRemove){
        for(Coordinates coordinates: crewCoordinates)
            System.out.println(coordinates);
        for(int i=0; i<crewtoRemove; i++){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Insert coordinate X: ");
            int x = scanner.nextInt();
            System.out.println("Insert coordinate Y: ");
            int y = scanner.nextInt();
            if(!(tilesTable[x][y].get() instanceof Cabin)){
                System.out.println("THE TILE IS NOT A CABIN");
                i--;
            }
            else if(((Cabin) tilesTable[x][y]).get().getCrew() == 0){
                System.out.println("THE CABIN IS EMPTY");
                i--;
            }
            else{
                ((EquipCabin) tilesTable[x][y].get()).removeCrew();
            }
        }

        //collegamento tra tiles e numCrew (how to do it?)
    }

    //New ChooseCrew METHOD
    public void chooseCrewtoRemove2(Coordinates coordinates){
        if(crewCoordinates.contains(coordinates)){
            tilesTable[coordinates.getX()][coordinates.getY()].removeCrew();
        }
        else
            System.out.println("THIS TILE IS NOT A CABIN");
    }

    public void chooseBatteryUse(int batteryConsum){
        for(Coordinates coordinates : batteryCoordinates)
            System.out.println(coordinates);

        for(int i=0; i<batteryConsum; i++){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Insert coordinate X: ");
            int x = scanner.nextInt();
            System.out.println("Insert coordinate Y: ");
            int y = scanner.nextInt();
            tilesTable[x][y].get().consumeBattery();
        }
    }

    public void chooseBatteryUse2(Coordinates coordinates){
        if(batteryCoordinates.contains(coordinates)){
            tilesTable[coordinates.getX()][coordinates.getY()].consumeBattery();
        }
        else
            System.out.println("THIS TILE IS NOT A BATTERY");
        //forse non serve il batteryCoordinates perché tanto se non è una batteryTile stampo il fatto che non lo è

    }


    public void epidemic(){
        HashSet<Coordinates> InfectedCabin = new HashSet<>();
        for(Coordinates coordinates : crewCoordinates){
            for(Coordinates coordinates2 : crewCoordinates){
                if((Math.abs(coordinates.getX() - coordinates2.getX())==1 || Math.abs(coordinates.getY() - coordinates2.getY())==1)&& !coordinates.equals(coordinates2)){
                    InfectedCabin.add(coordinates2);
                }
            }
        }
        for(Coordinates coordinates : InfectedCabin){
            tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
        }
    }



    //It returns the Covarage of the Shields Choosen by the Player. If it
    public Coverage chooseShields(Coordinates coordinates){
        if(!(tilesTable[coordinates.getX()][coordinates.getY()].getCoveredArea() == Coverage.NONE)){
            chooseBatteryUse(1);
        }
        else{
            System.out.println("THE TILE IS NOT A SHIELD");
        }
        return tilesTable[coordinates.getX()][coordinates.getY()].getCoveredArea();
    }

    public void chooseCargoStocktoEmpty(Coordinates coordinates){

    }

    //It
    public ArrayList<Coordinates> cargoHoldwithGodd(Goods good){
        ArrayList<Coordinates> cargoHoldwithGood = new ArrayList<>();
        for(Coordinates coordinates : cargoHoldCoordinates){
            if(tilesTable[coordinates.getX()][coordinates.getY()].getCargo().contains(good)){
                cargoHoldwithGood.add(coordinates);
            }
        }
        return cargoHoldwithGood;
    }














}