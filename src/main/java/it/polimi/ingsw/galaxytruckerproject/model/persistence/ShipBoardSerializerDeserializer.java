package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class ShipBoardSerializerDeserializer {



    private ShipBoardSerializerDeserializer() {}

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

    public static void load(Player player, int startLine, BufferedReader reader) throws IOException {
        ShipBoard shipBoard = player.getShipBoard();
        Tile[][] tilesTable = new Tile[5][7];
        ArrayList<Tile> loadedTiles = new ArrayList<>();


        String line;

        while ((line = reader.readLine()) != null || loadedTiles.size() < 35) {

            if (line.equals("NullTile")) {
                loadedTiles.add(null);
            } else {
                Tile tile = TileLoader.load(line);
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
                shipBoard.addBookedTile(TileLoader.load(line));
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

        shipBoard.verifyCorrectness();
    }
}
