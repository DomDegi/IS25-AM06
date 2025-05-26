package it.polimi.ingsw.galaxytruckerproject.client;

import java.io.Serializable;

/**
 * Enum representing the different client states during the game.
 * <p>
 * Each state defines a particular phase or action in the game, determining
 * what the player is currently doing (e.g., logging in, choosing connection type,
 * selecting a planet to land on, or managing goods).
 * </p>
 */
public enum ClientState implements Serializable {
    CHOOSE_UI,                         // Choosing between TUI (Text User Interface) or GUI (Graphical User Interface)
    CHOOSE_CONNECTION_TYPE,            // Choosing between RMI or Socket connection
    CHOOSE_IP_AND_PORT_SOCKET,         // Choosing the IP and port for Socket connection
    CHOOSE_IP_AND_PORT_RMI,            // Choosing the IP and port for RMI connection
    LOBBY,                             // Lobby phase where the player can choose or create a game
    LOBBY0,                            // Phase where the player enters game name, player count, and game mode
    LOBBY1,                            // Phase where the player enters the game name to join
    LOGIN,                             // Login phase where the player chooses their nickname
    COLOR_CHOICE,                      // Phase where the player selects their color
    COLOR_CHOICE0,                     // Another phase of color selection (alternate phase)
    ACTION,                            // Phase where the player can accept or deny an action
    COORD_REQUEST,                     // Phase where the player is requested to enter coordinates
    MANAGE_GOODS,                      // Phase where the player manages their goods or cargo hold
    PLANET_CHOICE,                     // Phase where the player chooses a planet to land on
    S_END_DRAW_TILE_CARD,              // Phase where the player can choose to finish construction or draw a tile/card
    S_MANAGE_CARDS,                    // Phase where the player manages the cards in hand during construction
    S_MANAGE_DRAWN_TILE,               // Phase where the player rotates, positions, or discards tiles
    S_FINISHED,                        // Phase where the ship construction is finished, waiting for correctness check
    WAIT,                              // Waiting for the server's response
    START_SHIP_CREATION,               // Start of ship creation in the trial or level 2 flight mode
    DRAW_CARD,                         // Phase where the player draws a card
    MANAGE_CABINS,                     // Phase where the player selects crew for their cabins
    ROLL_DICE,                         // Phase where the player rolls the dice
    RECONNECTING;                      // Reconnection phase after a server disconnection

    @Override
    public String toString() {
        switch (this) {
            case CHOOSE_UI -> { return "You want to use TUI or GUI? [tui] [gui]"; }
            case CHOOSE_CONNECTION_TYPE -> { return "You want to use RMI connection or Socket connection? [rmi] [socket]"; }
            case CHOOSE_IP_AND_PORT_RMI -> { return "You chose RMI: type the server information's [ip] [port] (type d to default to localhost)"; }
            case CHOOSE_IP_AND_PORT_SOCKET -> { return "You chose SOCKET: type the server information's [ip] [port] (type d to default to localhost)"; }
            case LOGIN -> { return "Choose your nickname [name] [done] [reset]"; }
            case LOBBY -> { return "You're in the lobby [createGame] [joinGame]"; }
            case LOBBY0 -> { return "Enter gameName, player number, gameMode [gameName numPlayer gameMode]"; }
            case LOBBY1 -> { return "Enter gameName [gameName]"; }
            case COLOR_CHOICE, COLOR_CHOICE0 -> { return "Choose your color [red] [yellow] [green] [blue]"; }
            case ACTION -> { return "You can accept or deny [yes] [no]"; }
            case COORD_REQUEST -> { return "Coordinates requested [coordinates] and insert [done] to complete the operation"; }
            case MANAGE_GOODS -> { return "Choose the good you want or the cargo hold that you need to modify"; }
            case PLANET_CHOICE -> { return "Choose one of the planets to land (0 to not land) [planetNumber]"; }
            case S_END_DRAW_TILE_CARD -> { return "Now you can draw a tile or a card [done] [draw tile|draw tile new] [draw tile b1] [draw tile b2] [draw tile turnedTileNum] [draw deck numDeck] [check playerNum] [turn]"; }
            case S_MANAGE_CARDS -> { return "You're seeing the cards [done]"; }
            case S_MANAGE_DRAWN_TILE -> { return "You can rotate, position, book, or refuse this tile [rotate] [put] [book] [refuse] [check playerNum] [turn]"; }
            case S_FINISHED -> { return "You've finished the ship creation, wait to know if your ship is correct [flightBoardPosition] [check playerNum] [turn]"; }
            case WAIT -> { return "Waiting for server"; }
            case START_SHIP_CREATION -> { return "TrialFlight: Start the ship creation [start] Level2Flight: Start the ship creation by turning the hourglass [Turn|Start]"; }
            case DRAW_CARD -> { return "Draw a card [draw]"; }
            case MANAGE_CABINS -> { return "Select the crew type in your equipped cabins [humans] [purpleAlien] [brownAlien]"; }
            case ROLL_DICE -> { return "Roll the dice [roll]"; }
            case RECONNECTING -> { return "Server disconnected: repeat the login or close the game? [login] [close]"; }
        }
        return "Error in client state to string";
    }
}
