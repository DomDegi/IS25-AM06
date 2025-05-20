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
import java.util.stream.Collectors;

public class UpdateDeserializer {

    public static void unpack(ClientController controller, String currentGameStatus) {

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

            for (int i = 0; i < 3; i++) {
                int deckNumber = i + 1;
                List<Card> subDeck = deckCards.subList(i * 3, (i + 1) * 3);
                deck.put(deckNumber, new ArrayList<>(subDeck));
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

            String myName = controller.getName();
            ArrayList<LightPlayer> otherPlayers = new ArrayList<>();

            for (int i = 0; i < playerCount; i++) {

                String[] playerData =  writer.readLine().split(" ");
                String currentName =  playerData[0];
                LightShipBoard lightShipBoard;
                if (currentName.equals(myName)) {
                    lightShipBoard = new LightShipBoard(controller.getMe());
                    controller.getMe().loadFromData(playerData);
                }
                else {
                    LightPlayer newPlayer = new LightPlayer(playerData[0], PlayersColor.fromString(playerData[1]));
                    lightShipBoard = new LightShipBoard(newPlayer);
                    newPlayer.loadFromData(playerData);
                    otherPlayers.add(newPlayer);
                }
                // Data of all the tiles in shipboard including the booked tiles
                ArrayList<Tile> ship = new ArrayList<>();
                for (int j = 0; j < 37; j++) {
                    String tileData =  writer.readLine();
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

        } catch (IOException e) {
            System.out.println("Error in unpacking currentGameStatus");
            throw new RuntimeException(e);
        }
    }
}
