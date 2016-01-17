package com.protowiki.model;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

/**
 *
 * @author Nick
 */
public class WikidataRemoteAPIModel {

    public static Logger logger = LoggerFactory.getLogger(WikidataRemoteAPIModel.class);

    static String connection_string = "jdbc:virtuoso://localhost:1111/CHARSET=UTF-8/log_enable=2";
    static String login = "dba";
    static String password = "dba";

    private static final String GET_WIKIPEDIA_ABSTRACT_FOR_LABEL = StringUtils.join(
            new String[]{
                "PREFIX ontology: <http://dbpedia.org/ontology/>",
                "PREFIX property: <http://dbpedia.org/property/>",
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
                "SELECT ?name ?abstract WHERE {",
                "?name rdfs:label \"%s\"@%s.",
                "?name ontology:abstract ?abstract",
                "FILTER (langMatches(lang(?abstract), \"en\"))",
                "}"
            }, "\n");

    private static final String GET_VIAF_FROM_HEBREW_AUTHORS = StringUtils.join(
            new String[]{
                "PREFIX wikibase: <http://wikiba.se/ontology#>",
                "PREFIX wd: <http://www.wikidata.org/entity/>",
                "PREFIX wdt: <http://www.wikidata.org/prop/direct/>",
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
                "PREFIX bd: <http://www.bigdata.com/rdf#>",
                "SELECT ?s ?sLabel ?viaf ?nli WHERE {",
                "?s wdt:P31 wd:Q5 .",
                "?s wdt:P949 ?nli .",
                "?s wdt:P214 $viaf",
                "SERVICE wikibase:label {",
                "bd:serviceParam wikibase:language \"he\" .",
                "}",
                "}"
            }, "\n");

    /**
     * Fetches from DBPedia the article abstract for an author in a certain
     * language
     *
     * @param author - the queried author
     * @param language - the language that abstract should be in (if string is 
     * null or empty, will default to 'en')
     */
    public void getWikipediaAbstract(String author, String language) {
        if (author == null || author.isEmpty()) {
            return;
        }
        if (language == null || language.isEmpty()) {
            language = "en";
        }
        
        String queryString = String.format(GET_WIKIPEDIA_ABSTRACT_FOR_LABEL, author, language);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query); // http://dbpedia.org/sparql
     
        ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _name = qs.get("name");
            RDFNode _abstract = qs.get("abstract");
            logger.info("Name: " + _name + ", Abstract: " + _abstract);
        }
    }

    /**
     * Fetches from local VOS all authors who have a VIAF ID and an NLI ID. These
     * response is built in the following format (columns per row): 1. s -
     * Subject 2. sLabel - Subject label 3. viaf - VIAF ID 4. nli - NLI ID
     */
    public void getAuthorsWithVIAF() {

        VirtGraph graph = new VirtGraph(connection_string, login, password);

        Query query = QueryFactory.create(GET_VIAF_FROM_HEBREW_AUTHORS);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, graph);
        ResultSet rs = vqe.execSelect();
        System.out.println(rs.hasNext() ? "has next" : "no data");
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _s = qs.get("s");
            RDFNode _sLabel = qs.get("sLabel");
            RDFNode _viaf = qs.get("viaf");
            RDFNode _nli = qs.get("nli");
            logger.info("s: " + _s + ", Label: " + _sLabel + ", VIAF ID: " + _viaf + ", NLI: " + _nli);
        }
    }
    
    /**
     * Fetches from Wikidata all authors who have a VIAF ID and an NLI ID. These
     * response is built in the following format (columns per row): 1. s -
     * Subject 2. sLabel - Subject label 3. viaf - VIAF ID 4. nli - NLI ID
     */
    public void getAuthorsWithVIAFRemote() {

        Query query = QueryFactory.create(GET_VIAF_FROM_HEBREW_AUTHORS);
        
        QueryExecution qe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
        ResultSet rs = ResultSetFactory.copyResults( qe.execSelect() );
        System.out.println(rs.hasNext() ? "has next" : "no data");
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _s = qs.get("s");
            RDFNode _sLabel = qs.get("sLabel");
            RDFNode _viaf = qs.get("viaf");
            RDFNode _nli = qs.get("nli");
            logger.info("s: " + _s + ", Label: " + _sLabel + ", VIAF ID: " + _viaf + ", NLI: " + _nli);
        }
    }
}
