package it.polimi.ingsw.galaxytruckerproject.view.GUI.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Button confirmButton;

    // Metodo chiamato automaticamente dopo il caricamento dell'FXML
    @FXML
    private void initialize() {
        // Configura l'azione del pulsante "Confirm"
        confirmButton.setOnAction(event -> handleConfirm());

        // Opzionale: Permetti di confermare anche premendo "Invio" nella TextField
        nicknameTextField.setOnAction(event -> handleConfirm());
    }

    // Gestisce la conferma del nickname
    private void handleConfirm() {
        String nickname = nicknameTextField.getText().trim();

        if (!nickname.isEmpty()) {
            System.out.println("Nickname confermato: " + nickname);  // Debug
            // Qui puoi:
            // 1. Passare il nickname al server
            // 2. Chiudere la finestra di login
            // 3. Caricare la prossima scena (es. lista dei game)

            // Esempio: chiudi la finestra corrente
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Inserisci un nickname valido!");  // Aggiungi un Label per l'errore se necessario
        }
    }

    // Metodo per ottenere il nickname (utile per passarlo ad altri controller)
    public String getNickname() {
        return nicknameTextField.getText().trim();
    }
}
