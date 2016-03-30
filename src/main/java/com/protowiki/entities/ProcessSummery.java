package com.protowiki.entities;

import java.util.Date;
import java.util.Map;

/**
 *  The process summery class holds information about the outcome of the MARC 
 *  binding process. This data is used to file a summery report.
 * 
 * @author Nick
 */
public class ProcessSummery {
    
    private Integer totalArticles;
    private Integer totalEnglishAbstractsFound;
    private Integer totalEnglishAbstractsApplied;
    private Integer totalHebrewAbstractsFound;
    private Integer totalHebrewAbstractsApplied;
    
    private Map<String, String> errors;
    private Date dateCreated;

    public Integer getTotalArticles() {
        return totalArticles;
    }

    public void setTotalArticles(Integer totalArticles) {
        this.totalArticles = totalArticles;
    }

    public Integer getTotalEnglishAbstractsFound() {
        return totalEnglishAbstractsFound;
    }

    public void setTotalEnglishAbstractsFound(Integer totalEnglishAbstractsFound) {
        this.totalEnglishAbstractsFound = totalEnglishAbstractsFound;
    }

    public Integer getTotalEnglishAbstractsApplied() {
        return totalEnglishAbstractsApplied;
    }

    public void setTotalEnglishAbstractsApplied(Integer totalEnglishAbstractsApplied) {
        this.totalEnglishAbstractsApplied = totalEnglishAbstractsApplied;
    }

    public Integer getTotalHebrewAbstractsFound() {
        return totalHebrewAbstractsFound;
    }

    public void setTotalHebrewAbstractsFound(Integer totalHebrewAbstractsFound) {
        this.totalHebrewAbstractsFound = totalHebrewAbstractsFound;
    }

    public Integer getTotalHebrewAbstractsApplied() {
        return totalHebrewAbstractsApplied;
    }

    public void setTotalHebrewAbstractsApplied(Integer totalHebrewAbstractsApplied) {
        this.totalHebrewAbstractsApplied = totalHebrewAbstractsApplied;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    
}
