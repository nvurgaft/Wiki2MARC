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
import org.apache.commons.lang3.StringUtils;
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

    protected String defaultPrefices;

    private String graphName;
    private VirtGraph graph;

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

        this.defaultPrefices = StringUtils.join(Prefixes.RDF, " ", "\n");
    }
    
    /**
     * Clears the working graph
     * @return true if the clearing process ran without throwing exceptions. 
     * Note that it doesn't mean the graph was actually cleared as the driver
     * passes no such information in the API.
     */
    public boolean clearGraph() {

        if (this.graph != null) {
            try {
                this.graph.clear();
            } catch (Exception ex) {
                logger.error("Exception while clearing graph " + this.graphName, ex);
                return false;
            }
        }
        return true;
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
        List<RDFStatement> statement = null;
        try {
            // define a describe query
            String query = defaultPrefices + "DESCRIBE " + subject + " FROM " + this.graphName;

            Query sparqlQuery = QueryFactory.create(query);
            VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery, this.graph);
            // execute the query and get the graph
            Model model = vqe.execDescribe();
            Graph queriedGraph = model.getGraph();
            // itreate over the retrieved triples, and place them inside a list
            ExtendedIterator<Triple> iter = queriedGraph.find(Node.ANY, Node.ANY, Node.ANY);
            statement = new ArrayList<>();
            while (iter.hasNext()) {
                Triple t = (Triple) iter.next();
                RDFStatement stmt = new RDFStatement(t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString());
                statement.add(stmt);
            }
        } catch (Exception ex) {
            logger.error("Exception occured while querying for statements", ex);
        }
        return statement;
    }

    /**
     * Selects a triple by providing an RDFStatement object collection
     *
     * @param stmts
     * @return Query result
     */
    public List<RDFStatement> selectTriples(Collection<? extends RDFStatement> stmts) {

        if (stmts == null || stmts.isEmpty()) {
            return null;
        }

        RDFStatement rdfs = stmts.stream().findFirst().get();
        String si = rdfs.getSubject(), pi = rdfs.getPredicate(), oi = rdfs.getObject(); // doesnt get value

        StringBuilder sb = new StringBuilder();
        stmts.stream().forEach(s -> {
            sb.append(s.getSubject()).append(" ").append(s.getPredicate()).append(" ").append(s.getObject()).append(" .\n");
        });
        String query = this.defaultPrefices + "SELECT * FROM <" + this.graphName + "> WHERE { " + sb.toString() + " }";
        Query sparqlQuery = QueryFactory.create(query);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery, this.graph);
        List<RDFStatement> stmtsList = new ArrayList<>();
        ResultSet rs = vqe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode s = qs.get(si);
            RDFNode p = qs.get(pi);
            RDFNode o = qs.get(oi);
            RDFStatement stmt = new RDFStatement(
                    s != null ? s.toString() : "null",
                    p != null ? p.toString() : "null",
                    o != null ? o.toString() : "null"
            );
            stmtsList.add(stmt);
            logger.info("fetched: " + stmt.toString());
        }
        return stmtsList;
    }

    /**
     * Selects a triple by providing an RDFStatement object
     *
     * @param statement
     * @return Query result
     */
    public List<RDFStatement> selectTriples(RDFStatement statement) {
        List<RDFStatement> stmts = new ArrayList<>();
        stmts.add(statement);
        return this.selectTriples(stmts);
    }

    /**
     * Selects a triple by providing statement literals
     *
     * @param s Subject
     * @param p Predicate
     * @param o Object
     * @return Query result
     */
    public List<RDFStatement> selectTriples(String s, String p, String o) {
        return selectTriples(new RDFStatement(s, p, o));
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
            String query = this.defaultPrefices + "INSERT INTO GRAPH <" + this.graphName + "> { " + stmt.toString() + "}";
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, this.graph);
            vur.exec();
            return true;
        } catch (Exception ex) {
            logger.error("Exception while inserting a statement", ex);
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
            String queryString = this.defaultPrefices + "INSERT INTO GRAPH <" + this.graphName + "> { " + sb.toString() + " }";

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
            String query = this.defaultPrefices + "DELETE FROM GRAPH <" + this.graphName + "> { " + stmt.toString() + " }";
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
            String queryString = this.defaultPrefices + "DELETE FROM GRAPH < " + this.graphName + "> { " + sb.toString() + "}";
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(queryString, this.graph);
            vur.exec();
            return true;
        } catch (Exception ex) {
            logger.error("Exception while batch deleting statements", ex);
        }
        return false;
    }
}
