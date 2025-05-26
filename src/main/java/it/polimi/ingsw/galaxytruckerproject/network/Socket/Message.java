package it.polimi.ingsw.galaxytruckerproject.network.Socket;

import java.io.Serializable;

/**
 * Represents a generic message used in the communication between the server and the client.
 * This is an abstract base class for all message types exchanged between the client and server.
 * <p>
 * Classes that extend this class define the specific details of the message and its content.
 * This class ensures that all messages can be serialized for transmission over the network.
 * </p>
 */
public abstract class Message implements Serializable {
    // This class is intentionally left empty, as it serves as a base class for specific message types
}
