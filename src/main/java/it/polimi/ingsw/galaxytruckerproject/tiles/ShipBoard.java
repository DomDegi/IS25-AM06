package it.polimi.ingsw.galaxytruckerproject.tiles;
import java.util.*;

import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.player.Player;


public class ShipBoard {
    protected final Player player; //protected because it need to be called in StartingCabin (Tiles)
    private Optional<Tile>[][] tilesTable;
    private int numExposedConnectors;
    private int penalty;
    private final ArrayList<Tile> bookedTiles;
    private int numBatteries;
    private float singleCannonPower;
    private final ArrayList<Coordinates> DoubleCannon;

    //TO ENNIO: IF THERE'S A REASON TO NOT USE THIS SET UP, FEEL FREE TO RESET EVERYTHING AS IT WAS
    private ArrayList<Coordinates> DoubleStraightCannon;
    private ArrayList<Coordinates> DoubleSideCannon;

    private int numSingleEngine;
    private final ArrayList<Coordinates> DoubleEngine;
    private final ArrayList<Coverage> shields;
    private ArrayList<Coordinates> batteryCoordinates;
    private ArrayList<Coordinates> crewCoordinates;
    private ArrayList<Coordinates> cargoHoldCoordinates;
    private int numBrownAliens;
    private int numPurpleAliens;
    private int numHumanCrew;


    public ShipBoard(Player player) {
        this.player = player;
        this.penalty = 0;
        this.bookedTiles = new ArrayList<>();
        this.numBatteries = 0;
        this.singleCannonPower = 0;
        this.DoubleCannon = new ArrayList<>();
        this.numSingleEngine = 0;
        this.DoubleEngine = new ArrayList<>();
        this.shields = new ArrayList<>();
        this.numBrownAliens = 0;
        this.numPurpleAliens = 0;
        this.numExposedConnectors = 0;
        this.numHumanCrew = 0;
    }

    //GETTER METHODS
    public int getPenalty() {
        return penalty;
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

    public int getNumHumanCrew() { return  numHumanCrew; }

    public int getNumExposedConnectors() {
        return numExposedConnectors;
    }
    public int getDoubleCannonPower(Coordinates coordinates) {
        if(DoubleCannon.contains(coordinates)){
            return tilesTable[coordinates.getX()][coordinates.getY()].get().getStrength();

        }
        return 0;
    }

    //Getter methods for managing the COORDINATES of tile groups of type SHIELD, EQUIP CABIN, and CARGO HOLD.
    public ArrayList<Coordinates> getBatteryCoordinates() {
        return batteryCoordinates;
    }

    public ArrayList<Coverage> getCoverageShields() {
        return shields;
    }

    public ArrayList<Coordinates> getCargoHoldCoordinates() {
        return cargoHoldCoordinates;
    }

    public ArrayList<Coordinates> getCabinsCoordinates() {
        return crewCoordinates;
    }

    public Optional<Tile>[][] getTilesTable() {
        return tilesTable;
    }

    //METODO INIZIALIZZAZIONE
    public void addBreakSingleCannonPower(float num) {
        this.singleCannonPower += num;
    }

    public void addBreakSingleEngine(boolean ab) {
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }

    public void addBreakDoubleEngine(boolean ab, Coordinates coordinates) {
        if (ab) DoubleEngine.add(coordinates);
        DoubleEngine.remove(coordinates);
    }

    public void addBreakDoubleCannon(boolean ab, Coordinates coordinates) {
        if (ab) {
            DoubleCannon.add(coordinates);
        } else DoubleCannon.remove(coordinates);
    }

    public void addBreakBrownAliens(boolean ab) {
        if (ab) numBrownAliens++;
        else numBrownAliens--;
    }

    public void addBreakPurpleAliens(boolean ab) {
        if (ab) numPurpleAliens++;
        else numPurpleAliens--;
    }

    public void addBreakHumanCrew(int num) {
        numHumanCrew += num;
        //Decide which crew to eliminate
    }

    public void addBreakBatteries(int num) {
        numBatteries += num;
        //if (num<0) -> Decide which Battery to use
    }

    public void addPenalty() {
        penalty++;
    }

    public boolean addBookedTile(Tile tile) {
        if (bookedTiles.size() == 2) {
            return false;
        }
        bookedTiles.add(tile);
        return true;
    }

    public ArrayList<Tile> getBookedTiles() {
        return bookedTiles;
    }

    public Tile removeBookedTile(int num) {
        if (num > 1 || num < 0) {
            System.out.println("the tile do not exist");
            return null;
        }
        Tile tile = bookedTiles.get(num);
        bookedTiles.remove(num);
        return tile;
    }

    //true: tile added correctly
    //false: tile occupied
    public boolean positionTile(Optional<Tile> tile, Coordinates coordinates) {
        if (tile.isPresent() && tilesTable[coordinates.getX()][coordinates.getY()].isEmpty()) {
            tilesTable[coordinates.getX()][coordinates.getY()] = tile;
            tile.get().setCoordinates(coordinates);
            tile.get().setShipBoard(this);
            return true;
        }
        return false;
    }

    public Tile getTile(int x, int y) {
        return tilesTable[x][y].get();
    }
    /*
        private ThreadLocal<Object> tilesTable() {
        }
    */
    //inizializzazione shipboard volo di prova e primo livello
    public void initializeLevel2() {
        this.tilesTable = new Optional[5][7];
        //Set empty the normal Tile
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                this.tilesTable[i][j] = Optional.empty();

            }
        }
        int[][] voidPositions = {
                {0, 0}, {0, 1}, {1, 0}, {4, 3}, {0, 3}, {0, 5},
                {0, 6}, {1, 6},
        };

        // Initialize VoidTile
        for (int[] pos : voidPositions) {
            this.tilesTable[pos[0]][pos[1]] = Optional.of(new VoidTile(new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH), new Link(Connectors.SMOOTH)));
        }
    }

    public void initializeTestFlight() {
        tilesTable = new Optional[5][7];
        // Coordinates of VoidTile
        int[][] voidPositions = {
                {0, 0}, {0, 1}, {0, 2}, {0, 4}, {0, 5}, {0, 6},
                {1, 0}, {1, 1}, {1, 5}, {1, 6},
                {2, 0}, {2, 6}, {3, 0}, {3, 6},
                {4, 0}, {4, 3}, {4, 6}
        };

        // Initialize VoidTile
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
        this.tilesTable = tilesTable;
    }

    //destroy tile+ return set of new possible shipboard coordinates
    public ArrayList<Set<Coordinates>> destroyTile(Coordinates coordinates) {
        if (coordinates.x == 0 && coordinates.y == 0) {
            System.out.println("Can't destroy ");
        }
        tilesTable[coordinates.x][coordinates.y].get().destroy();
        tilesTable[coordinates.getX()][coordinates.getY()] = Optional.empty();
        Set<Coordinates> set1 = null;
        Set<Coordinates> set2 = null;
        Set<Coordinates> set3 = null;
        Set<Coordinates> set4 = null;
        //southSet
        int x = coordinates.getX();
        int y = coordinates.getY();

        //south
        if (!tilesTable[x + 1][y].isEmpty() && tilesTable[x + 1][y].get().fillable()) {
            set1 = (brokenGraph(new Coordinates(x + 1, y)));
        }
        //east
        if (!tilesTable[x][y + 1].isEmpty() && tilesTable[x][y + 1].get().fillable() && (set1 == null || !set1.contains(tilesTable[x][y + 1].get().getCoordinates()))) {
            set2 = brokenGraph(new Coordinates(x, y + 1));
        }
        //north
        if (!tilesTable[x - 1][y].isEmpty() && tilesTable[x - 1][y].get().fillable() && (set1 == null || !set1.contains(tilesTable[x - 1][y].get().getCoordinates())) && (set2 == null || !set2.contains(tilesTable[x - 1][y].get().getCoordinates()))) {
            set3 = brokenGraph(new Coordinates(x - 1, y));
        }
        //west
        if (!tilesTable[x][y - 1].isEmpty() && tilesTable[x][y - 1].get().fillable() && (set1 == null || !set1.contains(tilesTable[x][y - 1].get().getCoordinates())) && (set2 == null || !set2.contains(tilesTable[x][y - 1].get().getCoordinates())) && (set3 == null || !set3.contains(tilesTable[x][y - 1].get().getCoordinates()))) {
            set4 = brokenGraph(new Coordinates(x, y - 1));
        }
        ArrayList<Set<Coordinates>> array = new ArrayList<>();
        if (set1 != null) {
            array.add(set1);
        }
        if (set2 != null) {
            array.add(set2);
        }
        if (set3 != null) {
            array.add(set3);
        }
        if (set4 != null) {
            array.add(set4);
        }
        return array;
    }

    public void SetNewShip(Set<Coordinates> set) {
        Coordinates c = new Coordinates(0, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!tilesTable[i][j].isEmpty() && tilesTable[i][j].get().fillable()) {
                    c.set(i, j);
                    if (!set.contains(c)) {
                        tilesTable[i][j].get().destroy();
                        tilesTable[i][j] = Optional.empty();
                    }
                }
            }
        }
    }

    //RETURN THE SET OF TILES LINKED WITH THE STARTING ONE
    //i assume that the correctness of the links has already been verified
    //in the case of construction errors this method will be played for each error
    public Set<Coordinates> brokenGraph(Coordinates start) {
        Set<Coordinates> set = new HashSet<Coordinates>();
        return brokenGraph2(start, set);
    }

    public Set<Coordinates> brokenGraph2(Coordinates start, Set<Coordinates> set) {
        int x = start.x;
        int y = start.y;
        set.add(new Coordinates(x, y));
        //south
        if (tilesTable[x][y].get().south.getConnectorsType() != Connectors.SMOOTH && tilesTable[x + 1][y].isPresent() && tilesTable[x + 1][y].get().fillable() && !set.contains(tilesTable[x + 1][y].get().getCoordinates())) {
            brokenGraph2(new Coordinates(x + 1, y), set);
        }
        //east
        if (tilesTable[x][y].get().east.getConnectorsType() != Connectors.SMOOTH && tilesTable[x][y + 1].isPresent() && tilesTable[x][y + 1].get().fillable() && !set.contains(tilesTable[x][y + 1].get().getCoordinates())) {
            brokenGraph2(new Coordinates(x, y + 1), set);
        }
        //north
        if (tilesTable[x][y].get().north.getConnectorsType() != Connectors.SMOOTH && tilesTable[x - 1][y].isPresent() && tilesTable[x - 1][y].get().fillable() && !set.contains(tilesTable[x - 1][y].get().getCoordinates())) {
            brokenGraph2(new Coordinates(x - 1, y), set);
        }
        //west
        if (tilesTable[x][y].get().west.getConnectorsType() != Connectors.SMOOTH && tilesTable[x][y - 1].isPresent() && tilesTable[x][y - 1].get().fillable() && !set.contains(tilesTable[x][y - 1].get().getCoordinates())) {
            brokenGraph2(new Coordinates(x, y - 1), set);
        }
        return set;
    }

    public boolean checkEarlyLanding() {
        return numHumanCrew == 0;
    }

    //ADDITIONAL METHOD THAT GETS IMPLEMENTED IN COUNT EXSPOSEDCONNECTORS
    public void checkBorderTile(int i, int j) {
        numExposedConnectors = 0;
        //if the tile at its LEFT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if (i - 1 < 0 || !tilesTable[i - 1][j].get().Placeable() || tilesTable[i + 1][j].isEmpty())
            if (!tilesTable[i][j].get().getWest().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile at its RIGHT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if (i + 1 > 6 || !tilesTable[i + 1][j].get().Placeable() || tilesTable[i + 1][j].isEmpty())
            if (!tilesTable[i][j].get().getEast().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile UNDER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if (j + 1 > 4 || tilesTable[i][j + 1].get().Placeable() || tilesTable[i][j + 1].isEmpty())
            if (!tilesTable[i][j].get().getSouth().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile OVER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if (j - 1 < 0 || tilesTable[i][j - 1].get().Placeable() || tilesTable[i][j + 1].isEmpty())
            if (!tilesTable[i][j].get().getNorth().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;
    }

    public int countExposedConnectors() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                checkBorderTile(i, j);
            }
        return numExposedConnectors;
    }

    public boolean verifyCorrectness() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent())
                    if(!tilesTable[i][j].get().isCorrect()) return false;
            }
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent())
                    tilesTable[i][j].get().getStat();
            }
        return true;
    }

    public void chooseHowToFillCabins() {
        for (Coordinates coordinates : crewCoordinates) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Insert crewType: ");
            String user = scanner.nextLine();
            CrewType crewType = CrewType.valueOf(user.toUpperCase());
            tilesTable[coordinates.getX()][coordinates.getY()].get().setCrewType(crewType);
        }
    }

    //New ChooseCrew METHOD
    public boolean chooseCrewToRemove(Coordinates coordinates) {
        if (crewCoordinates.contains(coordinates)) {
            tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
            return true;
        } else
            System.out.println("THIS TILE IS NOT A CABIN");
        return false;
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

    //BATTERY METHODS
    public boolean chooseBatteryUse(Coordinates coordinates){
        if(batteryCoordinates.contains(coordinates)){
            tilesTable[coordinates.getX()][coordinates.getY()].get().consumeBattery();
            return true;
        }
        else
            return false;
        //forse non serve il batteryCoordinates perché tanto se non è una batteryTile stampo il fatto che non lo è
    }

    //SHIELD METHODS
    //This method takes as input the coordinates of the Shield to be used and the coordinates of the BatteryComponents
    // from which it wants to consume the battery to activate the Shield.
    public Coverage chooseShields(Coordinates shieldCoordinates, Coordinates batteryCoordinates){
        if(!(tilesTable[shieldCoordinates.getX()][shieldCoordinates.getY()].get().getCoveredArea() == Coverage.NONE)){
            chooseBatteryUse(batteryCoordinates);
        }
        else{
            System.out.println("THE TILE IS NOT A SHIELD");
        }
        return tilesTable[shieldCoordinates.getX()][shieldCoordinates.getY()].get().getCoveredArea();
    }

    //GOODS METHODS

    //Returns true if the adding of the Good is successfully, false otherwise ()
    public int gainGoods(Goods goods, Coordinates coordinates){
        if(cargoHoldCoordinates.contains(coordinates)){
            if(tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().add(goods))
                return 1;
        }else{
            System.out.println("THIS TILE IS NOT A CARGO HOLDER, input again");
            return -1;
        }
        return 0;

    }

    //IT RETURNS THE COORDINATES OF EVERY CARGO_HOLD THAT CONTAINS A TYPE OF GOOD (RED, YELLOW, GREEN, BLU). IF
    //A CARGO_HOLD CONTAINS MORE THAN ONE GOOD WITH THE SAME COLOR IS GOING TO BE ADD TWICE.
    public ArrayList<Coordinates> cargoHoldContainsGood(Goods goodColor){
        ArrayList<Coordinates> cargoHoldContainsGood = new ArrayList<>();
        for(Coordinates coordinates : cargoHoldCoordinates){
            for(int i=0; i<tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().size(); i++){
                if(tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().contains(goodColor)){
                    System.out.println(coordinates.getX()+" "+coordinates.getY());
                    cargoHoldContainsGood.add(coordinates);
                }
            }
        }
        return cargoHoldContainsGood;
    }

    public void chooseCargoStockToEmpty(Coordinates coordinates){

        Goods colorGoodToCheck = new Goods(GoodsColor.RED);
        switch(colorGoodToCheck.getColor()){
            case RED:
                if(!cargoHoldContainsGood(colorGoodToCheck).isEmpty()){
                    if(cargoHoldContainsGood(colorGoodToCheck).contains(coordinates)){
                        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(colorGoodToCheck);
                    }
                    else {
                        System.out.println("YOU HAVE TO REMOVE A RED GOOD");
                        chooseCargoStockToEmpty(coordinates); //THE METHOD GETS INVOKED UNTIL THE COORDINATES CONTAINS
                        //A RED GOOD (MIMMO)
                    }
                    break;
                }
                colorGoodToCheck = new Goods(GoodsColor.YELLOW);

            case YELLOW:
                if(!cargoHoldContainsGood(colorGoodToCheck).isEmpty()){
                    if(cargoHoldContainsGood(colorGoodToCheck).contains(coordinates)){
                        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(colorGoodToCheck);
                    }
                    else {
                        System.out.println("YOU HAVE TO REMOVE A YELLOW GOOD");
                        chooseCargoStockToEmpty(coordinates);//THE METHOD GETS INVOKED UNTIL THE COORDINATES CONTAINS
                        //A YELLOW GOOD
                    }
                    break;
                }
                colorGoodToCheck = new Goods(GoodsColor.GREEN);

            case GREEN:
                if(!cargoHoldContainsGood(colorGoodToCheck).isEmpty()){
                    if(cargoHoldContainsGood(colorGoodToCheck).contains(coordinates)){
                        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(colorGoodToCheck);
                    }
                    else{
                        System.out.println("YOU HAVE TO REMOVE A GREEN GOOD");
                        chooseCargoStockToEmpty(coordinates);//THE METHOD GETS INVOKED UNTIL THE COORDINATES CONTAINS
                        //A GREEN GOOD
                    }
                    break;
                }
            case BLUE:
                if(!cargoHoldContainsGood(colorGoodToCheck).isEmpty()){
                    if(cargoHoldContainsGood(colorGoodToCheck).contains(coordinates)){
                        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(colorGoodToCheck);
                    }
                    else{
                        System.out.println("YOU HAVE TO REMOVE A BLUE GOOD");
                        chooseCargoStockToEmpty(coordinates);//THE METHOD GETS INVOKED UNITL THE COORDINATES CONTAINS
                        //A BLUE GOOD
                    }
                    break;
                }
            //THE CASE OF CHECKING IF THERE IS ANY GOOD, CAN BE DONE BY CALLING THIS METHOD BY ADDING A DEFAULT CASE,
            // BUT I DON'T THINK IT MAKE SENSE SINCE THIS METHOD NEED TO RECEIVE A COORDINATES.
            //THE CHECK IS GOING TO BE DONE BY THE GAME (WHERE IT GETS CHECKED IF THE NUMBER OF GOODS THAT NEED TO BE
            //REMOVED ARE EQUAL TO THE ONE THE PLAYER HAS. IN THAT CASE THEY ARE JUST GOING TO DELETE ALL THE GOODS.
        }

    }

    public void removeGood(Goods good, Coordinates coordinates){
        tilesTable[coordinates.getX()][coordinates.getY()].get().removeGood(good);
    }

    public void swapGoods(Coordinates coordinatesFrom,Coordinates coordinatesTo, Goods goodToSwap ){
        //Check if the coordinates are of a CargoHolder
        if(cargoHoldCoordinates.contains(coordinatesFrom) && cargoHoldCoordinates.contains(coordinatesTo)){
            if(cargoHoldCoordinates.contains(goodToSwap)){
               if(gainGoods(goodToSwap, coordinatesTo))
                    removeGood(goodToSwap, coordinatesFrom);
            }
            else{
                System.out.println("This Good is not present in the CargoHold you selected");
            }
            tilesTable[coordinatesFrom.getX()][coordinatesFrom.getY()].get().getCargo().contains(goodToSwap);
        }
        else{
            System.out.println("ONE OR BOTH THE TWO TILES ARE NOT A CARGOHOLDER");
        }


    }

    public int convertGoodsToCredit(){
        int credit = 0;
        Goods good = new Goods(GoodsColor.RED);
        credit = 4 * cargoHoldContainsGood(good).size();
        good = new Goods(GoodsColor.YELLOW);
        credit += 3 * cargoHoldContainsGood(good).size();
        good = new Goods(GoodsColor.GREEN);
        credit += 2 * cargoHoldContainsGood(good).size();
        good = new Goods(GoodsColor.BLUE);
        credit += cargoHoldContainsGood(good).size();
        return credit;
    }

    //if these methods finds no goods in cargo holds, returns false
    public boolean isCargoEmpty() {
        for (Coordinates coordinates : cargoHoldCoordinates) {
            if (!tilesTable[coordinates.x][coordinates.getY()].get().getCargo().isEmpty())
                return false;
        }
        return true;
    }

    //EXAMPLE OF WHAT IT NEEDS TO BE IMPLEMENTED, MAYBE I NEED TO GIVE TO PLAYER THE NUMBER OF RED,YELLOW, GREEN, BLU GOODS
    public void checkBeforeAsking(int goodsToRemove){
        int numberOfGoods = 0;
        //Check if the goods that need to be deleted are more/equal/or less than the goods contained in the Ship
        for(Coordinates coordinates : cargoHoldCoordinates){
            numberOfGoods += tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().size();
        }
        //CASE NumberOfGoods equals goods that need to be removed
        if(goodsToRemove == numberOfGoods){
            for(Coordinates coordinates : cargoHoldCoordinates){
                tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().clear();
            }
        }
        else if(goodsToRemove > numberOfGoods){
            ;

        }


    }

    //give back all the player Goods
    public ArrayList<Goods> getAllGoods(){
        ArrayList<Goods> goods = new ArrayList<>();
        int i;
        Goods good = new Goods(GoodsColor.RED);
        for(i=cargoHoldContainsGood(good).size();i==0;i--){
            goods.add(good);
        }
        good = new Goods(GoodsColor.YELLOW);
        for(i=cargoHoldContainsGood(good).size();i==0;i--){
            goods.add(good);

        }
        good = new Goods(GoodsColor.GREEN);
        for(i=cargoHoldContainsGood(good).size();i==0;i--){
            goods.add(good);
        }
        good = new Goods(GoodsColor.BLUE);
        for(i=cargoHoldContainsGood(good).size();i==0;i--){
            goods.add(good);
        }
        return goods;
    }
    public ArrayList<Goods> getSingleCargoGoods(Coordinates coordinatesToFind){
        ArrayList<Goods> goods = new ArrayList<>();
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.RED))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.RED));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.RED))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.YELLOW));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.RED))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.GREEN));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.RED))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.BLUE));
            }
        }
        return goods;
    }

}