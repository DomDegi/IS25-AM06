package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a trial deck used for test or demo purposes.
 * Cards are loaded from a JSON file and assigned predefined IDs.
 */
public class TrialCardDeck {

    /** List of cards in the trial deck */
    private ArrayList<Card> trialDeck;

    /**
     * Constructor that loads cards from a file and assigns specific IDs.
     * @param filename the JSON file containing card data
     */
    public TrialCardDeck(String filename) {
        this.trialDeck = new ArrayList<>();
        loadTrialCards(filename);
        int[] idsToGive = {2, 4, 5, 9, 13, 16, 18, 19};
        giveIds(idsToGive);
    }

    /**
     * Loads trial cards from a JSON file.
     * @param filename the name of the JSON file to load
     */
    public void loadTrialCards(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (inputStream == null) {
                System.out.println("Error: file " + filename + " not found");
                return;
            }

            CardCollection cardCollection = objectMapper.readValue(inputStream, CardCollection.class);
            trialDeck = cardCollection.getCards();
            Collections.shuffle(trialDeck);
        } catch (IOException e) {
            System.out.println("Error loading JSON file");
            e.printStackTrace();
        }
    }

    /**
     * Assigns predefined IDs to the first few cards in the deck.
     * @param idsToGive array of IDs to assign
     */
    public void giveIds(int[] idsToGive) {
        for (int i = 0; i < idsToGive.length; i++) {
            trialDeck.get(i).setId(idsToGive[i]);
        }
    }

    /**
     * Returns the full list of trial cards.
     * @return list of {@link Card} in the trial deck
     */
    public ArrayList<Card> getTrialDeck() {
        return trialDeck;
    }

    /**
     * Draws and removes the first card from the trial deck.
     * @return the drawn {@link Card}
     */
    public Card drawCard() {
        return trialDeck.removeFirst();
    }

    /**
     * Returns a list of cards from the deck that match the given IDs.
     * @param ids list of IDs to retrieve
     * @return list of matching {@link Card} objects
     */
    public ArrayList<Card> deckFromIDs(ArrayList<Integer> ids) {
        ArrayList<Card> deckFromIDs = new ArrayList<>();
        for (Card card : trialDeck) {
            if (ids.contains(card.getId())) {
                deckFromIDs.add(card);
            }
        }
        return deckFromIDs;
    }
}
