package it.polimi.ingsw.galaxytruckerproject.model.tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single link (connector) on a side of a tile.
 * A Link is defined by its {@link Connectors} type, which determines how it can connect with other links.
 * It supports JSON serialization/deserialization for saving/loading tile structures.
 */
public class Link implements java.io.Serializable {

    /**
     * Type of the connector (e.g., SINGLE, DOUBLE, UNIVERSAL, SMOOTH).
     */
    Connectors connectorsType;

    /**
     * Constructor used for deserialization with Jackson.
     *
     * @param connectorsType the type of connector
     */
    @JsonCreator
    public Link(@JsonProperty("connectorsType") Connectors connectorsType){
        this.connectorsType = connectorsType;
    }

    /**
     * Gets the type of connector for this link.
     *
     * @return the connector type
     */
    public Connectors getConnectorsType(){
        return connectorsType;
    }

    /**
     * Sets the connector type for this link.
     *
     * @param connectorsType the new connector type
     */
    public void setConnectorsType(Connectors connectorsType) {
        this.connectorsType = connectorsType;
    }

    /**
     * Returns the string representation of the connector type.
     *
     * @return connector type as string
     */
    @Override
    public String toString() {
        return connectorsType.toString();
    }

    /**
     * Checks if this link is compatible with another link based on connector types.
     * Two links are considered connected if:
     * - They have the same connector type
     * - One is UNIVERSAL and the other is SINGLE or DOUBLE
     *
     * @param other the other link to check connection with
     * @return true if the two links are compatible
     */
    public boolean isConnected(Link other){
        if (this.connectorsType == other.connectorsType)
            return true;
        if (this.connectorsType == Connectors.UNIVERSAL &&
                (other.connectorsType == Connectors.DOUBLE || other.connectorsType == Connectors.SINGLE))
            return true;
        if (other.connectorsType == Connectors.UNIVERSAL &&
                (this.connectorsType == Connectors.DOUBLE || this.connectorsType == Connectors.SINGLE))
            return true;

        return false;
    }

    /**
     * Sets the connector type. (Alias of setConnectorsType)
     *
     * @param connectorType new connector type
     */
    public void setConnectorType(Connectors connectorType) {
        this.connectorsType = connectorType;
    }
}
