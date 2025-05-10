package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;
import it.polimi.ingsw.galaxytruckerproject.view.GUI.GUI;
import it.polimi.ingsw.galaxytruckerproject.view.TUI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ControllerTUI implements ControllerUI {
    private final ClientController clientController;
    private DisplayableView view;

    public ControllerTUI(ClientController clientController) {
        this.clientController = clientController;
        view=this.clientController.getView();
    }

    public boolean input(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("\\s+", " ");
        String[] words = input.split(" ");
        if (words.length == 0||words[0].isEmpty()) {
            view.wrongLocalInput();
            return false;
        }

        switch (clientController.getState()) {
            case CHOOSE_UI->{
                switch(words[0]) {
                    case "gui","g"-> {
                        clientController.setView(new GUI());
                        view = this.clientController.getView();
                    }
                    case "tui","t"-> {
                        clientController.setView(new TUI());
                        view=this.clientController.getView();
                    }
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                clientController.setState(ClientState.CHOOSE_CONNECTION_TYPE);
            }

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi","r"->{
                        try {
                            clientController.connectRMI();
                        } catch (MalformedURLException | NotBoundException | RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "socket","s"-> {
                        try {
                            clientController.connectSocket();
                        } catch (IOException e) {
                            System.out.println("Error connecting to server via socket");
                        }
                    }
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                clientController.setState(ClientState.LOGIN);
            }

            case LOGIN->{
                switch (words[0]) {
                    case "done","d" -> clientController.doneNaming();
                    case "redo"-> clientController.getMe().setPlayerName("");
                    default-> clientController.getMe().setPlayerName(words[0]);
                }
            }

            case LOBBY-> {
                //CHOOSE TUI OR GUI
                switch(words[0]) {
                    case "creategame","c"-> clientController.setState(ClientState.LOBBY0);
                    case "joingame","j"-> clientController.setState(ClientState.LOBBY1);
                    default-> {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case LOBBY0 -> {
                String gameName = words[0];
                int numberOfPlayers;
                if(!(words.length > 1)){
                    view.wrongLocalInput();
                    return false;
                }
                numberOfPlayers = clientController.numerate(clientController.scroll(words,1));
                if(numberOfPlayers==-1||numberOfPlayers>4)
                    return false;
                if(!(words.length > 2)){
                    view.wrongLocalInput();
                    return false;
                }
                GameMode mode;
                switch(words[2]) {
                    case "trialmode","t"-> mode=GameMode.TRIAL;
                    case "level2mode","2"-> mode=GameMode.LEVEL2;
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                clientController.createGame(gameName,numberOfPlayers,mode);
            }

            case LOBBY1 -> {
                String gameName = words[0];
                clientController.joinGame(gameName);
            }

            case COLOR_CHOICE->{
                PlayersColor color;
                switch (words[0]){
                    case "red","r"->
                            color = PlayersColor.RED;
                    case "yellow","y"->
                            color = PlayersColor.YELLOW;
                    case "green","g"->
                            color = PlayersColor.GREEN;
                    case "blue","b"->
                            color = PlayersColor.BLUE;
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
                clientController.colorChoice(color);
            }

            case START_SHIP_CREATION -> {
                if (clientController.firstHourglassTurn(words))
                    return true;
                view.wrongLocalInput();
                return false;
            }

            case S_END_DRAW_TILE_CARD -> {
                if (clientController.secondHourglassTurn(words))
                    return true;
                if (clientController.checkShipBoards(words))
                    return true;
                switch (words[0]) {
                    case"done","d" -> clientController.doneShipboard();
                    case "draw" -> {
                        if(!(words.length > 1)){
                            view.wrongLocalInput();
                            return false;
                        }
                        switch (words[1]) {
                            case "deck" -> {
                                int chose ;
                                if(!(words.length > 2)){
                                    view.wrongLocalInput();
                                    return false;
                                }
                                chose = clientController.numerate(clientController.scroll(words, 2));
                                if (chose == -1)
                                    return false;
                                clientController.drawDeck(chose);
                            }
                            case "tile" -> {
                                if(!(words.length > 2)){
                                    clientController.drawTile();
                                    return true;
                                }
                                switch (words[2]){
                                    case "new" -> clientController.drawTile();
                                    case "b1" -> clientController.drawBooked(0);
                                    case "b2" -> clientController.drawBooked(1);
                                    default-> {
                                        int chose;
                                        chose = clientController.numerate(clientController.scroll(words,2));
                                        if (chose==-1)
                                            return false;
                                        clientController.drawDrawn(chose);
                                    }
                                }
                            }
                        }
                    }
                    default -> {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case S_MANAGE_CARDS-> {
                if(words[0].equals("done")) {
                    clientController.stopLookingAtCards();
                }
            }

            case S_MANAGE_DRAWN_TILE-> {
                if (clientController.secondHourglassTurn(words))
                    return true;
                if (clientController.checkShipBoards(words))
                    return true;
                switch (words[0]) {
                    case "rotate" -> clientController.rotateTile();
                    case "put" -> {
                        Coordinates coordinates;
                        if(!(words.length > 2)){
                            view.wrongLocalInput();
                            return false;
                        }
                        coordinates = clientController.transformCoordinates(clientController.scroll(words,1));
                        clientController.positionTile(coordinates);
                    }
                    case "refuse" -> clientController.refuseTile();
                    case "book" -> clientController.bookTile();
                }
            }

            case S_FINISHED-> {
                if (clientController.checkShipBoards(words))
                    return true;
                if (clientController.thirdHourglassTurn(words))
                    return true;
                int chose;
                chose = clientController.numerate(words);
                if (chose == -1)
                    return false;
                clientController.positionOnFlightBoard(chose);
            }

            case ROLL_DICE-> clientController.rollDice();

            case DRAW_CARD -> clientController.drawCard();

            case ACTION-> {
                if (clientController.checkShipBoards(words))
                    return true;
                if (clientController.land(words))
                    return true;
                switch (words[0]){
                    case "yes" -> clientController.sayYes();
                    case "no" -> clientController.sayNo();
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case PLANET_CHOICE-> {
                if (clientController.checkShipBoards(words))
                    return true;
                if (clientController.land(words))
                    return true;
                if (words[0].equals("no"))
                    words[0]="0";
                int chose;
                chose = clientController.numerate(words);
                if(chose==-1)
                    return false;
                clientController.choosePlanet(chose);
            }

            case MANAGE_GOODS-> {
                if (clientController.checkShipBoards(words))
                    return true;
                if (clientController.land(words))
                    return true;
                if(words[0].equals("done")) {
                    clientController.doneGoods();
                }
                if((words.length == 1)){
                    int chose;
                    chose = clientController.numerate(words);
                    if(chose==-1)
                        return false;
                    if(!clientController.chooseGoods(chose))
                        view.wrongLocalInput();
                }else if(words.length == 2){
                    Coordinates coords=clientController.transformCoordinates(words);
                    if(coords==null)
                        return false;
                    if(!clientController.chooseCargo(coords))
                        view.wrongLocalInput();
                }else {
                    view.wrongLocalInput();
                    return false;
                }
            }

            case MANAGE_CABINS -> {
                CrewType type;
                switch (words[0]) {
                    case "humans" -> type = CrewType.HUMAN;
                    case "brownalien" -> type = CrewType.BROWN;
                    case "purplealien" -> type = CrewType.PURPLE;
                    default -> {
                        view.wrongLocalInput();
                        clientController.setUpCabins();
                        return false;
                    }
                }
                clientController.manageCabins(type);
            }

            case COORD_REQUEST -> {
                if (clientController.checkShipBoards(words))
                    return true;
                if (clientController.getPhase() == GamePhases.CARDS) {
                    if (clientController.land(words))
                        return true;
                }
                if (words[0].equals("done")) {
                    return clientController.doneCoord();
                }
                else {
                    //gestire il -1 in base alla carta
                    Coordinates coords;
                    coords = clientController.transformCoordinates(words);
                    if(coords==null)
                        return false;
                    if(!clientController.checkCoord(coords)) {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case WAIT ->{
                switch(clientController.getPhase()){
                    case LOGIN -> {
                        try {
                            view.showGenericMessage("Wrong input in wait");
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case SHIPBOARD -> {
                        if (clientController.checkShipBoards(words))
                            return true;
                    }
                    case CARDS -> {
                        if (clientController.checkShipBoards(words))
                            return true;
                        if(clientController.land(words))
                            return true;
                    }
                }
                return false;
            }
        }
        return true;
    }
}
