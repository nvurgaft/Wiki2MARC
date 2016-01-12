package com.protowiki.beans;

import java.io.Serializable;

/**
 *
 * @author Nick
 */
public class Controlfield implements Serializable {
    
    String tag;
    String value;

    public Controlfield() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Controlfield: tag=" + tag + ", value=" + value;
    }
}
