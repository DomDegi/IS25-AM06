module it.polimi.ingsw.galaxytruckerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.smartcardio;
    requires java.compiler;
    requires java.desktop;
    requires java.rmi;

    opens it.polimi.ingsw.galaxytruckerproject.controller to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.model.tiles to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.model.cards.penalties to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.galaxytruckerproject.model.player to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.galaxytruckerproject.lightmodel;
    exports it.polimi.ingsw.galaxytruckerproject.controller;
    exports it.polimi.ingsw.galaxytruckerproject.model.player;
    exports it.polimi.ingsw.galaxytruckerproject.model.tiles;
    exports it.polimi.ingsw.galaxytruckerproject.model.cards;
    exports it.polimi.ingsw.galaxytruckerproject.model.cards.penalties;
    exports it.polimi.ingsw.galaxytruckerproject.model.cards.projectiles;
    exports it.polimi.ingsw.galaxytruckerproject.model;
    exports it.polimi.ingsw.galaxytruckerproject.view;
    exports it.polimi.ingsw.galaxytruckerproject.model.goods;
    exports it.polimi.ingsw.galaxytruckerproject.network.SOCKET.message;
    exports it.polimi.ingsw.galaxytruckerproject.client;
    opens it.polimi.ingsw.galaxytruckerproject.model.goods to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.galaxytruckerproject.network;
    exports it.polimi.ingsw.galaxytruckerproject.controller.interfaces;
}