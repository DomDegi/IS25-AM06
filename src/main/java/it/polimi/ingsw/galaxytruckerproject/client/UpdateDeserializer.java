package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.cards.Card;
import it.polimi.ingsw.galaxytruckerproject.model.cards.CardDeck;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Utility class responsible for deserializing the game update string sent by the server
 * and applying it to the {@link ClientController} to synchronize the client-side state.
 * <p>
 * This class is not instantiable and exposes a single static method for deserialization.
 * It handles parsing the game state string and updating the game components in the client.
 * </p>
 */
public class UpdateDeserializer {

    /**
     * Parses the serialized game state string and updates the local {@link ClientController}
     * accordingly. This method:
     * <ul>
     *   <li>Updates the hourglass turn counter.</li>
     *   <li>Restores the game mode and player count.</li>
     *   <li>Reconstructs the deck of cards and turned tiles.</li>
     *   <li>Loads each player's light model and ship board.</li>
     *   <li>Sorts and inserts players into the client-side flight board.</li>
     * </ul>
     *
     * @param controller         the client controller to update with the new game state
     * @param currentGameStatus  the serialized game state string received from the server
     * @throws RuntimeException if any parsing or I/O error occurs during deserialization
     */
    public static void unpack(ClientController controller,String playerName, String currentGameStatus) {

        try (BufferedReader writer = new BufferedReader(new StringReader(currentGameStatus))) {

            controller.putInStandby();

            String[] controllerData = writer.readLine().split(" ");
            controller.setHourglassTurns(Integer.parseInt(controllerData[0]));

            String[] firstGameData = writer.readLine().split(" ");
            controller.setGameModeWithoutInitializing(GameMode.valueOf(firstGameData[0]));
            int playerCount = Integer.parseInt(firstGameData[1]);

            String[] cardData = writer.readLine().split(" ");

            List<Integer> cardIDs = Arrays.stream(cardData)
                    .map(Integer::parseInt)
                    .toList();

            Map<Integer, ArrayList<Card>> deck = new HashMap<>();

            ArrayList<Card> deckCards = new CardDeck("cards.json").deckFromIDs(new ArrayList<>(cardIDs));

            if (controller.getGameMode() == GameMode.LEVEL2) {
                for (int i = 0; i < 3; i++) {
                    int deckNumber = i + 1;
                    List<Card> subDeck = deckCards.subList(i * 3, (i + 1) * 3);
                    deck.put(deckNumber, new ArrayList<>(subDeck));
                }
            }

            controller.setDeck(deck);

            String[] turnedTileLine = writer.readLine().split(" ");
            if (!turnedTileLine[0].isEmpty()) {

                ArrayList<Integer> turnedTileIds = new ArrayList<>();
                Map<Integer, Tile> turnedTiles;
                for (String tileData : turnedTileLine) {
                    turnedTileIds.add(Integer.parseInt(tileData));
                }
                turnedTiles = new TileFactory().mapFromIDs(turnedTileIds);
                controller.setTurnedTiles(turnedTiles);
            }

            ArrayList<LightPlayer> otherPlayers = new ArrayList<>();

            for (int i = 0; i < playerCount; i++) {

                String[] playerData = writer.readLine().split(" ");
                String currentName = playerData[0];
                LightShipBoard lightShipBoard;
                if (currentName.equals(playerName)) {
                    lightShipBoard = new LightShipBoard(controller.getMe());
                    controller.getMe().loadFromData(playerData);
                } else {
                    LightPlayer newPlayer = new LightPlayer(playerData[0], PlayersColor.fromString(playerData[1]));
                    lightShipBoard = new LightShipBoard(newPlayer);
                    newPlayer.loadFromData(playerData);
                    otherPlayers.add(newPlayer);
                }

                // Data of all the tiles in the shipboard, including the booked tiles
                ArrayList<Tile> ship = new ArrayList<>();
                for (int j = 0; j < 37; j++) {
                    String tileData = writer.readLine();
                    Tile toSet;
                    if (!tileData.equals("NullTile")) {
                        toSet = TileFactory.load(tileData);
                    } else {
                        toSet = null;
                    }
                    ship.add(toSet);
                }
                lightShipBoard.loadFromTiles(ship);
            }

            otherPlayers.add(controller.getMe());
            otherPlayers.sort(Comparator.comparingInt(LightPlayer::getRank));
            otherPlayers.forEach(lightPlayer -> {
                controller.getFlightBoard().addInGamePlayer(lightPlayer);
            });

            if (controller.getMe().getPosition() != 0 && controller.getMe().getRank() != 0) {
                controller.setPositioned(true);
            }

        } catch (IOException e) {
            System.out.println("Error in unpacking currentGameStatus");
            controller.setOffline();
        }
    }
}
