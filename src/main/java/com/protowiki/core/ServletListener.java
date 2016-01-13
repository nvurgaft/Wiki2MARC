
package com.protowiki.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class ServletListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(ServletListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Servlet context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Servlet context destroyed");
    }
    
}
