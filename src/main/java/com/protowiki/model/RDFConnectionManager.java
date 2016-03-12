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

    protected DatabaseProperties dbProperties = new DatabaseProperties("application.properties");

    private String username, password, host;
    private int port;
    private String graphName, connectionString;

    private VirtGraph graph;

    /**
     * Set up the credentials for Virtuoso RDF connection
     *
     * @param graphName
     */
    public RDFConnectionManager(String graphName) {
        this.graphName = graphName;

    }

    public void connecttToDefaultDatabase() {

        try {

            this.username = dbProperties.getProperty("username");
            this.password = dbProperties.getProperty("password");
            this.host = dbProperties.getProperty("host");
            this.port = Integer.valueOf(dbProperties.getProperty("port"));

            String format = "jdbc:virtuoso://%s:%s/charset=UTF-8/log_enable=2";
            this.connectionString = String.format(format, this.host, this.port);
        } catch (Exception e) {
            logger.error("An exception occured while attempting to connect to database", e);
        }
    }

    /**
     * Retrieves an RDF graph
     *
     * @return VirtGraph graph
     */
    public VirtGraph getGraphConnection() {
        return new VirtGraph(this.connectionString, username, password);
    }

    /**
     * Retrieves an RDF graph
     *
     * @param clearGraphOnStart Should the graph be cleared after retrieval
     * @return
     */
    public VirtGraph getGraphConnection(boolean clearGraphOnStart) {
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
}
