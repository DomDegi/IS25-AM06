package it.polimi.ingsw.galaxytruckerproject.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckerproject.FlightBoard;
import it.polimi.ingsw.galaxytruckerproject.Player;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class TestCardDeck {
    private ArrayList<Card> testDeck;

    public TestCardDeck(String filename) {
        this.testDeck = new ArrayList<>();
        loadTestCards(filename);
    }

    public void loadTestCards(String filename){
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (inputStream == null){
                System.out.println("Errore: file " + filename + " not found");
                return;
            }
            CardCollection cardCollection = objectMapper.readValue(inputStream, CardCollection.class);

            testDeck = cardCollection.getCards();
            Collections.shuffle(testDeck);
        }
        catch(IOException e){
            System.out.println("Errore durante il caricamento del file JSON");
            e.printStackTrace();
        }
    }

    public ArrayList<Card> getTestDeck() {
        return testDeck;
    }

    public Card drawCard(){
        return testDeck.removeFirst();
    }
}
