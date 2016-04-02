package com.protowiki.model;

import com.protowiki.values.Predicates;
import com.protowiki.beans.Author;
import com.protowiki.entities.RDFStatement;
import com.protowiki.utils.RDFUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;

/**
 * This class handles the database CRUD operation for the Author object
 *
 * @author Nick
 */
public class AuthorModel {

    public static Logger logger = LoggerFactory.getLogger(AuthorModel.class);

    private static final String GRAPH_NAME = "http://authors";

    private final RDFConnectionManager connectionManager;
    private VirtGraph graph;
    private final QueryHandler queryHandler;

    public AuthorModel() {

        connectionManager = new RDFConnectionManager(GRAPH_NAME);
        connectionManager.getGraphConnection(false);
        graph = new VirtGraph("jdbc:virtuoso://localhost:1111/CHARSET=UTF-8/log_enable=2", "dba", "dba");
        this.queryHandler = new QueryHandler(graph, GRAPH_NAME);
    }

    /**
     * Clears the authors graph
     *
     * @return
     */
    public boolean clearGraph() {
        return queryHandler.clearGraph();
    }

    /**
     *
     * @param authorsList
     * @return was the transaction successful
     */
    public boolean insertAuthorsIntoDB(List<Author> authorsList) {

        if (authorsList == null || authorsList.isEmpty()) {
            return false;
        }

        try {
            authorsList.stream().forEach(author -> {
                // build a statement model
                List<RDFStatement> stmts = Arrays.asList(
                        new RDFStatement(author.getMarcId(), Predicates.URI, author.getWikipediaUri()),
                        new RDFStatement(author.getMarcId(), Predicates.EN_NAME, author.getNames().get("en")),
                        new RDFStatement(author.getMarcId(), Predicates.HE_NAME, author.getNames().get("he")),
                        new RDFStatement(author.getMarcId(), Predicates.VIAF_ID, author.getViafId()),
                        new RDFStatement(author.getMarcId(), Predicates.NLI_ID, author.getNliId())
                );
                queryHandler.batchInsertStatements(stmts);
            });
        } catch (Exception ex) {
            logger.error("Exception while attempting to batch insert authors into DB", ex);
            return false;
        }
        return true;
    }

    /**
     * Takes a String map of viaf ids to abstracts and stores it in the database
     * model
     *
     * @param abstractsMap
     * @return true if and only is the data insertion was successful
     */
    public boolean insertAuthorsViafAndAbstracts(Map<String, String> abstractsMap) {
        if (abstractsMap == null || abstractsMap.isEmpty()) {
            return false;
        }
        try {
            List<RDFStatement> stmts = new ArrayList<>();
            abstractsMap.keySet().stream().forEach(key -> {
                stmts.add(new RDFStatement(key, Predicates.ABSTRACT, abstractsMap.get(key)));
            });

            queryHandler.batchInsertStatements(stmts);
        } catch (Exception ex) {
            logger.error("Exception while inserting abstracts to model", ex);
        }
        return true;
    }

    /**
     * Takes a pair of a VIAF id and an abstract and stores it in the database
     * model
     *
     * @param viaf
     * @param articleAbstract
     * @return true if and only is the data insertion was successful
     */
    public boolean insertAuthorsViafAndAbstracts(String viaf, String articleAbstract) {
        if (viaf == null || viaf.isEmpty()) {
            return false;
        }
        if (articleAbstract == null || articleAbstract.isEmpty()) {
            return false;
        }
        try {
            logger.info("Inserting viaf: " + viaf + ", and abstract: " + articleAbstract);
            queryHandler.insertStatement(viaf, "rdf:abstract", articleAbstract);
        } catch (Exception ex) {
            logger.error("Exception while inserting abstracts to model", ex);
            return false;
        }
        return true;
    }

    /**
     *
     * @return a map of <viad id keys, abstracts values> pairs
     */
    public Map<String, String> getAuthorsViafAndAbstracts() {
        List<RDFStatement> resultList;
        Map<String, String> resultMap = new HashMap<>();
        try {
            resultList = queryHandler.selectTriples("?viaf", Predicates.ABSTRACT, "?abstract");
            resultList.stream().forEach(r -> {
                resultMap.put(r.getSubject().trim(), r.getObject().trim());
            });
        } catch (Exception ex) {
            logger.error("Exception while fetching author abstracts from model", ex);
        }
        return resultMap;
    }
}
