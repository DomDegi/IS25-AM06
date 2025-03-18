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
    private int numCrew;

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

    //
    public int getNumSingleEngine(){return playerShip.getNumSingleEngine();}
    public ArrayList<Coordinates> getDoubleEngine(){return playerShip.getDoubleEngine();}
    public ArrayList<Coverage> getShipCoverage(){return playerShip.getCoverageShields();}
    public int getNumCrew() {return numCrew;}


    public boolean removeGoods(ArrayList<Coordinates> coordinates) {
        for (Coordinates coord : coordinates) {
            if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()) {
                if (playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED)).contains(coord)) {
                    playerShip.removeGood(new Goods(GoodsColor.RED), coord);
                } else return false;
            }

        }
        return true;
    }

    //REMOVES FROM SHIPBOARD THE BATTERIES PRESENT IN THE ARRAYLIST. IF IT'S NEEDED TO ELIMINATE MORE BATTERIES FROM
    //THE SAME TILE, THE COORDINATE HAS TO BE REPEATED. (IF YOU WANT TO ELIMINATE TWO BATTERIES FROM A COORDINATE[x][y]
    //YOU HAVE TO PASS THAT COORDINATE TWO TIMES)
    public void chooseBatteriesUse(ArrayList<Coordinates> BatteriesToConsume){
        for(Coordinates coordinate : BatteriesToConsume){
            playerShip.chooseBatteryUse(coordinate);
        }
    }


    //THIS IS AN ADDITIONAL METHOD THAT IT'S IMPLEMENTED IN CALCULATE ENGINE STRENGTH
    public int chooseDoubleEngine(ArrayList<Coordinates> DoubleEngineToUse, ArrayList<Coordinates> BatteriesToConsume){
        if(DoubleEngineToUse.size()!=BatteriesToConsume.size()){
            System.out.println("THE NUMBERS OF ENGINE AND BATTERIES DO NOT MATCH");
            return -1;
        }

        for(Coordinates coordinate : DoubleEngineToUse){
                if(!playerShip.getDoubleEngine().contains(coordinate)){
                    System.out.println("ONE OF THE DOUBLE ENGINE COORDINATES IS INCORRECT");
                    return -1;
            }
        }
        chooseBatteriesUse(BatteriesToConsume);
        //EVERY DOUBLE ENGINE IS EQUIVALENT TO 2
        return DoubleEngineToUse.size()*2;
    }

    //SETTER METHODS
    public Tile removeDrawnTile() {
        if (drawnTile != null) {
            Tile removed = drawnTile;
            drawnTile = null;
            return removed;
        }
        return null;
    }

    //WE'RE GOING TO MODIFY IT WHEN WHE USE THE EXPECTIONS
    //THIS METHOD CALCULATES ALL THE CURRENTE ENGINESTRENGTH, AFTER THE DECISION TO USE OR NOT THE DOUBLE ENGINS
    public int calculateEngineStrength(ArrayList<Coordinates> DoubleEngineToUse, ArrayList<Coordinates> BatteriesToConsume){
        int engineStrength=0;
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

    public void hasDrawnTile(Tile drawnTile) {
        this.drawnTile = drawnTile;
    }

    public int chooseDoubleCannon(ArrayList<Coordinates> DoubleCannonToUse, ArrayList<Coordinates> BatteriesToConsume){
        if(DoubleCannonToUse.size()!=BatteriesToConsume.size()){
            System.out.println("THE NUMBERS OF DOUBLE CANNON AND BATTERIES DO NOT MATCH");
            return -1;
        }

        int doubleCannonPower =0;
        for(Coordinates coordinate : DoubleCannonToUse){
            if(!playerShip.getDoubleEngine().contains(coordinate)){
                System.out.println("ONE OF THE DOUBLE CANNON COORDINATES IS INCORRECT");
                return -1;
            }else
                doubleCannonPower += playerShip.getTilesTable()[coordinate.getX()][coordinate.getY()].get().getStrength();
        }
        return doubleCannonPower;
    }

    public float calculateDoubleCannonPower(ArrayList<Coordinates> DoubleCannonToUse, ArrayList<Coordinates> BatteriesToConsume){
        float cannonPower=0;
        cannonPower = chooseDoubleCannon(DoubleCannonToUse,BatteriesToConsume);
        if(cannonPower!=-1){
            cannonPower += playerShip.getSingleCannonPower();
            if(cannonPower>0){
                cannonPower += playerShip.getNumPurpleAliens()*2;
            }
        return cannonPower;
        }
        return -1;
    }

    //MAYBE IT COULD MAKE SENSE TO USE ONE METHOD WITH THE ENUMERATION
    public void printCurrentInfoBatteries(){
        for(Coordinates coordinates : playerShip.getBatteryCoordinates()){
            System.out.println("Battery coordinates " + coordinates.getX() + " " + coordinates.getY());
        }
    }
    public void printCurrentInfoEngines(){
        System.out.println("Current number of Single Engine" + playerShip.getNumSingleEngine());
        System.out.println("Current coordinates of Double Engine");
        for(Coordinates coordinate : playerShip.getDoubleEngine()){
            System.out.println(coordinate.getX() + " " + coordinate.getY());
        }
        System.out.println("Current number of Brown Aliens"  + playerShip.getNumBrownAliens());
    }
    public void printCurrentInfoCannons(){
        System.out.println("Current number of Single Engine Power" + playerShip.getNumSingleEngine());
        System.out.println("Current coordinates of Straight Double Engine Power:");
        for(Coordinates coordinate : playerShip.getDoubleEngine()){
            if(playerShip.getTilesTable()[coordinate.getX()][coordinate.getY()].get().getStrength() == 2){
                System.out.println(coordinate.getX() + " " + coordinate.getY());
            }
        }
        System.out.println("Current coordinates of Sided Double Engine Power:");
        for(Coordinates coordinate : playerShip.getDoubleEngine()){
            if(playerShip.getTilesTable()[coordinate.getX()][coordinate.getY()].get().getStrength() == 1){
                System.out.println(coordinate.getX() + " " + coordinate.getY());
            }
        }
    }

    public void printCurrentInfoCabins() {
        for (Coordinates coordinate : playerShip.getCabinsCoordinates()) {
            System.out.println(playerShip.getTilesTable()[coordinate.getX()][ coordinate.getY()].get().toString() + "\n");
        }
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


    //IT ADDS CREDIT
    public void gainCredit(int credit) {
        this.credit += credit;
    }

    //THIS METHOD RECEIVES THE COORDINATES OF THE SHIELD THAT WANTS TO BE ACTIVED AND THE COORDINATE OF THE BATTERYCOMPONENTS
    //FROM WHICH IT'S GOING TO BE USED THE ONE BATTERY NECESSSARY TO POWER THE SHIELD
    public Coverage useShield(Coordinates shieldCoordinates, Coordinates batteryCoordinates) {
        return playerShip.chooseShields(shieldCoordinates,batteryCoordinates);
    }


}
