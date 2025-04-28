package it.polimi.ingsw.galaxytruckerproject.model.player;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player implements PlayerInterface {
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
    public Tile getDrawnTile() { return drawnTile; }
    public boolean isLanded() {return landed;}
    public void setLanded(boolean landed) {this.landed = landed;}
    public void addCredit(int credit) {this.credit += credit;}
    public void removeCredit(int credit) {this.credit -= credit;}
    public ShipBoard getShipBoard() {return playerShip;}
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
    public Map<Integer,ArrayList<Tile>> useEngines (int numberOfDoubleEngines, ArrayList<Coordinates> batteries) {
        if (numberOfDoubleEngines != playerShip.getDoubleEngine().size()) {
            return null;
        }
        if (batteries.size() != numberOfDoubleEngines) {
            return null;
        }
        ArrayList<Tile> updatedTiles = chooseBatteriesUse(batteries);
        Map<Integer,ArrayList<Tile>> value = new HashMap<>();
        if (chooseBatteriesUse(batteries) == null) {
            return null;
        }
        else {
            if (numberOfDoubleEngines == 0 && playerShip.getNumSingleEngine() == 0) {
                value.put(0,updatedTiles);
            }
            else {
                value.put(playerShip.getNumSingleEngine() + 2*numberOfDoubleEngines + 2*playerShip.getNumBrownAliens(),updatedTiles);
            }
            return value;
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
    public Map<Float,ArrayList<Tile>> useCannons (float doubleFireStrength, ArrayList<Coordinates> batteries) {
        float possibleDoubleFireStrength = 0;
        int batteriesCounter = 0;
        ArrayList<Coordinates> doubleCannons = new ArrayList<>(playerShip.getDoubleCannon());
        while (possibleDoubleFireStrength < doubleFireStrength || !doubleCannons.isEmpty()) {
            possibleDoubleFireStrength += playerShip.getDoubleCannonPower(doubleCannons.getFirst());
            batteriesCounter++;
            doubleCannons.removeFirst();
        }
        if (batteriesCounter > batteries.size() || possibleDoubleFireStrength < doubleFireStrength) {
            return null;
        }
        ArrayList<Tile> updatedTiles = chooseBatteriesUse(batteries);
        Map<Float,ArrayList<Tile>> value = new HashMap<>();
        if (updatedTiles == null) {
            return null;
        }
        else {
            if (doubleFireStrength == 0 && playerShip.getSingleCannonPower() == 0) {
                value.put(0F,updatedTiles);
            }
            else {
                value.put(playerShip.getSingleCannonPower() + doubleFireStrength + 2 * playerShip.getNumPurpleAliens(),updatedTiles);
            }
            return value;
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
    public ArrayList<Tile> chooseBatteriesUse(ArrayList<Coordinates> BatteriesToConsume){
        ArrayList<Coordinates> used = new ArrayList<>();
        for (Coordinates Coordinates : BatteriesToConsume) {
            if(!playerShip.chooseBatteryUse(Coordinates)) {
                for(Coordinates coordinates : used){
                    playerShip.addBattery(coordinates);
                }
                return null;
            }
            used.add(Coordinates);
        }
        ArrayList<Tile> updatedTiles = new ArrayList<>();
        for (Coordinates coordinates : BatteriesToConsume) {
            updatedTiles.add(playerShip.getTile(coordinates));
        }
        return updatedTiles;
    }

    public void printCurrentInfoBatteries(){
        for(Coordinates Coordinates : playerShip.getBatteryCoordinates()){
            System.out.println(playerName+ ": Battery Coordinates: " + Coordinates.getX() + " " + Coordinates.getY());
        }
    }

    public ArrayList<Tile> removeGoods(ArrayList<Coordinates> goodsCoordinates) {
        ArrayList<Coordinates> goodsToCheck = new ArrayList<>(goodsCoordinates);
        ArrayList<Coordinates> redCargo = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED));
        ArrayList<Coordinates> yellowCargo = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW));
        ArrayList<Coordinates> greenCargo = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN));
        ArrayList<Coordinates> blueCargo = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE));
        while (!goodsToCheck.isEmpty()) {
            if (!redCargo.isEmpty()) {
                if (!redCargo.contains(goodsToCheck.getFirst())) {
                    return null;
                }
                redCargo.remove(goodsToCheck.getFirst());
            }
            else if (!yellowCargo.isEmpty()) {
                if (!yellowCargo.contains(goodsToCheck.getFirst())) {
                    return null;
                }
                yellowCargo.remove(goodsToCheck.getFirst());
            }
            else if (!greenCargo.isEmpty()) {
                if (!greenCargo.contains(goodsToCheck.getFirst())) {
                    return null;
                }
                greenCargo.remove(goodsToCheck.getFirst());
            }
            else if (!blueCargo.isEmpty()) {
                if (!blueCargo.contains(goodsToCheck.getFirst())) {
                    return null;
                }
                blueCargo.remove(goodsToCheck.getFirst());
            }
            goodsToCheck.remove(goodsToCheck.getFirst());
        }

        ArrayList<Tile> updated = new ArrayList<>();
        for (Coordinates coordinates : goodsCoordinates) {
            ArrayList<Goods> singleCargoGoods = playerShip.getSingleCargoGoods(coordinates);
            if (singleCargoGoods.contains(new Goods(GoodsColor.RED))) {
                playerShip.removeGood(new Goods(GoodsColor.RED), coordinates);
            }
            else if (singleCargoGoods.contains(new Goods(GoodsColor.YELLOW))) {
                playerShip.removeGood(new Goods(GoodsColor.YELLOW), coordinates);
            }
            else if (singleCargoGoods.contains(new Goods(GoodsColor.GREEN))) {
                playerShip.removeGood(new Goods(GoodsColor.GREEN), coordinates);
            }
            else if (singleCargoGoods.contains(new Goods(GoodsColor.BLUE))) {
                playerShip.removeGood(new Goods(GoodsColor.BLUE), coordinates);
            }
            else {
                return null;
            }
            if (!updated.contains(playerShip.getTile(coordinates))) {
                updated.add(playerShip.getTile(coordinates));
            }
        }
        return updated;
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
    public ArrayList<Tile> removeCrew(ArrayList<Coordinates> coordinates) {
        for (Coordinates coord: coordinates) {
            if (!playerShip.getCabinsCoordinates().contains(coord)) {
                return null;
            }
        }
        ArrayList<Tile> updated = new ArrayList<>();
        for (Coordinates coord: coordinates) {
            playerShip.chooseCrewToRemove(coord);
            updated.add(playerShip.getTile(coord));
        }
        return updated;
    }

    public void printCurrentInfoCabins() {
        for (Coordinates Coordinates : playerShip.getCabinsCoordinates()) {
            System.out.println(playerShip.getTilesTable()[Coordinates.getX()][ Coordinates.getY()].get() + "\n");
        }
    }

    public boolean verifyAndSetupCrew(ArrayList<Tile>  cabins) {
        for (Tile cabin: cabins) {
            if (cabin.getCrewType().equals(CrewType.PURPLE) &&
                    !(cabin.getAlienability().equals(AlienOptions.PURPLE) || cabin.getAlienability().equals(AlienOptions.BOTH)))
                return false;
            if (cabin.getCrewType().equals(CrewType.BROWN) &&
                    !(cabin.getAlienability().equals(AlienOptions.BROWN) || cabin.getAlienability().equals(AlienOptions.BOTH)))
                return false;
            if (!cabin.getAlienability().equals(playerShip.getTile(cabin.getCoordinates()).getAlienability()))
                return false;
        }
        for (Tile cabin: cabins) {
            playerShip.updateTile(cabin.getCoordinates(), cabin);
        }
        return true;
    }

    public ArrayList<Tile> setAllCrewToHuman () {
        ArrayList<Coordinates> cabins = playerShip.getCabinsCoordinates();
        ArrayList<Tile> updatedTiles = new ArrayList<>();
        for (Coordinates coord: cabins) {
            playerShip.getTile(coord).setCrewType(CrewType.HUMAN);
            updatedTiles.add(playerShip.getTile(coord));
        }
        return updatedTiles;
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
