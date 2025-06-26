package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.lightmodel.LightPlayer;
import it.polimi.ingsw.galaxytruckerproject.model.player.Player;
import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Comparator;

public class EndController extends GUIControllers{
    @FXML
    public HBox columns;

    @FXML
    public void initialize() {
        ArrayList<LightPlayer> coolestPlayers = new ArrayList<>();
        ArrayList<LightPlayer> players = GUI.getController().getFlightBoard().getInGamePlayers();
        if (players.isEmpty()) {
            // Gestione del caso in cui non ci sono giocatori
            return;
        }
        coolestPlayers.add(players.getFirst());
        for(LightPlayer p : players){
            if (p.getShipBoard().countExposedConnectors() < coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
                coolestPlayers.clear();
                coolestPlayers.add(p);
            }
            if (p.getShipBoard().countExposedConnectors() == coolestPlayers.getFirst().getShipBoard().countExposedConnectors()){
                coolestPlayers.add(p);
            }
        }
        for(LightPlayer player: players){
            ListView<String> listView = new ListView<>();
            listView.setPrefWidth(500);
            ArrayList<String> credits = new ArrayList<>();
            credits.add(player.getPlayerName());
            credits.add("");
            credits.add("Rank:\t\t"+player.getRank());
            credits.add("Cargo Values:\t"+player.getShipBoard().convertGoodsToCredit());
            credits.add("Exposed Connectors:\t"+player.getShipBoard().countExposedConnectors());
            if(coolestPlayers.contains(player))
                credits.add("\t\tCOOLEST");
            credits.add("Penalties:\t"+player.getShipBoard().getPenalty());
            credits.add("");
            credits.add("Final Score: "+GUI.getScores().get(player.getPlayerName()));
            int max=0;
            ArrayList<Integer> scores = new ArrayList<>(GUI.getScores().values());
            if (!scores.isEmpty()) {
                scores.sort(Comparator.naturalOrder());
                max=scores.get(scores.size() - 1);
                if (GUI.getScores().get(player.getPlayerName()) == max)
                    credits.add("\t\tWINNER!");
            }
            ObservableList<String> score = FXCollections.observableArrayList(credits);
            listView.setItems(score);
            columns.getChildren().add(listView);
        }
    }
}
