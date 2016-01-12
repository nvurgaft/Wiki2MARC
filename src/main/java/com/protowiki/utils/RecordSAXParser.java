package com.protowiki.utils;

import com.protowiki.beans.Author;
import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import com.protowiki.beans.Subfield;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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

        if (fileName == null || fileName.isEmpty()) {
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

    public List<Author> transformRecordsListToAuthors(List<Record> recordsList) {

        if (recordsList == null || recordsList.isEmpty()) {
            return null;
        }

        List<Author> authorsList = recordsList.stream()
                .map(r -> {
                    Author author = new Author();
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

}
