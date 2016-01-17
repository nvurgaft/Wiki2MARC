package com.protowiki.beans;

import java.io.Serializable;

/**
 *
 * @author Nick
 */
public class FileDetails implements Serializable {
    
    private String name;
    private String extension;
    private Integer size;
    private String content;

    public FileDetails() {
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        int i = this.name.lastIndexOf('.');
        if (i>0) {
            this.extension = this.name.substring(i+1);
        }
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
        return "FileDetails{" + "name=" + name + "extension=" + extension + ", size=" + size + ", content=" + content + '}';
    }
}
