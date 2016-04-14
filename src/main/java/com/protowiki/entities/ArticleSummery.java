package com.protowiki.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *  The article summery class holds information about the outcome of the MARC 
 *  binding process for a particular MARC record. 
 *  This object is used as an array/collection item to form a summery report.
 * 
 * @author Nick
 */
public class ArticleSummery implements Serializable, Cloneable {
    
    private String articles;
    private String status;
    private String articlesExceptions;
    private Boolean foundEnglishAbstract;
    private Boolean foundHebrewAbstract;
    private Date summeryDateCreated;

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArticlesExceptions() {
        return articlesExceptions;
    }

    public void setArticlesExceptions(String articlesExceptions) {
        this.articlesExceptions = articlesExceptions;
    }

    public Boolean getFoundEnglishAbstract() {
        return foundEnglishAbstract;
    }

    public void setFoundEnglishAbstract(Boolean foundEnglishAbstract) {
        this.foundEnglishAbstract = foundEnglishAbstract;
    }

    public Boolean getFoundHebrewAbstract() {
        return foundHebrewAbstract;
    }

    public void setFoundHebrewAbstract(Boolean foundHebrewAbstract) {
        this.foundHebrewAbstract = foundHebrewAbstract;
    }

    public Date getSummeryDateCreated() {
        return summeryDateCreated;
    }

    public void setSummeryDateCreated(Date summeryDateCreated) {
        this.summeryDateCreated = summeryDateCreated;
    }

    @Override
    public String toString() {
        return "ArticleSummery => [" + 
                "articles: " + articles + 
                ", status: " + status + 
                ", articlesExceptions: " + articlesExceptions + 
                ", foundEnglishAbstract: " + foundEnglishAbstract + 
                ", foundHebrewAbstract: " + foundHebrewAbstract + 
                ", summeryDateCreated: " + summeryDateCreated + 
                "]";
    }
    

}