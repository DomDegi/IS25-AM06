package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class ClientController {

    private ClientState state;
    private int turns=0;
    private ArrayList<Goods> goodsList;
    private final Client client;
    private int indexDeckInHand;
    public ClientController (Client client) {
        this.client = client;
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
                int CoordinatesX;
                try {
                    CoordinatesX = Integer.parseInt(words[0]);
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input format. Please provide integer values.");
                    return;
                }
                int CoordinatesY;
                try {
                    CoordinatesY = Integer.parseInt(words[1]);
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input format. Please provide integer values.");
                    return;
                }
                Coordinates coordinates = new Coordinates(CoordinatesX, CoordinatesY);
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
                    if(chose>1&&chose<5){
                        System.out.println(shipboard[chose]);
                        return;
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
                    int CoordinatesX;
                    try {
                        CoordinatesX = Integer.parseInt(words[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input format. Please provide integer values.");
                        return;
                    }
                    int CoordinatesY;
                    try {
                        CoordinatesY = Integer.parseInt(words[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input format. Please provide integer values.");
                        return;
                    }
                    Coordinates coordinates = new Coordinates(CoordinatesX, CoordinatesY);
                    //check is free
                    state=ClientState.S_END_DRAW_TILE_CARD;
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
                int CoordinatesX;
                try {
                    CoordinatesX = Integer.parseInt(words[0]);
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input format. Please provide integer values.");
                    return;
                }
                int CoordinatesY;
                try {
                    CoordinatesY = Integer.parseInt(words[1]);
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input format. Please provide integer values.");
                    return;
                }
                Coordinates coordinates = new Coordinates(CoordinatesX, CoordinatesY);
                //check
            }
        }
    }






}
