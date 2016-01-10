package com.protowiki.beans;

import java.io.Serializable;

/**
 *
 * @author Nick
 */
public class FileDetails implements Serializable {
    
    private String name;
    private Integer size;
    private String content;

    public FileDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FileDetails{" + "name=" + name + ", size=" + size + ", content=" + content + '}';
    }
}
