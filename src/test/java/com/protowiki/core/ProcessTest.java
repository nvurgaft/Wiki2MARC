package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.model.AuthorModel;
import com.protowiki.model.WikidataRemoteAPIModel;
import com.protowiki.model.WikipediaRemoteAPIModel;
import com.protowiki.utils.RecordSAXParser;
import java.io.File;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * These tests act as Integration tests between the various components in this
 * application and represent the eventual business logic. These tests should be
 * run separately from the build process.
 *
 * @author Nick
 */
@Ignore
public class ProcessTest {

    public static Logger logger = LoggerFactory.getLogger(ProcessTest.class);

    private static final String FILE_PATH = "//content//";

    @Test
    public void testWhereAmI() {
        File file = new File("./src/test/");
        File[] files = file.listFiles();
        for (File file1 : files) {
            logger.info("" + file1);
        }
    }

    @Test
    public void testFetchRemoteAbstracts() {

        String fileName = "authbzi.xml";
        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer transformer = new DataTransformer();

        List<Record> records = parser.parseXMLFileForRecords("./src/test/" + fileName);
        List<Author> authorsList = transformer.transformRecordsListToAuthors(records);

        logger.info("connect remotly and query abstracts for these viaf ids");
        WikidataRemoteAPIModel remoteSparqlApi = new WikidataRemoteAPIModel();
        Map<String, Author> absMap = remoteSparqlApi.getMultipleWikipediaAbstractByViafIds(authorsList, "en");
        
        WikipediaRemoteAPIModel remoteJsonApi = new WikipediaRemoteAPIModel();
        //remoteJsonApi.getAbstractByArticleName(fileName, fileName)
        
        logger.info("inserting locally " + absMap.keySet().size() + " keys");
        AuthorModel authorModel = new AuthorModel();

        for (String key : absMap.keySet()) {
            System.out.println(absMap.get(key));
            authorModel.insertAuthorsViafAndAbstracts(key, absMap.get(key).getWikipediaArticleAbstract().get("en"));
        }
    }

    @Test
    public void testRunProcess() {

        String fileName = "authbzi.xml";
        MARCFileFactory factory = new MARCFileFactory();
        int result = factory.runProcess(FILE_PATH + fileName);

        logger.debug("Result: " + result);
    }

    @Test
    public void testEntireProcess() {

        String fileName = "authbzi.xml";
        DataTransformer optimus = new DataTransformer();

        logger.info("connect remotly and query abstracts for these viaf ids");
        AuthorModel authorModel = new AuthorModel();
        Map<String, String> rdfList = authorModel.getAuthorsViafAndAbstracts();

        logger.info("generate the updated MARC file");
        boolean result = optimus.generateMARCXMLFile(FILE_PATH, rdfList);
        assertTrue("Should be successful", result);
    }
}
