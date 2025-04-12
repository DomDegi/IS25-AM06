package it.polimi.ingsw.galaxytruckerproject.model.player;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.util.ArrayList;

public class Player {
    private int playerRanking;
    private final String playerName;
    private int playerPosition;
    private final PlayersColor playerColor;
    private int credit;
    private boolean landed;
    private ShipBoard playerShip;
    private Tile drawnTile;
    private ArrayList<Coordinates> firstCoordinatesChoice = new ArrayList<>();
    private boolean isDisconnected;

    public Player(String playerName, PlayersColor playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerRanking = 0;
        this.playerPosition = 0;
        this.credit = 0;
        this.landed = false;
        this.playerShip = new ShipBoard(this);
        this.drawnTile = null;
        this.isDisconnected = false;
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
                for (int i = 0; i < input.length; i+=2) {
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
    public int useEngines (int numberOfDoubleEngines, ArrayList<Coordinates> batteries) {
        if (numberOfDoubleEngines != playerShip.getDoubleEngine().size()) {
            return -1;
        }
        if (batteries.size() != numberOfDoubleEngines) {
            return -1;
        }
        if (! chooseBatteriesUse(batteries)) {
            return -1;
        }
        else {
            if (numberOfDoubleEngines == 0 && playerShip.getNumSingleEngine() == 0) {
                return 0;
            }
            else {
                return playerShip.getNumSingleEngine() + 2*numberOfDoubleEngines + 2*playerShip.getNumBrownAliens();
            }
        }
    }

    public void printCurrentInfoEngines(){
        System.out.println(playerName+" Current number of Single Engine " + playerShip.getNumSingleEngine());
        System.out.println(playerName+" Current Coordinates of Double Engine ");
        for(Coordinates Coordinates : playerShip.getDoubleEngine()){
            System.out.println(Coordinates.getX() + " " + Coordinates.getY());
        }
        System.out.println("Current number of Brown Aliens"  + playerShip.getNumBrownAliens());
    }

    //CANNON METHODS
    public float useCannons (float doubleFireStrength, ArrayList<Coordinates> batteries) {
        float possibleDoubleFireStrength = 0;
        int batteriesCounter = 0;
        ArrayList<Coordinates> doubleCannons = new ArrayList<>(playerShip.getDoubleCannon());
        while (possibleDoubleFireStrength < doubleFireStrength || !doubleCannons.isEmpty()) {
            possibleDoubleFireStrength += playerShip.getDoubleCannonPower(doubleCannons.getFirst());
            batteriesCounter++;
            doubleCannons.removeFirst();
        }
        if (batteriesCounter > batteries.size() || possibleDoubleFireStrength < doubleFireStrength) {
            return -1;
        }
        if (! chooseBatteriesUse(batteries)) {
            return -1;
        }
        else {
            if (doubleFireStrength == 0 && playerShip.getSingleCannonPower() == 0) {
                return 0;
            }
            else {
                return playerShip.getSingleCannonPower() + doubleFireStrength + 2 * playerShip.getNumPurpleAliens();
            }
        }
    }
    public void printCurrentInfoCannons(){
        System.out.println(playerName+ ": Current number of Single Cannon Strength: " + playerShip.getSingleCannonPower());
        System.out.println(playerName+ ": Current Coordinates of Straight Double Cannons:");
        for(Coordinates Coordinates : playerShip.getDoubleCannon()){
            if(playerShip.getTilesTable()[Coordinates.getX()][Coordinates.getY()].get().getStrength() == 2){
                System.out.println(Coordinates.getX() + " " + Coordinates.getY());
            }
        }
        System.out.println(playerName+ ": Current Coordinates of Sided Double Cannons: ");
        for(Coordinates Coordinates : playerShip.getDoubleCannon()){
            if(playerShip.getTilesTable()[Coordinates.getX()][Coordinates.getY()].get().getStrength() == 1){
                System.out.println(Coordinates.getX() + " " + Coordinates.getY());
            }
        }
    }

    //BATTERIES METHODS
    public boolean chooseBatteriesUse(ArrayList<Coordinates> BatteriesToConsume){
        ArrayList<Coordinates> used = new ArrayList<>();
        for (Coordinates Coordinates : BatteriesToConsume) {
            if(!playerShip.chooseBatteryUse(Coordinates)) {
                for(Coordinates coordinates : used){
                    playerShip.addBattery(coordinates);
                }
                return false;
            }
            used.add(Coordinates);
        }
        return true;
    }

    public void printCurrentInfoBatteries(){
        for(Coordinates Coordinates : playerShip.getBatteryCoordinates()){
            System.out.println(playerName+ ": Battery Coordinates: " + Coordinates.getX() + " " + Coordinates.getY());
        }
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
    public boolean removeCrew(ArrayList<Coordinates> coordinates) {
        for (Coordinates coord: coordinates) {
            if (!playerShip.getCabinsCoordinates().contains(coord)) {
                return false;
            }
        }
        for (Coordinates coord: coordinates) {
            playerShip.chooseCrewToRemove(coord);
        }
        return true;
    }

    public void printCurrentInfoCabins() {
        for (Coordinates Coordinates : playerShip.getCabinsCoordinates()) {
            System.out.println(playerShip.getTilesTable()[Coordinates.getX()][ Coordinates.getY()].get() + "\n");
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


    //Disconnected flag methods
    public void playerDisconnects() {
        this.isDisconnected = true;
    }

    public void playerReconnects() {
        this.isDisconnected = false;
    }

    public boolean IsDisconnected() {
        return isDisconnected;
    }

    //methods for testing

    public void setPlayerShip(ShipBoard shipBoard) {
        this.playerShip = shipBoard;
    }

}
