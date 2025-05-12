package it.polimi.ingsw.galaxytruckerproject.view.GUI.Controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConnectionController {
    // Riferimenti agli elementi FXML (assicurati di impostare gli fx:id in Scene Builder)
    @FXML
    private Button rmiButton;

    @FXML
    private Button socketButton;

    // Metodo chiamato automaticamente dopo il caricamento dell'FXML
    @FXML
    private void initialize() {
        // Configura le azioni dei pulsanti
        rmiButton.setOnAction(event -> handleRMIConnection());
        socketButton.setOnAction(event -> handleSocketConnection());
    }

    // Gestisce la connessione RMI
    private void handleRMIConnection() {
        System.out.println("Avvio connessione RMI...");
        // Sostituisci con la tua logica RMI
        // Esempio: NetworkService.startRMIConnection();
        loadNextScene();
    }

    // Gestisce la connessione Socket
    private void handleSocketConnection() {
        System.out.println("Avvio connessione Socket...");
        // Sostituisci con la tua logica Socket
        // Esempio: NetworkService.startSocketConnection();
        loadNextScene();
    }

    // Carica la prossima scena (es. login o lobby)
    private void loadNextScene() {
        try {
            Stage currentStage = (Stage) rmiButton.getScene().getWindow();
            // Sostituisci con il percorso del tuo prossimo FXML
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("next_scene.fxml"));
            // currentStage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}