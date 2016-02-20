
package com.protowiki.beans;

import java.io.Serializable;

/**
 *
 * @author Nick
 */
public class ResponseMessage implements Serializable {
    
    private int status;
    private String data;

    public ResponseMessage() {
    }

    public int getStatus() {
        return status;
    }

    public ResponseMessage setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getData() {
        return data;
    }

    public ResponseMessage setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" + "status=" + status + ", data=" + data + '}';
    }
}
