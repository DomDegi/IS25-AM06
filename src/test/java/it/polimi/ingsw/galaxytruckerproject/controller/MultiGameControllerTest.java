package it.polimi.ingsw.galaxytruckerproject.controller;

import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.network.MockVirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultiGameControllerTest {

    private MultiGameController controller;
    private MockVirtualView view1;
    private Controller personalController1;
    private MockVirtualView view2;
    private Controller personalController2;

    @BeforeEach
    void setUp() {
        controller = new MultiGameController();
        view1 = new MockVirtualView();
        personalController1 = new Controller(controller);
        personalController1.setView(view1);
        view2 = new MockVirtualView();
        personalController2 = new Controller(controller);
        personalController2.setView(view2);
    }

    @Test
    void loginWithUniqueNicknameShouldSucceed() {
        boolean success = controller.login("Alice", view1, personalController1);
        assertTrue(success, "Login should succeed with a unique nickname");
    }

    @Test
    void loginWithDuplicateNicknameShouldFail() {
        controller.login("Bob", view1, personalController1);

        // Second login with same nickname
        MockVirtualView view2 = new MockVirtualView();
        Controller controller2 = new Controller(controller);
        boolean result = controller.login("Bob", view2, controller2);

        assertFalse(result, "Login should fail if nickname is already taken");
    }

    @Test
    void testCreateGameSuccessfully() {
        personalController1.login("Carol");
        controller.createGame("Carol", "TestGame", 2, personalController1, GameMode.TRIAL);
        assertTrue(controller.allConnectedPlayers().containsKey("Carol"), "Player should be in a game after creation");
        personalController1.chooseColor(PlayersColor.RED);
        assertTrue(controller.isAlreadyInAGame("Carol"), "Player should be in a game after creation");
    }

    @Test
    void testJoinNonExistingGameShowsError() {
        controller.login("Dave", view1, personalController1);
        controller.joinGame("Dave", "NonExistingGame", personalController1);
    }

    @Test
    void SecondPlayerJoinsGameSuccessfully() {
        testCreateGameSuccessfully();
        personalController2.login("John");
        personalController2.joinGame("TestGame");
        personalController2.chooseColor(PlayersColor.YELLOW);
        assertTrue(controller.isAlreadyInAGame("John"), "Player should be in a game after join");
    }

    @Test
    void leavesTheGame() {
        SecondPlayerJoinsGameSuccessfully();
        personalController1.leaveGame();
        assertFalse(controller.isAlreadyInAGame("Carol"), "Player shouldn't be in the game after leaving");
    }

    @Test
    void triesToCreateAGameWithTheSameName() {
        testCreateGameSuccessfully();
        personalController2.login("John");
        personalController2.createGame("TestGame",2,GameMode.TRIAL);
        assertNull(controller.gameFromNickname("John"));
    }

    @Test
    void triesToCreateAGameWithWrongPlayerNumber() {
        testCreateGameSuccessfully();
        personalController2.login("John");
        personalController2.createGame("Game",5,GameMode.TRIAL);
        assertNull(controller.gameFromNickname("John"));
    }

    @Test
    void loginWithNullControllerOrViewFails() {
        boolean result1 = controller.login("Eve", null, personalController1);
        boolean result2 = controller.login("Eve", view1, null);
        boolean result3 = controller.login("Eve", null, null);

        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    @Test
    void notifyNewGameUpdatesViews() {
        controller.login("Luca", view1, personalController1);
        controller.notifyNewGame("Luca", view1);
    }

    @Test
    void testPingPongRunsWithoutError() throws InterruptedException {
        testCreateGameSuccessfully();
        GameController game = controller.gameFromNickname("Carol");

        // Avvia il ping pong in un thread separato
        Thread pingThread = new Thread(() -> controller.playPingPong());
        pingThread.start();

        // Attendi al massimo 15 secondi che il giocatore venga disconnesso
        int maxWaitMillis = 15000;
        int waited = 0;
        while (!game.getActivePlayers().isEmpty() && waited < maxWaitMillis) {
            Thread.sleep(100);
            waited += 100;
        }

        assertTrue(game.getActivePlayers().isEmpty(), "Players should be disconnected by PingPong");
    }

    @Test
    void testReconnectsToGame() throws InterruptedException {
        SecondPlayerJoinsGameSuccessfully();
        GameController game = controller.gameFromNickname("Carol");
        game.prepareForDisconnection("Carol");

        Thread pingThread = new Thread(() -> controller.playPingPong());
        pingThread.start();

        // Attendi al massimo 15 secondi che il giocatore venga disconnesso
        int maxWaitMillis = 15000;
        int waited = 0;
        while (!game.getActivePlayers().isEmpty() && waited < maxWaitMillis) {
            Thread.sleep(100);
            waited += 100;
            game.pong("John");
        }

        assertTrue(game.getActivePlayers().size() == 1, "Player Carol should be disconnected by PingPong");

        personalController1.login("Carol");
        assertEquals(2, game.getActivePlayers().size());
    }

}
