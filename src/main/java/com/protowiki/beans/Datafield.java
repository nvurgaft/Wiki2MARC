package com.protowiki.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Kobi
 */
public class Datafield implements Serializable{ 
    List<Subfield> subfields;
    List<String> ind;
    String tag;

    public Datafield() {
    }

    public List<Subfield> getSubfields() {
        return subfields;
    }

    public void setSubfields(List<Subfield> subfields) {
        this.subfields = subfields;
    }

    public List<String> getInd() {
        return ind;
    }

    public void setInd(List<String> ind) {
        this.ind = ind;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Datafield{" + "subfields=" + subfields + ", ind=" + ind + ", tag=" + tag + '}';
    }
}
