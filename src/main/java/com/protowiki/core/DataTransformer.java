package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Controlfield;
import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import com.protowiki.beans.Subfield;
import com.protowiki.values.MARCIdentifiers;
import com.protowiki.model.WikipediaRemoteAPIModel;
import com.protowiki.utils.ApplicationProperties;
import static com.protowiki.utils.Validators.isBlank;
import com.protowiki.values.Values;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Nick
 */
public class DataTransformer {

    public static Logger logger = LoggerFactory.getLogger(DataTransformer.class);

    ApplicationProperties applicationProperties = new ApplicationProperties(Values.APP_PROPS_NAME);

    /**
     * Takes a list of Record objects and transforms it into a list of Author
     * objects
     *
     * @param recordsList List of parsed records (MARC documents)
     * @return List of authors
     */
    public List<Author> transformRecordsListToAuthors(List<Record> recordsList) {

        if (isBlank(recordsList)) {
            return null;
        }

        return recordsList.stream().map(r -> {
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
                            switch (sf.getCode()) {
                                case "a":
                                    name = sf.getValue();
                                    break;
                                case "9":
                                    switch (sf.getValue()) {
                                        case "lat":
                                            lang = "en";
                                            break;
                                        case "heb":
                                            lang = "he";
                                            break;
                                        default:
                                            logger.warn("Language [{}] is not supported", sf.getValue());
                                    }
                                    break;
                                case "d":
                                    author.setYears(sf.getValue());
                                    break;
                                default:
                                    logger.warn("XML code [{}] is not supported", sf.getValue());
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
        }).collect(Collectors.toList());
    }

    /**
     * Goes over the articleAbstracts list and appends the abstracts as property
     * 999 on the MARX XML
     *
     * @param file the XML MARC file
     * @param articleAbstracts the abstracts as a list of strings
     * @return the modified XML MARC file
     */
    public boolean generateMARCXMLFile(File file, Map<String, String> articleAbstracts) {
        return this.generateMARCXMLFile(file.getAbsolutePath(), articleAbstracts);
    }

    /**
     * Goes over the articleAbstracts list and appends the abstracts as property
     * 999 on the MARX XML
     *
     * @param filePath the XML MARC file path
     * @param articleAbstracts the abstracts as a list of strings
     * @return the modified XML MARC file
     */
    public boolean generateMARCXMLFile(String filePath, Map<String, String> articleAbstracts) {
        // sanity checking the file path
        if (isBlank(filePath)) {
            return false;
        }
        // sanity checking the abstracts collection
        if (isBlank(articleAbstracts)) {
            return false;
        }

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder;
        Document doc = null;

        try {
            builder = domFactory.newDocumentBuilder();
            doc = builder.parse(new File(filePath));
            NodeList records = doc.getElementsByTagName("record");

            for (int i = 0; i < records.getLength(); i++) {
                NodeList datafieldNodes = records.item(i).getChildNodes();
                for (int j = 0; j < datafieldNodes.getLength(); j++) {
                    Node datafield = datafieldNodes.item(j);
                    NamedNodeMap attrMap = datafield.getAttributes();
                    if (attrMap != null) {
                        if (attrMap.getNamedItem("tag") != null && attrMap.getNamedItem("tag").getNodeValue().equals("901")) {

                            String viafId = datafield.getTextContent().trim();
                            String wpAbstract = articleAbstracts.get(viafId);
                            if (wpAbstract == null) {
                                wpAbstract = "null";
                            }

                            Element sfElem = doc.createElement("subfield");
                            sfElem.setAttribute("code", "a");
                            Text abstractText = doc.createTextNode(wpAbstract);

                            sfElem.appendChild(abstractText);
                            Element dfElem = doc.createElement("datafield");
                            dfElem.setAttribute("tag", "999");
                            dfElem.setAttribute("ind1", " ");
                            dfElem.setAttribute("ind2", " ");
                            dfElem.appendChild(sfElem);
                            records.item(i).appendChild(dfElem);
                        }

                        if (attrMap.getNamedItem("tag") != null && attrMap.getNamedItem("tag").getNodeValue().equals(MARCIdentifiers.AUTHOR_NAME)) {

                            String authorName = datafield.getTextContent().trim();

                            WikipediaRemoteAPIModel wrm = new WikipediaRemoteAPIModel();
                            String queriedHebrewAbstracts = wrm.getAbstractByArticleName(authorName, "he");

                            // create new subfield
                            Element valueSubField = doc.createElement("subfield");
                            valueSubField.setAttribute("code", "a");
                            valueSubField.appendChild(doc.createTextNode(queriedHebrewAbstracts));

                            Element keySubField = doc.createElement("subfield");
                            keySubField.setAttribute("code", "9");
                            keySubField.appendChild(doc.createTextNode("heb"));

                            // create new datafield and append subfield into it
                            Element dataField = doc.createElement("datafield");
                            dataField.setAttribute("tag", "999");
                            dataField.setAttribute("ind1", " ");
                            dataField.setAttribute("ind2", " ");
                            dataField.appendChild(keySubField);
                            dataField.appendChild(valueSubField);
                            records.item(i).appendChild(valueSubField);
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            logger.error("Exception while updating XML MARC file", ex);
        }

        if (doc == null) {
            logger.error("Exception doc is empty");
            return false;
        }

        try {
            logger.info("Transforming data to new XML content");
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String xmlOutput = result.getWriter().toString();
            logger.info("Writing content to XML file");
            File file = new File(filePath + ".updated");
            if (file.createNewFile()) {
                FileOutputStream fStream = new FileOutputStream(file.getAbsolutePath());
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fStream, "UTF-8");
                try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                    bufferedWriter.write(xmlOutput);
                }
            }
            logger.info("Done!!!");
        } catch (IllegalArgumentException | TransformerException | IOException ex) {
            logger.error("Exception while transforming XML MARC file", ex);
        }

        return true;
    }

    /**
     * Attempts to generate an updated MARC XML file by querying from remote
     * SPARQL end point
     *
     * @param filePath
     * @param viafAuthorsMap
     * @return true if and only if the entire process was successful
     */
    public boolean dynamicallyGenerateMARCXMLFile(String filePath, Map<String, Author> viafAuthorsMap) {
        // sanity checking the file path
        if (isBlank(filePath)) {
            return false;
        }

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder;
        Document doc = null;

        try {
            builder = domFactory.newDocumentBuilder();

            File file = new File(filePath);
            //InputStream inputStream= new FileInputStream(file);
            //Reader reader = new InputStreamReader(inputStream, "UTF-8");

            //InputSource is = new InputSource(new FileInputStream(file));
            //is.setEncoding("UTF-8");
            doc = builder.parse(new FileInputStream(file));
            NodeList records = doc.getElementsByTagName("record");

            for (int i = 0; i < records.getLength(); i++) {
                NodeList datafieldNodes = records.item(i).getChildNodes();
                for (int j = 0; j < datafieldNodes.getLength(); j++) {
                    Node datafield = datafieldNodes.item(j);

                    if (datafield.getTextContent() == null) {
                        datafield.setTextContent("N/A");
                    }

                    NamedNodeMap attrMap = datafield.getAttributes();

                    String viafId = datafield.getTextContent().trim();
                    Author author = viafAuthorsMap.get(viafId);

                    if (author == null) {
                        logger.info("Article not found in XML file, skipping....");
                        continue;
                    }

                    if (attrMap != null && author.getWikipediaArticleAbstract() != null) {
                        if (attrMap.getNamedItem("tag") != null && attrMap.getNamedItem("tag").getNodeValue().equals(MARCIdentifiers.VIAF_ID)) {

                            // create new subfield
                            Element valueSubField = doc.createElement("subfield");
                            valueSubField.setAttribute("code", "a");

                            String enAbstract = author.getWikipediaArticleAbstract().get("en");
                            if (enAbstract == null) {
                                enAbstract = "";
                            }
                            valueSubField.appendChild(doc.createTextNode(enAbstract));

                            Element keySubField = doc.createElement("subfield");
                            keySubField.setAttribute("code", "9");
                            keySubField.appendChild(doc.createTextNode("lat"));

                            // create new datafield and append subfield into it
                            Element dataField = doc.createElement("datafield");
                            dataField.setAttribute("tag", "999");
                            dataField.setAttribute("ind1", " ");
                            dataField.setAttribute("ind2", " ");
                            dataField.appendChild(keySubField);
                            dataField.appendChild(valueSubField);
                            records.item(i).appendChild(dataField);
                        }

                        if (attrMap.getNamedItem("tag") != null && attrMap.getNamedItem("tag").getNodeValue().equals(MARCIdentifiers.VIAF_ID)) {

                            // create new subfield
                            Element valueSubField = doc.createElement("subfield");
                            valueSubField.setAttribute("code", "a");

                            String heAbstract = author.getWikipediaArticleAbstract().get("he");
                            if (heAbstract == null) {
                                heAbstract = "";
                            }
                            valueSubField.appendChild(doc.createTextNode(heAbstract));

                            Element keySubField = doc.createElement("subfield");
                            keySubField.setAttribute("code", "9");
                            keySubField.appendChild(doc.createTextNode("heb"));

                            // create new datafield and append subfield into it
                            Element dataField = doc.createElement("datafield");
                            dataField.setAttribute("tag", "999");
                            dataField.setAttribute("ind1", " ");
                            dataField.setAttribute("ind2", " ");
                            dataField.appendChild(keySubField);
                            dataField.appendChild(valueSubField);
                            records.item(i).appendChild(dataField);
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            logger.error("Exception while updating XML MARC file", ex);
        }

        if (doc == null) {
            logger.error("Exception doc is empty");
            return false;
        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String xmlOutput = result.getWriter().toString();
            //System.out.println(xmlOutput);
            File file = new File(filePath + ".updated");

            if (file.exists()) {
                file.delete();
            }

            logger.info(filePath + ".updated created!");
            if (file.createNewFile()) {
                FileOutputStream fStream = new FileOutputStream(file.getAbsolutePath());
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fStream, "UTF-8");
                try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                    bufferedWriter.write(xmlOutput);
                }
            }

            boolean shouldOpen = applicationProperties.getBoolean("attempt_to_open_result", true);
            if (shouldOpen) {
                Desktop.getDesktop().open(file);
            }

        } catch (IllegalArgumentException | TransformerException | IOException ex) {
            logger.error("Exception while transforming XML MARC file", ex);
            return false;
        }
        return true;
    }
}
