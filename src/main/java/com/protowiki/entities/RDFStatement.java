package com.protowiki.entities;

/**
 *
 * @author Nick
 */
public class RDFStatement implements Cloneable {
    
    private String subject;
    private String predicate;
    private String object;
    
    public RDFStatement() {
        super();
    }
    
    public RDFStatement(String subject, String predicate, String object) {
        super();
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null || !(obj instanceof RDFStatement)) {
            return false;
        }
        RDFStatement stmt = (RDFStatement) obj;
        return (this.object.equals(stmt.object) && this.predicate.equals(stmt.predicate) && this.subject.equals(stmt.subject));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String description() {
        return "Subject: " + subject + ", Predicate: " + predicate + ", Object: " + object;
    }
    
        @Override
    public String toString() {
        return "<" + this.subject + "> <" + this.predicate + "> <" + this.object + ">";
    }
}
