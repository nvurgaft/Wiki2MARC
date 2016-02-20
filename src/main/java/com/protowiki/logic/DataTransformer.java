package com.protowiki.logic;

import com.protowiki.beans.Author;
import com.protowiki.beans.Controlfield;
import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import com.protowiki.beans.Subfield;
import com.protowiki.dal.RDFConnectionManager;
import com.protowiki.entities.RDFStatement;
import com.protowiki.dal.QueryHandler;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        List<Author> authorsList = recordsList.stream()
                .map(r -> {
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
    public boolean generateMARCXMLFile(File file, List<String> articleAbstracts) {
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
    public boolean generateMARCXMLFile(String filePath, List<String> articleAbstracts) {
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
            NodeList nodes = doc.getElementsByTagName("record");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node currentNode = nodes.item(i);

                Element sfElem = doc.createElement("subfield");
                sfElem.setAttribute("code", "a");
                Text abstractText = doc.createTextNode(articleAbstracts.get(0));
                sfElem.appendChild(abstractText);

                Element dfElem = doc.createElement("datafield");
                dfElem.setAttribute("tag", "999");
                dfElem.appendChild(sfElem);

                currentNode.getParentNode().appendChild(currentNode);
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
                            System.out.println("viaf: " + datafield.getTextContent());
                                // TODO
                        }

                    }
                }
            }

//            Element sfElem = doc.createElement("subfield");
//            sfElem.setAttribute("code", "a");
//            Text abstractText = doc.createTextNode("TEST TEXT PLEASE IGNORE");
//            sfElem.appendChild(abstractText);
//
//            Element dfElem = doc.createElement("datafield");
//            dfElem.setAttribute("tag", "999");
//            dfElem.appendChild(sfElem);
//
//            currentNode.getParentNode().appendChild(currentNode);
        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            logger.error("Exception while updating XML MARC file", ex);
        }

//        if (doc
//                == null) {
//            logger.error("Exception doc is empty");
//            return false;
//        }
//
//        try {
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            StreamResult result = new StreamResult(new StringWriter());
//            DOMSource source = new DOMSource(doc);
//            transformer.transform(source, result);
//
//            String xmlOutput = result.getWriter().toString();
//            logger.info(xmlOutput);
//
//        } catch (IllegalArgumentException | TransformerException ex) {
//            logger.error("Exception while transforming XML MARC file", ex);
//        }
        return false;
    }
}
