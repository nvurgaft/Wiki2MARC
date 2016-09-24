/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.protowiki.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nick
 */
public class Validators {
    
    public static boolean isBlank(Collection c) {
        return c==null || c.isEmpty();
    }
    
    public static boolean isBlank(Map m) {
        return m==null || m.isEmpty();
    }
    
    public static boolean isBlank(String s) {
        return s==null || s.isEmpty();
    }
    
    public static <T> T last(List<T> list) {
        if (isBlank(list)) {
            return null;
        }
        return list.get(list.size()-1);
    }
}
