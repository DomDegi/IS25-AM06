package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
    private ArrayList<Card> deckLvl1;
    private ArrayList<Card> deckLvl2;

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
                System.out.println("Errore: file " + filename + " not found");
                return;
            }
            CardCollection cardCollection = objectMapper.readValue(inputStream, CardCollection.class);
            ArrayList<Card> allCards = cardCollection.getCards();

            for (Card card : allCards) {
                if (card.getLevel() == 1) {
                    this.deckLvl1.add(card);
                } else if (card.getLevel() == 2) {
                    this.deckLvl2.add(card);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento del file JSON");
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
        Collections.shuffle(this.deckLvl1);
        Collections.shuffle(this.deckLvl2);
        ArrayList<Card> tier2FlightCards = new ArrayList<>();
        for (int i = 0; i < 8; i++) tier2FlightCards.add(drawCardLvl2());
        for (int i = 0; i < 4; i++) tier2FlightCards.add(drawCardLvl1());
        Collections.shuffle(tier2FlightCards);
        return tier2FlightCards;
    }
}

