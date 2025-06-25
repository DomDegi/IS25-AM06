package it.polimi.ingsw.galaxytruckerproject.lightmodel;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class represents a light version of the player in the game.
 * It contains the essential details about a player like their position, rank, credits, and their shipboard.
 * <p>
 * The class implements the PlayerInterface to provide the necessary methods for player management.
 * </p>
 */
public class LightPlayer implements PlayerInterface, Serializable {

    /**
     * The position of the player on the flight board.
     */
    private int position;

    /**
     * The shipboard associated with this player, containing the tiles and crew.
     */
    private LightShipBoard shipboard;

    /**
     * The rank of the player in the current game phase.
     */
    private int rank;

    /**
     * The name of the player.
     */
    private String playerName;

    /**
     * Indicates if the player has landed on a planet.
     */
    private boolean landed = false;

    /**
     * The color assigned to the player.
     */
    private PlayersColor color;

    /**
     * The number of credits the player has accumulated.
     */
    private int credits;

    /**
     * Constructs a `LightPlayer` instance with the provided player name and color.
     *
     * @param playerName the name of the player
     * @param color the color assigned to the player
     */
    public LightPlayer(String playerName, PlayersColor color) {
        this.playerName = playerName;
        this.color = color;
    }

    /**
     * Constructs a `LightPlayer` instance based on an existing `Player` object.
     *
     * @param player the original `Player` object to copy data from
     */
    public LightPlayer(Player player){
        this.playerName = player.getPlayerName();
        this.color = player.getPlayerColor();
    }

    /**
     * Returns a string representation of the player.
     *
     * @return a string with the player's name, position, and rank
     */
    @Override
    public String toString() {
        return playerName + " position:" + position + " rank:" + rank;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the position of the player on the flight board.
     *
     * @return the position of the player
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of the player on the flight board.
     *
     * @param position the new position of the player
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets the name of the player.
     *
     * @param playerName the new name of the player
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the rank of the player.
     *
     * @return the rank of the player
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the rank of the player.
     *
     * @param rank the new rank of the player
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Checks if the player has landed on a planet.
     *
     * @return true if the player has landed, false otherwise
     */
    public boolean isLanded() {
        return landed;
    }

    /**
     * Sets the landing status of the player.
     *
     * @param landed true if the player has landed, false otherwise
     */
    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    /**
     * Returns the shipboard associated with the player.
     *
     * @return the shipboard of the player
     */
    public LightShipBoard getShipBoard() {
        return shipboard;
    }

    /**
     * Sets the shipboard for the player.
     *
     * @param shipboard the new shipboard to set
     */
    public void setShipboard(LightShipBoard shipboard) {
        this.shipboard = shipboard;
    }

    /**
     * Books a tile for the player. If the tile cannot be booked, an error message is printed.
     *
     * @param tile the tile to be booked
     * @throws RemoteException if a remote exception occurs
     */
    public void bookTile(Tile tile) throws RemoteException {
        boolean mustBeTrue = shipboard.addBookedTile(tile);
        if (!mustBeTrue) {
            System.out.println("Error in tile booking " + tile);
        }
    }

    /**
     * Sets the color for the player.
     *
     * @param color the new color of the player
     */
    public void setColor(PlayersColor color) {
        this.color = color;
    }

    /**
     * Returns the color of the player.
     *
     * @return the color of the player
     */
    @Override
    public PlayersColor getPlayerColor() {
        return color;
    }

    /**
     * Increases the player's credit by the specified amount.
     *
     * @param credits the amount of credits to add to the player's total
     */
    public void gainCredits(int credits) {
        this.credits += credits;
    }

    /**
     * Returns the current number of credits the player has.
     *
     * @return the number of credits the player has
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the player's shipboard with the provided `LightShipBoard`.
     *
     * @param shipBoard the `LightShipBoard` to set
     */
    public void setPlayerShip(LightShipBoard shipBoard) {
        this.shipboard = shipBoard;
    }

    /**
     * Sets all crew members to human for the player's shipboard.
     * This is typically used in the "trial" mode or similar scenarios.
     *
     * @return a list of tiles that were updated
     */
    public ArrayList<Tile> setAllCrewToHuman() {
        ArrayList<Coordinates> cabins = shipboard.getCabinsCoordinates();
        ArrayList<Tile> updatedTiles = new ArrayList<>();
        for (Coordinates coord : cabins) {
            shipboard.getTile(coord).setCrewType(CrewType.HUMAN);
            updatedTiles.add(shipboard.getTile(coord));
        }
        shipboard.setCompleted(true);
        return updatedTiles;
    }

    /**
     * Loads data from a string array, typically used to initialize a player from external data.
     *
     * @param data the data array to load the player's information from
     * @throws RemoteException if a remote exception occurs
     */
    public void loadFromData(String[] data) throws RemoteException {
        this.playerName =  data[0];
        this.color = PlayersColor.fromString(data[1]);
        this.position = Integer.parseInt(data[2]);
        this.rank = Integer.parseInt(data[3]);
        this.credits = Integer.parseInt(data[4]);
        this.landed = data[5].equals("L");
        this.shipboard.setPenalty(Integer.parseInt(data[7]));
    }
}
