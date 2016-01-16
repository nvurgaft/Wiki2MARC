package com.protowiki.jena;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.protowiki.entities.RDFStatement;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

/**
 *
 * @author Nick
 */
@Ignore
public class TestJenaProvider {

    public static Logger logger = LoggerFactory.getLogger(TestJenaProvider.class);

    // default virtuoso login credentials 
    static String connection_string = "jdbc:virtuoso://localhost:1111/CHARSET=UTF-8/log_enable=2";
    static String login = "dba";
    static String password = "dba";

    @Test
    public void showStatements() {

        VirtGraph g = new VirtGraph(connection_string, login, password);
        
        String insertStatementQuery = "INSERT INTO GRAPH <http://test1> { <testSubject> <testPredicate> 'testLiteral'}";
        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(insertStatementQuery, g);
        vur.exec();

        String query = "SELECT * FROM <http://test1> WHERE {?s ?p ?o}";
        Query sparqlQuery = QueryFactory.create(query);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery, g);
        ResultSet rs = vqe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode s = qs.get("s");
            RDFNode p = qs.get("p");
            RDFNode o = qs.get("o");
            RDFStatement stmt = new RDFStatement(s.toString(), p.toString(), o.toString());
            logger.info(stmt.toString());
        }
    }

    @Test
    public void testInsertRDF() {

        VirtGraph graph = new VirtGraph(connection_string, login, password);

        // CLEAR the graph
        String clearGraphQuery = "CLEAR GRAPH <http://test1>";
        logger.info("execute: " + clearGraphQuery);
        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(clearGraphQuery, graph);
        vur.exec();

        // INSERT data into the graph
        String insertStatementQuery = "INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }";
        logger.info("execute: " + insertStatementQuery);
        vur = VirtuosoUpdateFactory.create(insertStatementQuery, graph);
        vur.exec();

        // SELECT data from the graph and print it
        String selectQueryString = "SELECT * FROM <http://test1> WHERE { ?s ?p ?o }";
        logger.info("execute: " + selectQueryString);
        Query sparqlQuery = QueryFactory.create(selectQueryString);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery, graph);

        ResultSet rs = vqe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode s = qs.get("s");
            RDFNode p = qs.get("p");
            RDFNode o = qs.get("o");
            logger.info("Subject: " + s.toString() + ", Predicate: " + p.toString() + ", Object: " + o.toString());
        }

        // DELETE statement from graph
        String deleteQuery = "DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }";
        logger.info("execute: " + deleteQuery);
        vur = VirtuosoUpdateFactory.create(deleteQuery, graph);
        vur.exec();

        // SELECT (again) data from the graph and print it
        logger.info("execute: " + selectQueryString);
        Query sparqlQuery1 = QueryFactory.create(selectQueryString);
        vqe = VirtuosoQueryExecutionFactory.create(sparqlQuery1, graph);

        ResultSet rs1 = vqe.execSelect();
        while (rs1.hasNext()) {
            QuerySolution qs = rs1.nextSolution();
            RDFNode s = qs.get("s");
            RDFNode p = qs.get("p");
            RDFNode o = qs.get("o");
            logger.info("Subject: " + s.toString() + ", Predicate: " + p.toString() + ", Object: " + o.toString());
        }
    }
}
