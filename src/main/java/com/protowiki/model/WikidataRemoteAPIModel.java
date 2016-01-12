package com.protowiki.model;

import java.sql.Connection;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Nick
 */
public class WikidataRemoteAPIModel {

    private static final String GET_WIKIPEDIA_ABSTRACT_FOR_LABEL = StringUtils.join(
            new String[]{
                "PREFIX ontology: <http://dbpedia.org/ontology/>",
                "PREFIX property: <http://dbpedia.org/property/>",
                "SELECT ?name ?abstract WHERE {",
                "?name rdfs:label \"Mark Twain\"@en.",
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
                "SELECT ?s ?sLabel ?viaf ?nli WHERE {",
                "?s wdt:P31 wd:Q5 .",
                "?s wdt:P949 ?nli .",
                "?s wdt:P214 $viaf",
                "SERVICE wikibase:label {",
                "bd:serviceParam wikibase:language \"he\" .",
                "}",
                "}"
            }, "\n");
    
    public void getWikipediaAbstract(Connection conn, String label) {
        if (conn==null || label==null || label.isEmpty()) return;
        
        // stub
    }
    
    public void getViafFromAuthors(Connection conn) {
        if (conn==null) return;
        
        // stub
    }
}
