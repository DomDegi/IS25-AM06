package it.polimi.ingsw.galaxytruckerproject.model.persistence;

import it.polimi.ingsw.galaxytruckerproject.controller.GameController;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;

import java.io.*;

public class PlayerSerializerDeserializer {

    private PlayerSerializerDeserializer() {}

    public static void save(Player player, Writer writer)  {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(player.toStringData());
            bufferedWriter.newLine();
            ShipBoardSerializerDeserializer.save(player.getShipBoard(), bufferedWriter);
        }  catch (IOException e) {
            System.out.println("Was not able to serialize player");
        }
    }

    public static void save(Player player,File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String line = player.toStringData();
            writer.write(line);
            writer.newLine();
            //saving player shipBoard right after player info (total should be 36 lines)
            ShipBoardSerializerDeserializer.save(player.getShipBoard(),writer);

        } catch (IOException e) {
            System.out.println("was not able to serialize player");
        }
    }

    public static void saveToFile(Player player, File file) {
        try (FileWriter fw = new FileWriter(file, true)) {
            save(player, fw);  // reuses the save() logic
        } catch (IOException e) {
            System.out.println("was not able to serialize player");
        }
    }

    public static void load(GameController gameController, BufferedReader reader){
        int currentLine = 0;

        try {
            //now we should be reading playerLine
            String playerLine = reader.readLine();


            // Rebuilds player from txt file through player loader method
            Player player = new Player();
            String[] playerData = playerLine.split(" ");
            player.playerLoader(playerData);


            // Loads ShipBoard
            ShipBoardSerializerDeserializer.load(player, currentLine, reader);

            if (player.isDisconnected()) {
                gameController.getDisconnectedPlayers().put(player.getPlayerName(), player);
            } else {
                gameController.getActivePlayers().put(player.getPlayerName(), player);
            }

        } catch (IOException e) {
            System.out.println("was not able to deserialize player");
        }

        // returns line where next player if present starts
    }
}

