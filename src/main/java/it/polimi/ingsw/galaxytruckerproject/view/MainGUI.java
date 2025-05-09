package it.polimi.ingsw.galaxytruckerproject.view;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


import javafx.event.ActionEvent;
import javafx.util.Duration;

import static javafx.application.Application.launch;

public class MainGUI extends Application implements EventHandler<ActionEvent> {

    Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Scene scene, scene1, scene2, scene3, getNameScene;
    VBox vbox1;
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
    public void start(Stage primaryStage) {
        Label label = new Label("Choose connection");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        button1 = new Button("RMI");
        button2 = new Button("Socket");

        button1.setPrefSize(120, 50);
        button2.setPrefSize(120, 50);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        HBox buttonsLayout = new HBox(30);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(button1, button2);
        layout.getChildren().addAll(label, buttonsLayout);


        Scene scene = new Scene(layout);

        // Layout per la "pagina" RMI
        VBox rmiLayout = new VBox(20);
        rmiLayout.setAlignment(Pos.CENTER);
        Label label1 = new Label("RMI Successfully connected");
        rmiLayout.getChildren().add(label1);

        // Layout per la "pagina" Socket
        VBox socketLayout = new VBox(20);
        socketLayout.setAlignment(Pos.CENTER);
        Label label2 = new Label("Socket successfully Connected");
        socketLayout.getChildren().add(label2);

        // Layout per la scena successiva
        VBox getNameLayout = new VBox(20);
        getNameLayout.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Enter your nickname");

        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Enter your nickname here");
        nicknameField.setMaxWidth(300); // opzionale, per non farla troppo larga
        getNameLayout.getChildren().add(nicknameField);

        nameLabel.setStyle("-fx-font-size: 28px;");
        getNameLayout.getChildren().add(nameLabel);

        button1.setOnAction(e -> {
            scene.setRoot(rmiLayout);
            startAutoTransition(primaryStage, scene, getNameLayout, 2);
        });

        button2.setOnAction(e -> {
            scene.setRoot(socketLayout);
            startAutoTransition(primaryStage, scene, getNameLayout, 2);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == button1) {
            System.out.println("Button clicked");
        }
    }




    // Metodo per gestire la transizione automatica
    private void startAutoTransition(Stage stage, Scene scene, Parent nextRoot, int seconds) {
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));
        pause.setOnFinished(e -> scene.setRoot(nextRoot));
        pause.play();
    }

}
