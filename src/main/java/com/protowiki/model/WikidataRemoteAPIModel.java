package com.protowiki.model;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.protowiki.beans.Author;
import com.protowiki.utils.RDFUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

/**
 * TODO
 *
 * 1. parse xml for viaf id's 2. for each viaf id query wikidata for hebrew and
 * english label 3. take labels and fetch abstracts using the wikipedia api
 *
 * @author Nick
 */
public class WikidataRemoteAPIModel {

    public static Logger logger = LoggerFactory.getLogger(WikidataRemoteAPIModel.class);

    static String connection_string = "jdbc:virtuoso://localhost:1111/CHARSET=UTF-8/log_enable=2";
    static String login = "dba";
    static String password = "dba";

    private static final String GET_AUTHOR_LABEL_BY_VIAF = StringUtils.join(
            new String[]{
                Prefixes.WIKIBASE, Prefixes.WD, Prefixes.WDT, Prefixes.RDFS, Prefixes.BD,
                "SELECT ?name ?translated WHERE {",
                "?name wdt:P214 '%s'", // a numerical value, ex: 50566653
                "SERVICE wikibase:label {",
                "bd:serviceParam wikibase:language '%s' .",
                "?name rdfs:label ?translated",
                "}",
                "}"
            }, "\n");

    private static final String GET_WIKIPEDIA_ABSTRACT_USING_LABEL = StringUtils.join(
            new String[]{
                Prefixes.ONTOLOGY, Prefixes.PROPERTY, Prefixes.RDFS,
                "SELECT ?name ?abstract WHERE {",
                "?name rdfs:label '%s'@%s.",
                "?name ontology:abstract ?abstract",
                "FILTER (LANG(?abstract)='%s')",
                "}"
            }, "\n");

    private static final String GET_WIKIPEDIA_ABSTRACT_USING_VIAF = StringUtils.join(
            new String[]{
                Prefixes.ONTOLOGY, Prefixes.PROPERTY, Prefixes.RDFS,
                "SELECT ?name ?abstract WHERE {",
                "?name <http://dbpedia.org/property/viaf> %s .",
                "?name ontology:abstract ?abstract",
                "FILTER (LANG(?abstract)='%s')",
                "}"
            }, "\n");

    private static final String GET_MULTIPLE_WIKIPEDIA_ABSTRACT_BY_VIAF = StringUtils.join(
            new String[]{
                Prefixes.ONTOLOGY, Prefixes.PROPERTY, Prefixes.RDFS,
                "SELECT ?name ?viaf ?abstract WHERE {",
                "VALUES ?viaf { %s }",
                "?name <http://dbpedia.org/property/viaf> ?viaf.",
                "?name ontology:abstract ?abstract .",
                "FILTER (LANG(?abstract)='en')",
                "}"
            }, "\n");

    private static final String GET_VIAF_FROM_HEBREW_AUTHORS = StringUtils.join(
            new String[]{
                Prefixes.WIKIBASE, Prefixes.WD, Prefixes.WDT, Prefixes.RDFS, Prefixes.BD,
                "SELECT ?s ?viaf ?nli ?enName ?heName WHERE {",
                "?s wdt:P31 wd:Q5 .",
                "?s wdt:P949 ?nli .",
                "?s wdt:P214 $viaf .",
                "SERVICE wikibase:label {",
                "bd:serviceParam wikibase:language 'he' .",
                "?s rdfs:label ?heName .",
                "}",
                "SERVICE wikibase:label {",
                "bd:serviceParam wikibase:language 'en' .",
                "?s rdfs:label ?enName .",
                "}",
                "}"
            }, "\n");

    /**
     * Runs a user provided SPARQL query on the Wikidata endpoint
     *
     * @param queryString The query string
     * @param sparqlServiceUrl The SPARQL endpoint ULR, if null or empty string,
     * will default to "https://query.wikidata.org/sparql"
     * @return the query results in a string
     */
    public String runQueryOnWikidata(String queryString, String sparqlServiceUrl) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }

        if (sparqlServiceUrl == null || sparqlServiceUrl.isEmpty()) {
            sparqlServiceUrl = "https://query.wikidata.org/sparql";
        }

        StringBuilder sb = new StringBuilder();
        try {
            Query query = QueryFactory.create(GET_VIAF_FROM_HEBREW_AUTHORS);
            QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlServiceUrl, query);
            ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                Iterator<String> iter = qs.varNames();
                sb.append(StringUtils.repeat("-", 50)).append("\n");
                while (iter.hasNext()) {
                    String field = iter.next();
                    sb.append(field).append(": ").append(qs.get(field)).append("\n");
                }
            }
        } catch (Exception ex) {
            logger.error("An exception has occured while processing the query", ex);
            return new StringBuilder()
                    .append("An exception has occured while processing the query")
                    .append("\n")
                    .append(ex)
                    .append("\n")
                    .toString();
        }
        return sb.toString();
    }

    /**
     *
     * @param viafId
     * @param language
     * @return
     */
    public String getAuthorLabelByViaf(String viafId, String language) {
        if (viafId == null || viafId.isEmpty()) {
            return null;
        }
        if (language == null || language.isEmpty()) {
            language = "en";
        }

        String queryString = String.format(GET_AUTHOR_LABEL_BY_VIAF, viafId, language);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/", query);
        String result = null;
        ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _name = qs.get("name");
            RDFNode _translated = qs.get("translated");
            logger.info("Label: " + _name + ", Translated: " + _translated);
            result = _translated.toString();
        }
        return result;
    }

    /**
     * Fetches (by label) from DBPedia the article abstract for an author in a
     * certain language
     *
     * @param author The queried author
     * @param language The language that abstract should be in (if string is
     * null or empty, will default to 'en')
     *
     * @return The article abstract text string
     */
    public String getWikipediaAbstractByName(String author, String language) {
        if (author == null || author.isEmpty()) {
            return null;
        }
        if (language == null || language.isEmpty()) {
            language = "en";
        }

        String queryString = String.format(GET_WIKIPEDIA_ABSTRACT_USING_LABEL, author, language, language);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        String result = null;
        ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _name = qs.get("name");
            RDFNode _abstract = qs.get("abstract");
            logger.info("Name: " + _name + ", Abstract: " + _abstract);
            result = _abstract.toString();
        }
        return result;
    }

    /**
     * Fetches (by viaf id) from DBPedia the article abstract for an author in a
     * certain language
     *
     * @param viafId The author's viaf ID
     * @param language The language that abstract should be in (if string is
     * null or empty, will default to 'en')
     *
     * @return The article abstract text string
     */
    public String getWikipediaAbstractByViafId(String viafId, String language) {
        if (viafId == null || viafId.isEmpty()) {
            return null;
        }
        if (language == null || language.isEmpty()) {
            language = "en";
        }

        String queryString = String.format(GET_WIKIPEDIA_ABSTRACT_USING_VIAF, viafId, language);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        String result = null;
        ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _name = qs.get("name");
            RDFNode _abstract = qs.get("abstract");
            logger.info("Name: " + _name + ", Abstract: " + _abstract);
            result = _abstract.toString();
        }
        return result;
    }

    /**
     * Fetches (by viaf id) from DBPedia the article abstract for an author in a
     * certain language
     *
     * @param authors
     * @param language The language that abstract should be in (if string is
     * null or empty, will default to 'en')
     *
     * @return Map of viaf as keys and abstracts as values
     */
    public Map<String, String> getMultipleWikipediaAbstractByViafIds(List<Author> authors, String language) {

        // buffer each remote query with up to 50 viaf ids per request
        if (authors.size() > 50) {
            List<String> stack = new ArrayList<>();
            Map<String, String> bufferedResult = new HashMap<>();

            for (Author author : authors) {
                if (author.getViafId() == null || author.getViafId().isEmpty()) {
                    continue;
                }
                stack.add(author.getViafId());
                if (stack.size() == 50) {
                    Map<String, String> temp = this.getMultipleWikipediaAbstractByViafIdsInner(stack, language);
                    bufferedResult.putAll(temp);
                    stack.clear();
                }
            }
            if (!stack.isEmpty()) {
                this.getMultipleWikipediaAbstractByViafIdsInner(stack, language);
                stack.clear();
            }
            return bufferedResult;
        } else {
            List<String> viafIds = authors.stream().map(a -> {
                return a.getViafId();
            }).collect(Collectors.toList());
            return this.getMultipleWikipediaAbstractByViafIdsInner(viafIds, language);
        }
    }

    /**
     *
     * @param viafIds
     * @param language
     * @return Map of viaf as keys and abstracts as values
     */
    private Map<String, String> getMultipleWikipediaAbstractByViafIdsInner(List<String> viafIds, String language) {
        if (viafIds == null || viafIds.isEmpty()) {
            return null;
        }
        if (language == null || language.isEmpty()) {
            language = "en";
        }

        String joinedViafs = StringUtils.join(viafIds, " ");

        String queryString = String.format(GET_MULTIPLE_WIKIPEDIA_ABSTRACT_BY_VIAF, joinedViafs, language);
        System.out.println("Query string: " + queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        Map<String, String> resultMap = new HashMap<>();
        ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            RDFNode _name = qs.get("name");
            RDFNode _viaf = qs.get("viaf");
            RDFNode _abstract = qs.get("abstract");
            String viaf = RDFUtils.spliceLiteralType(_viaf.toString());
            String abs = RDFUtils.spliceLiteralLaguageTag(_abstract.toString());
            resultMap.put(viaf, abs);
        }
        return resultMap;
    }

    /**
     * Fetches from local VOS all authors who have a VIAF ID and an NLI ID.
     * These response is built in the following format (columns per row): 1. s -
     * Subject 2. sLabel - Subject label 3. viaf - VIAF ID 4. nli - NLI ID
     *
     * @return A list of Author objects
     */
    public List<Author> getAuthorsWithVIAF() {
        List<Author> authors = null;
        try {
            VirtGraph graph = new VirtGraph(connection_string, login, password);

            Query query = QueryFactory.create(GET_VIAF_FROM_HEBREW_AUTHORS);
            VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, graph);
            ResultSet rs = vqe.execSelect();
            authors = new ArrayList<>();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                RDFNode _s = qs.get("s");
                RDFNode _viaf = qs.get("viaf");
                RDFNode _nli = qs.get("nli");
                RDFNode _enName = qs.get("enName");
                RDFNode _heName = qs.get("heName");
                Author author = new Author();
                author.setWikipediaUri(_s.toString());
                author.setViafId(_viaf.toString());
                author.setNliId(_nli.toString());
                author.setNames(new HashMap<>());
                author.getNames().put("en", _enName.toString());
                author.getNames().put("he", _heName.toString());
                authors.add(author);
            }
        } catch (Exception ex) {
            logger.error("Exception while marshalling authors list from local VOS RDF", ex);
        }
        return authors;
    }

    /**
     * Fetches from Wikidata all authors who have a VIAF ID and an NLI ID.
     * Response is built in the following format (columns per row): 1. s -
     * Subject 2. viaf - VIAF ID 3. nli - NLI ID 4. enName - Author full name in
     * English (defaults to Q id is none was provided) 5. heName - Author full
     * name in Hebrew (defaults to Q id is none was provided)
     *
     * @return A list of Author objects
     */
    public List<Author> getAuthorsWithVIAFRemote() {

        List<Author> authors = null;
        try {
            Query query = QueryFactory.create(GET_VIAF_FROM_HEBREW_AUTHORS);

            QueryExecution qe = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
            ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
            authors = new ArrayList<>();
            while (rs.hasNext()) {
                QuerySolution qs = rs.nextSolution();
                RDFNode _s = qs.get("s");
                RDFNode _viaf = qs.get("viaf");
                RDFNode _nli = qs.get("nli");
                RDFNode _enName = qs.get("enName");
                RDFNode _heName = qs.get("heName");
                Author author = new Author();
                author.setWikipediaUri(_s.toString());
                author.setViafId(_viaf.toString());
                author.setNliId(_nli.toString());
                author.setNames(new HashMap<>());
                author.getNames().put("en", _enName.toString());
                author.getNames().put("he", _heName.toString());
                authors.add(author);
            }
        } catch (Exception ex) {
            logger.error("Exception while marshalling authors list from remote SPARQL API", ex);
        }
        return authors;
    }
}
