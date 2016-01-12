package com.protowiki.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Kobi
 */
public class Record implements Serializable {
    
    List<Controlfield> controlfields;
    List<Datafield> datafields;

    public Record() {
    }

    public List<Datafield> getDatafields() {
        return datafields;
    }

    public void setDatafields(List<Datafield> datafields) {
        this.datafields = datafields;
    }

    public List<Controlfield> getControlfields() {
        return controlfields;
    }

    public void setControlfields(List<Controlfield> controlfields) {
        this.controlfields = controlfields;
    }

    @Override
    public String toString() {
        return "Record: \n\tcontrolfields=" + controlfields + ", datafields=" + datafields + '}';
    }
}
