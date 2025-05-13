package it.polimi.ingsw.galaxytruckerproject.model.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.description.field.FieldDescription;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CardDeck {
    private final ArrayList<Card> deckLvl1;
    private final ArrayList<Card> deckLvl2;
    private Map<Integer,Card> completeDeck;

    public CardDeck(String filename) {
        this.deckLvl1 = new ArrayList<>();
        this.deckLvl2 = new ArrayList<>();
        loadCards(filename);
    }

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
                    card.setId(cardId);
                } else if (card.getLevel() == 2) {
                    this.deckLvl2.add(card);
                    card.setId(cardId);
                }
                cardId++;
            }
        } catch (IOException e) {
            System.out.println("Error during the loading of " + filename);
            e.printStackTrace();
        }
    }

    public ArrayList<Card> getDeckLvl1() {
        return deckLvl1;
    }

    public ArrayList<Card> getDeckLvl2() {
        return deckLvl2;
    }

    public Card drawCardLvl1() {
        return deckLvl1.removeFirst();
    }

    public Card drawCardLvl2() {
        return deckLvl2.removeFirst();
    }

    public ArrayList<Card> getTier2FlightCards() {
        //Collections.shuffle(this.deckLvl1);
       // Collections.shuffle(this.deckLvl2);
        ArrayList<Card> tier2FlightCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tier2FlightCards.add(drawCardLvl1());
            tier2FlightCards.add(drawCardLvl2());
            tier2FlightCards.add(drawCardLvl2());
        }
        return tier2FlightCards;
    }

    public ArrayList<Card> deckFromIDs(ArrayList<Integer> ids) {
        loadCompleteDeckMap();
        ArrayList<Card> deckFromIDs = new ArrayList<>();

        for (int id: ids) {
            Card card = completeDeck.get(id);
            if (card != null) {
                deckFromIDs.add(card);
            } else {
                System.out.println("Card with id: " + id + " not found in completeDeckMap");
            }
        }

        return deckFromIDs;
    }

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