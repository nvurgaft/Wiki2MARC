package com.protowiki.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Nick
 */
public class Datafield implements Serializable{ 
    List<Subfield> subfields;
    String[] ind;
    String tag;

    public Datafield() {
    }

    public List<Subfield> getSubfields() {
        return subfields;
    }

    public void setSubfields(List<Subfield> subfields) {
        this.subfields = subfields;
    }

    public String[] getInd() {
        return ind;
    }

    public void setInd(String[] ind) {
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
        String inds = "";
        for (String s: ind) {
            inds = inds.concat(s + " ");
        }
        return "Datafield: \n\t\tsubfields=" + subfields + ", ind=" + inds + ", tag=" + tag;
    }
}
