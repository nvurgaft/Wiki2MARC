
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
        if (str.charAt(0) == '\"') {
            str = str.substring(1, str.length() - 1);
        }
        if (str.charAt(str.length() - 1) == '\"') {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }
}
