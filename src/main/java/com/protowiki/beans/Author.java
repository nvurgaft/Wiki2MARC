package com.protowiki.beans;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Nick
 */
public class Author implements Serializable{
    private Map<String, String> names;
    private String viafId;
    private String years;
    
    public Author() {}
    
     public String getViafId() {
        return this.viafId;
    }
    
    public void setViafId(String viafId) {
        this.viafId = viafId;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String born) {
        this.years = born;
    }

    @Override
    public String toString() {
        return "Author: " + "names=" + names + ", viafId=" + viafId + ", years=" + years;
    }
}
