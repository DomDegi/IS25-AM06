module it.polimi.ingsw.galaxytruckerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.smartcardio;
    requires java.compiler;


    opens it.polimi.ingsw.galaxytruckerproject to javafx.fxml;
    exports it.polimi.ingsw.galaxytruckerproject;
}