package it.polimi.ingsw.galaxytruckerproject.player;
import it.polimi.ingsw.galaxytruckerproject.Goods;
import it.polimi.ingsw.galaxytruckerproject.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.tiles.Coverage;
import it.polimi.ingsw.galaxytruckerproject.tiles.ShipBoard;
import it.polimi.ingsw.galaxytruckerproject.tiles.Tile;

import java.util.ArrayList;

public class Player {
    private int playerRanking;
    private final String playerName;
    private int playerPosition;
    private final PlayersColor playerColor;
    private int credit;
    private boolean landed;
    private final ShipBoard playerShip;
    private Tile drawnTile;
    private ArrayList<Coordinates> firstCoordinatesChoice = new ArrayList<>();

    public Player(String playerName, PlayersColor playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerRanking = 0;
        this.playerPosition = 0;
        this.credit = 0;
        this.landed = false;
        this.playerShip = new ShipBoard(this);
        this.drawnTile = null;
    }

    //GETTER METHODS
    public String getPlayerName() {return playerName;}
    public int getPlayerPosition() {return playerPosition;}
    public int getPlayerRanking() {return playerRanking;}
    public void setPlayerRanking(int playerRanking) {this.playerRanking = playerRanking;}
    public void setPlayerPosition(int playerPosition) {this.playerPosition = playerPosition;}
    public PlayersColor getPlayerColor() {return playerColor;}
    public int getCredit() {return credit;}
    public ShipBoard getShipBoard() {return playerShip;}
    public Tile getDrawnTile() { return drawnTile; }
    public boolean isLanded() {return landed;}
    public void setLanded(boolean landed) {this.landed = landed;}
    public void addCredit(int credit) {this.credit += credit;}
    public void removeCredit(int credit) {this.credit -= credit;}
    public ShipBoard getPlayerShip() {return playerShip;}
    public int getTotalCrew() { return playerShip.getNumHumanCrew() + playerShip.getNumBrownAliens() +  playerShip.getNumPurpleAliens(); }



    //DRAWN TILES METHODS
    public Tile removeDrawnTile() {
        if (drawnTile != null) {
            Tile removed = drawnTile;
            drawnTile = null;
            return removed;
        }
        return null;
    }
    public void hasDrawnTile(Tile drawnTile) {
        this.drawnTile = drawnTile;
    }


    //translates an array of strings in an array of coordinates if possible
    public ArrayList<Coordinates> parseCoordinates(String[] input) {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        if (input.length % 2 == 0 && input.length > 0) {
            try {
                for (int i = 0; i < input.length; i++) {
                    coordinates.add(new Coordinates(Integer.parseInt(input[i]), Integer.parseInt(input[i + 1])));
                }
                return coordinates;
            } catch(NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Invalid input\n");
        //returns empty list
        return coordinates;
    }

    //ENGINE METHODS
    //input an empty array to get the value of single engines + aliens
    public int useDoubleEngines(ArrayList<Coordinates> chosenCoordinates) {

        //this conditions returns the value of single engine power + brown aliens
        if (chosenCoordinates.isEmpty()) {
            return calculateEngineStrength(chosenCoordinates, chosenCoordinates);
        }

        //this condition inputs the chosen engines to use and awaits for another input to choose the batteries
        if (firstCoordinatesChoice.isEmpty()) {
            firstCoordinatesChoice = new ArrayList<>(chosenCoordinates);
            return -2;
        }
        //this condition calculate if the inputs were correct, uses the batteries and returns the total value
        else {
            int engineStrength = calculateEngineStrength(firstCoordinatesChoice, chosenCoordinates);
            if  (engineStrength == -1) {
                System.out.println("Invalid input\n");
                firstCoordinatesChoice = new ArrayList<>();
                return -1;
            }
            firstCoordinatesChoice = new ArrayList<>();
            return engineStrength;
        }
    }
    //these methods are running together with useDoubleEngine complete the player's choice to use or not double engines
    public int chooseDoubleEngine(ArrayList<Coordinates> DoubleEngineToUse, ArrayList<Coordinates> BatteriesToConsume){
        if(DoubleEngineToUse.size()!=BatteriesToConsume.size()){
            System.out.println("THE NUMBERS OF ENGINE AND BATTERIES DO NOT MATCH");
            return -1;
        }

        ArrayList<Coordinates> used = new ArrayList<>();

        for(Coordinates coordinates : DoubleEngineToUse){
            if(!playerShip.getDoubleEngine().contains(coordinates) || used.contains(coordinates)){
                System.out.println("ONE OF THE DOUBLE ENGINE Coordinates IS INCORRECT");
                return -1;
            }
            else {
                used.add(coordinates);
            }
        }
        chooseBatteriesUse(BatteriesToConsume);
        //EVERY DOUBLE ENGINE IS EQUIVALENT TO 2
        return DoubleEngineToUse.size()*2;
    }
    public int calculateEngineStrength(ArrayList<Coordinates> DoubleEngineToUse, ArrayList<Coordinates> BatteriesToConsume){
        int engineStrength;
        engineStrength= chooseDoubleEngine(DoubleEngineToUse,BatteriesToConsume);
        if(engineStrength!=-1){
            engineStrength += playerShip.getNumSingleEngine();
            if(engineStrength>0){
                engineStrength += playerShip.getNumBrownAliens()*2;
            }
            return engineStrength;
        }
        return -1;
    }
    public void printCurrentInfoEngines(){
        System.out.println("Current number of Single Engine" + playerShip.getNumSingleEngine());
        System.out.println("Current Coordinates of Double Engine");
        for(Coordinates Coordinates : playerShip.getDoubleEngine()){
            System.out.println(Coordinates.getX() + " " + Coordinates.getY());
        }
        System.out.println("Current number of Brown Aliens"  + playerShip.getNumBrownAliens());
    }

    //CANNON METHODS
    //input an empty array to get the value of single cannons + aliens
    public float useDoubleCannons (ArrayList<Coordinates> chosenCoordinates){

        //this conditions returns the value of single engine power + brown aliens
        if (chosenCoordinates.isEmpty()) {
            return calculateCannonStrength(chosenCoordinates, chosenCoordinates);
        }

        //this condition inputs the chosen engines to use and awaits for another input to choose the batteries
        if (firstCoordinatesChoice.isEmpty()) {
            firstCoordinatesChoice = new ArrayList<>(chosenCoordinates);
            return -2;
        }
        //this condition calculate if the inputs were correct, uses the batteries and returns the total value
        else {
            float cannonStrength = calculateCannonStrength(firstCoordinatesChoice, chosenCoordinates);
            if  (cannonStrength == -1) {
                System.out.println("Invalid input\n");
                firstCoordinatesChoice = new ArrayList<>();
                return -1;
            }
            firstCoordinatesChoice = new ArrayList<>();
            return cannonStrength;
        }
    }

    public int chooseDoubleCannon(ArrayList<Coordinates> DoubleCannonToUse, ArrayList<Coordinates> BatteriesToConsume){
        if(DoubleCannonToUse.size()!=BatteriesToConsume.size()){
            System.out.println("THE NUMBERS OF DOUBLE CANNON AND BATTERIES DO NOT MATCH");
            return -1;
        }

        ArrayList<Coordinates> used = new ArrayList<>();

        int doubleCannonPower =0;
        for(Coordinates coordinates : DoubleCannonToUse){
            if(!playerShip.getDoubleEngine().contains(coordinates) || used.contains(coordinates)){
                System.out.println("ONE OF THE DOUBLE CANNON Coordinates IS INCORRECT");
                return -1;
            }else {
                doubleCannonPower += playerShip.getTilesTable()[coordinates.getX()][coordinates.getY()].get().getStrength();
                used.add(coordinates);
            }
        }
        return doubleCannonPower;
    }
    public float calculateCannonStrength(ArrayList<Coordinates> DoubleCannonToUse, ArrayList<Coordinates> BatteriesToConsume){
        float cannonStrength=0;
        cannonStrength = chooseDoubleCannon(DoubleCannonToUse,BatteriesToConsume);
        if(cannonStrength!=-1){
            cannonStrength += playerShip.getSingleCannonPower();
            if(cannonStrength>0){
                cannonStrength += playerShip.getNumPurpleAliens()*2;
            }
            return cannonStrength;
        }
        return -1;
    }
    public void printCurrentInfoCannons(){
        System.out.println("Current number of Single Engine Power" + playerShip.getNumSingleEngine());
        System.out.println("Current Coordinates of Straight Double Engine Power:");
        for(Coordinates Coordinates : playerShip.getDoubleEngine()){
            if(playerShip.getTilesTable()[Coordinates.getX()][Coordinates.getY()].get().getStrength() == 2){
                System.out.println(Coordinates.getX() + " " + Coordinates.getY());
            }
        }
        System.out.println("Current Coordinates of Sided Double Engine Power:");
        for(Coordinates Coordinates : playerShip.getDoubleEngine()){
            if(playerShip.getTilesTable()[Coordinates.getX()][Coordinates.getY()].get().getStrength() == 1){
                System.out.println(Coordinates.getX() + " " + Coordinates.getY());
            }
        }
    }

    //BATTERIES METHODS
    public void chooseBatteriesUse(ArrayList<Coordinates> BatteriesToConsume){
        for(Coordinates coordinates : BatteriesToConsume){
            playerShip.chooseBatteryUse(coordinates);
        }
    }
    public void printCurrentInfoBatteries(){
        for(Coordinates Coordinates : playerShip.getBatteryCoordinates()){
            System.out.println("Battery Coordinates " + Coordinates.getX() + " " + Coordinates.getY());
        }
    }

    //GOODS METHODS
    //places goods and returns the goods that got placed. Use the array to remove from the cards the goods already placed
    //input all the coordinates in card event. WHen removing from the array in card the once that didn't have coordinates
    //or had wrong coordinates won't get removed
    public ArrayList<Goods> gainGoods (ArrayList<Coordinates> whereToPlace, ArrayList<Goods> goods){

        ArrayList<Goods> placedCorrectly = new ArrayList<Goods>();

        //if you put more coordinates than the goods in the card event
        while (whereToPlace.size() > goods.size()){
            whereToPlace.removeLast();
        }

        Goods placedGood;
        while (!whereToPlace.isEmpty()) {
            placedGood = goods.removeFirst();
            if (playerShip.gainGoods(placedGood ,whereToPlace.removeFirst())) {
                placedCorrectly.add(placedGood);
            }
        }
        //goods that got placed
        return placedCorrectly;
    }

    public int removeGoods(ArrayList<Coordinates> Coordinates) {
        int counter = 0;

        while (!Coordinates.isEmpty()) {
            Coordinates toRemove = Coordinates.removeFirst();
            if (playerShip.isCargoEmpty()) {
                if(!playerShip.chooseBatteryUse(toRemove))
                    return counter;
            }
            else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()) {
                if (playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED)).contains(toRemove)) {
                    playerShip.removeGood(new Goods(GoodsColor.RED), toRemove);
                } else return counter;
            }
            else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).isEmpty()) {
                if (playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).contains(toRemove)) {
                    playerShip.removeGood(new Goods(GoodsColor.YELLOW), toRemove);
                } else return counter;
            }
            else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).isEmpty()) {
                if (playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).contains(toRemove)) {
                    playerShip.removeGood(new Goods(GoodsColor.GREEN), toRemove);
                } else return counter;
            }
            else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).isEmpty()) {
                if (playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).contains(toRemove)) {
                    playerShip.removeGood(new Goods(GoodsColor.BLUE), toRemove);
                } else return counter;
            }
            counter++;
        }
        return counter;
    }
    public void printCurrentInfoCargoHolds() {
        System.out.println("RED goods are at: ");
        playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED));
        System.out.println("\n");
        System.out.println("YELLOW goods are at: ");
        playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW));
        System.out.println("\n");
        System.out.println("GREEN goods are at: ");
        playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN));
        System.out.println("\n");
        System.out.println("BLUE goods are at: ");
        playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE));
        System.out.println("\n");
    }

    //CREW METHODS
    public int removeCrew(ArrayList<Coordinates> coordinates) {
        int counter = 0;
        for (Coordinates coord: coordinates) {
            if (getShipBoard().chooseCrewToRemove(coord))
                counter++;
        }
        return counter;
    }

    public void printCurrentInfoCabins() {
        for (Coordinates Coordinates : playerShip.getCabinsCoordinates()) {
            System.out.println(playerShip.getTilesTable()[Coordinates.getX()][ Coordinates.getY()].get().toString() + "\n");
        }
    }

    //IT ADDS CREDIT
    public void gainCredit(int credit) {
        this.credit += credit;
    }

    //THIS METHOD RECEIVES THE Coordinates OF THE SHIELD THAT WANTS TO BE ACTIVATED AND THE Coordinates OF THE BATTERY_COMPONENTS
    //FROM WHICH IT'S GOING TO BE USED THE ONE BATTERY NECESSARY TO POWER THE SHIELD
    public Coverage useShield(Coordinates shieldCoordinates, Coordinates batteryCoordinates) {
        return playerShip.chooseShields(shieldCoordinates,batteryCoordinates);
    }

}
