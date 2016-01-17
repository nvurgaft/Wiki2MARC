package com.protowiki.beans;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author Nick
 */
public class Author implements Serializable{
    private Map<String, String> names;
    private URL wikipediaUrl;
    private String wikipediaArticleAbstract;
    private String viafId;
    private String nliId;
    private String years;
    
    public Author() {}

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public URL getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(URL wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
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
        return "Author{" + "names=" + names + ", wikipediaUrl=" + wikipediaUrl + ", viafId=" + viafId + ", nliId=" + nliId + ", years=" + years + '}';
    }
}
