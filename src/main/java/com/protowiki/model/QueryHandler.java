package com.protowiki.model;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.protowiki.entities.RDFStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

/**
 * This is simple data access implementation, that reads, writes and deletes
 * records into a local Virtuoso RDF database.
 *
 * @author Nick
 */
public class QueryHandler {

    public static Logger logger = LoggerFactory.getLogger(QueryHandler.class);

    String graphName;
    VirtGraph graph;

    /**
     * Constructs the handler object using a given Graph name and a Virtuoso
     * Graph
     *
     * @param graph
     * @param graphName
     */
    public QueryHandler(VirtGraph graph, String graphName) {
        this.graphName = graphName;
        this.graph = graph;
    }

    /**
     * Returns a collection of RDFStatements that match the described subject
     *
     * @param subject Subject
     * @return collection of RDFStatements
     */
    public Collection<RDFStatement> getStatements(String subject) {

        if (subject == null || subject.isEmpty()) {
            return null;
        }
        // define a describe query
        Query sparqlQuery = QueryFactory.create("DESCRIBE " + subject + " FROM " + this.graphName);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery, this.graph);
        // execute the query and get the graph
        Model model = vqe.execDescribe();
        Graph queriedGraph = model.getGraph();
        // itreate over the retrieved triples, and place them inside a list
        ExtendedIterator<Triple> iter = queriedGraph.find(Node.ANY, Node.ANY, Node.ANY);
        List<RDFStatement> statement = new ArrayList<>();
        while (iter.hasNext()) {
            Triple t = (Triple) iter.next();
            RDFStatement stmt = new RDFStatement(t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString());
            statement.add(stmt);
        }
        return statement;
    }

    public void selectTriples(String s, String p, String o) {
        selectTriples(new RDFStatement(s, p, o));
    }

    public void selectTriples(RDFStatement statement) {

        String query = "SELECT * FROM <http://test1> WHERE {?s ?p ?o}";
        Query sparqlQuery = QueryFactory.create(query);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery, this.graph);
        ResultSet rs = vqe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode s = qs.get("s");
            RDFNode p = qs.get("p");
            RDFNode o = qs.get("o");
            RDFStatement stmt = new RDFStatement(s.toString(), p.toString(), o.toString());
            logger.info(stmt.toString());
        }
        
        
        // TODO
    }

    /**
     * Inserts a statement into the graph
     *
     * @param s Subject
     * @param p Predicate
     * @param o Object
     * @return true if and only if the query execution was successful
     */
    public boolean insertStatement(String s, String p, String o) {
        return this.insertStatement(new RDFStatement(s, p, o));
    }

    /**
     * Inserts a statement into the graph
     *
     * @param stmt Statement (RDFStatement triple)
     * @return true if and only if the query execution was successful
     */
    public boolean insertStatement(RDFStatement stmt) {
        if (stmt == null) {
            return false;
        }
        try {
            String query = "INSERT INTO GRAPH <" + this.graphName + "> { " + stmt.toString() + "}";
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, this.graph);
            vur.exec();
            return true;
        } catch (Exception ex) {
            logger.error("Exception while deleting a statement", ex);
        }
        return false;
    }

    /**
     * Batch insert statements into the graph
     *
     * @param statements
     * @return true if and only if the query execution was successful
     */
    public boolean batchInsertStatements(Collection<? extends RDFStatement> statements) {
        if (statements == null || statements.isEmpty()) {
            return false;
        }

        try {
            StringBuilder sb = new StringBuilder();
            statements.stream().forEach(s -> {
                sb.append(s.toString()).append(" . ");
            });
            String queryString = "INSERT INTO GRAPH <" + this.graphName + "> { " + sb.toString() + " }";

            logger.info("Running query: \n\n" + queryString + " \n\n");

            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(queryString, this.graph);
            vur.exec();
            return true;
        } catch (Exception ex) {
            logger.error("Exception while batch inserting statements", ex);
        }
        return false;
    }

    /**
     * Delete a statement from a graph
     *
     * @param s Subject
     * @param p Predicate
     * @param o Object
     * @return true if and only if the query execution was successful
     */
    public boolean deleteStatement(String s, String p, String o) {
        return this.deleteStatement(new RDFStatement(s, p, o));
    }

    /**
     * Delete a statement from a graph
     *
     * @param stmt (RDFStatement triple)
     * @return true if and only if the query execution was successful
     */
    public boolean deleteStatement(RDFStatement stmt) {
        if (stmt == null) {
            return false;
        }
        try {
            String query = "DELETE FROM GRAPH <" + this.graphName + "> { " + stmt.toString() + " }";
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, this.graph);
            vur.exec();
            return true;
        } catch (Exception ex) {
            logger.error("Exception while deleting a statement", ex);
        }
        return false;
    }

    /**
     * Batch delete statements from the graph
     *
     * @param statements (RDFStatement triple)
     * @return true if and only if the query execution was successful
     */
    public boolean batchDeleteStatements(Collection<? extends RDFStatement> statements) {
        if (statements == null || statements.isEmpty()) {
            return false;
        }

        try {
            StringBuilder sb = new StringBuilder();
            statements.stream().forEach(s -> {
                sb.append(s.toString()).append(" . ");
            });
            String queryString = "DELETE FROM GRAPH < " + this.graphName + "> { " + sb.toString() + "}";
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(queryString, this.graph);
            vur.exec();
            return true;
        } catch (Exception ex) {
            logger.error("Exception while batch inserting statements", ex);
        }
        return false;
    }
}
