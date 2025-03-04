module it.polimi.ingsw.galaxytruckerproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.galaxytruckerproject to javafx.fxml;
    exports it.polimi.ingsw.galaxytruckerproject;
}