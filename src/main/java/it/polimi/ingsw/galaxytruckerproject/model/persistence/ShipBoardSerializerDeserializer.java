package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class ShipBoardSerializerDeserializer {

    String fileName;


    public ShipBoardSerializerDeserializer(String fileName) {
        this.fileName = fileName;
    }

    public void save(ShipBoard shipBoard) {
        Optional<Tile>[][] tilesTable = shipBoard.getTilesTable();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    Optional<Tile> tile = tilesTable[i][j];
                    String line;
                    if  (tile.isPresent()) {
                        line = tile.get().toStringData();
                    }
                    else {
                        line = "NullTile";
                    }
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error while writing file for shipboard save: " + e.getMessage());
        }
    }

    public int load(Player player, int startLine) {

        ShipBoard shipBoard = player.getShipBoard();
        Tile[][] tilesTable = new Tile[5][7];
        ArrayList<Tile> loadedTiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int currentLine = 0;
            int endLine = 0;

            while ((line = reader.readLine()) != null) {
                currentLine++;
                if (currentLine < startLine) continue;

                if (line.equals("NullTile")) {
                    loadedTiles.add(null);
                    continue;
                }

                Tile tile = tileTypeLoader(line);
                if (tile == null) {
                    //Stop if line isn't parseable in this class
                    break;
                }
                loadedTiles.add(tile);
            }
            endLine = currentLine;

            if (loadedTiles.size() < 35) {
                System.out.println("Error: not enough tiles to fill shipboard.");
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

        } catch (IOException e) {
            System.out.println("Error while reading file for shipboard load: " + e.getMessage());
        }
        return -1;
    }

    public Tile tileTypeLoader(String line) {
        String[] lineSplit = line.split(" ");
        switch(lineSplit[0]) {
            case "AL" -> {
                AlienLifeSupportsSystem al = new AlienLifeSupportsSystem();
                al.tileLoader(lineSplit);
                return al;
            }
            case "BC" -> {
                BatteryComponents bc = new BatteryComponents();
                bc.tileLoader(lineSplit);
                return bc;
            }
            case "CB" -> {
                CargoBlue cb = new CargoBlue();
                cb.tileLoader(lineSplit);
                return cb;
            }
            case "CR" -> {
                CargoRed cr = new CargoRed();
                cr.tileLoader(lineSplit);
                return cr;
            }
            case "DC"  -> {
                DoubleCannon dc = new DoubleCannon();
                dc.tileLoader(lineSplit);
                return dc;
            }
            case "DE" -> {
                DoubleEngine de = new DoubleEngine();
                de.tileLoader(lineSplit);
                return de;
            }
            case "EC"  -> {
                EquipCabin ec = new EquipCabin();
                ec.tileLoader(lineSplit);
                return ec;
            }
            case "SH" -> {
                Shields sh = new Shields();
                sh.tileLoader(lineSplit);
                return sh;
            }
            case "SC" -> {
                SingleCannon sc = new SingleCannon();
                sc.tileLoader(lineSplit);
                return sc;
            }
            case "SE"  -> {
                SingleEngine se = new SingleEngine();
                se.tileLoader(lineSplit);
                return se;
            }
            case "ST" -> {
                StartingCabin s =  new StartingCabin();
                s.tileLoader(lineSplit);
                return s;
            }
            case "VT" -> {
                return new VoidTile();
            }
        }
        return null;
    }
}
