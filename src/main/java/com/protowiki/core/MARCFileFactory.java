package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.model.WikipediaRemoteAPIModel;
import com.protowiki.model.AuthorModel;
import com.protowiki.model.WikidataRemoteAPIModel;
import com.protowiki.utils.RDFUtils;
import com.protowiki.utils.RecordSAXParser;
import java.util.HashMap;
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

    private Boolean useLocalDatabase;
    private final AuthorModel authorModel;

    /**
     * MARCFileFactory Constructor
     *
     * @param useLocalDatabase flag indicates should data fetched remotely be
     * saved on a local Virtuoso database, by being false, no data connection
     * will be made to the DB.
     */
    public MARCFileFactory(Boolean useLocalDatabase) {
        if (useLocalDatabase) {
            authorModel = new AuthorModel();
        } else {
            authorModel = null;
        }
    }

    /**
     * ### Runs the main data mapping process ###
     *
     * @param filePath
     * @return 0 if the process was successful, otherwise 1
     */
    public int runProcess(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return 1;
        }

        List<Author> authors;
        try {

            authors = this.parseMARCFileForAuthors(filePath);

//            this.obtainAbstractsViaSPARQLAPI(authors);
//            this.obtainAbstractsViaWikipediaAPI(authors);
//            this.generateNewFile(filePath);
            logger.info(String.format("Parsed out %d authors", authors.size()));
            authors.stream().forEach(a -> System.out.println(a));

            logger.info("injectWikipediaLabels");
            this.injectWikipediaLabels(authors);

            logger.info("injectWikipediaAbstracts");
            this.injectWikipediaAbstracts(authors);

            this.updateMARCFile(filePath, authors);

            if (useLocalDatabase && authorModel != null) {
                logger.info("Inserting authors into database");
                authorModel.insertAuthorsIntoDB(authors);
            }

        } catch (Exception ex) {
            logger.error("Exception while running mapping process", ex);
            return 1;
        }
        return 0;
    }

    /**
     * Load the MARC file to memory, parses it, and return a list of Authors
     * objects
     *
     * @param filePath String path to the MARC file
     * @return List of Author objects
     */
    private List<Author> parseMARCFileForAuthors(String filePath) throws Exception {

        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer transformer = new DataTransformer();

        List<Record> records = parser.parseXMLFileForRecords(filePath);
        logger.info("records => " + records.size());
        return transformer.transformRecordsListToAuthors(records);
    }

    /**
     *
     * @param authorsList
     */
    @Deprecated
    private void obtainAbstractsViaWikipediaAPI(List<Author> authorsList) throws Exception {

        // connect remotly and query abstracts for these article names
        WikipediaRemoteAPIModel wikipediaRemoteApi = new WikipediaRemoteAPIModel();
        Map<String, String> absMap = wikipediaRemoteApi.getMultipleAbstractsByAuthors(authorsList, "he"); // list<viaf, abstracts>
        // insert locally
        absMap.keySet().stream().forEach((key) -> {
            logger.info("Key: " + key + ", Value: " + absMap.get(key));
            authorModel.insertAuthorsViafAndAbstracts(key, absMap.get(key));
        });
    }

    private List<Author> injectWikipediaLabels(List<Author> authors) throws Exception {

        WikidataRemoteAPIModel wikidata = new WikidataRemoteAPIModel();

        for (Author author : authors) {
            if (author.getViafId() != null && !author.getViafId().isEmpty()) {
                logger.info("Serving viaf: " + author.getViafId());
                Map<String, String> names = wikidata.getMultipleAuthorLabelsByViaf(author.getViafId());
                for (String key : names.keySet()) {
                    String value = names.get(key);
                    author.getNames().put(key, RDFUtils.spliceLiteralLaguageTag(value));
                }
            } else {
                logger.info("No viaf id found for this article");
            }
        }

        return authors;
    }

    private List<Author> injectWikipediaAbstracts(List<Author> authors) throws Exception {

        WikipediaRemoteAPIModel wikipedia = new WikipediaRemoteAPIModel();

        for (Author author : authors) {

            for (String lang : author.getNames().keySet()) {
                String name = author.getNames().get(lang);
                String abs = wikipedia.getAbstractByArticleName(name, lang);
                logger.info("abstract: " + abs);

                if (author.getWikipediaArticleAbstract() == null) {
                    author.setWikipediaArticleAbstract(new HashMap<String, String>());
                }

                author.getWikipediaArticleAbstract().put(lang, abs);
            }
        }
        return authors;
    }

    /**
     * Fetches remote Wikipedia abstracts using the MARC XML file viaf id's
     *
     * @param filePath the MARC XML file path
     */
    @Deprecated
    private void obtainAbstractsViaSPARQLAPI(List<Author> authorsList) throws Exception {

        // connect remotly and query abstracts for these viaf ids
        WikidataRemoteAPIModel wikidataRemoteApi = new WikidataRemoteAPIModel();
        Map<String, Author> absMap = wikidataRemoteApi.getMultipleWikipediaAbstractByViafIds(authorsList, "en"); // map<viaf, abstract>

        // insert locally
        absMap.keySet().stream().forEach((key) -> {
            authorModel.insertAuthorsViafAndAbstracts(key, absMap.get(key).getWikipediaArticleAbstract().get("en"));
        });
    }

    /**
     * Generated an updated XML file using the database data bound to the old
     * one
     *
     * @param filePath the MARC XML file path
     * @return true if file generation was successful
     */
//    @Deprecated
//    private boolean generateNewFile(String filePath) throws Exception {
//        DataTransformer transformer = new DataTransformer();
//
//        logger.debug("connect remotly and query abstracts for these viaf ids");
//        List<RDFStatement> rdfList = authorModel.getAuthorsViafAndAbstracts();
//
//        logger.debug("generate the updated MARC file");
//        return transformer.generateMARCXMLFile(filePath, rdfList);
//    }
    private boolean updateMARCFile(String file, List<Author> authors) throws Exception {
        DataTransformer transformer = new DataTransformer();

//        logger.debug("connect remotly and query abstracts for these viaf ids");
//        Map<String, String> rdfList = authorModel.getAuthorsViafAndAbstracts();
        Map<String, Author> viafAuthorsMap = new HashMap<>();
        for (Author author : authors) {
            viafAuthorsMap.put(author.getViafId(), author);
        }

        logger.debug("generate the updated MARC file");
        return transformer.dynamicallyGenerateMARCXMLFile(file, viafAuthorsMap);
    }
}
