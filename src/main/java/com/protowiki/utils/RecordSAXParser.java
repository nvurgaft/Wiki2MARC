package com.protowiki.utils;

import com.protowiki.beans.Record;
import static com.protowiki.utils.Validators.isBlank;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Nick
 */
public class RecordSAXParser {

    public static Logger logger = LoggerFactory.getLogger(RecordSAXParser.class);
    
    /**
     * Provided an XML file, this method invokes a native Java SAX parser
     * handler implementation and maps the data unto a list of Record objects.
     * 
     * @param file
     * @return 
     */
    public List<Record> parseXMLFileForRecords(File file) {
        return this.parseXMLFileForRecords(file.getAbsolutePath());
    }

    /**
     * Provided an XML file path, this method invokes a native Java SAX parser
     * handler implementation and maps the data unto a list of Record objects.
     *
     * @param fileName
     * @return List of records
     */
    public List<Record> parseXMLFileForRecords(String fileName) {

        if (isBlank(fileName)) {
            return null;
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        RecordHandler handler = new RecordHandler();
        try {
            InputStream xmlInput = new FileInputStream(fileName);
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlInput, handler);
        } catch (ParserConfigurationException | SAXException | IOException err) {
            logger.error("Exception while parsing file " + fileName, err);
        }
        return handler.getRecords();
    }

}
