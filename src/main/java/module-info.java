module it.polimi.ingsw.galaxytruckerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.smartcardio;
    requires java.compiler;
    requires java.desktop;

    opens it.polimi.ingsw.galaxytruckerproject to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.tiles to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.cards.projectiles to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.cards.penalties to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.galaxytruckerproject;
    exports it.polimi.ingsw.galaxytruckerproject.player;
    exports it.polimi.ingsw.galaxytruckerproject.tiles;
    exports it.polimi.ingsw.galaxytruckerproject.cards;
    exports it.polimi.ingsw.galaxytruckerproject.cards.penalties;
    exports it.polimi.ingsw.galaxytruckerproject.cards.projectiles;
}