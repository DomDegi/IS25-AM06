package it.polimi.ingsw.galaxytruckerproject.view;

import it.polimi.ingsw.galaxytruckerproject.client.ClientController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;



import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.function.Consumer;

import static javafx.application.Application.launch;

public class MainGuiTrying extends Application implements EventHandler<ActionEvent> {
    ClientController clientController;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Scene scene, scene1, scene2, scene3, getNameScene;
    VBox vbox1;

    final String[] selectedColorHolder = new String[1];


    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) {
        clientController = new ClientController();

        // Scena iniziale: scelta connessione
        Label label = new Label("Choose connection");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        button1 = new Button("RMI");
        button2 = new Button("Socket");
        button1.setPrefSize(120, 50);
        button2.setPrefSize(120, 50);

        VBox connectionLayout = new VBox(20);
        connectionLayout.setAlignment(Pos.CENTER);
        HBox buttonsLayout = new HBox(30);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(button1, button2);
        connectionLayout.getChildren().addAll(label, buttonsLayout);

        Scene scene = new Scene(connectionLayout);

        // RMI Layout
        VBox rmiLayout = new VBox(20);
        rmiLayout.setAlignment(Pos.CENTER);
        Label label1 = new Label("RMI Successfully connected");
        rmiLayout.getChildren().add(label1);

        // Socket Layout
        VBox socketLayout = new VBox(20);
        socketLayout.setAlignment(Pos.CENTER);
        Label label2 = new Label("Socket successfully connected");
        socketLayout.getChildren().add(label2);

        // Layout per inserimento nickname
        VBox nicknameLayout = new VBox(20);
        nicknameLayout.setAlignment(Pos.CENTER);
        Label nicknameLabel = new Label("Enter your nickname");
        nicknameLabel.setStyle("-fx-font-size: 28px;");
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter here");
        nicknameField.setMaxWidth(300);
        Button confirmNickname = new Button("Confirm");
        confirmNickname.setDefaultButton(true);
        confirmNickname.setVisible(false);
        nicknameField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmNickname.setVisible(!newVal.trim().isEmpty());
        });
        nicknameLayout.getChildren().addAll(nicknameLabel, nicknameField, confirmNickname);

        // Gestione pulsanti RMI/Socket
        button1.setOnAction(e -> {
            try {
                clientController.connectRMI();
                scene.setRoot(rmiLayout);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            startAutoTransition(primaryStage, scene, nicknameLayout, 2);
        });

        button2.setOnAction(e -> {
            // clientController.connectSocket(); // da implementare
            scene.setRoot(socketLayout);
            System.out.println("Socket selected (simulazione)");
            startAutoTransition(primaryStage, scene, nicknameLayout, 2);
        });

        // Conferma nickname e passa a createJoinGame
        confirmNickname.setOnAction(event -> {
            String nickname = nicknameField.getText().trim();
            if (!nickname.isEmpty()) {
                VBox createJoinGameLayout = createJoinGame(nickname, scene);
                scene.setRoot(createJoinGameLayout);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }











    public void handle(ActionEvent event) {
        if(event.getSource() == button1) {
            System.out.println("Button clicked");
        }
    }

    public VBox chooseColor(Consumer<String> colorSelected) {
        VBox colorLayout = new VBox(20);
        colorLayout.setAlignment(Pos.CENTER);

        Label label = new Label("Choose your color");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox imagesBox = new HBox(20);
        imagesBox.setAlignment(Pos.CENTER);

        String[] colors = {"red", "blue", "green", "yellow"};

        for (String color : colors) {
            Image image = new Image(getClass().getResourceAsStream("/images/" + color + "_starting_cabin.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            imageView.setOnMouseClicked(e -> {
                System.out.println("Colore selezionato: " + color);
                colorSelected.accept(color);
            });

            imagesBox.getChildren().add(imageView);
        }

        colorLayout.getChildren().addAll(label, imagesBox);
        return colorLayout;
    }

    public VBox createJoinGame(String nickname, Scene scene) {
        VBox gameLayout = new VBox(20);
        gameLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, " + nickname + "!");
        welcomeLabel.setStyle("-fx-font-size: 28px;");

        Label createLabel = new Label("Create a New Game or...");
        Label joinLabel = new Label("...join an existing Game");

        TextField gameNameField = new TextField();
        gameNameField.setPromptText("Enter your new Game Name here");
        gameNameField.setMaxWidth(300);

        Button confirmGameName = new Button("Confirm");
        confirmGameName.setDefaultButton(true);
        confirmGameName.setVisible(false);

        gameNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmGameName.setVisible(!newVal.trim().isEmpty());
        });

        // Pulsanti esempio per join
        Button joinGame1 = new Button("Joinable Game 1");
        Button joinGame2 = new Button("Joinable Game 2");
        joinGame1.setPrefSize(120, 50);
        joinGame2.setPrefSize(120, 50);

        // Bottone per andare alla selezione colore
        Button nextButton = new Button("Next");
        nextButton.setVisible(false);

        confirmGameName.setOnAction(e -> {
            System.out.println("Game name: " + gameNameField.getText());
            nextButton.setVisible(true);
        });

        nextButton.setOnAction(e -> {
            VBox colorLayout = chooseColor(selectedColor -> {
                System.out.println("Colore scelto per " + nickname + ": " + selectedColor);
                // TODO: proseguire con lo stato del gioco
            });
            scene.setRoot(colorLayout);
        });

        gameLayout.getChildren().addAll(
                welcomeLabel,
                createLabel, gameNameField, confirmGameName,
                joinLabel, joinGame1, joinGame2,
                nextButton
        );

        return gameLayout;
    }

    // Metodo per gestire la transizione automatica
    public void startAutoTransition(Stage stage, Scene scene, Parent nextRoot, int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> scene.setRoot(nextRoot));
        pause.play();
    }

}
