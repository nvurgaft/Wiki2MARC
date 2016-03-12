package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.model.WikipediaRemoteAPIModel;
import com.protowiki.model.AuthorModel;
import com.protowiki.model.WikidataRemoteAPIModel;
import com.protowiki.utils.RecordSAXParser;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class MARCFileFactory {

    public static Logger logger = LoggerFactory.getLogger(MARCFileFactory.class);
    
    AuthorModel authorModel;
    
    public MARCFileFactory() {
        authorModel = new AuthorModel();
    }

    /**
     *  Runs the main data mapping process
     * 
     * @param filePath
     * @return 0 if the process was successful, otherwise 1
     */
    public int runProcess(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return 1;
        }
        
        List<Author> authorsList = null;
        try {
            
            authorsList = this.parseMARCFileForAuthors(filePath);
            
            this.obtainAbstractsViaSPARQLAPI(authorsList);
            this.obtainAbstractsViaWikipediaAPI(authorsList);
            this.generateNewFile(filePath);
        } catch (Exception ex) {
            logger.error("Exception while running mapping process", ex);
            return 1;
        }
        return 0;
    }
    
    /**
     * Load the MARC file to memory
     * @param filePath
     * @return 
     */
    private List<Author> parseMARCFileForAuthors(String filePath) throws Exception {

        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer transformer = new DataTransformer();
        
        List<Record> records = parser.parseXMLFileForRecords(filePath);
        return transformer.transformRecordsListToAuthors(records);
    }
    
    /**
     * 
     * @param authorsList 
     */
    private void obtainAbstractsViaWikipediaAPI(List<Author> authorsList) throws Exception {
        
        // connect remotly and query abstracts for these article names
        WikipediaRemoteAPIModel wikipediaRemoteApi = new WikipediaRemoteAPIModel();
        Map<String, String> absMap = wikipediaRemoteApi.getAbstractsByArticleNames(authorsList, "he"); // list<viaf, abstracts>
        // insert locally
        absMap.keySet().stream().forEach((key) -> {
            logger.info("Key: " + key + ", Value: " + absMap.get(key));
            authorModel.insertAuthorsViafAndAbstracts(key, absMap.get(key));
        });
    }

    /**
     *  Fetches remote Wikipedia abstracts using the MARC XML file viaf id's
     * @param filePath the MARC XML file path
     */
    private void obtainAbstractsViaSPARQLAPI(List<Author> authorsList) throws Exception {
        
        // connect remotly and query abstracts for these viaf ids
        WikidataRemoteAPIModel wikidataRemoteApi = new WikidataRemoteAPIModel();
        Map<String, String> absMap = wikidataRemoteApi.getMultipleWikipediaAbstractByViafIds(authorsList, "en"); // map<viaf, abstract>
        
        // insert locally
        absMap.keySet().stream().forEach((key) -> {
            authorModel.insertAuthorsViafAndAbstracts(key, absMap.get(key));
        });
    }

    /**
     *  Generated an updated XML file using the database data bound to the 
     *  old one
     * @param filePath the MARC XML file path
     * @return true if file generation was successful
     */
    private boolean generateNewFile(String filePath) throws Exception {
        DataTransformer transformer = new DataTransformer();

        logger.debug("connect remotly and query abstracts for these viaf ids");
        Map<String, String> rdfList = authorModel.getAuthorsViafAndAbstracts();

        logger.debug("generate the updated MARC file");
        return transformer.generateMARCXMLFile(filePath, rdfList);
    }
}
