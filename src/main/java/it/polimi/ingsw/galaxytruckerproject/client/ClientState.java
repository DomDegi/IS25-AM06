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
    MANAGE_CARDS,//scorre le carte che ha in mano durante la fase di costruzione
    S_MANAGE_DRAWN_TILE,//ruota, posiziona o scarta
    S_FINISHED,//costruzione della shipboard finita, attesa della verifica correttezza
    WAIT_OTHER_PLAYER_ACTION,
    START_SHIP_CREATION
}
