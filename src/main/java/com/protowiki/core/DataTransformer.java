package com.protowiki.core;

import com.protowiki.beans.Author;
import com.protowiki.beans.Controlfield;
import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import com.protowiki.beans.Subfield;
import com.protowiki.db.RDFConnectionManager;
import com.protowiki.entities.RDFStatement;
import com.protowiki.model.QueryHandler;
import com.protowiki.model.WikidataRemoteAPIModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import virtuoso.jena.driver.VirtGraph;

/**
 *
 * @author Nick
 */
public class DataTransformer {

    public static Logger logger = LoggerFactory.getLogger(DataTransformer.class);

    /**
     * Takes a list of Record objects and transforms it into a list of Author
     * objects
     *
     * @param recordsList List of parsed records (MARC documents)
     * @return List of authors
     */
    public List<Author> transformRecordsListToAuthors(List<Record> recordsList) {

        if (recordsList == null || recordsList.isEmpty()) {
            return null;
        }

        List<Author> authorsList = recordsList.stream().map(r -> {
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
        }).collect(Collectors.toList());
        return authorsList;
    }

    /**
     * Attempts to insert an Author RDF
     *
     * @param s Subject
     * @param p Predicate
     * @param o Object
     * @return True if statement was successfully inserted to RDF DB
     */
    public boolean insertAuthorIntoDB(String s, String p, String o) {
        VirtGraph g = new RDFConnectionManager("http://testdb").getGraphConnection();
        QueryHandler handler = new QueryHandler(g, "http://testdb");

        return handler.insertStatement(s, p, o);
    }

    /**
     * Attempts to batch insert an Author RDF
     *
     * @param authors List of Author objects
     */
    public void batchInsertAuthorIntoDB(List<Author> authors) {
        VirtGraph g = new RDFConnectionManager("http://testdb").getGraphConnection();
        QueryHandler handler = new QueryHandler(g, "http://testdb");

        authors.stream().forEach(author -> {
            List<RDFStatement> queryStatement = new ArrayList<>();
            queryStatement.add(new RDFStatement(author.getWikipediaUri(), "rdf:viaf", author.getViafId()));
            author.getNames().keySet().stream().forEach(key -> {
                queryStatement.add(new RDFStatement(author.getWikipediaUri(), "rdf:" + key + "Name", author.getNames().get(key)));
            });
            handler.batchInsertStatements(queryStatement);
        });
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
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        // sanity checking the abstracts collection
        if (articleAbstracts == null || articleAbstracts.isEmpty()) {
            return false;
        }

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder = null;
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
                            System.out.println("viafId : " + viafId);
                            String wpAbstract = articleAbstracts.get(viafId);
                            
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
            logger.info(xmlOutput);

        } catch (IllegalArgumentException | TransformerException ex) {
            logger.error("Exception while transforming XML MARC file", ex);
        }

        return false;
    }

    /**
     * Attempts to generate an updated MARC XML file by querying from remote
     * SPARQL end point
     * @param filePath
     * @return true if and only if the entire process was successful
     */
    public boolean dynamicallyGenerateMARCXMLFile(String filePath) {
        // sanity checking the file path
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringComments(true);
        DocumentBuilder builder = null;
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

                            WikidataRemoteAPIModel ram = new WikidataRemoteAPIModel();
                            String queriedAbstract = ram.getWikipediaAbstractByViafId(viafId, "en");

                            Element sfElem = doc.createElement("subfield");
                            sfElem.setAttribute("code", "a");
                            Text abstractText = doc.createTextNode(queriedAbstract);
                            sfElem.appendChild(abstractText);
                            Element dfElem = doc.createElement("datafield");
                            dfElem.setAttribute("tag", "999");
                            dfElem.setAttribute("ind1", " ");
                            dfElem.setAttribute("ind2", " ");
                            dfElem.appendChild(sfElem);
                            records.item(i).appendChild(dfElem);
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
            if (file.createNewFile()) {
                FileOutputStream fStream = new FileOutputStream(file.getAbsolutePath());
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fStream, "UTF-8");
                try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                    bufferedWriter.write(xmlOutput);
                }
            }

        } catch (IllegalArgumentException | TransformerException | IOException ex) {
            logger.error("Exception while transforming XML MARC file", ex);
            return false;
        }
        return true;
    }
}
