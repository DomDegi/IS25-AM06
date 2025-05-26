package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Utility class for serializing and deserializing the {@link ShipBoard} of a {@link Player}.
 * Responsible for saving the tile layout and booked tiles, and reconstructing the board from a text representation.
 * <p>This class is not instantiable.</p>
 */
public class ShipBoardSerializerDeserializer {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ShipBoardSerializerDeserializer() {}

    /**
     * Serializes the content of the provided {@link ShipBoard} to the given writer.
     * It includes:
     * <ul>
     *   <li>All tiles in the 5x7 ship grid (written line by line).</li>
     *   <li>The two booked tiles (or "NullTile" if not present).</li>
     * </ul>
     *
     * @param shipBoard the ship board to serialize
     * @param writer    the writer to output the serialized data to
     * @throws IOException if an I/O error occurs during writing
     */
    public static void save(ShipBoard shipBoard, BufferedWriter writer)  throws IOException {
        Optional<Tile>[][] tilesTable = shipBoard.getTilesTable();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                Optional<Tile> tile = tilesTable[i][j];
                String line;
                if  (tile.isPresent()) {
                    line = tile.get().toStringData();}
                else {
                    line = "NullTile";
                }
                writer.write(line);
                writer.newLine();
            }
        }
        ArrayList<Tile> bookedTiles = shipBoard.getBookedTiles();
        switch(bookedTiles.size()) {
            case 0 -> {
                for (int i = 0; i < 2; i++) {
                    writer.write("NullTile");
                    writer.newLine();
                }
            }
            case 1 -> {
                writer.write(bookedTiles.getFirst().toStringData());
                writer.newLine();
            }
            case 2 -> {
                for (Tile tile : bookedTiles) {
                    writer.write(bookedTiles.getFirst().toStringData());
                    writer.newLine();
                }
            }
        }
        writer.close();
    }

    /**
     * Deserializes and reconstructs the {@link ShipBoard} of the given player from a buffered reader.
     * Reads the 5x7 grid tiles and any booked tiles, repositions them in the ship board,
     * and optionally verifies correctness if {@code verify} is true.
     *
     * @param player   the player whose ship board is being loaded
     * @param reader   the source of the serialized data
     * @param verify   if true, runs {@code shipBoard.verifyCorrectness()} after loading
     * @throws IOException if an I/O error occurs during reading
     */
    public static void load(Player player, BufferedReader reader, boolean verify) throws IOException {
        ShipBoard shipBoard = player.getShipBoard();
        Tile[][] tilesTable = new Tile[5][7];
        ArrayList<Tile> loadedTiles = new ArrayList<>();


        String line;

        while ((line = reader.readLine()) != null && loadedTiles.size() < 35) {

            if (line.equals("NullTile")) {
                loadedTiles.add(null);
            } else {
                Tile tile = TileFactory.load(line);
                if (tile == null) {
                    System.out.println("Tile parsing failed, line: " + line);
                    loadedTiles.add(null); // oppure fai qualcosa di più robusto
                } else {
                    loadedTiles.add(tile);
                }
            }

            if (loadedTiles.size() == 35) break;
        }

        if (loadedTiles.size() != 35) {
            System.out.println("Error: not enough tiles to fill shipboard or too many tiles in shipBoard.");
            return;
        }

        for (int i = 0; i < 2; i++) {
            line = reader.readLine();
            if (!line.equals("NullTile")) {
                shipBoard.addBookedTile(TileFactory.load(line));
            }
        }

        shipBoard.simpleInitialize();

        //loading the shipboard
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                Tile currentTile = loadedTiles.removeFirst();
                if (currentTile == null) {
                    shipBoard.positionNullTile(Optional.empty(), new Coordinates(i, j));
                }
                else {
                    shipBoard.positionTile(Optional.of(currentTile), new Coordinates(i, j));
                }
            }
        }

        if (verify) {
            shipBoard.verifyCorrectness();
        }
    }
}
