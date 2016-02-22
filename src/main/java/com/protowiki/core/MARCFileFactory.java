package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.model.WikipediaRemoteAPIModel;
import com.protowiki.model.AuthorModel;
import com.protowiki.model.WikidataRemoteAPIModel;
import com.protowiki.utils.RecordSAXParser;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class MARCFileFactory {

    public static Logger logger = LoggerFactory.getLogger(MARCFileFactory.class);

    /**
     *  Runs the main data mapping process
     * 
     * @param filePath
     * @return 0 if the process was successful, otherwise -1
     */
    public int runProcess(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return -1;
        }

        try {
            this.fetchRemoteData(filePath);
            this.generateNewFile(filePath);
        } catch (Exception ex) {
            logger.error("Exception while running mapping process", ex);
            return -1;
        }
        return 0;
    }

    /**
     *  Fetches remote wikipedia abstracts using the MARC XML file viaf id's
     * @param filePath the MARC XML file path
     */
    private void fetchRemoteData(String filePath) {
        
        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer transformer = new DataTransformer();
        
        List<Record> records = parser.parseXMLFileForRecords(filePath);
        List<Author> authorsList = transformer.transformRecordsListToAuthors(records);
        
        // scan the authors list and get a list of viaf ids
        List<String> viafs = authorsList.stream().filter(a -> {
            return a.getViafId() != null;
        }).map(a -> {
            return a.getViafId().trim();
        }).collect(Collectors.toList());
        
        // connect remotly and query abstracts for these viaf ids
        WikidataRemoteAPIModel wikidataRemoteApi = new WikidataRemoteAPIModel();
        Map<String, String> absMap = wikidataRemoteApi.getMultipleWikipediaAbstractByViafIds(viafs, "en"); // map<viaf, abstract>
        
        List<String> hebrewNames = authorsList.stream().map(a-> {
            return a.getNames().get("he");
        }).collect(Collectors.toList());
        
        WikipediaRemoteAPIModel wikipediaRemoteApi = new WikipediaRemoteAPIModel();
        Map<String, String> heAbstracts = wikipediaRemoteApi.getAbstractsByArticleNames("he", hebrewNames); // list<abstracts>

        
        // insert locally
        AuthorModel authorModel = new AuthorModel();
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
    private boolean generateNewFile(String filePath) {
        DataTransformer transformer = new DataTransformer();

        logger.debug("connect remotly and query abstracts for these viaf ids");
        AuthorModel authorModel = new AuthorModel();
        Map<String, String> rdfList = authorModel.getAuthorsViafAndAbstracts();

        logger.debug("generate the updated MARC file");
        return transformer.generateMARCXMLFile(filePath, rdfList);
    }
}
