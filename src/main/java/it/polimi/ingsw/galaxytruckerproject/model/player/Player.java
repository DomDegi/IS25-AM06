package it.polimi.ingsw.galaxytruckerproject.model.player;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player in the Galaxy Trucker game.
 * A player has a name, color, ship, crew, cargo, credits, and various gameplay states like landing or disconnection.
 *
 * This class handles interactions with the ship, including:
 * - managing crew
 * - handling engines and cannons
 * - placing and removing goods
 * - using shields and batteries
 * - serialization/deserialization for saving/loading game state
 *
 * Implements {@code PlayerInterface} and is {@code Serializable}.
 */
public class Player implements PlayerInterface , Serializable {
    private int playerRanking;
    private String playerName;
    private int playerPosition;
    private PlayersColor playerColor;
    private int credit;
    private boolean landed;
    private ShipBoard playerShip;
    private Tile drawnTile;
    private boolean isDisconnected;

    /**
     * Constructs a new Player with the specified name and color.
     *
     * @param playerName  the name of the player
     * @param playerColor the color of the player
     */
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

    /**
     * Creates a shallow clone of the player with the ship and drawn tile set to null.
     *
     * @return a new Player instance used for transmission (e.g., to client)
     * @throws RuntimeException if cloning fails
     */
    public Player send() {
        try {
            Player cloned = (Player) super.clone();
            cloned.setPlayerShip(null);
            cloned.setDrawnTile(null);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    //GETTER METHODS
    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public int getPlayerRanking() {
        return playerRanking;
    }

    public void setPlayerRanking(int playerRanking) {
        this.playerRanking = playerRanking;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public PlayersColor getPlayerColor() {
        return playerColor;
    }

    public int getCredit() {
        return credit;
    }

    public Tile getDrawnTile() {
        return drawnTile;
    }

    public boolean isLanded() {
        return landed;
    }

    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    public void addCredit(int credit) {
        this.credit += credit;
    }

    public void removeCredit(int credit) {
        this.credit -= credit;
    }

    public ShipBoard getShipBoard() {
        return playerShip;
    }

    public int getTotalCrew() {
        return playerShip.getNumHumanCrew() + playerShip.getNumBrownAliens() + playerShip.getNumPurpleAliens();
    }


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


    //ENGINE METHODS
    /**
     * Uses a specified number of double engines and the corresponding batteries.
     * Computes total engine power.
     *
     * @param numberOfDoubleEngines the number of double engines to activate
     * @param batteries coordinates of the batteries used
     * @return map of engine power gained and the updated tiles; null if invalid
     */
    public Map<Integer, ArrayList<Tile>> useEngines(int numberOfDoubleEngines, ArrayList<Coordinates> batteries) {
        if (numberOfDoubleEngines > playerShip.getDoubleEngine().size()) {
            return null;
        }
        if (batteries.size() != numberOfDoubleEngines) {
            return null;
        }
        ArrayList<Tile> updatedTiles = chooseBatteriesUse(batteries);
        Map<Integer, ArrayList<Tile>> value = new HashMap<>();
        if (updatedTiles == null) {
            return null;
        } else {
            if (numberOfDoubleEngines == 0 && playerShip.getNumSingleEngine() == 0) {
                value.put(0, updatedTiles);
            } else {
                value.put(playerShip.getNumSingleEngine() + 2 * numberOfDoubleEngines + 2 * playerShip.getNumBrownAliens(), updatedTiles);
            }
            return value;
        }
    }

    public void printCurrentInfoEngines() {
        System.out.println(playerName + " Current number of Single Engine " + playerShip.getNumSingleEngine());
        System.out.println(playerName + " Current Coordinates of Double Engine ");
        for (Coordinates Coordinates : playerShip.getDoubleEngine()) {
            System.out.println(Coordinates.getX() + " " + Coordinates.getY());
        }
        System.out.println("Current number of Brown Aliens " + playerShip.getNumBrownAliens());
    }

    //CANNON METHODS
    /**
     * Uses cannons to reach a desired firepower by consuming batteries.
     *
     * @param doubleFireStrength desired firepower from double cannons
     * @param batteries coordinates of the batteries used
     * @return map of firepower and updated tiles; null if invalid
     */
    public Map<Float, ArrayList<Tile>> useCannons(float doubleFireStrength, ArrayList<Coordinates> batteries) {
        float possibleDoubleFireStrength = 0;
        int batteriesCounter = 0;
        ArrayList<Coordinates> doubleCannons = new ArrayList<>(playerShip.getDoubleCannon());

        // Adjusted the condition to prevent infinite loop
        while (possibleDoubleFireStrength < doubleFireStrength && !doubleCannons.isEmpty()) {
            possibleDoubleFireStrength += playerShip.getDoubleCannonPower(doubleCannons.getFirst());
            batteriesCounter++;
            doubleCannons.removeFirst();
        }

        // Check if the required strength was reached
        if (batteriesCounter > batteries.size() || possibleDoubleFireStrength < doubleFireStrength) {
            return null;
        }

        ArrayList<Tile> updatedTiles = chooseBatteriesUse(batteries);
        if (updatedTiles == null) {
            return null;
        }

        Map<Float, ArrayList<Tile>> value = new HashMap<>();
        if (doubleFireStrength == 0 && playerShip.getSingleCannonPower() == 0) {
            value.put(0F, updatedTiles);
        } else {
            value.put(playerShip.getSingleCannonPower() + doubleFireStrength + 2 * playerShip.getNumPurpleAliens(), updatedTiles);
        }

        return value;
    }


    public void printCurrentInfoCannons() {
        System.out.println(playerName + ": Current number of Single Cannon Strength: " + playerShip.getSingleCannonPower());
        System.out.println(playerName + ": Current number of purple Aliens " +  playerShip.getNumPurpleAliens());
        System.out.println(playerName + ": Current Coordinates of Straight Double Cannons:");
        for (Coordinates Coordinates : playerShip.getDoubleCannon()) {
            if (playerShip.getTilesTable()[Coordinates.getX()][Coordinates.getY()].get().getStrength() == 2) {
                System.out.println(Coordinates.getX() + " " + Coordinates.getY());
            }
        }
        System.out.println(playerName + ": Current Coordinates of Sided Double Cannons: ");
        for (Coordinates Coordinates : playerShip.getDoubleCannon()) {
            if (playerShip.getTilesTable()[Coordinates.getX()][Coordinates.getY()].get().getStrength() == 1) {
                System.out.println(Coordinates.getX() + " " + Coordinates.getY());
            }
        }
    }

    //BATTERIES METHODS
    /**
     * Tries to consume the given list of batteries in order.
     *
     * @param BatteriesToConsume list of battery coordinates to use
     * @return list of tiles that were successfully updated; null if failed
     */
    public ArrayList<Tile> chooseBatteriesUse(ArrayList<Coordinates> BatteriesToConsume) {
        ArrayList<Coordinates> used = new ArrayList<>();
        for (Coordinates Coordinates : BatteriesToConsume) {
            if (!playerShip.chooseBatteryUse(Coordinates)) {
                for (Coordinates coordinates : used) {
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

    public void printCurrentInfoBatteries() {
        for (Coordinates Coordinates : playerShip.getBatteryCoordinates()) {
            System.out.println(playerName + ": Battery Coordinates: " + Coordinates.getX() + " " + Coordinates.getY());
        }
    }

    /**
     * Removes goods from the specified coordinates if valid.
     *
     * @param toRemoveFrom list of coordinates to remove goods from
     * @return list of tiles updated after removal; null if invalid
     */
    public ArrayList<Tile> removeGoods(ArrayList<Coordinates> toRemoveFrom) {
        ArrayList<Tile> updatedTiles = new ArrayList<>();

        if (!removeGoodsCheck(toRemoveFrom)) {
            return null;
        }

        for (Coordinates Coordinates : toRemoveFrom) {
            if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED)).isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.RED), Coordinates);
                if (!updatedTiles.contains(playerShip.getTile(Coordinates)))
                    updatedTiles.add(playerShip.getTile(Coordinates));
            } else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.YELLOW), Coordinates);
                if (!updatedTiles.contains(playerShip.getTile(Coordinates)))
                    updatedTiles.add(playerShip.getTile(Coordinates));
            } else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).isEmpty()) {
                playerShip.removeGood(new Goods(GoodsColor.GREEN), Coordinates);
                if (!updatedTiles.contains(playerShip.getTile(Coordinates)))
                    updatedTiles.add(playerShip.getTile(Coordinates));
            } else if (!playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).isEmpty()) {
            playerShip.removeGood(new Goods(GoodsColor.BLUE), Coordinates);
            if (!updatedTiles.contains(playerShip.getTile(Coordinates)))
                updatedTiles.add(playerShip.getTile(Coordinates));
            }
        }
        return updatedTiles;
    }
    /**
     * Checks whether goods can be removed from the given coordinates.
     *
     * @param toRemoveFrom list of coordinates
     * @return true if the removal is valid
     */
    public boolean removeGoodsCheck(ArrayList<Coordinates> toRemoveFrom) {
        int redGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED)).size();
        int yellowGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW)).size();
        int greenGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN)).size();
        int blueGoods = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE)).size();

        int redCount = 0; int yellowCount = 0; int greenCount = 0; int blueCount = 0;
        for (Coordinates Coordinates : toRemoveFrom) {
            if (redCount < redGoods) {
                if (playerShip.singleCargoContainsGood(GoodsColor.RED, Coordinates))
                    redCount++;
                else
                    return false;
            }
            else if (yellowCount < yellowGoods) {
                if (playerShip.singleCargoContainsGood(GoodsColor.YELLOW, Coordinates))
                    yellowCount++;
                else {
                    return false;
                }
            }
            else if (greenCount < greenGoods) {
                if (playerShip.singleCargoContainsGood(GoodsColor.GREEN, Coordinates))
                    greenCount++;
                else {
                    return false;
                }
            }
            else if (blueCount < blueGoods) {
                if (playerShip.singleCargoContainsGood(GoodsColor.BLUE, Coordinates))
                    blueCount++;
                else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Automatically places the given goods in the player's cargo holds.
     *
     * @param goodsList list of goods to place
     * @return list of tiles updated with new goods
     */
    public ArrayList<Tile> automaticGoodsPositioner(ArrayList<Goods> goodsList) {
        ArrayList<Coordinates> coordinates = new ArrayList<>(playerShip.getCargoHoldCoordinates());
        ArrayList<Tile> updatedTiles = new ArrayList<>();

        // Goods to be placed: avoid modifying original
        ArrayList<Goods> remainingGoods = new ArrayList<>(goodsList);

        for (Goods good : remainingGoods) {
            boolean placed = false;

            // Iterate over a copy so we can safely remove from coordinates
            ArrayList<Coordinates> coordsCopy = new ArrayList<>(coordinates);

            for (Coordinates coord : coordsCopy) {
                Tile tile = playerShip.getTile(coord);
                int result = tile.addGood(good);

                if (result == 1) {
                    // Successfully added
                    if (!updatedTiles.contains(tile)) {
                        updatedTiles.add(tile);
                    }
                    placed = true;
                    break; // Next good
                } else if (result == 0) {
                    // Cargo hold full — remove from future consideration
                    coordinates.remove(coord);
                } else if (result == -1) {
                    // Incompatible (e.g. red good in non-red hold), keep trying
                    continue;
                }
            }

            // If not placed, just skip it — it cannot be placed
        }

        return updatedTiles;
    }




    public void printCurrentInfoCargoHolds() {
        ArrayList<Coordinates> toPrint;
        System.out.println("RED goods are at: ");
        toPrint = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.RED));
        coordinatesPrinter(toPrint);
        System.out.println("YELLOW goods are at: ");
        toPrint = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.YELLOW));
        coordinatesPrinter(toPrint);
        System.out.println("GREEN goods are at: ");
        toPrint = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.GREEN));
        coordinatesPrinter(toPrint);
        System.out.println("BLUE goods are at: ");
        toPrint = playerShip.cargoHoldContainsGood(new Goods(GoodsColor.BLUE));
        coordinatesPrinter(toPrint);
    }

    void coordinatesPrinter(ArrayList<Coordinates> coordinates) {
        int i = 0;
        for (Coordinates c : coordinates) {
            System.out.print(c.toString());
            i ++;
            if (i % 5 == 0)
                System.out.print("\n");
        }
        System.out.print("\n");
    }

    //CREW METHODS
    /**
     * Removes crew members from the given cabin coordinates.
     *
     * @param coordinates list of cabin coordinates
     * @return list of updated tiles, or null if any coordinate is invalid
     */
    public ArrayList<Tile> removeCrew(ArrayList<Coordinates> coordinates) {
        for (Coordinates coord: coordinates) {
            if (!playerShip.getCabinsCoordinates().contains(coord)) {
                return null;
            }
        }
        ArrayList<Tile> updated = new ArrayList<>();
        for (Coordinates coord: new ArrayList<>(coordinates)) {
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

    /**
     * Verifies if alien crew can be correctly placed in cabins and updates them.
     *
     * @param cabins list of cabin tiles with crew to set
     * @return true if setup is valid and successful
     */
    public boolean verifyAndSetupCrew(ArrayList<Tile>  cabins) {
        for (Tile cabin: cabins) {
            if (cabin.getCrewType().equals(CrewType.PURPLE) &&
                    !(playerShip.getTile(cabin.getCoordinates()).getAlienability().equals(AlienOptions.PURPLE) || playerShip.getTile(cabin.getCoordinates()).getAlienability().equals(AlienOptions.BOTH)))
                return false;
            if (cabin.getCrewType().equals(CrewType.BROWN) &&
                    !(playerShip.getTile(cabin.getCoordinates()).getAlienability().equals(AlienOptions.BROWN) || playerShip.getTile(cabin.getCoordinates()).getAlienability().equals(AlienOptions.BOTH)))
                return false;
        }
        for (Tile cabin: cabins) {
            playerShip.getTile(cabin.getCoordinates()).setCrewType(cabin.getCrewType());
        }
        return true;
    }

    /**
     * Sets all cabin tiles to contain human crew.
     *
     * @return list of tiles that were updated
     */
    public ArrayList<Tile> setAllCrewToHuman () {
        ArrayList<Coordinates> cabins = playerShip.getCabinsCoordinates();
        ArrayList<Tile> updatedTiles = new ArrayList<>();
        for (Coordinates coord: cabins) {
            playerShip.getTile(coord).setCrewType(CrewType.HUMAN);
            updatedTiles.add(playerShip.getTile(coord));
        }
        playerShip.setCompleted(true);
        return updatedTiles;
    }

    /**
     * Removes all crew from the ship and resets crew counter to zero.
     */
    public void setCrewToZero(){
        ArrayList<Coordinates> cabins = playerShip.getCabinsCoordinates();
        removeCrew(cabins);
        cabins = playerShip.getCabinsCoordinates();
        if(!cabins.isEmpty()){
            removeCrew(cabins);
        }
        playerShip.setCrewNumberToZero();
    }

    /**
     * sets the player's credit to the value of the parameter
     * @param credit player's total credits
     */
    public void gainCredit(int credit) {
        this.credit += credit;
    }

    /**
     * sets player's disconnected flag to true
     */
    public void playerDisconnects() {
        this.isDisconnected = true;
        System.out.println("Player disconnected");
    }

    /**
     * sets player's disconnected flag to false
     */
    public void playerReconnects() {
        this.isDisconnected = false;
    }

    public boolean IsDisconnected() {
        return isDisconnected;
    }

    public void setPlayerShip(ShipBoard shipBoard) {
        this.playerShip = shipBoard;
    }

    public void setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
    }

    /**
     * Serializes the player state into a single string line for saving.
     *
     * @return the serialized representation of the player
     */
    public String toStringData() {
        StringBuilder sb = new StringBuilder();
        //First 5 word divided by " " space (attributes[0-4])
        sb.append(playerName).append(" ").append(playerColor.toString()).append(" ")
                .append(playerPosition).append(" ").append(playerRanking).append(" ")
                .append(credit).append(" ");
        //attribute[5]
        if (landed) {
            sb.append("L").append(" ");
        }
        else {
            sb.append("N").append(" ");
        }
        //attribute[6]
        if (isDisconnected){
            sb.append("D").append(" ");
        }
        else {
            sb.append("N").append(" ");
        }
        //attribute[7]
        sb.append(playerShip.getPenalty()).append(" ");
        //attributes[8]
        if (playerShip.isCompleted()) {
            sb.append("C").append(" ");
        }
        else {
            sb.append("N").append(" ");
        }
        //attribute[9 - depends on the tile]
        if (drawnTile == null) {
            sb.append("N");
        }
        else {
            sb.append(drawnTile.toStringData());
        }
        return sb.toString();
    }

    /**
     * No-argument constructor used for deserialization.
     */
    public Player() {
        this.playerRanking = 0;
        this.playerPosition = 0;
    }
    
    /**
     * Reconstructs a Player from a saved string of attributes.
     *
     * @param attributes array of player attributes from save file
     */
    public void playerLoader(String[] attributes) {
        if (attributes.length < 10) {
            throw new IllegalArgumentException("Insufficient attributes: at least 10 are needed " + attributes.length);
        }
        this.playerName = attributes[0];
        this.playerColor = PlayersColor.fromString(attributes[1]);
        this.playerPosition = Integer.parseInt(attributes[2]);
        this.playerRanking = Integer.parseInt(attributes[3]);
        this.credit = Integer.parseInt(attributes[4]);
        this.landed = attributes[5].equals("L");
        this.isDisconnected = attributes[6].equals("D");

        this.playerShip = new ShipBoard(this);
        this.playerShip.setPenalty(Integer.parseInt(attributes[7]));
        this.playerShip.setCompleted(attributes[8].equals("C"));
        if (attributes[9].equals("N")) {
            this.drawnTile = null;
        }
        else {
            StringBuilder tileData = new StringBuilder();
            for (int i = 9; i < attributes.length; i++) {
                tileData.append(attributes[i]).append(" ");
            }
            this.drawnTile = TileFactory.load(tileData.toString());
        }
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setDrawnTile(Tile drawnTile) {
        this.drawnTile = drawnTile;
    }

    /**
     * @return true if the player is currently disconnected
     */
    public boolean isDisconnected() {
        return isDisconnected;
    }

    public ShipBoard getPlayerShip() {
        return playerShip;
    }

    public void setPlayerColor(PlayersColor playerColor) {
        this.playerColor = playerColor;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
