package it.polimi.ingsw.galaxytruckerproject.client;

import java.io.Serializable;

public enum ClientState implements Serializable {
    CHOOSE_UI,
    CHOOSE_CONNECTION_TYPE,
    CHOOSE_IP_AND_PORT_SOCKET,
    CHOOSE_IP_AND_PORT_RMI,
    LOBBY,//choose game
    LOBBY0,
    LOBBY1,
    LOGIN,//choose name
    COLOR_CHOICE,
    ACTION,// yes or no
    COORD_REQUEST,//usare askCoordinates in modo da settare per la richiesta di coordinata corretta
    MANAGE_GOODS,
    PLANET_CHOICE,//scelta su quale pianeta atterrare
    S_END_DRAW_TILE_CARD,//può scegliere se finire la costruzione o pescale una tle/carta
    S_MANAGE_CARDS,//scorre le carte che ha in mano durante la fase di costruzione
    S_MANAGE_DRAWN_TILE,//ruota, posiziona o scarta
    S_FINISHED,//costruzione della shipboard finita, attesa della verifica correttezza
    WAIT,
    START_SHIP_CREATION,
    DRAW_CARD,
    MANAGE_CABINS,
    ROLL_DICE;

    @Override
    public String toString() {
        switch(this) {
            case CHOOSE_UI -> {return "You want to use TUI or GUI? [tui] [gui]";}
            case CHOOSE_CONNECTION_TYPE -> {return "You want to use RMI connection or Socket connection? [rmi] [socket] (choice not implemented)";}
            case CHOOSE_IP_AND_PORT_RMI -> {return "You chose RMI: type the server information's [ip] [port]";}
            case CHOOSE_IP_AND_PORT_SOCKET -> {return "You chose SOCKET: type the server information's [ip] [port] (type d to default to localhost)";}
            case LOGIN -> {return "Choose your nickname [name] [done] [reset]";}
            case LOBBY -> {return"You're in the lobby [createGame] [joinGame]";}
            case LOBBY0 ->{return"Enter gameName, player number and gameMode [gameName numPlayer gameMode]";}
            case LOBBY1 -> {return "Enter gameName [gameName]";}
            case COLOR_CHOICE -> {return "Choose your color [red] [yellow] [green] [blue]";}
            case ACTION -> {return "You can accept or deny [yes] [no]";}
            case COORD_REQUEST -> {return "Coordinates requested [coordinates]";}
            case MANAGE_GOODS -> {return  "Going to manage goods of the planet";}
            case PLANET_CHOICE -> {return "Choose one of the planets to land [planetNumber]";}
            case S_END_DRAW_TILE_CARD -> {return "Now you can draw a tile or a card [done] [draw tile|draw tile new] [draw tile b1] [draw tile b2] [draw tile turnedTileNum] [draw deck numDeck] [check playerNum] [turn]";}
            case S_MANAGE_CARDS -> {return "You're seeing the cards [done]";}
            case S_MANAGE_DRAWN_TILE -> {return "You can rotate, position, book or refuse this tile [rotate] [put] [book] [refuse] [check playerNum] [turn]";}
            case S_FINISHED -> {return "You've finished the ship creation, wait to know if your ship is correct [flightBoardPosition] [check playerNum] [turn]";}
            case WAIT -> {return "Waiting for server";}
            case START_SHIP_CREATION -> {return "Turn the hourglass to start the ship creation [start|turn]";}
            case DRAW_CARD -> {return "Draw a card [draw]";}
            case MANAGE_CABINS -> {return "Select the crew type in yours equip cabins [humans] [purpleAlien] [brownAlien]";}
            case ROLL_DICE -> {return"Roll the dice [roll]";}
        }
        return "Error in client state to string";
    }
}
