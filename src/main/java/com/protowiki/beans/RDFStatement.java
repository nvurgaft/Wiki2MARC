package com.protowiki.beans;

import java.util.Objects;

/**
 *
 * @author Nick
 */
public class RDFStatement implements Cloneable {

    private String subject;
    private String predicate;
    private String object;

    /**
     * Initialize a new RDFStatement object
     */
    public RDFStatement() {
        super();
    }

    /**
     * Initialize a new RDFStatement object
     *
     * @param subject Subject name
     * @param predicate Predicate name
     * @param object Object name
     */
    public RDFStatement(String subject, String predicate, String object) {
        super();
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    /**
     * Subject getter method
     *
     * @return Subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Subject setter method
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Predicate getter method
     *
     * @return
     */
    public String getPredicate() {
        return predicate;
    }

    /**
     * Predicate setter method
     *
     * @param predicate
     */
    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    /**
     * Object getter method
     *
     * @return
     */
    public String getObject() {
        return object;
    }

    /**
     * Object setter method
     *
     * @param object
     */
    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RDFStatement)) {
            return false;
        }
        RDFStatement stmt = (RDFStatement) obj;
        return (this.object.equals(stmt.object) && this.predicate.equals(stmt.predicate) && this.subject.equals(stmt.subject));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.subject);
        hash = 97 * hash + Objects.hashCode(this.predicate);
        hash = 97 * hash + Objects.hashCode(this.object);
        return hash;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * This method returns a human readable textual representation of the
     * statement
     *
     * @return Description string
     */
    public String description() {
        return "Subject: " + subject + ", Predicate: " + predicate + ", Object: " + object;
    }

    @Override
    public String toString() {
        return "<" + this.subject + "> <" + this.predicate + "> \"" + this.object + "\"";
    }
}
