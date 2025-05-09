package it.polimi.ingsw.galaxytruckerproject.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.util.ArrayList;


class CardDeckTest {

    private ArrayList<Card> cardsLvl2;
    private ArrayList<Card> cardsLvl1;
    private ArrayList<Card> trialCards;


    @BeforeEach
    void setUp() {
        CardDeck cardDeck = new CardDeck("cards.json");
        TrialCardDeck trialCardDeck = new TrialCardDeck("trialFlightCards.json");
        cardsLvl1 = cardDeck.getDeckLvl1();
        cardsLvl2 = cardDeck.getDeckLvl2();
        trialCards = trialCardDeck.getTrialDeck();
    }

    @Test
    void print_cards_to_output_file() {
        String filename = "cardsList_output";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Card card: cardsLvl1) {
                writer.write(card.toString() + "\n");
            }
            for (Card card: cardsLvl2) {
                writer.write(card.toString() + "\n");
            }
        } catch (Exception ignore){}
    }

    @Test
    void print_trial_deck_to_output_file() {
        String filename = "trialCardsList_output";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Card card: trialCards) {
                writer.write(card.toString() + "\n");
            }
        } catch (Exception ignore){}
    }
}