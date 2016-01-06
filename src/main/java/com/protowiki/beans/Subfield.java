package com.protowiki.beans;

import java.io.Serializable;

/**
 *
 * @author Kobi
 */
public class Subfield implements Serializable{
    private String code;
    private String value;

    public Subfield() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Subfield{" + "code=" + code + ", value=" + value + '}';
    }
}
