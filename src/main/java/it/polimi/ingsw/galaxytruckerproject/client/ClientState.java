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
    /**
     * Represents the state where the user interface (UI) of the client is selected.
     *
     * This state is part of the overall system that manages the client's interaction flow,
     * specifically involving UI selection. In this phase, the client chooses which type
     * of user interface will be used for the game. The possible types of UI to be selected
     * could include graphical, console-based, or other supported interfaces.
     */
    CHOOSE_UI,
    /**
     * Represents a state in the client where the user is prompted to select
     * the type of connection they want to use to interact with the game server.
     *
     * This state is usually part of the initialization or setup process before
     * the player joins the game. The available connection types may include
     * options such as socket-based connections or RMI (Remote Method Invocation).
     */
    CHOOSE_CONNECTION_TYPE,
    /**
     * Represents the phase where the client chooses the IP address and port
     * to establish a socket-based connection.
     *
     * This phase occurs during the setup process, where the client specifies
     * connection details needed to communicate with the server.
     */
    CHOOSE_IP_AND_PORT_SOCKET,
    /**
     * The phase indicating that the client is required to choose
     * an IP address and a port specifically for RMI connections.
     *
     * This constant is part of a larger workflow for managing the
     * states in which the client operates. It is used when the client
     * must initiate communication using the RMI connection type.
     */
    CHOOSE_IP_AND_PORT_RMI,
    /**
     * Represents the state in the client where the player is in the lobby.
     *
     * During this state, players typically wait for the game to start, interact with
     * the lobby settings, or see other players who have joined.
     */
    LOBBY,//choose game
    /**
     * A specific state in the client representing the first phase of the lobby experience.
     * This phase may involve preparatory interactions, such as waiting for other players
     * to join or initial setup tasks to be performed by the player.
     *
     * It is part of the `ClientState` enumeration, controlling different client-side
     * interaction states during the game's lifecycle.
     */
    LOBBY0,
    /**
     * Represents the state when the player is in the first stage of the game lobby.
     * This state typically involves preparing or organizing settings prior to gameplay,
     * such as forming teams, waiting for other players, or reviewing game details.
     */
    LOBBY1,
    /**
     * Represents the phase where the player is logging into the game.
     *
     * In this phase, the player completes the necessary actions to authenticate
     * and enter the game environment. This phase is typically the first phase
     * encountered and is required before proceeding to other phases of gameplay.
     */
    LOGIN,
    /**
     * Represents the phase in which the player chooses a color in the game.
     *
     * This phase is typically used to allow a player to select their preferred
     * color for representation during the game. The chosen color can affect
     * the player's identity or association with specific elements like ships
     * or territories within the game.
     */
    COLOR_CHOICE,
    /**
     * Represents a specific phase in the game where the player makes a secondary color choice.
     * This phase is part of the client's state management and is used to track the player's
     * decision-making process during gameplay.
     *
     * Associated with the player's customization or preferences, this phase allows for
     * further interaction or detailed selection beyond the primary color choice.
     */
    COLOR_CHOICE0,
    /**
     * Represents a specific action or state within the game client.
     *
     * This variable indicates a key stage or behavior in the gameplay, guiding
     * the client towards executing actions or responding to designated events.
     */
    ACTION,
    /**
     * Represents a game phase where the player is prompted to interact by providing specific
     * coordinates or selections relevant to the game context.
     *
     * The type of coordinate request is determined and handled as per {@link CoordReqType}.
     * These requests could include actions like breaking tiles, choosing equipment, or managing goods.
     *
     * This phase is used within the flow of the game to direct player decisions that require input.
     */
    COORD_REQUEST,
    /**
     * Represents the phase in the game where the player manages goods.
     * This phase allows the player to handle various operations related to
     * their resources, such as organizing, trading, or using the goods they
     * have acquired during the game.
     */
    MANAGE_GOODS,
    /**
     * Represents the phase of the game where the player selects a planet.
     *
     * This variable is used to indicate the point in the game where players are required
     * to make a decision about which planet to choose, based on the current state of the game
     * and available options.
     *
     * It is part of the flow control for the game's phases, ensuring that actions and decisions
     * occur in the proper sequence.
     */
    PLANET_CHOICE,
    /**
     * Represents the state where the game phase for drawing a tile card is complete.
     * This state signifies that the player has finished the action of drawing a new tile card,
     * and the game is ready to proceed to the next phase or action depending on the game flow.
     */
    S_END_DRAW_TILE_CARD,//può scegliere se finire la costruzione o pescale una tle/carta
    /**
     * Represents the state where the player manages cards during the game.
     *
     * This state allows players to interact with game cards, such as performing actions
     * related to cards like selecting, discarding.
     */
    S_MANAGE_CARDS,
    /**
     * Represents the game state where the player manages a tile that has been drawn.
     *
     * This state occurs when a new tile is drawn, and the player needs to decide how
     * to handle it. This can include tasks such as placing the tile on the board,
     * discarding it, or other actions as defined by the game rules.
     */
    S_MANAGE_DRAWN_TILE,//ruota, posiziona o scarta
    /**
     * Represents the finished state within the client's state machine.
     *
     * This state indicates that the game has reached its conclusion for the current client.
     * It signifies the end of the game process, where no further actions or decisions
     * are required from the player. Transitioning to or from this state depends on
     * the game's flow and logic.
     */
    S_FINISHED,//costruzione della shipboard finita, attesa della verifica correttezza
    /**
     * The WAIT state indicates a phase where the client is waiting for a response or event
     * to proceed. This state is often transitional and occurs between active phases,
     * allowing the client to pause while awaiting input, a game update, or other
     * external actions.
     */
    WAIT,
    /**
     * Enum constant representing the state where the client is waiting
     * for the player's turn to draw a new tile during the game.
     *
     * This state indicates that the gameplay is paused for the current
     * player as they wait to draw the next tile. The transition from this
     * state occurs when the player is allowed to proceed with the drawing
     * action.
     */
    WAIT_TO_DRAW,
    /**
     * Represents the phase of the game where players start the creation and
     * construction of their ship. During this phase, players manage ship tiles,
     * customize their shipboards, and make design decisions to prepare for gameplay.
     *
     * This phase is integral to the game's progression and involves interactions
     * related to strategic placement and organization of ship components.
     */
    START_SHIP_CREATION,
    /**
     * Represents the phase where the player is required to draw a card from the deck.
     *
     * This phase signifies an action-oriented step in the game where a player interacts
     * directly with the card deck, potentially impacting the game's progress or outcomes.
     */
    DRAW_CARD,
    /**
     * Represents the phase where the player manages cabin-related tasks.
     *
     * This phase involves organizing or arranging cabins within their ship
     * during the game's ship management process. It may include tasks such
     * as assigning crew members to cabins or modifying cabin structures.
     */
    MANAGE_CABINS,
    /**
     * Represents the game phase where the player rolls dice.
     *
     * This phase may be used to determine random outcomes or actions
     * within the game's ruleset, such as combat results or resource generation.
     * It is part of the phases that help in managing the game's state transitions.
     */
    ROLL_DICE,
    /**
     * Represents the phase where the client is attempting to reconnect to the game.
     * This phase is used when a disconnection occurs and the client needs to
     * re-establish a connection to the server and resume the game state.
     */
    RECONNECTING;

    @Override
    public String toString() {
        switch(this) {
            case CHOOSE_UI -> {return "You want to use TUI or GUI? [tui] [gui]";}
            case CHOOSE_CONNECTION_TYPE -> {return "You want to use RMI connection or Socket connection? [rmi] [socket]";}
            case CHOOSE_IP_AND_PORT_RMI -> {return "You chose RMI: type the server information's [ip] [port] (type d to default to localhost)";}
            case CHOOSE_IP_AND_PORT_SOCKET -> {return "You chose SOCKET: type the server information's [ip] [port] (type d to default to localhost)";}
            case LOGIN -> {return "Choose your nickname [name] [done] [reset]";}
            case LOBBY -> {return"You're in the lobby [createGame] [joinGame]";}
            case LOBBY0 ->{return"Enter gameName, player number, gameMode [gameName numPlayer gameMode]";}
            case LOBBY1 -> {return "Enter gameName [gameName]";}
            case COLOR_CHOICE,COLOR_CHOICE0 -> {return "Choose your color [red] [yellow] [green] [blue]";}
            case ACTION -> {return "You can accept or deny [yes] [no]";}
            case COORD_REQUEST -> {return "Coordinates requested [coordinates] and insert [done] to complete the operation";}
            case MANAGE_GOODS -> {return  "choose the good you want or the cargo hold that you need to modify ";}
            case PLANET_CHOICE -> {return "Choose one of the planets to land (0 to not land) [planetNumber]";}
            case S_END_DRAW_TILE_CARD -> {return "Now you can draw a tile or a card [done] [draw tile|draw tile new] [draw tile b1] [draw tile b2] [draw tile turnedTileNum] [draw deck numDeck] [check playerNum] [turn] [buildship]";}
            case S_MANAGE_CARDS -> {return "You're seeing the cards [done]";}
            case S_MANAGE_DRAWN_TILE -> {return "You can rotate, position, book or refuse this tile [rotate] [put] [book] [refuse] [check playerNum] [turn]";}
            case S_FINISHED -> {return "You've finished the ship creation, wait to know if your ship is correct [flightBoardPosition] [check playerNum] [turn]";}
            case WAIT -> {return "Waiting for server";}
            case START_SHIP_CREATION -> {return "TrialFlight: Start the ship creation [start] Level2Flight: Start the ship creation by turning the hourglass [Turn|Start]";}
            case DRAW_CARD -> {return "Draw a card [draw]";}
            case WAIT_TO_DRAW -> {return "Waiting other players";}
            case MANAGE_CABINS -> {return "Select the crew type in yours equip cabins [humans] [purpleAlien] [brownAlien]";}
            case ROLL_DICE -> {return"Roll the dice [roll]";}
            case RECONNECTING ->  {return "Server disconnected: repeat the login or close the game? [login] [close]";}
        }
        return "Error in client state to string";
    }
}
