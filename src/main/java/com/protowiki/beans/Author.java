package com.protowiki.beans;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Nick
 */
public class Author implements Serializable {
    
    private String marcId;
    private Map<String, String> names; // language prefix, name
    private String wikipediaUri;
    private Map<String, String> wikipediaAbstracts; // language prefix, content
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

    public Map<String, String> getWikipediaArticleAbstract() {
        return wikipediaAbstracts;
    }

    public void setWikipediaArticleAbstract(Map<String, String> wikipediaArticleAbstract) {
        this.wikipediaAbstracts = wikipediaArticleAbstract;
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
        return "Author details => names: " + names + ", wikipediaUrl: " + wikipediaUri + ", viafId: " + viafId + ", nliId: " + nliId + ", years: " + years;
    }
}
