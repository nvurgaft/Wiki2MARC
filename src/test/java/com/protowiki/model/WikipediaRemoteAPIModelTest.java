package com.protowiki.model;

import com.protowiki.beans.Author;
import com.protowiki.utils.ApplicationProperties;
import com.protowiki.values.Values;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class WikipediaRemoteAPIModelTest {

    public static Logger logger = LoggerFactory.getLogger(WikipediaRemoteAPIModelTest.class);

    ApplicationProperties applicationProperties = new ApplicationProperties(Values.APP_PROPS_NAME);

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        logger.info("Before method: {}", testName.getMethodName());
    }

    @After
    public void tearDown() {
        logger.info("After method: {}", testName.getMethodName());
    }

    /**
     * Test of getAbstractByArticleName method, of class
     * WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractByArticleNameEn() {

        String articleName = "Big_Mac";
        String language = "en";
        WikipediaRemoteAPIModel instance = new WikipediaRemoteAPIModel();
        String result = instance.getAbstractByArticleName(articleName, language);
        logger.info("Result: " + result);
    }

    @Test
    public void testStripString() {

        String result = "koby\n\n\n".replace('\n', ' ');

        logger.info("result: {}", result);

    }

    /**
     * Test of getAbstractByArticleName method, of class
     * WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractByArticleNameHe() {

        String articleName = "תפוח אדמה";
        String language = "he";
        WikipediaRemoteAPIModel instance = new WikipediaRemoteAPIModel();
        String result = instance
                .getAbstractByArticleName(articleName, language)
                .replace("\\n", "")
                .replace("\n", " ")
                .replace('\r', ' ')
                .replace("\\", "")
                .trim();
        result = trimQuotationMarks(result);

        File file = new File("temp1.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(result);
            writer.flush();
        } catch (Exception e) {
            logger.error("Exception", e);
        }

        logger.info("Result: {}", result);
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
                Runtime.getRuntime().exec(cmd);
            } else {
                Desktop.getDesktop().edit(file);
            }
        } catch (IOException ioex) {
            logger.error("IOException", ioex);
        }
    }

    public String trimQuotationMarks(String str) {
        if (str.charAt(0) == '\"') {
            str = str.substring(1, str.length() - 1);
        }
        if (str.charAt(str.length() - 1) == '\"') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    /**
     * Test of getAbstractsByArticleNames method, of class
     * WikipediaRemoteAPIModel.
     */
    @Test
    public void testGetAbstractsByArticleNames() {

        Author a1 = new Author();
        a1.setNames(new HashMap<>());
        a1.getNames().put("en", "Mark_Twain");
        a1.setViafId("1");

        Author a2 = new Author();
        a2.setNames(new HashMap<>());
        a2.getNames().put("en", "Steve_Jobs");
        a2.setViafId("2");

        List<Author> authors = Arrays.asList(a1, a2);
        WikipediaRemoteAPIModel instance = new WikipediaRemoteAPIModel();
        Map<String, String> result = instance.getMultipleAbstractsByAuthors(authors, "en");
        result.keySet().forEach(key -> {
            logger.info("Key: {}, Value: {}", key, result.get(key));
        });
    }

    @Test
    public void testOpenXMLFile() {
        
        try {
            boolean shouldOpen = applicationProperties.getBoolean("attempt_to_open_result", true);
            if (shouldOpen) {
                logger.debug("Open");
                Desktop.getDesktop().open(new File("C:\\CYGWIN_SYSLOG.TXT"));
            } else {
                logger.debug("Dont open");
            }
        } catch (IOException ex) {
            logger.error("IOException", ex);
        }
    }

}
