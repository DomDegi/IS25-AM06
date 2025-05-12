package it.polimi.ingsw.galaxytruckerproject.view.GUI.Controllers;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class IpAndPortController {

    private ClientController clientController;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private Button defaultButton;

    private String connectionType;



    // Metodo chiamato dal ConnectionController
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        String ip = ipField.getText();
        String portText = portField.getText();

        if (ip.isEmpty() || portText.isEmpty()) {
            System.out.println("IP o porta mancanti.");
            return;
        }

        try {
            int port = Integer.parseInt(portText);
            if(connectionType.equals("Socket")) {
                try {
                    clientController.connectSocket(ip,port);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    clientController.connectRMI();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            proceedToChooseColor(ip, port);
        } catch (NumberFormatException e) {
            System.out.println("Porta non valida.");
        }
    }

    @FXML
    private void handleDefault(ActionEvent event) {
        String defaultIp = "127.0.0.1";
        int defaultPort = 1099;

        proceedToChooseColor(defaultIp, defaultPort);
    }

    private void proceedToChooseColor(String ip, int port) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ChooseColor.fxml"));
            Parent root = loader.load();


            ChooseColorController controller = loader.getController();
            controller.setConnectionType(connectionType);


            Stage stage = (Stage) ipField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*public class IpAndPortController {

    private ClientController clientController;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    //lo setto in ConnectionController
    private String connectionType;

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
        System.out.println("Connessione scelta: " + connectionType);
    }

    @FXML
    private void initialize() {
        connectButton.setOnAction(event -> {
            String ip = ipField.getText();
            String port = portField.getText();
            int portInt = Integer.parseInt(port);

            System.out.println("Connect to" + ip + ":" + port + " via " + connectionType);

            if (connectionType.equals("RMI")) {
                try {
                    clientController.connectRMI();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else if (connectionType.equals("RMI")) {
                try {
                    clientController.connectSocket(ip,portInt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}

*/