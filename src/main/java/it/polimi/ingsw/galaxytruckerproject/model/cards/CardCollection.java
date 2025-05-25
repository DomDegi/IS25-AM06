package it.polimi.ingsw.galaxytruckerproject.model.cards;

import java.util.ArrayList;

/**
 * Represents a collection of cards used in the game.
 * Provides accessors to get and set the list of cards.
 */
public class CardCollection {

    /**
     * List of cards in the collection.
     */
    private ArrayList<Card> cards;

    /**
     * Returns the list of cards.
     * @return list of {@link Card} objects
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Sets the list of cards.
     * @param cards list of {@link Card} objects to assign
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}