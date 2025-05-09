package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import static it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory.loadTilesFromJson;

class TileFactoryTest {
    private ArrayList<Tile> tiles;
    private ArrayDeque<Tile> tilesQueue;

    @BeforeEach
    void setUp() {
        tiles = loadTilesFromJson("TilesLikeGraphics.json");
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