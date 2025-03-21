package it.polimi.ingsw.galaxytruckerproject.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {

    private ArrayList<Card> cardsLvl2;
    private ArrayList<Card> cardsLvl1;
    private ArrayList<Card> trialCards;


    @BeforeEach
    void setUp() {
        CardDeck cardDeck = new CardDeck("cards.json");
        cardsLvl1 = cardDeck.getDeckLvl1();
        cardsLvl2 = cardDeck.getDeckLvl2();
        trialCards = new TrialCardDeck("trialFlightCards.json").getTrialDeck();
    }

    @Test
    void print_cards_to_output_file() {
        String filename = "cardsList_output";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Card card : cardsLvl1) {
                writer.write(card.toString() + "\n");
            }
            for (Card card : cardsLvl2) {
                writer.write(card.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void print_trial_cards_to_output_file() {
        String filename = "trialCardsList_output";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Card card : trialCards) {
                writer.write(card.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}