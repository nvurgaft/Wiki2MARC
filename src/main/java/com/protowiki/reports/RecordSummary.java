package com.protowiki.reports;

import java.io.Serializable;
import java.util.Date;

/**
 *  The article summery class holds information about the outcome of the MARC 
 *  binding process for a particular MARC record. 
 *  This object is used as an array/collection item to form a summery report.
 * 
 * @author Nick
 */
public class RecordSummary implements Serializable {
    
    private String recordId; // tag 001
    private String labelHe;
    private String labelEn;
    private String viaf;
    private Boolean foundEnglishAbstract;
    private Boolean foundHebrewAbstract;
    private String status; // success or fail
    private Date dateCreated;

    public String getRecordId() {
        return recordId;
    }

    public RecordSummary setRecordId(String recordId) {
        this.recordId = recordId;
        return this;
    }

    public String getLabelHe() {
        return labelHe;
    }

    public RecordSummary setLabelHe(String labelHe) {
        this.labelHe = labelHe;
        return this;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public RecordSummary setLabelEn(String labelEn) {
        this.labelEn = labelEn;
        return this;
    }

    public String getViaf() {
        return viaf;
    }

    public RecordSummary setViaf(String viaf) {
        this.viaf = viaf;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public RecordSummary setStatus(String status) {
        this.status = status;
        return this;
    }

    public Boolean getFoundEnglishAbstract() {
        return foundEnglishAbstract;
    }

    public RecordSummary setFoundEnglishAbstract(Boolean foundEnglishAbstract) {
        this.foundEnglishAbstract = foundEnglishAbstract;
        return this;
    }

    public Boolean getFoundHebrewAbstract() {
        return foundHebrewAbstract;
    }

    public RecordSummary setFoundHebrewAbstract(Boolean foundHebrewAbstract) {
        this.foundHebrewAbstract = foundHebrewAbstract;
        return this;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public RecordSummary setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public String toString() {
        return String.format("ArticleSummery{recordId: %s, labelHe: %s, labelEn: %s, viaf: %s, foundEnglishAbstract: %o, foundHebrewAbstract: $o, status: $s, dateCreated: $o", 
                recordId, labelHe, labelEn, viaf, foundEnglishAbstract, foundHebrewAbstract, status, dateCreated);
    }
}
