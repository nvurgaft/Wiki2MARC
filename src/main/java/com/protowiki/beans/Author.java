package com.protowiki.beans;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Nick
 */
public class Author implements Serializable{
    
    private String marcId;
    private Map<String, String> names;
    private String wikipediaUri;
    private String wikipediaArticleAbstract;
    private String viafId;
    private String nliId;
    private String years;
    
    public Author() {}

    public String getMarcId() {
        return marcId;
    }

    public void setMarcId(String marcId) {
        this.marcId = marcId;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public String getWikipediaUri() {
        return wikipediaUri;
    }

    public void setWikipediaUri(String wikipediaUri) {
        this.wikipediaUri = wikipediaUri;
    }

    public String getWikipediaArticleAbstract() {
        return wikipediaArticleAbstract;
    }

    public void setWikipediaArticleAbstract(String wikipediaArticleAbstract) {
        this.wikipediaArticleAbstract = wikipediaArticleAbstract;
    }
    
     public String getViafId() {
        return this.viafId;
    }
    
    public void setViafId(String viafId) {
        this.viafId = viafId;
    }

    public String getNliId() {
        return nliId;
    }

    public void setNliId(String nliId) {
        this.nliId = nliId;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String born) {
        this.years = born;
    }

    @Override
    public String toString() {
        return "Author{" + "names=" + names + ", wikipediaUrl=" + wikipediaUri + ", viafId=" + viafId + ", nliId=" + nliId + ", years=" + years + '}';
    }
}
