package com.protowiki.reports;

import com.protowiki.entities.ArticleSummery;
import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocCalculation;
import net.sf.dynamicreports.adhoc.configuration.AdhocColumn;
import net.sf.dynamicreports.adhoc.configuration.AdhocConfiguration;
import net.sf.dynamicreports.adhoc.configuration.AdhocGroup;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.adhoc.configuration.AdhocSort;
import net.sf.dynamicreports.adhoc.configuration.AdhocSubtotal;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class ReportGenerator {

    public static Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

    public static void generateBasicReport(ArticleSummery summery) {
        
        if (summery==null) {
            logger.warn("Cannot generate report from a null summery object, discarding.");
            return;
        }

        AdhocConfiguration configuration = new AdhocConfiguration();

        AdhocReport report = new AdhocReport();
        configuration.setReport(report);

        //columns
        AdhocColumn column = new AdhocColumn();
        column.setName("article name");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("status");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("english abstract");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("hebrew abstract");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("exceptions");
        report.addColumn(column);

        //groups
        AdhocGroup group = new AdhocGroup();
        group.setName("item");
        report.addGroup(group);

        //subtotal
        AdhocSubtotal subtotal = new AdhocSubtotal();
        subtotal.setName("english abstract");
        subtotal.setCalculation(AdhocCalculation.SUM);
        report.addSubtotal(subtotal);
        subtotal = new AdhocSubtotal();
        subtotal.setCalculation(AdhocCalculation.SUM);
        subtotal.setName("hebrew abstract");
        report.addSubtotal(subtotal);

        //sorts
        AdhocSort sort = new AdhocSort();
        sort.setName("item");
        report.addSort(sort);

//        try {
//            JasperReportBuilder reportBuilder = AdhocManager.createReport(configuration.getReport(), new ReportCustomizer());
//            reportBuilder.setDataSource();
//            reportBuilder.show();
//        } catch (DRException e) {
//            logger.error("DRException while attempting to generate report", e);
//        }
    }
}
