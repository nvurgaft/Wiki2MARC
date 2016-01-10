package com.protowiki.utils;

import com.protowiki.beans.Datafield;
import com.protowiki.beans.Record;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Kobi
 */
@Ignore
public class RecordHandlerTest {
    
    public RecordHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test "testParseRecord()"
     * 
     * Should: Take a single XML record and parse it into a record object.
     * Pass: If and only if the newly generated object is equal to the expected
     *       object provided.
     * Fail: Otherwise.
     */
    @Test
    public void testParseRecord() {
        
        Record testRecord = new Record();
        Datafield datafield = new Datafield();
        
        fail("No test logic provided");
    }
}
