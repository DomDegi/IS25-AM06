package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * A factory class for creating and managing {@link Tile} objects from JSON resources or string data.
 * Provides methods for loading, deserializing, and organizing tile instances used in the game.
 */
public class TileFactory {

    /**
     * Loads a list of tiles from a JSON file located in the resources.
     *
     * @param fileName the name of the JSON file.
     * @return a list of deserialized {@link Tile} objects.
     */
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
    /**
     * Creates a specific {@link Tile} instance based on the provided {@link JsonTile} and assigns a unique key.
     *
     * @param jsonTile the data structure representing a tile in JSON format.
     * @param key the unique key to assign to the tile.
     * @return the created {@link Tile}, or null if the type is unknown.
     */
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

    /**
     * Shuffles the given list of tiles and returns them as a thread-safe stack.
     *
     * @param tiles the list of tiles to shuffle.
     * @return a {@link ConcurrentLinkedDeque} representing the stack of tiles.
     */
    public ConcurrentLinkedDeque<Tile> getStack(ArrayList<Tile> tiles) {
        //Collections.shuffle(tiles);
        return new ConcurrentLinkedDeque<>(tiles);
    }

    /**
     * Reconstructs a stack of tiles based on a list of tile IDs.
     *
     * @param ids the list of tile IDs.
     * @return a stack ({@link ConcurrentLinkedDeque}) containing the corresponding tiles.
     */
    public ConcurrentLinkedDeque<Tile> stackFromIDs(ArrayList<Integer> ids) {
        Map<Integer,Tile> tileMap = this.tileMap();
        ConcurrentLinkedDeque<Tile> stack = new ConcurrentLinkedDeque<>();
        for (Integer id : ids) {
            stack.add(tileMap.get(id));
        }
        return stack;
    }

    /**
     * Creates a concurrent map of tile IDs to tile instances from a given list of IDs.
     *
     * @param ids the list of tile IDs.
     * @return a {@link ConcurrentHashMap} mapping IDs to {@link Tile} objects.
     */
    public ConcurrentHashMap<Integer, Tile> mapFromIDs(ArrayList<Integer> ids) {
        Map<Integer,Tile> tileMap = this.tileMap();
        ConcurrentHashMap<Integer,Tile> newMap = new ConcurrentHashMap<>(ids.size());
        for (Integer id : ids) {
            newMap.put(id,tileMap.get(id));
        }
        return newMap;
    }

    /**
     * Loads all tiles from the default JSON file and creates a map from tile key to tile instance.
     *
     * @return a {@link Map} mapping tile keys to {@link Tile} instances.
     */
    public Map<Integer,Tile> tileMap() {
        ArrayList<Tile> tileArray = loadTilesFromJson("Tiles.json");

        Map<Integer,Tile> tileMap = new HashMap<>(tileArray.size());
        for(Tile tile: tileArray) {
            tileMap.put(tile.getKey(),tile);
        }
        return tileMap;
    }
    /**
     * Loads a tile object from a single-line string representation.
     * The first token in the line determines the type of tile.
     *
     * @param line the string representing a tile.
     * @return the corresponding {@link Tile} instance.
     */
    public static Tile load(String line) {
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
                StartingCabin st =  new StartingCabin();
                st.tileLoader(lineSplit);
                return st;
            }
            case "PP" -> {
                Pipe pp = new Pipe();
                pp.tileLoader(lineSplit);
                return pp;
            }
            case "VT" -> {
                return new VoidTile();
            }
        }
        return null;
    }

    /**
     * Inner static class used for JSON deserialization of tiles.
     * Contains fields that match the structure of tile entries in the JSON file.
     */
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
        public void setImagePath() { this.imagePath = imagePath; }
        public int getKey() {return key;}
        public void setKey(int key) {this.key = key;}

    }
}
