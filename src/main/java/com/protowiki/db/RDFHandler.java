package com.protowiki.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

/**
 *
 * @author Nick
 */
public class RDFHandler {

    public static Logger logger = LoggerFactory.getLogger(RDFHandler.class);
    
    private String username;
    private String password;
    private String host;
    private int port;
    private String graphName;
    
    private VirtGraph graph;
    
    public RDFHandler(String username, String password, String host, int port, String graphName, boolean clearGraphOnStart) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.graphName = graphName;
        String connString = "jdbc:virtuoso://%s:%s/charset=UTF-8/log_enable=2";
        this.graph = new VirtGraph(String.format(connString, this.host, this.port), username, password);
        if (clearGraphOnStart) {
            this.clearGraph();
        }
    }
    
    public void clearGraph() {
        try {
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create("CLEAR GRAPH <" + this.graphName + ">" , this.graph);
            vur.exec();
        } catch(Exception e) {
            logger.error("Exception while clearing " + this.graphName, e);
        }
    }
    
    public void close() {
        this.graph.close();
    }
}
