package it.polimi.ingsw.galaxytruckerproject.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {

    private ArrayList<Card> cardsLvl2;
    private ArrayList<Card> cardsLvl1;
    private CardDeck cardDeck;


    @BeforeEach
    void setUp() {
        this.cardDeck = new CardDeck("cards.json")
        cardsLvl1 = cardDeck.getDeckLvl1();
        cardsLvl2 = cardDeck.getDeckLvl2();
    }

    @Test
    void print_cards_to_output_file() {
        String filename = "cardsList_output";
        try (FileWriter writer = new FileWriter(filename) {
            for (Card card:  cardsLvl1) {
                writer.write(card.toString() + "\n\n");
            }
        }
    }
}