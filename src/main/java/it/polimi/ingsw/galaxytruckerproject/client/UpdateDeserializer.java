package it.polimi.ingsw.galaxytruckerproject.client;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightShipBoard;
import it.polimi.ingsw.galaxytruckerproject.model.GameMode;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.model.player.PlayersColor;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.Tile;
import it.polimi.ingsw.galaxytruckerproject.model.tiles.TileFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class UpdateDeserializer {

    public static void unpack(ClientController controller, String currentGameStatus) {

        try (BufferedReader writer = new BufferedReader(new StringReader(currentGameStatus))) {

            controller.putInStandby();

            String[] controllerData =  writer.readLine().split(" ");
            controller.setHourglassTurns(Integer.parseInt(controllerData[0]));

            String[] firstGameData =  writer.readLine().split(" ");
            controller.setGameMode(GameMode.valueOf(firstGameData[0]));
            int playerCount = Integer.parseInt(firstGameData[1]);

            String[] turnedTileLine =  writer.readLine().split(" ");
            ArrayList<Integer>  turnedTileIds = new ArrayList<>();
            Map<Integer, Tile> turnedTiles;
            for (String tileData : turnedTileLine) {
                turnedTileIds.add(Integer.parseInt(tileData));
            }
            turnedTiles = new TileFactory().mapFromIDs(turnedTileIds);
            controller.setTurnedTiles(turnedTiles);

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
