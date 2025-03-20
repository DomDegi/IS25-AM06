package it.polimi.ingsw.galaxytruckerproject.tiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileFactory {

    public static ArrayList<Tile> loadTilesFromJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Tile> tiles = new ArrayList<>();

        try (InputStream inputStream = TileFactory.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Errore: file " + fileName + " non trovato nelle risorse!");
            }

            // Deserializza il JSON in una lista di oggetti generici (JsonTile)
            List<JsonTile> jsonTiles = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonTile.class));

            for (JsonTile jsonTile : jsonTiles) {
                Tile tile = createTileFromJson(jsonTile);
                if (tile != null) {
                    tiles.add(tile);
                }
            }

            System.out.println("✅ File " + fileName + " caricato correttamente! Tiles trovate: " + tiles.size());

        } catch (IOException e) {
            System.err.println("❌ Errore durante il caricamento del file JSON: " + fileName);
            e.printStackTrace();
        }

        return tiles;
    }

    private static Tile createTileFromJson(JsonTile jsonTile) {
        switch (jsonTile.getType()) {
            case "DoubleCannon":
                return new DoubleCannon(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "SingleCannon":
                return new SingleCannon(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "Shields":
                return new Shields(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "SingleEngine":
                return new SingleEngine(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "DoubleEngine":
                return new DoubleEngine(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "Pipe":
                return new Pipe(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "EquipCabin":
                return new EquipCabin(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "BatteryComponents":
                return new BatteryComponents(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getNumCells());
            case "AlienLifeSupportsSystem":
                return new AlienLifeSupportsSystem(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getAlienType());
            case "CargoBlue":
                return new CargoBlue(jsonTile.getTotSpaces(), jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            case "CargoRed":
                return new CargoRed(jsonTile.getTotSpaces(), jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
            default:
                System.err.println("⚠️ Tipo di tile sconosciuto: " + jsonTile.getType());
                return null;
        }
    }

    public ArrayDeque<Tile> getStack(ArrayList<Tile> tiles) {
        Collections.shuffle(tiles);
        return new ArrayDeque<>(tiles);
    }

    // Classe JSON per il mapping dei dati
    public static class JsonTile {
        private String type;
        private Link north;
        private Link east;
        private Link south;
        private Link west;
        private int numCells;
        private CrewType alienType;
        private int totSpaces;

        // Getters e Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Link getNorth() { return north; }
        public void setNorth(Link north) { this.north = north; }
        public Link getEast() { return east; }
        public void setEast(Link east) { this.east = east; }
        public Link getSouth() { return south; }
        public void setSouth(Link south) { this.south = south; }
        public Link getWest() { return west; }
        public void setWest(Link west) { this.west = west; }
        public int getNumCells() { return numCells; }
        public void setNumCells(int numCells) { this.numCells = numCells; }
        public CrewType getAlienType() { return alienType; }
        public void setAlienType(CrewType alienType) { this.alienType = alienType; }
        public int getTotSpaces() { return totSpaces; }
        public void setTotSpaces(int totSpaces) { this.totSpaces = totSpaces; }
    }
}
