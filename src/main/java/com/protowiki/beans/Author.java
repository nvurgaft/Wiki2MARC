package com.protowiki.beans;

import java.io.Serializable;

/**
 *
 * @author Kobi
 */
public class Author implements Serializable{
    private String name;
    private Long viafId;
    
    public Author() {}
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
     public Long getViafId() {
        return this.viafId;
    }
    
    public void setViafId(Long viafId) {
        this.viafId = viafId;
    }

    @Override
    public String toString() {
        return "Author{" + "name=" + name + ", viafId=" + viafId + '}';
    }
}
