
package com.protowiki.utils;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Nick
 */
public class StringLib {
    
    
    public static String toUtf8(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes("UTF-8"));
    }
    
    public static String trimQuotationMarks(String str) {
        if (first(str) == '\"') {
            str = str.substring(1, str.length() - 1);
        }
        if (last(str) == '\"') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
    
    public static char first(String str) {
        return str.charAt(0);
    }
    
    public static char last(String str) {
        return str.charAt(str.length()-1);
    }
}
