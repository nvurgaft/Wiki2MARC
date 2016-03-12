
package com.protowiki.rest;

import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
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
public class HeartbeatResourceTest {
    
    public static Logger logger = LoggerFactory.getLogger(HeartbeatResourceTest.class);

    /**
     * Test of getServerHeartbeat method, of class HeartbeatResource.
     */
    @Test
    public void testGetServerHeartbeat() {
        
        HeartbeatResource instance = new HeartbeatResource();
        Response expResult = Response.ok("ok").build();
        Response result = instance.getServerHeartbeat();
        assertEquals(expResult, result);
    }
    
}
