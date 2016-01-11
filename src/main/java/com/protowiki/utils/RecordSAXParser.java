package com.protowiki.utils;

import com.protowiki.beans.Record;
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

    private static Logger logger = LoggerFactory.getLogger(RecordSAXParser.class);

    public List<Record> parseXMLFileForRecords(String fileName) {
        
        if (fileName==null || fileName.isEmpty()) return null;
        
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
