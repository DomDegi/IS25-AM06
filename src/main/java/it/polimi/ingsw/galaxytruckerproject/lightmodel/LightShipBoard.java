package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.util.*;

public class LightShipBoard {

    //Variable that saves the reference to the ShipBoard present in the model along with all the game logic
    private ShipBoard shipBoard;

    protected final Player player; //protected because it need to be called in StartingCabin (Tiles)
    private Optional<Tile>[][] tilesTable;
    private int numExposedConnectors;
    private int penalty;
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
    private int numHumanCrew;


    public LightShipBoard(ShipBoard shipBoard) {
        this.player = shipBoard.getPlayer();
        this.shipBoard = shipBoard;
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
        Tile tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL));
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
        Tile tile = new StartingCabin(new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL),new Link(Connectors.UNIVERSAL));
        positionTile(Optional.of(tile), new Coordinates(2, 3));
    }


    public boolean positionTile(Optional<Tile> tile, Coordinates coordinates) {
        if (tile.isPresent() && tilesTable[coordinates.getX()][coordinates.getY()].isEmpty()) {
            tilesTable[coordinates.getX()][coordinates.getY()] = tile;
            /*we're going to add the class tile also in the client. IF so we need to add this:
            //Personally I don't think we should but we'll see
            //EDIT: I think we should, */
            tile.get().setShipBoard(this);
            tile.get().setCoordinates(coordinates);
            return true;
        }
        return false;
    }

    public boolean checkEarlyLanding() {
        return numHumanCrew == 0;
    }

    //ADDITIONAL METHOD THAT GETS IMPLEMENTED IN COUNT EXSPOSEDCONNECTORS
    public void checkBorderTile(int i, int j) {

        //if the tile at its LEFT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if ((j - 1 < 0 || tilesTable[i][j-1].isEmpty() || !tilesTable[i][j-1].get().fillable())&& tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getWest().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile at its RIGHT is either out of bounds, a VoidTile, or empty,
        //then our Tile iss a borderTile and I have to check if it is Exsposed
        if ((j + 1 > 6 ||  tilesTable[i] [j+1].isEmpty() || !tilesTable[i][j+1].get().fillable()) && tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getEast().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile UNDER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if ((i + 1 > 4  || tilesTable[i+1][j].isEmpty() || !tilesTable[i+1][j].get().fillable())&& tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getSouth().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;

        //if the tile OVER is either out of bounds, a VoidTile, or empty,
        // then our Tile iss a borderTile and I have to check if it is Exsposed
        if ((i - 1 < 0  || tilesTable[i-1][j].isEmpty() || !tilesTable[i-1][j].get().fillable())&& tilesTable[i][j].isPresent() &&  tilesTable[i][j].get().fillable())
            if (!tilesTable[i][j].get().getNorth().getConnectorsType().equals(Connectors.SMOOTH))
                numExposedConnectors++;
    }

    public int countExposedConnectors() {
        numExposedConnectors = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 7; j++) {
                checkBorderTile(i, j);
            }
        return numExposedConnectors;
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
    public ArrayList<Coordinates> cargoHoldContainsGood(Goods goodColor){
        ArrayList<Coordinates> cargoHoldContainsGood = new ArrayList<>();
        for(Coordinates coordinates : cargoHoldCoordinates){
            for(int i=0; i<tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().size(); i++){
                if(tilesTable[coordinates.getX()][coordinates.getY()].get().getCargo().get(i).getColor()==goodColor.getColor()){
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
