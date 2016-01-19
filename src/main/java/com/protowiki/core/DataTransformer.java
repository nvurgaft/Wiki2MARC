
package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Controlfield;
import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import com.protowiki.beans.Subfield;
import com.protowiki.db.RDFConnectionManager;
import com.protowiki.entities.RDFStatement;
import com.protowiki.model.QueryHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.jena.driver.VirtGraph;

/**
 *
 * @author Nick
 */
public class DataTransformer {
    
    public static Logger logger = LoggerFactory.getLogger(DataTransformer.class);
    
    /**
     *  Takes a list of Record objects and transforms it into a list of Author objects
     * 
     * @param recordsList List of parsed records (MARC documents)
     * @return List of authors
     */
    public List<Author> transformRecordsListToAuthors(List<Record> recordsList) {

        if (recordsList == null || recordsList.isEmpty()) {
            return null;
        }

        List<Author> authorsList = recordsList.stream()
                .map(r -> {
                    Author author = new Author();
                    
                    for (Controlfield cf : r.getControlfields()) {
                        if (cf.getTag().equals("001")) {
                            author.setMarcId(cf.getValue());
                            break;
                        }
                    }
                    
                    author.setNames(new HashMap<>());
                    for (Datafield df : r.getDatafields()) {
                        switch (df.getTag()) {
                            case "100":
                            case "400":
                                String lang = "",
                                 name = "";
                                for (Subfield sf : df.getSubfields()) {
                                    if (sf.getCode().equals("a")) {
                                        name = sf.getValue();
                                    } else if (sf.getCode().equals("9")) {
                                        lang = sf.getValue();
                                    } else if (sf.getCode().equals("d")) {
                                        author.setYears(sf.getValue());
                                    }
                                }
                                if (!name.isEmpty() && !lang.isEmpty()) {
                                    author.getNames().put(lang, name);
                                }
                                break;
                            case "901":
                                for (Subfield sf : df.getSubfields()) {
                                    if (sf.getCode().equals("a")) {
                                        author.setViafId(sf.getValue());
                                        break;
                                    }
                                }
                                break;
                            default:

                        }
                    }
                    return author;
                })
                .collect(Collectors.toList());

        return authorsList;
    }
    
    /**
     *  Attempts to insert an Author RDF
     * 
     * @param s Subject 
     * @param p Predicate
     * @param o Object
     * @return True if statement was successfully inserted to RDF DB
     */
    public boolean insertAuthorIntoDB(String s, String p, String o) {
        VirtGraph g = new RDFConnectionManager("http://testdb").getGraphConnection();
        QueryHandler handler = new QueryHandler(g, "http://testdb");
        
        return handler.insertStatement(s, p, o);
    }
    
    /**
     *  Attempts to batch insert an Author RDF
     * 
     * @param authors List of Author objects
     */
    public void batchInsertAuthorIntoDB(List<Author> authors) {
        VirtGraph g = new RDFConnectionManager("http://testdb").getGraphConnection();
        QueryHandler handler = new QueryHandler(g, "http://testdb");
        
        authors.stream().forEach(author -> {
            List<RDFStatement> queryStatement = new ArrayList<>();
            queryStatement.add(new RDFStatement(author.getWikipediaUri(), "rdf:viaf", author.getViafId()));           
            author.getNames().keySet().stream().forEach(key -> {
                queryStatement.add(new RDFStatement(author.getWikipediaUri(), "rdf:"+key+ "Name", author.getNames().get(key)));
            });
            handler.batchInsertStatements(queryStatement);
        });
    }
}
