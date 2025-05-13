package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LightShipBoard implements ShipBoardInterface , Remote {

    //Variable that saves the reference to the ShipBoard present in the model along with all the game logic
    private ShipBoard shipBoard;

    protected final LightPlayer player; //protected because it need to be called in StartingCabin (Tiles)
    private Optional<Tile>[][] tilesTable;
    private int penalty;
    private ArrayList<Tile> bookedTiles;
    private int numExposedConnectors;
    private int numBatteries;
    private float singleCannonPower;
    private ArrayList<Coordinates> DoubleCannon;
    private ArrayList<Coordinates> cargoHoldCoordinates;

    private int numSingleEngine;
    private ArrayList<Coordinates> DoubleEngine;
    private ArrayList<Coverage> shields;
    private ArrayList<Coordinates> batteryCoordinates;
    private ArrayList<Coordinates> crewCoordinates;

    private int numBrownAliens;
    private int numPurpleAliens;
    private int numHumanCrew;
    private int credit;
    private boolean first=true;


    public LightShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        this.bookedTiles = new ArrayList<>();
        this.cargoHoldCoordinates = new ArrayList<>();
        this.crewCoordinates = new ArrayList<>();
        this.credit = 0;
        this. player = new LightPlayer(this.shipBoard.getPlayer());
        this.tilesTable = shipBoard.getTilesTable();
    }

    public LightShipBoard(LightPlayer player) {
        this.player = player;
        this.player.setPlayerShip(this);
        this.penalty = 0;
        this.bookedTiles = new ArrayList<Tile>();
        this.numBatteries = 0;
        this.singleCannonPower = 0;
        this.DoubleCannon = new ArrayList<Coordinates>();
        this.batteryCoordinates= new ArrayList<Coordinates>();
        this.crewCoordinates=  new ArrayList<Coordinates>();
        this.cargoHoldCoordinates= new ArrayList<Coordinates>();
        this.numSingleEngine = 0;
        this.DoubleEngine = new ArrayList<Coordinates>();
        this.shields = new ArrayList<Coverage>();
        this.numBrownAliens = 0;
        this.numPurpleAliens = 0;
        this.numExposedConnectors = 0;
        this.numHumanCrew = 0;
        this.credit = 0;
    }

    //the Client will intialize which level he wants to play. Then he's going to comunicate it to ShipBoard in the
    //Server, which is also going to call the same method
    public void initializeTestFlight() {
        this.tilesTable = new Optional[5][7];
        // Inizializza le caselle riempibili a null
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                this.tilesTable[i][j] = Optional.empty();

            }
        }
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
        Tile tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL), 0);
        positionTile(Optional.of(tile), new Coordinates(2, 3));

        this.tilesTable = tilesTable;
    }
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
        Tile tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL), 0);
        positionTile(Optional.of(tile), new Coordinates(2, 3));
    }

    public LightPlayer getPlayer() {
        return player;
    }

    public void addBreakSingleCannonPower(float num) {
        this.singleCannonPower += num;
    }

    public void addBreakSingleEngine(boolean ab) {
        if (ab) numSingleEngine++;
        else numSingleEngine--;
    }

    public void addBreakDoubleEngine(boolean ab, Coordinates coordinates) {
        if (ab) {
            DoubleEngine.add(coordinates);
        } else DoubleEngine.remove(coordinates);
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
    public Tile getTile(Coordinates coordinates){
        if(tilesTable[coordinates.getX()][coordinates.getY()].isPresent())
            return tilesTable[coordinates.getX()][coordinates.getY()].get();
        else
            return null;
    }
    public Tile getTile(int x, int y){
        if(tilesTable[x][y].isPresent())
            return tilesTable[x][y].get();
        else
            return null;
    }

    public boolean positionTile(Optional<Tile> tile, Coordinates coordinates) {
        if (tile.isPresent() && tilesTable[coordinates.getX()][coordinates.getY()].isEmpty()){
            if (!first&&(coordinates.getX() - 1 >= 0 && coordinates.getX() - 1 <= 4 && coordinates.getY() >= 0 && coordinates.getY() <= 6 && getTilesTable()[coordinates.getX() - 1][coordinates.getY()].isPresent()
                    && !(getTilesTable()[coordinates.getX() - 1][coordinates.getY()].get().getSouth().getConnectorsType()==Connectors.SMOOTH && tile.get().getNorth().getConnectorsType()==Connectors.SMOOTH))
                    ||(coordinates.getX() + 1 >= 0 && coordinates.getX() + 1 <= 4 && coordinates.getY() >= 0 && coordinates.getY() <= 6 && getTilesTable()[coordinates.getX() + 1][coordinates.getY()].isPresent()
                    && !(getTilesTable()[coordinates.getX() + 1][coordinates.getY()].get().getNorth().getConnectorsType()==Connectors.SMOOTH && tile.get().getSouth().getConnectorsType()==Connectors.SMOOTH))
                    ||(coordinates.getX() >= 0 && coordinates.getX() <= 4 && coordinates.getY() - 1 >= 0 && coordinates.getY() - 1 <= 6 && getTilesTable()[coordinates.getX()][coordinates.getY() - 1].isPresent()
                    && !(getTilesTable()[coordinates.getX()][coordinates.getY() - 1].get().getEast().getConnectorsType()==Connectors.SMOOTH && tile.get().getWest().getConnectorsType()==Connectors.SMOOTH))
                    ||(coordinates.getX() >= 0 && coordinates.getX() <= 4 && coordinates.getY() + 1 >= 0 && coordinates.getY() + 1 <= 6 && getTilesTable()[coordinates.getX()][coordinates.getY() + 1].isPresent()
                    && !((getTilesTable()[coordinates.getX()][coordinates.getY() + 1].get().getWest().getConnectorsType()==Connectors.SMOOTH && tile.get().getEast().getConnectorsType()==Connectors.SMOOTH)))){
                tilesTable[coordinates.getX()][coordinates.getY()] = tile;
                tile.get().setShipBoard(this);
                tile.get().setCoordinates(coordinates);
            /*we're going to add the class tile also in the client. IF so we need to add this:
            //Personally I don't think we should but we'll see
            tile.get().setShipBoard(this);

            //EDIT: I think we should, */
                return true;
            }else if(first){
                tilesTable[coordinates.getX()][coordinates.getY()] = tile;
                tile.get().setShipBoard(this);
                tile.get().setCoordinates(coordinates);
                first=false;
                return true;
            }
        }
        return false;
    }

    public void setCargoHoldCoordinates(ArrayList<Coordinates> cargoHoldCoordinates) {
        this.cargoHoldCoordinates = cargoHoldCoordinates;
    }

    public ArrayList<Coverage> getCoverageShields(){
        return shields;
    }
    //New ChooseCrew METHOD
    public boolean chooseCrewToRemove(Coordinates coordinates) {
        return tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
    }

    //BATTERY METHODS
    public boolean chooseBatteryUse(Coordinates coordinates){
        tilesTable[coordinates.getX()][coordinates.getY()].get().consumeBattery();
        return true;
    }

    //forse non serve il batteryCoordinates perché tanto se non è una batteryTile stampo il fatto che non lo è


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
    public int gainGoods(Goods goods, Coordinates coordinates) {
        if(cargoHoldCoordinates.contains(coordinates)){
            if(tilesTable[coordinates.getX()][coordinates.getY()].get().addGood(goods)==1)
                return 0;
            if( tilesTable[coordinates.getX()][coordinates.getY()].get().addGood(goods)==-1){
                System.out.println("THIS TILE IS NOT RED CARGO HOLDER, input again");
                return -1;
            }

        }
        else{
            System.out.println("THIS TILE IS NOT A CARGO HOLDER, input again");
            return -1;
        }
        return 1;
    }

    //IT RETURNS THE COORDINATES OF EVERY CARGO_HOLD THAT CONTAINS A TYPE OF GOOD (RED, YELLOW, GREEN, BLU). IF
    //A CARGO_HOLD CONTAINS MORE THAN ONE GOOD WITH THE SAME COLOR IS GOING TO BE ADD TWICE.
    public ArrayList<Coordinates> cargoHoldContainsGood(Goods good){
        ArrayList<Coordinates> cargoHoldContainsGood = new ArrayList<>();
        for(Coordinates coordinates : cargoHoldCoordinates){
            for(int i=0; i<tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().size(); i++){
                if(tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().get(i).getColor()==good.getColor()){
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
                        chooseCargoStockToEmpty(coordinates);//THE METHOD GETS INVOKED UNTIL THE COORDINATES CONTAINS
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


    /*
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
    */

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
            if (!tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().isEmpty())
                return false;
        }
        return true;
    }



    //give back all the player Goods
    public ArrayList<Goods> getAllGoods(){
        ArrayList<Goods> goods = new ArrayList<>();
        int i;
        Goods good = new Goods(GoodsColor.RED);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);
        }
        good = new Goods(GoodsColor.YELLOW);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);

        }
        good = new Goods(GoodsColor.GREEN);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
            goods.add(good);
        }
        good = new Goods(GoodsColor.BLUE);
        for(i=cargoHoldContainsGood(good).size();i>0;i--){
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
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.YELLOW))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.YELLOW));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.GREEN))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.GREEN));
            }
        }
        for(Coordinates coordinates :cargoHoldContainsGood(new Goods(GoodsColor.BLUE))) {
            if (coordinates.equals(coordinatesToFind)) {
                goods.add(new Goods(GoodsColor.BLUE));
            }
        }
        return goods;
    }
    public void addBattery(Coordinates coordinates){
        getTile(coordinates).addBattery();
    }

    public void removeCrew(ArrayList<Coordinates> coordinatesEpidemic){
        for (Coordinates coordinates : coordinatesEpidemic) {
            getTile(coordinates).removeCrew();
        }
    }

    public void destroy(ArrayList<Coordinates> coordinatesDestroyed){
        for (Coordinates coordinates : coordinatesDestroyed) {
            if(getTile(coordinates)!=null) {
                getTile(coordinates).destroy();
                tilesTable[coordinates.getX()][coordinates.getY()]=Optional.empty();
            }
        }
    }

    public String toString(){
        StringBuilder s = new StringBuilder("Shipboard: ");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable()) {
                    s.append(tilesTable[i][j].get().toString());
                    s.append("\n");
                }
            }
            s.append("\n-\n");
        }
        s.append("\ncredit:"+credit + "\n");
        return s.toString();
    }
    public boolean checkEarlyLanding() {
        return numHumanCrew == 0;
    }

    public void SetNewShip(Set<Coordinates> set) {
        Coordinates c = new Coordinates(0, 0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable()) {
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



    public ArrayList<Tile> epidemic(){
        HashSet<Coordinates> InfectedCabin = new HashSet<>();
        for(Coordinates coordinates : crewCoordinates){
            for(Coordinates coordinates2 : crewCoordinates){
                if ((coordinates.getX() == coordinates2.getX() && coordinates.getY() - 1 == coordinates2.getY() && !this.getTile(coordinates).getWest().getConnectorsType().equals(Connectors.SMOOTH))
                        || (coordinates.getX() == coordinates2.getX() && coordinates.getY() + 1 == coordinates2.getY() && !this.getTile(coordinates).getEast().getConnectorsType().equals(Connectors.SMOOTH))
                        || (coordinates.getX() - 1 == coordinates2.getX() && coordinates.getY() == coordinates2.getY() && !this.getTile(coordinates).getNorth().getConnectorsType().equals(Connectors.SMOOTH))
                        || coordinates.getX() + 1 == coordinates2.getX() && coordinates.getY() == coordinates2.getY() && !this.getTile(coordinates).getSouth().getConnectorsType().equals(Connectors.SMOOTH)
                ) {
                    //(Math.abs(coordinates.getX() - coordinates2.getX())==1 ^ Math.abs(coordinates.getY() - coordinates2.getY())==1)&& !coordinates.equals(coordinates2)
                    InfectedCabin.add(coordinates2);
                }
            }
        }
        for(Coordinates coordinates : InfectedCabin){
            tilesTable[coordinates.getX()][coordinates.getY()].get().removeCrew();
        }
        ArrayList<Tile> modifiedCabin = new ArrayList<>();
        for (Coordinates coordinates: InfectedCabin) {
            modifiedCabin.add(this.getTile(coordinates));
        }
        return modifiedCabin;
    }


    public ArrayList<Coordinates> getBatteryCoordinates() {
        return batteryCoordinates;
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

    public Set<Coordinates> connectedSet(Coordinates start, Set<Coordinates> set) {
        int x = start.getX();
        int y = start.getY();
        set.add(new Coordinates(x, y));
        //south
        if (x<4 && tilesTable[x][y].get().getSouth().getConnectorsType() != Connectors.SMOOTH && tilesTable[x + 1][y].isPresent() && tilesTable[x + 1][y].get().fillable() && !set.contains(tilesTable[x + 1][y].get().getCoordinates())) {
            connectedSet(new Coordinates(x + 1, y), set);
        }
        //east
        if (y<6 && tilesTable[x][y].get().getEast().getConnectorsType() != Connectors.SMOOTH && tilesTable[x][y + 1].isPresent() && tilesTable[x][y + 1].get().fillable() && !set.contains(tilesTable[x][y + 1].get().getCoordinates())) {
            connectedSet(new Coordinates(x, y + 1), set);
        }
        //north
        if (x>0 && tilesTable[x][y].get().getNorth().getConnectorsType() != Connectors.SMOOTH && tilesTable[x - 1][y].isPresent() && tilesTable[x - 1][y].get().fillable() && !set.contains(tilesTable[x - 1][y].get().getCoordinates())) {
            connectedSet(new Coordinates(x - 1, y), set);
        }
        //west
        if (y>0 && tilesTable[x][y].get().getWest().getConnectorsType() != Connectors.SMOOTH && tilesTable[x][y - 1].isPresent() && tilesTable[x][y - 1].get().fillable() && !set.contains(tilesTable[x][y - 1].get().getCoordinates())) {
            connectedSet(new Coordinates(x, y - 1), set);
        }
        return set;
    }

    public boolean verifyCorrectness() {
        Set<Coordinates> set = new HashSet<Coordinates>();
        set=this.connectedSet( new Coordinates(2,3), set);
        // da controllare che tutte le caselle non vuote siano nel set per la correttezza (no caso delle due navi separate)
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable() )
                    if(!tilesTable[i][j].get().isCorrect() ||! set.contains(tilesTable[i][j].get().getCoordinates()) ) return false;
            }
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent())
                    tilesTable[i][j].get().getStat();
            }
        int i=bookedTiles.size();
        for(int j=0;j<i;j++) {
            addPenalty();
        }
        return true;
    }

    public void setGetStat(){
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                if (tilesTable[i][j].isPresent() && tilesTable[i][j].get().fillable())
                    tilesTable[i][j].get().getStat();
            }
    }

    /*
    public void setTilesTable(Coordinates coordinates) {
        tilesTable = shipBoard.getTilesTable();
    }

    public void setNumSingleEngine() {
        this.numSingleEngine = shipBoard.getNumSingleEngine();
    }

    public void setDoubleEngine() {
        this.DoubleEngine = shipBoard.getDoubleEngine();
    }

    public void setNumBrownAliens() {
        this.numBrownAliens = shipBoard.getNumBrownAliens();
    }

    public void setNumPurpleAliens() {
        this.numPurpleAliens = shipBoard.getNumPurpleAliens();
    }

    public void setNumHumanCrew() {
        this.numHumanCrew = shipBoard.getNumHumanCrew();
    }

    public void setNumBattery() {
        this.numBatteries = shipBoard.getNumBatteries();
    }

    public void setSingleCannonPower() {
        this.singleCannonPower = shipBoard.getSingleCannonPower();
    }

    public void setDoubleCannon() {
        this.DoubleCannon = shipBoard.getDoubleCannon();
    }

    public void setNumExposedConnectors(){
        this.numExposedConnectors = shipBoard.getNumExposedConnectors();
    }

    public void setShields() {
        this.shields = shipBoard.getCoverageShields();
    }

    public void setBatteryCoordinates() {
        this.batteryCoordinates = shipBoard.getBatteryCoordinates();
    }

    public void setCrewCoordinates() {
        this.crewCoordinates = shipBoard.getCabinsCoordinates();
    }

    public void setCargoHoldCoordinates() {
        this.cargoHoldCoordinates = shipBoard.getCargoHoldCoordinates();
    }

    public void setBookedTiles() {
        this.bookedTiles = shipBoard.getBookedTiles();
    }
    */










}