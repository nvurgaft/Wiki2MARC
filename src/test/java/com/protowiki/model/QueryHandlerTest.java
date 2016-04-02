package com.protowiki.model;

import com.protowiki.entities.RDFStatement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;

/**
 *
 * @author Nick
 */
@Ignore
public class QueryHandlerTest {
    
    public static Logger logger = LoggerFactory.getLogger(QueryHandlerTest.class);
    
    private static final String GRAPH_NAME = "http://test_author";
    private final VirtGraph graph;
    private final QueryHandler qh;
    
    public QueryHandlerTest() {
        graph = new VirtGraph("jdbc:virtuoso://localhost:1111/CHARSET=UTF-8/log_enable=2", "dba", "dba");
        qh = new QueryHandler(this.graph, GRAPH_NAME);
    }
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void before() {
        logger.info("before: " + testName.getMethodName());
    }
    
    @After
    public void after() {
        logger.info("after: " + testName.getMethodName());
    }

    /**
     * Test of getStatements method, of class QueryHandler.
     */
    @Test
    public void testGetStatements() {

        String subject = "<Test_URI>";

        Collection<RDFStatement> result = qh.getStatements(subject);
        result.stream().forEach(r -> {
            logger.info("record: " + r.toString());
        });
    }

    /**
     * Test of selectTriples method, of class QueryHandler.
     */
    @Test
    public void testSelectTriples_Collection() {

        List<RDFStatement> stmts = Arrays.asList(new RDFStatement("?a", "?b", "?c"));
        List<RDFStatement> result = qh.selectTriples(stmts);
        result.stream().forEach(r-> {
            logger.info("record: " + r);
        });
    }


    /**
     * Test of selectTriples method, of class QueryHandler.
     */
    @Test
    public void testSelectTriples_3args() {
        
        List<RDFStatement> result = qh.selectTriples("?a", "?b", "?c");
        
        result.stream().forEach(r -> {
            logger.info("record: " + r);
        });
    }

    /**
     * Test of insertStatement method, of class QueryHandler.
     */
    @Test
    public void testInsertStatement_3args() {

        String s = "Test_URI";
        String p = "http://www.w3.org/1999/02/22-rdf-syntax-ns#brName";
        String o = "jorge_smith";
        boolean result = qh.insertStatement(s, p, o);
        logger.info("insert result: " + result);
        assertEquals("Should successfult insert a triple" ,true, result);
    }
    
    @Test
    public void testInsertStatement() {
        String s = "71388952";
        String p = "rdf:abstract";
        String o = "Dame Agatha Mary Clarissa Christie, DBE (née Miller; 15 September 1890 – 12 January 1976) was an English crime novelist, short story writer and playwright. She also wrote six romances under the name Mary Westmacott, but she is best known for the 66 detective novels and 14 short story collections that she wrote under her own name, most of which revolve around the investigative work of such characters as Hercule Poirot, Jane Marple, Parker Pyne, Harley Quin/Mr Satterthwaite and Tommy and Tuppence Beresford. She wrote the world's longest-running play, a murder mystery, The Mousetrap. In 1971 she was made a Dame for her contribution to literature.Christie was born into a wealthy upper-middle-class family in Torquay, Devon. She served in a hospital during the First World War before marrying and starting a family in London. She was initially unsuccessful at getting her work published, but in 1920 The Bodley Head press published her novel The Mysterious Affair at Styles, featuring the character of Hercule Poirot. This launched her literary career.The Guinness Book of World Records lists Christie as the best-selling novelist of all time. Her novels have sold roughly 2 billion copies, and her estate claims that her works come third in the rankings of the world's most-widely published books, behind only Shakespeare's works and the Bible. According to Index Translationum, she remains the most-translated individual author – having been translated into at least 103 languages. And Then There Were None is Christie's best-selling novel with 100 million sales to date, making it the world's best-selling mystery ever, and one of the best-selling books of all time.Christie's stage play The Mousetrap holds the record for the longest initial run: it opened at the Ambassadors Theatre in the West End on 25 November 1952 and as of 2015 is still running after more than 25,000 performances. In 1955 Christie was the first recipient of the Mystery Writers of America's highest honour, the Grand Master Award, and in the same year Witness for the Prosecution received an Edgar Award by the MWA for Best Play. In 2013, The Murder of Roger Ackroyd was voted the best crime novel ever by 600 fellow writers of the Crime Writers' Association. On 15 September 2015, coinciding with Agatha Christie's 125th birthday, And Then There Were None was voted as the \"World's Favorite Christie\", followed closely by Murder on the Orient Express and The Murder of Roger Ackroyd. Most of her books and short stories have been adapted for television, radio, video games and comics, and more than thirty feature films have been based on her work";
        
        boolean result = qh.insertStatement(s, p, o);
        logger.info("insert result: " + result);
        assertEquals("Should successfult insert a triple" ,true, result);
    }
    
    
    /**
     * Test of insertStatement method, of class QueryHandler.
     */
    @Test
    public void testInsertStatement_RDFStatement() {

        String s = "Test_URI";
        String p = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o = "john_yates";
        boolean result = qh.insertStatement(new RDFStatement(s, p, o));
        logger.info("insert result: " + result);
        assertEquals("Should successfult insert an RDFStatement object" ,true, result);
    }

    /**
     * Test of batchInsertStatements method, of class QueryHandler.
     */
    @Test
    public void testBatchInsertStatements() {
        
        String s1 = "Test_URI";
        String p1 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o1 = "john_yates";
        
        String s2 = "Test_URI";
        String p2 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o2 = "neil_armstrong";
        
        String s3 = "Test_URI";
        String p3 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o3 = "Nick Vurgaft";
        
        List<? extends RDFStatement> statements = Arrays.asList(
                new RDFStatement(s1, p1, o1), 
                new RDFStatement(s2, p2, o2),
                new RDFStatement(s3, p3, o3)
        );
        
        boolean result = qh.batchInsertStatements(statements);
        assertEquals(true, result);
    }

    /**
     * Test of deleteStatement method, of class QueryHandler.
     */
    @Test
    public void testDeleteStatement_3args() {

        String s = "Test_URI";
        String p = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o = "john_yates";
        boolean result = qh.deleteStatement(s, p, o);
        assertEquals(true, result);
    }

    /**
     * Test of deleteStatement method, of class QueryHandler.
     */
    @Test
    public void testDeleteStatement_RDFStatement() {
        
        String s = "Test_URI";
        String p = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o = "john_yates";
        
        RDFStatement stmt = new RDFStatement(s, p, o);
        boolean result = qh.deleteStatement(stmt);
        assertEquals(true, result);
    }

    /**
     * Test of batchDeleteStatements method, of class QueryHandler.
     */
    @Test
    public void testBatchDeleteStatements() {
        
        String s1 = "Test_URI";
        String p1 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o1 = "john_yates";
        
        String s2 = "Test_URI";
        String p2 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#enName";
        String o2 = "neil_armstrong";
        
        List<? extends RDFStatement> statements = Arrays.asList(new RDFStatement(s1, p1, o1), new RDFStatement(s2, p2, o2));
        boolean result = qh.batchDeleteStatements(statements);
        assertEquals(true, result);
    }
    
    @Test
    public void testStreams() {
        
        IntStream.range(42, 47).map(i -> {
            return (int) Math.pow(i, 2);
        }).forEach(n -> {
            System.out.println(n);
        });
    }
    
}
