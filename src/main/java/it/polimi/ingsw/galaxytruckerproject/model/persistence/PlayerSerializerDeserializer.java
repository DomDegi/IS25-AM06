package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.*;

public class PlayerSerializerDeserializer {

    String fileName;

    public PlayerSerializerDeserializer(String fileName) {
        this.fileName = fileName;
    }

    public void save(Player player) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String line = player.toStringData();
            writer.write(line);
            writer.newLine();
            //saving player shipBoard right after player info (total should be 36 lines)
            ShipBoardSerializerDeserializer shipBoardSaver =  new ShipBoardSerializerDeserializer();
            shipBoardSaver.save(player.getShipBoard(),writer);

        } catch (IOException e) {
            System.out.println("was not able to serialize player");
            e.printStackTrace();
        }
    }

    public int load(GameController gameController, int startLine) {
        int currentLine = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            // Discards line until startLine
            while (currentLine < startLine && reader.readLine() != null) {
                currentLine++;
            }

            // Reads player line
            String playerLine = reader.readLine();

            if (playerLine == null) {
                throw new IllegalStateException("startLine is over the end of file");
            }
            currentLine++;  // now should point to first shipBoard line

            // Rebuilds player from txt file through player loader method
            Player player = new Player();
            String[] playerData = playerLine.split(" ",-1);
            player.playerLoader(playerData);


            // Loads ShipBoard
            ShipBoardSerializerDeserializer shipLoader = new ShipBoardSerializerDeserializer();
            currentLine = shipLoader.load(player, currentLine, reader);

            if (player.isDisconnected()) {
                gameController.getDisconnectedPlayers().put(player.getPlayerName(), player);
            }
            else {
                gameController.getActivePlayers().put(player.getPlayerName(), player);
            }

        } catch (IOException e) {
            System.out.println("was not able to deserialize player");
            e.printStackTrace();
        }

        // returns line where next player if present starts
        return currentLine;
    }
}

