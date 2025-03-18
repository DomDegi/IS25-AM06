package it.polimi.ingsw.galaxytruckerproject.tiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileFactory {

    public static ArrayList<Tile> loadTilesFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Tile> tiles = new ArrayList<>();

        try {
            File jsonFile = new File(filePath);

            // Deserializza il JSON in una lista di oggetti generici (può essere qualsiasi tipo di Tile, CargoHold o SingleCannon)
            List<JsonTile> jsonTiles = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, JsonTile.class));

            // Creiamo le istanze di Tile, CargoHold o SingleCannon a seconda dei dati nel JSON
            for (JsonTile jsonTile : jsonTiles) {
                Tile tile = null;

                switch (jsonTile.getType()) {
                    case "DoubleCannon": {
                        tile = new DoubleCannon(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "SingleCannon":{
                        tile = new SingleCannon(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "Shields":{
                        tile=new Shields(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "SingleEngine":{
                        tile=new SingleEngine(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "DoubleEngine":{
                        tile=new DoubleEngine(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "Pipe":{
                        tile=new Pipe(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "EquipCabin":{
                        tile=new EquipCabin(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case "BatteryComponents":{
                        tile=new BatteryComponents(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getNumCells());
                        break;
                    }
                    case "AlienLifeSupportsSystem": {
                        tile=new AlienLifeSupportsSystem(jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest(), jsonTile.getAlienType());
                        break;
                    }
                    case"CargoBlue":{
                        tile=new CargoBlue(jsonTile.getTotSpaces(), jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }
                    case"CargoRed":{
                        tile=new CargoRed(jsonTile.getTotSpaces(), jsonTile.getNorth(), jsonTile.getEast(), jsonTile.getSouth(), jsonTile.getWest());
                        break;
                    }

                    default:
                        System.out.println("Unknown tile type: " + jsonTile.getType());
                        break;
                }

                if (tile != null) {
                    tiles.add(tile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tiles;
    }

    public ArrayDeque<Tile> getStack(ArrayList<Tile> tiles) {
        Collections.shuffle(tiles);
        ArrayDeque<Tile> stack = new ArrayDeque<>();
        for (Tile tile : tiles) {
            stack.push(tile);
        }
        return stack;
    }

    // Classe JSON per mappare i dati dal file JSON
    static class JsonTile {
        private String type;
        private Link north;
        private Link east;
        private Link south;
        private Link west;
        private int numCells;
        private CrewType alienType;
        private int totSpaces;

        // Getters e Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Link getNorth() {
            return north;
        }

        public void setNorth(Link north) {
            this.north = north;
        }

        public Link getEast() {
            return east;
        }

        public void setEast(Link east) {
            this.east = east;
        }

        public Link getSouth() {
            return south;
        }

        public void setSouth(Link south) {
            this.south = south;
        }

        public Link getWest() {
            return west;
        }

        public void setWest(Link west) {
            this.west = west;
        }

        public int getNumCells() {
            return numCells;
        }

        public void setNumCells(int numCells) {
            this.numCells = numCells;
        }

        public CrewType getAlienType() {
            return alienType;
        }

        public void setAlienType(CrewType alienType) {
            this.alienType = alienType;
        }

        public int getTotSpaces() {
            return totSpaces;
        }

        public void setTotSpaces(int totSpaces) {
            this.totSpaces = totSpaces;
        }

    }
}
