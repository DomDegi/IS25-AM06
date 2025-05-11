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

public class MainGUI extends Application implements EventHandler<ActionEvent> {
    ClientController clientController;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Scene scene, scene1, scene2, scene3, getNameScene;
    VBox vbox1;

    final String[] selectedColorHolder = new String[1];


    public static void main(String[] args) {
        launch(args);
    }

    /*public void start(Stage primaryStage) {

        Label label = new Label("Choose connection");

        //BUTTON RMI
        button1 = new Button();
        button1.setText("RMI");

        //BUTTON SOCKET
        button2 = new Button();
        button2.setText("Socket");

        //BUTTON TO GO BACK TO THE INITIAL SCENE
        button3 = new Button();
        button3.setText("return");

        HBox layout = new HBox(30);
        layout.getChildren().addAll(label,button1,button2);
        scene = new Scene(layout,200,200);

        HBox layout1 = new HBox(30);
        Label label1 = new Label("RMI Successfully connected");
        layout1.getChildren().addAll(label1,button3);
        scene1 = new Scene(layout1,200,200);

        HBox layout2 = new HBox(30);
        Label label2 = new Label("Socket successfully Connected");
        layout2.getChildren().addAll(label2,button3);
        scene2 = new Scene(layout2,200,200);


        button1.setOnAction( e ->
            primaryStage.setScene(scene1));
        button2.setOnAction( e -> primaryStage.setScene(scene2));
        button3.setOnAction( e -> primaryStage.setScene(scene));



        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.show();



    }*/
    /*public void start(Stage primaryStage) {
        Label label = new Label("Choose connection");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;"); // Stile opzionale per la label

        //BUTTONS
        button1 = new Button("RMI");
        button2 = new Button("Socket");
        button3 = new Button("return");

        button1.setPrefSize(120, 50);
        button2.setPrefSize(120, 50);


        // LAYOUT OF FIRST PAGE
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);


        HBox buttonsLayout = new HBox(30);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(button1, button2);


        layout.getChildren().addAll(label, buttonsLayout);
        scene = new Scene(layout, 400, 300);

        //RMI SCENE
        VBox rmiLayout = new VBox(20);
        rmiLayout.setAlignment(Pos.CENTER);
        Label label1 = new Label("RMI Successfully connected");
        rmiLayout.getChildren().addAll(label1);
        rmiLayout.prefWidthProperty().bind(primaryStage.widthProperty());
        rmiLayout.prefHeightProperty().bind(primaryStage.heightProperty());
        scene1 = new Scene(rmiLayout, 400, 300);

        //SOCKET SCENE
        VBox socketLayout = new VBox(20);
        socketLayout.setAlignment(Pos.CENTER);
        Label label2 = new Label("Socket successfully Connected");
        socketLayout.getChildren().addAll(label2);
        socketLayout.prefWidthProperty().bind(primaryStage.widthProperty());
        socketLayout.prefHeightProperty().bind(primaryStage.heightProperty());


        scene2 = new Scene(socketLayout, 400, 300);



        button1.setOnAction(e -> primaryStage.setScene(scene1));
        button2.setOnAction(e -> primaryStage.setScene(scene2));
        //button3.setOnAction(e -> primaryStage.setScene(scene));



        VBox nextLayout = new VBox(20);
        nextLayout.setAlignment(Pos.CENTER);
        Label nextLabel = new Label("Enter your nickname");
        nextLabel.setStyle("-fx-font-size: 28px;");
        nextLayout.getChildren().add(nextLabel);
        getNameScene = new Scene(nextLayout, 600, 400);

        // Modifica dei gestori eventi per le scene RMI e Socket
        button1.setOnAction(e -> {
            primaryStage.setScene(scene1);
            startAutoTransition(primaryStage, getNameScene, 3); // 3 secondi
        });

        button2.setOnAction(e -> {
            primaryStage.setScene(scene2);
            startAutoTransition(primaryStage, getNameScene, 3); // 3 secondi
        });




        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true); // Modalità fullscreen
        primaryStage.show();
    }*/

    //lo stage è la finestra che mi compare
    public void start(Stage primaryStage) {
        clientController = new ClientController();

        //chooseConnection Layout
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

        // RMI LAYOUT
        VBox rmiLayout = new VBox(20);
        rmiLayout.setAlignment(Pos.CENTER);
        Label label1 = new Label("RMI Successfully connected");
        rmiLayout.getChildren().add(label1);

        // SOCKET LAYOUT
        VBox socketLayout = new VBox(20);
        socketLayout.setAlignment(Pos.CENTER);
        Label label2 = new Label("Socket successfully Connected");
        socketLayout.getChildren().add(label2);

        // Choose your nickname Layout
        VBox nicknameLayout = new VBox(20);
        nicknameLayout.setAlignment(Pos.CENTER);
        Label nicknameLabel = new Label("Enter your nickname");
        nicknameLabel.setStyle("-fx-font-size: 28px;");

        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter here");
        nicknameField.setMaxWidth(300);
        Button confirmButton = new Button("Confirm");
        confirmButton.setDefaultButton(true);
        confirmButton.setVisible(false);
        nicknameField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setVisible(!newVal.trim().isEmpty());
        });

        nicknameLayout.getChildren().addAll(nicknameLabel, nicknameField, confirmButton);



        button1.setOnAction(e -> {
            try {
                clientController.connectRMI();
                scene.setRoot(rmiLayout);
            } catch (MalformedURLException | NotBoundException | RemoteException e2) {
                throw new RuntimeException(e2);
            }
            startAutoTransition(primaryStage, scene, nicknameLayout, 2);
        });

        button2.setOnAction(e -> {
            try {
                clientController.connectSocket("localhost",12345);
                scene.setRoot(socketLayout);
            } catch (IOException e1) {
                System.out.println("Error connecting to server via socket");
            }
            startAutoTransition(primaryStage, scene, nicknameLayout, 2);
        });

        confirmButton.setOnAction(event -> {
            String nickname = nicknameField.getText().trim();
            if (!nickname.isEmpty()) {
                System.out.println("Nickname inserito: " + nickname);

                // AGGIUNGEREMO QUI LA LOGICA PER GESTIRE IL NICKNAME(SET STATE E ROBE COSì IMMAGINO)


                VBox colorLayout = chooseColor(selectedColor -> {
                    selectedColorHolder[0] = selectedColor;
                    System.out.println("Colore scelto per " + nicknameField.getText() + ": " + selectedColor);

                    VBox createJoinGame = createJoinGame(nickname, selectedColor);
                    scene.setRoot(createJoinGame);

                });
                scene.setRoot(colorLayout);

            } else {
                System.out.println("Nickname vuoto.");
            }
        });



        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }



    public VBox chooseColor(Consumer<String> colorSelected){
        VBox colorLayout = new VBox(20);
        colorLayout.setAlignment(Pos.CENTER);
        Label label = new Label("Choose your color");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox imagesBox = new HBox(20);
        imagesBox.setAlignment(Pos.CENTER);

        // Array dei colori disponibili
        String[] colors = {"red", "blue", "green", "yellow"};

        for (String color : colors) {
            Image image = new Image(getClass().getResourceAsStream("/images/" + color + "_starting_cabin.png")); // metti le immagini in resources/images/
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


    public VBox createJoinGame(String nickname, String color) {
        VBox gameLayout = new VBox(20);
        gameLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, " + nickname + "!");
        welcomeLabel.setStyle("-fx-font-size: 28px;");

        Label colorLabel = new Label("Your selected color: " + color);
        colorLabel.setStyle("-fx-font-size: 20px;");

        Label createLabel = new Label("Create a New Game or...");
        Label joinLabel = new Label("...join an existente Game");


        TextField gameNameField = new TextField();
        gameNameField.setPromptText("Enter your new Game Name here");
        gameNameField.setMaxWidth(300);
        Button confirmButton = new Button("Confirm");
        confirmButton.setDefaultButton(true);
        confirmButton.setVisible(false);
        gameNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setVisible(!newVal.trim().isEmpty());
        });


        button1 = new Button("Joinable Game");
        button2 = new Button("JOinable Game");

        button1.setPrefSize(120, 50);
        button2.setPrefSize(120, 50);

        gameLayout.getChildren().addAll(welcomeLabel, colorLabel,createLabel, gameNameField, confirmButton, joinLabel, button1, button2);

        return gameLayout;
    }








    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == button1) {
            System.out.println("Button clicked");
        }
    }




    // Metodo per gestire la transizione automatica
    public void startAutoTransition(Stage stage, Scene scene, Parent nextRoot, int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> scene.setRoot(nextRoot));
        pause.play();
    }

}
