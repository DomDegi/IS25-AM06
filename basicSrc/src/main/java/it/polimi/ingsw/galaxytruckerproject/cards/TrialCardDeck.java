package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class TrialCardDeck {
    private ArrayList<Card> trialDeck;

    public TrialCardDeck(String filename) {
        this.trialDeck = new ArrayList<>();
        loadTrialCards(filename);
    }

    public void loadTrialCards(String filename){
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (inputStream == null){
                System.out.println("Errore: file " + filename + " not found");
                return;
            }
            CardCollection cardCollection = objectMapper.readValue(inputStream, CardCollection.class);

            trialDeck = cardCollection.getCards();
            Collections.shuffle(trialDeck);
        }
        catch(IOException e){
            System.out.println("Errore durante il caricamento del file JSON");
            e.printStackTrace();
        }
    }

    public ArrayList<Card> getTrialDeck() {
        return trialDeck;
    }

    public Card drawCard(){
        return trialDeck.removeFirst();
    }
}
