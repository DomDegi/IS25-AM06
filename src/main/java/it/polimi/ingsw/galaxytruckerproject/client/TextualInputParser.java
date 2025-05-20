package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Coordinates;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.CrewType;
import it.polimi.ingsw.galaxytruckerproject.view.DisplayableView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;


public class TextualInputParser {
    private final ClientController clientController;
    private DisplayableView view;
    private String gameName;
    private int numberOfPlayers;
    private GameMode mode;


    public TextualInputParser(ClientController clientController) {
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

            case CHOOSE_CONNECTION_TYPE->{
                switch(words[0]) {
                    case "rmi","r"->{
                        clientController.setState(ClientState.CHOOSE_IP_AND_PORT_RMI);
                    }
                    case "socket","s"-> {
                        clientController.setState(ClientState.CHOOSE_IP_AND_PORT_SOCKET);
                    }
                    default->{
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }

            case CHOOSE_IP_AND_PORT_RMI -> {
                try {
                    clientController.connectRMI();
                } catch (MalformedURLException | NotBoundException | RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            case CHOOSE_IP_AND_PORT_SOCKET -> {
                try {
                    if (Objects.equals(words[0], "d")) {
                        clientController.connectSocket("localhost",12345);
                    }
                    else {
                        if(words.length==2)
                            clientController.connectSocket(words[0], Integer.parseInt(words[1]));
                        else {
                            view.wrongLocalInput();
                            return false;
                        }
                    }
                } catch (IOException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
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
                gameName = words[0];
                if(!(words.length > 1)){
                    view.wrongLocalInput();
                    clientController.rollBackState();
                    return false;
                }
                try {
                    numberOfPlayers = Integer.parseInt(words[1]);
                } catch (NumberFormatException e) {
                    view.wrongLocalInput();
                    clientController.rollBackState();
                    return false;
                }
                if(numberOfPlayers==-1||numberOfPlayers>4) {
                    view.wrongLocalInput();
                    clientController.rollBackState();
                    return false;
                }
                if(!(words.length > 2)){
                    view.wrongLocalInput();
                    clientController.rollBackState();
                    return false;
                }
                switch(words[2]) {
                    case "trialmode","t"-> mode=GameMode.TRIAL;
                    case "level2mode","2"-> mode=GameMode.LEVEL2;
                    default->{
                        view.wrongLocalInput();
                        clientController.rollBackState();
                        return false;
                    }
                }
                clientController.setState(ClientState.COLOR_CHOICE0);
                return true;
            }

            case COLOR_CHOICE0->{
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
                clientController.createGame(gameName,numberOfPlayers,mode);
                clientController.colorChoice(color);
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
                if (((clientController.getGameMode() == GameMode.LEVEL2 && words[0].equals("turn")) || words[0].equals("start")) && clientController.firstHourglassTurn()) {
                    return true;
                }else {
                    view.wrongLocalInput();
                    return false;
                }
            }

            case S_END_DRAW_TILE_CARD -> {
                if (words[0].equals("turn") && clientController.secondHourglassTurn())
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
                            default -> {
                                view.wrongLocalInput();
                                return false;
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
                if (words[0].equals("turn") &&clientController.secondHourglassTurn())
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
                if (words[0].equals("turn") &&clientController.thirdHourglassTurn())
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
                    case "humans", "h" -> type = CrewType.HUMAN;
                    case "brownalien", "b" -> type = CrewType.BROWN;
                    case "purplealien", "p" -> type = CrewType.PURPLE;
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
                } else {
                    Coordinates coords;
                    coords = clientController.transformCoordinates(words);
                    if (coords == null)
                        return false;
                    if (!clientController.checkCoord(coords)) {
                        view.wrongLocalInput();
                        return false;
                    }
                }
            }
            case WAIT ->{
                switch(clientController.getPhase()){
                    case LOGIN -> {
                        view.showGenericMessage("Wrong input in wait");
                    }
                    case SHIPBOARD -> {
                        if (clientController.checkShipBoards(words))
                            return true;
                        if (clientController.getPreviousState()==ClientState.S_FINISHED)
                            if(words[0].equals("turn") &&clientController.thirdHourglassTurn())
                                return true;
                        view.showGenericMessage("Wrong input in wait");
                    }
                    case CARDS -> {
                        if (clientController.checkShipBoards(words))
                            return true;
                        if(clientController.land(words))
                            return true;
                        view.showGenericMessage("Wrong input in wait");
                    }
                }
                return false;
            }
        }
        return true;
    }
}
