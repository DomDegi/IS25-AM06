package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class ShipBoardSerializerDeserializer {



    public ShipBoardSerializerDeserializer() {}

    public void save(ShipBoard shipBoard, BufferedWriter writer)  throws IOException {
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
        writer.close();
    }

    public int load(Player player, int startLine, BufferedReader reader) throws IOException {

        TileLoader tileLoader = new TileLoader();
        ShipBoard shipBoard = player.getShipBoard();
        Tile[][] tilesTable = new Tile[5][7];
        ArrayList<Tile> loadedTiles = new ArrayList<>();


        String line;
        int currentLine = 0;
        int endLine = 0;

        while ((line = reader.readLine()) != null) {
            currentLine++;
            if (currentLine < startLine) continue;


            if (line.equals("NullTile")) {
                loadedTiles.add(null);
            } else {
                Tile tile = tileLoader.load(line);
                if (tile == null) {
                    System.out.println("Tile parsing failed, line: " + line);
                    loadedTiles.add(null); // oppure fai qualcosa di più robusto
                } else {
                    loadedTiles.add(tile);
                }
            }

            if (loadedTiles.size() == 35) break;
        }
        endLine = currentLine;

        if (loadedTiles.size() != 35) {
            System.out.println("Error: not enough tiles to fill shipboard or too many tiles in shipBoard.");
            return -1;
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
        return endLine;
    }
}
