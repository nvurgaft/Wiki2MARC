package com.protowiki.utils;

import com.protowiki.beans.Controlfield;
import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import com.protowiki.beans.Subfield;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Using a SAX parser, we transform an XML Record file into in memory data
 * structure
 *
 * @author Nick
 */
public class RecordHandler extends DefaultHandler {

    private static Logger logger = LoggerFactory.getLogger(RecordHandler.class);
    
    // XML tag identifiers
    private static final String RECORD = "record";
    private static final String CONTROL_FIELD = "controlfield";
    private static final String DATA_FIELD = "datafield";
    private static final String SUB_FIELD = "subfield";

    private List<Record> records;
    private Stack<String> elementStack;
    private Stack<Object> objectStack;

    public RecordHandler() {
        records = new ArrayList<>();
        elementStack = new Stack<>();
        objectStack = new Stack<>();
    }

    @Override
    public void startDocument() throws SAXException {
        logger.info("Started parsing");
    }

    @Override
    public void endDocument() throws SAXException {
        logger.info("Finished parsing");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        this.elementStack.push(qName);

        switch (qName) {
            case RECORD:
                Record record = new Record();
                record.setDatafields(new ArrayList<>());
                record.setControlfields(new ArrayList<>());
                this.objectStack.push(record);
                break;
            case CONTROL_FIELD:
                Controlfield controlfield  = new Controlfield();
                String tag = attributes.getValue("tag");
                controlfield.setTag(tag);
                controlfield.setValue(new String());
                this.objectStack.push(controlfield);
                break;
            case DATA_FIELD:
                Datafield datafield = new Datafield();
                String[] inds = {attributes.getValue("ind1").trim(), attributes.getValue("ind2").trim()};
                datafield.setInd(inds);
                datafield.setTag(attributes.getValue("tag").trim());
                datafield.setSubfields(new ArrayList<>());
                this.objectStack.push(datafield);
                break;
            case SUB_FIELD:
                Subfield subfield = new Subfield();
                subfield.setCode(attributes.getValue("code").trim());
                this.objectStack.push(subfield);
                break;
            default:
            
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        this.elementStack.pop();

        switch (qName) {
            case RECORD:
                Record r = (Record) this.objectStack.pop();
                records.add(r);
                break;
            case CONTROL_FIELD:
                Controlfield cf = (Controlfield) this.objectStack.pop();
                if (currentElement().equals(RECORD)) {
                    Record parentRecord = (Record) this.objectStack.peek();
                    parentRecord.getControlfields().add(cf);
                }
                break;
            case DATA_FIELD:
                Datafield df = (Datafield) this.objectStack.pop();
                if (currentElement().equals(RECORD)) {
                    Record parentRecord = (Record) this.objectStack.peek();
                    parentRecord.getDatafields().add(df);
                }
                break;
            case SUB_FIELD:
                Subfield sf = (Subfield) this.objectStack.pop();
                if (currentElement().equals(DATA_FIELD)) {
                    Datafield parentDatafield = (Datafield) this.objectStack.peek();
                    parentDatafield.getSubfields().add(sf);
                }
                break;
            default:
            
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (value.isEmpty()) return;

        switch (currentElement()) {
            case CONTROL_FIELD:
                Controlfield controlfield = (Controlfield) this.objectStack.peek();
                controlfield.setValue(value);
                break;
            case SUB_FIELD:
                Subfield subfield = (Subfield) this.objectStack.peek();
                subfield.setValue(value);
                break;
            default:
            
        }
    }

    private String currentElement() {
        return this.elementStack.peek();
    }

    private String currentElementParent() {
        if (this.elementStack.size() < 2) {
            return null;
        }
        return this.elementStack.get(this.elementStack.size() - 2);
    }

    private Object currentObject() {
        return this.objectStack.peek();
    }

    public List<Record> getRecords() {
        return this.records;
    }
}
