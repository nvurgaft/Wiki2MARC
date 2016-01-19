package com.protowiki.model;

import com.protowiki.beans.Author;
import com.protowiki.db.RDFConnectionManager;
import com.protowiki.entities.RDFStatement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;

/**
 *
 * @author Nick
 */
public class AuthorModel {
    
    public static Logger logger = LoggerFactory.getLogger(AuthorModel.class);
    
    // default virtuoso login credentials 
    private static final String GRAPH_NAME = "http://authors";

    
    /**
     * 
     * @param authorsList
     * @return was the transaction successful
     */
    public boolean insertAuthorsIntoDB(List<Author> authorsList) {
        
        if (authorsList==null || authorsList.isEmpty()) {
            return false;
        }
        
        VirtGraph graph = new RDFConnectionManager(GRAPH_NAME).getGraphConnection(false);
        QueryHandler qh = new QueryHandler(graph, GRAPH_NAME);
        
        authorsList.stream().forEach(author -> {
            // build a statement model
            List<RDFStatement> stmts = new ArrayList<>();
            stmts.add(new RDFStatement(author.getMarcId(), "rdf:url", author.getWikipediaUri()));
            stmts.add(new RDFStatement(author.getMarcId(), "rdf:enName", author.getNames().get("en")));
            stmts.add(new RDFStatement(author.getMarcId(), "rdf:heName", author.getNames().get("he")));
            stmts.add(new RDFStatement(author.getMarcId(), "rdf:viaf", author.getViafId()));
            stmts.add(new RDFStatement(author.getMarcId(), "rdf:nli", author.getNliId()));
            
            qh.batchInsertStatements(stmts);
            
            // WIP
            
        });
        
        
        return false;
    }
    
    public boolean fetchAuthorsFromDB() {
        
        VirtGraph graph = new RDFConnectionManager(GRAPH_NAME).getGraphConnection(false);
        QueryHandler qh = new QueryHandler(graph, GRAPH_NAME);
        
        // TODO
        
        return false;
    }

}
