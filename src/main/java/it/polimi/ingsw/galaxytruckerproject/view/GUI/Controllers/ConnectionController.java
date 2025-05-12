package it.polimi.ingsw.galaxytruckerproject.view.GUI.Controllers;


import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;




public class ConnectionController {

    private ClientController clientController;

    private String connectionType;

    //per la gang: vanno inseriti anche nel corrispondente fxml sotto la denominazione fx: id = "nome variabile"
    @FXML
    private Button rmiButton;

    @FXML
    private Button socketButton;


    @FXML
    private void handleRMIClick(ActionEvent event) {
            connectionType = "RMI";
            goToIpAndPortScene();
        }


    @FXML
    private void handleSocketClick(ActionEvent event) {
        connectionType = "Socket";
        goToIpAndPortScene();
    }


    @FXML
    private void goToIpAndPortScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/GUI/IpAndPort.fxml"));
            Parent root = loader.load();

            // Passaggio del tipo di connessione al controller successivo
            IpAndPortController controller = loader.getController();
            controller.setConnectionType(connectionType);

            //copre anche il caso in cui viene cliccato socketButton perché tanto i due pulsanti si trovano
            //nella stessa scena
            Stage stage = (Stage) rmiButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }














    public void setClientController(ClientController clientController) {
        this.clientController = clientController;

    }
}