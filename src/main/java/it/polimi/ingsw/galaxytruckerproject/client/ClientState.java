package it.polimi.ingsw.galaxytruckerproject.client;

public enum ClientState {
    CHOOSE_UI,
    CHOOSE_CONNECTION_TYPE,
    LOBBY,//choose game
    LOGIN,//choose name
    COLOR_CHOICE,
    ACTION,// yes or no
    COORD_REQUEST,//usare askCoordinates in modo da settare per la richiesta di coordinata corretta
    MANAGE_GOODS,
    PLANET_CHOICE,//scelta su quale pianteta atterrare
    S_END_DRAW_TILE_CARD,//può scegliere se finire la costruzione o pescale una tle/carta
    S_MANAGE_CARDS,//scorre le carte che ha in mano durante la fase di costruzione
    S_MANAGE_DRAWN_TILE,//ruota, posiziona o scarta
    S_FINISHED,//costruzione della shipboard finita, attesa della verifica correttezza
    WAIT_OTHER_PLAYER_ACTION,
    START_SHIP_CREATION,
    DRAW_CARD,
    MANAGE_CABINS,
    ROLL_DICE;

    @Override
    public String toString() {
        switch(this) {
            case CHOOSE_UI -> {return "you want to use TUI or GUI?";}
            case CHOOSE_CONNECTION_TYPE -> {return "you want to use RMI connection or Socket connection?";}
            case LOBBY -> {return"you're in the lobby";}
            case LOGIN -> {return "choose your nickname";}
            case COLOR_CHOICE -> {return "choose your color";}
            case ACTION -> {return "you can accept or deny";}
            case COORD_REQUEST -> {return "coordinates requested";}
            case MANAGE_GOODS -> {return  "going to manage goods of the planet ";}
            case PLANET_CHOICE -> {return "choose one of the planets to land";}
            case S_END_DRAW_TILE_CARD -> {return "now you can draw a tile or a card";}
            case S_MANAGE_CARDS -> {return "you're seeing the cards";}
            case S_MANAGE_DRAWN_TILE -> {return "you can rotate, position, book or refuse this tile ";}
            case S_FINISHED -> {return "you've finished the ship creation, wait to know if your ship is correct";}
            case WAIT_OTHER_PLAYER_ACTION -> {return "waiting for server";}
            case START_SHIP_CREATION -> {return "turn the hourglass to start the ship creation";}
            case DRAW_CARD -> {return "draw a card";}
            case MANAGE_CABINS -> {return "select the crew type in yours equip cabins";}
            case ROLL_DICE -> {return"roll the dice";}
        }
        return "error in client state to string";
    }
}
