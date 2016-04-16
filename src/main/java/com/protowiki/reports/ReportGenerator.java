package com.protowiki.reports;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class ReportGenerator {

    public static Logger logger = LoggerFactory.getLogger(ReportGenerator.class);
    
    /**
     * 
     * @param processSummery
     * @param reportName 
     */
    public void generateBasicReport(ProcessReportContext processSummery, String reportName) {

        if (processSummery == null) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDate = sdf.format(new Date());

        String fileName = String.format("%s-%s.html", reportName, currentDate);

        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile("templates/report-template.mustache");
            mustache.execute(new PrintWriter(new File(fileName), "UTF-8"), processSummery).flush();
        } catch (IOException ioex) {
            logger.error("An IOException occured while attempting to generate a report", ioex);
        }
    }
}
