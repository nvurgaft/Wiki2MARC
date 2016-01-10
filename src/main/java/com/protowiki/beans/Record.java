package com.protowiki.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Kobi
 */
public class Record implements Serializable {
    
    List<Datafield> datafields;

    public Record() {
    }

    public Record(List<Datafield> datafields) {
        this.datafields = datafields;
    }

    public List<Datafield> getDatafields() {
        return datafields;
    }

    public void setDatafields(List<Datafield> datafields) {
        this.datafields = datafields;
    }

    @Override
    public String toString() {
        return "Record{" + "datafields=" + datafields + '}';
    }
}
