package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class ClientController {

    private ClientState state;
    private ArrayList<Goods> goodsList;
    private final Client client;
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
                if (words[0].equals("yes"))
                {
                    //accept action
                }
                if(words[0].equals("no"))
                {
                    //deny action
                }

            }
            case MANAGE_GOODS:{
                GoodsManager goodsManager=new GoodsManager(client.getMe(),goodsList);
                goodsManager.getReward(words);
            }
            case COORD_REQUEST:{

            }
            case S_END_DRAW_TILE_CARD:{
                if(words[0].equals("done")){
                    //notify done
                }
                if(words[0].equals("draw") && words[1].equals("tile")){
                    state=ClientState.S_MANAGE_DRAWN_TILE;
                    //notify drawn tile
                }
                if(words[0].equals("draw") && words[1].equals("card")){
                    SplittableRandom
                    //notify drawn card
                }

            }
            case S_MANAGE_DRAWN_TILE:{
                if
            }
            case S_FINISHED:{

            }
            case S_CORRECT_SHIPBOARD:{

            }
        }
    }






}
