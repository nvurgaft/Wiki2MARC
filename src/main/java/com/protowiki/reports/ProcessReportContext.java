package com.protowiki.reports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This is the report context used by the Mustache.java template engine.
 *
 * @author Nick
 */
public class ProcessReportContext {

    private final String reportName;
    private final String summery;
    private final List<RecordSummery> recordSummeries;
    private Integer index = 0;

    public ProcessReportContext(List<RecordSummery> recordSummeries, String reportName, String summery) {
        this.recordSummeries = recordSummeries;
        this.reportName = reportName;
        this.summery = summery;
    }
    
    public String reportName() {
        return this.reportName;
    }
    
    public List<RecordSummery> recordSummeries() {
        return this.recordSummeries;
    }

    public String summery() {
        return this.summery;
    }

    public String dateGenerated() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
    
    public Long totalEnglishAbstractsFound() {     
        return this.recordSummeries.stream().filter(s -> {
            return s.getFoundEnglishAbstract().equals(true);
        }).count();
    }
    
    public Long totalHebrewAbstractsFound() {     
        return this.recordSummeries.stream().filter(s -> {
            return s.getFoundHebrewAbstract().equals(true);
        }).count();
    }
    
    public Long totalEnglishLabelsFound() {     
        return this.recordSummeries.stream().filter(s -> {
            return s.getLabelEn()!=null && !s.getLabelEn().isEmpty();
        }).count();
    }
    
    public Long totalHebrewLabelsFound() {     
        return this.recordSummeries.stream().filter(s -> {
            return s.getLabelHe()!=null && !s.getLabelHe().isEmpty();
        }).count();
    }
    
    public Long totalFoundViaf() {
        return this.recordSummeries.stream().filter(s -> {
            return (s.getViaf()!=null && !s.getViaf().isEmpty());
        }).count();
    }
    
    public Long totalSuccessful() {
        return this.recordSummeries.stream().filter(s -> {
            return s.getStatus().equals("SUCCESS");
        }).count();
    }
    
    public Long totalEnglishAbstractsNotFound() {     
        return this.index - this.totalEnglishAbstractsFound();
    }
    
    public Long totalHebrewAbstractsNotFound() {     
        return this.index - this.totalHebrewAbstractsFound();
    }
    
    public Long totalEnglishLabelsNotFound() {     
        return this.index - this.totalEnglishLabelsFound();
    }
    
    public Long totalHebrewLabelsNotFound() {     
        return this.index - this.totalHebrewLabelsFound();
    }
    
    public Long totalFailed() {
        return this.index - this.totalSuccessful();
    }
    
    public Integer index() {
        return ++this.index;
    }
}
