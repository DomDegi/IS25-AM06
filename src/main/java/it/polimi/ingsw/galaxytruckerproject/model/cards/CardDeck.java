package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a deck of cards divided by level.
 * Provides functionality for loading, drawing, and organizing cards.
 */
public class CardDeck {

    /** List of level 1 cards */
    private final ArrayList<Card> deckLvl1;

    /** List of level 2 cards */
    private final ArrayList<Card> deckLvl2;

    /** Map containing all cards indexed by their ID */
    private Map<Integer, Card> completeDeck;

    /**
     * Constructor that initializes and loads the cards from a file.
     * @param filename JSON file containing the cards
     */
    public CardDeck(String filename) {
        this.deckLvl1 = new ArrayList<>();
        this.deckLvl2 = new ArrayList<>();
        loadCards(filename);
    }

    /**
     * Loads the cards from a JSON file and distributes them into level 1 and level 2 decks.
     * @param filename name of the JSON file in resources
     */
    public void loadCards(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (inputStream == null) {
                System.out.println("Error: file " + filename + " not found");
                return;
            }

            CardCollection cardCollection = objectMapper.readValue(inputStream, CardCollection.class);
            ArrayList<Card> allCards = cardCollection.getCards();

            int cardId = 1;
            for (Card card : allCards) {
                if (card.getLevel() == 1) {
                    this.deckLvl1.add(card);
                } else if (card.getLevel() == 2) {
                    this.deckLvl2.add(card);
                }
                card.setId(cardId);
                cardId++;
            }
        } catch (IOException e) {
            System.out.println("Error during the loading of " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Returns the level 1 card deck.
     * @return list of level 1 cards
     */
    public ArrayList<Card> getDeckLvl1() {
        return deckLvl1;
    }

    /**
     * Returns the level 2 card deck.
     * @return list of level 2 cards
     */
    public ArrayList<Card> getDeckLvl2() {
        return deckLvl2;
    }

    /**
     * Draws and removes the first card from the level 1 deck.
     * @return the drawn level 1 card
     */
    public Card drawCardLvl1() {
        return deckLvl1.removeFirst();
    }

    /**
     * Draws and removes the first card from the level 2 deck.
     * @return the drawn level 2 card
     */
    public Card drawCardLvl2() {
        return deckLvl2.removeFirst();
    }

    /**
     * Creates a mixed flight deck composed of level 1 and level 2 cards.
     * Shuffles both decks and returns 12 cards (4 groups of 3 cards: 1 from level 1 and 2 from level 2).
     * @return combined and shuffled flight deck
     */
    public ArrayList<Card> getTier2FlightCards() {
        Collections.shuffle(this.deckLvl1);
        Collections.shuffle(this.deckLvl2);
        ArrayList<Card> tier2FlightCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tier2FlightCards.add(drawCardLvl1());
            tier2FlightCards.add(drawCardLvl2());
            tier2FlightCards.add(drawCardLvl2());
        }
        return tier2FlightCards;
    }

    /**
     * Returns a list of cards corresponding to the given IDs.
     * @param ids list of card IDs
     * @return list of cards
     */
    public ArrayList<Card> deckFromIDs(ArrayList<Integer> ids) {
        loadCompleteDeckMap();
        ArrayList<Card> deckFromIDs = new ArrayList<>();

        for (int id : ids) {
            Card card = completeDeck.get(id);
            if (card != null) {
                deckFromIDs.add(card);
            } else {
                System.out.println("Card with id: " + id + " not found in completeDeckMap");
            }
        }

        return deckFromIDs;
    }

    /**
     * Populates the completeDeck map with all cards indexed by their ID.
     */
    public void loadCompleteDeckMap() {
        this.completeDeck = new HashMap<>();
        ArrayList<Card> temp = new ArrayList<>();
        temp.addAll(this.deckLvl1);
        temp.addAll(this.deckLvl2);
        for (Card card : temp) {
            completeDeck.put(card.getId(), card);
        }
    }
}