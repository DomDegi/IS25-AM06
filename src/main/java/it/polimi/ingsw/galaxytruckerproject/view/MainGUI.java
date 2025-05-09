package it.polimi.ingsw.galaxytruckerproject.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import javafx.event.ActionEvent;

import static javafx.application.Application.launch;

public class MainGUI extends Application implements EventHandler<ActionEvent> {

    Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Scene scene, scene1, scene2, scene3;
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
    public void start(Stage primaryStage) {
        Label label = new Label("Choose connection");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;"); // Stile opzionale per la label

        //BUTTONS
        button1 = new Button("RMI");
        button2 = new Button("Socket");
        button3 = new Button("return");

        button1.setPrefSize(120, 50);
        button2.setPrefSize(120, 50);


        // LAYOUT OF FIRST PAGE
        VBox Layout = new VBox(20);
        Layout.setAlignment(Pos.CENTER);


        HBox buttonsLayout = new HBox(30);
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.getChildren().addAll(button1, button2);


        Layout.getChildren().addAll(label, buttonsLayout);
        scene = new Scene(Layout, 400, 300);

        //RMI SCENE
        VBox rmiLayout = new VBox(20);
        rmiLayout.setAlignment(Pos.CENTER);
        Label label1 = new Label("RMI Successfully connected");
        rmiLayout.getChildren().addAll(label1);
        scene1 = new Scene(rmiLayout, 400, 300);

        //SOCKET SCENE
        VBox socketLayout = new VBox(20);
        socketLayout.setAlignment(Pos.CENTER);
        Label label2 = new Label("Socket successfully Connected");
        socketLayout.getChildren().addAll(label2);
        scene2 = new Scene(socketLayout, 400, 300);


        button1.setOnAction(e -> primaryStage.setScene(scene1));
        button2.setOnAction(e -> primaryStage.setScene(scene2));
        //button3.setOnAction(e -> primaryStage.setScene(scene));

        // Configurazione finestra
        primaryStage.setScene(scene);
        primaryStage.setTitle("Galaxy Trucker");
        primaryStage.setFullScreen(true); // Modalità fullscreen
        primaryStage.show();
    }


    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == button1) {
            System.out.println("Button clicked");
        }
    }
}
