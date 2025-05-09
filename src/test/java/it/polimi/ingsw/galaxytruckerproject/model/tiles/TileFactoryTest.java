package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import static it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory.loadTilesFromJson;

class TileFactoryTest {
    private ArrayList<Tile> tiles;

    @BeforeEach
    void setUp() {
        tiles = loadTilesFromJson("Tiles.json");
        ConcurrentLinkedDeque<Tile> tilesQueue = new TileFactory().getStack(tiles);
    }

    @Test
    public void print_tiles_to_file() {
        String filename = "tileList_output";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Tile tile : tiles) {
                writer.write(tile.toString() + "\n\n");
            }
            System.out.println("Tiles written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}