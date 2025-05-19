package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TileFactory {

    public static ArrayList<Tile> loadTilesFromJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Tile> tiles = new ArrayList<>();
        int key=1;

        try (InputStream inputStream = TileFactory.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Errore: file " + fileName + " non trovato nelle risorse!");
            }

            // Deserializza il JSON in una lista di oggetti generici (JsonTile)
            List<JsonTile> jsonTiles = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JsonTile.class));

            for (JsonTile jsonTile : jsonTiles) {
                Tile tile = createTileFromJson(jsonTile,key);
                key++;
                if (tile != null) {
                    tiles.add(tile);
                }
            }


        } catch (IOException e) {
            System.err.println("Error during loading of tiles json " + fileName);
            e.printStackTrace();
        }

        return tiles;
    }

    private static Tile createTileFromJson(JsonTile jsonTile, int key) {
        return switch (jsonTile.getType()) {
            case "DoubleCannon" ->
                    new DoubleCannon(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "SingleCannon" ->
                    new SingleCannon(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "Shields" ->
                    new Shields(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "SingleEngine" ->
                    new SingleEngine(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "DoubleEngine" ->
                    new DoubleEngine(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "Pipe" ->
                    new Pipe(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "EquipCabin" ->
                    new EquipCabin(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "BatteryComponents" ->
                    new BatteryComponents(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key, jsonTile.getNumCells());
            case "AlienLifeSupportsSystem" ->
                    new AlienLifeSupportsSystem(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key, jsonTile.getAlienType());
            case "CargoBlue" ->
                    new CargoBlue(jsonTile.getTotSpaces(), jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            case "CargoRed" ->
                    new CargoRed(jsonTile.getTotSpaces(), jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getImagePath(), 0, key);
            default -> {
                System.err.println("⚠️ Tipo di tile sconosciuto: " + jsonTile.getType());
                yield null;
            }
        };
    }

    public ConcurrentLinkedDeque<Tile> getStack(ArrayList<Tile> tiles) {
        Collections.shuffle(tiles);
        return new ConcurrentLinkedDeque<>(tiles);
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
        private String imagePath;
        private int key;

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
        public String getImagePath() { return imagePath; }
        public int getKey() {return key;}
        public void setKey(int key) {this.key = key;}

    }
}
