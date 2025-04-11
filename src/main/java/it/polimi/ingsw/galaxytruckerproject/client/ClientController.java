package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.goods.Goods;
//import it.polimi.ingsw.galaxytruckerproject.model.goods.GoodsManager;
import it.polimi.ingsw.galaxytruckerproject.network.RMI.VirtualController;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CargoBlue;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;
import static it.polimi.ingsw.galaxytruckerproject.client.ClientState.*;

public class ClientController {

    private ClientState state;
    private int turns;
    private VirtualController virtualController;
    private ArrayList<Goods> goodsList;
    private final Client client;
    private int indexDeckInHandOrPlanet;
    private CoordInputManager coordInputManager;
    private LightShipBoard lightShipBoard;

    public ClientController(Client client) {
        this.client = client;
        this.indexDeckInHandOrPlanet = 0;
        this.turns = 0;
        state = ClientState.LOBBY;
    }

    public VirtualController getVirtualController() {
        return virtualController;
    }





    public void input(String input) throws RemoteException {
        input.toLowerCase();
        input.replaceAll("\\s+"," ");
        String [] words = input.split(" ");
        if(words[0].isEmpty()){
            System.out.println("Empty input");
            return;
        }
        switch (state) {
            case LOBBY: {

            }

            case ACTION: {
                if (checkShipBoards(words))
                    return;
                switch (words[0]){
                    case "yes" -> {
                        //accept action

                    }
                    case "no" -> {
                        //deny action

                    }
                }
            }

            case PLANET_CHOICE: {
                int chose;
                if (checkShipBoards(words))
                    return;
                if (words[0].equals("no")) {
                    //notify server
                    return;
                }
                try {
                    chose = Integer.parseInt(words[0]);
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input format. Please provide integer values.");
                    return;
                }
                if (chose > 0 && chose </*num pianeti*/) {
                    state = ClientState.MANAGE_GOODS;
                    indexDeckInHandOrPlanet = chose;
                    //notify choose planet
                    return;
                }
            }

            case MANAGE_GOODS: {
                GoodsManager goodsManager = new GoodsManager(client.getMe(), goodsList);
                if (checkShipBoards(words))
                    return;
                if (!goodsManager.getReward(words).isEmpty()) {
                    //notify server changes
                }
                ;
            }

            case COORD_REQUEST:{
                if(words[0].equals("done")) {
                    coordInputManager.endCheckingFase();
                }
                else
                    coordInputManager.checkCoord(transformCoordinates(words));
            }

            case MANAGE_CARDS:{
                if (words[0].equals("previous")) {
                    //accept action(local)
                }
                if(words[0].equals("next")) {
                    //deny action (local)
                }
                if (words[0].equals("done")) {
                    //accept action(send to server that i'm no more using this deck)
                }
            }

            case S_END_DRAW_TILE_CARD: {
                if(firstTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case"done" -> {
                        state = ClientState.S_FINISHED;
                        //notify done
                        return;
                    }
                    case "draw" -> {
                        switch (words[1]) {
                            case "card" -> {
                                int chose;
                                if (words[2].isEmpty()) {
                                    System.out.println("Choose one of the decks");
                                    return;
                                }
                                try {
                                    chose = Integer.parseInt(words[2]);
                                } catch (NumberFormatException e) {
                                    System.out.println("\nInvalid input format. Please provide integer values.");
                                    return;
                                }
                                if (chose > 0 && chose < 4) {
                                    state = ClientState.MANAGE_CARDS;
                                    indexDeckInHandOrPlanet = chose;
                                    //notify drawn card
                                    ArrayList<Card> displayedCards = client.getDeck().get(indexDeckInHandOrPlanet);
                                    return;
                                }
                                return;
                            }
                            case "tile" -> {
                                state = ClientState.S_MANAGE_DRAWN_TILE;
                                //notify drawn tile
                                client.setTileInHand(/*Drawn Tiles*/);
                                return;
                            }
                        }
                    }
                }
            }

            case S_MANAGE_DRAWN_TILE: {
                if(firstTurn(words))
                    return;
                if (checkShipBoards(words))
                    return;
                switch (words[0]) {
                    case "rotate" -> {
                        Tile tile = client.getTileInHand();
                        tile.rotate();
                        client.setTileInHand(tile);
                    }
                    case "position" -> {
                        for (int i = 0; i < words.length - 1; i++) {
                            words[i] = words[i + 1];
                        }
                        Coordinates coordinates=transformCoordinates(words);
                        if(coordinates!=null){
                            client.getMe().getShipBoard().positionTile(/*tile*/,);
                            //notify positioned
                        }
                        return;
                    }
                    case "refuse" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;

                        client.getDrawnTiles().put(/*id Tile*/,/*drawn Tile*/);
                        //notify refused
                        return;
                    }
                    case "book" -> {
                        state = ClientState.S_END_DRAW_TILE_CARD;
                        //notify booked
                        return;
                    }
                }
            }

            case S_FINISHED: {
                if (checkShipBoards(words))
                    return;
                if (words[0].equals("turn") && turns < 2) {
                    turns++;
                    //notify time
                }
            }
            case WAIT_OTHER_PLAYER_ACTION:{

            }
        }
    }

    public Coordinates transformCoordinates(String[] input) {
        int CoordinatesX;
        try {
            CoordinatesX = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        if(CoordinatesX<0||CoordinatesX>4){
            System.out.println("\nInvalid input format. Please provide integer values.");
            return null;
        }
        int CoordinatesY;
        try {
            CoordinatesY = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid coord number.");
            return null;
        }
        if(CoordinatesY<0||CoordinatesY>6){
            System.out.println("\nInvalid coord number.");
            return null;
        }

        return new Coordinates(CoordinatesX, CoordinatesY);
    }


    private boolean checkShipBoards(String[] input) {
        if (input[0].equals("check")) {
            int chose;
            if (input[1].isEmpty()) {
                System.out.println("Choose one of the shipboards");
                return false;
            }
            try {
                chose = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input format. Please provide integer values.");
                return false;
            }
            if (chose == 0) {
                System.out.println(client.getMe().getShipBoard());
            }
            if (chose >= 1 && chose <= 4) {
                System.out.println(client.getPlayersList().get(indexDeckInHandOrPlanet).getShipBoard());
            }
            return true;
        }
        return false;
    }

    private boolean firstTurn(String[] input) {
        if (input[0].equals("turn") && turns==0) {
            turns++;
            //notify time
            return true;
        }
        return false;
    }
}
