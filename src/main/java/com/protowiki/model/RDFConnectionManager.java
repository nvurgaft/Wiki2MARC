package com.protowiki.model;

import com.protowiki.utils.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

/**
 *
 * @author Nick
 */
public class RDFConnectionManager {

    public static Logger logger = LoggerFactory.getLogger(RDFConnectionManager.class);

    protected DatabaseProperties dbProperties;

    private String username, password, host, connectionString;
    private final String graphName;
    private int port;
    private boolean connected;

    private VirtGraph graph;

    /**
     * Set up the credentials for Virtuoso RDF connection
     *
     * @param graphName
     */
    public RDFConnectionManager(String graphName) {
        this.graphName = graphName;
    }

    public void connectToDefaultDatabase() {

        try {
            dbProperties = new DatabaseProperties("application.properties");
            this.username = this.getProperty("login", "admin");
            this.password = this.getProperty("password", "admin");
            this.host = this.getProperty("host", "localhost");
            this.port = Integer.valueOf(this.getProperty("port", "1111"));

            String format = "jdbc:virtuoso://%s:%s/charset=UTF-8/log_enable=2";
            this.connectionString = String.format(format, this.host, this.port);
            this.setConnected(true);
        } catch (Exception e) {
            logger.error("An exception occured while attempting to connect to database", e);
            this.setConnected(false);
        }
    }

    public void disconnect() {
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = -1;
        this.setConnected(false);
    }

    /**
     * Retrieves an RDF graph
     *
     * @return VirtGraph graph
     */
    public VirtGraph getGraphConnection() {
        if (!this.isConnected()) {
            this.connectToDefaultDatabase();
        }
        return new VirtGraph(this.connectionString, username, password);
    }

    /**
     * Retrieves an RDF graph
     *
     * @param clearGraphOnStart Should the graph be cleared after retrieval
     * @return
     */
    public VirtGraph getGraphConnection(boolean clearGraphOnStart) {

        if (!this.isConnected()) {
            this.connectToDefaultDatabase();
        }

        this.graph = new VirtGraph(String.format(this.connectionString, this.host, this.port), username, password);
        if (clearGraphOnStart) {
            this.clearGraph();
        }
        return this.graph;
    }

    /**
     * Clears the RDF graph
     */
    private void clearGraph() {
        try {
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create("CLEAR GRAPH <" + this.graphName + ">", this.graph);
            vur.exec();
        } catch (Exception e) {
            logger.error("Exception while clearing " + this.graphName, e);
        }
    }

    /**
     * Fetches the property value from the database property, if null will
     * return the default value
     *
     * @param property property key
     * @param defaultsTo Default value to return
     * @return String value
     */
    public String getProperty(String property, String defaultsTo) {
        return dbProperties.getProperty(property) == null ? defaultsTo : dbProperties.getProperty(property);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
