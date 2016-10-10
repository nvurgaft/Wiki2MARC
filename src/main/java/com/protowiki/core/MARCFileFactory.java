package com.protowiki.core;

import com.protowiki.app.JobExecutorService;
import com.protowiki.beans.Author;
import com.protowiki.beans.Record;
import com.protowiki.model.WikipediaRemoteAPIModel;
import com.protowiki.model.AuthorModel;
import com.protowiki.model.WikidataRemoteAPIModel;
import com.protowiki.reports.ProcessReportContext;
import com.protowiki.reports.RecordSummary;
import com.protowiki.reports.ReportGenerator;
import com.protowiki.utils.RDFUtils;
import com.protowiki.utils.RecordSAXParser;
import static com.protowiki.utils.Validators.isBlank;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.swing.text.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class MARCFileFactory {

    public static Logger logger = LoggerFactory.getLogger(MARCFileFactory.class);

    private boolean useLocalDatabase;
    private final AuthorModel authorModel;

    /**
     * MARCFileFactory Constructor
     *
     * @param useLocalDatabase flag indicates should data fetched remotely be
     * saved on a local Virtuoso database, by being false, no data connection
     * will be made to the DB.
     */
    public MARCFileFactory(Boolean useLocalDatabase) {

        if (this.useLocalDatabase) {
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
        if (isBlank(filePath)) {
            return 1;
        }

        List<Author> authors;
        try {

            authors = this.parseMARCFileForAuthors(filePath);
            logger.info("Parsed out {} authors", authors.size());
            authors.stream().forEach(System.out::println);

            logger.info("Injecting Wikipedia labels");
            this.injectWikipediaLabels(authors);

            logger.info("Injecting Wikipedia abstracts");
            List<String> abstractList = this.injectWikipediaAbstracts(authors);

            this.updateMARCFile(filePath, authors);

            if (useLocalDatabase && authorModel != null) {
                logger.info("Inserting authors into database");
                authorModel.insertAuthorsIntoDB(authors);
            }

            List<RecordSummary> recordSummeries = new ArrayList<>();

            authors.stream().forEach(author -> {
                RecordSummary articleSummery = new RecordSummary();

                boolean gotEnAbstract = author.getNames().get("en") == null ? false : !author.getNames().get("en").isEmpty();
                boolean gotHeAbstract = author.getNames().get("he") == null ? false : !author.getNames().get("he").isEmpty();
                String articleStatus = (author.getNames() == null || author.getWikipediaArticleAbstract() == null) ? "FAILED" : "SUCCESS";

                articleSummery
                        .setLabelEn(author.getNames().get("en"))
                        .setLabelHe(author.getNames().get("he"))
                        .setViaf(author.getViafId())
                        .setRecordId(author.getMarcId())
                        .setFoundEnglishAbstract(gotEnAbstract)
                        .setFoundHebrewAbstract(gotHeAbstract)
                        .setStatus(articleStatus);

                articleSummery.setDateCreated(new Date());

                recordSummeries.add(articleSummery);
            });

            ReportGenerator reportGenerator = new ReportGenerator();
            String now = new DateFormatter().valueToString(new Date());
            String reportFileName = "wiki2marc-report";

            ProcessReportContext c = new ProcessReportContext(recordSummeries, reportFileName, now);
            reportGenerator.generateBasicReport(c, reportFileName);

            this.openResults(reportFileName);

        } catch (Exception ex) {
            logger.error("Exception while running mapping process", ex);
            return 1;
        }
        return 0;
    }

    private void openResults(String filePath) {

        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        try {
            // find the system's default program to open the file
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                String cmd = "rundll32 url.dll,FileProtocolHandler " + new File(filePath).getCanonicalPath();
                Runtime.getRuntime().exec(cmd);
            } else {
                Desktop.getDesktop().edit(new File(filePath));
            }
        } catch (IOException ioex) {
            logger.error("IOException", ioex);
        }
    }

    /**
     * Load the MARC file to memory, parses it, and return a list of Authors
     * objects
     *
     * @param filePath String path to the MARC file
     * @return List of Author objects
     */
    private List<Author> parseMARCFileForAuthors(String filePath) throws Exception {

        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        RecordSAXParser parser = new RecordSAXParser();
        DataTransformer transformer = new DataTransformer();

        List<Record> records = parser.parseXMLFileForRecords(filePath);
        logger.info("records => {}", records.size());
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
            logger.info("Key: {}, Value: {}", key, absMap.get(key));
            authorModel.insertAuthorsViafAndAbstracts(key, absMap.get(key));
        });
    }

    private List<Author> injectWikipediaLabels(List<Author> authors) throws Exception {

        if (isBlank(authors)) {
            return null;
        }

        WikidataRemoteAPIModel wikidata = new WikidataRemoteAPIModel();

        List<Map<String, String>> nameLists = authors.stream()
                .map(author -> {
                    return wikidata.getMultipleAuthorLabelsByViaf(author.getViafId());
                })
                .filter(result -> {
                    return result != null;
                })
                .collect(Collectors.toList());

        nameLists.stream().forEach(nameMap -> {
            nameMap.keySet().stream()
                    .forEach(key -> {
                        nameMap.put(key, RDFUtils.spliceLiteralLaguageTag(nameMap.get(key)));
                    });
        });

        return authors;
    }

    private List<String> injectWikipediaAbstracts(List<Author> authors) throws Exception {

        if (isBlank(authors)) {
            return null;
        }

        WikipediaRemoteAPIModel wikipedia = new WikipediaRemoteAPIModel();

        // load the callable jobs
        List<Callable<?>> asyncTasks = authors.stream()
                .flatMap(author -> {
                    return author.getNames().keySet().stream()
                            .map(lang -> callAbstract(wikipedia, author, lang));
                })
                .collect(Collectors.toList());

        // execute the jobs and wait for all of them to complete
        List<Future<?>> futures = JobExecutorService.submitMultipleJobs(asyncTasks);
        List<String> results = futures.stream()
                .map(future -> {
                    Object f = null;
                    try {
                        f = future.get(10, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        logger.error("Failed getting result from future job", e);
                    }
                    return (String) f;
                })
                .collect(Collectors.toList());

        return results;
    }

    protected Callable<?> callAbstract(WikipediaRemoteAPIModel api, Author author, String lang) {
        return () -> {
            String name = author.getNames().get(lang);
            String abs = api.getAbstractByArticleName(name, lang);
            logger.info("abstract: {}", abs);
            if (author.getWikipediaArticleAbstract() == null) {
                author.setWikipediaArticleAbstract(new HashMap<>());
            }
            return author.getWikipediaArticleAbstract().put(lang, abs);
        };
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
    private boolean updateMARCFile(String file, List<Author> authors) throws Exception {
        DataTransformer transformer = new DataTransformer();

//        logger.debug("connect remotly and query abstracts for these viaf ids");
//        Map<String, String> rdfList = authorModel.getAuthorsViafAndAbstracts();
//        Map<String, Author> viafAuthorsMap = authors.stream()
//                .collect(Collectors.toMap(author -> author.getViafId(), author -> author));
        Map<String, Author> viafAuthorsMap = new HashMap<>();
        authors.forEach(author -> {
            if (author != null || author.getViafId()!=null || !author.getViafId().isEmpty()) {
                viafAuthorsMap.put(author.getViafId(), author);
            }
        });

        logger.debug("generate the updated MARC file");
        return transformer.dynamicallyGenerateMARCXMLFile(file, viafAuthorsMap);
    }
}
