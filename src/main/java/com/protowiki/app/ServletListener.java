
package com.protowiki.app;

import com.protowiki.values.Values;
import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class ServletListener implements ServletContextListener {

    public static Logger logger = LoggerFactory.getLogger(ServletListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Servlet context initialized");    
        File initDir = new File(Values.FILE_PATH);
        if (!initDir.exists()) {
            if (initDir.mkdir()) {
                logger.info("Directory {} was created", Values.FILE_PATH);
            } else {
                logger.info("Directory {} found", Values.FILE_PATH);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Killing executor service");
        JobExecutorService.killExecutor();
        logger.info("Servlet context destroyed");
    }
    
}
