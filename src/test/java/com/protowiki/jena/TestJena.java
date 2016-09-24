package com.protowiki.jena;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.VCARD;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
@Ignore
public class TestJena {

    public static Logger logger = LoggerFactory.getLogger(TestJena.class);

    static String tutorialURI = "http://hostname/rdf/tutorial/";
    static String briansName = "Brian McBride";
    static String briansEmail1 = "brian_mcbride@hp.com";
    static String briansEmail2 = "brian_mcbride@hpl.hp.com";
    static String title = "An Introduction to RDF and the Jena API";
    static String date = "23/01/2001";

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial01.java
     */
    @Test
    public void testDefaultModel() {

        String personUri = "http://somewhere/JohnSmith";
        String fullName = "John Smith";

        Model model = ModelFactory.createDefaultModel();
        Resource johnSmith = model.createResource(personUri);
        johnSmith.addProperty(VCARD.FN, fullName);
    }

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial02.java
     */
    @Test
    public void testDefaultModelExtended() {

        String personUri = "http://somewhere/JohnSmith";
        String fullName = "John Smith";
        String givenName = "John";
        String familyName = "Smith";

        Model model = ModelFactory.createDefaultModel();
        Resource johnSmith = model.createResource(personUri)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N, model.createResource()
                        .addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName)
                );
    }

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial03.java
     */
    @Test
    public void testStatement() {

        String personUri = "http://somewhere/JohnSmith";
        String fullName = "John Smith";
        String givenName = "John";
        String familyName = "Smith";

        Model model = ModelFactory.createDefaultModel();
        model.createResource(personUri)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N, model.createResource()
                        .addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName)
                );

        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            logger.info("Subject: {}, Predicate: {}", subject);
            if (object instanceof Resource) {
                logger.debug(" " + object.toString());
            } else {
                logger.debug(" \"{}\"", object.toString());
            }
            logger.info(" .");
        }
    }

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial04.java
     */
    @Test
    public void testWriteRdf() {

        String personUri = "http://somewhere/JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;

        Model model = ModelFactory.createDefaultModel();
        Resource johnSmith
                = model.createResource(personUri)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,
                        model.createResource()
                        .addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName));

        model.write(System.out);
    }

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial05.java
     */
    @Test
    public void testReadRdf() {

        String inputFileName = "vc-db-1.rdf";
        Model model = ModelFactory.createDefaultModel();

        InputStream stream = FileManager.get().open(inputFileName);
        if (stream == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        model.read(stream, "");
        model.write(System.out);
    }

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial06.java
     */
    @Test
    public void testReadRdfExtended() {

        String inputFileName = "vc-db-1.rdf";
        String johnSmithURI = "http://somewhere/JohnSmith/";

        Model model = ModelFactory.createDefaultModel();

        InputStream stream = FileManager.get().open(inputFileName);
        if (stream == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        model.read(new InputStreamReader(stream), "");
        Resource vcard = model.getResource(johnSmithURI);

        Resource name = (Resource) vcard.getRequiredProperty(VCARD.N)
                .getObject();

        String fullName = vcard.getRequiredProperty(VCARD.FN)
                .getString();

        vcard.addProperty(VCARD.NICKNAME, "Smithy")
                .addProperty(VCARD.NICKNAME, "Adman");

        logger.info("The nicknames of \"{}\" are:", fullName);
        StmtIterator iter = vcard.listProperties(VCARD.NICKNAME);
        while (iter.hasNext()) {
            logger.info("    {}", iter.nextStatement().getObject().toString());
        }
    }

    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial07.java
     */
    @Test
    public void testQueryingAModel() {

        String inputFileName = "vc-db-1.rdf";

        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        model.read(in, "");
        ResIterator iter = model.listResourcesWithProperty(VCARD.FN);
        if (iter.hasNext()) {
            logger.info("The database contains vcards for:");
            while (iter.hasNext()) {
                logger.info("  {}", iter.nextResource().getRequiredProperty(VCARD.FN).getString());
            }
        } else {
            logger.info("No vcards were found in the database");
        }
    }
    
    /**
     * https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial08.java
     */
    @Test
    public void testQueryingAModelUsingSelector() {

        String inputFileName = "vc-db-1.rdf";

        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        model.read(in, "");
        StmtIterator iter = model.listStatements(new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
            @Override
            public boolean selects(Statement s) {
                return s.getString().endsWith("Smith");
            }
        });
        
        if (iter.hasNext()) {
            logger.info("The database contains vcards for these Smith's:");
            while (iter.hasNext()) {
                logger.info("  {}", iter.nextStatement().getString());
            }
        } else {
            logger.info("No Smith's were found in the database");
        }
    }
}
