package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.Game;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

        gameController = new GameController("pippo");
    }

    @Test
    void serializeGame() {
        gameSerializerDeserializer.save(game);
        gameSerializerDeserializer.load(gameController,0);
        assertEquals(gameController.getGame().getMode(), game.getMode());
        assertEquals(gameController.getGame().getPlayerCount(), game.getPlayerCount());
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

        //the following assert only works if we don't shuffle the deck
        /*
        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.get(i).getId(), copy.get(i).getId());
        } */
    }

}