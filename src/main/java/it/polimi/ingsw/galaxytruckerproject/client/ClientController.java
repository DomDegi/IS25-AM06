package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;

public class ClientController {

    private ClientState state;
    private int turns;
    private ArrayList<Goods> goodsList;
    private final Client client;
    private int indexDeckInHand;

    public ClientController (Client client) {
        this.client = client;
        this.indexDeckInHand = 0;
        this.turns=0;
        state=ClientState.LOBBY;
    }

    public void input(String input) {
        input.toLowerCase();
        input.replaceAll("\\s+"," ");
        String [] words = input.split(" ");
        switch (state) {
            case LOBBY:{

            }

            case ACTION:{
                if (words[0].equals("yes")) {
                    //accept action
                }
                if(words[0].equals("no")) {
                    //deny action
                }
            }

            case MANAGE_GOODS:{
                GoodsManager goodsManager=new GoodsManager(client.getMe(),goodsList);
                goodsManager.getReward(words);
            }

            case COORD_REQUEST:{
                //if coordinate process else END PROCESSING
                transformCoordinates(words);
            }

            case MANAGE_CARDS:{
                if (words[0].equals("previous")) {
                    //accept action
                }
                if(words[0].equals("next")) {
                    //deny action
                }
                if (words[0].equals("done")) {
                    //accept action
                }
            }

            case S_END_DRAW_TILE_CARD:{
                if(words[0].equals("done")){
                    state=ClientState.S_FINISHED;
                    //notify done
                }
                if(words[0].equals("turn")&&turns==0) {
                    turns++;
                    //notify time
                    //notify turns
                }
                if(words[0].equals("check")) {
                    int chose;
                    if(words[1].isEmpty()){
                        System.out.println("Choose one of the shipboards");
                        return;
                    }
                    try {
                        chose = Integer.parseInt(words[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input format. Please provide integer values.");
                        return ;
                    }
                    if(chose==0) {
                        System.out.println(client.getMe().getShipBoard());
                    }
                    if(chose>=1 && chose<=4){
                        System.out.println(client.getPlayersList().get(indexDeckInHand).getShipBoard());
                    }
                    return;
                }
                if(words[0].equals("draw") && words[1].equals("tile")){
                    state=ClientState.S_MANAGE_DRAWN_TILE;
                    //notify drawn tile
                }
                if(words[0].equals("draw") && words[1].equals("card")){
                    int chose;
                    if(words[2].isEmpty()){
                        System.out.println("Choose one of the decks");
                        return;
                    }
                    try {
                        chose = Integer.parseInt(words[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input format. Please provide integer values.");
                        return ;
                    }
                    if(chose>0&&chose<4){
                        state=ClientState.MANAGE_CARDS;
                        indexDeckInHand=chose;
                        //notify drawn card
                        ArrayList<Card> displayedCards=client.getDeck().get(indexDeckInHand);
                        return;
                    }
                    return;
                }
            }

            case S_MANAGE_DRAWN_TILE:{
                if(words[0].equals("rotate")){

                }
                if(words[0].equals("position")) {
                    for (int i = 0; i < words.length - 1; i++) {
                        words[i] = words[i + 1];
                    }
                    transformCoordinates(words);
                    //notify positioned
                }
                if(words[0].equals("refuse")){
                    state=ClientState.S_END_DRAW_TILE_CARD;
                    //notify refused
                }
                if(words[0].equals("book")){
                    state=ClientState.S_END_DRAW_TILE_CARD;
                    //notify booked
                }
            }

            case S_FINISHED:{
                if(words[0].equals("turn")&&turns<2) {
                    turns++;
                    //notify time
                }
            }

            case S_CORRECT_SHIPBOARD:{
                transformCoordinates(words);
                //check
            }
        }
    }

    private Coordinates transformCoordinates(String[] input) {
        int CoordinatesX;
        try {
            CoordinatesX = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        int CoordinatesY;
        try {
            CoordinatesY = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        return new Coordinates(CoordinatesX, CoordinatesY);
    }
}
