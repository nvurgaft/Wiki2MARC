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
public class RecordSummery implements Serializable {
    
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

    public RecordSummery setRecordId(String recordId) {
        this.recordId = recordId;
        return this;
    }

    public String getLabelHe() {
        return labelHe;
    }

    public RecordSummery setLabelHe(String labelHe) {
        this.labelHe = labelHe;
        return this;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public RecordSummery setLabelEn(String labelEn) {
        this.labelEn = labelEn;
        return this;
    }

    public String getViaf() {
        return viaf;
    }

    public RecordSummery setViaf(String viaf) {
        this.viaf = viaf;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public RecordSummery setStatus(String status) {
        this.status = status;
        return this;
    }

    public Boolean getFoundEnglishAbstract() {
        return foundEnglishAbstract;
    }

    public RecordSummery setFoundEnglishAbstract(Boolean foundEnglishAbstract) {
        this.foundEnglishAbstract = foundEnglishAbstract;
        return this;
    }

    public Boolean getFoundHebrewAbstract() {
        return foundHebrewAbstract;
    }

    public RecordSummery setFoundHebrewAbstract(Boolean foundHebrewAbstract) {
        this.foundHebrewAbstract = foundHebrewAbstract;
        return this;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public RecordSummery setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public String toString() {
        return "ArticleSummery{recordId=" + recordId + ", labelHe=" + labelHe + ", labelEn=" + labelEn + ", viaf=" + viaf + ", foundEnglishAbstract=" + foundEnglishAbstract + ", foundHebrewAbstract=" + foundHebrewAbstract + ", status=" + status + ", dateCreated=" + dateCreated + '}';
    }
}
