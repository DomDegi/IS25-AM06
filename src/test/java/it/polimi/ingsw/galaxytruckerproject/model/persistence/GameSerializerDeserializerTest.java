package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.*;

class GameSerializerDeserializerTest {

    GameController gameController;
    Game game = new Game(GameMode.LEVEL2,4);
    String fileName =  "serializerTest";
    GameSerializerDeserializer gameSerializerDeserializer = new GameSerializerDeserializer(fileName);

    @BeforeEach
    void setUp() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            // This will clear the file content by opening it in overwrite mode
            writer.write("");  // Optional: explicitly write to ensure file is cleared.
        } catch (IOException e) {
            System.out.println("Error while clearing file: " + e.getMessage());
        }
        game.addPlayer("pluto", PlayersColor.RED);
        for (int i = 0; i < 3; i++) {
            game.drawTile("pluto");
            game.refuseTile("pluto");
        }
        gameController = new GameController("pippo");
    }

    @Test
    void serializeGame() {
        gameSerializerDeserializer.save(game);
        gameSerializerDeserializer.load(gameController,0);
        assertEquals(gameController.getGame().getMode(), game.getMode());
        assertEquals(gameController.getGame().getPlayerCount(), game.getPlayerCount());
    }

    @Test
    void card_test() {
        serializeGame();
        ArrayList<Card> original = game.getInGameCards();
        ArrayList<Card> copy= gameController.getGame().getInGameCards();

        assertEquals(original.size(), copy.size());

        for (Card card : original) {
            System.out.println(card.toString());
        }
        System.out.println("\n\n\n");
        for (Card card : copy) {
            System.out.println(card.toString());
        }

        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.get(i).getId(), copy.get(i).getId());
        }
    }

    @Test
    void tile_stack_test() {
        serializeGame();
        ConcurrentLinkedDeque<Tile> original = game.getTileStack();
        ConcurrentLinkedDeque<Tile> copy = gameController.getGame().getTileStack();

        assertEquals(original.size(), copy.size());

        Iterator<Tile> itOriginal = original.iterator();
        Iterator<Tile> itCopy = copy.iterator();

        while (itOriginal.hasNext() && itCopy.hasNext()) {
            Tile tileOriginal = itOriginal.next();
            Tile tileCopy = itCopy.next();
            assertEquals(tileOriginal.getKey(), tileCopy.getKey());
        }
    }

    @Test
    void turned_tiles_test() {
        serializeGame();
        ConcurrentHashMap<Integer,Tile> original = game.getTurnedTiles();
        ConcurrentHashMap<Integer,Tile> copy = gameController.getGame().getTurnedTiles();

        assertEquals(original.size(), copy.size());

        for (Tile tile : original.values()) {
            System.out.println(tile.toString());
        }

        System.out.println("\n\n\n");

        for (Tile tile : original.values()) {
            System.out.println(tile.toString());
        }

        for (Integer key : copy.keySet()) {
            assertTrue(original.containsKey(key));
        }
    }
}