package it.polimi.ingsw.galaxytruckerproject.view.gui;

import it.polimi.ingsw.galaxytruckerproject.view.GUI;
import javafx.fxml.FXML;
import javafx.scene.image.*;

import java.io.InputStream;

public class DeckCheckController {

    @FXML
    public ImageView imageView1;
    @FXML
    public ImageView imageView2;
    @FXML
    public ImageView imageView3;

    @FXML
    public void initialize() {
        imageView1.setImage(loadImage(GUI.displayableCards().get(0).getFilePath()));
        imageView2.setImage(loadImage(GUI.displayableCards().get(1).getFilePath()));
        imageView3.setImage(loadImage(GUI.displayableCards().get(2).getFilePath()));
    }

    private Image loadImage(String path) {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Immagine non trovata: " + path);
            return null;
        }
        return new Image(stream);
    }

    @FXML
    public void done() {
        GUI.doneChecking();
    }

}
